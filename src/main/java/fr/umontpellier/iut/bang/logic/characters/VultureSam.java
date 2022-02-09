package fr.umontpellier.iut.bang.logic.characters;

import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.logic.cards.Colt;

public class VultureSam extends BangCharacter {
    public VultureSam() {
        super("Vulture Sam", 4);
    }

    @Override
    public void onPlayerDeath(Player player, Player deadPlayer) {
        while (deadPlayer.getHand().size() > 0) {
            player.addToHand(deadPlayer.getHand().remove(0));
        }
        while (deadPlayer.getInPlay().size() > 0) {
            player.addToHand(deadPlayer.getInPlay().remove(0));
        }
        if (deadPlayer.getWeapon().getName() != "Colt .45") {
            player.addToHand(deadPlayer.getWeapon());
            deadPlayer.setWeapon(new Colt());
        }
    }
}
