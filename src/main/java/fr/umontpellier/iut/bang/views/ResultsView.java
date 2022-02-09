package fr.umontpellier.iut.bang.views;

import fr.umontpellier.iut.bang.BangIHM;
import fr.umontpellier.iut.bang.logic.Player;
import javafx.collections.ListChangeListener;
import javafx.stage.Stage;

public abstract class ResultsView extends Stage {

    private BangIHM bangIHM;

    public ResultsView(BangIHM bangIHM) {
        this.bangIHM = bangIHM;
    }

    public BangIHM getBangIHM() {
        return bangIHM;
    }

    /**
     * Définit l'action à exécuter lorsque qu'un joueur (ou plusieurs) a gagné la partie
     */
    protected void setWinnersListener(ListChangeListener<Player> winnersListener) {
        bangIHM.getIGame().winnersProperty().addListener(winnersListener);
    }

    /**
     * Retourne au début (renseignement des noms des joueurs) pour redémarrer une partie
     */
    protected abstract void playAgain();

    /**
     * Termine la partie
     */
    protected abstract void stop();
}
