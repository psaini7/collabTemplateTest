/******************************************************************
 * BomCompare				                                      *
 * Represents a Exporter class for automating the BOM UI and 	  *
 * PDC Export from build. It generated the missing product_ID 	  *
 * in PDC_exporter when compared with BOM exporter.				  *
 * An Excel file is generated in OS dir econftemp/BomCompare	  *
 * folder			  											  *
 ******************************************************************/
/**@author Aagreet Sinha*/
package com.virtubuild.services.abbeconf.compare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.Exporter;

public class BomCompare extends Exporter {
	String exporterXmlExportPdc = "";
	String exporterXmlCustomExporter = "";
	static String osDir = System.getenv("SystemDrive") + "\\econftemp\\";
	private static final Logger LOGGER = LoggerFactory.getLogger(BomCompare.class);

	private void prepareData() throws Exception {
		exporterXmlExportPdc = getManager().getExporterController().runExporter("export_PDC");
		exporterXmlCustomExporter = getManager().getExporterController().runExporter("custom_exporter");

		BomCompareExportPdc bcep = new BomCompareExportPdc();
		List<Part> listOfListsBcep = bcep.prepareData(exporterXmlExportPdc);
		List<Part> finalList = null;
		BomCompareCustomExporter bcce = new BomCompareCustomExporter();
		List<Part> listOfListsBcce = bcce.prepareData(exporterXmlCustomExporter);

		Set<Part> set = new HashSet<>(listOfListsBcep);

		if (set.size() < listOfListsBcep.size()) {
			listOfListsBcep = findDuplicatesId(listOfListsBcep);
		}

		if (!checkEqual(listOfListsBcce, listOfListsBcep)) {
			if (listOfListsBcce.size() >= listOfListsBcep.size()) {
				listOfListsBcce.removeAll(listOfListsBcep);
				finalList = listOfListsBcce;
			} else {

				listOfListsBcep.removeAll(listOfListsBcce);
				finalList = listOfListsBcep;
			}

			final File file = new File(osDir + "BomCompareOutput.xlsx");
			if (file.exists() && !file.renameTo(file)) {
				infoBox("Please colse the running excel file and run again.", "Warning : File already open");
			} else {
				ExcelWriter excelWriter = new ExcelWriter();
				excelWriter.main(finalList);
				Removeduplicate.main();
			}

		}
	}

	public List<Part> findDuplicatesId(List<Part> listOfListsBcep) {
		Map<String, Part> partsById = new HashMap<>();
		for (Part item : listOfListsBcep) {
			if (partsById.containsKey(item.getId().toUpperCase() + ":" + item.getColNo())) {
				Part part = partsById.get(item.getId().toUpperCase() + ":" + item.getColNo());
				part.setQuantity(part.getQuantity() + item.getQuantity());
			} else {
				partsById.put(item.getId().toUpperCase() + ":" + item.getColNo(), item);
			}
		}
		return new ArrayList<>(partsById.values());
	}

	boolean checkEqual(List<Part> l1, List<Part> l2) {
		return l1.size() == l2.size() && l1.containsAll(l2);
	}

	private static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public String getDataString() {
		try {
			prepareData();
		} catch (InvalidFormatException | InterruptedException | IOException e) {
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	@Override
	public void prepareData(Collection<String> arg0) {

	}

}
