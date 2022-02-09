package fr.umontpellier.iut.bang;

import fr.umontpellier.iut.bang.logic.Game;
import fr.umontpellier.iut.bang.views.GameView;
import fr.umontpellier.iut.bang.views.ResultsView;
import fr.umontpellier.iut.bang.views.StartView;
import fr.umontpellier.iut.bang.views.ourviews.YourGameView;
import fr.umontpellier.iut.bang.views.ourviews.YourResultsView;
import fr.umontpellier.iut.bang.views.ourviews.YourStartView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BangIHM extends Application {

    private GameView gameView;
    private StartView startView;
    private ResultsView resultsView;
    private Stage primaryStage;
    private IGame game;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Bang");
        //initStartView();
        //startView.setPlayersListSetListener(whenPlayersNamesListIsSet);
        //initPlayersNames();
        startGame();
    }

    public void startGame() {
       // List<String> playerNames = startView.getPlayersNamesList();
        List<String> playerNames = new ArrayList<>();

        playerNames.add("Paul");
        playerNames.add("Ringo");
        playerNames.add("George");

        game = new IGame(new Game(Game.makePlayers(playerNames.toArray(new String[playerNames.size()]))));
        initGameView();
        initResultView();

        Scene scene = new Scene(gameView);
        primaryStage.setHeight(950);
        primaryStage.setWidth(1500);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            this.onStopGame();
            event.consume();
        });
        primaryStage.show();
    }

    /**
     * Pour instancier la vue de renseignement des noms des joueurs
     */
    private void initStartView() {
        startView = new YourStartView();
    }

    /**
     * Pour instancier la vue principale du jeu
     */
    private void initGameView() {
        gameView = new YourGameView(game);
    }

    /**
     * Pour instancier la vue de fin de partie
     */
    private void initResultView() {
        resultsView = new YourResultsView(this);
    }

    private final ListChangeListener<String> whenPlayersNamesListIsSet = change -> {
        if (!startView.getPlayersNamesList().isEmpty())
            startGame();
    };

    public IGame getIGame() {
        return game;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void initPlayersNames() {
        startView.show();
    }

    public void onStopGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you really want to stop playing ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }
}
