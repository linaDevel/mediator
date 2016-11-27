package ru.linachan.mediator.collector;

public class SearchResult {

    public String url;
    public String title;

    public SearchResult(String title, String url) {
        this.url = url;
        this.title = title;
    }
}
