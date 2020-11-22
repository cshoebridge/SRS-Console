package com.obiwanwheeler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

public class Card {

    private String frontSide;
    private String backSide;

    private CardState state;

    //private LocalDate initialViewDate;
    @JsonIgnore private Duration interval;

    @JsonCreator
    public Card(@JsonProperty("frontSide") String frontSide, @JsonProperty("backSide") String backSide,
                @JsonProperty("state") CardState state /*, @JsonProperty("initialViewDate") LocalDate initialViewDate,*/) {
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.state = state;
        //this.initialViewDate = initialViewDate;
        //TODO get this property from JSON
        this.interval = Duration.ZERO;

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

    public Duration getInterval() {
        return interval;
    }

    public void setInterval(Duration interval) {
        this.interval = interval;
    }

    //endregion

    public enum CardState{
        NEW, LEARNT, LEARNING;
    }
}
