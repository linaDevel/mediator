package ru.linachan.mediator.collector.zz;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import ru.linachan.mediator.common.QueryHelper;
import ru.linachan.mediator.common.Id;

public class ZZModel {

    public final Id id;
    public final String name;
    public final String image;

    public ZZModel(Element element) {
        QueryHelper data = new QueryHelper(element);

        id = new Id(
            data.selectAttr("li > div.model-card-wrap > div.model-card > div.card-image > a", "href")
                .replace("/profile/view/id/", "")
        );

        name = data.selectText("li > div.model-card-wrap > div.model-card > h2.model-card-title > a");
        image = data.selectAttr("li > div > div.model-card > div > a > img", "src").replace("model-medium", "model-large");
    }

    @Override
    public String toString() {
        return name;
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
