package com.virtubuild.services.abbeconf.outputs;

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

public class ReadXml {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReadXml.class);

	public List<Property> prepareData(String exporterXml) {

		String date = "";
		String distributioBoard = "";
		String electricalSystem = "";
		String icc = "";
		String icw = "";
		String ipk = "";
		String number = "";
		String project = "";
		String ratedFrequency = "";
		String ratedImpulseWithStandVoltage = "";
		String ratedInsulationVoltage = "";
		String ratedVolatge = "";
		String rating = "";

		List<Property> list = new ArrayList<>();

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(exporterXml.getBytes("UTF-8"));
			Document doc = db.parse(is);

			NodeList rowNodes = doc.getElementsByTagName("property");
						
			for (int r = 0; r < rowNodes.getLength(); r++) {
				Element rowElements = (Element) rowNodes.item(r);

				if (r == 0) {
					date = rowElements.getAttribute("date");
				} else if (r == 1) {
					distributioBoard = rowElements.getAttribute("distribution_board");
				} else if (r == 2) {
					electricalSystem = rowElements.getAttribute("electrical_system");
				} else if (r == 3) {
					icc = rowElements.getAttribute("icc");
				} else if (r == 4) {
					icw = rowElements.getAttribute("icw");
				} else if (r == 5) {
					ipk = rowElements.getAttribute("ipk");
				} else if (r == 6) {
					number = rowElements.getAttribute("number");
				} else if (r == 7) {
					project = rowElements.getAttribute("project");
				} else if (r == 8) {
					ratedFrequency = rowElements.getAttribute("rated_frequency");
				} else if (r == 9) {
					ratedImpulseWithStandVoltage = rowElements.getAttribute("rated_impulse_withstand_voltage");
				} else if (r == 10) {
					ratedInsulationVoltage = rowElements.getAttribute("rated_insulation_voltage");
				} else if (r == 11) {
					ratedVolatge = rowElements.getAttribute("rated_volatge");
				} else if (r == 12) {
					rating = rowElements.getAttribute("rating");
				}

			}

			Property property = new Property();
			property.setDate(date);
			property.setDistributioBoard(distributioBoard);
			property.setElectricalSystem(electricalSystem);
			property.setIcc(icc);
			property.setIcw(icw);
			property.setIpk(ipk);
			property.setNumber(number);
			property.setProject(project);
			property.setRatedFrequency(ratedFrequency);
			property.setRatedImpulseWithStandVoltage(ratedImpulseWithStandVoltage);
			property.setRatedInsulationVoltage(ratedInsulationVoltage);
			property.setRatedVolatge(ratedVolatge);
			property.setRating(rating);

			WriteExcelFile writer = new WriteExcelFile();
			writer.fileGenerate(property);
			
			

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return list;

	}

}
