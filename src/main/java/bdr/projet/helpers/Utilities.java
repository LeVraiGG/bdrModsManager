package bdr.projet.helpers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.google.common.hash.Hashing;

public class Utilities {
    public static Image internetUrlToImage(URL url, Image defaultImage) {
        try {
            BufferedImage i = ImageIO.read(url);
            WritableImage im = new WritableImage(200, 132);
            im = SwingFXUtils.toFXImage(i, im);
            return im;
        } catch (IOException | NullPointerException e) {
            return defaultImage;
        }
    }

    public static String encryptSHA256(String s) {
        return Hashing.sha256().hashString(s, StandardCharsets.UTF_8).toString();
    }

    public static String getDate(TemporalAccessor date) {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(date == null ? LocalDateTime.now() : date);
    }

    public static String getNow() {
        return getDate(null);
    }
}
