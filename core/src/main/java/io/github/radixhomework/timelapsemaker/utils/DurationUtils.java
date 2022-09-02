package io.github.radixhomework.timelapsemaker.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DurationUtils {

    /**
     * Log with level {@code INFO} the duration in seconds
     * @param since time in nanoseconds
     */
    public static void logDurationInSeconds(long since) {
        log.info("Duration: {}s", (System.nanoTime() - since) / Math.pow(10, 9));
    }
}
