package proyectoDesarrollo.interfaz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    public static Properties loadFromResources(String fileName) throws IOException {
        Properties props = new Properties();
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) throw new FileNotFoundException(fileName + " no encontrado en resources");
            props.load(is);
        }
        return props;
    }

    public static Properties loadFromFile(String path) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        }
        return props;
    }
}