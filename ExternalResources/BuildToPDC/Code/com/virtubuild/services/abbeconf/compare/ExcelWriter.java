package com.virtubuild.services.abbeconf.compare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriter.class);
	private static String[] columns = { "Product ID", "Art Number" };
	static String osDir = System.getenv("SystemDrive") + "\\econftemp\\";

	public void main(List<Part> mismatchData) throws Exception {
		try {
			// Create a Workbook
			Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xlxs` file
	
			// Create a Sheet
			Sheet sheet = workbook.createSheet("BOM Compare Result");
	
			// Create a Font for styling header cells
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.RED.getIndex());
	
			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
	
			// Create a Row
			Row headerRow = sheet.createRow(0);
	
			// Create cells
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}
	
			// Create Other rows and cells with mismatchData data
			int rowNum = 1;
			for (Part part : mismatchData) {
				Row row = sheet.createRow(rowNum++);
	
				row.createCell(0).setCellValue(part.getId());
	
				row.createCell(1).setCellValue(part.getArtNo());
	
				// row.createCell(2).setCellValue(part.getQuantity());
			}
	
			// Resize all columns to fit the content size
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
	
			
			Path path = Paths.get(osDir);
			File file = new File(osDir + "Bom-Compare-Result.xlsx");

			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			
	
			if (!file.exists()) {
				FileOutputStream fileOut = new FileOutputStream(file);
				workbook.write(fileOut);
				fileOut.close();
	
				// Closing the workbook
				workbook.close();
			} else {
				
				FileInputStream myxlsx = new FileInputStream(file);
			    XSSFWorkbook workbookAppend = new XSSFWorkbook(myxlsx);
			    XSSFSheet worksheet = workbookAppend.getSheetAt(0);
				
				int lastRowNum = worksheet.getLastRowNum();
				// Append Other rows and cells from mismatchData data
				for (Part part : mismatchData) {
					Row rowLast = worksheet.createRow(++lastRowNum);

					rowLast.createCell(0).setCellValue(part.getId());

					rowLast.createCell(1).setCellValue(part.getArtNo());

					// row.createCell(2).setCellValue(part.getQuantity());
				}
				myxlsx.close();
				
				FileOutputStream output_file = new FileOutputStream(new File(osDir + "Bom-Compare-Result.xlsx"));
				// write changes
				workbookAppend.write(output_file);
				output_file.close();
				workbookAppend.close();
				
			}

		}
		catch (Exception e ) {
			LOGGER.error(e.getMessage());
		}
	}



}