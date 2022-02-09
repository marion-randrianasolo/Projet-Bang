package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public class Barrel extends BlueCard {
    public Barrel(int value, CardSuit suit) {
        super("Barrel", value, suit);
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        player.incrementProtections(1);
    }

    @Override
    public void onRemoveFromPlay(Player player) {
        player.incrementProtections(-1);
    }
}
