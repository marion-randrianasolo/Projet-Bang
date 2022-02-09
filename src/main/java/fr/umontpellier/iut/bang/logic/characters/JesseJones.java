package fr.umontpellier.iut.bang.logic.characters;

import fr.umontpellier.iut.bang.logic.GameState;
import fr.umontpellier.iut.bang.logic.Game;
import fr.umontpellier.iut.bang.logic.Player;
import fr.umontpellier.iut.bang.logic.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class JesseJones extends BangCharacter {
    public JesseJones() {
        super("Jesse Jones", 4);
    }

    @Override
    public void onStartTurn(Player player) {
        Game currentGame = player.getGame();
        List<Player> possibleTargets = player.getOtherPlayers();
        List<Card> nextPossibleCards = new ArrayList<>();
        for (Player otherPlayer : possibleTargets){
            nextPossibleCards.addAll(otherPlayer.getAllCards());
        }
        if (nextPossibleCards.size() > 0) {
            currentGame.setNextPossibleCards(nextPossibleCards);
            currentGame.setPossibleTargets(possibleTargets);
            currentGame.setCanDrawPileBeSelected(true);
            currentGame.setCurrentState(GameState.DRAWNING);
        } else {
            super.onStartTurn(player);
        }
    }

    @Override
    public void playSpecificFirstStep(Player player, Card card) {
        super.onStartTurn(player);
    }
}
