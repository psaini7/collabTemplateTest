package com.virtubuild.services.clientgui.custom;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import com.virtubuild.clientgui.Configurator;
import com.virtubuild.core.Exporter;

public class BOMExporter extends Exporter {

	TreeTableMain ttm;
	List<MyDataNode> children1;

	private static final Logger LOGGER = LoggerFactory.getLogger(BOMExporter.class);

	@Override
	public String getDataString() {

		String exporterXml = "";

		try {
			
			exporterXml = getManager().getExporterController().runExporter("custom_exporter");

		} catch (Exception e) {

			LOGGER.error(e.getMessage());

		}
		return exporterXml;
	}
	
	public String refineData(String exporterXml) {

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

			List<MyDataNode> dataNodeList = new ArrayList<>();
			for (int j = 0; j < nl.getLength(); j++) {
				Node currentItem = nl.item(j);
				MyDataNode mydata = new MyDataNode();

				NodeList currentItemChilNodes = currentItem.getChildNodes();

				for (int k = 0; k < currentItemChilNodes.getLength(); k++) {
					Node currentChildItem = currentItemChilNodes.item(k);
					if (currentChildItem.getAttributes() == null) {
						continue;
					}

					String key = currentChildItem.getAttributes().getNamedItem("id").getNodeValue();
					String value = currentChildItem.getAttributes().getNamedItem("value").getNodeValue();

					if (key.equalsIgnoreCase("qty")) {
						mydata.setQuantity(Float.parseFloat(value));
					} else if(key.equalsIgnoreCase("link")){
						mydata.setLink(value);
					} else if (key.equalsIgnoreCase("descr")) {
						mydata.setDesc(value);
					} else if (key.equalsIgnoreCase("artNo")) {
						mydata.setOrderNumber(value);
					} else if (key.equalsIgnoreCase("productId")) {
						mydata.setProductType(value);
					} else if (key.equalsIgnoreCase("price")) {
						try {
							mydata.setGrossPrice(Float.parseFloat(value));
						} catch (Exception e) {
							LOGGER.info("Incorrect format warning");
							String newValue = "0";
							LOGGER.info("setting the Price as 0");
							mydata.setGrossPrice(Float.parseFloat(newValue));
						}

					} else if (key.equalsIgnoreCase("priceSum")) {
						try {
							mydata.setGrossPriceSum(Float.parseFloat(value));
						} catch (Exception e) {
							LOGGER.info("incorrect format warning ");
							String newValue = "0";
							LOGGER.info("setting the Gross Price as 0");
							mydata.setGrossPriceSum(Float.parseFloat(newValue));
						}

					} else if (key.equalsIgnoreCase("unit_of_measure")) {
						mydata.setUoM(value);
					} else if (key.equalsIgnoreCase("Title")) {
						mydata.setTitle(value);
					} else if (key.equalsIgnoreCase("column_number")) {
						try {
							mydata.setColumnNumber(Integer.parseInt(value));
						} catch (Exception e) {
							LOGGER.info("incorrect format warning ");
							String newValue = "0";
							LOGGER.info("setting the column number as 0");
							mydata.setColumnNumber(Integer.parseInt(newValue));
						}
					} else if (key.equalsIgnoreCase("column_name")) {
						try {
							mydata.setColumnName(value);
						} catch (Exception e) {
							LOGGER.info("Error reading column name in exporter");
						}
					} else if (key.equalsIgnoreCase("element_position")) {
						try {
							mydata.setPosition(value);
						} catch (Exception e) {
							LOGGER.info("Incorrect format warning");
							String newValue = "0";
							LOGGER.info("setting the Price as 0");
							mydata.setGrossPrice(Float.parseFloat(newValue));
						}

					}

				}
				dataNodeList.add(mydata);
			}
			TreeTableMain.listNodes = new ArrayList<>();

			TreeTableMain.listNodes.addAll(dataNodeList);
			
			TreeTableMain.listCompleteData = new ArrayList<>();
			
			TreeTableMain.listCompleteData.clear();
			
			TreeTableMain.listCompleteData.addAll(dataNodeList);
			
			
			TreeTableMain.selectAll = true;
			

		} catch (Exception e) {

			LOGGER.error ("Error occured: " + e.getMessage());
		}

		return exporterXml;

	}

	@Override
	public void prepareData(Collection<String> arg0) {
		
	}

}
