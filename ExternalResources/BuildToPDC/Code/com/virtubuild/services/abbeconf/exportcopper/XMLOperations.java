package com.virtubuild.services.abbeconf.exportcopper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLOperations {
private ArrayList<String> list=null;
private HashMap<Integer,List<ExcelData>> dicMasterDataColumnCopper;
private HashMap<Integer,List<ExcelData>> dicMasterDataColumnStandard;
private ConfigProperties objProperties;
private BufferedReader error;
private BufferedReader op;
private NumberFormat nf;
static String osDir = System.getenv("SystemDrive") + "\\econftemp\\";

private static final Logger LOGGER = LoggerFactory.getLogger(XMLOperations.class);

	public void ReadXML(String sPath){
		Scanner s;
		//Reading and storing input XML
		try {
			s = new Scanner(new File(sPath));
			list = new ArrayList<String>();
			while (s.hasNextLine()){
			    list.add(s.nextLine());
			}
			s.close();				
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage());
		}		
	}
	
	XMLOperations(ConfigProperties objProperties){
		this.objProperties=objProperties;
		DecimalFormat decFormat = new DecimalFormat(); 
		DecimalFormatSymbols decSymbols = decFormat.getDecimalFormatSymbols();
		Character c=new Character(decSymbols.getDecimalSeparator());
		Character c1=new Character('.');
		if(c.equals(c1)) {
			nf=NumberFormat.getInstance(Locale.US);
		}else nf=NumberFormat.getInstance(Locale.GERMAN);
		
	}
	
	
	public boolean DoXMLOperations(HashMap<Integer,List<ExcelData>> dicMasterDataColumnCopper,HashMap<Integer,List<ExcelData>> dicMasterDataColumnStandard,List<ExcelData> listUniqueCopper,List<ExcelData> listUniqueStandard,List<ExcelData> listSummary){
		 boolean bSuccess=true;
		//Assigning dictionaries for copper and standard to the class dictionaries
		if(dicMasterDataColumnCopper!=null) this.dicMasterDataColumnCopper=dicMasterDataColumnCopper;
		if(dicMasterDataColumnStandard!=null) this.dicMasterDataColumnStandard=dicMasterDataColumnStandard;
		ArrayList<String> listXMLRunning;
		//Reading and storing input XML
		ReadXML(objProperties.sCustomPath + "\\CopperConnections\\input.xml");
		PopulateXMLHeaders(list);
		listXMLRunning=PopulateUniqueMaterials(listUniqueCopper,listUniqueStandard);
		listXMLRunning=PopulateSpecificMaterial(listXMLRunning);
		PopulateSummaryTable(listXMLRunning,listSummary);		
		CopyDirectory(objProperties.sCustomPath);
		SaveXML(osDir + "\\ReportGenerator\\input\\output.xml",listXMLRunning);
		GenerateReport(objProperties.sOutputPath);
		CopyOutputToUserSelectedLocation(osDir + "\\ReportGenerator\\output\\eConfigure RBBS Report.pdf",objProperties.sOutputPath);
		
		try {
			deleteDirectory(osDir);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		return bSuccess;
	}
	
	private boolean CopyDirectory(String sSourcePath){
		 boolean bSuccess=true;
		//Copying jar files required for operation
		File sourceDirectory = new File(sSourcePath + "\\ReportGenerator\\");
		File targetDirectory = new File(osDir + "\\ReportGenerator\\");
		directoryCopy(sourceDirectory,targetDirectory);
		return bSuccess;
	}
	
	private void OpenPDFGenerated(String sFilePath)throws IOException, InterruptedException{
			
			Process p;
			p = Runtime.getRuntime()
					.exec("rundll32 url.dll,FileProtocolHandler " + sFilePath);
			p.waitFor();	
		
	}
	private void deleteDirectory(String source) throws IOException {
		FileUtils.deleteDirectory(new File(source));
	}
	
	private boolean CopyOutputToUserSelectedLocation(String sSourcePath,String sUserPath){
		boolean bSuccess=false;
		boolean bFileGenerated=false;
		long startTime = System.currentTimeMillis(); //fetch starting time
		while(bFileGenerated==false)
		{
		    if((System.currentTimeMillis()-startTime)>30000){
		    	break;
		    }
		    File objOutputFile=new File(sSourcePath);
		    if(objOutputFile.exists() && objOutputFile.length()>100) bFileGenerated=true;
		}
		if(bFileGenerated==true){
			
			String fileSuffix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String sDestName="output_" + fileSuffix +".pdf";
			 //File objDest=new File(sUserPath + sDestName);
			 File objDest=new File(sUserPath + ".pdf");
			 File objSource=new File(sSourcePath);
			 try {
				/* try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
					FileUtils.copyFile(objSource, objDest);
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
			 if(objDest.exists()) bSuccess=true;
			 try {
				 JOptionPane.showMessageDialog( new JFrame(), "Report is generated successfully!",  "ABB e-Configure", JOptionPane.INFORMATION_MESSAGE);
				OpenPDFGenerated(sUserPath + ".pdf");
			} catch (IOException | InterruptedException e) {
				LOGGER.error(e.getMessage());
			}
		}
		return bSuccess;
	}
	
	private static void directoryCopy(File source, File dest) {
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	private void GenerateReport(String sUserpath){
		
			Runtime re = Runtime.getRuntime();
			try {
				
				
				Process command = re.exec(
				"cmd /c start /wait javaw -jar " + osDir + "\\ReportGenerator\\ReportGenerator.jar -i "
						+ osDir + "\\ReportGenerator\\input\\output.xml -t " + osDir
						+ "\\ReportGenerator\\documentCopperExport\\" + this.objProperties.sProductLineName + " -o " + osDir + "\\ReportGenerator\\output");
				this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
				this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));
				try {
					command.waitFor();
				} catch (InterruptedException e) {
					LOGGER.error(e.getMessage());
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		
	}
	
	private void SaveXML(String sPath1,ArrayList<String> listXMLRunning){
		if(listXMLRunning!=null){
			listXMLRunning.add("    </blocks>");
			listXMLRunning.add("  </report>");
			listXMLRunning.add("</reportexport>");
			Path out = Paths.get(sPath1);
			try {
				Files.write(out,listXMLRunning,Charset.defaultCharset());
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}
	
	private void PopulateXMLHeaders(ArrayList<String> list){
		if(list!=null){	
			boolean bBlockFound=false;
			for(int iCount=0;iCount<list.size();iCount++){
				String sLine=list.get(iCount);
				if (sLine.contains("01_introduction")&& sLine.contains("block")) bBlockFound=true;
				if(bBlockFound==true){
					if (sLine.contains("id=\"date")) list.set(iCount, FormParameterRow("date",objProperties.sDate));
					if (sLine.contains("id=\"phone_customer")) list.set(iCount, FormParameterRow("phone_customer",""));
					if (sLine.contains("id=\"address_customer")) list.set(iCount, FormParameterRow("address_customer",objProperties.sCustomerAddress));
					if (sLine.contains("id=\"validity_date")) list.set(iCount, FormParameterRow("validity_date",""));
					if (sLine.contains("id=\"description")) list.set(iCount, FormParameterRow("description",objProperties.sDescription));
					if (sLine.contains("id=\"name_customer")) list.set(iCount, FormParameterRow("name_customer",objProperties.sCustomername));
					if (sLine.contains("id=\"zip_code_customer")) list.set(iCount, FormParameterRow("zip_code_customer",objProperties.sCustomerZip));
					if (sLine.contains("id=\"country_customer")) list.set(iCount, FormParameterRow("country_customer",objProperties.sUsCustomerCountry));
					if (sLine.contains("id=\"mail_customer")) list.set(iCount, FormParameterRow("mail_customer",""));
					if (sLine.contains("id=\"name_user")) list.set(iCount, FormParameterRow("name_user",objProperties.sUsername));
					if (sLine.contains("id=\"mail_user")) list.set(iCount, FormParameterRow("mail_user",""));
					if (sLine.contains("id=\"city_customer")) list.set(iCount, FormParameterRow("city_customer",objProperties.sCustomerCity));
					if (sLine.contains("id=\"phone_user")) list.set(iCount, FormParameterRow("phone_user",""));
					if (sLine.contains("id=\"name\"")) list.set(iCount, FormParameterRow("name",objProperties.sUsername));
					if (sLine.contains("id=\"department_user")) list.set(iCount, FormParameterRow("department_user",objProperties.sUserDept));
					if (sLine.contains("id=\"currency")) list.set(iCount, FormParameterRow("currency",""));
					if (sLine.contains("id=\"validity")) list.set(iCount, FormParameterRow("validity",objProperties.sValidity));
					if (sLine.contains("id=\"country_price")) {
						list.set(iCount, FormParameterRow("country_price",""));
						break;
					}
				}
			}			
		}
	}
	
	private ArrayList<String> PopulateUniqueMaterials(List<ExcelData> listUniqueCopper,List<ExcelData> listUniqueStandard){
		ArrayList<String> listXMLRunning = new ArrayList<String>();
		if(list!=null){	
			boolean bBlockFound=false;

			for(int iCount=0;iCount<list.size();iCount++){
				String sLine=list.get(iCount);
				listXMLRunning.add(sLine);
				if(sLine.contains("05_unique_materials") && sLine.contains("block")) bBlockFound=true;						
				if(bBlockFound==true){
					bBlockFound=false;
					if(listUniqueCopper!=null){
						FillUpUniqueMaterialsCopper(listXMLRunning,listUniqueCopper);	
						FillUpUniqueMaterialsStandard(listXMLRunning,listUniqueStandard);
						break;
																
					}					
				}
			}
		}
		return listXMLRunning;
	}
	
	private void FillUpUniqueMaterialsCopper(ArrayList<String> list,List<ExcelData> listExcelData){
		String sBlankRow="                ";
		String sBlankColumn="              ";
		list.add("        <datatables>");
		list.add("          <datatable>");
		list.add("            <columns>");
		//Making header for unique material copper(column)
		FormColumnText(list, "8 Digit Code", "0", sBlankColumn);
		FormColumnText(list, "D", "1", sBlankColumn);
		FormColumnText(list, "DXFLink", "2", sBlankColumn);
		FormColumnText(list, "Length", "3", sBlankColumn);
		FormColumnText(list, "LibraryText", "4", sBlankColumn);
		FormColumnText(list, "PDFLink", "5", sBlankColumn);
		FormColumnText(list, "Quantity", "6", sBlankColumn);
		FormColumnText(list, "SAP IT Component", "7", sBlankColumn);
		FormColumnText(list, "W", "8", sBlankColumn);
		FormColumnText(list, "Weight", "9", sBlankColumn);
		//FormColumnText(list, "InsManText", "10", sBlankColumn);
		//FormColumnText(list, "InsManLink", "11", sBlankColumn);
		list.add("            </columns>");
		if (listExcelData!=null){
			 list.add("            <rows>");
			 for(int iCount=0 ; iCount<listExcelData.size();iCount++ ){				
				 ExcelData objExcelData = listExcelData.get(iCount);
					list.add("              <row order=\"" + iCount + "\">");
					FormRowText(list,"D",objExcelData.sD,sBlankRow);
					FormRowText(list,"SAP IT Component",objExcelData.sSAPITComp,sBlankRow);
					FormRowText(list,"Length",objExcelData.sLength,sBlankRow);
					String sPDFLink=objExcelData.sPDFLibLink;
					if(sPDFLink!=null){
						if (sPDFLink.contains("&") && sPDFLink.contains("&amp;")==false){
							sPDFLink=objExcelData.sPDFLibLink.replace("&","&amp;");
						}else sPDFLink=objExcelData.sPDFLibLink;
					}					
					FormRowText(list,"PDFLink",sPDFLink,sBlankRow);
					FormRowText(list,"W",objExcelData.sW,sBlankRow);
					FormRowText(list,"Quantity",objExcelData.sQuantity,sBlankRow);
					FormRowText(list,"8 Digit Code",objExcelData.sBOMCode,sBlankRow);
					FormRowText(list,"LibraryText",objExcelData.sLibraryText,sBlankRow);
					String sDXFLink=objExcelData.sDXFLibLink;
					if(sDXFLink!=null){
						if (sDXFLink.contains("&") && sDXFLink.contains("&amp;")==false){
							sDXFLink=objExcelData.sDXFLibLink.replace("&","&amp;");
						}else sDXFLink=objExcelData.sDXFLibLink;
					}
					
					FormRowText(list,"DXFLink",sDXFLink,sBlankRow);
					String sWeight=objExcelData.sWeight;
					/*
					String instructionManualText = objExcelData.sInsmanText;
					
					if(null != instructionManualText){
						FormRowText(list,"InsManText",instructionManualText,sBlankRow);
					}
					
					String instructionManual=objExcelData.sInsmanLink;
					if(instructionManual!=null){
						if (instructionManual.contains("&") && instructionManual.contains("&amp;")==false){
							instructionManual=objExcelData.sInsmanLink.replace("&","&amp;");
						}else instructionManual=objExcelData.sInsmanLink;
					}
					
					FormRowText(list,"InsManLink",instructionManual,sBlankRow);*/
					
					if(objExcelData.sWeight!=null){
						
						double number;
						try {
							
							if(sWeight.matches("\\d+\\.\\d+")){
								
								number = Double.valueOf(sWeight);
								
							}
							
							else{
								
								Long numberLong = new Long(sWeight);
								
								//number=(double) nf.parse(sWeight);
							    
							    number = numberLong.doubleValue();
							    
							}
							
						
						//double dWeight=Double.valueOf(sWeight);
						sWeight = String.valueOf((double) Math.round(number * 100) / 100);
						} catch (Exception e) {
							LOGGER.error(e.getMessage());
						}
					}					
					FormRowText(list,"Total_Weight",sWeight,sBlankRow);
					list.add("              </row>");
			 }			
		}
		list.add("            </rows>");
		list.add("          </datatable>");
		list.add("        </datatables>");
		list.add("      </block>");
		
	}
	
	private void FillUpUniqueMaterialsStandard(ArrayList<String> list,List<ExcelData> listExcelData){
		String sBlankRow="                ";
		String sBlankColumn="              ";
		list.add("      <block id=\"06_unique_materials_standard\" order=\"6\" refid=\"06_unique_materials_standard\" type=\"datablock\">");
		list.add("        <datatables>");
		list.add("          <datatable>");
		list.add("            <columns>");
		//Making header for unique material standard(column)
		FormColumnText(list, "8_Digit_Code", "0", sBlankColumn);
		FormColumnText(list, "DIN", "1", sBlankColumn);
		FormColumnText(list, "Description", "2", sBlankColumn);
		FormColumnText(list, "ISO", "3", sBlankColumn);
		FormColumnText(list, "L", "4", sBlankColumn);
		FormColumnText(list, "M", "5", sBlankColumn);
		FormColumnText(list, "Quantity", "6", sBlankColumn);
		FormColumnText(list, "UNI", "7", sBlankColumn);
		list.add("            </columns>");
		if (listExcelData!=null){
			 list.add("            <rows>");
			 for(int iCount=0 ; iCount<listExcelData.size();iCount++ ){				
				 ExcelData objExcelData = listExcelData.get(iCount);
					list.add("              <row order=\"" + iCount + "\">");
					FormRowText(list,"UNI",objExcelData.sUNI,sBlankRow);
					FormRowText(list,"Description",objExcelData.sDesc,sBlankRow);
					FormRowText(list,"8_Digit_Code",objExcelData.sBOMCode,sBlankRow);
					FormRowText(list,"ISO",objExcelData.sISO,sBlankRow);
					FormRowText(list,"DIN",objExcelData.sDIN,sBlankRow);
					FormRowText(list,"Final_quantity",objExcelData.sQuantity,sBlankRow);
					FormRowText(list,"L",objExcelData.sL,sBlankRow);
					FormRowText(list,"M",objExcelData.sM,sBlankRow);
					list.add("              </row>");
			 }			
		}
		list.add("            </rows>");
		list.add("          </datatable>");
		list.add("        </datatables>");
		list.add("      </block>");
		
	}
	
	private void PopulateSummaryTable(ArrayList<String> listXMLRunning,List<ExcelData> listSummary){
		if(listXMLRunning!=null){
			listXMLRunning.add("      <block id=\"09_summary_header\" order=\"9\" refid=\"09_summary_header\" type=\"datablock\">");
			listXMLRunning.add("        <parameters>");
			listXMLRunning.add("          <parameter id=\"Property\" value=\"empty\"/>");
			listXMLRunning.add("        </parameters>");
			listXMLRunning.add("      </block>");
			
				String sBlankRow="                ";
				String sBlankColumn="              ";
				listXMLRunning.add("      <block id=\"010_summary_table\" order=\"10\" refid=\"010_summary_table\" type=\"datablock\">");
				listXMLRunning.add("        <datatables>");
				listXMLRunning.add("          <datatable>");
				listXMLRunning.add("            <columns>");
				//Making header for summary(column)
				FormColumnText(listXMLRunning, "8 Digit Code", "0", sBlankColumn);
				FormColumnText(listXMLRunning, "D", "1", sBlankColumn);
				FormColumnText(listXMLRunning, "DXFLink", "2", sBlankColumn);
				FormColumnText(listXMLRunning, "Length", "3", sBlankColumn);
				FormColumnText(listXMLRunning, "LibraryText", "4", sBlankColumn);
				FormColumnText(listXMLRunning, "PDFLink", "5", sBlankColumn);
				FormColumnText(listXMLRunning, "Quantity", "6", sBlankColumn);
				FormColumnText(listXMLRunning, "SAP IT Component", "7", sBlankColumn);
				FormColumnText(listXMLRunning, "W", "8", sBlankColumn);
				FormColumnText(listXMLRunning, "Weight", "9", sBlankColumn);
				listXMLRunning.add("            </columns>");
				if (listSummary!=null){
					listXMLRunning.add("            <rows>");
					 for(int iCount=0 ; iCount<listSummary.size();iCount++ ){				
						 ExcelData objExcelData = listSummary.get(iCount);
						 listXMLRunning.add("              <row order=\"" + iCount + "\">");
							FormRowText(listXMLRunning,"D",objExcelData.sD,sBlankRow);
							FormRowText(listXMLRunning,"SAP IT Component",objExcelData.sSAPITComp,sBlankRow);
							FormRowText(listXMLRunning,"Length",objExcelData.sLength,sBlankRow);
							String sPDFLink=objExcelData.sPDFLibLink;
							if(sPDFLink!=null){
								if (sPDFLink.contains("&") && sPDFLink.contains("&amp;")==false){
									sPDFLink=objExcelData.sPDFLibLink.replace("&","&amp;");
								}else sPDFLink=objExcelData.sPDFLibLink;
							}
							
							FormRowText(listXMLRunning,"PDFLink",sPDFLink,sBlankRow);
							FormRowText(listXMLRunning,"W",objExcelData.sW,sBlankRow);
							FormRowText(listXMLRunning,"Summary_final_quantity",objExcelData.sQuantity,sBlankRow);
							FormRowText(listXMLRunning,"8 Digit Code",objExcelData.sBOMCode,sBlankRow);
							FormRowText(listXMLRunning,"LibraryText",objExcelData.sLibraryText,sBlankRow);
							String sDXFLink=objExcelData.sDXFLibLink;
							if(sDXFLink!=null){
								if (sDXFLink.contains("&") && sDXFLink.contains("&amp;")==false){
									sDXFLink=objExcelData.sDXFLibLink.replace("&","&amp;");
								}else sDXFLink=objExcelData.sDXFLibLink;
							}
							
							FormRowText(listXMLRunning,"DXFLink",sDXFLink,sBlankRow);
							FormRowText(listXMLRunning,"Weight",objExcelData.sWeight,sBlankRow);
							listXMLRunning.add("              </row>");
					 }			
				}
				listXMLRunning.add("            </rows>");
				listXMLRunning.add("          </datatable>");
				listXMLRunning.add("        </datatables>");
				listXMLRunning.add("      </block>");
			
		}
	}
	
	
	private ArrayList<String>  PopulateSpecificMaterial(ArrayList<String> listXMLRunning){
		if(listXMLRunning!=null){	
			listXMLRunning.add("      <block id=\"07_specific_material\" order=\"7\" refid=\"07_specific_material\" type=\"datablock\">");
			listXMLRunning.add("        <parameters>");
			listXMLRunning.add("          <parameter id=\"Property\" value=\"empty\"/>");
			listXMLRunning.add("        </parameters>");
			listXMLRunning.add("      </block>");
			if(dicMasterDataColumnCopper!=null){
					for (Map.Entry<Integer,List<ExcelData>> entry : dicMasterDataColumnCopper.entrySet()){
						int iColNo=entry.getKey();							
						for (Map.Entry<Integer,List<ExcelData>> entryStandard : dicMasterDataColumnStandard.entrySet()){
							int iColNoStandard=entryStandard.getKey();								
							if(iColNo==iColNoStandard){
								List<ExcelData> listCopperExcel=entry.getValue();
								List<ExcelData> listStandardExcel=entryStandard.getValue();
								FillUpSpecificMaterialCopper(listXMLRunning,iColNo,listCopperExcel);
								FillUpSpecificMaterialStandard(listXMLRunning,iColNo,listStandardExcel);
								break;
							}
						}
					}																						
			}
						
			/*listXMLRunning.add("    </blocks>");
			listXMLRunning.add("  </report>");
			listXMLRunning.add("</reportexport>");
			Path out = Paths.get("D:\\Test\\XML\\output.xml");
			try {
				Files.write(out,listXMLRunning,Charset.defaultCharset());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		return listXMLRunning;
		
	}
	
	private void FillUpSpecificMaterialStandard(ArrayList<String> list,int iColNumber,List<ExcelData> listExcelData){
		String sBlankRow="                ";
		String sBlankColumn="              ";
		String sVerNumber=String.valueOf(((iColNumber-1) *3)+1);
		
		
		sVerNumber=String.valueOf(((iColNumber-1) *3)+3);
		//Making header for specific material standard(column)
		list.add("      <block id=\"08_specific_material_standard\" order=\"8." + sVerNumber + "\" refid=\"08_specific_material_standard\" type=\"datablock\">");
		list.add("        <datatables>");
		list.add("          <datatable>");
		list.add("            <columns>");
		FormColumnText(list, "8_Digit_Code", "0", sBlankColumn);
		FormColumnText(list, "DIN", "1", sBlankColumn);
		FormColumnText(list, "Description", "2", sBlankColumn);
		FormColumnText(list, "ISO", "3", sBlankColumn);
		FormColumnText(list, "L", "4", sBlankColumn);
		FormColumnText(list, "M", "5", sBlankColumn);
		FormColumnText(list, "Quantity", "6", sBlankColumn);
		FormColumnText(list, "UNI", "7", sBlankColumn);		
		list.add("            </columns>");
		
		if (listExcelData!=null){
			 list.add("            <rows>");
			 for(int iCount=0 ; iCount<listExcelData.size();iCount++ ){				
				 ExcelData objExcelData = listExcelData.get(iCount);
					list.add("              <row order=\"" + iCount + "\">");
					FormRowText(list,"UNI",objExcelData.sUNI,sBlankRow);
					FormRowText(list,"Description",objExcelData.sDesc,sBlankRow);
					FormRowText(list,"8_Digit_Code",objExcelData.sBOMCode,sBlankRow);
					FormRowText(list,"ISO",objExcelData.sISO,sBlankRow);
					FormRowText(list,"DIN",objExcelData.sDIN,sBlankRow);
					FormRowText(list,"Final_quantity",objExcelData.sQuantity,sBlankRow);
					FormRowText(list,"L",objExcelData.sL,sBlankRow);
					FormRowText(list,"M",objExcelData.sM,sBlankRow);					
					list.add("              </row>");
			 }			
		}
		list.add("            </rows>");
		list.add("          </datatable>");
		list.add("        </datatables>");
		list.add("      </block>");
		
		
	}
	
	
	private void FillUpSpecificMaterialCopper(ArrayList<String> list,int iColNumber,List<ExcelData> listExcelData){
		String sBlankRow="                ";
		String sBlankColumn="              ";
		String sColNumber=String.valueOf(iColNumber);
		String sVerNumber=String.valueOf(((iColNumber-1) *3)+1);
		//Making header for specific material column
		list.add("      <block id=\"08_specific_material_column\" order=\"8." + sVerNumber + "\" refid=\"08_specific_material_column\" type=\"datablock\">");
		list.add("        <parameters>");
		list.add("          <parameter id=\"order\" value=\"" + sColNumber + "\"/>");
		list.add("        </parameters>");
		list.add("      </block>");
		
		sVerNumber=String.valueOf(((iColNumber-1) *3)+2);
		//Making header for specific material copper(column)
		list.add("      <block id=\"08_specific_material_copper\" order=\"8." + sVerNumber + "\" refid=\"08_specific_material_copper\" type=\"datablock\">");
		list.add("        <datatables>");
		list.add("          <datatable>");
		list.add("            <columns>");
		FormColumnText(list, "8 Digit Code", "0", sBlankColumn);
		FormColumnText(list, "D", "1", sBlankColumn);
		FormColumnText(list, "DXFLink", "2", sBlankColumn);
		FormColumnText(list, "Length", "3", sBlankColumn);
		FormColumnText(list, "LibraryText", "4", sBlankColumn);
		FormColumnText(list, "PDFLink", "5", sBlankColumn);
		FormColumnText(list, "Quantity", "6", sBlankColumn);
		FormColumnText(list, "SAP IT Component", "7", sBlankColumn);
		FormColumnText(list, "W", "8", sBlankColumn);
		FormColumnText(list, "Weight", "9", sBlankColumn);
		FormColumnText(list, "InsManText", "10", sBlankColumn);
		FormColumnText(list, "InsManLink", "11", sBlankColumn);
		list.add("            </columns>");
		
		if (listExcelData!=null){
			list.add("            <rows>");
			 for(int iCount=0 ; iCount<listExcelData.size();iCount++ ){
				 ExcelData objExcelData = listExcelData.get(iCount);				 
					list.add("              <row order=\"" + iCount + "\">");
					FormRowText(list,"D",objExcelData.sD,sBlankRow);
					FormRowText(list,"SAP IT Component",objExcelData.sSAPITComp,sBlankRow);
					FormRowText(list,"Length",objExcelData.sL,sBlankRow);
					String sPDFLink=objExcelData.sPDFLibLink;
					if(sPDFLink!=null){
						if (sPDFLink.contains("&") && sPDFLink.contains("&amp;")==false){
							sPDFLink=objExcelData.sPDFLibLink.replace("&","&amp;");
						}else sPDFLink=objExcelData.sPDFLibLink;
					}
				
					FormRowText(list,"PDFLink",sPDFLink,sBlankRow);
					FormRowText(list,"W",objExcelData.sW,sBlankRow);
					FormRowText(list,"Final_quantity",objExcelData.sQuantity,sBlankRow);
					FormRowText(list,"8 Digit Code",objExcelData.sBOMCode,sBlankRow);
					FormRowText(list,"LibraryText",objExcelData.sLibraryText,sBlankRow);
					String sDXFLink=objExcelData.sDXFLibLink;
					if(sDXFLink!=null){
						if (sDXFLink.contains("&") && sDXFLink.contains("&amp;")==false){
							sDXFLink=objExcelData.sDXFLibLink.replace("&","&amp;");
						}else sDXFLink=objExcelData.sDXFLibLink;
					}
					FormRowText(list,"DXFLink",sDXFLink,sBlankRow);
					
					String instructionManualText = objExcelData.sInsmanText;
					
					if(null != instructionManualText){
						FormRowText(list,"InsManText",instructionManualText,sBlankRow);
					}
					
					String instructionManual=objExcelData.sInsmanLink;
					if(instructionManual!=null){
						if (instructionManual.contains("&") && instructionManual.contains("&amp;")==false){
							instructionManual=objExcelData.sInsmanLink.replace("&","&amp;");
						}else instructionManual=objExcelData.sInsmanLink;
					}
					
					FormRowText(list,"InsManLink",instructionManual,sBlankRow);
					
					
					FormRowText(list,"Weight",objExcelData.sWeight,sBlankRow);
					list.add("              </row>");
			 }
			
		}
		list.add("            </rows>");
		list.add("          </datatable>");
		list.add("        </datatables>");
		list.add("      </block>");
		
		/*Path out = Paths.get("D:\\Test\\XML\\output.xml");
		try {
			Files.write(out,list,Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private void FormRowText(ArrayList<String> list,String sKey, String sValue,String sBlank){
		String sWorkingText=sBlank + "<datum id=\"" + sKey + "\" value=\"" +sValue + "\"/>";
		list.add(sWorkingText);
	}
	private void FormColumnText(ArrayList<String> list,String sKey, String sValue,String sBlank){
		String sWorkingText=sBlank + "<column name=\"" + sKey + "\" order=\"" +sValue + "\"/>";
		list.add(sWorkingText);
	}
	 private String FormParameterRow(String sParameterName,String sParameterValue){
		 String sReturnString="";
		 sReturnString="          <parameter id=\"" + sParameterName + "\" value=\"" + sParameterValue + "\"/>";
		 return sReturnString;
	 }
}
