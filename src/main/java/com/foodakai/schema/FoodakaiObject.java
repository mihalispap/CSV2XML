package com.foodakai.schema;

import com.foodakai.schema.FoodakaiAddition;
import com.foodakai.schema.FoodakaiDetails;
import com.foodakai.schema.FoodakaiFlagged;
import com.foodakai.schema.FoodakaiHazard;
import com.foodakai.schema.FoodakaiSupplier;
import com.foodakai.schema.FoodakaiSupplierInfo;
import com.foodakai.utils.Annotation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FoodakaiObject {
    public FoodakaiDetails details = new FoodakaiDetails();
    public ArrayList<FoodakaiAddition> additions = new ArrayList();
    public ArrayList<FoodakaiHazard> hazards = new ArrayList();
    public ArrayList<FoodakaiFlagged> flagged = new ArrayList();
    public ArrayList<FoodakaiSupplier> suppliers = new ArrayList();
    public String language = "en";
    public String original_url = "";
    public ArrayList<String> brands = new ArrayList();
    public String potential_hazard = "";
    public String potential_origin = "";
    public String related_to = "";
    public ArrayList<FoodakaiBrandInfo> brands_info = new ArrayList();
    public ArrayList<FoodakaiSupplierInfo> suppliers_info = new ArrayList();
    public String data_source="";

    private boolean shouldProcess(String name, String[] meta_elements) {
        if (ArrayUtils.contains((Object[])meta_elements, (Object)name)) {
            return true;
        }
        return false;
    }

    public void addOnMeta(String name, String value, String[] meta_elements, String source) {
        if (!this.shouldProcess(name, meta_elements)) {
            return;
        }
        if (source.equals("fda")) {
            this.processFDA(name, value);
        }
        if (source.equals("fsa")) {
            this.processFSA(name, value);
        }
    }

    private void writeSupplierInfo(PrintWriter writer) {
        int i = 0;
        while (i < this.suppliers_info.size()) {
            String line = "<supplier_info>";
            writer.print(line);
            line = "<name><![CDATA[" + this.suppliers_info.get((int)i).name + "]]></name>";
            line = String.valueOf(line) + "<level><![CDATA[" + this.suppliers_info.get((int)i).level + "]]></level>";
            line = String.valueOf(line) + "</supplier_info>";
            writer.print(line);
            ++i;
        }
    }

    private void writeBrandInfo(PrintWriter writer) {
        int i = 0;
        while (i < this.brands_info.size()) {
            String line = "<brand_info>";
            writer.print(line);
            line = "<name><![CDATA[" + this.brands_info.get((int)i).name + "]]></name>";
            line = String.valueOf(line) + "<brand_quantity><![CDATA[" + this.brands_info.get((int)i).brand_quantity + "]]></brand_quantity>";
            line = String.valueOf(line) + "<brand_distribution><![CDATA[" + this.brands_info.get((int)i).brand_distribution + "]]></brand_distribution>";
            line = String.valueOf(line) + "</brand_info>";
            writer.print(line);
            ++i;
        }
    }

    private void writeAdditions(PrintWriter writer) {
        int i = 0;
        while (i < this.additions.size()) {
            String line = "<addition>";
            writer.print(line);
            line = "<addition_value><![CDATA[" + this.additions.get((int)i).addition_value + "]]></addition_value>";
            line = String.valueOf(line) + "<addition_country><![CDATA[" + this.additions.get((int)i).addition_country + "]]></addition_country>";
            line = String.valueOf(line) + "<addition_date><![CDATA[" + this.additions.get((int)i).addition_date + "]]></addition_date>";
            line = String.valueOf(line) + "<addition_followup><![CDATA[" + this.additions.get((int)i).addition_followup + "]]></addition_followup>";
            line = String.valueOf(line) + "<addition_comments><![CDATA[" + this.additions.get((int)i).addition_comments + "]]></addition_comments>";
            line = String.valueOf(line) + "</addition>";
            writer.print(line);
            ++i;
        }
    }

    private void writeHazards(PrintWriter writer) {
        int i = 0;
        while (i < this.hazards.size()) {
            String line = "<hazard>";
            writer.print(line);
            line = "<substance><![CDATA[" + this.hazards.get((int)i).substance + "]]></substance>";
            line = String.valueOf(line) + "<category><![CDATA[" + this.hazards.get((int)i).category + "]]></category>";
            line = String.valueOf(line) + "<analytical_result><![CDATA[" + this.hazards.get((int)i).analytical_result + "]]></analytical_result>";
            line = String.valueOf(line) + "<units><![CDATA[" + this.hazards.get((int)i).units + "]]></units>";
            line = String.valueOf(line) + "<sampling_date><![CDATA[" + this.hazards.get((int)i).sampling_date + "]]></sampling_date>";
            if (!this.hazards.get((int)i).score.isEmpty()) {
                line = String.valueOf(line) + "<score><![CDATA[" + this.hazards.get((int)i).score + "]]></score>";
                line = String.valueOf(line) + "<reason><![CDATA[" + this.hazards.get((int)i).reason + "]]></reason>";
                line = String.valueOf(line) + "<timestamp><![CDATA[" + this.hazards.get((int)i).timestamp + "]]></timestamp>";
            }
            line = String.valueOf(line) + "</hazard>";
            writer.print(line);
            ++i;
        }
    }

    private void writeFlaggeds(PrintWriter writer) {
        int i = 0;
        while (i < this.flagged.size()) {
            String line = "<flagged>";
            writer.print(line);
            line = "<country><![CDATA[" + this.flagged.get((int)i).country + "]]></country>";
            line = String.valueOf(line) + "<distribution><![CDATA[" + this.flagged.get((int)i).distribution + "]]></distribution>";
            line = String.valueOf(line) + "<origin><![CDATA[" + this.flagged.get((int)i).origin + "]]></origin>";
            line = String.valueOf(line) + "<via><![CDATA[" + this.flagged.get((int)i).via + "]]></via>";
            line = String.valueOf(line) + "<flag><![CDATA[" + this.flagged.get((int)i).flag + "]]></flag>";
            line = String.valueOf(line) + "<last_updated><![CDATA[" + this.flagged.get((int)i).last_updated + "]]></last_updated>";
            line = String.valueOf(line) + "<published><![CDATA[" + this.flagged.get((int)i).published + "]]></published>";
            line = String.valueOf(line) + "</flagged>";
            writer.print(line);
            ++i;
        }
    }

    private void writeSuppliers(PrintWriter writer) {
        int i = 0;
        while (i < this.suppliers.size()) {
            String line = "<supplier>";
            writer.print(line);
            line = "<name><![CDATA[" + this.suppliers.get((int)i).name + "]]></name>";
            line = String.valueOf(line) + "<address><![CDATA[" + this.suppliers.get((int)i).address + "]]></address>";
            line = String.valueOf(line) + "<country><![CDATA[" + this.suppliers.get((int)i).country + "]]></country>";
            line = String.valueOf(line) + "<city><![CDATA[" + this.suppliers.get((int)i).city + "]]></city>";
            line = String.valueOf(line) + "</supplier>";
            writer.print(line);
            ++i;
        }
    }

    private void writeDetails(PrintWriter writer) {
        String line = "<details>";
        writer.print(line);
        line = "<title><![CDATA[" + this.details.title + "]]></title>";
        line = String.valueOf(line) + "<id><![CDATA[" + this.details.id + "]]></id>";
        line = String.valueOf(line) + "<description><![CDATA[" + this.details.description + "]]></description>";
        line = String.valueOf(line) + "<case_date><![CDATA[" + this.details.case_date + "]]></case_date>";
        line = String.valueOf(line) + "<update_date><![CDATA[" + this.details.update_date + "]]></update_date>";
        line = String.valueOf(line) + "<type><![CDATA[" + this.details.type + "]]></type>";
        line = String.valueOf(line) + "<action><![CDATA[" + this.details.action + "]]></action>";
        line = String.valueOf(line) + "<case_from><![CDATA[" + this.details.case_from + "]]></case_from>";
        line = String.valueOf(line) + "<distribution_status><![CDATA[" + this.details.distribution_status + "]]></distribution_status>";
        line = String.valueOf(line) + "<product><![CDATA[" + this.details.product + "]]></product>";
        line = String.valueOf(line) + "<product_category><![CDATA[" + this.details.product_category + "]]></product_category>";
        line = String.valueOf(line) + "<risk><![CDATA[" + this.details.risk + "]]></risk>";
        line = String.valueOf(line) + "</details>";
        writer.print(line);
    }

    public void write(String output_directory) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(String.valueOf(output_directory) + File.separator + this.details.id + ".xml", "UTF-8");
        String line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        line = String.valueOf(line) + "<notification>";
        writer.print(line);
        this.writeDetails(writer);
        this.writeAdditions(writer);
        this.writeHazards(writer);
        this.writeFlaggeds(writer);
        this.writeSuppliers(writer);
        this.writeSupplierInfo(writer);
        this.writeBrandInfo(writer);
        line = "<language><![CDATA[" + this.language + "]]></language>";
        writer.print(line);
        line = "<original_source><![CDATA[" + this.original_url + "]]></original_source>";
        writer.print(line);
        int i = 0;
        while (i < this.brands.size()) {
            line = "<brand><![CDATA[" + this.brands.get(i) + "]]></brand>";
            writer.print(line);
            ++i;
        }
        if (!this.potential_hazard.isEmpty() && !this.potential_hazard.equals("")) {
            line = "<potential_hazard><![CDATA[" + this.potential_hazard + "]]></potential_hazard>";
            writer.print(line);
        }
        if (!this.related_to.isEmpty() && !this.related_to.equals("")) {
            line = "<related_to><![CDATA[" + this.related_to + "]]></related_to>";
            writer.print(line);
        }
        line = "</notification>";
        writer.print(line);
        writer.close();
    }

    private void processFSA(String name, String value) {
        if (name.equals("twitter:title")) {
            this.details.title = value;
        } else if (name.equals("description")) {
            this.details.description = value;
        }
    }

    private void processFDA(String name, String value) {
        if (name.equals("dc.title")) {
            this.details.title = value;
        } else if (name.equals("dc.description")) {
            this.details.description = value;
        } else if (name.equals("dc.language")) {
            this.language = value;
        } else if (name.equals("posted")) {
            this.details.case_date = value;
        } else if (name.equals("index_date")) {
            this.details.update_date = value;
        } else if (name.equals("product_description")) {
            this.details.product_category = value;
        } else if (name.equals("recall_desc")) {
            FoodakaiHazard hazard = new FoodakaiHazard();
            hazard.substance = value;
            this.hazards.add(hazard);
        } else if (name.equals("company_name")) {
            FoodakaiSupplier supplier = new FoodakaiSupplier();
            supplier.name = value;
            this.suppliers.add(supplier);
        } else if (name.equals("recall_category")) {
            this.details.type = String.valueOf(this.details.type) + " - " + value;
        } else if (name.equals("dc.subject")) {
            this.details.type = String.valueOf(this.details.type) + " - " + value.replace(", ", " - ");
        } else if (name.equals("brand_name")) {
            this.brands.add(value);
        }
    }

    public FoodakaiObject() {
    }

    public FoodakaiObject(String path, String filename) {
        try {
            Node n;
            File fXmlFile = new File(String.valueOf(path) + File.separator + filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("details");
            Element element = (Element)nList.item(0);
            try {
                this.details.title = element.getElementsByTagName("title").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.title = "";
            }
            try {
                this.details.id = element.getElementsByTagName("id").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.id = "";
            }
            try {
                this.details.action = element.getElementsByTagName("action").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.action = "";
            }
            try {
                this.details.case_date = element.getElementsByTagName("case_date").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.case_date = "";
            }
            try {
                this.details.case_from = element.getElementsByTagName("case_from").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.case_from = "";
            }
            try {
                this.details.description = element.getElementsByTagName("description").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.description = "";
            }
            try {
                this.details.product = element.getElementsByTagName("product").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.product = "";
            }
            try {
                this.details.product_category = element.getElementsByTagName("product_category").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.product_category = "";
            }
            try {
                this.details.risk = element.getElementsByTagName("risk").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.risk = "";
            }
            try {
                this.details.type = element.getElementsByTagName("type").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.type = "";
            }
            try {
                this.details.update_date = element.getElementsByTagName("update_date").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.update_date = "";
            }
            try {
                this.details.distribution_status = element.getElementsByTagName("distribution_status").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.details.distribution_status = "";
            }
            nList = doc.getElementsByTagName("notification");
            element = (Element)nList.item(0);
            try {
                this.language = element.getElementsByTagName("language").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.language = "";
            }
            try {
                this.potential_hazard = element.getElementsByTagName("potential_hazard").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.potential_hazard = "";
            }
            try {
                this.original_url = element.getElementsByTagName("original_source").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.original_url = "";
            }
            try {
                this.potential_origin = element.getElementsByTagName("potential_origin").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.potential_origin = "";
            }
            try {
                this.related_to = element.getElementsByTagName("related_to").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.related_to = "";
            }
            nList = doc.getElementsByTagName("notification");
            element = (Element)nList.item(0);
            int i = 0;
            while (i < element.getElementsByTagName("brand").getLength()) {
                this.brands.add(element.getElementsByTagName("brand").item(i).getTextContent().trim());
                ++i;
            }
            i = 0;
            while (i < element.getElementsByTagName("supplier").getLength()) {
                FoodakaiSupplier supplier = new FoodakaiSupplier();
                n = element.getElementsByTagName("supplier").item(i);
                try {
                    supplier.name = ((Element)n).getElementsByTagName("name").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    supplier.name = "";
                }
                try {
                    supplier.address = ((Element)n).getElementsByTagName("address").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    supplier.address = "";
                }
                try {
                    supplier.city = ((Element)n).getElementsByTagName("city").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    supplier.city = "";
                }
                try {
                    supplier.country = ((Element)n).getElementsByTagName("country").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    supplier.country = "";
                }
                this.suppliers.add(supplier);
                ++i;
            }
            i = 0;
            while (i < element.getElementsByTagName("supplier_info").getLength()) {
                FoodakaiSupplierInfo supplierinfo = new FoodakaiSupplierInfo();
                n = element.getElementsByTagName("supplier_info").item(i);
                try {
                    supplierinfo.name = ((Element)n).getElementsByTagName("name").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    supplierinfo.name = "";
                }
                try {
                    supplierinfo.level = ((Element)n).getElementsByTagName("level").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    supplierinfo.level = "";
                }
                this.suppliers_info.add(supplierinfo);
                ++i;
            }
            i = 0;
            while (i < element.getElementsByTagName("addition").getLength()) {
                FoodakaiAddition addition = new FoodakaiAddition();
                n = element.getElementsByTagName("addition").item(i);
                try {
                    addition.addition_value = ((Element)n).getElementsByTagName("addition_value").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    addition.addition_value = "";
                }
                try {
                    addition.addition_country = ((Element)n).getElementsByTagName("addition_country").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    addition.addition_country = "";
                }
                try {
                    addition.addition_date = ((Element)n).getElementsByTagName("addition_date").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    addition.addition_date = "";
                }
                try {
                    addition.addition_followup = ((Element)n).getElementsByTagName("addition_followup").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    addition.addition_followup = "";
                }
                try {
                    addition.addition_comments = ((Element)n).getElementsByTagName("addition_comments").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    addition.addition_comments = "";
                }
                this.additions.add(addition);
                ++i;
            }
            i = 0;
            while (i < element.getElementsByTagName("brand_info").getLength()) {
                FoodakaiBrandInfo brandinfo = new FoodakaiBrandInfo();
                n = element.getElementsByTagName("brand_info").item(i);
                try {
                    brandinfo.name = ((Element)n).getElementsByTagName("name").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    brandinfo.name = "";
                }
                try {
                    brandinfo.brand_distribution = ((Element)n).getElementsByTagName("brand_distribution").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    brandinfo.brand_distribution = "";
                }
                try {
                    brandinfo.brand_quantity = ((Element)n).getElementsByTagName("brand_quantity").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    brandinfo.brand_quantity = "";
                }
                this.brands_info.add(brandinfo);
                ++i;
            }
            i = 0;
            while (i < element.getElementsByTagName("flagged").getLength()) {
                FoodakaiFlagged fl = new FoodakaiFlagged();
                n = element.getElementsByTagName("flagged").item(i);
                try {
                    fl.country = ((Element)n).getElementsByTagName("country").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    fl.country = "";
                }
                try {
                    fl.distribution = ((Element)n).getElementsByTagName("distribution").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    fl.distribution = "";
                }
                try {
                    fl.origin = ((Element)n).getElementsByTagName("origin").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    fl.origin = "";
                }
                try {
                    fl.via = ((Element)n).getElementsByTagName("via").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    fl.via = "";
                }
                try {
                    fl.flag = ((Element)n).getElementsByTagName("flag").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    fl.flag = "";
                }
                try {
                    fl.last_updated = ((Element)n).getElementsByTagName("last_updated").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    fl.last_updated = "";
                }
                try {
                    fl.published = ((Element)n).getElementsByTagName("published").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    fl.published = "";
                }
                this.flagged.add(fl);
                ++i;
            }
            i = 0;
            while (i < element.getElementsByTagName("hazard").getLength()) {
                FoodakaiHazard hazard = new FoodakaiHazard();
                n = element.getElementsByTagName("hazard").item(i);
                try {
                    hazard.substance = ((Element)n).getElementsByTagName("substance").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    hazard.substance = "";
                }
                try {
                    hazard.category = ((Element)n).getElementsByTagName("category").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    hazard.category = "";
                }
                try {
                    hazard.analytical_result = ((Element)n).getElementsByTagName("analytical_result").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    hazard.analytical_result = "";
                }
                try {
                    hazard.units = ((Element)n).getElementsByTagName("units").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    hazard.units = "";
                }
                try {
                    hazard.sampling_date = ((Element)n).getElementsByTagName("sampling_date").item(0).getTextContent().trim();
                }
                catch (Exception e) {
                    hazard.sampling_date = "";
                }
                this.hazards.add(hazard);
                ++i;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasHazard(String hazard) {
        int i = 0;
        while (i < this.hazards.size()) {
            if (this.hazards.get((int)i).substance.equals(hazard)) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public void addAnnotation(Annotation annotation) {
        FoodakaiHazard hazard = new FoodakaiHazard();
        hazard.substance = annotation.value;
        hazard.score = String.valueOf(annotation.score);
        hazard.reason = annotation.reason;
        hazard.timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        this.hazards.add(hazard);
    }

    public boolean hasProduct(String product) {

        if(this.details.product.equals(product))
            return true;
        return false;
    }

    public void addProductAnnotation(Annotation annotation)
    {
        this.details.product=annotation.value;
    }


}

