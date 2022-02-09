package fr.umontpellier.iut.bang.logic.characters;

import fr.umontpellier.iut.bang.logic.Player;

public class SuzyLafayette extends BangCharacter {
    public SuzyLafayette() {
        super("Suzy Lafayette", 4);
    }

    @Override
    public void onEmptyHand(Player player) {
        player.drawToHand();
    }
}
