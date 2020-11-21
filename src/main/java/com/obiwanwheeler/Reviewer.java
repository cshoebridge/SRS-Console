package com.obiwanwheeler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reviewer {

    private List<Card> newCards, learningCards, learntCards;

    public Reviewer(){

        try {
            //temporary TODO in FX get file from form
            Deck deckToReview = DeckFileReader.deserializeDeck("src/main/resources/com/obiwanwheeler/decks/testDeck.json");

            List<List<Card>> splitDeck = DeckManager.splitDeck(deckToReview);
            newCards = splitDeck.get(0);
            learningCards = splitDeck.get(1);
            learntCards = splitDeck.get(2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doReview(){
        //Do review
        System.out.println("New");
        newCards.forEach(c -> System.out.println(c.getFrontSide()));
        System.out.println("Learning");
        learningCards.forEach(c -> System.out.println(c.getFrontSide()));
        System.out.println("Learnt");
        learntCards.forEach(c -> System.out.println(c.getFrontSide()));
    }
}
