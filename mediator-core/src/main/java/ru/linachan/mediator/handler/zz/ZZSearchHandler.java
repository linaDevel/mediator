package ru.linachan.mediator.handler.zz;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ru.linachan.mediator.collector.zz.ZZSearch;
import ru.linachan.mediator.common.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class ZZSearchHandler implements HttpHandler {

    private final ZZSearch search;

    public ZZSearchHandler(ZZSearch search) {
        this.search = search;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = HttpUtils.parseQuery(httpExchange.getRequestURI());

        JSONObject response = new JSONObject();

        if (params.containsKey("q")) {
            JSONArray results = new JSONArray();

            for (ZZSearch.ZZSearchResult result: search.searchByName(params.get("q"))) {
                JSONObject data = new JSONObject();

                data.put("title", result.title);
                data.put("link", result.url);

                results.add(data);
            }

            response.put("results", results);
        } else {
            response.put("results", new JSONArray());
        }

        byte[] responseBytes = response.toJSONString().getBytes();

        httpExchange.sendResponseHeaders(200, responseBytes.length + 2);

        httpExchange.getResponseBody().write(responseBytes);
        httpExchange.getResponseBody().write("\r\n".getBytes());

        httpExchange.close();
    }
}
