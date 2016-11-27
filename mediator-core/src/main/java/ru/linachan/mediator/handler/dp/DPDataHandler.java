package ru.linachan.mediator.handler.dp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import ru.linachan.mediator.collector.dp.DPSearch;
import ru.linachan.mediator.collector.dp.DPVideo;
import ru.linachan.mediator.common.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class DPDataHandler implements HttpHandler {

    private final DPSearch search;

    public DPDataHandler(DPSearch dpSearch) {
        search = dpSearch;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = HttpUtils.parseQuery(httpExchange.getRequestURI());

        JSONObject response = new JSONObject();

        if (params.containsKey("id")) {
            response.put("data", new DPVideo(params.get("id")).toJSON());
        } else {
            response.put("data", null);
        }

        byte[] responseBytes = response.toJSONString().getBytes();

        httpExchange.sendResponseHeaders(200, responseBytes.length + 2);

        httpExchange.getResponseBody().write(responseBytes);
        httpExchange.getResponseBody().write("\r\n".getBytes());

        httpExchange.close();
    }
}
