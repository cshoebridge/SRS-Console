package com.obiwanwheeler;

import com.obiwanwheeler.objects.Deck;
import com.obiwanwheeler.utilities.FileExtensions;
import com.obiwanwheeler.utilities.OptionGroupFileParser;
import com.obiwanwheeler.utilities.Serializer;

import java.util.Scanner;

public class DeckCreator {

    Scanner scanner = new Scanner(System.in);

    public void createNewDeck(){
        String deckName = askForName();
        //TODO in FX give option to choose option group
        String optionGroupName = askForOptionGroupName();
        Deck newDeck = new Deck(deckName, findOptionGroupFile(optionGroupName));
        Serializer.SERIALIZER_SINGLETON.serializeToNew(newDeck);
    }

    private String askForName(){
        System.out.print("what do you want to name the new deck? : ");
        return scanner.nextLine();
    }

    private String askForOptionGroupName(){
        System.out.print("what do you want this deck's option group to be? : ");
        return scanner.nextLine();
    }

    private String findOptionGroupFile(String groupName){
        return OptionGroupFileParser.OPTION_GROUP_FOLDER_PATH + groupName + FileExtensions.JSON;
    }
}
