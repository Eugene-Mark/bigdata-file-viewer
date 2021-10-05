package org.eugene;

import javafx.application.Application;
import org.apache.commons.cli.*;
import org.eugene.config.Config;

/**
 * Hello big data!
 *
 */
public class App
{
    public static void main(String[] args) {
        Options options = new Options();

        Option analytics = new Option("a","analytics",false,"Enable analytics");
        analytics.setRequired(false);
        options.addOption(analytics);

        CommandLineParser parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options,args);
        }catch(ParseException e){
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name",options);
            System.exit(1);
        }

        boolean enableAnalytics=(cmd.hasOption("analytics")||cmd.hasOption("a"));
        Config.getInstance().setAnalytics(enableAnalytics);
        Application.launch(FXApplication.class, args);
    }
}
