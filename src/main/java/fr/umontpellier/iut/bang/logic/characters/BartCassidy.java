package fr.umontpellier.iut.bang.logic.characters;

import fr.umontpellier.iut.bang.logic.Player;

public class BartCassidy extends BangCharacter {
    public BartCassidy() {
        super("Bart Cassidy", 4);
    }

    @Override
    public void onDecrementHealth(int n, Player player, Player attacker) {
        for (int i = 0; i < n; i++) {
            player.drawToHand();
        }
    }
}
