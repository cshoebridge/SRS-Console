package com.obiwanwheeler.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obiwanwheeler.objects.Deck;
import com.obiwanwheeler.objects.OptionGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OptionGroupFileParser {

    public static final OptionGroupFileParser OPTION_GROUP_FILE_PARSER_SINGLETON = new OptionGroupFileParser();

    public static final String OPTION_GROUP_FOLDER_PATH = "src/main/resources/com/obiwanwheeler/option-groups/";
    public static final String DEFAULT_OPTION_GROUP_PATH = "src/main/resources/com/obiwanwheeler/option-groups/default-option-group.json";

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private OptionGroupFileParser(){}

    public OptionGroup deserializeOptionGroup(String optionGroupFilePath){
        assert optionGroupFilePath != null;
        File optionGroupFile = new File(optionGroupFilePath);
        OptionGroup deserializedOptionGroup;
        try{
            deserializedOptionGroup = OBJECT_MAPPER.readValue(optionGroupFile, new TypeReference<>() {});
            return deserializedOptionGroup;
        } catch (FileNotFoundException e) {
            System.out.println("this decks option group does not exist : setting to default...");
            try {
                return OBJECT_MAPPER.readValue(new File(DEFAULT_OPTION_GROUP_PATH), new TypeReference<>() {});
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
                System.out.println("default option group file missing or corrupt");
                return null;
            } catch (IOException ioException)
            {
                ioException.printStackTrace();
                System.out.println("unable to deserialize default option file");
                return null;
            }
        } catch(IOException e){
            System.out.println("unable to deserialize option group file");
            return null;
        }
    }
}
