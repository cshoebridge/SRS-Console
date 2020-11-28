package com.obiwanwheeler.utilities;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obiwanwheeler.objects.OptionGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OptionGroupFileParser {

    public static final OptionGroupFileParser OPTION_GROUP_FILE_PARSER_SINGLETON = new OptionGroupFileParser();
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
            System.out.println("requested option group could not be found");
            return null;
        } catch(IOException e){
            System.out.println("unable to deserialize option-group file");
            return null;
        }
    }
}
