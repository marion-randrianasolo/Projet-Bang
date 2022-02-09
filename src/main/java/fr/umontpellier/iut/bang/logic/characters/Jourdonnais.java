package fr.umontpellier.iut.bang.logic.characters;

import fr.umontpellier.iut.bang.logic.Player;

public class Jourdonnais extends BangCharacter {
    public Jourdonnais() {
        super("Jourdonnais", 4);
    }

    @Override
    public void onStartGame(Player player) {
        player.incrementProtections(1);
    }
}
