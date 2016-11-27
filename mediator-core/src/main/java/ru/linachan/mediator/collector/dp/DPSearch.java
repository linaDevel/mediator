package ru.linachan.mediator.collector.dp;

import org.jsoup.nodes.Element;
import ru.linachan.mediator.common.QueryHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DPSearch {

    public class DPSearchResult {

        public final String url;
        public final String title;
        public final DPType type;

        public DPSearchResult(Element element) {
            QueryHelper data = new QueryHelper(element);
            if (element.classNames().contains("dvd")) {
                type = DPType.MOVIE;

                url = data.selectAttr("div.title-bar > div.title-text > h4 > span > a", "href").replace("/movies/info/", "");
                title = data.selectText("div.title-bar > div.title-text > h4 > span > a");
            } else if (element.classNames().contains("scene")) {
                type = DPType.VIDEO;

                String movie = data.selectText("div.release-info > div.info-left > div > h4 > span > a");
                String scene = data.selectText("div.title-bar > div.title-text > h4 > span");

                url = data.selectAttr("div.release-info > div.info-left > div > h4 > span > a", "href").replace("/movies/info/", "");
                title = String.format("%s - %s", movie, scene);
            } else {
                type = DPType.UNKNOWN;

                url = null;
                title = null;
            }
        }

        public DPVideo getVideo() throws IOException {
            return new DPVideo(url);
        }

        @Override
        public String toString() {
            return String.format("<%s>='%s'", title, url);
        }

    }

    public List<DPSearchResult> searchByName(String request) throws IOException {
        QueryHelper data = new QueryHelper(
            "http://www.digitalplayground.com/search/?q=" + URLEncoder.encode(request, "UTF-8")
        );

        List<DPSearchResult> results = new ArrayList<>();

        results.addAll(
            data.selectAll("#container > div.search-wrapper.resp > section.resp.scene.section-search > div > article > div").stream()
                .map(DPSearchResult::new)
                .collect(Collectors.toList())
        );

        results.addAll(
            data.selectAll("#container > div.search-wrapper.resp > section.resp.dvd.section-search > div > article > div").stream()
                .map(DPSearchResult::new)
                .collect(Collectors.toList())
        );

        return results;
    }
}
