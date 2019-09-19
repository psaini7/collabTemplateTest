package com.virtubuild.services.abbeconf.compare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Removeduplicate {
	static String osDir = System.getenv("SystemDrive") + "\\econftemp\\";
	private static final Logger LOGGER = LoggerFactory.getLogger(BomCompare.class);

	public static void main() {

		File file = new File(osDir + "Bom-Compare-Result.xlsx");

		XSSFWorkbook workbook_Output;

		XSSFWorkbook workbook_Output_Dup;

		XSSFWorkbook workbook_Output_Final;

		XSSFWorkbook workbook_Output_Final_2 = null;

		XSSFSheet sheet_Output;

		XSSFSheet sheet_Output_Dup;

		XSSFSheet sheet_Output_Final;

		XSSFRow row_Output;

		XSSFRow row_Output_Dup;

		XSSFRow row_Output_Final;

		XSSFCell cell;

		XSSFRow row_Output_dup;

		// Set<Integer> markrowdup = new HashSet<Integer>();

		// List<HSSFCell> cellStoreList=new ArrayList<HSSFCell>();

		try {

			FileInputStream fistream_Output = new FileInputStream(file);

			workbook_Output = new XSSFWorkbook(fistream_Output);

			File file_dup = new File(file + "Output_Dup.xlsx");

			FileOutputStream fistream_output_dup = new FileOutputStream(file_dup);

			workbook_Output.write(fistream_output_dup);

			fistream_output_dup.close();

			FileInputStream fistream_Output_Dup = new FileInputStream(file + "Output_Dup.xlsx");

			workbook_Output_Dup = new XSSFWorkbook(fistream_Output_Dup);

			// File file_final_2 = new File("C:\\Users\\Satya\\Desktop\\Output_CRQ.xls");

			for (int sheetnum = 0; sheetnum < workbook_Output.getNumberOfSheets(); sheetnum++) {

				Set<Integer> markrowdup = new HashSet<Integer>();

				List<XSSFCell> cellStoreList = new ArrayList<XSSFCell>();

				sheet_Output_Dup = workbook_Output_Dup.getSheetAt(sheetnum);

				sheet_Output = workbook_Output.getSheetAt(sheetnum);

				int lastrow = sheet_Output.getLastRowNum();

				for (int i = 1; i <= lastrow; i++) {

					row_Output_Dup = sheet_Output_Dup.getRow(i);

					row_Output = sheet_Output.getRow(i);

					for (int j = i; j <= lastrow; j++) {

						row_Output_dup = sheet_Output.getRow(j);

						if (i != j)

						{

							if (row_Output.getCell(1).getStringCellValue() == row_Output_dup.getCell(1)
									.getStringCellValue())

							{

								markrowdup.add(j);

								LOGGER.info(row_Output.getCell(1).getStringCellValue() + " : Duplicte row num: " + j);

							}

						}

					}

				}

				Object[] o = markrowdup.toArray();

				Arrays.sort(o);

				for (Object object : o) {

					int row = ((Integer) object).intValue();

					row_Output_Dup = sheet_Output_Dup.getRow(row);

					sheet_Output_Dup.removeRow(row_Output_Dup);

				}

				File file_final = new File(osDir + "BomCompareOutput.xlsx");

				FileOutputStream fostream_final = new FileOutputStream(file_final);

				workbook_Output_Dup.write(fostream_final);

				fostream_final.close();

				FileInputStream fistream_final = new FileInputStream(file_final);

				workbook_Output_Final = new XSSFWorkbook(fistream_final);

				sheet_Output_Final = workbook_Output_Final.getSheetAt(sheetnum);

				for (int i = 0; i <= sheet_Output_Final.getLastRowNum(); i++) {

					row_Output_Final = sheet_Output_Final.getRow(i);

					if (row_Output_Final == null)

						LOGGER.info("Row is null");

					else

					{

						Iterator<Cell> iterator_output_final = row_Output_Final.cellIterator();

						while (iterator_output_final.hasNext()) {

							cell = (XSSFCell) iterator_output_final.next();

							cellStoreList.add(cell);

						}

					}

				}

				Iterator<XSSFCell> iterator_final_data = cellStoreList.iterator();

				File file_final_2 = new File(osDir + "Output_CRQ.xlsx");

				FileOutputStream fostream_final_2 = new FileOutputStream(file_final_2);

				if (workbook_Output_Final_2 == null)

					workbook_Output_Final_2 = new XSSFWorkbook();

				workbook_Output_Final_2.createSheet(workbook_Output.getSheetName(sheetnum));

				XSSFSheet temp = workbook_Output_Final_2.getSheetAt(sheetnum);

				int i = 0;

				int temp_row = 0;

				XSSFRow row = temp.createRow(temp_row);

				while (iterator_final_data.hasNext()) {

					XSSFCell cell2 = iterator_final_data.next();

					row.createCell(i).setCellValue(cell2.getStringCellValue());

					LOGGER.info(cell2.getStringCellValue() + ":  :");

					i++;

					if (i == 4)

					{

						i = 0;

						temp_row++;

						row = temp.createRow(temp_row);

					}

				}

				workbook_Output_Final_2.write(fostream_final_2);

				// fostream_final_2.close();

				fistream_final.close();

				fistream_Output.close();

				fostream_final_2.flush();

				fostream_final_2.close();

			}

			fistream_Output.close();

			fistream_Output_Dup.close();
			
			File f1 = new File(osDir + "Bom-Compare-Result.xlsxOutput_Dup.xlsx");
			File f2 = new File(osDir + "Output_CRQ.xlsx");
			File f3 = new File(osDir + "Bom-Compare-Result.xlsx");
			
			f1.delete();
			f2.delete();
			f3.delete();
		}

		catch (Exception e) {

			LOGGER.error(e.getMessage());

		}

	}

}
