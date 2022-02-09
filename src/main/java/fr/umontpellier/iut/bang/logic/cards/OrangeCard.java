package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public abstract class OrangeCard extends Card {
    public OrangeCard(String name, int value, CardSuit suit) {
        super(name, value, suit);
    }

    @Override
    public boolean canPlayFromHand(Player player) {
        return true;
    }

    @Override
    public void playedBy(Player player) {
        player.discard(this);
    }

    @Override
    public void onPass(Player attacker, Player target) {
        super.onPass(attacker, target);
    }
}
