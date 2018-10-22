package com.foodakai;


import com.foodakai.ops.Executor;
import com.foodakai.schema.FoodakaiObject;
import com.foodakai.utils.ConfVar;
import com.foodakai.utils.Configuration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConvertCSV2XML {

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println("Usage: <file.yml>");
            System.exit(0);
        }

        ConfVar conf=ConfVar.getInstance();
        conf.loadConfiguration(args[0]);

        Configuration config = conf.getConfiguration();

        Executor executor = new Executor();

        ArrayList<String> files = executor.execute(config.getCsvs().getMethod(), "");

        for(String f : files)
        {
            File csvData = new File(f);
            CSVParser csvFileParser = CSVFormat.DEFAULT.parse(new FileReader(new File(f)));

            int no_recs=0;
            //CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180);
            for (CSVRecord csvRecord : csvFileParser) {
                if(config.isHas_header() && no_recs == 0)
                {
                    no_recs++;
                    continue;
                }

                Object obj = null;

                if(config.getType().equals("notification"))
                    obj = new FoodakaiObject();
                executor.process_csv_record(csvRecord, obj);
            }
        }

        System.exit(1);
    }

}



































