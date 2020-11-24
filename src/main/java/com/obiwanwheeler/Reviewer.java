package com.obiwanwheeler;

import java.util.*;

public class Reviewer {

    private final List<Deck> splitDeck;
    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private final String deckFilePath;
    private final int totalNumberOfCardsToBeReviewed;
    private final Deck updatedDeck = new Deck(new LinkedList<>());

    public Reviewer(String deckFilePath){

        this.deckFilePath = deckFilePath;

        //TODO get file from FX
        Deck deckToReview = DeckFileManager.DECK_FILE_MANAGER_SINGLETON.deserializeDeck(this.deckFilePath);
        assert deckToReview != null;
        Deck filteredDeck = DeckManager.DECK_MANAGER_SINGLETON.getCardsToBeReviewedToday(deckToReview);
        totalNumberOfCardsToBeReviewed = deckToReview.cards.size();

        splitDeck = DeckManager.DECK_MANAGER_SINGLETON.splitDeck(filteredDeck);
    }

    public void doReview(){
        //do review
        while(!isFinished(updatedDeck)){

            //chooses a deck to show a card from
            Deck deckToPullFrom = chooseADeck();
            //then tries to remove it from queue if empty.
            tryToRemoveDeckFromQueue(deckToPullFrom);
            //get lowest interval cards in that deck
            List<Card> lowestIntervalCards = DeckManager.DECK_MANAGER_SINGLETON.getLowestIntervalCards(deckToPullFrom);
            //chooses a card from those
            Card cardToReview = lowestIntervalCards.get(random.nextInt(deckToPullFrom.cards.size()));
            outputCardSides(cardToReview);
            //wait for the user to give input on whether they recalled the card correctly or not
            //and adjust intervals accordingly.
            waitForAndProcessInput(cardToReview, deckToPullFrom);
        }
        finishReview();
    }

    private Deck chooseADeck(){

        Deck chosenDeck;

        //makes sure to choose a deck that actually exists
        do {
            chosenDeck = splitDeck.get(random.nextInt(splitDeck.size()));
        } while(chosenDeck.cards == null);

        return chosenDeck;
    }

    private void tryToRemoveDeckFromQueue(Deck potentiallyEmptyDeck){

        //if all the cards have already been removed from it, it no longer needs to exist in that review session
        //unless it is the learning queue, as even if initially empty, cards may move into it:
        //it is the only queue with this behaviour
        if (potentiallyEmptyDeck.cards.isEmpty() || potentiallyEmptyDeck.cards.stream().anyMatch(c -> c.getState() == Card.CardState.LEARNING)){
            splitDeck.remove(potentiallyEmptyDeck);
        }
    }

    //TODO do this in FX
    private void outputCardSides(Card cardToOutput){
        System.out.println(cardToOutput.getFrontSide());
        scanner.nextLine();
        System.out.println(cardToOutput.getFrontSide() + "\n" + cardToOutput.getBackSide());
    }

    private void waitForAndProcessInput(Card reviewedCard, Deck deckReviewedCardComesFrom){

        boolean markedGood = scanner.next().equals("y");

        if (!markedGood && reviewedCard.getState() == Card.CardState.LEARNT){
            deckReviewedCardComesFrom.cards.remove(reviewedCard);
            splitDeck.get(Card.CardState.LEARNING.ordinal()).cards.add(reviewedCard);
        }
        else{

            boolean cardIsFinishedForSession = IntervalHandler.INTERVAL_HANDLER_SINGLETON.updateInterval(reviewedCard, markedGood);

            if (cardIsFinishedForSession){
                deckReviewedCardComesFrom.cards.remove(reviewedCard);
                updatedDeck.cards.add(reviewedCard);
            }
        }
    }

    private boolean isFinished(Deck updatedDeck){
        return updatedDeck.cards.size() == totalNumberOfCardsToBeReviewed;
    }

    private void finishReview(){
        DeckFileManager.DECK_FILE_MANAGER_SINGLETON.serializeDeck(deckFilePath, updatedDeck);
    }
}
