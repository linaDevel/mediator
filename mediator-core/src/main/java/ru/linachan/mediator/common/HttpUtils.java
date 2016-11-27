package ru.linachan.mediator.common;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpUtils {

    private HttpUtils() {}

    public static Map<String, String> parseQuery(URI requestUri) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();

        String[] pairs = requestUri.getQuery().split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");

            query_pairs.put(
                URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
            );
        }
        return query_pairs;
    }

}
