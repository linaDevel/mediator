package ru.linachan.mediator.collector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;

public class SearchProvider {

    private final JSONObject title;
    private final JSONObject url;

    public SearchProvider(JSONObject title, JSONObject url) {
        this.title = title;
        this.url = url;
    }

    private String tryGetValue(JSONObject rule, Element element) {
        String value = null;

        switch ((String) rule.get("type")) {
            case "text":
                value = element.text();
                break;
            case "attr":
                value = element.attr((String) rule.get("name"));
                break;
        }

        if (rule.containsKey("replace")&&value != null) {
            for (Object replacement: (JSONArray) rule.get("replace")) {
                value = value.replace(
                    (String) ((JSONArray) replacement).get(0),
                    (String) ((JSONArray) replacement).get(1)
                );
            }
        }

        return value;
    }

    public SearchResult parse(Element element) {
        return new SearchResult(
            tryGetValue(this.title, element),
            tryGetValue(this.url, element)
        );
    }
}
