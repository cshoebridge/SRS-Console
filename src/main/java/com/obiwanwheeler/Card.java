package com.obiwanwheeler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;

public class Card {

    private String frontSide;
    private String backSide;

    private CardState state;

    private Period daysFromFirstSeenToNextReview;

    private LocalDate initialViewDate;
    @JsonIgnore private Duration minutesUntilNextReviewInThisSession;
    @JsonIgnore private boolean shouldBeReviewed;

    @JsonCreator
    public Card(@JsonProperty("frontSide") String frontSide, @JsonProperty("backSide") String backSide,
                @JsonProperty("state") CardState state ,
                @JsonProperty("initialViewDate") LocalDate initialViewDate,
                @JsonProperty ("daysFromFirstSeenToNextReview") Period daysFromFirstSeenToNextReview) {
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.state = state;
        //TODO get these properties from JSON
        this.daysFromFirstSeenToNextReview = daysFromFirstSeenToNextReview;
        this.initialViewDate = initialViewDate;

        minutesUntilNextReviewInThisSession = daysFromFirstSeenToNextReview.getDays() >= 1 ? Duration.ofMinutes(-1) : Duration.ZERO;
        LocalDate nextReviewDate = initialViewDate.plus(daysFromFirstSeenToNextReview);
        shouldBeReviewed = LocalDate.now().isAfter(nextReviewDate) || LocalDate.now().equals(nextReviewDate);
    }

    //region getters and setters
    public String getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public void setBackSide(String backSide) {
        this.backSide = backSide;
    }

    public CardState getState() {
        return state;
    }

    public void setState(CardState state) {
        this.state = state;
    }

    public LocalDate getInitialViewDate() {
        return initialViewDate;
    }

    public void setInitialViewDate(LocalDate initialViewDate){
        this.initialViewDate = initialViewDate;
    }

    public Duration getMinutesUntilNextReviewInThisSession() {
        return minutesUntilNextReviewInThisSession;
    }

    public void setMinutesUntilNextReviewInThisSession(Duration minutesUntilNextReviewInThisSession) {
        this.minutesUntilNextReviewInThisSession = minutesUntilNextReviewInThisSession;
    }

    public Period getDaysFromFirstSeenToNextReview() {
        return daysFromFirstSeenToNextReview;
    }

    public void setDaysFromFirstSeenToNextReview(Period daysFromFirstSeenToNextReview) {
        this.daysFromFirstSeenToNextReview = daysFromFirstSeenToNextReview;
    }

    public boolean getShouldBeReviewed() {
        return shouldBeReviewed;
    }

    public void setShouldBeReviewed(boolean shouldBeReviewed){
        this.shouldBeReviewed = shouldBeReviewed;
    }
    //endregion

    public enum CardState{
        NEW, LEARNT, LEARNING
    }
}
