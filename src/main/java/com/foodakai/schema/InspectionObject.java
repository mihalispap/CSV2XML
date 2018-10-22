package com.foodakai.schema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.foodakai.utils.Annotation;

public class InspectionObject {

	/*
<?xml version="1.0" encoding="UTF-8"?>
<inspection>
	<title></title>
	<inspection_date></inspection_date>
	<id></id>
	<description></description>
	<language></language>
	<original_source></original_source>
	<inspection_result></inspection_result>
	<inspection_action></inspection_action>
	<supplier>
		<name></name>
		<address></address>
		<country></country>
	</supplier>
	<supplier_info>
		<name></name>
		<level></level>
	</supplier_info>
</inspection>
	 * */

	public ArrayList<FoodakaiSupplier> suppliers = new ArrayList<FoodakaiSupplier>();
	public String language = "en";
	public String original_url= "";
	public ArrayList<FoodakaiSupplierInfo> suppliers_info = new ArrayList<FoodakaiSupplierInfo>();
	public String title = "";
	public String inspection_date="";
	public String id="";
	public String description="";
	public ArrayList<String> inspection_result = new ArrayList<String>();
	public String inspection_action="";
	public String issue_resolved="";
    public ArrayList<FoodakaiHazard> hazards = new ArrayList();
    public String product="";
    public String potential_origin="";
    public String origin="";

    public InspectionObject(){}
    
	private void writeSupplierInfo(PrintWriter writer)
	{
		for(int i=0;i<suppliers_info.size();i++)
		{
			String line="<supplier_info>";
			writer.print(line);
			
				line="<name><![CDATA["+suppliers_info.get(i).name+"]]></name>";
				line+="<level><![CDATA["+suppliers_info.get(i).level+"]]></level>";
			
			line+="</supplier_info>";
			writer.print(line);
		}
	}
	
	private void writeSuppliers(PrintWriter writer)
	{
		for(int i=0;i<suppliers.size();i++)
		{
			String line="<supplier>";
			writer.print(line);
			
				line="<name><![CDATA["+suppliers.get(i).name.trim()+"]]></name>";
				line+="<address><![CDATA["+suppliers.get(i).address.trim()+"]]></address>";
				line+="<country><![CDATA["+suppliers.get(i).country.trim()+"]]></country>";
			
			line+="</supplier>";
			writer.print(line);
			
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

	
	public void write(String output_directory) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(output_directory+File.separator+
						this.id+".xml", "UTF-8");
		
		String line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		line+="<inspection>";
		writer.print(line);


		line="<title><![CDATA["+title.trim()+"]]></title>";
		writer.print(line);
		
		line="<issue_resolved><![CDATA["+issue_resolved+"]]></issue_resolved>";
		writer.print(line);

		line="<inspection_date><![CDATA["+inspection_date+"]]></inspection_date>";
		writer.print(line);
		
		line="<id><![CDATA["+id.trim()+"]]></id>";
		writer.print(line);

		if(!origin.isEmpty())
		{
			line="<origin><![CDATA["+origin.trim()+"]]></origin>";
			writer.print(line);
		}
		
		line="<description><![CDATA["+description+"]]></description>";
		writer.print(line);

		for(int i=0;i<inspection_result.size();i++)
		{
			line="<inspection_result><![CDATA["+inspection_result.get(i).trim()+"]]></inspection_result>";
			writer.print(line);
		}
		
		line="<inspection_action><![CDATA["+inspection_action+"]]></inspection_action>";
		writer.print(line);

		line="<language><![CDATA["+language.trim()+"]]></language>";
		writer.print(line);
		
		line="<original_source><![CDATA["+original_url+"]]></original_source>";
		writer.print(line);

		line="<product><![CDATA["+product+"]]></product>";
		writer.print(line);
		
		writeSuppliers(writer);
		writeSupplierInfo(writer);
		writeHazards(writer);

		line="</inspection>";
		writer.print(line);
		writer.close();
		
	}

	public InspectionObject(String path, String filename) {
        try {
            Node n;
            File fXmlFile = new File(String.valueOf(path) + File.separator + filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("inspection");
            Element element = (Element)nList.item(0);
            
            try {
            	this.title = element.getElementsByTagName("title").item(0).getTextContent().trim();
            }
            catch (Exception e) {
            	this.title = "";
            }
            
            try {
            	this.issue_resolved = element.getElementsByTagName("issue_resolved").item(0).getTextContent().trim();
            }
            catch (Exception e) {
            	this.issue_resolved = "";
            }
            
            try {
            	this.inspection_date = element.getElementsByTagName("inspection_date").item(0).getTextContent().trim();
            }
            catch (Exception e) {
            	this.inspection_date = "";
            }
            
            try {
            	this.description = element.getElementsByTagName("description").item(0).getTextContent().trim();
            }
            catch (Exception e) {
            	this.description = "";
            }
            
            try {
            	this.product = element.getElementsByTagName("product").item(0).getTextContent().trim();
            }
            catch (Exception e) {
            	this.product = "";
            }
            
            try {
            	this.language = element.getElementsByTagName("language").item(0).getTextContent().trim();
            }
            catch (Exception e) {
            	this.language = "";
            }
            
            try {
            	this.id = element.getElementsByTagName("id").item(0).getTextContent().trim();
            }
            catch (Exception e) {
            	this.id = "";
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
                this.inspection_action = element.getElementsByTagName("inspection_action").item(0).getTextContent().trim();
            }
            catch (Exception e) {
                this.inspection_action = "";
            }
            
            int i = 0;
            while (i < element.getElementsByTagName("inspection_result").getLength()) {
                this.inspection_result.add(element.getElementsByTagName("inspection_result").item(i).getTextContent().trim());
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
        catch(Exception e){}
        
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
        
    	if(this.product.equals(product))
    		return true;
        return false;
    }
    
    public void addProductAnnotation(Annotation annotation)
    {
        this.product=annotation.value;
    }

	
}



