package ru.linachan.mediator.common;

import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Image {

    public final String source;
    public final double ratio;

    public Image(String image) {
        source = image;
        ratio = getAspectRatio();
    }

    private double getAspectRatio() {
        try {
            BufferedImage image = ImageIO.read(new URL(source));
            return (double) image.getWidth() / (double) image.getHeight();
        } catch (IOException e) {
            return 0f;
        }
    }

    @Override
    public String toString() {
        return String.format("IMG(%s)=1:%f", source, ratio);
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("link", source);
        jsonObject.put("ratio", ratio);

        return jsonObject;
    }
}
