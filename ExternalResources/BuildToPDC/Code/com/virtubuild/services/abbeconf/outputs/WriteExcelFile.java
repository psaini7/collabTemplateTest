package com.virtubuild.services.abbeconf.outputs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WriteExcelFile {
	private static final Logger LOGGER = LoggerFactory.getLogger(WriteExcelFile.class);
	static String osDir = System.getenv("SystemDrive") + "\\econftemp\\";

	/*private WriteExcelFile() {
		super();
	}*/

	public  void fileGenerate(Property property) {
		try {
			Thread.sleep(2000);
			boolean bFileExists=false;
			final File pngfile = new File("C:\\econftemp\\DesignVerification\\frontview.png");
			while(!bFileExists)
			{
				if(pngfile.exists() && !pngfile.isDirectory()) {
					bFileExists=true;
					break;
				}
			}
			FileInputStream file = new FileInputStream(new File(osDir + "DesignVerification\\DesignVerification.xlsx"));
			
			
			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			//****************************************************************************************************
			XSSFSheet sheet3 = workbook.getSheetAt(2);
			
			InputStream inputStream = new FileInputStream("C:\\econftemp\\DesignVerification\\frontview.png");

			// Get the contents of an InputStream as a byte[].
			byte[] bytes = IOUtils.toByteArray(inputStream);
			// Adds a picture to the workbook
			int pictureIdx = workbook.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
			// close the input stream
			inputStream.close();
			// Returns an object that handles instantiating concrete classes
			CreationHelper helper = workbook.getCreationHelper();
			// Creates the top-level drawing patriarch.
			Drawing drawing = sheet3.createDrawingPatriarch();

			// Create an anchor that is attached to the worksheet
			ClientAnchor anchor = helper.createClientAnchor();

			// create an anchor with upper left cell _and_ bottom right cell
			anchor.setCol1(1); // Column B
			anchor.setRow1(2); // Row 3
			anchor.setCol2(2); // Column C
			anchor.setRow2(3); // Row 4

			// Creates a picture
			Picture pict = drawing.createPicture(anchor, pictureIdx);
			// Reset the image to the original size
			pict.resize();
			
			
			//****************************************************************************************************

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Retrieve the row and check for null
			XSSFRow sheetrow1 = sheet.getRow(1);
			XSSFRow sheetrow2 = sheet.getRow(2);
			XSSFRow sheetrow4 = sheet.getRow(4);
			XSSFRow sheetrow5 = sheet.getRow(5);
			XSSFRow sheetrow23 = sheet.getRow(23);
			XSSFRow sheetrow25 = sheet.getRow(25);
			XSSFRow sheetrow27 = sheet.getRow(27);
			XSSFRow sheetrow29 = sheet.getRow(29);
			XSSFRow sheetrow31 = sheet.getRow(31);
			XSSFRow sheetrow33 = sheet.getRow(33);

			if (sheetrow1 == null || sheetrow2 == null || sheetrow4 == null || sheetrow5 == null || sheetrow23 == null
					|| sheetrow25 == null || sheetrow27 == null || sheetrow29 == null || sheetrow31 == null
					|| sheetrow33 == null) {

				sheetrow1 = sheet.createRow(1);
				sheetrow2 = sheet.createRow(2);
				sheetrow4 = sheet.createRow(4);
				sheetrow5 = sheet.createRow(5);
				sheetrow23 = sheet.createRow(23);
				sheetrow25 = sheet.createRow(25);
				sheetrow27 = sheet.createRow(27);
				sheetrow29 = sheet.createRow(29);
				sheetrow31 = sheet.createRow(31);
				sheetrow33 = sheet.createRow(33);
			}
			// Update the value of cell
			XSSFCell date = sheetrow1.getCell(13);
			XSSFCell number = sheetrow2.getCell(13);
			XSSFCell ratedVoltage = sheetrow23.getCell(3);
			XSSFCell ratedFrequency = sheetrow25.getCell(3);
			XSSFCell electricalSyatem = sheetrow27.getCell(3);
			XSSFCell ratedCurrentAssembly = sheetrow29.getCell(5);
			XSSFCell ratedInsulationVoltage = sheetrow31.getCell(5);
			XSSFCell ratedImpulseVoltage = sheetrow33.getCell(5);
			XSSFCell icc = sheetrow25.getCell(8);
			XSSFCell icw = sheetrow27.getCell(8);
			XSSFCell ipk = sheetrow29.getCell(8);
			XSSFCell project = sheetrow4.getCell(12);
			XSSFCell distributioBoard = sheetrow5.getCell(12);

			if (date == null || number == null || ratedVoltage == null || ratedFrequency == null
					|| electricalSyatem == null || ratedCurrentAssembly == null || ratedInsulationVoltage == null
					|| ratedImpulseVoltage == null || icw == null || icc == null || ipk == null || project == null
					|| distributioBoard == null) {
				date = sheetrow1.createCell(13);
				number = sheetrow2.createCell(13);
				ratedVoltage = sheetrow23.createCell(3);
				ratedFrequency = sheetrow25.createCell(3);
				electricalSyatem = sheetrow27.createCell(3);
				ratedCurrentAssembly = sheetrow29.createCell(5);
				ratedInsulationVoltage = sheetrow31.createCell(5);
				ratedImpulseVoltage = sheetrow33.createCell(5);
				icc = sheetrow25.createCell(8);
				icw = sheetrow27.createCell(8);
				ipk = sheetrow29.createCell(8);
				project = sheetrow4.createCell(12);
				distributioBoard = sheetrow5.createCell(12);

			}
			date.setCellValue(property.getDate());

			number.setCellValue(property.getNumber());

			ratedVoltage.setCellValue(property.getRatedVolatge());

			ratedFrequency.setCellValue(property.getRatedFrequency());

			electricalSyatem.setCellValue(property.getElectricalSystem());

			ratedCurrentAssembly.setCellValue(property.getRating());

			ratedInsulationVoltage.setCellValue(property.getRatedInsulationVoltage());

			ratedImpulseVoltage.setCellValue(property.getRatedImpulseWithStandVoltage());

			icc.setCellValue(property.getIcc());

			icw.setCellValue(property.getIcw());

			ipk.setCellValue(property.getIpk());

			project.setCellValue(property.getProject());

			distributioBoard.setCellValue(property.getDistributioBoard());

			XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);

			FileOutputStream outputStream = new FileOutputStream(osDir + "DesignVerification\\DesignVerification.xlsx");
			workbook.write(outputStream);
			outputStream.close();
			workbook.close();
			file.close();
			//pngfile.delete();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

	}

}
