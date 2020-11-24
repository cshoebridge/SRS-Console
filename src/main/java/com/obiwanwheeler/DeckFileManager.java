package com.obiwanwheeler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public final class DeckFileManager {

    public static final DeckFileManager DECK_FILE_MANAGER_SINGLETON = new DeckFileManager();

    private final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    public Deck deserializeDeck(String deckFilePath) {
        File deckFile = new File(deckFilePath);
        Deck deck;

        OBJECTMAPPER.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));

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
