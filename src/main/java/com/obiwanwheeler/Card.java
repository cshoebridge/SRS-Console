package com.obiwanwheeler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {

    private String frontSide;
    private String backSide;

    private CardState state;

    @JsonCreator
    public Card(@JsonProperty("frontSide") String frontSide, @JsonProperty("backSide") String backSide, @JsonProperty("state") CardState state) {
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.state = state;
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
    //endregion

    public enum CardState{
        NEW, LEARNT, LEARNING;
    }
}
