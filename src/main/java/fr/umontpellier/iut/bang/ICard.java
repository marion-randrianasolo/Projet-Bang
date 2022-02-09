package fr.umontpellier.iut.bang;

import fr.umontpellier.iut.bang.logic.cards.Card;

public class ICard {

    private Card card;

    public ICard(Card card) {
        this.card = card;
    }

    public String getName() {
        return card.getName();
    }

    public String getImageName() {
        return card.getImageName();
    }

    public Card getCard() {
        return card;
    }
}
