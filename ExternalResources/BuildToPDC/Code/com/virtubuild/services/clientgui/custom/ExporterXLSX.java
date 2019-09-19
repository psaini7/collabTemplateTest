package com.virtubuild.services.clientgui.custom;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.Exporter;

public class ExporterXLSX extends Exporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExporterXLSX.class);

	@Override
	public String getDataString() {

		String exportedCSVFile = "";

		try {

			exportedCSVFile = getManager().getExporterController().runExporter("BOM_CSV");

		} catch (Exception e) {

			LOGGER.error("Error running custom exporter " + e.getMessage());

		}
		return exportedCSVFile;

	}

	@Override
	public void prepareData(Collection<String> arg0) {

	}

}
