package fr.umontpellier.iut.bang.views;

import fr.umontpellier.iut.bang.IGame;
import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.logic.cards.Card;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class GameView extends Pane {

    private IGame game;

    public GameView(IGame game) {
        this.game = game;
    }

    public IGame getIGame() {
        return game;
    }

    public void setGame(IGame game) {
        this.game = game;
    }

    private VBox vueConfirmation;

    private Button oui;
    private Button non;
    private Stage dialog;

    /**
     * Pour définir le message qui indique à l'utilisateur la prochaine action qu'il peut faire
     * Varie en fonction de l'état du jeu (GameState)
     */
    protected abstract void bindNextActionMessage();

    /**
     * Pour définir l'action à exécuter quand l'utilisateur choisit de passer
     */
    protected abstract void setPassSelectedListener();

    /**
     * Pour définir l'action à exécuter quand le joueur qui a le tour change
     */
    protected void setCurrentPlayerChangesListener(ChangeListener<? super Player> whenCurrentPlayerChanges) {
        game.currentPlayerProperty().addListener(whenCurrentPlayerChanges);
    }

    /**
     * Pour définir l'action à exécuter lorsqu'un joueur meurt
     */
    protected void setRemoveDeadPlayerAreaListener(ListChangeListener<Player> removeAreaListener) {
        game.playersProperty().addListener(removeAreaListener);
    }

    /**
     * Pour définir l'action à exécuter lorsqu'une carte d'attaque vient d'être jouée
     */
    protected void setCurrentAttackChangesListener(ChangeListener<? super Card> whenCurrentAttackChanges) {
        game.currentAttackProperty().addListener(whenCurrentAttackChanges);
    }

    public void confirmation() {
      dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        vueConfirmation = new VBox();

        Image imagefond = new Image("images/orange.png");
        Background bg = new Background(new BackgroundImage(imagefond, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1500, 1000, true, true, false, true)));
        vueConfirmation.setBackground(bg);

        Label confirmation = new Label("Etes-vous sûr ?");
        confirmation.setFont(Font.font("Courier", FontWeight.BOLD, 30));
        vueConfirmation.getChildren().add(confirmation);

        HBox ouinon = new HBox();
        oui = new Button("Oui");
        non = new Button("Non");

        //region1
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        ouinon.getChildren().add(region1);
        //style boutons
        oui.setMinSize(130, 60);
        oui.setFont(Font.font("King", FontWeight.NORMAL, 25));
        oui.setStyle("-fx-background-color: rgba(255,255,255,0.95);");
        ouinon.getChildren().add(oui);

        non.setMinSize(130, 60);
        non.setFont(Font.font("King", FontWeight.NORMAL, 25));
        non.setStyle("-fx-background-color: rgba(255,255,255,0.95);");
        ouinon.getChildren().add(non);
        //region2
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);
        ouinon.getChildren().add(region2);
        //space
        ouinon.setSpacing(20);
        vueConfirmation.setSpacing(30);

        //ajout à la vue
        vueConfirmation.getChildren().add(ouinon);
        vueConfirmation.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(vueConfirmation, 500, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public VBox getVueConfirmation() {
        return this.vueConfirmation;
    }

    public Button getOui() {
        return this.oui;
    }

    public Button getNon() {
        return this.non;
    }

    public Stage getDialog() {
        return dialog;
    }

    protected void setPiocheChangesListener(ChangeListener<? super Boolean> whenDrawPileSelectedChange) {
        game.canDrawPileBeSelectedProperty().addListener(whenDrawPileSelectedChange);
    }

}
