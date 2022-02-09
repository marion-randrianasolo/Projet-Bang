package fr.umontpellier.iut.bang.logic.characters;

/*public class PedroRamirez extends BangCharacter {
    public PedroRamirez() {
        super("Pedro Ramirez", 4);
    }

    @Override
    public void onStartTurn(Player player) {
        Game game = player.getGame();
        Card topCard = game.getTopOfDiscardPile();
        if (topCard != null) {
*//*             List<Card> choice = new ArrayList<>();
           choice.add(topCard);
            Card chosenCard = player.chooseCard(
                    String.format("Vous pouvez prendre %s dans la défausse (ou passer pour piocher deux cartes)", topCard.getName()),
                    choice,
                    false,
                    true);
            if (chosenCard == topCard) {
                game.removeFromDiscard(topCard);
                player.addToHand(topCard);
            } else {
                player.drawToHand();
            }*//*
        }
        player.drawToHand();
    }
}*/
