package pro.learnup.testdata;

import api.models.Phone;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoConnector {
    private static MongoClient mongoClient;

    public static MongoDatabase getDataBase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create("mongodb://root:password@localhost:27017");
        }
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        Logger.getLogger("org.mongodb.driver").setLevel(Level.ALL);
        return mongoClient.getDatabase("mobileShop").withCodecRegistry(pojoCodecRegistry);
    }
}