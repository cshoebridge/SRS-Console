package com.obiwanwheeler;

import java.io.IOException;
import java.util.*;

public class Reviewer {

    private final List<Deck> splitDeck;
    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private final String deckFilePath;
    private final int totalCardsToBeReviewed;

    public Reviewer(String deckFilePath){

        this.deckFilePath = deckFilePath;

        //temporary TODO in FX get file from form
        Deck deckToReview = DeckFileManager.deserializeDeck(this.deckFilePath);
        assert deckToReview != null;
        totalCardsToBeReviewed = deckToReview.cards.size();

        splitDeck = DeckManager.splitDeck(deckToReview);
    }

    public void doReview(){

        //Do review
        Deck updatedDeck = new Deck(new LinkedList<>());

        while(!isFinished(updatedDeck)){

            //chooses a deck to show a card from
            Deck deckToPullFrom;
            do {
                deckToPullFrom = splitDeck.get(random.nextInt(splitDeck.size()));
            } while(deckToPullFrom.cards == null);


            if (deckToPullFrom.cards.isEmpty()){
                splitDeck.remove(deckToPullFrom);
                continue;
            }

            //get lowest interval cards in that deck
            List<Card> lowestIntervalCards = DeckManager.getLowestIntervalCards(deckToPullFrom);

            //chooses a card among those
            Card cardToReview = lowestIntervalCards.get(random.nextInt(deckToPullFrom.cards.size()));
            System.out.println(cardToReview.getFrontSide());

            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(cardToReview.getFrontSide() + "\n" + cardToReview.getBackSide());
            boolean markedGood = scanner.next().equals("y");

            boolean shouldRemove = IntervalHandler.updateInterval(cardToReview, markedGood);
            if (shouldRemove){
                deckToPullFrom.cards.remove(cardToReview);
                updatedDeck.cards.add(cardToReview);
            }
        }

        DeckFileManager.serializeDeck(deckFilePath, updatedDeck);
    }

    private boolean isFinished(Deck updatedDeck){
        return updatedDeck.cards.size() == totalCardsToBeReviewed;
    }
}
