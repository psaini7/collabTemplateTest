package com.virtubuild.services.clientgui.copperforecast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlToString {
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlToString.class);

	public String refineData(String exporterXml) {
		String csvStrFinal = "";
		List<String> lstTitle = new ArrayList<>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(exporterXml.getBytes("UTF-8"));
			Document doc = db.parse(is);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//reportexport/report/blocks/block/datatables/datatable/rows/row");
			NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			List<ExcelObject> dataNodeList = new ArrayList<>();

			for (int j = 0; j < nl.getLength(); j++) {
				Node currentItem = nl.item(j);
				ExcelObject mydata = new ExcelObject();

				NodeList currentItemChilNodes = currentItem.getChildNodes();

				for (int k = 0; k < currentItemChilNodes.getLength(); k++) {
					Node currentChildItem = currentItemChilNodes.item(k);
					if (currentChildItem.getAttributes() == null) {
						continue;
					}

					String key = currentChildItem.getAttributes().getNamedItem("id").getNodeValue();
					String value = currentChildItem.getAttributes().getNamedItem("value").getNodeValue();

					if (key.equalsIgnoreCase("qty")) {
						mydata.setQuantity(value);
					} else if (key.equalsIgnoreCase("link")) {
						mydata.setLink(value);
					} else if (key.equalsIgnoreCase("descr")) {
						mydata.setDesc(value);
					} else if (key.equalsIgnoreCase("artNo")) {
						mydata.setTypeCode(value);
						;
					} else if (key.equalsIgnoreCase("productId")) {
						mydata.setProductID(value);
					} else if (key.equalsIgnoreCase("price")) {
						mydata.setGrossPrice(value);
					} else if (key.equalsIgnoreCase("priceSum")) {
						mydata.setGrossPriceSum(value);
					} else if (key.equalsIgnoreCase("unit_of_measure")) {
						mydata.setUoM(value);
					} else if (key.equalsIgnoreCase("Title")) {
						mydata.setClassBOM(value);
					} else if (key.equalsIgnoreCase("column_number")) {
						try {
							mydata.setColumnNumber(Integer.parseInt(value));
						} catch (Exception e) {
							LOGGER.info("incorrect format warning ");
							String newValue = "0";
							LOGGER.info("setting the column number as 0");
							mydata.setColumnNumber(Integer.parseInt(newValue));
						}
					
					} else if (key.equalsIgnoreCase("element_position")) {
						mydata.setPosition(value);
					}

				}
				dataNodeList.add(mydata);
				
			}
			Collections.sort(dataNodeList);
			//System.out.println(dataNodeList);
			// TreeTableMain.listNodes = new ArrayList<>();

			// TreeTableMain.listNodes = BOMExporter.dataNodeList;

			/*
			 * for (MyDataNode node : TreeTableMain.listNodes ) {
			 * 
			 * System.out.println(node.getDesc()+" ," + node.getTitle()+" ," +
			 * node.getOrderNumber());
			 * 
			 * }
			 */
			
			HashMap<String, Integer> columnNumbers = new HashMap<>();
			for (ExcelObject dataNode : dataNodeList) {
				Integer value = dataNode.getColumnNumber();
				columnNumbers.put("ColumnNo_" + value.toString(), value);
			}	
			HashMap<String, List<ExcelObject>> entireMap = new HashMap<>();
			Iterator it = columnNumbers.entrySet().iterator();
			while (it.hasNext()) {
				List<ExcelObject> listOfSpecificColNum = new ArrayList<>();
				Map.Entry pair = (Map.Entry) it.next();
				for (ExcelObject node : dataNodeList) {
					if (node.getColumnNumber().equals(pair.getValue())) {
						listOfSpecificColNum.add(node);
					}
				}
				entireMap.put(pair.getKey().toString(), listOfSpecificColNum);
			}
			
			for (ExcelObject dataNode : dataNodeList) {
				if (lstTitle.contains(dataNode.getClassBOM()) == false) {
					lstTitle.add(dataNode.getClassBOM());
				}
				//System.out.println("Xml to string:"+lstTitle);
			}
			
			List<List<ExcelObject>> children2 = new ArrayList<>();
			List<ExcelObject> children3 = new ArrayList<>();
			for (Map.Entry<String, List<ExcelObject>> entry : entireMap.entrySet()) {
				
				for (int i = 0; i < lstTitle.size(); i++) {
					List<ExcelObject> children = new ArrayList<>();
					for (ExcelObject dataNode : entry.getValue()) {
						if (dataNode.getClassBOM().equalsIgnoreCase(lstTitle.get(i))) {
							children.add(dataNode);
						}
					}
					if (!children.isEmpty()) {
						Collections.sort(children, new Comparator<ExcelObject>() {

							@Override
							public int compare(ExcelObject arg0, ExcelObject arg1) {

								return arg0.getTypeCode().compareTo(arg1.getTypeCode());

							}
						});
					}
					children2.add(children); //list of excel objects
					for(int k=0;k<children.size();k++)
					{
						children3.add(children.get(k));
					}
				}
			}
			Collections.sort(children3);
			//System.out.println(children3);
			
			String csvStr = null;
			for (ExcelObject p : children3) {

				csvStr = p.getColumnNumber() + "," + p.getClassBOM() + "," + p.getProductID() + "," + p.getTypeCode()
						+ "," + p.getPosition() + "," + p.getQuantity() + "," + p.getUoM() + "," + p.getDesc() + ","
						+ p.getGrossPrice() + "," + p.getGrossPriceSum() + "," + p.getLink();

				csvStrFinal = csvStrFinal + " \n" + csvStr;
			}
		} catch (Exception e) {

			LOGGER.error("Error occured: " + e.getMessage());
		}

		return csvStrFinal;

	}
	 
   }
