package org.eugene;

import org.apache.commons.cli.*;
import org.eugene.config.Config;
import org.springframework.boot.CommandLineRunner;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootCliRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("bigdata-file-viewer server launched, waiting for file viewing request");
    }
}
