package fr.umontpellier.iut.bang.logic;

import fr.umontpellier.iut.bang.logic.cards.*;
import fr.umontpellier.iut.bang.logic.characters.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import static fr.umontpellier.iut.bang.logic.Role.*;
import static fr.umontpellier.iut.bang.logic.cards.CardSuit.*;

public class Game {

    /**
     * Joueur dont c'est le tour
     */
    private ObjectProperty<Player> currentPlayer = new SimpleObjectProperty<>();
    /**
     * Joueur cible lorsqu'une attaque est en cours
     */
    private ObjectProperty<Player> targetPlayer = new SimpleObjectProperty<>();
    /**
     * Carte qui a déclenché l'attaque, quand une attaque est en cours (Bang, Duel, ...)
     */
    private ObjectProperty<Card> currentAttack = new SimpleObjectProperty<>();
    /**
     * Etat courant du jeu
     */
    private ObjectProperty<GameState> currentState;
    /**
     * Liste des joueurs encore en vie dans la partie
     */
    private ObservableList<Player> players;
    /**
     * Liste des cartes qui ont été piochées
     */
    private ObservableList<Card> drawnCards;
    /**
     * Liste des joueurs qui ont gagné la partie
     * (cette liste est vide pendant que la partie est en cours et remplie lorsque la partie se termine)
     */
    private ObservableList<Player> winners;
    /**
     * Utile pour permettre à Jesse Jones de choisir où il va piocher pendant la première phase de son tour
     */
    BooleanProperty canDrawPileBeSelected;

    /**
     * Méthode qui permet de démarrer la partie
     */
    public void run() {
        setCurrentPlayer(sheriffPlayer);
        for (Player player : players) {
            for (int i = 0; i < player.getHealthPoints(); i++) {
                player.drawToHand();
            }
        }
        getCurrentPlayer().initTurn();
    }

    /**
     * Méthode à appeler quand l'user choisit de passer
     */
    public void onPass() {
        if (isAttackInProgress()) {
            if (isTargetSelected()) {
                Player attacker = getCurrentPlayer();
                Player passingPlayer = getTargetPlayer();
                if (isDuelInProgress() && initialDuelTarget == getCurrentPlayer()) {
                    attacker = getTargetPlayer();
                    passingPlayer = getCurrentPlayer();
                }
                passingPlayer.shuffleHand();
                getCurrentAttack().onPass(attacker, passingPlayer);
            }
        }
        else {
            if (getCurrentState() == GameState.CHOOSE_ACTION)
                discardOrMoveNext();
        }
    }

    /**
     * Méthode à appeler quand une carte a été sélectionnée
     */
    public void onCardSelection(Card selectedCard, Player targetPlayer) {
        if (isPlayable(selectedCard)) {
            if (!isAttackInProgress()) {
                switch (getCurrentState()) {
                    case DISCARD: {
                        discardSelectedCard(selectedCard);
                        break;
                    }
                    case CHOOSE_ACTION: {
                        if (selectedCard.expectsReaction()) {
                            setCurrentAttack(selectedCard);
                            getCurrentPlayer().playFromHand(selectedCard);
                        }
                        else {
                            setCurrentAttack(null);
                            getCurrentPlayer().playFromHand(selectedCard);
                            nextPossibleCards = getCurrentPlayer().getPossibleCards();
                            if (nextPossibleCards.isEmpty() && drawnCards.isEmpty())
                                moveToNextPlayer();
                        }
                        break;
                    }
                    case DRAWNING: {
                        handleCardDrawning(selectedCard);
                        setCanDrawPileBeSelected(false);
                        break;
                    }
                }
            } else {
                    getCurrentAttack().onReact(targetPlayer, selectedCard);
            }
        }
    }

    /**
     * Méthode à appeler quand la cible d'une attaque a été sélectionnée
     */
    public void onTargetSelection(Player selectedPlayer) {
        if (isPossibleTarget(selectedPlayer)) {
            clearPossibleTargets();
            setTargetPlayer(selectedPlayer);
            getCurrentAttack().onTargetSelection(getTargetPlayer());
        }
    }

    /**
     * Méthode à appeler lorsque l'utilisateur joueur a sélectionné une carte piochée
     */
    public void onDrawnCardSelection(Card selectedCard) {
        switch (getCurrentState()) {
            case ESCAPING_JAIL:
                onCloseWhenEscapingJail(selectedCard);
                break;
            case PASSING_DYNAMITE:
                onCloseWhenPassingDynamite(selectedCard);
                break;
            case DISTRIBUTING:
                onCardDistribution(selectedCard);
                break;
            case SAVED_BY_PROTECTION:
                onCloseWhenSavedByProtection(selectedCard);
                break;
            case CHOOSING_DRAWN_CARD_TO_DISCARD:
            case SHOWING_DRAWN_CARDS:
                onBangCharacterSpecificFirstStep(selectedCard);
                break;
        }
    }

    /**
     * Méthode à appeler pour terminer l'étape initiale de pioche
     * pour un personnage dont l'étape initiale de pioche n'est pas standard
     */
    public void onBangCharacterSpecificFirstStep(Card closingCard) {
        getCurrentPlayer().getBangCharacter().playSpecificFirstStep(getCurrentPlayer(), closingCard);
        resetCurrentPlayer();
    }

    /**
     * Méthode à appeler quand la carte dégainée permet de sortir de prison
     */
    public void onCloseWhenEscapingJail(Card closingCard) {
        discardDrawnCard(closingCard);
        getCurrentPlayer().initTurn();
    }

    /**
     * Méthode à appeler quand la carte dégainée a empêché la dynamite d'exploser
     * (la carte Dynamite passe alors automatiquement au joueur suivant)
     */
    public void onCloseWhenPassingDynamite(Card closingCard) {
        discardDrawnCard(closingCard);
        getCurrentPlayer().handleJail();
    }

    /**
     * Méthode à appeler quand un joueur cible d'une attaque
     * et qui possède le moyen de dégainer (Barrel, Jourdonnais, ...)
     * a dégainé une carte qui le sauve
     */
    public void onCloseWhenSavedByProtection(Card closingCard) {
        discardDrawnCard(closingCard);
        resetCurrentPlayer();
    }

    /**
     * Méthode à appeler après qu'un joueur ait choisi sa carte
     * dans le cas d'un GeneralStore
     */
    public void onCardDistribution(Card card){
        drawnCards.remove(card);
        getTargetPlayer().addToHand(card);
        setTargetPlayer(getNextPlayer(getTargetPlayer()));
        updateCurrentState();
        if (drawnCards.size() == 1) {
            onCardDistribution(drawnCards.get(0));
            resetCurrentPlayer();
        }
    }

    /**
     * Méthode à appeler pour afficher la pioche si le personnage est Jesse Jones
     */
    public boolean canDrawPileBeSelected(){
        return getCurrentState() == GameState.DRAWNING;
    }

    /**
     * Accès à la propriété
     */
    public Player getCurrentPlayer() {
        return currentPlayer.getValue();
    }

    /**
     * Accès à la propriété
     */
    public GameState getCurrentState() {
        return currentState.getValue();
    }

    /**
     * Accès à la propriété
     */
    public ObservableList<Player> getPlayers() {
        return players;
    }

    /**
     * Accès à la propriété
     */
    public ObjectProperty<Card> currentAttackProperty() {
        return currentAttack;
    }

    /**
     * Accès à la propriété
     */
    public ObjectProperty<GameState> currentStateProperty() {
        return currentState;
    }

    /**
     * Accès à la propriété
     */
    public ObservableList<Player> winnersProperty() {
        return winners;
    }

    /**
     * Accès à la propriété
     */
    public ObjectProperty<Player> currentPlayerProperty() {
        return currentPlayer;
    }

    /**
     * Accès à la propriété
     */
    public ObservableList<Player> playersProperty() {
        return players;
    }

    /**
     * Accès à la propriété
     */
    public ObservableList<Card> drawnCardsProperty() {
        return drawnCards;
    }

    /**
     * Accès à la propriété
     */
    public BooleanProperty canDrawPileBeSelectedProperty() {
        return canDrawPileBeSelected;
    }


    /**
     * Les lignes qui suivent ne devraient pas être utiles dans la partie IHM
     */

    private List<Card> nextPossibleCards = new ArrayList<>();
    private List<Player> possibleTargets = new ArrayList<>();
    private Deque<Card> discardPile;
    public Deque<Card> getDiscardPile() {
        return discardPile;
    }
    private Deque<Card> drawPile;
    private Player sheriffPlayer;
    private Player renegadePlayer;
    private List<Player> deputyPlayers = new ArrayList<>();
    private List<Player> outlawPlayers = new ArrayList<>();
    private Player initialDuelTarget;

    public Game(List<Player> players) {
        // Préparation des piles de cartes
        discardPile = new ArrayDeque<>();
        drawPile = new ArrayDeque<>();
        fillDrawPile();
        drawnCards = FXCollections.observableArrayList();
        winners = FXCollections.observableArrayList();
        this.players = FXCollections.observableArrayList(players);
        canDrawPileBeSelected = new SimpleBooleanProperty(false);
        for (Player player : players) {
            player.setGame(this);
            switch (player.getRole()) {
                case SHERIFF:
                    sheriffPlayer = player;
                    break;
                case RENEGADE:
                    renegadePlayer = player;
                    break;
                case OUTLAW:
                    outlawPlayers.add(player);
                    break;
                case DEPUTY:
                    deputyPlayers.add(player);
                    break;
            }
        }
        setInitialState();
    }

    public void setInitialState() {
        GameState.setGame(this);
        currentState = new SimpleObjectProperty<>(GameState.CHOOSE_ACTION);
        initialDuelTarget = getTargetPlayer();
        setTargetPlayer(null);
    }

    public static List<Player> makePlayers(String[] playerNames) {
        int nbOfPlayers = playerNames.length;
        List<BangCharacter> possibleCharacters = getBangCharacters();
        Collections.shuffle(possibleCharacters);
        List<Role> possibleRoles = new ArrayList<>(Arrays.asList(SHERIFF, RENEGADE, OUTLAW, OUTLAW, DEPUTY, OUTLAW, DEPUTY));
        List<Role> rolesInGame = possibleRoles.subList(0, nbOfPlayers);
        Collections.shuffle(rolesInGame);
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < nbOfPlayers; i++) {
            players.add(new Player(playerNames[i], possibleCharacters.get(i), rolesInGame.get(i)));
        }
        return players;
    }

    private static List<BangCharacter> getBangCharacters() {
        List<BangCharacter> possibleCharacters = new ArrayList<>();
        possibleCharacters.add(new BartCassidy());
        possibleCharacters.add(new BlackJack());
     //   possibleCharacters.add(new JesseJones());
        possibleCharacters.add(new Jourdonnais());
        possibleCharacters.add(new KitCarlson());
        possibleCharacters.add(new PaulRegret());
        possibleCharacters.add(new RoseDoolan());
        possibleCharacters.add(new SlabTheKiller());
        possibleCharacters.add(new SuzyLafayette());
        possibleCharacters.add(new VultureSam());
        possibleCharacters.add(new WillyTheKid());
//        possibleCharacters.add(new CalamityJanet());
//        possibleCharacters.add(new ElGringo());
//        possibleCharacters.add(new LuckyDuke());
//        possibleCharacters.add(new PedroRamirez());
//        possibleCharacters.add(new SidKetchum());
        return possibleCharacters;
    }

    private void fillDrawPile() {
        List<Card> cards = new ArrayList<>();
        for (int i = 2; i <= 9; i++)
            cards.add(new Bang(i, CLUB));
        for (int i = 2; i <= 14; i++)
            cards.add(new Bang(i, DIAMOND));
        cards.add(new Bang(12, HEART));
        cards.add(new Bang(13, HEART));
        cards.add(new Bang(14, HEART));
        cards.add(new Bang(14, SPADE));
        for (int i = 6; i <= 11; i++)
            cards.add(new Beer(i, HEART));
        cards.add(new CatBalou(9, DIAMOND));
        cards.add(new CatBalou(10, DIAMOND));
        cards.add(new CatBalou(11, DIAMOND));
        cards.add(new CatBalou(13, HEART));
        cards.add(new Duel(8, CLUB));
        cards.add(new Duel(11, SPADE));
        cards.add(new Duel(12, DIAMOND));
        cards.add(new Gatling(10, HEART));
        cards.add(new Indians(13, DIAMOND));
        cards.add(new Indians(14, DIAMOND));

        for (int i = 2; i <= 8; i++)
            cards.add(new Missed(i, SPADE));
        for (int i = 10; i <= 14; i++)
            cards.add(new Missed(i, CLUB));

        cards.add(new Panic(11, HEART));
        cards.add(new Panic(12, HEART));
        cards.add(new Panic(14, HEART));
        cards.add(new Panic(8, DIAMOND));
        cards.add(new Saloon(5, HEART));
        cards.add(new GeneralStore(9, CLUB));
        cards.add(new GeneralStore(12, SPADE));
        cards.add(new Stagecoach(9, SPADE));
        cards.add(new Stagecoach(9, SPADE));
        cards.add(new WellsFargo(3, HEART));
        cards.add(new Barrel(12, SPADE));
        cards.add(new Barrel(13, SPADE));
        cards.add(new Dynamite(2, HEART));
        cards.add(new Jail(4, HEART));
        cards.add(new Jail(10, SPADE));
        cards.add(new Jail(11, SPADE));
        cards.add(new Mustang(8, HEART));
        cards.add(new Mustang(9, HEART));
        cards.add(new Remington(13, CLUB));
        cards.add(new RevCarabine(14, CLUB));
        cards.add(new Schofield(11, CLUB));
        cards.add(new Schofield(12, CLUB));
        cards.add(new Schofield(13, SPADE));
        cards.add(new Scope(14, SPADE));
        cards.add(new Volcanic(10, CLUB));
        cards.add(new Volcanic(10, SPADE));
        cards.add(new Winchester(8, SPADE));
        Collections.shuffle(cards);
        drawPile = new ArrayDeque<>(cards);
    }

    public Card drawCard() {
        if (drawPile.isEmpty()) {
            List<Card> cards = new ArrayList<>(discardPile);
            discardPile.clear();
            Collections.shuffle(cards);
            drawPile = new ArrayDeque<>(cards);
        }
        return drawPile.pop();
    }

    public void addToDiscard(Card c) {
        discardPile.push(c);
    }

    public void addToDraw(Card c) {
        drawPile.push(c);
    }

    public boolean canUseBeer() {
        return players.size() >= 3;
    }

    public void removePlayer(Player p) {
        if (p == getCurrentPlayer()) {
            int index = players.indexOf(p);
            setCurrentPlayer(players.get((index + 1) % players.size()));
        }
        players.remove(p);

        updateGameFinished();
    }

    private void updateGameFinished() {
        if (sheriffPlayer.isDead()) {
            if (players.size() == 1 && players.get(0).getRole() == RENEGADE) {
                winners.add(renegadePlayer);
            } else {
                winners.addAll(outlawPlayers);
            }
        } else {
            boolean sheriffWon = true;
            for (Player p : players) {
                if (p.getRole() == RENEGADE || p.getRole() == OUTLAW) {
                    sheriffWon = false;
                    break;
                }
            }
            if (sheriffWon) {
                deputyPlayers.add(0, sheriffPlayer);
                winners.addAll(deputyPlayers);
            }
        }
    }

    public void moveToNextPlayer() {
        setCurrentPlayer(getNextPlayer(getCurrentPlayer()));
        if (!getCurrentPlayer().isShowingCardSavingFromDynamite())
            getCurrentPlayer().handleJail();
    }

    public void discardOrMoveNext() {
        if (getCurrentPlayer().shouldDiscard()) {
            nextPossibleCards = getCurrentPlayer().getHand();
            setCurrentState(GameState.DISCARD);
        } else {
            moveToNextPlayer();
        }
    }

    public void updateCurrentState() {
        GameState currState = getCurrentState();
        setCurrentState(currState.getNext());
        setCurrentState(currState);
    }

    public void resetCurrentPlayer() {
        possibleTargets.clear();
        setCurrentAttack(null);
        setTargetPlayer(null);
        nextPossibleCards = getCurrentPlayer().getPossibleCards();
        if(nextPossibleCards.isEmpty())
            moveToNextPlayer();
        else {
            setCurrentState(GameState.CHOOSE_ACTION);
        }
    }

    public void setCanDrawPileBeSelected(boolean value) {
        canDrawPileBeSelected.setValue(value);
    }

    public int getPlayerDistance(Player player1, Player player2) {
        int index1 = players.indexOf(player1);
        int index2 = players.indexOf(player2);
        int n = players.size();
        return Math.min((index2 - index1 + n) % n, (index1 - index2 + n) % n);
    }

    public void discardSelectedCard(Card selectedCard) {
        getCurrentPlayer().discardFromHand(selectedCard);
        nextPossibleCards = getCurrentPlayer().getHand();
        discardOrMoveNext();
    }

    public void switchDuelTarget() {
        if (getTargetPlayer().equals(getCurrentPlayer())) { //initialDuelTarget
            setTargetPlayer(initialDuelTarget);
        } else {
            setTargetPlayer(getCurrentPlayer());
        }
        updateCurrentState();
    }

    public boolean isPlayable(Card selectedCard) {
        return nextPossibleCards.contains(selectedCard);
    }

    public boolean isAttackInProgress() {
        return getCurrentAttack() != null;
    }

    public boolean isTargetSelected() {
        return targetPlayer.getValue() != null;
    }

    public boolean isPossibleTarget(Player player) {
        return possibleTargets.contains(player);
    }

    public boolean isDuelInProgress() {
        return getCurrentAttack().getName().equals("Duel");
    }

    public void clearPossibleTargets() {
        possibleTargets.clear();
    }

    public void setNextPossibleCards(List<Card> nextPossibleCards) {
        this.nextPossibleCards = nextPossibleCards;
    }

    public List<Card> getDrawnCards() {
        return drawnCards;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer.setValue(targetPlayer);
    }

    public Player getTargetPlayer() {
        return targetPlayer.getValue();
    }

    public Player getCurrentDuelAttacker() {
        if (getTargetPlayer().equals(initialDuelTarget))
            return getCurrentPlayer();
        return getTargetPlayer();
    }

    public void setPossibleTargets(List<Player> possibleTargets) {
        this.possibleTargets.clear();
        this.possibleTargets.addAll(possibleTargets);
    }

    public Card getCurrentAttack() {
        return currentAttack.getValue();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer.set(currentPlayer);
    }

    public Player getNextPlayer(Player startPlayer) {
        int index = players.indexOf(startPlayer);
        index += 1;
        index %= players.size();
        return players.get(index);
    }

    public void setCurrentState(GameState currentState) {
        this.currentState.setValue(currentState);
    }

    public void setCurrentAttack(Card currentAttack) {
        this.currentAttack.setValue(currentAttack);
    }

    public void setInitialDuelTarget(Player initialDuelTarget) {
        this.initialDuelTarget = initialDuelTarget;
        setTargetPlayer(initialDuelTarget);
    }

    public void handleCardDrawning(Card stolenCard) {
        for (Player otherPlayer : possibleTargets) {
            if (otherPlayer.getAllCards().contains(stolenCard)) {
                otherPlayer.losesCard(stolenCard);
                getCurrentPlayer().addToHand(stolenCard);
                break;
            }
        }
        getCurrentPlayer().drawToHand();
        resetCurrentPlayer();
    }

    public void discardDrawnCard(Card card) {
        drawnCards.remove(card);
        addToDiscard(card);
    }

    public void showCard(Card card) {
        drawnCards.add(card);
    }

    public void showCards(List<Card> listOfDrawnCards) {
        drawnCards.addAll(listOfDrawnCards);
    }

    public String getNamesOfPossibleTargets() {
        StringJoiner joiner = new StringJoiner(" or ");
        for (Player p : possibleTargets) {
            joiner.add(String.format("%s", p.getName()));
        }
        return joiner.toString();
    }

    public List<Card> getNextPossibleCards() {
        return nextPossibleCards;
    }
}
