package fr.umontpellier.iut.bang.logic.cards;

import fr.umontpellier.iut.bang.logic.GameState;

public class Indians extends LoopingCard {
    public Indians(int value, CardSuit suit) {
        super("Indians!", value, suit);
    }

    public String getProtectionCardName() {
        return "Bang!";
    }

    public GameState getTargetState() {
        return GameState.TARGET_HAS_BANG;
    }

}
