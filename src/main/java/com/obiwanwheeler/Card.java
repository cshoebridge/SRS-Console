package com.obiwanwheeler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDate;

public class Card {

    private String frontSide;
    private String backSide;

    private CardState state;

    private Duration daysFromFirstSeenToNextReview;

    private final boolean shouldBeReviewed;

    @JsonCreator
    public Card(@JsonProperty("frontSide") String frontSide, @JsonProperty("backSide") String backSide,
                @JsonProperty("state") CardState state ,
                @JsonProperty("initialViewDate") LocalDate initialViewDate,
                @JsonProperty ("daysFromFirstSeenToNextReview") Duration daysFromFirstSeenToNextReview) {
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.state = state;
        //TODO get these properties from JSON
        this.daysFromFirstSeenToNextReview = daysFromFirstSeenToNextReview;
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

    public Duration getDaysFromFirstSeenToNextReview() {
        return daysFromFirstSeenToNextReview;
    }

    public void setDaysFromFirstSeenToNextReview(Duration daysFromFirstSeenToNextReview) {
        this.daysFromFirstSeenToNextReview = daysFromFirstSeenToNextReview;
    }

    public boolean getShouldBeReviewed() {
        return shouldBeReviewed;
    }
    //endregion

    public enum CardState{
        NEW, LEARNT, LEARNING
    }
}
