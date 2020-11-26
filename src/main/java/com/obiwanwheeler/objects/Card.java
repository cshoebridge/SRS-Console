package com.obiwanwheeler.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

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
        this.initialViewDate = initialViewDate;

        if (state != CardState.NEW){
            this.daysFromFirstSeenToNextReview = Objects.requireNonNullElse(daysFromFirstSeenToNextReview, Period.ZERO);
            minutesUntilNextReviewInThisSession = daysFromFirstSeenToNextReview.getDays() >= 1 ? Duration.ofMinutes(-1) : Duration.ZERO;
            LocalDate nextReviewDate = initialViewDate.plus(daysFromFirstSeenToNextReview);
            shouldBeReviewed = LocalDate.now().isAfter(nextReviewDate) || LocalDate.now().equals(nextReviewDate);
        }
        else{
            shouldBeReviewed = true;
            minutesUntilNextReviewInThisSession = Duration.ZERO;
        }
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

    //region builder class
    public static class Builder{
        private String frontSide;
        private String backSide;

        private CardState state;

        public Builder setFrontSide(String frontSide) {
            this.frontSide = frontSide;
            return this;
        }

        public Builder setBackSide(String backSide) {
            this.backSide = backSide;
            return this;
        }

        public Builder setState(CardState state) {
            this.state = state;
            return this;
        }

        public Card build(){
            return new Card(frontSide, backSide, state, LocalDate.now(), Period.ZERO);
        }
    }
    //endregion

    public enum CardState{
        NEW, LEARNING, LEARNT
    }
}
