package com.virtubuild.services.abbeconf.compare;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BomCompareExportPdc {

	private static final Logger LOGGER = LoggerFactory.getLogger(BomCompareExportPdc.class);

	public List<Part> prepareData(String exporterXml) {

		String colNo = "";
		String id = "";
		String quantity = "";
		String artNo = "";

		List<Part> listOfLists = new ArrayList<>();

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(exporterXml.getBytes("UTF-8"));
			Document doc = db.parse(is);

			NodeList switchboardNodes = doc.getElementsByTagName("switchboard");

			for (int c = 0; c < switchboardNodes.getLength(); c++) {
				Element colElements = (Element) switchboardNodes.item(c);

				NodeList rowNodes = colElements.getElementsByTagName("parts");

				for (int r = 0; r < 1; r++) {
					Element rowElements = (Element) rowNodes.item(r);

					NodeList datumNodes = rowElements.getElementsByTagName("part");
					for (int da = 0; da < datumNodes.getLength(); da++) {
						Element line = (Element) datumNodes.item(da);
						id = line.getAttribute("identreference");
						quantity = line.getAttribute("quantity");
						artNo = line.getAttribute("typereference");

						Part part = new Part();
						part.setColNo("1");
						part.setId(id);
						part.setArtNo(artNo);
						part.setQuantity(Double.parseDouble(quantity));
						listOfLists.add(part);
					}

				}
			}

			NodeList colNodes = doc.getElementsByTagName("column");

			for (int c = 0; c < colNodes.getLength(); c++) {
				Element colElements = (Element) colNodes.item(c);
				colNo = colElements.getAttribute("id");
				String[] subStrings = colNo.split("_");
				colNo = subStrings[subStrings.length - 1];

				NodeList rowNodes = colElements.getElementsByTagName("parts");

				for (int r = 0; r < rowNodes.getLength(); r++) {
					Element rowElements = (Element) rowNodes.item(r);

					NodeList datumNodes = rowElements.getElementsByTagName("part");
					for (int da = 0; da < datumNodes.getLength(); da++) {
						Element line = (Element) datumNodes.item(da);
						id = line.getAttribute("identreference");
						quantity = line.getAttribute("quantity");
						artNo = line.getAttribute("typereference");

						Part part = new Part();
						part.setColNo(colNo);
						part.setId(id);
						part.setArtNo(artNo);
						part.setQuantity(Double.parseDouble(quantity));
						listOfLists.add(part);
					}

				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return listOfLists;

	}

}
