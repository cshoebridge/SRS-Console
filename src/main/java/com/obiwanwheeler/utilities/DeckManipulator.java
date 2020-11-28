package com.obiwanwheeler.utilities;

import com.obiwanwheeler.objects.Card;
import com.obiwanwheeler.objects.Deck;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class DeckManipulator {

    public static final DeckManipulator DECK_MANIPULATOR_SINGLETON = new DeckManipulator();

    private DeckManipulator(){}

    public List<Card> getKnownCardsToBeReviewedToday(Deck fullDeck){
        return new Deck(fullDeck.getCards().stream().filter(c -> c.getShouldBeReviewed() && c.getState() == Card.CardState.LEARNT).collect(toList())).getCards();
    }

    public List<Card> getNewCards(Deck fullDeck){
        return new Deck(fullDeck.getCards().stream().filter(c -> c.getState() == Card.CardState.NEW).collect(toList())).getCards();
    }

    public List<Card> getCardsNotBeingReviewedToday(Deck fullDeck){
        return fullDeck.getCards().stream().filter(c -> !c.getShouldBeReviewed()).collect(toList());
    }

    public List<Card> getCardsToReviewToday(Deck fullDeck){
        int numberOfNewCardsToLearnToday = fullDeck.getOptionGroup().getNumberOfNewCardsToLearn();
        List<Card> reappearingKnownCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getKnownCardsToBeReviewedToday(fullDeck);
        List<Card> potentialNewCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getNewCards(fullDeck);

        List<Card> cardsToReviewToday = new LinkedList<>();

        if (potentialNewCards.size() != 0){
            for (int i = 0; i < numberOfNewCardsToLearnToday && i < potentialNewCards.size(); i++) {
                Card cardAboutToBeAdded = potentialNewCards.get(i);
                cardAboutToBeAdded.setInitialViewDate(LocalDate.now());
                cardsToReviewToday.add(cardAboutToBeAdded);
            }
        }
        cardsToReviewToday.addAll(reappearingKnownCards);
        return cardsToReviewToday;
    }

    public List<Card> getLowestIntervalCards(List<Card> partialDeck){
        int minInterval = partialDeck.stream().min(Comparator.comparing(Card::getMinutesUntilNextReviewInThisSession)).
                orElseThrow(NoSuchElementException::new).getMinutesUntilNextReviewInThisSession().toMinutesPart();
        return partialDeck.stream().filter(c -> c.getMinutesUntilNextReviewInThisSession().toMinutesPart() == minInterval).collect(toList());
    }
}
