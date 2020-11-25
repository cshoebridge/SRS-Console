package com.obiwanwheeler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Deck {

    public List<Card> cards;

    @JsonCreator
    public Deck(@JsonProperty("cards") List<Card> cards) {
        this.cards = cards;
    }

}
