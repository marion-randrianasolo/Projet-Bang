package fr.umontpellier.iut.bang.views.ourviews;

import fr.umontpellier.iut.bang.ICard;
import fr.umontpellier.iut.bang.IGame;
import fr.umontpellier.iut.bang.IPlayer;
import fr.umontpellier.iut.bang.logic.cards.Card;
import fr.umontpellier.iut.bang.views.CardView;
import fr.umontpellier.iut.bang.views.PlayerArea;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class YourCardView extends CardView {

    public YourCardView(ICard card, PlayerArea playerArea) {
        super(card, playerArea);

        ImageView cartes = new ImageView(getICard().getImageName());

        cartes.setFitHeight(81);
        cartes.setFitWidth(51);

        this.getChildren().add(cartes);
        Label nomCarte = new Label(" "+ card.getName());
        this.getChildren().add(nomCarte);


        setCardSelectionListener();


    }

    @Override
    public void setVisible() {

    }

    @Override
    public void setUnVisible() {

    }

    @Override
    protected void setCardSelectionListener() {
        setOnMouseClicked(whenCardSelected);
    }

    EventHandler<MouseEvent>  whenCardSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            YourCardView selectedCardView = (YourCardView) mouseEvent.getSource();
            ICard selectedCard = selectedCardView.getICard();
            IPlayer owner = selectedCardView.getPlayerArea().getIPlayer();
            IGame  currentGame = selectedCardView.getPlayerArea().getGameView().getIGame();
            currentGame.onCardSelection(selectedCard,owner);

        }
    };
}
