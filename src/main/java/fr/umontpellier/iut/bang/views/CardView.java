package fr.umontpellier.iut.bang.views;

import fr.umontpellier.iut.bang.ICard;
import fr.umontpellier.iut.bang.logic.cards.Card;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public abstract class CardView extends Pane /*Button*/ {

    private ICard card;
    private PlayerArea playerArea;

    /**
     * Image d'une carte masquée
     */
    static Image back = new Image("/src/main/resources/images/cards/back_1H.png");

    public CardView(ICard card, PlayerArea playerArea) {
        this.card = card;
        this.playerArea = playerArea;
    }

    public Card getCard() {
        return card.getCard();
    }

    public ICard getICard() {
        return card;
    }

    public PlayerArea getPlayerArea() {
        return playerArea;
    }

    public static Image getBack() {
        return back;
    }

    /**
     * Pour afficher une carte masquée
     */
    public abstract void setVisible();

    /**
     * Pour masquer une carte affichée
     */
    public abstract void setUnVisible();

    /**
     * Pour définir l'action à exécuter quand la carte est sélectionnée par l'utilisateur
     */
    protected abstract void setCardSelectionListener();
}
