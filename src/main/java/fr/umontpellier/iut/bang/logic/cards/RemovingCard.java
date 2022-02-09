package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.GameState;
import fr.umontpellier.iut.bang.logic.Game;
import fr.umontpellier.iut.bang.logic.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class RemovingCard  extends OrangeCard {

    private boolean expectsReaction = true;

    public RemovingCard(String name, int value, CardSuit suit) {
        super(name, value, suit);
    }

    @Override
    public boolean expectsReaction(){
        return expectsReaction;
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        Game currentGame = player.getGame();
        List<Player> possibleTargets = getTargets(player);
        List<Card> nextPossibleCards = new ArrayList<>();
        for (Player otherPlayer : possibleTargets){
            nextPossibleCards.addAll(otherPlayer.getAllCards());
        }
        if (nextPossibleCards.size() > 1) {
            currentGame.setNextPossibleCards(nextPossibleCards);
            currentGame.setPossibleTargets(possibleTargets);
            currentGame.setCurrentState(getNextState());
        } else {
            if (nextPossibleCards.size() == 1) {
                Card cardToTransfer = nextPossibleCards.get(0);
                for (Player otherPlayer : possibleTargets) {
                    if (otherPlayer.getAllCards().contains(cardToTransfer)) {
                        otherPlayer.losesCard(cardToTransfer);
                        transferCardTo(cardToTransfer, player);
                        break;
                    }
                }
            }
            expectsReaction = false;
            currentGame.resetCurrentPlayer();
        }
    }

    @Override
    public void onReact(Player targetPlayer, Card reactingCard) {
        targetPlayer.losesCard(reactingCard);
        transferCardTo(reactingCard, targetPlayer.getGame().getCurrentPlayer());
        targetPlayer.getGame().resetCurrentPlayer();
    }

    public abstract void transferCardTo(Card card, Player player);

    public abstract List<Player> getTargets(Player player);

    public abstract GameState getNextState();

}
