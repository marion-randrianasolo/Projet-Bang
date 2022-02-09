package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public class Mustang extends BlueCard {
    public Mustang(int value, CardSuit suit) {
        super("Mustang", value, suit);
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        player.incrementDistanceIncrementToOthers(1);
    }

    @Override
    public void onRemoveFromPlay(Player player) {
        player.incrementDistanceIncrementToOthers(-1);
    }
}
