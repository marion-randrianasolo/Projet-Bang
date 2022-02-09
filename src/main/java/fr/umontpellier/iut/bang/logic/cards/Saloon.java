package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public class Saloon extends OrangeCard {
    public Saloon(int value, CardSuit suit) {
        super("Saloon", value, suit);
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        player.incrementHealth(1);
        for (Player target: player.getOtherPlayers()) {
            target.incrementHealth(1);
        }
    }
}
