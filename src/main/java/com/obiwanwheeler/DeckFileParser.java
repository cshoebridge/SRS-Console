package com.obiwanwheeler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public final class DeckFileParser {

    public static final DeckFileParser DECK_FILE_PARSER_SINGLETON = new DeckFileParser();

    private final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    public Deck deserializeDeck(String deckFilePath) {
        File deckFile = new File(deckFilePath);
        Deck deck;

        OBJECTMAPPER.registerModule(new JavaTimeModule());

        try {
            deck = OBJECTMAPPER.readValue(deckFile, new TypeReference<>(){});
            return deck;
        } catch (IOException e) {
            e.printStackTrace();
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
