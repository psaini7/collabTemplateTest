package com.virtubuild.services.abbeconf.compare;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BomCompareCustomExporter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( BomCompareCustomExporter.class );
	String id = "";
	String value = "";
	String quantity = "";
	String artNo = "";
	String colNo = "";

	HashMap<String, HashMap<String, String>> mapOfMap = new HashMap<String, HashMap<String, String>>();

	List<Part> listOfLists = new ArrayList<>();

	public List<Part> prepareData(String exporterXml) {

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(exporterXml.getBytes("UTF-8"));
			Document doc = db.parse(is);

			NodeList rowNodes = doc.getElementsByTagName("row");

			for (int r = 0; r < rowNodes.getLength(); r++) {
				HashMap<String, String> singleMap = new HashMap<String, String>();
				Element rowElements = (Element) rowNodes.item(r);

				NodeList datumNodes = rowElements.getElementsByTagName("datum");
				for (int da = 0; da < datumNodes.getLength(); da++) {
					Element line = (Element) datumNodes.item(da);
					id = line.getAttribute("id");
					value = line.getAttribute("value");
					
					singleMap.put(id, value);

				}
				mapOfMap.put(String.valueOf(r), singleMap);
			}

			for (int i = 0; i < rowNodes.getLength(); i++) {
				colNo = mapOfMap.get(String.valueOf(i)).get("column_number");
				id = mapOfMap.get(String.valueOf(i)).get("productId");
				quantity = mapOfMap.get(String.valueOf(i)).get("qty");
				artNo = mapOfMap.get(String.valueOf(i)).get("artNo");
				
				
				Part part = new Part();
				part.setColNo(colNo);
				part.setId(id);
				part.setArtNo(artNo);
				part.setQuantity(Double.parseDouble(quantity));

				listOfLists.add(part);

			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return listOfLists;

	}

}
