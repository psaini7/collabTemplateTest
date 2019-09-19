package com.virtubuild.services.clientgui.custom;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.complete.CompleterSkeleton;

/**
 * 
 * @author Shiyam Mosies 
 * Created Date: 30-July-2018 Version: 1.0.0 
 * Purpose: This Completer is invoked when exporting an XLSX file from Configurator
 * 
 */

public class ExportXSLXCompleter extends CompleterSkeleton {

	private String strXSLX;

	private static final Logger LOGGER = LoggerFactory.getLogger(ExportXSLXCompleter.class);

	/**
	 * Method to convert CSV file to XLSX file
	 * 
	 * @param pathToSave
	 */
	public static void csvToXLSX(String pathToSave) {

		String currentLine = null;
		int rowNum = -1;
		BufferedReader csvReader;
		String csvFileAddress;
		String xlsxFileAddress;
		XSSFWorkbook workBook;
		XSSFSheet sheet;

		// List<String> listPDF = new ArrayList<>();
		/*
		 * listPDF.add(
		 * "http://search.abb.com/library/Download.aspx?DocumentID=1STS100196R0001&LanguageCode=de&LanguageCode=en&LanguageCode=es&LanguageCode=fr&LanguageCode=it&LanguageCode=pl&LanguageCode=ru&DocumentPartId=&Action=Launch"
		 * );
		 * 
		 * listPDF.add(
		 * "http://search.abb.com/library/Download.aspx?DocumentID=1STS100194R0001&LanguageCode=de&LanguageCode=en&LanguageCode=es&LanguageCode=fr&LanguageCode=it&LanguageCode=pl&LanguageCode=ru&DocumentPartId=&Action=Launch"
		 * );
		 * 
		 * listPDF.add(
		 * "http://search.abb.com/library/Download.aspx?DocumentID=1STS100193R0001&LanguageCode=de&LanguageCode=en&LanguageCode=es&LanguageCode=fr&LanguageCode=it&LanguageCode=pl&LanguageCode=ru&DocumentPartId=&Action=Launch"
		 * );
		 */
		try {

			csvFileAddress = pathToSave; // CSV file path
			xlsxFileAddress = pathToSave.replace("csv", "xlsx"); // XLSX file path

			workBook = new XSSFWorkbook();
			sheet = workBook.createSheet("Sheet1");

			csvReader = new BufferedReader(new FileReader(csvFileAddress));
			
			int lineNo = 0;

			while ((currentLine = csvReader.readLine()) != null) {
				
				lineNo ++;
				
				
				String strArray[] = currentLine.split(",");
				
				/*
				
				// Fetching instruction manuals using Type Code
				String[] instructionManualLinks = null;
				
				if(lineNo > 1){
					instructionManualLinks = TreeTableMain.mapTypeCodeURL.get(strArray[4]).split(",");
					
					if (instructionManualLinks == null){
						
						instructionManualLinks = new String[2];
						
						instructionManualLinks[0] = "https://search.abb.com/library/Download.aspx?DocumentID=1STC803001D0201&LanguageCode=en&DocumentPartId=&Action=Launch";
						
					}
						
					
				}
					
				*/
				
				rowNum++;
				
				XSSFRow currentRow = (XSSFRow) sheet.createRow(rowNum);
				
				for (int i = 0; i < strArray.length; i++) {
					
					//System.out.println("Column no. " + i);
					
					currentRow.createCell(i).setCellValue(strArray[i]);
					
					// inserting new column for data which is not present in custom_exporter i.e
					// instruction manual links
					XSSFCell cell = currentRow.createCell(strArray.length, CellType.STRING);
					XSSFCell cell2 = currentRow.createCell(strArray.length + 1, CellType.STRING);

					if (currentRow.getRowNum() == 0) {
						cell.setCellValue("Instruction Manual 1");
						cell2.setCellValue("Instruction Manual 2");
						XSSFCellStyle style = workBook.createCellStyle();
						XSSFFont font = workBook.createFont();
						font.setBold(true);
						style.setFont(font);
						cell.setCellStyle(style);
						cell2.setCellStyle(style);
					} else {
						// cell.setCellValue(listPDF.get(0));
						
						//cell.setCellValue(MyTreeTableCellRenderer.listPDF.get(0));
						//cell2.setCellValue(MyTreeTableCellRenderer.listPDF.get(1));
						
						//Hardcoded URL in excel cells
						
						cell.setCellValue("https://search.abb.com/library/Download.aspx?DocumentID=1STC803001D0201&LanguageCode=en&DocumentPartId=&Action=Launch");
						cell2.setCellValue("https://search.abb.com/library/Download.aspx?DocumentID=1STC803001D0201&LanguageCode=en&DocumentPartId=&Action=Launch");
						
						/*if( null != instructionManualLinks && instructionManualLinks.length > 0 ){
							
							cell.setCellValue(instructionManualLinks[0]);
							
							if(instructionManualLinks.length > 1){
								
								cell2.setCellValue(instructionManualLinks[1]);
								
							}
							
						}*/
						
					}

					// Setting the column headers to bold

					if (rowNum == 0) {
						
						XSSFCellStyle style = workBook.createCellStyle();
						XSSFFont font = workBook.createFont();
						font.setBold(true);
						style.setFont(font);
						currentRow.getCell(i).setCellStyle(style);
					}

				}
			}

			// Setting the filter to the column headers

			sheet.setAutoFilter(new CellRangeAddress(0, rowNum, sheet.getRow(0).getFirstCellNum(),
					sheet.getRow(0).getLastCellNum() - 1));

			// Excel cell formatting from text to number

			DataFormat fmt = workBook.getCreationHelper().createDataFormat();
			CellStyle cellStylePosition = workBook.createCellStyle();
			CellStyle cellStyleQty = workBook.createCellStyle();
			CellStyle cellStyleColumNo = workBook.createCellStyle();
			CellStyle cellStylePrice = workBook.createCellStyle();
			CellStyle cellStylePriceSum = workBook.createCellStyle();

			cellStyleColumNo.setDataFormat(fmt.getFormat("0"));// columnno
			cellStylePosition.setDataFormat(fmt.getFormat("0"));// pos
			cellStyleQty.setDataFormat(fmt.getFormat("0.00"));// qty
			cellStylePrice.setDataFormat(fmt.getFormat("0.00")); // price
			cellStylePriceSum.setDataFormat(fmt.getFormat("0.00")); // priceSum

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				sheet.getRow(i).getCell(1).setCellStyle(cellStyleColumNo);
				sheet.getRow(i).getCell(5).setCellStyle(cellStylePosition);
				sheet.getRow(i).getCell(6).setCellStyle(cellStyleQty);
				sheet.getRow(i).getCell(9).setCellStyle(cellStylePrice);
				sheet.getRow(i).getCell(10).setCellStyle(cellStylePriceSum);

				String strColumnNo = sheet.getRow(i).getCell(1).getStringCellValue();
				String strPosition = sheet.getRow(i).getCell(5).getStringCellValue();
				String strQty = sheet.getRow(i).getCell(6).getStringCellValue();
				String strPrice = sheet.getRow(i).getCell(9).getStringCellValue();
				String strPriceSum = sheet.getRow(i).getCell(10).getStringCellValue();

				sheet.getRow(i).getCell(1).setCellValue(new BigDecimal(strColumnNo).doubleValue());
				String rtl = "\u200F" + strPosition;
				sheet.getRow(i).getCell(5).setCellValue(rtl);
				sheet.getRow(i).getCell(6).setCellValue(new BigDecimal(strQty).doubleValue());
				sheet.getRow(i).getCell(9).setCellValue(new BigDecimal(strPrice).doubleValue());
				sheet.getRow(i).getCell(10).setCellValue(new BigDecimal(strPriceSum).doubleValue());
			}

			// Resize all columns to fit the content size
			for (int i = 1; i <= sheet.getLastRowNum()-1; i++) {
				sheet.autoSizeColumn(i);
			}

			// Column width for instruction manuals column
		/*	sheet.setColumnWidth(0, 3200);
			sheet.setColumnWidth(1, 4500);
			sheet.setColumnWidth(2, 5200);
			sheet.setColumnWidth(3, 4300);
			sheet.setColumnWidth(4, 2700);
			sheet.setColumnWidth(5, 2000);
			sheet.setColumnWidth(6, 2000);
			sheet.setColumnWidth(7, 15000);
			sheet.setColumnWidth(8, 2500);
			sheet.setColumnWidth(9, 3000);*/
			sheet.setColumnWidth(11, 5500);
			sheet.setColumnWidth(12, 5500);
			sheet.setColumnWidth(13, 5500);
			

			// Formatting cell value as a Hyperlink
			CellStyle hlink_style = workBook.createCellStyle();
			XSSFFont hlink_font = workBook.createFont();
			hlink_font.setUnderline(XSSFFont.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Hyperlink linkInstructionManual1 = workBook.getCreationHelper().createHyperlink(HyperlinkType.URL);
				Hyperlink linkInstructionManual2 = workBook.getCreationHelper().createHyperlink(HyperlinkType.URL);
				Hyperlink linkABBProduct = workBook.getCreationHelper().createHyperlink(HyperlinkType.URL);

				String instructionManual1Text = sheet.getRow(i).getCell(12).getStringCellValue();
				String instructionManual2Text = sheet.getRow(i).getCell(13).getStringCellValue();
				String abbProductText = sheet.getRow(i).getCell(11).getStringCellValue(); // No text for hyperlink as of now
				String productID = sheet.getRow(i).getCell(3).getStringCellValue();
				// File file3= new File(sheet.getRow(i).getCell(2).getStringCellValue());

				linkInstructionManual1.setAddress(instructionManual1Text);
				linkInstructionManual2.setAddress(instructionManual2Text);
				linkABBProduct.setAddress(abbProductText.trim());
				// link3.setAddress(file3.toURI().toString());

				sheet.getRow(i).getCell(12).setHyperlink(linkInstructionManual1);
				sheet.getRow(i).getCell(12).setCellValue("1STS100196R0001");
				sheet.getRow(i).getCell(13).setHyperlink(linkInstructionManual2);
				sheet.getRow(i).getCell(13).setCellValue("1STS100193R0001");
				sheet.getRow(i).getCell(11).setHyperlink(linkABBProduct);
				sheet.getRow(i).getCell(11).setCellValue(productID);
			}
			/*
			 * for (int i = 1; i <= sheet.getLastRowNum(); i++) { String abbLinkText =
			 * sheet.getRow(i).getCell(10).getStringCellValue(); String abbLinkHyperLink =
			 * "HYPERLINK(" + "\"" + abbLinkText.trim() + "\"" + ")";
			 * sheet.getRow(i).getCell(10).setCellFormula(abbLinkHyperLink);
			 * 
			 * 
			 * 
			 * 
			 * String insManual1LinkText = sheet.getRow(i).getCell(11).getStringCellValue();
			 * String insManual1LinkHyperLink = "HYPERLINK(" + "\"" +
			 * insManual1LinkText.trim() + "\"" + ")";
			 * sheet.getRow(i).getCell(11).setCellFormula(insManual1LinkHyperLink);
			 * 
			 * String insManual2LinkText = sheet.getRow(i).getCell(12).getStringCellValue();
			 * String insManual2LinkHyperLink = "HYPERLINK(" + "\"" +
			 * insManual2LinkText.trim() + "\"" + ")";
			 * sheet.getRow(i).getCell(12).setCellFormula(insManual2LinkHyperLink);
			 * 
			 * XSSFFormulaEvaluator.evaluateAllFormulaCells(workBook);
			 * 
			 * }
			 */

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				sheet.getRow(i).getCell(11).setCellStyle(hlink_style);
				sheet.getRow(i).getCell(12).setCellStyle(hlink_style);
				sheet.getRow(i).getCell(13).setCellStyle(hlink_style);
			}

			// Inside border in all cells
			/*
			 * for (int i = 0; i <= sheet.getLastRowNum(); i++) { XSSFCellStyle style =
			 * workBook.createCellStyle(); style.setBorderBottom(BorderStyle.THIN);
			 * style.setBorderTop(BorderStyle.THIN); style.setBorderRight(BorderStyle.THIN);
			 * style.setBorderLeft(BorderStyle.THIN); for (int j = 0; j <
			 * sheet.getRow(0).getLastCellNum(); j++) {
			 * sheet.getRow(i).getCell(j).setCellStyle(style); } }
			 */
			csvReader.close();

			FileOutputStream fileOutputStream = new FileOutputStream(xlsxFileAddress);
			workBook.write(fileOutputStream);
			workBook.close();
			fileOutputStream.close();
			JOptionPane.showMessageDialog( new JFrame(), "Report generated successfully!",  "ABB e-Configure", JOptionPane.INFORMATION_MESSAGE);
			openGeneratedFile(xlsxFileAddress);
			
		} catch (Exception ex) {
			LOGGER.error("Some error occured in converting CSV to XSLX: " + ex.getMessage());
		}

	}
	
	private static void openGeneratedFile(String filePath) {
		try {
			Desktop.getDesktop().open(new File(filePath));
		} catch (IOException e) {
			LOGGER.error("Error while opening file" + e.getMessage());
		}
		
	}
	
	private String selectFileAndValidate() {
		String sPath="";
		JFrame saveDialogBox = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save XLSX file");
		fileChooser.setSelectedFile(new File("eConfigure_BOM_export"));

		int userSelection = fileChooser.showSaveDialog(saveDialogBox);

		File fileToSave = fileChooser.getSelectedFile();
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			
			if(new File(fileToSave.getPath() +".xlsx").exists()) {
				JOptionPane.showMessageDialog( new JFrame(), "File already exists!",  "ABB e-Configure", JOptionPane.INFORMATION_MESSAGE);
				String sAnotherPath=selectFileAndValidate();
				if(sAnotherPath!="") {
					sPath=sAnotherPath;
				}
			}else sPath=fileToSave.getPath();
		}
		return sPath;
	}
	@Override
	public boolean doComplete() {

		/*
		 * This method is invoked when an user selects .xlsx from the drop down list and
		 * clicks on Export button.
		 */

		// Get the exported CSV as string
		// strXSLX = getManager().getExporterController().runExporter("BOM_CSV");

		String exportedXml = null;
		XmlToString xmlToString = null;
		String csvStrFinal = null;
		
		String sPath=selectFileAndValidate();
		
		
		
		/*JFrame saveDialogBox = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save XLSX file");

		int userSelection = fileChooser.showSaveDialog(saveDialogBox);

		File fileToSave = fileChooser.getSelectedFile();*/

		//if (userSelection == JFileChooser.APPROVE_OPTION) {
			if (sPath != "") {			
		exportedXml = getManager().getExporterController().runExporter("custom_exporter");
			xmlToString = new XmlToString();
			csvStrFinal = xmlToString.refineData(exportedXml);

			try {
				//String csvFilePath = fileToSave.getAbsolutePath() + ".csv";
				String csvFilePath = sPath + ".csv";
				String stringFirstlineNew = "Column Name" + "," + "Column_No" + "," + "Class" + "," + "Product ID" + "," + "Type Code" + ","
						+ "Position" + "," + "Qty" + "," + "UoM" + "," + "Description" + "," + "Price" + ","
						+ "Price Sum" + "," + "ABB Product ";
				// String stringFinalToXLX = stringFirstLine + csvStrFinal;
				String stringFinalToXLX = stringFirstlineNew + csvStrFinal;
				FileUtils.writeStringToFile(new File(csvFilePath), stringFinalToXLX);
				
				//BufferedReader csvReader = new BufferedReader(new FileReader(csvFilePath));
				//csvReader.close();
				
				csvToXLSX(csvFilePath);

				// Deleting CSV file after converting it to XLSX
				try {
					Files.deleteIfExists(Paths.get(csvFilePath));

				} catch (NoSuchFileException e) {
					LOGGER.error("No such file/directory exists " + e.getMessage());
				} catch (DirectoryNotEmptyException e) {
					LOGGER.error("Directory is not empty. " + e.getMessage());
				} catch (IOException e) {
					LOGGER.error("Invalid permissions. " + e.getMessage());
				}

				catch (Exception e) {
					
					LOGGER.error("Error in exporting excel file " + e.getMessage());
				}
			} catch (IOException e) {
				LOGGER.error("Some error occured in Exporting XSLX file: " + e.getMessage());
			}

		}
		
		return true;
	}

	@Override
	public boolean doCompleteSupported() {
		return true;
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	public boolean isCompleteSupported() {
		return super.isCompleteSupported();
	}

}
