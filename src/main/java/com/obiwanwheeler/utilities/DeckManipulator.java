package com.obiwanwheeler.utilities;

import com.obiwanwheeler.objects.Card;
import com.obiwanwheeler.objects.Deck;

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

    public Deck getCardsNotBeingReviewedToday(Deck fullDeck){
        return new Deck(fullDeck.getCards().stream().filter(c -> !c.getShouldBeReviewed()).collect(toList()));
    }

    public List<Card> getLowestIntervalCards(List<Card> partialDeck){
        int minInterval = partialDeck.stream().min(Comparator.comparing(Card::getMinutesUntilNextReviewInThisSession)).
                orElseThrow(NoSuchElementException::new).getMinutesUntilNextReviewInThisSession().toMinutesPart();
        return partialDeck.stream().filter(c -> c.getMinutesUntilNextReviewInThisSession().toMinutesPart() == minInterval).collect(toList());
    }
}
