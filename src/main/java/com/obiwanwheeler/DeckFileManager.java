package com.obiwanwheeler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public final class DeckFileManager {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    public static Deck deserializeDeck(String deckFilePath) {
        File deckFile = new File(deckFilePath);
        Deck deck;
        try {
            deck = OBJECTMAPPER.readValue(deckFile, new TypeReference<>(){});
            return deck;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void serializeDeck(String deckFilePath, Deck deckToWrite) {
        File deckFile = new File(deckFilePath);
        try {
            OBJECTMAPPER.writerWithDefaultPrettyPrinter().writeValue(deckFile, deckToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
