package fr.umontpellier.iut.bang.views.ourviews;

import fr.umontpellier.iut.bang.ICard;
import fr.umontpellier.iut.bang.IGame;
import fr.umontpellier.iut.bang.IPlayer;
import fr.umontpellier.iut.bang.logic.cards.BlueCard;
import fr.umontpellier.iut.bang.logic.cards.Card;
import fr.umontpellier.iut.bang.views.CardView;
import fr.umontpellier.iut.bang.views.DrawnCardsView;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.nio.Buffer;

public class YourDrawnCardsView extends DrawnCardsView {
    private HBox cartes;
    private Button carte;

    public YourDrawnCardsView(IGame game) {
        super(game);
        cartes= new HBox();
        setDrawnCardsChangedListener(drawnCardsListener);
        this.getChildren().add(cartes);
    }

    private ListChangeListener<Card> drawnCardsListener = new ListChangeListener<Card>() {
        @Override
        public void onChanged(Change<? extends Card> change) {
            while(change.next()){
                if(change.wasAdded()){
                    for (Card c :change.getAddedSubList()){
                        carte = new Button(c.toString());
                        cartes.getChildren().add(carte);
                        carte.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                getIGame().onDrawnCardSelection(new ICard(findCard((Button)  mouseEvent.getSource())));
                                getIGame().drawnCardsProperty().remove(findCard((Button)  mouseEvent.getSource()));
                            }
                        });
                    }
                }
                if(change.wasRemoved()){
                    for(Card c : change.getRemoved()){
                        cartes.getChildren().remove(findButton(cartes,c));

                    }
                }
            }

        }

    };

    private Button findButton(HBox container, Card card) {
        for (Node n : container.getChildren()) {
            Button nodeCardView = (Button) n;
            if (card.toString().equals(nodeCardView.getText()))
                return nodeCardView;
        }
        return null;
    }

    private Card findCard(Button button) {
        for(Card c : getIGame().getGame().getDrawnCards()){
            if(c.toString().equals(button.getText())){
                return c;

            }
        }

        return null;
    }


}
