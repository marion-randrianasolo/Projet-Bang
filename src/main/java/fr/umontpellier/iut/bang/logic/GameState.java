package fr.umontpellier.iut.bang.logic;

public enum GameState {
    DISCARD("Discard down to %s card(s)"),
    SELECT_TARGET("Click on player's name to attack : %s"),
    SELECT_JAIL_TARGET("Click on player's name to put in jail : %s"),
    PICK_UP_ANY_CARD("Select any card from %s"),
    TARGET_HAS_MISSED("%s may play a Missed! card or pass"),
    TARGET_HAS_BANG("%s may play a Bang! card or pass"),
    DISCARD_ANY_CARD("Select any card from any other player%s"),
    CHOOSE_ACTION("Choose card to play%s"),
    DRAWNING("Draw card or select any card from any player"),
    ESCAPING_JAIL("Click on card to close%s"),
    PASSING_DYNAMITE("Click on card to close%s"),
    DISTRIBUTING("To %s : select a card"),
    SAVED_BY_PROTECTION("Click on card to close%s"),
    CHOOSING_DRAWN_CARD_TO_DISCARD("Click on card to discard%s"),
    SHOWING_DRAWN_CARDS("Click on card to close%s");

    private String label;
    private static Game currentGame;

    GameState(String label) {
        this.label = label;
    }

    public GameState getNext() {
        return values()[(ordinal()+1) % values().length];
    }

    @Override
    public String toString() {
        String additionalInformation;
        switch (this) {
            case DISCARD:
                additionalInformation = String.valueOf(currentGame.getCurrentPlayer().getHealthPoints());
                break;
            case SELECT_TARGET:
            case SELECT_JAIL_TARGET:
            case PICK_UP_ANY_CARD:
                additionalInformation = currentGame.getNamesOfPossibleTargets();
                break;
            case TARGET_HAS_MISSED:
            case TARGET_HAS_BANG:
            case DISTRIBUTING:
                additionalInformation = currentGame.getTargetPlayer().getName();
                break;
            default:
                additionalInformation = "";
        }
        return String.format(label, additionalInformation);
    }

    public static void setGame(Game game) {
        currentGame = game;
    }
}