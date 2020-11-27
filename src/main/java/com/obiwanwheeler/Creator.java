package com.obiwanwheeler;

import java.util.Scanner;

public class Creator {

    Scanner scanner = new Scanner(System.in);

    public void startCreate(){
        System.out.println("would you like to make a deck (d), or add new cards to existing ones (c)?");
        String userChoice = scanner.next();
        switch (userChoice){
            case "d": new DeckCreator().createNewDeck();
                break;
            case "c": new CardCreator().doCardCreation();
                break;
        }
    }
}
