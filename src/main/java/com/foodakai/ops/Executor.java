package com.foodakai.ops;

import com.foodakai.schema.FoodakaiBrandInfo;
import com.foodakai.schema.FoodakaiHazard;
import com.foodakai.schema.FoodakaiObject;
import com.foodakai.schema.FoodakaiSupplier;
import com.foodakai.utils.ConfVar;
import com.foodakai.utils.Method;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class Executor {

    public <T> T execute(Method method, String input){

        if(method.getAction().equals("input_directory")) {
            return fetch_csv_list(method.getDirectory());
        }

        return null;
    }

    public void process_csv_record(CSVRecord record, Object fdk_object) throws Exception
    {

        if(ConfVar.getInstance().getConfiguration().getOutput().getMethod().getAction().equals("directory")) {
            File file = new File(ConfVar.getInstance().getConfiguration().getOutput().getMethod().getDirectory());
            file.mkdirs();
        }

        if(ConfVar.getInstance().getConfiguration().getType().equals("notification")) {
            fdk_object = (FoodakaiObject) fdk_object;

            for (Map.Entry<String, Integer> entry : ConfVar.getInstance().getConfiguration().getMappings().entrySet()) {

                String value = extract_value(record, entry.getValue());

                switch(entry.getKey())
                {
                    case "id":
                        ((FoodakaiObject) fdk_object).details.id = value;
                        break;
                    case "supplier_name":
                        FoodakaiSupplier supplier = new FoodakaiSupplier();
                        supplier.name = value;
                        ((FoodakaiObject) fdk_object).suppliers.add(supplier);
                        break;
                    case "supplier_address":
                        supplier = ((FoodakaiObject) fdk_object).suppliers.get(0);
                        supplier.address = value;
                        ((FoodakaiObject) fdk_object).suppliers.remove(0);
                        ((FoodakaiObject) fdk_object).suppliers.add(supplier);
                        break;
                    case "supplier_city":
                        supplier = ((FoodakaiObject) fdk_object).suppliers.get(0);
                        supplier.city = value;
                        ((FoodakaiObject) fdk_object).suppliers.remove(0);
                        ((FoodakaiObject) fdk_object).suppliers.add(supplier);
                        break;
                    case "supplier_country":
                        supplier = ((FoodakaiObject) fdk_object).suppliers.get(0);
                        supplier.country = value;
                        ((FoodakaiObject) fdk_object).suppliers.remove(0);
                        ((FoodakaiObject) fdk_object).suppliers.add(supplier);
                        break;
                    case "notification_type":
                        ((FoodakaiObject) fdk_object).details.type = value;
                        break;
                    case "brand":
                        ((FoodakaiObject) fdk_object).brands.add(value);
                        break;
                    case "brand_distribution":
                        if(((FoodakaiObject) fdk_object).brands_info.size()>0)
                        {
                            ((FoodakaiObject) fdk_object).brands_info.get(0).brand_distribution = value;
                        }
                        else
                        {
                            FoodakaiBrandInfo bi = new FoodakaiBrandInfo();
                            bi.name = ((FoodakaiObject) fdk_object).brands.get(0);
                            bi.brand_distribution = value;
                            ((FoodakaiObject) fdk_object).brands_info.add(bi);
                        }
                        break;
                    case "brand_quantity":
                        if(((FoodakaiObject) fdk_object).brands_info.size()>0)
                        {
                            ((FoodakaiObject) fdk_object).brands_info.get(0).brand_quantity = value;
                        }
                        else
                        {
                            FoodakaiBrandInfo bi = new FoodakaiBrandInfo();
                            bi.name = ((FoodakaiObject) fdk_object).brands.get(0);
                            bi.brand_quantity = value;
                            ((FoodakaiObject) fdk_object).brands_info.add(bi);
                        }
                        break;
                    case "hazard":
                        FoodakaiHazard hazard = new FoodakaiHazard();
                        hazard.substance = value;
                        ((FoodakaiObject) fdk_object).hazards.add(hazard);
                        ((FoodakaiObject) fdk_object).potential_hazard = value;
                        break;
                    case "date":
                        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
                        try {
                            Date d = f.parse(value);
                            //long milliseconds = d.getTime();
                            ((FoodakaiObject) fdk_object).details.case_date = String.valueOf(d.toString());
                            //((FoodakaiObject) fdk_object).details.case_date = String.valueOf(value);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        break;
                }
            }

            /*
            * TODO:
            *       postprocessing tasks (eg. cleaning)
            * */

            ((FoodakaiObject) fdk_object).original_url = ConfVar.getInstance().getConfiguration().getUrl_pattern();
            if(ConfVar.getInstance().getConfiguration().getUrl_pattern().contains("{id}"))
            {
                ((FoodakaiObject) fdk_object).original_url = ((FoodakaiObject) fdk_object).original_url
                        .replace("{id}", ((FoodakaiObject) fdk_object).details.id);
            }

            if(ConfVar.getInstance().getConfiguration().getPost_process()!=null)
            {
                for (Map.Entry<String, Method> entry : ConfVar.getInstance().getConfiguration().getPost_process().entrySet()) {

                    String curr_value = "";

                    switch(entry.getKey())
                    {
                        case "type":
                            curr_value = ((FoodakaiObject) fdk_object).details.type;
                            break;
                        case "title":
                            curr_value = ((FoodakaiObject) fdk_object).details.title;
                            break;
                        case "id":
                            curr_value = ((FoodakaiObject) fdk_object).details.id;
                            break;
                        default:
                            break;
                    }

                    if(entry.getValue().getAction().equals("if_replace"))
                    {
                        for (Map.Entry<String, String> contains : entry.getValue().getContains().entrySet()) {

                            if(curr_value.toLowerCase().contains(contains.getKey())) {
                                curr_value = contains.getValue();
                                break;
                            }
                        }
                    }
                    if(entry.getValue().getAction().equals("concat"))
                    {
                        for(String p : entry.getValue().getParts())
                        {
                            try {
                                int i = Integer.parseInt(p);
                                curr_value += extract_value(record, i)+" ";
                            } catch(NumberFormatException e) {
                                curr_value += p+" ";
                            } catch(NullPointerException e) {
                            }
                        }
                        curr_value = curr_value.trim();
                    }

                    switch(entry.getKey())
                    {
                        case "type":
                            ((FoodakaiObject) fdk_object).details.type = curr_value;
                            break;
                        case "title":
                            ((FoodakaiObject) fdk_object).details.title = curr_value;
                            break;
                        default:
                            break;
                    }

                }
            }

            ((FoodakaiObject) fdk_object).details.id = ConfVar.getInstance().getConfiguration()
                    .getData_source().toUpperCase()+"_"+((FoodakaiObject) fdk_object).details.id;
            ((FoodakaiObject) fdk_object).write(ConfVar.getInstance().getConfiguration().getOutput().getMethod().getDirectory());
        }


    }

    private <T> T fetch_csv_list(String input)
    {
        ArrayList<String> matches = new ArrayList<String>();

        File folder = new File(input);
        //File[] listOfFiles = folder.listFiles();

        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".csv");
            }
        });

        for (File f : listOfFiles)
            matches.add(f.getAbsolutePath());

        return (T) matches;

    }

    private String extract_value(CSVRecord record, Integer column_number)
    {
        return (String)record.get(column_number);
    }

}












