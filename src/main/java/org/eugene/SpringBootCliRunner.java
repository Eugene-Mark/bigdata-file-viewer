package org.eugene;

import javafx.application.Application;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.eugene.config.AnalyticsSettings;
import org.springframework.boot.CommandLineRunner;

@Slf4j
public class SpringBootCliRunner implements CommandLineRunner {
  @Override
  public void run(String... args) {
	Options options = new Options();

	Option analytics = new Option("a", "analytics", false, "Enable analytics");
	analytics.setRequired(false);
	options.addOption(analytics);

	CommandLineParser parser = new PosixParser();
	HelpFormatter formatter = new HelpFormatter();
	CommandLine cmd = null;

	try {
	  cmd = parser.parse(options, args);
	} catch (ParseException e) {
	  log.info(e.getMessage());
	  formatter.printHelp("utility-name", options);
	  System.exit(1);
	}

	boolean enableAnalytics = (cmd.hasOption("analytics") || cmd.hasOption("a"));
	AnalyticsSettings.getInstance().setAnalyticsEnabled(enableAnalytics);
	Application.launch(FXApplication.class, args);
  }
}
