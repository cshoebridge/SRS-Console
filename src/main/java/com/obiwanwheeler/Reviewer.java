package com.obiwanwheeler;

import java.io.IOException;
import java.util.*;

public class Reviewer {

    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);

    private final String deckFilePath;

    private final int totalNumberOfCardsToBeReviewed;
    private final List<Card> unchangedCards;
    private final Deck updatedDeck = new Deck(new LinkedList<>());

    private final List<Deck> splitDeck;

    public Reviewer(String deckFilePath){

        this.deckFilePath = deckFilePath;

        //TODO get file from FX
        Deck deckToReview = DeckFileParser.DECK_FILE_PARSER_SINGLETON.deserializeDeck(this.deckFilePath);
        assert deckToReview != null;

        unchangedCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getCardsNotBeingReviewedToday(deckToReview).cards;

        Deck filteredDeck = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getCardsToBeReviewedToday(deckToReview);
        totalNumberOfCardsToBeReviewed = filteredDeck.cards.size();
        splitDeck = DeckManipulator.DECK_MANIPULATOR_SINGLETON.splitDeck(filteredDeck);
    }

    public void doReview(){
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

        if (!markedGood){
            processCardMarkedBad(reviewedCard, deckReviewedCardComesFrom);
        }
        else{
            processCardMarkedGood(reviewedCard, deckReviewedCardComesFrom);
        }
    }

    private void processCardMarkedBad(Card markedCard, Deck deckMarkedCardComesFrom){
        if (markedCard.getState() == Card.CardState.LEARNT){
            IntervalHandler.INTERVAL_HANDLER_SINGLETON.relearnCard(markedCard);
            moveCardToDifferentDeck(markedCard, deckMarkedCardComesFrom, splitDeck.get(Card.CardState.LEARNING.ordinal()));
        }
        else {
            IntervalHandler.INTERVAL_HANDLER_SINGLETON.decreaseInterval(markedCard);
        }
    }

    private void processCardMarkedGood(Card markedCard, Deck deckMarkedCardComesFrom){
        IntervalHandler.INTERVAL_HANDLER_SINGLETON.increaseInterval(markedCard);

        if (checkIfCardIsFinishedForSession(markedCard)){
            finishReviewingCardForSession(markedCard, deckMarkedCardComesFrom);
        }
    }

    private void moveCardToDifferentDeck(Card cardToMove, Deck sourceDeck, Deck targetDeck){
        sourceDeck.cards.remove(cardToMove);
        targetDeck.cards.add(cardToMove);
    }

    private boolean isFinished(Deck updatedDeck){
        return updatedDeck.cards.size() == totalNumberOfCardsToBeReviewed;
    }

    public boolean checkIfCardIsFinishedForSession(Card cardToCheck){
        return cardToCheck.getState() == Card.CardState.LEARNT;
    }

    private void finishReviewingCardForSession(Card finishedCard, Deck sourceDeck){
        sourceDeck.cards.remove(finishedCard);
        updatedDeck.cards.add(finishedCard);
    }

    private void finishReview(){
        System.out.println("no cards left to review today");
        updatedDeck.cards.addAll(unchangedCards);
        DeckFileParser.DECK_FILE_PARSER_SINGLETON.serializeDeck(deckFilePath, updatedDeck);
    }
}
