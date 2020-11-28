package com.obiwanwheeler;

import java.util.Scanner;

public class App {

    Scanner scanner = new Scanner(System.in);

    public void runApp(){
        do {
            tempMenu();
            System.out.println("do something else?");
        }while (scanner.nextLine().equals("y"));
    }

    private void tempMenu(){
        System.out.println("create a new deck or new cards (1)\ndo a review (2)\ncreate a new options group (3)");

        switch (scanner.nextLine()){
            case "1": Creator creator = new Creator();
                creator.startCreate();
                break;
            case "2":
                Reviewer reviewer = new Reviewer();
                if (reviewer.tryInitialiseReview()){
                    reviewer.doReview();
                }
                else{
                    System.out.println("unable to do the review");
                }
                break;
            case "3": OptionGroupCreator optionGroupCreator = new OptionGroupCreator();
                optionGroupCreator.createNewOptionGroup();
                break;
        }
    }

}
