package com.obiwanwheeler;

import java.time.Duration;

public final class IntervalHandler {

    public static final IntervalHandler INTERVAL_HANDLER_SINGLETON = new IntervalHandler();

    public boolean updateInterval(Card card, boolean shouldIncrease){

        if (shouldIncrease) {
            return increaseInterval(card);
        }
        else{
            decreaseInterval(card);
            return false;
        }
    }

    private boolean increaseInterval(Card card){

        if (card.getState() == Card.CardState.NEW){
            card.setDaysFromFirstSeenToNextReview(Duration.ofMinutes(1));
            card.setState(Card.CardState.LEARNING);
            return false;
        }
        else {
            switch(card.getDaysFromFirstSeenToNextReview().toMinutesPart()){
                case 1:
                    card.setDaysFromFirstSeenToNextReview(Duration.ofMinutes(10));
                    return false;
                case 10:
                    card.setDaysFromFirstSeenToNextReview(Duration.ofDays(1));
                    card.setState(Card.CardState.LEARNT);
                    return true;
                default:
                    //TODO make algorithm for calculating next interval better
                    card.setDaysFromFirstSeenToNextReview(card.getDaysFromFirstSeenToNextReview().multipliedBy(2));
                    card.setState(Card.CardState.LEARNT);
                    return true;
            }
        }
    }

    private void decreaseInterval(Card card) {

        //if either of these is true, the cards interval cannot be decreased further
        if (card.getState() != Card.CardState.NEW || card.getDaysFromFirstSeenToNextReview() != Duration.ofMinutes(1)){
            switch(card.getState()){
                case LEARNT:
                    card.setState(Card.CardState.LEARNING);
                    card.setDaysFromFirstSeenToNextReview(Duration.ofMinutes(10));
                    break;
                case LEARNING:
                    card.setDaysFromFirstSeenToNextReview(Duration.ofMinutes(1));
                    break;
            }
        }
    }
}
