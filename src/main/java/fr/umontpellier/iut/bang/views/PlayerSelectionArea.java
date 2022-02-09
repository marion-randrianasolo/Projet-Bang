package fr.umontpellier.iut.bang.views;

import javafx.scene.layout.Pane;

public abstract class PlayerSelectionArea extends Pane {

    private PlayerArea playerArea;

    public PlayerSelectionArea(PlayerArea playerArea) {
        this.playerArea = playerArea;
    }

    protected PlayerArea getPlayerArea() {
        return playerArea;
    }

    /**
     * Pour définir l'action à exécuter quand le joueur prend son tour
     */
    public abstract void setVisible();

    /**
     * Pour définir l'action quand le tour passe au joueur suivant
     */
    public abstract void setUnVisible();

    /**
     * Pour définir l'action quand le joueur a été sélectionné
     * cela se produit généralement quand il est la cible d'une attaque
     */
    protected abstract void setPlayerSelectedListener();

}
