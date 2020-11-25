package com.obiwanwheeler;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class DeckManipulator {

    public static final DeckManipulator DECK_MANIPULATOR_SINGLETON = new DeckManipulator();

    public Deck getCardsToBeReviewedToday(Deck fullDeck){
        return new Deck(fullDeck.cards.stream().filter(Card::getShouldBeReviewed).collect(toList()));
    }

    public List<Deck> splitDeck(Deck filteredDeck){
        Deck newCards = new Deck(filteredDeck.cards.stream().filter(c -> c.getState() == Card.CardState.NEW).collect(toList()));
        //at the start of any given review session the learning list will be empty, as cards may only enter the 'learning' state at review time.
        Deck learningCards = new Deck(new LinkedList<>());
        Deck learntCards = new Deck(filteredDeck.cards.stream().filter(c -> c.getState() == Card.CardState.LEARNT).collect(toList()));

        return new LinkedList<>(Arrays.asList(newCards, learningCards, learntCards));
    }

    public List<Card> getLowestIntervalCards(Deck splitDeck){
        int minInterval = splitDeck.cards.stream().min(Comparator.comparing(Card::getMinutesUntilNextReviewInThisSession)).
                orElseThrow(NoSuchElementException::new).getMinutesUntilNextReviewInThisSession().toMinutesPart();
        return splitDeck.cards.stream().filter(c -> c.getMinutesUntilNextReviewInThisSession().toMinutesPart() == minInterval).collect(toList());
    }
}
