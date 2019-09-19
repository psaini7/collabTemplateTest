package com.virtubuild.services.abbeconf.export;

import static de.ksquared.jgaeb.gaeb1990.file.File.Identifier.KE_86;
import static de.ksquared.jgaeb.gaeb1990.group.Group.Name.GR_01;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.virtubuild.core.Exporter;

import de.ksquared.jgaeb.gaeb1990.element.Element.Type;
import de.ksquared.jgaeb.gaeb1990.file.FileBuilder;
import de.ksquared.jgaeb.gaeb1990.frame.Frame;
import de.ksquared.jgaeb.gaeb1990.frame.FrameBuilder;
import de.ksquared.jgaeb.gaeb1990.group.GroupBuilder;

public class GAEBExporter extends Exporter {

	private String fileContent = " ";
	String exporterXml = "";
	
	private static final String DEVICE_TYPE = "device_type";
	private static final String DEVICE_POLES = "device_poles";
	private static final String ARTICLE_TYPE = "article_type";
	private static final String HEIGHT = "height";
	private static final String WIDTH_COUPLER = "width_coupler";
	

	public void prepareData() {

		FileBuilder file;
		GroupBuilder group01;
		FrameBuilder frame;
		String id = "";
		String value = "";
		file = new FileBuilder(KE_86); // optional group: GR_03 (this one could also be template of KE_84, with ZA_07
										// and without ZA_25)
		group01 = file.newGroup(GR_01);

		HashMap<String, String> map = new HashMap<String, String>();
		exporterXml = getManager().getExporterController().runExporter("exporter_tender");

		try {

			// Parse the file

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(exporterXml.getBytes("UTF-8"));
			Document doc = db.parse(is);

			NodeList nodes2 = doc.getElementsByTagName("blocks");

			for (int i2 = 0; i2 < nodes2.getLength(); i2++) {
				org.w3c.dom.Element element2 = (org.w3c.dom.Element) nodes2.item(i2);
				NodeList nodes1 = element2.getElementsByTagName("block");
				for (int i1 = 0; i1 < nodes1.getLength(); i1++) {
					org.w3c.dom.Element element1 = (org.w3c.dom.Element) nodes1.item(i1);
					String blockrefid = element1.getAttribute("refid");

					if (blockrefid.equalsIgnoreCase("coupler_column")) {

						NodeList nodes = element1.getElementsByTagName("parameters");
						for (int i = 0; i < nodes.getLength(); i++) {
							org.w3c.dom.Element element = (org.w3c.dom.Element) nodes.item(i);

							// Process the lines

							NodeList lines = element.getElementsByTagName("parameter");

							for (int j = 0; j < lines.getLength(); j++) {
								org.w3c.dom.Element line = (org.w3c.dom.Element) lines.item(j);
								id = line.getAttribute("id");
								value = line.getAttribute("value");

								map.put(id, value);

							}
						}
					}

				}
			}

			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is1 = classloader.getResourceAsStream("Tender_StaticText.txt");
			InputStreamReader streamReader = new InputStreamReader(is1, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(streamReader);
			BufferedReader bufferedReader = new BufferedReader(reader);

			String line1 = "";
			while ((line1 = bufferedReader.readLine()) != null) {
				String str = line1;
				if (str.contains("replace_date")) {

					DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
					Date date = new Date();
					String str2 = str.replace("replace_date", dateFormat.format(date));
					str = str2;
				}

				if (str.contains(DEVICE_TYPE)) {

					String str2 = str.replace(DEVICE_TYPE,
							map.get(DEVICE_TYPE) != null ? map.get(DEVICE_TYPE) : "");
					str = str2;
				}
				if (str.contains(DEVICE_POLES)) {

					String str2 = str.replace(DEVICE_POLES,
							map.get(DEVICE_POLES) != null ? map.get(DEVICE_POLES) : "");
					str = str2;
				}
				if (str.contains(ARTICLE_TYPE)) {

					String str2 = str.replace(ARTICLE_TYPE,
							map.get(ARTICLE_TYPE) != null ? map.get(ARTICLE_TYPE) : "");
					str = str2;
				}
				if (str.contains(HEIGHT)) {

					String str2 = str.replace(HEIGHT, map.get(HEIGHT) != null ? map.get(HEIGHT) : "");
					str = str2;
				}
				if (str.contains(WIDTH_COUPLER)) {

					String str2 = str.replace(WIDTH_COUPLER,
							map.get(WIDTH_COUPLER) != null ? map.get(WIDTH_COUPLER) : "");
					str = str2;
				}
				frame = group01.newFrame(Frame.Type.ZA_T1);
				frame.addElement(Type.TEXT, "  " + str);
			}
			is1.close();
			fileContent = file.toString();

		} catch (Exception e) {

		}

	}

	@Override
	public String getDataString() {
		prepareData();
		return this.fileContent;
	}

	@Override
	public void prepareData(Collection<String> arg0) {

	}

}
