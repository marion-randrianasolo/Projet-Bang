package fr.umontpellier.iut.bang;

import fr.umontpellier.iut.bang.logic.Game;
import fr.umontpellier.iut.bang.logic.GameState;
import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.logic.cards.Card;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.util.Deque;
import java.util.List;

public class IGame {

    private Game game;

    public IGame(Game game) {
        this.game = game;
    }

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
        game.run();
    }

    /**
     * Méthode à appeler quand l'user choisit de passer
     */
    public void onPass() {
        game.onPass();
    }

    /**
     * Méthode à appeler quand une carte a été sélectionnée
     */
    public void onCardSelection(ICard selectedCard, IPlayer targetPlayer) {
        game.onCardSelection(selectedCard.getCard(), targetPlayer.getPlayer());
      }

    /**
     * Méthode à appeler quand la cible d'une attaque a été sélectionnée
     */
    public void onTargetSelection(IPlayer selectedPlayer) {
        game.onTargetSelection(selectedPlayer.getPlayer());
    }

    /**
     * Méthode à appeler pour terminer l'étape initiale de pioche
     * pour un personnage dont l'étape initiale de pioche n'est pas standard
     */
    public void onBangCharacterSpecificFirstStep(ICard closingCard) {
        game.onBangCharacterSpecificFirstStep(closingCard!=null? closingCard.getCard(): null);
    }

    /**
     * Méthode à appeler lorsque l'utilisateur joueur a sélectionné une carte piochée
     */
    public void onDrawnCardSelection(ICard card){
        game.onDrawnCardSelection(card.getCard());
    }

    /**
     * Méthode à appeler pour afficher la pioche si le personnage est Jesse Jones
     */
    public boolean canDrawPileBeSelected(){
        return game.canDrawPileBeSelected();
    }

    /**
     * Accès à la propriété
     */
    public Player getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    /**
     * Accès à la propriété
     */
    public ObservableList<Player> getPlayers() {
        return game.getPlayers();
    }

    /**
     * Accès à la propriété
     */
    public ObjectProperty<Card> currentAttackProperty() {
        return game.currentAttackProperty();
    }

    /**
     * Accès à la propriété
     */
    public ObjectProperty<GameState> currentStateProperty() {
        return game.currentStateProperty();
    }

    /**
     * Accès à la propriété
     */
    public ObservableList<Player> winnersProperty() {
        return game.winnersProperty();
    }

    /**
     * Accès à la propriété
     */
    public ObjectProperty<Player> currentPlayerProperty() {
        return game.currentPlayerProperty();
    }

    /**
     * Accès à la propriété
     */
    public ObservableList<Player> playersProperty() {
        return game.playersProperty();
    }

    /**
     * Accès à la propriété
     */
    public ObservableList<Card> drawnCardsProperty() {
        return game.drawnCardsProperty();
    }

    /**
     * Accès à la propriété
     */
    public BooleanProperty canDrawPileBeSelectedProperty() {
        return game.canDrawPileBeSelectedProperty();
    }

    public Game getGame() {
        return game;
    }
}
