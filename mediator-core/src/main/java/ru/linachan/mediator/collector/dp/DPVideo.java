package ru.linachan.mediator.collector.dp;

import org.json.simple.JSONObject;
import ru.linachan.mediator.common.Id;
import ru.linachan.mediator.common.Image;
import ru.linachan.mediator.common.QueryHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DPVideo {

    public final Id id;

    public final String date;

    public final String title;
    public final String description;

    public final List<DPModel> models;
    public final Image poster;

    public DPVideo(String videoId) throws IOException {
        QueryHelper data = new QueryHelper("http://www.digitalplayground.com/movies/info/" + videoId);

        id = new Id(videoId);

        date = data.selectText("#container > section:nth-child(1) > div > aside > div.info-right-side > ul > li:nth-child(1) > span");

        title = data.selectText("#container > section:nth-child(1) > div > header > h1");
        description = data.selectText("#scrollbar1 > div.viewport > div > p");

        models = data.selectAll("#container > section:nth-child(1) > div > aside > div.info-right-side > div > article > div").stream()
            .map(DPModel::new)
            .collect(Collectors.toList());

        poster = new Image(data.selectAttr("#front-cover", "src"));
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", id.id);
        jsonObject.put("ref", id.ref);

        jsonObject.put("title", title);
        jsonObject.put("date", date);
        jsonObject.put("description", description);

        jsonObject.put("models", models.stream().map(DPModel::toJSON).collect(Collectors.toList()));
        jsonObject.put("poster", poster.toJSON());

        return jsonObject;
    }
}
