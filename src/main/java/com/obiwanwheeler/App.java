package com.obiwanwheeler;

import com.obiwanwheeler.utilities.DeckFileParser;
import com.obiwanwheeler.utilities.FileExtensions;

import java.util.Scanner;

public class App {

    public void runApp(){
        tempMenu();
    }

    private void tempMenu(){
        System.out.println("create a new deck or new cards (1)\ndo a review (2)");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextLine()){
            case "1": Creator creator = new Creator();
                creator.startCreate();
                break;
            case "2":
                System.out.println("what deck do you want to review");
                Reviewer reviewer = new Reviewer(getDeckPathFromName(scanner.nextLine()));
                //do the review
                reviewer.doReview();
        }
    }

    private String getDeckPathFromName(String deckName){
        return DeckFileParser.deckFolderPath + deckName + FileExtensions.JSON;
    }
}
