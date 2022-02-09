package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public class Beer extends OrangeCard {
    public Beer(int value, CardSuit suit) {
        super("Beer", value, suit);
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        if (player.getGame().canUseBeer()) {
            player.incrementHealth(1);
        }
    }
}
