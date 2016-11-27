package ru.linachan.mediator.handler.dp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.linachan.mediator.collector.dp.DPSearch;
import ru.linachan.mediator.collector.zz.ZZSearch;
import ru.linachan.mediator.common.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class DPSearchHandler implements HttpHandler {

    private final DPSearch search;

    public DPSearchHandler(DPSearch dpSearch) {
        search = dpSearch;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = HttpUtils.parseQuery(httpExchange.getRequestURI());

        JSONObject response = new JSONObject();

        try {
            if (params.containsKey("q")) {
                JSONArray results = new JSONArray();

                for (DPSearch.DPSearchResult result: search.searchByName(params.get("q"))) {
                    JSONObject data = new JSONObject();

                    data.put("title", result.title);
                    data.put("link", result.url);
                    data.put("type", result.type.toString().toLowerCase());

                    results.add(data);
                }

                response.put("results", results);
            } else {
                response.put("results", new JSONArray());
            }
        } catch(Throwable e) {
            response.put("errType", e.getClass().getName());
            response.put("errMsg", e.getMessage());
        }

        byte[] responseBytes = response.toJSONString().getBytes();

        httpExchange.sendResponseHeaders(200, responseBytes.length + 2);

        httpExchange.getResponseBody().write(responseBytes);
        httpExchange.getResponseBody().write("\r\n".getBytes());

        httpExchange.close();
    }
}
