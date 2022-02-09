package fr.umontpellier.iut.bang.views;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public abstract class StartView extends Stage implements Initializable {

    private ObservableList<String> playersNamesList;

    public StartView() {
        playersNamesList = FXCollections.observableArrayList();
    }

    public ObservableList<String> playersNamesListProperty() {
        return playersNamesList;
    }

    public List<String> getPlayersNamesList() {
        return playersNamesList;
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public abstract void setPlayersListSetListener(ListChangeListener<String> whenPlayersNamesListIsSet);

    /**
     * Définit l'action à exécuter lorsque le nombre de participants change
     */
    protected abstract void setNbPlayersChangeListener(ChangeListener<Integer> whenNbPlayersChanged);

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setAllPlayersNamesList() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNumberOfPlayers() ; i++) {
            String name = getPlayerNameByNumber(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            }
            else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            getPlayersNamesList().clear();
            getPlayersNamesList().addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected abstract int getNumberOfPlayers();

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected abstract String getPlayerNameByNumber(int playerNumber);

}
