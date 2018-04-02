package com.foodakai;


import com.foodakai.utils.ConfVar;
import com.foodakai.utils.Configuration;

public class ConvertCSV2XML {

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println("Usage: <file.yml>");
            System.exit(0);
        }

        ConfVar conf=ConfVar.getInstance();
        conf.loadConfiguration(args[0]);

        Configuration config = conf.getConfiguration();


        System.exit(1);
    }

}



































