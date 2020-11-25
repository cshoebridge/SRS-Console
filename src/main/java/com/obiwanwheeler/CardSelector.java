package com.obiwanwheeler;

import java.util.List;
import java.util.Random;

public class CardSelector {

    List<Deck> splitDeck;
    Random random = new Random();

    public CardSelector(List<Deck> splitDeck){
        this.splitDeck = splitDeck;
    }

    public Deck chooseADeck(){

        Deck chosenDeck;

        //makes sure to choose a deck that actually exists
        do {
            chosenDeck = splitDeck.get(random.nextInt(splitDeck.size()));
        } while(chosenDeck.cards == null);

        return chosenDeck;
    }

    public void removeDeckFromQueue(Deck deckToRemove){
        splitDeck.remove(deckToRemove);
    }
}
