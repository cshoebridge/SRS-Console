package com.obiwanwheeler.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.obiwanwheeler.objects.Deck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class DeckFileParser {

    public static final DeckFileParser DECK_FILE_PARSER_SINGLETON = new DeckFileParser();

    public static final String deckFolderPath = "src/main/resources/com/obiwanwheeler/decks/";

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private DeckFileParser(){}

    public Deck deserializeDeck(String deckFilePath) {
        File deckFile = new File(deckFilePath);
        Deck deserializedDeck;

        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        try {
            deserializedDeck = OBJECT_MAPPER.readValue(deckFile, new TypeReference<>(){});
            return deserializedDeck;
        } catch (FileNotFoundException e){
            System.out.println("requested deck could not be found");
            return null;
        } catch (IOException e) {
            System.out.println("unable to deserialize deck file");
            return null;
        }
    }

    public void serializeToExistingDeck(String deckFilePath, Deck deckToSerialize) {
        File deckFile = new File(deckFilePath);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(deckFile, deckToSerialize);
        } catch (IOException e) {
            System.out.println("unable to serialize deck");
        }
    }

    public void serializeToNewDeck(Deck deckToSerialize){

        File newDeckFile = new File(deckFolderPath + deckToSerialize.getDeckName() + FileExtensions.JSON);
        if (canMakeFile(newDeckFile)){
            try {
                OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(newDeckFile, deckToSerialize);
            } catch (IOException e) {
                System.out.println("unable to create deck");
            }
        }
        else{
            System.out.println("a deck with this name already exists!");
        }
    }

    private boolean canMakeFile(File fileToMake){
        try {
            return fileToMake.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("couldn't create deck file");
            return false;
        }
    }
}
