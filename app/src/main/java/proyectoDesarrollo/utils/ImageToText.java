package proyectoDesarrollo.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;



import javafx.scene.image.Image;

public class ImageToText {

    public static String imageFileToBase64(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        return bufferedImageToBase64(bufferedImage, "png");
    }

    public static String fxImageToBase64(Image image) throws IOException {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        return bufferedImageToBase64(bufferedImage, "png");
    }

    public static String bufferedImageToBase64(BufferedImage bufferedImage, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static Image base64ToFxImage(String base64) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage bufferedImage = ImageIO.read(bais);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }


    public static BufferedImage base64ToBufferedImage(String base64) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return ImageIO.read(bais);
    }
}
