package org.eugene.config;

public class Config{
    private Config(){
    }

    private static Config instance;
    private boolean statistics;

    public static Config getInstance(){
        if(instance==null){
            instance = new Config();
        }
        return instance;
    }

    public boolean enableAnalytics(){
        return statistics;
    }

    public void setAnalytics(boolean statistics){
        this.statistics = statistics;
    }
}
