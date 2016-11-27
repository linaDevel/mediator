package ru.linachan.mediator.common;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;

public class QueryHelper {

    private final Node data;

    public QueryHelper(String url) throws IOException {
        data = Jsoup.connect(url).get();
    }

    public QueryHelper(Node node) {
        data = node;
    }

    public String selectAttr(String selector, String attr) {
        return select(selector).attr(attr);
    }

    public String selectText(String selector) {
        return select(selector).text();
    }

    public Element select(String selector) {
        return selectAll(selector).first();
    }

    public Elements selectAll(String selector) {
        return ((Element) data).select(selector);
    }

}
