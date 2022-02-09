package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.GameState;
import fr.umontpellier.iut.bang.logic.Game;
import fr.umontpellier.iut.bang.logic.Player;

public abstract class LoopingCard extends OrangeCard {
    public LoopingCard(String name, int value, CardSuit suit) {
        super(name, value, suit);
    }

    @Override
    public boolean expectsReaction(){
        return true;
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        loopAttackFrom(player.getGame().getCurrentPlayer());
    }

    @Override
    public void onPass(Player attacker, Player target) {
        target.decrementHealth(1, attacker);;
        loopAttackFrom(target);
    }

    @Override
    public void onReact(Player reactingPlayer, Card reactingCard){
        reactingPlayer.discardFromHand(reactingCard);
        loopAttackFrom(reactingPlayer);
    }

    public void loopAttackFrom(Player player) {
        Game currentGame = player.getGame();
        Player targetPlayer = currentGame.getNextPlayer(player);
        while (!targetPlayer.equals(currentGame.getCurrentPlayer())
                && !targetPlayer.hasCardInHand(getProtectionCardName())) {
            targetPlayer.decrementHealth(1, currentGame.getCurrentPlayer());
            targetPlayer = currentGame.getNextPlayer(targetPlayer);
        }
        if (targetPlayer.equals(currentGame.getCurrentPlayer())) {
            currentGame.resetCurrentPlayer();
        } else {
            currentGame.setTargetPlayer(targetPlayer);
            currentGame.setNextPossibleCards(targetPlayer.getHandCardsByName(getProtectionCardName(), 1));
            currentGame.setCurrentState(getTargetState());
            currentGame.updateCurrentState();
        }
    }

    public abstract String getProtectionCardName();

    public abstract GameState getTargetState();

}
