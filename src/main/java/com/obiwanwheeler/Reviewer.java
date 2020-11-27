package com.obiwanwheeler;

import com.obiwanwheeler.objects.OptionGroup;
import com.obiwanwheeler.utilities.CardSelector;
import com.obiwanwheeler.utilities.DeckFileParser;
import com.obiwanwheeler.utilities.DeckManipulator;
import com.obiwanwheeler.utilities.IntervalHandler;
import com.obiwanwheeler.objects.Card;
import com.obiwanwheeler.objects.Deck;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Reviewer {

    private final Scanner scanner = new Scanner(System.in);

    private final String deckFilePath;

    private final int totalNumberOfCardsToBeReviewed;
    private final List<Card> unchangedCards;
    private final List<Card> cardsToReviewToday = new LinkedList<>();
    private final Deck updatedDeck;
    private final IntervalHandler intervalHandler;

    public Reviewer(String deckFilePath){

        this.deckFilePath = deckFilePath;

        //TODO get file from FX
        Deck deckToReview = DeckFileParser.DECK_FILE_PARSER_SINGLETON.deserializeDeck(this.deckFilePath);

        updatedDeck = new Deck(new LinkedList<>());
        if (Objects.requireNonNull(deckToReview).getDeckName() == null || Objects.requireNonNull(deckToReview).getDeckName().equals("") ){
            String[] splitFilePath = deckFilePath.split("/");
            updatedDeck.setDeckName(splitFilePath[splitFilePath.length - 1].replace(".json", ""));
        }
        else{
            updatedDeck.setDeckName(deckToReview.getDeckName());
        }
        updatedDeck.setOptionGroup(Objects.requireNonNull(deckToReview).getOptionGroup());
        updatedDeck.setOptionGroupFilePath(deckToReview.getOptionGroupFilePath());
        OptionGroup reviewSettings = deckToReview.getOptionGroup();
        intervalHandler = new IntervalHandler(reviewSettings);
        int numberOfNewCardsToLearnToday = reviewSettings.getNumberOfNewCardsToLearn();

        unchangedCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getCardsNotBeingReviewedToday(deckToReview).getCards();

        List<Card> reappearingKnownCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getKnownCardsToBeReviewedToday(deckToReview);
        List<Card> potentialNewCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getNewCards(deckToReview);

        if (potentialNewCards.size() != 0){
            for (int i = 0; i < numberOfNewCardsToLearnToday && i < potentialNewCards.size(); i++) {
                Card cardAboutToBeAdded = potentialNewCards.get(i);
                cardAboutToBeAdded.setInitialViewDate(LocalDate.now());
                cardsToReviewToday.add(cardAboutToBeAdded);
            }
        }
        cardsToReviewToday.addAll(reappearingKnownCards);
        totalNumberOfCardsToBeReviewed = cardsToReviewToday.size();
    }

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
        return updatedDeck.getCards().size() == totalNumberOfCardsToBeReviewed;
    }

    public boolean checkIfCardIsFinishedForSession(Card cardToCheck){
        return cardToCheck.getState() == Card.CardState.LEARNT;
    }

    private void finishReviewingCardForSession(Card finishedCard){
        cardsToReviewToday.remove(finishedCard);
        updatedDeck.getCards().add(finishedCard);
    }

    private void finishReview(){
        System.out.println("no cards left to review today");
        updatedDeck.getCards().addAll(unchangedCards);
        DeckFileParser.DECK_FILE_PARSER_SINGLETON.serializeToExistingDeck(deckFilePath, updatedDeck);
    }
}
