package ru.linachan.mediator;

import com.sun.net.httpserver.HttpServer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.linachan.mediator.collector.SearchEngine;
import ru.linachan.mediator.collector.DataEngine;
import ru.linachan.mediator.handler.DataHandler;
import ru.linachan.mediator.handler.SearchHandler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class MediatorCore {

    public static void main(String[] args) throws IOException, ParseException {
        File providers = new File("providers");

        if (!providers.exists()) {
            System.err.println("Provider folder doesn't exists!");
            System.exit(1);
        }

        File[] providerList = providers.listFiles();

        if (providerList == null) {
            System.err.println("No providers found!");
            System.exit(1);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 1);

        Map<String, SearchEngine> searchEngines = new HashMap<>();
        Map<String, DataEngine> dataEngines = new HashMap<>();

        JSONParser parser = new JSONParser();
        for (File provider: providerList) {
            if (provider.getName().endsWith(".json")) {
                JSONObject providerData = (JSONObject) parser.parse(new FileReader(provider));
                String providerName = provider.getName().replace(".json", "");

                searchEngines.put(providerName, SearchEngine.fromJSON(providerData));
                dataEngines.put(providerName, DataEngine.fromJSON(providerData));

                System.out.println(String.format("Provider registered: %s", providerName));
            }
        }

        server.createContext("/api/search", new SearchHandler(searchEngines));
        server.createContext("/api/data", new DataHandler(dataEngines));

        System.out.println("Starting Mediator...");
        server.start();
    }
}
