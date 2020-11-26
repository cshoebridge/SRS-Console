package com.obiwanwheeler.utilities;

import com.obiwanwheeler.objects.Card;

import java.util.List;
import java.util.Random;

public class CardSelector {

    Random random = new Random();

    public Card chooseACard(List<Card> cardsToChooseFrom){
        //get lowest interval cards
        List<Card> lowestIntervalCards = DeckManipulator.DECK_MANIPULATOR_SINGLETON.getLowestIntervalCards(cardsToChooseFrom);
        //chooses a card from those
        return lowestIntervalCards.get(random.nextInt(lowestIntervalCards.size()));
    }
}
