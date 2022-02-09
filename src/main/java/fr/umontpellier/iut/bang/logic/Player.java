package fr.umontpellier.iut.bang.logic;

import fr.umontpellier.iut.bang.logic.cards.BlueCard;
import fr.umontpellier.iut.bang.logic.cards.Card;
import fr.umontpellier.iut.bang.logic.cards.Colt;
import fr.umontpellier.iut.bang.logic.cards.WeaponCard;
import fr.umontpellier.iut.bang.logic.characters.BangCharacter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static fr.umontpellier.iut.bang.logic.Role.SHERIFF;
import static fr.umontpellier.iut.bang.logic.cards.CardSuit.HEART;
import static fr.umontpellier.iut.bang.logic.cards.CardSuit.SPADE;

public class Player {

    /**
     * Cartes en main
     */
    private ObservableList<Card> hand;
    /**
     * Cartes bleues actuellement posées devant le joueur (hors arme)
     */
    private ObservableList<BlueCard> inPlay;
    /**
     * Arme posée devant le joueur
     */
    private ObjectProperty<WeaponCard> weapon;
    /**
     * Points de vie courants
     */
    private IntegerProperty healthPoints;

    public ObservableList<Card> handProperty() {
        return hand;
    }

    public ObjectProperty<WeaponCard> weaponProperty() {
        return weapon;
    }

    public ObservableList<BlueCard> inPlayProperty() {
        return inPlay;
    }

    public IntegerProperty healthPointsProperty() {
        return healthPoints;
    }

    public String getName() {
        return name;
    }

    public int getHealthPoints() {
        return healthPoints.getValue();
    }

    public Role getRole() {
        return role;
    }

    public BangCharacter getBangCharacter() {
        return bangCharacter;
    }

    public Game getGame() {
        return game;
    }

    public int getHealthPointsMax() {
        int hp = bangCharacter.getHealthPoints();
        if (role == Role.SHERIFF)
            return hp + 1;
        return hp;
    }

    public WeaponCard getWeapon() {
        return weapon.getValue();
    }















    /**
     * Les lignes qui suivent ne devraient pas être utiles dans la partie IHM
     */

    /**
     * Nom du joueur
     */
    private String name;
    /**
     * Rôle dans la partie (Shériff, adjoint, hors-la-loi ou renégat)
     */
    private Role role;
    /**
     * Personnage
     */
    private BangCharacter bangCharacter;
    /**
     * Partie à laquelle le joueur appartient
     */
    private Game game;
    /**
     * Modificateur de distance qui affecte la distance à laquelle les autres joueurs voient le joueur courant
     * (une valeur de n signifie que les autres joueurs le voient à +n)
     */
    private int distanceIncrementToOthers;
    /**
     * Modificateur de distance qui affecte la distance à laquelle le joueur courant voit les autres joueurs
     * (une valeur de n signifie qu'il voit les autres joueurs à -n)
     */
    private int distanceDecrementToOthers;
    /**
     * Indique si le joueur a déjà joué une carte Bang! à ce tour
     */
    private boolean hasPlayedBangThisTurn;
    /**
     * Nombre d'effets permettant de joueur un nombre illimité de cartes Bang! par tour
     * (Willy the Kid ou Volcanic)
     */
    private int unlimitedBangs;
    /**
     * Nombre d'effets permettant de se protéger contre un effet Bang!
     * (Jourdonnais ou Planque)
     */
    private int numberOfProtections;
    /**
     * Nombre de Missed joués en réponse à un Bang
     * (Slab le Flingueur)
     */
    private int numberOfMissedPlayed;

    public Player(String name, BangCharacter bangCharacter, Role role) {
        this.name = name;
        this.role = role;
        this.bangCharacter = bangCharacter;
        bangCharacter.onStartGame(this);
        numberOfMissedPlayed = 0;
        healthPoints = new SimpleIntegerProperty(getHealthPointsMax());
        hand = FXCollections.observableArrayList();
        inPlay = FXCollections.observableArrayList();
        weapon = new SimpleObjectProperty<>(new Colt());
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<BlueCard> getInPlay() {
        return inPlay;
    }

    public void setHasPlayedBangThisTurn(boolean value) {
        hasPlayedBangThisTurn = value;
    }

    public int getWeaponRange() {
        if (weapon.getValue() == null) {
            return 1;
        }
        return weapon.getValue().getRange();
    }

    public List<Player> getOtherPlayers() {
        List<Player> players = game.getPlayers();
        List<Player> otherPlayers = new ArrayList<>();
        int index = players.indexOf(this);
        otherPlayers.addAll(players.subList(index + 1, players.size()));
        otherPlayers.addAll(players.subList(0, index));
        return otherPlayers;
    }

    public List<Player> getPlayersInRange(int range) {
        return getOtherPlayers()
                .stream()
                .filter(p -> distanceTo(p) <= range)
                .collect(Collectors.toList());
    }

    public boolean isDead() {
        return getHealthPoints() <= 0;
    }

    public void setWeapon(WeaponCard weapon) {
        if (this.weapon.getValue().canBeDiscarded()) {
            this.weapon.getValue().onRemoveFromPlay(this);
            discard(this.weapon.getValue());
        }
        this.weapon.set(weapon);
    }

    public BlueCard getCardInPlay(String cardName) {
        for (BlueCard c : inPlay) {
            if (c.getName().equals(cardName)) {
                return c;
            }
        }
        return null;
    }

    public void incrementHealth(int n) {
        healthPoints.setValue(getHealthPoints()+n);
        if (getHealthPoints() > getHealthPointsMax()) {
            healthPoints.setValue(getHealthPointsMax());
        }
    }

    public void decrementHealth(int n, Player attacker) {
        healthPoints.setValue(getHealthPoints() - n);
        if (getHealthPoints() <= 0) {
            // le joueur va mourir, il a une dernière chance d'utiliser les Bières qu'il a en main
            if (game.canUseBeer()) {
                List<Card> beers = getHand()
                        .stream()
                        .filter(c -> c.getName().equals("Beer"))
                        .collect(Collectors.toList());
                if (getHealthPoints() + beers.size() > 0) {
                    while (getHealthPoints() <= 0) {
                        discardFromHand(beers.remove(0));
                        healthPoints.setValue(getHealthPoints() + 1);
                    }
                }
            }
        }
        if (getHealthPoints() <= 0) {
            // le joueur est mort
            healthPoints.setValue(0);
            die();
        } else {
            bangCharacter.onDecrementHealth(n, this, attacker);
        }

    }

    public void incrementDistanceIncrementToOthers(int i) {
        this.distanceIncrementToOthers += i;
    }

    public void incrementDistanceDecrementToOthers(int i) {
        this.distanceDecrementToOthers += i;
    }

    public int distanceTo(Player player) {
        int distance = game.getPlayerDistance(this, player);
        // prise en compte des modificateurs de chaque joueur
        distance -= distanceDecrementToOthers;
        distance += player.distanceIncrementToOthers;
        return distance;
    }

    public Card drawCard() {
        return game.drawCard();
    }

    public Card drawToHand() {
        Card card = drawCard();
        hand.add(card);
        return card;
    }

    public Card removeRandomCardFromHand() {
        if (getHand().size() > 0) {
            Random random = new Random();
            Card card = getHand().get(random.nextInt(getHand().size()));
            removeFromHand(card);
            return card;
        }
        return null;
    }

    public Card randomDraw() {
        return bangCharacter.randomDraw(this);
    }

    public boolean canPlayBang() {
        if (unlimitedBangs > 0) return true;
        return !hasPlayedBangThisTurn;
    }

    public void moveHandCardToLastPosition(Card lastCard) {
        hand.remove(hand.indexOf(lastCard));
        hand.add(lastCard);
    }

    public void initTurn() {
        hasPlayedBangThisTurn = false;
        bangCharacter.onStartTurn(this); // phase 1: piocher des cartes
    }

    public void handleJail() {
        BlueCard jailCard = getCardInPlay("Jail");
        if (jailCard != null) {
            // le joueur a une carte Prison
            Card randomCard = randomDraw();
            discardFromInPlay(jailCard);
            if (randomCard.getSuit() == HEART) {
                // le joueur sort de prison
                game.setCurrentState(GameState.ESCAPING_JAIL);
                game.showCard(randomCard);
            } else {
                discard(randomCard);
                game.moveToNextPlayer();
            }
        } else
            game.getCurrentPlayer().initTurn();
    }

    public boolean isShowingCardSavingFromDynamite() {
        BlueCard dynamiteCard = getCardInPlay("Dynamite");
        if (dynamiteCard != null) {
            // le joueur a une carte Dynamite
            Card randomCard = randomDraw();
            if (randomCard.getSuit() == SPADE
                    && randomCard.getValue() >= 2
                    && randomCard.getValue() <= 9) {
                // la dynamite explose
                decrementHealth(3, game.getCurrentPlayer());
                discardFromInPlay(dynamiteCard);
                discard(randomCard);
                return false;
            } else {
                removeFromInPlay(dynamiteCard);
                getOtherPlayers().get(0).addToInPlay(dynamiteCard);
                game.setCurrentState(GameState.PASSING_DYNAMITE);
                game.showCard(randomCard);
                return true;
            }
        }
        return false;
    }

    public boolean shouldDiscard() {
        return getHand().size() > getHealthPoints();
    }

    public void reactToBang() {
        if (!savedByProtections(numberOfProtections))
            tryMissed();
    }

    public void tryMissed() {
        int numberOfMissedRequired = getGame().getCurrentPlayer().getBangCharacter().getNumberOfMissedRequired();
        List<Card> possibleCards = getHandCardsByName("Missed!", numberOfMissedRequired);
        if (possibleCards.size() < numberOfMissedRequired) {
            decrementHealth(1, game.getCurrentPlayer());
            game.resetCurrentPlayer();
        }
        else {
            game.setNextPossibleCards(possibleCards);
            game.setCurrentState(GameState.TARGET_HAS_MISSED);
        }
    }

    public boolean savedByProtections(int numberOfProtections) {
        // planques
        if (numberOfProtections > 0) {
            Card randomCard = randomDraw();
            if (randomCard.getSuit() == HEART) {
                game.setCurrentState(GameState.SAVED_BY_PROTECTION);
                game.showCard(randomCard);
                return true;
            } else {
                discard(randomCard);
                return savedByProtections(numberOfProtections - 1);
            }
        } else
            return false;
    }

    public void reactToDuel() {
        if (canReactWithBang()) {
            game.setInitialDuelTarget(this);
            game.setTargetPlayer(this);
            game.setCurrentState(GameState.TARGET_HAS_BANG);
        }
        else
            game.resetCurrentPlayer();
    }

    public boolean canReactWithBang() {
        List<Card> possibleCards = getHandCardsByName("Bang!", 1);
        if (possibleCards.isEmpty()) {
            decrementHealth(1, game.getCurrentPlayer());
            return false;
        } else {
            game.setNextPossibleCards(possibleCards);
            return true;
        }
    }

    public void playsBangOnDuel() {
        getGame().switchDuelTarget();
        List<Card> possibleCards = getGame().getTargetPlayer().getHandCardsByName("Bang!", 1);
        if (!possibleCards.isEmpty()) {
            getGame().setNextPossibleCards(possibleCards);
        } else {
            getGame().getTargetPlayer().decrementHealth(1, getGame().getCurrentDuelAttacker());
            getGame().resetCurrentPlayer();
        }
    }

    public void isGivenJailCard() {
        addToInPlay((BlueCard) game.getCurrentAttack());
        game.resetCurrentPlayer();
    }

    private void die() {
        for (Player player : getOtherPlayers()) {
            player.getBangCharacter().onPlayerDeath(player, this);
        }
        // défausser toutes les cartes (main + en jeu)
/*        while (getHand().size() > 0) {
            discard(hand.remove(0));
        }
        while (inPlay.size() > 0) {
            discard(inPlay.remove(0));
        }*/
        looseAllCard();
        if (weapon.getValue().getName() != "Colt .45") {
            discard(weapon.getValue());
            setWeapon(new Colt());
        }
        game.removePlayer(this);
        if (getRole() == Role.DEPUTY)
            game.getCurrentPlayer().handlePenalty();
        if (getRole() == Role.OUTLAW)
            game.getCurrentPlayer().handleReward();
    }

    public void handlePenalty() {
        if (getRole() == SHERIFF) {
            looseAllCard();
        }
    }

    public void handleReward() {
        drawToHand();
        drawToHand();
        drawToHand();
    }

    private void looseAllCard() {
        while (getHand().size() > 0) {
            discard(hand.remove(0));
        }
        while (inPlay.size() > 0) {
            discard(inPlay.remove(0));
        }
    }

    public boolean hasCardInHand(String cardName) {
        return !(getHandCardsByName(cardName, 1).isEmpty());
    }

    public List<Card> getHandCardsByName(String cardName, int numberOfCardsToKeep) {
        List<Card> possibleCards = getHand()
                .stream()
                .filter(c -> c.getName().equals(cardName))
                .collect(Collectors.toList());
        while (possibleCards.size() > numberOfCardsToKeep)
            possibleCards.remove(possibleCards.size() - 1);
        for (Card c : possibleCards)
            moveHandCardToLastPosition(c);
        return possibleCards;
    }

    public List<Card> getAllCards() {
        List<Card> allCards = new ArrayList<>();
        if (weapon.getValue().getName() != "Colt .45")
            allCards.add(weapon.getValue());
        allCards.addAll(inPlay);
        allCards.addAll(getHand());
        return allCards;
    }

    public List<Card> getPossibleCards() {
        return getHand()
                .stream()
                .filter(c -> c.canPlayFromHand(this))
                .collect(Collectors.toList());
    }

    public void playFromHand(Card card) {
        if (removeFromHand(card)) {
            card.playedBy(this);
        }
    }

    public void addToInPlay(BlueCard card) {
        inPlay.add(card);
    }

    public void incrementUnlimitedBangs(int i) {
        unlimitedBangs += i;
    }

    public void incrementProtections(int i) {
        numberOfProtections += i;
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public void discard(Card card) {
        game.addToDiscard(card);
    }

    public boolean removeFromHand(Card card) {
        boolean didRemove = hand.remove(card);
        if (hand.isEmpty()) {
            bangCharacter.onEmptyHand(this);
        }
        return didRemove;
    }

    public boolean removeFromInPlay(BlueCard card) {
        boolean didRemove = inPlay.remove(card);
        if (inPlay.isEmpty()) {
            card.onRemoveFromPlay(this);
        }
        return didRemove;
    }

    public void discardFromHand(Card c) {
        if (removeFromHand(c)) {
            discard(c);
        }
    }

    public void discardFromInPlay(BlueCard c) {
        if (removeFromInPlay(c)) {
            discard(c);
        }
    }

    public void losesCard(Card card) {
        if (card == weapon.getValue()) {
            setWeapon(new Colt());
            return;
        }
        if (hand.contains(card)) {
            removeFromHand(card);
            return;
        }
        if (inPlay.contains(card)) {
            removeFromInPlay((BlueCard) card);
            return;
        }
    }

    public void incrementNumberOfMissedPlayed() {
        numberOfMissedPlayed++;
    }

    public void resetNumberOfMissedPlayed() {
        numberOfMissedPlayed = 0;
    }

    public boolean hasPlayedTheRequiredNumberOfMissed() {
        return numberOfMissedPlayed == getGame().getCurrentPlayer().getBangCharacter().getNumberOfMissedRequired();
    }

    public void drawCardsToDistribute() {
        getGame().setTargetPlayer(getGame().getCurrentPlayer());
        List<Card> drawnCards = new ArrayList<>();
        List<Player> allPlayers = getGame().getCurrentPlayer().getOtherPlayers();
        allPlayers.add(0, getGame().getCurrentPlayer());
        for (Player player : allPlayers) {
            drawnCards.add(drawCard());
        }
        getGame().setCurrentState(GameState.DISTRIBUTING);
        getGame().showCards(drawnCards);
    }

    public void shuffleHand() {
        List<Card> tempHand = new ArrayList<>(getHand());
        Collections.shuffle(tempHand);
        getHand().clear();
        getHand().addAll(tempHand);
    }




    /**
     * Les lignes qui suivent sont utiles pour mon debug
     */

    @Override
    public String toString() {
        return name;
    }
}