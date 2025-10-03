package org.eugene;

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

    }
}
