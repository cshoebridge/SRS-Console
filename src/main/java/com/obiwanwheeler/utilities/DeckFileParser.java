package com.obiwanwheeler.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.obiwanwheeler.objects.Deck;

import java.io.File;
import java.io.IOException;

public final class DeckFileParser {

    public static final DeckFileParser DECK_FILE_PARSER_SINGLETON = new DeckFileParser();

    private final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    private DeckFileParser(){}

    public Deck deserializeDeck(String deckFilePath) {
        File deckFile = new File(deckFilePath);
        Deck deserializedDeck;

        OBJECTMAPPER.registerModule(new JavaTimeModule());

        try {
            deserializedDeck = OBJECTMAPPER.readValue(deckFile, new TypeReference<>(){});
            return deserializedDeck;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("unable to deserialize deck file");
        }
        return null;
    }

    public void serializeDeck(String deckFilePath, Deck deckToWrite) {
        File deckFile = new File(deckFilePath);
        OBJECTMAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            OBJECTMAPPER.writerWithDefaultPrettyPrinter().writeValue(deckFile, deckToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
