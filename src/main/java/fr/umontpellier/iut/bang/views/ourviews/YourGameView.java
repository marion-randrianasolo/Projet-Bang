package fr.umontpellier.iut.bang.views.ourviews;

import fr.umontpellier.iut.bang.IGame;
import fr.umontpellier.iut.bang.IPlayer;
import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.views.GameView;
import fr.umontpellier.iut.bang.views.PlayerArea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;

public class YourGameView extends GameView {
    private Label texte;
    private static int clickExplication;
    private GridPane  vuePrincipale;
    private BorderPane vueJeu;
    private BorderPane vuePageAccueil;
    private Button pass;
    private Stage dialogPause;
    private Stage dialogEndGame;
    static TextArea a = new TextArea();
    public static String message;
    static TextField pseudo = new TextField();
    private Button defausse;
    private HBox piochePassDefausse;
    private Button pioche;



    public YourGameView(IGame game) {
        super(game);

        vuePageAccueil = new BorderPane();

        Image imagefond = new Image("images/accueil.png");
        Background bg = new Background(new BackgroundImage(imagefond, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
        this.setBackground(bg);


        // possibilite jeu
        VBox choixPartie = new VBox(50);
        MenuButton nbJoueur = new MenuButton("Nombre de joueur");
        MenuItem trois = new MenuItem("3");
        MenuItem quatre = new MenuItem("4");
        MenuItem cinq = new MenuItem("5");
        MenuItem six = new MenuItem("6");
        nbJoueur.getItems().addAll(trois, quatre, cinq, six);
        choixPartie.layout();


        MenuButton nbBot = new MenuButton("Nombre de bot");
        MenuItem botDeux = new MenuItem("2");
        MenuItem botTrois = new MenuItem("3");
        MenuItem botQuatre = new MenuItem("4");
        MenuItem botCinq = new MenuItem("5");
        nbBot.getItems().addAll(botDeux, botTrois, botQuatre, botCinq);

        pseudo.setText("Choisissez un pseudo");
        pseudo.setMaxWidth(160);

        // clear text dans TextField quand appui avec souris

        pseudo.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pseudo.clear();
            }
        });

        choixPartie.setAlignment(Pos.CENTER);
        choixPartie.getChildren().addAll(nbJoueur, nbBot, pseudo);
        choixPartie.setMinSize(400, 300);
        vuePageAccueil.setLeft(choixPartie);

        // introduction et photo
        VBox centre = new VBox(50);
        ImageView logo1 = new ImageView("images/logo.png");
        Label logo = new Label();
        logo.setGraphic(logo1);
        logo.setStyle("-fx-background-color: rgba(0,0,0,0.32);");
        logo1.setFitHeight(193);
        logo1.setFitWidth(712);
        Button commencer = new Button("Commencer une partie");
        EventHandler<ActionEvent> commencerPartie = actionEvent -> view();
        commencer.setOnAction(commencerPartie);
        centre.getChildren().addAll(logo, choixPartie, commencer);
        centre.setAlignment(Pos.CENTER);
        centre.setMinSize(500, 900);


        vuePageAccueil.setCenter(centre);
        centre.layout();

        VBox liens = new VBox();
        Label liensForum = new Label("Lien vers le forum");
        liensForum.setPadding(new Insets(0, 5, 5, 10));
        liens.setAlignment(Pos.BASELINE_CENTER);
        liens.setMinSize(300, 750);
        vuePageAccueil.setLeft(liens);


        //Explications
        VBox droite = new VBox();
        Button explication = new Button("Explications");

        explication.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (clickExplication == 0) {
                Label texteIntro = new Label("Bang ! vous transporte dans l'univers impitoyable du Far West. Incarnez un shérif, un hors-la-loi ou un renégat pour des affrontements sanglants ! Chaque rôle a ses propres objectifs et ses conditions de victoire : le shérif doit survivre, les hors-la-loi doivent tenter de l'éliminer, tandis que le renégat doit vaincre tout le monde. Au début de la partie, seul le Shérif est connu de tous. Les autres joueurs gardent leurs rôles bien dissimulés. Ceux-ci étant définis aléatoirement par un tirage au sort. Une fois les trois camps répartis, chaque joueur reçoit une carte personnage avec ses caractéristiques et ses points de vie, ce qui lui permettra d'affronter ses adversaires lors de la fusillade !");
                texteIntro.setWrapText(true);
                texteIntro.setPadding(new Insets(20, 20, 20, 20));
                texteIntro.setMaxWidth(300);
                texteIntro.setStyle("-fx-background-color: rgba(232,215,215,0.51);");
                droite.getChildren().add(texteIntro);
                clickExplication++;
            }
        });
        droite.getChildren().add(explication);
        droite.setPadding(new Insets(20, 10, 20, 0));
        droite.setAlignment(Pos.TOP_CENTER);
        droite.setMinSize(400, 750);
        droite.layout();
        vuePageAccueil.setRight(droite);
        this.getChildren().add(vuePageAccueil);
    }

    public void view() {
        Image fond = new Image("images/1061223.jpg");
        Background bg = new Background(new BackgroundImage(fond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
        this.setBackground(bg);
        piochePassDefausse = new HBox();
        defausse= new Button();
        vueJeu = new BorderPane();


        Image fondPlateau = new Image("images/plateau.png");
        Background wood = new Background(new BackgroundImage(fondPlateau, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));


        //agencement et pass
        this.getChildren().remove(vuePageAccueil);
        vuePrincipale = new GridPane();
        vuePrincipale.setBackground(wood);
        pass = new Button("Pass");
        piochePassDefausse.getChildren().add(pass);
        int i=0;
        int j=0;
        if(getIGame().getPlayers().size()%2==0){
            for(Player p : getIGame().getPlayers()) {

              if (j % 2 == 0) {
                vuePrincipale.add(new YourPlayerArea(new IPlayer(p), this), i, j);
                i += 2;
                if (i >= (getIGame().getPlayers().size())) {
                    j += 3;
                    i = i - 2;
                }
              } else {
                vuePrincipale.add(new YourPlayerArea(new IPlayer(p), this), i, j);
                i -= 2;
              }
            }
        }else{
            for(Player p : getIGame().getPlayers()) {

                if(j%2!=0) {
                    vuePrincipale.add(new YourPlayerArea(new IPlayer(p), this), i, j);
                    i -= 2;
                } else if (i>(getIGame().getPlayers().size()/2)){
                    j++;
                    i++;
                    vuePrincipale.add(new YourPlayerArea(new IPlayer(p), this), i, j);
                    j+=2;
                    i-=3;
                }
                else{
                    vuePrincipale.add(new YourPlayerArea(new IPlayer(p), this), i, j);
                    i += 2;
                    if (i >= (getIGame().getPlayers().size())) {
                        j += 3;
                        i = i - 2;
                    }
                }
            }
        }
        setPassSelectedListener();
        vuePrincipale.alignmentProperty().setValue(Pos.CENTER);
        vuePrincipale.setPadding(new Insets(10,10,10,0));
        vuePrincipale.setVgap(50);
        vuePrincipale.setHgap(20);
        vuePrincipale.getCellBounds(200,500);
        vuePrincipale.setMinWidth(500);
        vuePrincipale.setMaxSize(1200, 750);

        setCurrentPlayerChangesListener(whenCurrentPlayerChanges);
        setPassSelectedListener();

        //texte
        texte = new Label();
        bindNextActionMessage();
        VBox plateauDroit = new VBox();

        VBox infos = new VBox();
        infos.setStyle("-fx-background-color: white;");
        infos.setMinSize(texte.getWidth(),100);
        Label infoJeu = new Label("Test d'info du jeu \n");
        infoJeu.setMinWidth(texte.getWidth());
        infos.getChildren().add(texte);
        infos.setAlignment(Pos.BOTTOM_CENTER);


        //chat
        VBox chats = new VBox();
        Pane chat = new Pane();

        TextField t = new TextField("Ecrire un message");
        Button send = new Button("Envoyer");
        TextArea log = new TextArea();
        log.setVisible(false);
        log.setEditable(false);

        a.setEditable(false);
        a.setTranslateX(50);
        a.setTranslateY(25);
        a.setMaxWidth(330);
        a.setMaxHeight(450);
        a.setMinHeight(450);
        t.setMaxHeight(20);
        t.setMaxWidth(250);
        send.setMaxWidth(100);
        t.setLayoutY(445);
        send.setLayoutY(445);
        t.setLayoutX(55);
        t.setMinWidth(240);
        send.setLayoutX(300);

        a.appendText("Game started " + new Date() +"\n");
        a.appendText("Current players : " + getIGame().getPlayers() + "\n");


        // clear text dans TextField quand appui avec souris
        t.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                t.clear();
            }
        });

        // envoi message + clear TextField avec bouton envoyer
        send.setOnAction (e -> {
            message(t, log);
            t.clear();

        });
        // envoi message + clear TextField avec touche ENTRER
        t.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)){
                message(t, log);
                t.clear();
            }
        });

        chat.getChildren().addAll(a,t,send);
        chats.getChildren().add(chat);



        //boutons
        HBox bouton = new HBox();
        Button pause = new Button("Pause/paramètres");
        Button regles = new Button("Règles");
        pause.setStyle("-fx-background-color: #e0a382;");
        regles.setStyle("-fx-background-color: #e0a382;");
        bouton.setSpacing(15);
        bouton.getChildren().addAll(pause, regles);

        pause.setOnAction(actionEvent -> pause());

        regles.setOnAction(event -> {
            Application a = new Application() {
                @Override
                public void start(Stage stage) {
                }
            };
            a.getHostServices().showDocument("src/main/resources/Bang-regles.pdf");
        });


        plateauDroit.getChildren().addAll(infos, chats, bouton);
        plateauDroit.setSpacing(50);
        plateauDroit.setAlignment(Pos.CENTER);
        plateauDroit.setPadding(new Insets(0, 0, 0, 25));
        plateauDroit.setMaxHeight(950);


        vuePrincipale.add(piochePassDefausse,1,1);


        // plateau de jeu
        vueJeu.setMaxHeight(950);
        vueJeu.setPadding(new Insets(20, 40, 20, 20));
        vueJeu.setRight(plateauDroit);
        vueJeu.setCenter(vuePrincipale);


        //Forum
        VBox liens = new VBox();
        Hyperlink lienForum = new Hyperlink("Lien vers le forum");
        lienForum.setPadding(new Insets(10, 5, 5, 10));
        liens.getChildren().add(lienForum);

        lienForum.setOnAction(event -> {
            Application a = new Application() {
                @Override
                public void start(Stage stage) {
                }
            };
            a.getHostServices().showDocument("https://boardgamegeek.com/forum/4088/bang/rules");
        });

        HBox vide = new HBox();
        vide.setMinSize(30, 750);
        vueJeu.setLeft(vide);
        vueJeu.setBottom(liens);

        getChildren().add(vueJeu);
        YourDrawnCardsView test = new YourDrawnCardsView(this.getIGame());
        vuePrincipale.add(test,0,1);
        setRemoveDeadPlayerAreaListener(removeAreaListener);
        setGameEnd();
      // setPiocheChangesListener(whenDrawPileSelectedChange);
        getIGame().run();


    }

    private void message(TextField t, TextArea log) {
        message = t.getText();
        System.out.println(message);
        if (message != null && message.length() >= 1) {
            a.appendText(getPseudo().getText() + " : " + message + "\n");
            log.appendText(getPseudo().getText() + new Date() + " -> " + message + "\n");
        }
    }

    public static TextField getPseudo() {
        return pseudo;
    }

    public void pause() {
        dialogPause = new Stage();
        dialogPause.initModality(Modality.APPLICATION_MODAL);
        VBox vuePagePause = new VBox(20);
        Image imagefond = new Image("images/image0.jpg");
        Background bg = new Background(new BackgroundImage(imagefond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1500, 1000, true, true, false, true)));
        vuePagePause.setBackground(bg);

        Label pause = new Label("PAUSE");
        pause.setFont(Font.font("Courier", FontWeight.BOLD, 50));
        vuePagePause.getChildren().add(pause);

        // boutons
        Button reprendre = new Button("Reprendre");
        Button recommencer = new Button("Recommencer");
        Button parametres = new Button("Paramètres");
        Button quitter = new Button("Quitter");

        vuePagePause.getChildren().addAll(reprendre, recommencer, parametres, quitter);
        vuePagePause.setSpacing(50);
        vuePagePause.setAlignment(Pos.CENTER);

        // taille, fond et police des boutons
        reprendre.setMinSize(300, 125);
        reprendre.setFont(Font.font("Courier", FontWeight.NORMAL, 25));
        reprendre.setStyle("-fx-background-color: rgba(255,255,255,0.95);");
        recommencer.setMinSize(300, 125);
        recommencer.setFont(Font.font("Courier", FontWeight.NORMAL, 25));
        recommencer.setStyle("-fx-background-color: rgba(255,255,255,0.95);");
        parametres.setMinSize(300, 125);
        parametres.setFont(Font.font("Courier", FontWeight.NORMAL, 25));
        parametres.setStyle("-fx-background-color: rgba(255,255,255,0.95);");
        quitter.setMinSize(300, 125);
        quitter.setFont(Font.font("Courier", FontWeight.NORMAL, 25));
        quitter.setStyle("-fx-background-color: rgba(0,0,0,0.05);");

        //reprendre
        reprendre.setOnAction(event -> dialogPause.close());
        //parametres
        parametres.setOnAction(event -> parametres());
        //recommencer
        recommencer.setOnAction(event -> confirmationRestartFromPause());
        //quitter
        quitter.setOnAction(event -> confirmationQuit());

        Scene dialogScene = new Scene(vuePagePause, 1500, 950);
        dialogPause.setScene(dialogScene);
        dialogPause.show();
    }
    public void endMenu() {
        dialogEndGame = new Stage();
        dialogEndGame.initModality(Modality.APPLICATION_MODAL);
        VBox vuePagePause = new VBox(20);
        Image imagefond = new Image("images/image0.jpg");
        Background bg = new Background(new BackgroundImage(imagefond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1500, 1000, true, true, false, true)));
        vuePagePause.setBackground(bg);

        Label endlabel = new Label("Bravo !! / RIP ..." + "\n" + "Prêt à réessayer ?");
        endlabel.setFont(Font.font("Courier", FontWeight.BOLD, 50));
        vuePagePause.getChildren().add(endlabel);

        // boutons
        Button recommencer = new Button("Recommencer");
        Button modifierPartie = new Button("Modifier Partie");
        Button parametres = new Button("Paramètres");
        Button quitter = new Button("Quitter");

        vuePagePause.getChildren().addAll(recommencer, modifierPartie,parametres, quitter);
        vuePagePause.setSpacing(50);
        vuePagePause.setAlignment(Pos.CENTER);

        // taille, fond et police des boutons
        recommencer.setMinSize(300, 125);
        recommencer.setFont(Font.font("Courier", FontWeight.NORMAL, 25));
        recommencer.setStyle("-fx-background-color: rgba(255,255,255,0.95);");
        parametres.setMinSize(300, 125);
        parametres.setFont(Font.font("Courier", FontWeight.NORMAL, 25));
        parametres.setStyle("-fx-background-color: rgba(255,255,255,0.95);");
        quitter.setMinSize(300, 125);
        quitter.setFont(Font.font("Courier", FontWeight.NORMAL, 25));
        quitter.setStyle("-fx-background-color: rgba(0,0,0,0.05);");
        modifierPartie.setMinSize(300,125);
        modifierPartie.setFont(Font.font("Courier", FontWeight.NORMAL, 25));
        modifierPartie.setStyle("-fx-background-color: rgba(255,255,255,0.95);");

        //modfierPartie
        modifierPartie.setOnAction(event -> Platform.exit());
        //parametres
        parametres.setOnAction(event -> parametres());
        //recommencer
        recommencer.setOnAction(event -> confirmationRestartFromEndGame());
        //quitter
        quitter.setOnAction(event -> confirmationQuit());

        Scene dialogScene = new Scene(vuePagePause, 1500, 950);
        dialogEndGame.setScene(dialogScene);
        dialogEndGame.show();
    }

    public void parametres() {

        Stage dialogParametres = new Stage();
        dialogParametres.initModality(Modality.APPLICATION_MODAL);
        VBox vueParametres = new VBox(20);
        Image imagefond = new Image("images/orange.png");
        Background bg = new Background(new BackgroundImage(imagefond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1500, 1000, true, true, false, true)));
        vueParametres.setBackground(bg);

        Label volume = new Label("VOLUME");
        volume.setFont(Font.font("Courier", FontWeight.BOLD, 50));
        vueParametres.getChildren().add(volume);

        vueParametres.setAlignment(Pos.CENTER);
        Scene dialogScene = new Scene(vueParametres, 500, 200);
        dialogParametres.setScene(dialogScene);
        dialogParametres.show();
    }

    public void confirmationRestartFromPause() {
        super.confirmation();
        // confirmation oui
        getOui().setOnAction(event -> {
                    getDialog().close();
                    if (dialogPause.isShowing())
                        dialogPause.close();
                    vueJeu.getChildren().clear();
                    view();
                });
        // confirmation non
        getNon().setOnAction(event -> getDialog().close());
    }
    public void confirmationRestartFromEndGame(){
        super.confirmation();
        // confirmation oui
        getOui().setOnAction(event -> {
            getDialog().close();
            if (dialogEndGame.isShowing())
                dialogEndGame.close();
            vueJeu.getChildren().clear();
            view();
        });
        // confirmation non
        getNon().setOnAction(event -> getDialog().close());
    }
    public void confirmationQuit() {
        super.confirmation();
        // confirmation oui
        getOui().setOnAction(event -> Platform.exit());
        // confirmation non
        getNon().setOnAction(event -> getDialog().close());

    }

    ChangeListener<? super Player> whenCurrentPlayerChanges = new ChangeListener<Player>() {
        @Override
        public void changed(ObservableValue<? extends Player> observableValue, Player oldPlayer, Player newPlayer) {
            if (oldPlayer != null) {
                findPlayerArea(oldPlayer).deHightlightCurrentArea();
            }
            findPlayerArea(newPlayer).highlightCurrentArea();


        }
    };

    private PlayerArea findPlayerArea(Player player) {
        for (Node n : vuePrincipale.getChildren()) {
            PlayerArea nodePlayerArea = (PlayerArea) n;
            Player nodePlayer = nodePlayerArea.getPlayer();
            if (nodePlayer.equals(player))
                return nodePlayerArea;
        }
        return null;
    }


    @Override
    protected void bindNextActionMessage() {
        texte.textProperty().bind(Bindings.convert(getIGame().currentStateProperty()));

          }

    @Override
    protected void setPassSelectedListener() {
        pass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getIGame().onPass();
            }
        });

    }
    /*ChangeListener<? super Boolean> whenDrawPileSelectedChange = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if(t1==true){
                pioche = new Button();
                ImageView cartePioche = new ImageView("images/cards/back.png");
                cartePioche.setFitHeight(81);
                cartePioche.setFitWidth(51);
                pioche.setGraphic(cartePioche);
                pioche.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        getIGame().getCurrentPlayer().drawToHand();
                        getIGame().getCurrentPlayer().drawToHand();
                        getIGame().currentStateProperty().setValue(getIGame().currentStateProperty().getValue().getNext());
                    }
                });
                piochePassDefausse.getChildren().add(pioche);
            }else{
                piochePassDefausse.getChildren().remove(pioche);
            }
            }

    };*/
 ListChangeListener<Player> removeAreaListener = new ListChangeListener<Player>() {
        @Override
        public void onChanged(Change<? extends Player> change) {
            change.next();
            for(Player p :change.getRemoved()){
                vuePrincipale.getChildren().remove(findPlayerArea(p));
            }
        }

    };

    protected void setGameEnd() {
        getIGame().winnersProperty().addListener(gameEnd);
    }
    ListChangeListener<Player> gameEnd = new ListChangeListener<Player>() {
        @Override
        public void onChanged(Change<? extends Player> change) {
            change.next();
            endMenu();
        }

    };

}
