package ru.linachan.mediator.collector.zz;

import org.jsoup.nodes.Element;
import ru.linachan.mediator.common.QueryHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

public class ZZSearch {

    public class ZZSearchResult {

        public final String url;
        public final String title;

        public ZZSearchResult(Element element) {
            url = element.attr("href").replace("/scenes/view/id/", "");
            title = element.attr("title");
        }

        public ZZVideo getVideo() throws IOException {
            return new ZZVideo(url);
        }

        @Override
        public String toString() {
            return String.format("<%s>='%s'", title, url);
        }
    }

    public List<ZZSearchResult> searchByName(String request) throws IOException {
        QueryHelper data = new QueryHelper(
            "http://www.brazzers.com/search/all/?q=" + URLEncoder.encode(request, "UTF-8")
        );

        return data.selectAll("#container > section.section-release-container > div > div > div > div.card-image > a").stream()
            .map(ZZSearchResult::new)
            .collect(Collectors.toList());
    }
}
