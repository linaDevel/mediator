package ru.linachan.mediator.common.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Image {

    public final String link;
    public final double ratio;

    public Image(String image) {
        link = image;
        ratio = getAspectRatio();
    }

    private double getAspectRatio() {
        try {
            BufferedImage image = ImageIO.read(new URL(link));
            return (double) image.getWidth() / (double) image.getHeight();
        } catch (IOException e) {
            return 0f;
        }
    }

    @Override
    public String toString() {
        return String.format("IMG(%s)=1:%f", link, ratio);
    }
}
