package com.rishi.utility;

import com.mongodb.client.*;
import org.bson.Document;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class MongoDB {

    private static String uri="mongodb+srv://SumanPriya:8dlQGS0usUCo4YcP@rbprod0.rvukq.mongodb.net/rboost";
    private static  MongoClient mongoClient ;

    private MongoDB(){

    }

    public static MongoClient getClient(){
        if(mongoClient==null)
            mongoClient= MongoClients.create(uri);
        return  mongoClient;
    }
    public static MongoDatabase getConnection(String databaseName){
     return getClient().getDatabase(databaseName);
    }

    public static MongoCollection<Document>  getCollection(MongoDatabase database, String collectionName){
        return database.getCollection(collectionName);
    }

    public static void closeDBConnection(){
        getClient().close();
    }

    public static Map<String, JSONObject> getLeadDetailsByPhone(MongoCollection<Document> collection, String phoneNumber){
        Document searchQuery = new Document();
        searchQuery.put("phone",phoneNumber );
        FindIterable<Document> cursor = collection.find(searchQuery);
        Map<String,JSONObject> map=new LinkedHashMap<String,JSONObject>();
        MongoCursor<Document> cursorIterator = cursor.iterator();
            while(cursorIterator.hasNext()) {
                Document detail = cursorIterator.next();
                JSONObject detail_json = new JSONObject( detail.toJson());
                map.put(detail_json.getJSONObject(("apartment")).get("name").toString(),detail_json);
            }
            return map;
    }

    public static Map<String, String> getLeadIdByPhone(MongoCollection<Document> collection, String phoneNumber){
        Document searchQuery = new Document();
        searchQuery.put("phone",phoneNumber );
        FindIterable<Document> cursor = collection.find(searchQuery);
        Map<String,String> map=new LinkedHashMap<String,String>();
        MongoCursor<Document> cursorIterator = cursor.iterator();
        while(cursorIterator.hasNext()) {
            Document detail = cursorIterator.next();
            JSONObject detail_json = new JSONObject( detail.toJson());
            map.put(detail_json.getJSONObject(("apartment")).get("name").toString(),detail_json.getJSONObject(("_id")).get("$oid").toString());
        }
        return map;
    }


}