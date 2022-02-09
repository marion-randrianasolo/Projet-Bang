package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public class Volcanic extends WeaponCard {
    public Volcanic(int value, CardSuit suit) {
        super("Volcanic", value, suit);
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
    public void onRemoveFromPlay(Player p) {
        p.incrementUnlimitedBangs(-1);
    }
}
