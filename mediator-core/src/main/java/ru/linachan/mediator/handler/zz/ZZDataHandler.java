package ru.linachan.mediator.handler.zz;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import ru.linachan.mediator.collector.zz.ZZSearch;
import ru.linachan.mediator.collector.zz.ZZVideo;
import ru.linachan.mediator.common.HttpUtils;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ZZDataHandler implements HttpHandler {

    private final ZZSearch search;

    public ZZDataHandler(ZZSearch search) {
        this.search = search;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = HttpUtils.parseQuery(httpExchange.getRequestURI());

        JSONObject response = new JSONObject();

        if (params.containsKey("id")) {
            response.put("data", new ZZVideo(params.get("id")).toJSON());
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
