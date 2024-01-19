package bdr.projet.helpers;

import bdr.projet.beans.Comment;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.common.hash.Hashing;

public class Transformator {
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

    public static ArrayList<String> objectsToString(ArrayList<?> objects) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < objects.size(); ++i) res.add(objects.toString());
        return res;
    }
}
