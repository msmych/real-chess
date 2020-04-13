package matvey.realchess.console;

import java.io.IOException;
import java.util.Properties;

class Props {

    private final Properties properties = new Properties();

    Props(String file) {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return properties.get(key).toString();
    }
}
