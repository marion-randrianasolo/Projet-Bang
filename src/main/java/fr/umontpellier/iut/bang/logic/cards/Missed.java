package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.Player;

public class Missed extends OrangeCard {
    public Missed(int value, CardSuit suit) {
        super("Missed!", value, suit);
    }

    @Override
    public boolean canPlayFromHand(Player player) {
        return false;
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        player.incrementNumberOfMissedPlayed();
        player.getGame().getNextPossibleCards().remove(this);//pour Slab le flingueur
    }

}
