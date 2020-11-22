package com.obiwanwheeler;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class DeckManager {

    public static List<Deck> splitDeck(Deck fullDeck){
        Deck newCards = new Deck(fullDeck.cards.stream().filter(c -> c.getState() == Card.CardState.NEW).collect(toList()));
        //at the start of any given review session the learning list will be empty, as cards may only enter the 'learning' state at review time.
        Deck learningCards = new Deck(new LinkedList<>());
        Deck learntCards = new Deck(fullDeck.cards.stream().filter(c -> c.getState() == Card.CardState.LEARNT).collect(toList()));

        return new LinkedList<>(Arrays.asList(newCards, learningCards, learntCards));
    }

    public static List<Card> getLowestIntervalCards(Deck splitDeck){
        int minInterval = splitDeck.cards.stream().min(Comparator.comparing(Card::getInterval)).orElseThrow(NoSuchElementException::new).getInterval().toMinutesPart();
        return splitDeck.cards.stream().filter(c -> c.getInterval().toMinutesPart() == minInterval).collect(toList());
    }
}
