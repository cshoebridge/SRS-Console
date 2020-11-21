package com.obiwanwheeler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DeckManager {

    public static List<List<Card>> splitDeck(Deck fullDeck){
        List<Card> newCards = fullDeck.cards.stream().filter(c -> c.getState() == Card.CardState.NEW).collect(toList());
        //at the start of any given review session the learning list will be empty, as cards may only enter the 'learning' state at review time.
        List<Card> learningCards = Collections.emptyList();
        List<Card> learntCards = fullDeck.cards.stream().filter(c -> c.getState() == Card.CardState.LEARNT).collect(toList());

        return Arrays.asList(newCards, learningCards, learntCards);
    }
}
