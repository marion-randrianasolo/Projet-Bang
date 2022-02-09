package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.GameState;
import fr.umontpellier.iut.bang.logic.Player;

import java.util.List;

public class Bang extends OrangeCard {
    public Bang(int value, CardSuit suit) {
        super("Bang!", value, suit);
    }

    @Override
    public boolean canPlayFromHand(Player player) {
        return player.canPlayBang() && !player.getPlayersInRange(player.getWeaponRange()).isEmpty();
    }

    @Override
    public void playedBy(Player player) {
        super.playedBy(player);
        player.setHasPlayedBangThisTurn(true);
        List<Player> possibleTargets = player.getPlayersInRange(player.getWeaponRange());
        player.getGame().setPossibleTargets(possibleTargets);
        player.getGame().getNextPossibleCards().clear();
        player.getGame().setCurrentState(GameState.SELECT_TARGET);
    }

    @Override
    public boolean expectsReaction(){
        return true;
    }

    @Override
    public void onPass(Player attacker, Player target){
        target.decrementHealth(1, attacker);
        attacker.getGame().resetCurrentPlayer();
    }

    @Override
    public void onReact(Player targetPlayer, Card reactingCard){
        targetPlayer.playFromHand(reactingCard);
        if (targetPlayer.hasPlayedTheRequiredNumberOfMissed()) {
            targetPlayer.resetNumberOfMissedPlayed();
            targetPlayer.getGame().resetCurrentPlayer();
        } else {
            onReact(targetPlayer, targetPlayer.getGame().getNextPossibleCards().get(0));
        }
    }

    @Override
    public void onTargetSelection(Player targetPlayer){
        targetPlayer.reactToBang();
    }

}