package ru.linachan.mediator.collector.dp;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import ru.linachan.mediator.common.Id;
import ru.linachan.mediator.common.QueryHelper;

public class DPModel {

    public final Id id;

    public final String name;
    public final String image;

    public DPModel(Element element) {
        QueryHelper data = new QueryHelper(element);

        id = new Id(
            data.selectAttr("div.preview-image > a", "href").replace("/model/", "")
        );

        name = data.selectText("div.title-bar > div.title-text > h4 > a");
        image = data.selectAttr("div.preview-image > a > img", "src").replace("135x182_1", "300x404_1");
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", id.id);
        jsonObject.put("ref", id.ref);

        jsonObject.put("name", name);
        jsonObject.put("image", image);

        return jsonObject;
    }
}
