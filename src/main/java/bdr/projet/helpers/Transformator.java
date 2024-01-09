package bdr.projet.helpers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static bdr.projet.helpers.Constantes.URL_IMG_NOT_FOUND;

public class Transformator {
    public static Image internetUrlToImage(URL url, Image defaultImage){
        try {
            BufferedImage i = ImageIO.read(url);
            WritableImage im = new WritableImage(200, 132);
            im = SwingFXUtils.toFXImage(i, im);
            return im;
        } catch (IOException | NullPointerException e) {
            return defaultImage;
        }
    }
}
