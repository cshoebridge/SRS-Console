package com.obiwanwheeler.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.obiwanwheeler.interfaces.SerializableObject;

import java.io.File;
import java.io.IOException;

public class Serializer {

    public static final Serializer SERIALIZER_SINGLETON = new Serializer();
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public <T extends SerializableObject> void serializeToExisting(String existingFilePath, T objectToSerialize) {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        File existingFile = new File(existingFilePath);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(existingFile, objectToSerialize);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("unable to serialize");
        }
    }

    public <T extends SerializableObject> void serializeToNew(T objectToSerialize){
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        File newFile = new File(objectToSerialize.getFolderPath() + objectToSerialize.getFileName() + FileExtensions.JSON);
        if (canMakeFile(newFile)){
            try {
                OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(newFile, objectToSerialize);
                System.out.println("file successfully created");
            } catch (IOException e) {
                System.out.println("unable to create file");
            }
        }
        else{
            System.out.println("a file with this name already exists!");
        }
    }

    private boolean canMakeFile(File fileToMake){
        try {
            return fileToMake.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("couldn't create file");
            return false;
        }
    }
}
