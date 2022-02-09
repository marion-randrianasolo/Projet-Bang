package fr.umontpellier.iut.bang.logic.characters;

import fr.umontpellier.iut.bang.logic.GameState;
import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.logic.cards.Card;
import fr.umontpellier.iut.bang.logic.cards.CardSuit;

public class BlackJack extends BangCharacter {
    public BlackJack() {
        super("Black Jack", 4);
    }

    @Override
    public void onStartTurn(Player player) {
        player.drawToHand();
        Card card = player.getGame().drawCard();
        player.getGame().setCurrentState(GameState.SHOWING_DRAWN_CARDS);
        player.getGame().showCard(card);
    }

    @Override
    public void playSpecificFirstStep(Player player, Card card) {
        player.getGame().getDrawnCards().remove(card);
        player.addToHand(card);
        if (card.getSuit() == CardSuit.HEART || card.getSuit() == CardSuit.DIAMOND) {
            player.drawToHand();
        }
        player.getGame().resetCurrentPlayer();
    }
}
