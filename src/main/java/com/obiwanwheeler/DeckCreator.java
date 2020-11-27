package com.obiwanwheeler;

import com.obiwanwheeler.objects.Deck;
import com.obiwanwheeler.utilities.DeckFileParser;

import java.util.Scanner;

public class DeckCreator {

    Scanner scanner = new Scanner(System.in);

    public void createNewDeck(){
        String deckName = askForName();
        //TODO in FX give option to choose option group
        Deck newDeck = new Deck(deckName, null);
        DeckFileParser.DECK_FILE_PARSER_SINGLETON.serializeToNewDeck(newDeck);
    }

    private String askForName(){
        System.out.print("what do you want to name the new deck? : ");
        return scanner.nextLine();
    }
}
