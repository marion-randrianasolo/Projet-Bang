package fr.umontpellier.iut.bang.views;

import fr.umontpellier.iut.bang.IPlayer;
import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.logic.cards.BlueCard;
import fr.umontpellier.iut.bang.logic.cards.Card;
import fr.umontpellier.iut.bang.logic.cards.WeaponCard;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public abstract class PlayerArea extends Pane {

    private GameView gameView;
    private IPlayer player;

    public PlayerArea(IPlayer player, GameView gameView) {
        this.gameView = gameView;
        this.player = player;
    }

    public GameView getGameView() {
        return gameView;
    }

    public IPlayer getIPlayer() {
        return player;
    }

    public Player getPlayer() {
        return player.getPlayer();
    }

    /**
     * Pour définir l'action à exécuter quand le joueur prend son tour
     */
    public abstract void highlightCurrentArea();

    /**
     * Pour définir l'action quand le tour passe au joueur suivant
     */
    public abstract void deHightlightCurrentArea();

    /**
     * Pour définir l'action quand la main du joueur change
     */
    protected void setHandListener(ListChangeListener<Card> whenHandIsUpdated) {
        player.handProperty().addListener(whenHandIsUpdated);
    }

    /**
     * Pour définir l'action quand les cartes du joueur en jeu changent
     */
    protected void setInPlayListener(ListChangeListener<BlueCard> whenInPlayIsUpdated) {
        player.inPlayProperty().addListener(whenInPlayIsUpdated);
    }

    /**
     * Pour définir l'action quand les points de vie du joueur en jeu changent
     */
    protected void setHealthPointsListener(ChangeListener<? super Number> whenInPlayIsUpdated) {
        player.healthPointsProperty().addListener(whenInPlayIsUpdated);
    }

    /**
     * Pour définir l'action quand l'arme du joueur change
     * Un joueur a toujours au moins une carte Colt .45
     */
    protected void setWeaponListener(ChangeListener<? super WeaponCard> whenWeaponChanges) {
        player.weaponProperty().addListener(whenWeaponChanges);
    }

}
