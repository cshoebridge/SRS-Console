package com.obiwanwheeler.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.obiwanwheeler.objects.Deck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class DeckFileParser {

    public static final DeckFileParser DECK_FILE_PARSER_SINGLETON = new DeckFileParser();

    public static final String DECK_FOLDER_PATH = "src/main/resources/com/obiwanwheeler/decks/";

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
            e.printStackTrace();
            System.out.println("unable to deserialize deck file");
            return null;
        }
    }
}
