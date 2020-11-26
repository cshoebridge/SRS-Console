package com.obiwanwheeler.utilities;

import com.obiwanwheeler.objects.Card;
import com.obiwanwheeler.objects.OptionGroup;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public final class IntervalHandler {

    //TODO get this from config file
    private final List<Integer> intervalSteps;
    private final int graduatingIntervalInDays;
    private final int correctAnswerIncreaseInDays;
    private final int relapseDecreaseInDays;

    public IntervalHandler(OptionGroup config) {
        this.intervalSteps = config.getIntervalSteps();
        this.graduatingIntervalInDays = config.getGraduatingIntervalInDays();
        this.correctAnswerIncreaseInDays = config.getCorrectAnswerIncreaseInDays();
        this.relapseDecreaseInDays = config.getRelapseDecreaseInDays();
    }

    //region when correct answer given

    public void increaseInterval(Card cardToIncrease){

        if (cardToIncrease.getState() == Card.CardState.NEW){
            moveFromNewToLearningQueue(cardToIncrease);
        }
        else {
            increaseCardInterval(cardToIncrease);
        }
    }

    private void moveFromNewToLearningQueue(Card cardToMove){
        cardToMove.setMinutesUntilNextReviewInThisSession(Duration.ofMinutes(intervalSteps.get(0)));
        cardToMove.setState(Card.CardState.LEARNING);
    }

    private void increaseCardInterval(Card cardToIncrease){
        if (cardToIncrease.getState() == Card.CardState.LEARNT){
            updateLearntCardInterval(cardToIncrease);
            return;
        }

        if (canGraduate(cardToIncrease)){
            graduateCard(cardToIncrease);
            return;
        }

        int nextStepIndex = intervalSteps.indexOf(cardToIncrease.getMinutesUntilNextReviewInThisSession().toMinutesPart()) + 1;
        changeMinutesIntervalStep(cardToIncrease, intervalSteps.get(nextStepIndex));
    }

    private void updateLearntCardInterval(Card cardToUpdate){
        //TODO make algorithm for calculating next interval better
        cardToUpdate.setDaysFromFirstSeenToNextReview(cardToUpdate.getDaysFromFirstSeenToNextReview().plusDays(correctAnswerIncreaseInDays));
    }

    private boolean canGraduate(Card cardToCheck){
        return cardToCheck.getMinutesUntilNextReviewInThisSession().toMinutesPart() == intervalSteps.get(intervalSteps.size() - 1);
    }

    private void graduateCard(Card cardToGraduate){
        cardToGraduate.setDaysFromFirstSeenToNextReview(Period.ofDays(graduatingIntervalInDays));
        cardToGraduate.setState(Card.CardState.LEARNT);
    }

    //endregion

    //region when wrong answer given

    public void decreaseInterval(Card cardToDecrease) {
        if (canDecreaseInterval(cardToDecrease)){
            int nextIntervalIndex = intervalSteps.indexOf(cardToDecrease.getMinutesUntilNextReviewInThisSession().toMinutesPart()) - 1;
            changeMinutesIntervalStep(cardToDecrease, nextIntervalIndex);
        }
    }

    private boolean canDecreaseInterval(Card cardToCheck){
        return cardToCheck.getState() != Card.CardState.NEW && cardToCheck.getMinutesUntilNextReviewInThisSession().toMinutesPart() != intervalSteps.get(0);
    }

    public void relearnCard(Card relapsedCard){
        relapsedCard.setMinutesUntilNextReviewInThisSession(Duration.ofMinutes(10));
        relapsedCard.setDaysFromFirstSeenToNextReview(relapsedCard.getDaysFromFirstSeenToNextReview().minusDays(relapseDecreaseInDays));
        if (relapsedCard.getDaysFromFirstSeenToNextReview().isNegative()) relapsedCard.setDaysFromFirstSeenToNextReview(Period.ofDays(1));
    }

    //endregion

    private void changeMinutesIntervalStep(Card cardToUpdate, int nextStep){
        cardToUpdate.setMinutesUntilNextReviewInThisSession(Duration.ofMinutes(nextStep));
    }
}
