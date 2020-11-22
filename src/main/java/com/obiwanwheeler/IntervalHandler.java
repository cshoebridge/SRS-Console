package com.obiwanwheeler;

import java.time.Duration;

public class IntervalHandler {

    public static boolean updateInterval(Card card, boolean shouldIncrease){

        if (shouldIncrease) {
            return increaseInterval(card);
        }
        else{
            decreaseInterval(card);
            return false;
        }
    }

    private static boolean increaseInterval(Card card){

        if (card.getState() == Card.CardState.NEW){

            card.setInterval(Duration.ofMinutes(1));
            card.setState(Card.CardState.LEARNING);
            return false;
        }
        else {

            switch(card.getInterval().toMinutesPart()){
                case 1:
                    card.setInterval(Duration.ofMinutes(10));
                    return false;
                case 10:
                    card.setInterval(Duration.ofDays(1));
                    card.setState(Card.CardState.LEARNT);
                    return true;
                default:
                    //TODO make algorithm for calculating next interval better
                    card.setInterval(card.getInterval().multipliedBy(2));
                    card.setState(Card.CardState.LEARNT);
                    return true;
            }
        }
    }

    private static void decreaseInterval(Card card) {

        //if either of these is true, the cards interval cannot be decreased further
        if (card.getState() != Card.CardState.NEW || card.getInterval() != Duration.ofMinutes(1)){

            switch(card.getState()){
                case LEARNT:
                    card.setState(Card.CardState.LEARNING);
                    card.setInterval(Duration.ofMinutes(10));
                    break;
                case LEARNING:
                    card.setInterval(Duration.ofMinutes(1));
                    break;
            }
        }
    }
}
