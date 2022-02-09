package fr.umontpellier.iut.bang;

import fr.umontpellier.iut.bang.logic.Game;
import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.logic.Role;
import fr.umontpellier.iut.bang.logic.cards.BlueCard;
import fr.umontpellier.iut.bang.logic.cards.Card;
import fr.umontpellier.iut.bang.logic.cards.WeaponCard;
import fr.umontpellier.iut.bang.logic.characters.BangCharacter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

public class IPlayer {

    private Player player;

    public IPlayer(Player player) {
        this.player = player;
    }

    /**
     * Cartes en main
     */
    private ObservableList<Card> hand;
    /**
     * Cartes bleues actuellement posées devant le joueur (hors arme)
     */
    private ObservableList<BlueCard> inPlay;
    /**
     * Arme posée devant le joueur
     */
    private ObjectProperty<WeaponCard> weapon;
    /**
     * Points de vie courants
     */
    private IntegerProperty healthPoints;

    public ObservableList<Card> handProperty() {
        return player.handProperty();
    }

    public ObjectProperty<WeaponCard> weaponProperty() {
        return player.weaponProperty();
    }

    public ObservableList<BlueCard> inPlayProperty() {
        return player.inPlayProperty();
    }

    public IntegerProperty healthPointsProperty() {
        return player.healthPointsProperty();
    }

    public String getName() {
        return player.getName();
    }

    public int getHealthPoints() {
        return player.getHealthPoints();
    }

    public Role getRole() {
        return player.getRole();
    }

    public BangCharacter getBangCharacter() {
        return player.getBangCharacter();
    }

    public Game getGame() {
        return player.getGame();
    }

    public int getHealthPointsMax() {
        return player.getHealthPointsMax();
    }

    public WeaponCard getWeapon() {
        return player.getWeapon();
    }

    public Player getPlayer() {
        return player;
    }
}