package fr.umontpellier.iut.bang.views;

import fr.umontpellier.iut.bang.IGame;
import fr.umontpellier.iut.bang.logic.cards.Card;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Classe utilisée si il faut afficher les cartes piochées
 * (Barrel, Jail, Dynamite, GeneralStore, première étape d'un tour pour certains personnages...)
 */
public abstract class DrawnCardsView extends Pane {

    private IGame game;

    public DrawnCardsView(IGame game) {
        this.game = game;
    }

    protected IGame getIGame() {
        return game;
    }

    /**
     * Permet d'afficher (ou de ne plus afficher) les cartes piochées
     * Dans le package "logic", les cartes piochées sont toutes ajoutées en une fois à la liste drawnCards
     * Elles seront enlevées au fur et à mesure des sélections de l'utilisateur
     */
    protected void setDrawnCardsChangedListener(ListChangeListener<Card> drawnCardsListener) {
        game.drawnCardsProperty().addListener(drawnCardsListener);
    }

}
