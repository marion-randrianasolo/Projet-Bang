package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public class Stagecoach extends OrangeCard {
    public Stagecoach(int value, CardSuit suit) {
        super("Stagecoach", value, suit);
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        player.drawToHand();
        player.drawToHand();
    }
}
