package org.eugene;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Hello big data!
 *
 */
public class App
{
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SpringBootCliRunner.class);
        builder.headless(false).run(args);
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s%n");
    }
}
