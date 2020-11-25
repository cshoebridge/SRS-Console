package com.obiwanwheeler;

import java.time.Duration;
import java.time.Period;

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
            card.setMinutesUntilNextReviewInThisSession(Duration.ofMinutes(1));
            card.setState(Card.CardState.LEARNING);
            return false;
        }
        else {
            switch(card.getMinutesUntilNextReviewInThisSession().toMinutesPart()){
                case 1:
                    card.setMinutesUntilNextReviewInThisSession(Duration.ofMinutes(10));
                    return false;
                case 10:
                    card.setDaysFromFirstSeenToNextReview(Period.ofDays(1));
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
        if (card.getState() != Card.CardState.NEW || card.getMinutesUntilNextReviewInThisSession() != Duration.ofMinutes(1)){
            switch(card.getState()){
                case LEARNT:
                    card.setState(Card.CardState.LEARNING);
                    card.setMinutesUntilNextReviewInThisSession(Duration.ofMinutes(10));
                    break;
                case LEARNING:
                    card.setMinutesUntilNextReviewInThisSession(Duration.ofMinutes(1));
                    break;
            }
        }
    }
}
