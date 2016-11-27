package ru.linachan.mediator.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import ru.linachan.mediator.collector.DataEngine;
import ru.linachan.mediator.common.HttpUtils;

import java.io.IOException;
import java.util.Map;

public class DataHandler implements HttpHandler {

    private final Map<String, DataEngine> dataEngines;

    public DataHandler(Map<String, DataEngine> dataEngines) {
        this.dataEngines = dataEngines;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = HttpUtils.parseQuery(httpExchange.getRequestURI());

        JSONObject response = new JSONObject();

        try {
            if (params.containsKey("p")) {
                DataEngine dataEngine = dataEngines.getOrDefault(params.get("p"), null);

                if (dataEngine != null) {
                    response.put("data", dataEngine.getData(params.get("id")));
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
