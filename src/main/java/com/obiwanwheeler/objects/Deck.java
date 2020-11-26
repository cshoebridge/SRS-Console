package com.obiwanwheeler.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.obiwanwheeler.utilities.OptionGroupFileParser;

import java.util.List;

public class Deck {

    private List<Card> cards;
    @JsonIgnore private Card.CardState cardTypeHeld;

    private String optionGroupFilePath;
    @JsonIgnore private OptionGroup optionGroup;

    //used in IDE
    public Deck(List<Card> cards) {
        this.cards = cards;
        this.cardTypeHeld = null;
        optionGroup = null;
    }

    //used in IDE
    public Deck(List<Card> cards, Card.CardState cardTypeHeld) {
        this.cards = cards;
        this.cardTypeHeld = cardTypeHeld;
        optionGroup = null;
    }

    //used by Jackson
    @JsonCreator
    public Deck(@JsonProperty("cards") List<Card> cards, @JsonProperty("optionGroupFilePath") String optionGroupFilePath){
        this.cards = cards;
        this.optionGroupFilePath = optionGroupFilePath;
        this.optionGroup = OptionGroupFileParser.OPTION_GROUP_FILE_PARSER_SINGLETON.deserializeOptionGroup(optionGroupFilePath);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Card.CardState getCardTypeHeld() {
        return cardTypeHeld;
    }

    public String getOptionGroupFilePath() {
        return optionGroupFilePath;
    }

    public void setOptionGroupFilePath(String optionGroupFilePath) {
        this.optionGroupFilePath = optionGroupFilePath;
    }

    public OptionGroup getOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(OptionGroup optionGroup){
        this.optionGroup = optionGroup;
    }
}
