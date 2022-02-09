package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.GameState;
import fr.umontpellier.iut.bang.logic.Player;

import java.util.List;

public class Duel extends OrangeCard {
    public Duel(int value, CardSuit suit) {
        super("Duel", value, suit);
    }

    @Override
    public boolean expectsReaction(){
        return true;
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        List<Player> possibleTargets = player.getOtherPlayers();
        player.getGame().setPossibleTargets(possibleTargets);
        player.getGame().getNextPossibleCards().clear();
        player.getGame().setCurrentState(GameState.SELECT_TARGET);
    }

    @Override
    public void onPass(Player attacker, Player passingPlayer){
        passingPlayer.decrementHealth(1, attacker);;
        passingPlayer.getGame().resetCurrentPlayer();
    }

    @Override
    public void onReact(Player reactingPlayer, Card reactingCard){
        reactingPlayer.discardFromHand(reactingCard);
        reactingPlayer.playsBangOnDuel();
    }

    @Override
    public void onTargetSelection(Player targetPlayer){
        targetPlayer.getGame().clearPossibleTargets();
        targetPlayer.reactToDuel();
    }
}