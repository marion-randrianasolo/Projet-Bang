package fr.umontpellier.iut.bang.views.ourviews;

import fr.umontpellier.iut.bang.views.StartView;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.ResourceBundle;

public class YourStartView  extends StartView {
    @Override
    public void setPlayersListSetListener(ListChangeListener<String> whenPlayersNamesListIsSet) {

    }

    @Override
    protected void setNbPlayersChangeListener(ChangeListener<Integer> whenNbPlayersChanged) {

    }

    @Override
    protected int getNumberOfPlayers() {
        ObservableList<String> list = playersNamesListProperty();
        return list.size();
    }

    @Override
    protected String getPlayerNameByNumber(int playerNumber) {
        ObservableList<String> list = playersNamesListProperty();
        return list.get(playerNumber);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
