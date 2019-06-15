package nl.java_etl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Resources {
    public static Properties loadProperties(File file) throws IOException {
        try (
                InputStream is = new FileInputStream(file);
                ){
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        }
    }

    public static String load(Object o, String file) throws IOException {
        Class<?> clazz = o.getClass();
        URL url = clazz.getResource(file);
        if (url == null) {
            throw new FileNotFoundException(clazz.getPackage().getName() + "/" + file);
        }
        Path path = Paths.get(url.getPath());
        try (
                Stream<String> lines = Files.lines(path);
                ) {
            String query = lines.collect(Collectors.joining("\n"));
            lines.close();
            return query;
        }
    }

}
