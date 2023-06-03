package com.rishi.utility;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
public class ReadJSONFile {

    public static JsonObject readJsonFile(String filePath) throws Exception {
        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(filePath);
        JsonElement obj = parser.parse(reader);
        JsonObject jsonObject = obj.getAsJsonObject();
        return jsonObject;
    }

    public static String getTranscriptData(String filePath) throws Exception {
        JsonObject obj = readJsonFile(filePath);
        JsonObject results = obj.getAsJsonObject("results");
        JsonArray transcripts = results.getAsJsonArray("transcripts");
        JsonObject transcript = transcripts.get(0).getAsJsonObject();
        return transcript.get("transcript").toString();
    }
}