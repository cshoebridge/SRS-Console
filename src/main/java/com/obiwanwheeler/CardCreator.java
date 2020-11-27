package com.obiwanwheeler;

import com.obiwanwheeler.objects.Card;
import com.obiwanwheeler.objects.Deck;
import com.obiwanwheeler.utilities.DeckFileParser;
import com.obiwanwheeler.utilities.FileExtensions;

import java.util.Scanner;

public class CardCreator {

    private final Scanner scanner = new Scanner(System.in);

    public void doCardCreation(){

        do {
            createNewCard();
            System.out.println("do you want to make another card?");
        } while (scanner.nextLine().equals("y"));
    }

    private void createNewCard(){
        String deckToAddToPath = getDeckFilePathWithInput();

        Deck tempDeck = getDeckToAddTo(deckToAddToPath);

        Card newCard = buildCardWithInput();

        tempDeck.getCards().add(newCard);

        rewriteDeck(deckToAddToPath, tempDeck);
    }

    private String getDeckFilePathWithInput(){
        System.out.println("what deck do you want to add this card to?");
        String deckToAddToName = scanner.nextLine();
        return DeckFileParser.deckFolderPath + deckToAddToName + FileExtensions.JSON;
    }

    private Deck getDeckToAddTo(String deckFilePath){
        return DeckFileParser.DECK_FILE_PARSER_SINGLETON.deserializeDeck(deckFilePath);
    }

    private Card buildCardWithInput(){
        System.out.print("target language sentence : ");
        String targetSentence = scanner.nextLine();
        System.out.print("native language translation : ");
        String nativeTranslation = scanner.nextLine();

        Card.Builder cardBuilder = new Card.Builder();
        return cardBuilder.setFrontSide(targetSentence).setBackSide(nativeTranslation).setState(Card.CardState.NEW).build();
    }

    private void rewriteDeck(String deckToAddToPath, Deck deckToRewrite){
        DeckFileParser.DECK_FILE_PARSER_SINGLETON.serializeToExistingDeck(deckToAddToPath, deckToRewrite);
    }
}
