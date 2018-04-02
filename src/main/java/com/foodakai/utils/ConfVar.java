package com.foodakai.utils;


import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class ConfVar {

    private static volatile ConfVar instance = null;

    private ConfVar() {}

    public static ConfVar getInstance() {
        if (instance == null) {
            synchronized(ConfVar.class) {
                if (instance == null) {
                    instance = new ConfVar();
                }
            }
        }
        return instance;
    }

    private String conf_file;
    private Configuration configuration;

    public String getConf_file() {
        return conf_file;
    }

    public void setConf_file(String conf_file) {
        this.conf_file = conf_file;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void loadConfiguration(String yaml_file){

        this.conf_file=yaml_file;

        Yaml yaml = new Yaml();
        try( InputStream in = Files.newInputStream( Paths.get( this.getConf_file() ) ) ) {
            configuration = yaml.loadAs( in, Configuration.class );
            System.out.println( configuration.toString() );
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
