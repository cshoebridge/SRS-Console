package com.obiwanwheeler;

import java.io.IOException;
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
        Deck deckToReview = DeckFileParser.DECK_FILE_PARSER_SINGLETON.deserializeDeck(this.deckFilePath);
        assert deckToReview != null;
        Deck filteredDeck = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getCardsToBeReviewedToday(deckToReview);
        totalNumberOfCardsToBeReviewed = filteredDeck.cards.size();
        splitDeck = DeckManipulator.DECK_MANIPULATOR_SINGLETON.splitDeck(filteredDeck);
    }

    public void doReview(){
        //check if there is any cards to review today
        if (totalNumberOfCardsToBeReviewed == 0){
            System.out.println("no cards to review today");
            return;
        }
        //do review
        while(!isFinished(updatedDeck)){
            //chooses a deck to show a card from
            Deck deckToPullFrom = chooseADeck();
            //then tries to remove it from queue if empty, moving onto another iteration if successful
            if (ableToRemoveDeckFromQueue(deckToPullFrom)){
                removeDeckFromQueue(deckToPullFrom);
                continue;
            }
            //get lowest interval cards in that deck
            List<Card> lowestIntervalCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getLowestIntervalCards(deckToPullFrom);
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

    private boolean ableToRemoveDeckFromQueue(Deck potentiallyEmptyDeck){
        //if all the cards have already been removed from it, it no longer needs to exist in that review session
        //unless it is the learning queue, as even if initially empty, cards may move into it:
        //it is the only queue with this behaviour
        return potentiallyEmptyDeck.cards.stream().noneMatch(c -> c.getState() == Card.CardState.LEARNING) &&
                potentiallyEmptyDeck.cards.isEmpty();
    }

    private void removeDeckFromQueue(Deck deckToRemove){
        splitDeck.remove(deckToRemove);
    }

    //TODO do this in FX
    private void outputCardSides(Card cardToOutput){
        System.out.println(cardToOutput.getFrontSide());
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        DeckFileParser.DECK_FILE_PARSER_SINGLETON.serializeDeck(deckFilePath, updatedDeck);
    }
}
