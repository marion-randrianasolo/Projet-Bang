package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public class Scope extends BlueCard {
    public Scope(int value, CardSuit suit) {
        super("Scope", value, suit);
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        player.incrementDistanceDecrementToOthers(1);
    }

    @Override
    public void onRemoveFromPlay(Player player) {
        player.incrementDistanceDecrementToOthers(-1);
    }
}
