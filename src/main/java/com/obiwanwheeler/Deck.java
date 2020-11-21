package com.obiwanwheeler;
import java.util.List;

public class Deck {

    public List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    //private String deckFilePath;
}
