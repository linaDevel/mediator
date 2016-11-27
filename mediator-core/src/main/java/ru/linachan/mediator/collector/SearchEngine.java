package ru.linachan.mediator.collector;

import org.json.simple.JSONObject;
import ru.linachan.mediator.common.QueryHelper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SearchEngine {

    private final String url;
    private final String filter;
    private final SearchProvider provider;

    public SearchEngine(String url, String filter, SearchProvider provider) {
        this.url = url;
        this.filter = filter;
        this.provider = provider;
    }

    public List<SearchResult> search(String request) throws IOException {
        QueryHelper data = new QueryHelper(String.format(url, request));

        return data.selectAll(filter).stream()
            .map(provider::parse)
            .collect(Collectors.toList());
    }

    public static SearchEngine fromJSON(JSONObject data) {
        JSONObject searchData = (JSONObject) data.get("search");
        JSONObject fieldsData = (JSONObject) searchData.get("fields");

        JSONObject titlePattern = (JSONObject) fieldsData.get("title");
        JSONObject urlPattern = (JSONObject) fieldsData.get("url");

        return new SearchEngine(
            (String) searchData.get("url"),
            (String) searchData.get("filter"),
            new SearchProvider(titlePattern, urlPattern)
        );
    }
}
