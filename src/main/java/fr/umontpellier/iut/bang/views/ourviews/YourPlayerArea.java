package fr.umontpellier.iut.bang.views.ourviews;

import fr.umontpellier.iut.bang.ICard;
import fr.umontpellier.iut.bang.IPlayer;
import fr.umontpellier.iut.bang.logic.Game;
import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.logic.cards.*;
import fr.umontpellier.iut.bang.views.CardView;
import fr.umontpellier.iut.bang.views.GameView;
import fr.umontpellier.iut.bang.views.PlayerArea;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class YourPlayerArea extends PlayerArea {
    private ImageView imageJoueur = new ImageView("images/characters/nobody.png");
    private ImageView imageArme;
    private Label nomArme;
    private  HBox vueDeLaMain;
    private VBox vueJoueur;
    private  HBox vueEnJeu;
    private HBox vueJoueurEtJeu;
    private  VBox bullets;
    private Button nomJoueur;

    public YourPlayerArea(IPlayer player, GameView gameView) {
        super(player, gameView);
        vueDeLaMain = new HBox();
        vueJoueur= new VBox();
        vueEnJeu = new HBox();
        vueJoueurEtJeu = new HBox();
        imageArme= new ImageView("images/cards/colt45_1H.png");
        nomArme= new Label("Colt 45");
        nomJoueur = new Button(player.getName());

        //taille image arme
        imageArme.setFitHeight(81);
        imageArme.setFitWidth(51);

        //Personnage joueur
        lienPhoto();
        imageJoueur.setFitHeight(81);
        imageJoueur.setFitWidth(51);


        //point de vie au debut, balle en couleur
        bullets = new VBox();
        for(int i =0;i<player.getHealthPointsMax();i++){
            ImageView bullet = new ImageView("images/bullet.png");
            bullet.setFitHeight(10);
            bullet.setFitWidth(15);
            bullets.getChildren().add(bullet);
        }


        Pane joueur= new Pane();
        Pane arme = new Pane();
        arme.getChildren().addAll(imageArme,nomArme);
        setHealthPointsListener(whenHealtPointIsUpdated);
        joueur.getChildren().addAll(imageJoueur,bullets);
        setWeaponListener(whenWeaponChanges);


        vueJoueurEtJeu.getChildren().addAll(joueur,arme,vueEnJeu);
        vueJoueurEtJeu.setSpacing(20);
        vueJoueur.getChildren().addAll(nomJoueur,vueJoueurEtJeu, vueDeLaMain);
        setHandListener(whenHandIsUpdated);
        setInPlayListener(whenInPlayIsUpdated);
        getChildren().add(vueJoueur);
        this.setPadding(new Insets(10,10,10,10));

        nomJoueur.setOnAction(actionEvent -> YourPlayerArea.this.getGameView().getIGame().onTargetSelection(YourPlayerArea.this.getIPlayer()));

    }
    public void lienPhoto() {

        switch (this.getIPlayer().getBangCharacter().getName()) {
            case "Bart Cassidy":
                imageJoueur = new ImageView("images/characters/bartcassidy.png");
                break;
            case "Black Jack":
                imageJoueur = new ImageView("images/characters/blackjack.png");
                break;
            case "Calamity Janet":
                imageJoueur = new ImageView("images/characters/calamityjanet.png");

                break;
            case "El Gringo":
                imageJoueur = new ImageView("images/characters/elgringo.png");

                break;
            case "Jesse Jones":
                imageJoueur = new ImageView("images/characters/jessejones.png");

                break;
            case "Jourdonnais":
                imageJoueur = new ImageView("images/characters/jourdonnais.png");

                break;
            case "Kit Carlson":
                imageJoueur = new ImageView("images/characters/kitcarlson.png");

                break;
            case "Lucky Duke":
                imageJoueur = new ImageView("images/characters/luckyduke.png");

                break;
            case "Paul Regret":
                imageJoueur = new ImageView("images/characters/paulregret.png");

                break;
            case "Pedro Ramirez":
                imageJoueur = new ImageView("images/characters/pedroramirez.png");

                break;
            case "Rose Doolan":
                imageJoueur = new ImageView("images/characters/rosedoolan.png");

                break;
            case "Sid Ketchum":
                imageJoueur = new ImageView("images/characters/sidketchum.png");

                break;
            case "Slab The Killer":
                imageJoueur = new ImageView("images/characters/slabthekiller.png");

                break;
            case "Suzy Lafayette":
                imageJoueur = new ImageView("images/characters/suzylafayette.png");

                break;
            case "Vulture Sam":
                imageJoueur = new ImageView("images/characters/vulturesam.png");

                break;
            default:
                imageJoueur = new ImageView("images/characters/willythekid.png");

                break;
        }

    }

    private CardView findCardView(HBox container, Card card) {
        for (Node n : container.getChildren()) {
            CardView nodeCardView = (CardView) n;
            Card nodeCard = nodeCardView.getCard();
            if (nodeCard.equals(card))
                return nodeCardView;
        }
        return null;
    }

    private ListChangeListener<Card> whenHandIsUpdated = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends Card> change) {
            while(change.next()){
                if(change.wasAdded()){
                    for (Card c :change.getAddedSubList()){
                        vueDeLaMain.getChildren().add(new YourCardView(new ICard(c), YourPlayerArea.this));
                    }
                }
                if(change.wasRemoved()){
                    for(Card c : change.getRemoved()){
                        vueDeLaMain.getChildren().remove(findCardView(vueDeLaMain,c));
                    }
                }
            }
        }
    };
    private ListChangeListener<BlueCard> whenInPlayIsUpdated = new ListChangeListener<>() {
        @Override
        public void onChanged(Change<? extends BlueCard> change) {
            while(change.next()){
                if(change.wasAdded()){
                    for (BlueCard c :change.getAddedSubList()){
                        vueEnJeu.getChildren().add(new YourCardView(new ICard(c), YourPlayerArea.this));
                    }
                }
                if(change.wasRemoved()){
                    for(BlueCard c : change.getRemoved()){
                        vueEnJeu.getChildren().remove(findCardView(vueEnJeu,c));
                    }
                }
            }
        }
    };


    @Override
    public void highlightCurrentArea() {

         setStyle("-fx-background-color : rgba(0,247,255,0.42)");
    }

    @Override
    public void deHightlightCurrentArea() {
        setStyle("-fx-background-color : transparent");

    }

    //permet de changer la carte d'arme
    private ChangeListener<? super WeaponCard> whenWeaponChanges = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends WeaponCard> observableValue, WeaponCard weaponCard, WeaponCard t1) {
         if(t1==null){
             YourPlayerArea.this.imageArme.setImage(new Image("images/cards/colt45_1H.png"));
             YourPlayerArea.this.nomArme.setText("Colt45");
         }else{
             YourPlayerArea.this.imageArme.setImage(new Image(t1.getImageName()));
             YourPlayerArea.this.nomArme.setText(t1.getName());

         }
        }
    };

    //permet de mettre a jour les points de vie au niveau visuel, les balles grises et en couleur
    private ChangeListener<? super Number> whenHealtPointIsUpdated = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
            ImageView bullet = new ImageView("images/bullet.png");
            ImageView bulletGrey = new ImageView("images/bullet_grey.png");
            bullet.setFitHeight(10);
            bullet.setFitWidth(15);
            bulletGrey.setFitHeight(10);
            bulletGrey.setFitWidth(15);

                while (bullets.getChildren().size() != 0) {
                    bullets.getChildren().remove(0);
                }

                for (int i = 0; i < t1.intValue(); i++) {
                    bullet = new ImageView("images/bullet.png");
                    bullet.setFitHeight(10);
                    bullet.setFitWidth(15);
                    bullets.getChildren().add(bullet);
                }
                for (int i = YourPlayerArea.this.getIPlayer().getHealthPointsMax(); i > t1.intValue(); i--) {
                    bulletGrey = new ImageView("images/bullet_grey.png");
                    bulletGrey.setFitHeight(10);
                    bulletGrey.setFitWidth(15);
                    bullets.getChildren().add(bulletGrey);
                }


        }
    };





}
