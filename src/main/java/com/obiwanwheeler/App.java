package com.obiwanwheeler;

public class App {

    public void runApp(){

        Reviewer reviewer = new Reviewer("src/main/resources/com/obiwanwheeler/decks/testDeck.json");

        //do the review
        reviewer.doReview();
    }
}
