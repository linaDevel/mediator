package ru.linachan.mediator.collector.zz;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import ru.linachan.mediator.common.Image;
import ru.linachan.mediator.common.QueryHelper;
import ru.linachan.mediator.common.Id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ZZVideo {

    public final Id id;

    public final String date;

    public final String title;
    public final String description;
    public final String category;

    public final List<ZZModel> models;
    public final List<ZZTag> tags;
    public final List<Image> images;

    public ZZVideo(String videoId) throws IOException {
        QueryHelper data = new QueryHelper("http://www.brazzers.com/scenes/view/id/" + videoId);

        id = new Id(videoId);

        date = data.selectText("#container > section.release-player.section-header.clearfix > header > div.more-scene-info > aside");

        title = data.selectText("#container > section.release-player.section-header.clearfix > header > div.scene-main-info > h1");
        description = data.selectText("#scene-description > p");
        category = data.selectText("#container > section.release-player.section-header.clearfix > header > div.more-scene-info > a > span.label-text");

        models = data.selectAll("#hd-model-carousel > li").stream()
            .map(ZZModel::new)
            .collect(Collectors.toList());

        tags = data.selectAll("#container > section.release-information > div > div.scene-tour-right > div.content-box.scene-fuck-stats > ul > li > div > a").stream()
            .map(ZZTag::new)
            .collect(Collectors.toList());

        images = getImages(data.select("#hdpic-carousel > li > a")).parallelStream()
            .map(Image::new)
            .collect(Collectors.toList());
    }

    private List<String> getImages(Element element) {
        List<String> images = new ArrayList<>();

        for (int imageId = 1; imageId <= 15; imageId++) {
            images.add(Pattern.compile("/([0-9]{2})\\.jpg$")
                .matcher(element.attr("href"))
                .replaceFirst(String.format("/%02d.jpg", imageId))
            );
        }

        return images;
    }

    @Override
    public String toString() {
        return String.format(
            "Title: %s%nDate: %s%nCategory: %s%nDescription: %s%nTags: %s%nModels:%s%nImages: %s%n",
            title, date, category, description, tags, models, images
        );
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", id.id);
        jsonObject.put("ref", id.ref);

        jsonObject.put("title", title);
        jsonObject.put("date", date);
        jsonObject.put("category", category);
        jsonObject.put("description", description);

        jsonObject.put("models", models.stream().map(ZZModel::toJSON).collect(Collectors.toList()));
        jsonObject.put("tags", tags.stream().map(ZZTag::toJSON).collect(Collectors.toList()));
        jsonObject.put("images", images.stream().map(Image::toJSON).collect(Collectors.toList()));

        return jsonObject;
    }
}