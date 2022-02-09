package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

import static fr.umontpellier.iut.bang.logic.cards.CardSuit.HEART;

public class Colt extends WeaponCard {
    public Colt() {
        super("Colt .45", 1, HEART);
    }

    @Override
    public int getRange() {
        return 1;
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        player.incrementUnlimitedBangs(1);
    }

    @Override
    public boolean canBeDiscarded() {
        return false;
    }
}
