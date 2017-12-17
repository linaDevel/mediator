package ru.linachan.mediator.collector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.linachan.mediator.common.QueryHelper;
import ru.linachan.mediator.common.data.Image;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DataEngine {

    private String url;
    private JSONObject fields;

    private DataEngine() {}

    public static DataEngine fromJSON(JSONObject providerData) {
        DataEngine engine = new DataEngine();

        JSONObject engineData = (JSONObject) providerData.get("data");

        engine.url = (String) engineData.get("url");
        engine.fields = (JSONObject) engineData.get("fields");

        return engine;
    }

    private String replaceIfRequired(JSONObject rule, String data) {
        if (rule.containsKey("replace")&&data != null) {
            for (Object replaceRule: (JSONArray) rule.get("replace")) {
                data = Pattern.compile((String) ((JSONArray) replaceRule).get(0)).matcher(data)
                    .replaceAll(Matcher.quoteReplacement((String) ((JSONArray) replaceRule).get(1)));
            }
        }

        return data;
    }

    @SuppressWarnings("unchecked")
    private Object tryGetValue(JSONObject rule, QueryHelper data) {
        String target = (String) rule.get("target");

        switch ((String) rule.get("type")) {
            case "text":
                return replaceIfRequired(rule, data.selectText(target));
            case "attr":
                return replaceIfRequired(rule, data.selectAttr(target, (String) rule.get("name")));
            case "image":
                JSONObject image = new JSONObject();
                Image imageData;

                if (rule.containsKey("name")) {
                    imageData = new Image(replaceIfRequired(
                        rule, data.selectAttr((String) rule.get("target"), (String) rule.get("name"))
                    ));
                } else {
                    imageData = new Image(replaceIfRequired(
                        rule, data.selectText((String) rule.get("target"))
                    ));
                }

                image.put("link", imageData.link);
                image.put("ratio", imageData.ratio);

                return image;
            case "image_list":
                JSONArray imageList = new JSONArray();
                String imageLink = replaceIfRequired(
                    rule, rule.containsKey("name") ? data.selectAttr((String) rule.get("target"), (String) rule.get("name")) : data.selectText((String) rule.get("target"))
                );

                for (long index = (Long) rule.get("indexStart"); index <= (Long) rule.get("indexEnd"); index++) {
                    JSONObject imageInfo = new JSONObject();
                    Image imageObj = new Image(replaceIfRequired(
                        rule, Pattern.compile((String) rule.get("index")).matcher(imageLink)
                            .replaceAll(String.format((String) rule.get("indexFormat"), index))
                    ));

                    imageInfo.put("link", imageObj.link);
                    imageInfo.put("ratio", imageObj.ratio);

                    imageList.add(imageInfo);
                }

                return imageList;
            case "list":
                JSONArray array = new JSONArray();
                JSONObject fields = (JSONObject) rule.get("fields");

                array.addAll(data.selectAll(target).parallelStream()
                    .map(element -> {
                        JSONObject item = new JSONObject();

                        for (Object key: fields.keySet()) {
                            item.put(key, tryGetValue((JSONObject) fields.get(key), new QueryHelper(element)));
                        }

                        return item;
                    })
                    .collect(Collectors.toList())
                );

                return array;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getData(String id) throws IOException {
        JSONObject jsonObject = new JSONObject();
        QueryHelper helper = new QueryHelper(String.format(url, id));

        for (Object key: fields.keySet()) {
            jsonObject.put(key, tryGetValue((JSONObject) fields.get(key), helper));
        }

        return jsonObject;
    }
}
