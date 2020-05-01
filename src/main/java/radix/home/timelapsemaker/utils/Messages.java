package radix.home.timelapsemaker.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Component
public class Messages {

    private Properties properties = new Properties();

    public Messages() {
        try (InputStream is = this.getClass().getResourceAsStream("/messages.properties")) {
            properties.load(is);
        } catch (Exception e) {
            log.error("messages.properties file not found", e);
        }
    }

    public String getValue(String key) {
        log.debug(String.format("Getting property with key: %s", key));
        return properties.getProperty(key);
    }
}
