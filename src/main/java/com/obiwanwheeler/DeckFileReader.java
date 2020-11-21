package com.obiwanwheeler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class DeckFileReader {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

    public static Deck deserializeDeck(String deckFilePath) throws IOException {

        File deckFile = new File(deckFilePath);
        ArrayList<Card> cards = OBJECTMAPPER.readValue(deckFile, new TypeReference<>(){});
        return new Deck(cards);
    }
}
