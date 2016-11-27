package ru.linachan.mediator.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ru.linachan.mediator.collector.SearchEngine;
import ru.linachan.mediator.collector.SearchResult;
import ru.linachan.mediator.common.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class SearchHandler implements HttpHandler {

    private final Map<String, SearchEngine> searchEngines;

    public SearchHandler(Map<String, SearchEngine> searchEngines) {
        this.searchEngines = searchEngines;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = HttpUtils.parseQuery(httpExchange.getRequestURI());

        JSONObject response = new JSONObject();

        try {
            if (params.containsKey("p")) {
                SearchEngine searchEngine = searchEngines.getOrDefault(params.get("p"), null);

                if (searchEngine != null) {
                    if (params.containsKey("q")) {
                        JSONArray results = new JSONArray();

                        for (SearchResult result : searchEngine.search(params.get("q"))) {
                            JSONObject data = new JSONObject();

                            data.put("title", result.title);
                            data.put("link", result.url);

                            results.add(data);
                        }

                        response.put("results", results);
                    } else {
                        response.put("error", "InvalidRequest: SearchEngine Query is not specified");
                    }
                } else {
                    response.put("error", "InvalidRequest: Provider is not available");
                }
            } else {
                response.put("error", "InvalidRequest: Provider is not specified");
            }
        } catch (Throwable e) {
            response.put("error", String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()));
            e.printStackTrace();
        }

        byte[] responseBytes = response.toJSONString().getBytes();

        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, responseBytes.length + 2);

        httpExchange.getResponseBody().write(responseBytes);
        httpExchange.getResponseBody().write("\r\n".getBytes());

        httpExchange.close();
    }
}
