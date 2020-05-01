package radix.home.timelapsemaker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Comparator;

@Slf4j
@Configuration
public class ImageComparator {

    @Bean
    public Comparator<File> byFileName() {
        return Comparator.comparing(File::getName);
    }
}
