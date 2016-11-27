package ru.linachan.mediator.collector.zz;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import ru.linachan.mediator.common.QueryHelper;
import ru.linachan.mediator.common.Id;

public class ZZTag {

    public final Id id;
    public final String title;
    public final String time;

    public ZZTag(Element element) {
        QueryHelper data = new QueryHelper(element);

        id = new Id(
            element.attr("href").replace("/categories/view/id/", "")
        );

        title = data.selectText("var");
        time = data.selectText("time");
    }

    @Override
    public String toString() {
        return title;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", id.id);
        jsonObject.put("ref", id.ref);

        jsonObject.put("name", title);
        jsonObject.put("time", time);

        return jsonObject;
    }
}
