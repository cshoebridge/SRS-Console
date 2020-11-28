package com.obiwanwheeler;

import com.obiwanwheeler.utilities.*;
import com.obiwanwheeler.objects.Card;
import com.obiwanwheeler.objects.Deck;

import java.io.IOException;
import java.util.*;

public class Reviewer {

    private final Scanner scanner = new Scanner(System.in);

    private String deckFilePath;

    private int numberOfCardsLeftToBeReviewed;
    private List<Card> cardsToReviewToday = new LinkedList<>();

    private Deck updatedDeck;

    private IntervalHandler intervalHandler;

    //region initialise review
    public boolean tryInitialiseReview(){
        deckFilePath = DeckFileParser.DECK_FOLDER_PATH + getNameOfDeckToReview() + FileExtensions.JSON;
        Deck deckToReview = DeckFileParser.DECK_FILE_PARSER_SINGLETON.deserializeDeck(deckFilePath);
        if (deckToReview == null){
            return false;
        }
        initialiseUpdatedDeck(deckToReview);
        insertUnchangedCards(deckToReview);

        intervalHandler = new IntervalHandler(deckToReview.getOptionGroup());

        cardsToReviewToday = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getCardsToReviewToday(deckToReview);
        numberOfCardsLeftToBeReviewed = cardsToReviewToday.size();
        return true;
    }

    private String getNameOfDeckToReview(){
        System.out.println("what deck do you want to review");
        return scanner.nextLine();
    }

    private void initialiseUpdatedDeck(Deck sourceDeck){
        updatedDeck = new Deck(new LinkedList<>());
        updatedDeck.setDeckName(sourceDeck.getDeckName());
        updatedDeck.setOptionGroupFilePath(sourceDeck.getOptionGroupFilePath());
    }

    private void insertUnchangedCards(Deck sourceDeck){
        List<Card> unchangedCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getCardsNotBeingReviewedToday(sourceDeck);
        updatedDeck.getCards().addAll(unchangedCards);
    }
    //endregion

    public void doReview(){
        //do review
        CardSelector cardSelector = new CardSelector();

        while(!reviewIsFinished()){
            Card cardToReview = cardSelector.chooseACard(cardsToReviewToday);

            outputCardSides(cardToReview);
            //wait for the user to give input on whether they recalled the card correctly or not
            //and adjust intervals accordingly.
            waitForAndProcessInput(cardToReview);

            System.out.println("State: " + cardToReview.getState());
            System.out.println("Next interval : " + cardToReview.getMinutesUntilNextReviewInThisSession().toMinutesPart());
        }
        finishReview();
    }

    //TODO do this in FX
    private void outputCardSides(Card cardToOutput){
        System.out.println(cardToOutput.getTargetLanguageSentence());
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(cardToOutput.getTargetLanguageSentence() + "\n" + cardToOutput.getNativeLanguageTranslation());
    }

    private void waitForAndProcessInput(Card reviewedCard){

        boolean markedGood = scanner.next().equals("y");

        if (!markedGood){
            processCardMarkedBad(reviewedCard);
        }
        else{
            processCardMarkedGood(reviewedCard);
        }
    }

    private void processCardMarkedBad(Card markedCard){
        if (markedCard.getState() == Card.CardState.LEARNT){
            intervalHandler.relearnCard(markedCard);
        }
        else {
            intervalHandler.decreaseInterval(markedCard);
        }
    }

    private void processCardMarkedGood(Card markedCard){
        intervalHandler.increaseInterval(markedCard);

        if (checkIfCardIsFinishedForSession(markedCard)){
            finishReviewingCardForSession(markedCard);
        }
    }

    private boolean reviewIsFinished(){
        return numberOfCardsLeftToBeReviewed == 0;
    }

    public boolean checkIfCardIsFinishedForSession(Card cardToCheck){
        return cardToCheck.getState() == Card.CardState.LEARNT;
    }

    private void finishReviewingCardForSession(Card finishedCard){
        cardsToReviewToday.remove(finishedCard);
        numberOfCardsLeftToBeReviewed--;
        updatedDeck.getCards().add(finishedCard);
    }

    private void finishReview(){
        System.out.println("no cards left to review today");
        Serializer.SERIALIZER_SINGLETON.serializeToExisting(deckFilePath, updatedDeck);
    }
}
