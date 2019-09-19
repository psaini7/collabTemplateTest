package com.virtubuild.services.abbeconf.exportcopper;
import java.io.*;
import java.math.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import com.ibm.icu.text.NumberFormat;
import com.virtubuild.services.clientgui.copperforecast.MyDataNode;
import com.virtubuild.services.clientgui.copperforecast.TreeTableMain;
import com.virtubuild.services.clientgui.custom.SyndicateBean;




public class CopperCheck {
	//Class level variables
	private ConfigProperties objProperties;
	private List<ExcelData> listMasterExcelDataRBBS;
	private List<ExcelData> listMasterExcelDataN185;
	private HashMap<Integer,List<ExcelData>> dicMasterDataColumnCopper;
	private HashMap<Integer,List<ExcelData>> dicMasterDataColumnStandard;
	private List<ExcelData> listMasterExcelDataCopper;
	private List<ExcelData> listMasterExcelDataStandard;
	private List<ExcelData> listMasterStandardFileData;
	private List<ExcelData> listUniqueCopper;
	private List<ExcelData> listUniqueStandard;
	private List<ExcelData> listUniqueSAP;
	private List<ExcelData> listMasterExcelSyndication;
	private List<ExcelData> listMasterExcelABBLibraryLinks;
	private static NumberFormat nf;
	
	CopperCheck(ConfigProperties i_objProperties){
		objProperties=i_objProperties;
		DecimalFormat decFormat = new DecimalFormat(); 
		DecimalFormatSymbols decSymbols = decFormat.getDecimalFormatSymbols();
		Character c=new Character(decSymbols.getDecimalSeparator());
		Character c1=new Character('.');
		if(c.equals(c1)) {
			nf=NumberFormat.getInstance(Locale.US);
		}else nf=NumberFormat.getInstance(Locale.GERMAN);
			
	}
	
	private void WriteToText(String sPath){
		 /*BufferedWriter writer = null;
	        try {
	            //create a temporary file
	            String timeLog = new SimpleDateFormat("yyyyMMdd_HH").format(Calendar.getInstance().getTime());
	            File logFile = new File("D:\\Test\\Logs\\" + timeLog);

	            // This will output the full path where the file will be written to...
	            //System.out.println(logFile.getCanonicalPath());

	            writer = new BufferedWriter(new FileWriter(logFile,true));
	            writer.write(sPath);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }
	        }*/
	}
	
	
	
	
	public void DoExportOperations(ConfigProperties i_objProperties) throws SecurityException, IOException{
		
		objProperties=i_objProperties;
		 HashMap<Integer, List<String>> SortedMap;
		 //Getting the list of all exported codes from Build
		 List<MyDataNode> listNodesForCopper= TreeTableMain.listNodesForCopper;
		 WriteToText(listNodesForCopper.size() + "\n");
		 
		 XMLOperations objXMLOps= new XMLOperations(objProperties);
		 //Identifying the copper parts with quantity zero and arrange it according to column number
		 SortedMap= IdentifyCopperCodesWithQuantityZero(listNodesForCopper);
		
			if(com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelSyndication==null || com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelSyndication.isEmpty() || com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelABBLibraryLinks==null || com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelABBLibraryLinks.isEmpty()) {
				 //String pathSyndicate = objProperties.sInputPath + "Syndication_SYN85233(02-12-2019)(11-37-CET)_WithLinks.xlsx";
				 //listMasterExcelSyndication=ReadExcelValuesFromSyndicationExcel(pathSyndicate);
				com.virtubuild.services.clientgui.custom.TreeTableMain.readInputExcelFiles();
			}
			
			//Reading syndication excel
			listMasterExcelSyndication=com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelSyndication;
			
			//Reading ABB Library Links excel
		    listMasterExcelABBLibraryLinks=com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelABBLibraryLinks;
		 
		
		
		 
		 ///WriteToText(pathSyndicate + " *** " + pathABBLibraryLinks + "\n");
		 
		 //Checking for productline
		 if(objProperties.sProductLine.toLowerCase().startsWith("rbbs")){
			 ExportCopperPartsRBBS(SortedMap);
			 ExportStandardPartsRBBS(SortedMap);
			 FormListsForXML();
			 objXMLOps.DoXMLOperations(dicMasterDataColumnCopper, dicMasterDataColumnStandard,listUniqueCopper,listUniqueStandard,listUniqueSAP);
			 //objWord.CloseWordDocument();
			 //ConvertToPDF(objProperties.sResourcePath,objProperties.sOutputPath,objProperties.sOutputFileName + ".docx");
		 }else if (objProperties.sProductLine.toLowerCase().startsWith("n185")){
			 ExportCopperPartsN185(SortedMap);
			 ExportStandardPartsN185(SortedMap);
			 FormListsForXML();
			 objXMLOps.DoXMLOperations(dicMasterDataColumnCopper, dicMasterDataColumnStandard,listUniqueCopper,listUniqueStandard,listUniqueSAP);
			 //ConvertToPDF(objProperties.sResourcePath,objProperties.sOutputPath,objProperties.sOutputFileName + ".docx");
			 //objWord.CloseWordDocument();
			 
		 }
		
		 //SavePDF();
		
	}
	
	private void FormListsForXML(){
		DoCopperSectionDetails();
		DoStandardDetails();
		DoSummary();
	}
	
	private void DoCopperSectionDetails(){
		if(listMasterExcelDataCopper!=null){
			List<String> listUniqueSections = new ArrayList<String>();
			listUniqueCopper=new ArrayList<ExcelData>();
			//Iterating through the list to find unique values of section
			for(int iCount=0 ; iCount<listMasterExcelDataCopper.size();iCount++ ){
				ExcelData objExcelData=listMasterExcelDataCopper.get(iCount);
				String sSection=objExcelData.sSection;
				if(listUniqueSections.contains(sSection)==false)listUniqueSections.add(sSection);			
			}
			if(listUniqueSections.size()!=0){
				for(int iCount=0 ; iCount<listUniqueSections.size();iCount++ ){
					ExcelData objNewExcelData = new ExcelData();
					String sSectionUnique=listUniqueSections.get(iCount);
					if(sSectionUnique.contains("null")) {
						continue;
					}
					objNewExcelData.sSection=sSectionUnique;
					double dLength=0;
					String sBomCode="";
					for(int iCount1=0 ; iCount1<listMasterExcelDataCopper.size();iCount1++ ){
						ExcelData objExcelData=listMasterExcelDataCopper.get(iCount1);
						String sSection=objExcelData.sSection;
						if(sSection.contains("null")==false) {
							if(sBomCode.equals(objExcelData.sBOMCode)==false){
								sBomCode=objExcelData.sBOMCode;
								if(sSectionUnique.equals(sSection)){
									if(objNewExcelData.sW==null)objNewExcelData.sW= objExcelData.sW;
									if(objNewExcelData.sD==null)objNewExcelData.sD= objExcelData.sD;
									dLength=dLength + (Double.valueOf(objExcelData.sLength) * Double.valueOf(objExcelData.sQuantity));
								}
							}	
							
							String oneHyperlink = null;
		    		 		
		    		 		SyndicateBean syndicateBean =  com.virtubuild.services.clientgui.custom.TreeTableMain.dataSyndicate.get(objExcelData.sBOMCode);
		    		 		
		    		 		String instructionManualID = syndicateBean.getInstructionManualID();
		    		 		
		    		 		List<String> hyperLinks  = (List<String>) com.virtubuild.services.clientgui.custom.TreeTableMain.dataABBLinks.get(instructionManualID);
		    		 		
		    		 		if(null != hyperLinks && !hyperLinks.isEmpty()){
								 
								 for (String hyperlink : hyperLinks){
									 oneHyperlink = hyperlink;
								 }
		    		 		
		    		 		}
		    		 		
		    		 		objNewExcelData.sInsmanText = instructionManualID;
		    		 		objNewExcelData.sInsmanLink = oneHyperlink;
							
							
						}
													
					}	
					objNewExcelData.sLength=String.valueOf(dLength);
					double dWeight=(8900* Double.valueOf(objNewExcelData.sW)*Double.valueOf(objNewExcelData.sD)* dLength)/1000000000;
					objNewExcelData.sWeight=String.format("%.2f", dWeight);
					listUniqueCopper.add(objNewExcelData);
				}
			}
			
			
		}
	}
	
	private void DoStandardDetails(){
		if(listMasterExcelDataStandard!=null){
			List<String> listUniqueDescs = new ArrayList<String>();
			listUniqueStandard=new ArrayList<ExcelData>();
			//Iterating through the list to find unique values of section
			for(int iCount=0 ; iCount<listMasterExcelDataStandard.size();iCount++ ){
				ExcelData objExcelData=listMasterExcelDataStandard.get(iCount);
				String sDescription=objExcelData.sDesc;
				if(listUniqueDescs.contains(sDescription)==false)listUniqueDescs.add(sDescription);			
			}
			if(listUniqueDescs.size()!=0){
				for(int iCount=0 ; iCount<listUniqueDescs.size();iCount++ ){
					ExcelData objNewExcelData = new ExcelData();
					String sDescUnique=listUniqueDescs.get(iCount);
					objNewExcelData.sDesc=sDescUnique;
					int iQuantity=0;
					for(int iCount1=0 ; iCount1<listMasterExcelDataStandard.size();iCount1++ ){
						ExcelData objExcelData=listMasterExcelDataStandard.get(iCount1);
						String sDescr=objExcelData.sDesc;
						if(sDescUnique.equals(sDescr)){
							if(objNewExcelData.sM==null)objNewExcelData.sM= objExcelData.sM;
							if(objNewExcelData.sL==null)objNewExcelData.sL= objExcelData.sL;
							if(objNewExcelData.sDIN==null)objNewExcelData.sDIN= objExcelData.sDIN;
							if(objNewExcelData.sISO==null)objNewExcelData.sISO= objExcelData.sISO;
							if(objNewExcelData.sUNI==null)objNewExcelData.sUNI= objExcelData.sUNI;							
							iQuantity=iQuantity + Integer.valueOf(objExcelData.sQuantity);
						}									
					}						
					objNewExcelData.sQuantity=String.valueOf(iQuantity);
					listUniqueStandard.add(objNewExcelData);
				}
			}						
		}
	}

	private void DoSummary(){
		if(listMasterExcelDataCopper!=null){
			List<String> listUniqueDescs = new ArrayList<String>();
			listUniqueSAP=new ArrayList<ExcelData>();
			//Iterating through the list to find unique values of section
			for(int iCount=0 ; iCount<listMasterExcelDataCopper.size();iCount++ ){
				ExcelData objExcelData=listMasterExcelDataCopper.get(iCount);
				String sSAPComp=objExcelData.sSAPITComp;
				if(listUniqueDescs.contains(sSAPComp)==false)listUniqueDescs.add(sSAPComp);			
			}
			if(listUniqueDescs.size()!=0){
				for(int iCount=0 ; iCount<listUniqueDescs.size();iCount++ ){
					ExcelData objNewExcelData = new ExcelData();
					String sUniqueSAP=listUniqueDescs.get(iCount);
					objNewExcelData.sSAPITComp=sUniqueSAP;
					int iQuantity=0;
					for(int iCount1=0 ; iCount1<listMasterExcelDataCopper.size();iCount1++ ){
						ExcelData objExcelData=listMasterExcelDataCopper.get(iCount1);
						String sSAPITComp=objExcelData.sSAPITComp;
						if(sUniqueSAP.equals(sSAPITComp)){
							if(objNewExcelData.sW==null)objNewExcelData.sW= objExcelData.sW;
							if(objNewExcelData.sD==null)objNewExcelData.sD= objExcelData.sD;
							if(objNewExcelData.sLibraryText==null)objNewExcelData.sLibraryText= objExcelData.sLibraryText;
							if(objNewExcelData.sDXFLibLink==null)objNewExcelData.sDXFLibLink= objExcelData.sDXFLibLink;
							if(objNewExcelData.sPDFLibLink==null)objNewExcelData.sPDFLibLink= objExcelData.sPDFLibLink;							
							iQuantity=iQuantity + Integer.valueOf(objExcelData.sQuantity);
						}									
					}						
					objNewExcelData.sQuantity=String.valueOf(iQuantity);
					listUniqueSAP.add(objNewExcelData);
				}
			}						
		}
	}
	
	
	
	private void ExportStandardPartsRBBS( HashMap<Integer, List<String>> SortedMap) throws FileNotFoundException{
	
		if (SortedMap!= null){
			WriteToText("Exporting Standard Parts");
			
			FormDataStructureForExportStandard(SortedMap,listMasterExcelDataRBBS);	
			
		}		
	}
	
	
	private void ExportStandardPartsN185( HashMap<Integer, List<String>> SortedMap) throws FileNotFoundException{
		
		if (SortedMap!= null){
			FormDataStructureForExportStandard(SortedMap,listMasterExcelDataN185);	
			
		}		
	}
	
	
	
	public static List<ExcelData> ReadStandardExcelFile2(String sFilePath){
		
		//String sFilePath=objProperties.sInputPath + objProperties.sStandardFileNameIn;
		FileInputStream ip = null;
   	 	//Instantiating a list of Excel Data
		List<ExcelData> listMasterExcelData=new ArrayList<ExcelData>();
	try {				
		//Creating input stream
		ip = new FileInputStream(new File(sFilePath));
		//instantiating excel workbook
		XSSFWorkbook objExcelWorkbook = new XSSFWorkbook(ip);
		XSSFSheet objWorkingSheet = objExcelWorkbook.getSheet("Sheet2");
		
	    //Getting size and dimension of range of data
		long rowCnt = 0;//row counter
	    rowCnt = objWorkingSheet.getPhysicalNumberOfRows();
	    int iFirstRow =objWorkingSheet.getFirstRowNum();
	    			   
	    //Iterating through each row
	    for (int iRowCnt=iFirstRow+1; iRowCnt <= rowCnt; iRowCnt++) {
	    	
	    	//instantiating new excelData class
	    	ExcelData clsExcelData = new ExcelData();
	    	Row objCurrentRow = objWorkingSheet.getRow(iRowCnt);
	    	
	    	//Reading the required columns from the row
	    	clsExcelData.sBOMCode = GetCellValue(objCurrentRow,0);	
	 		clsExcelData.sDesc = GetCellValue(objCurrentRow,2);			
	 		clsExcelData.sBech = GetCellValue(objCurrentRow,3);			      		 					   
	 		clsExcelData.sM = RemoveDotAndZero(GetCellValue(objCurrentRow,4));			    
	 		clsExcelData.sL = RemoveDotAndZero(GetCellValue(objCurrentRow,5));
	 		clsExcelData.sDIN = RemoveDotAndZero(GetCellValue(objCurrentRow,6));
	 		clsExcelData.sISO = RemoveDotAndZero(GetCellValue(objCurrentRow,7));
	 		clsExcelData.sUNI = RemoveDotAndZero(GetCellValue(objCurrentRow,8));
	 						
	 		//Assigning to the class object
	    	listMasterExcelData.add(clsExcelData);
	    		    			          	
	    }
	    //Closing workbook
		objExcelWorkbook.close();
		return listMasterExcelData;
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		
		e.printStackTrace();
		return listMasterExcelData;
	}
		
		
	}
	//*************************************************************************************Read All the data from the excel for Standard Parts***********************************************************************
	@SuppressWarnings("unused")
	public List<ExcelData> ReadExcelValuesForStandardPartsRBBS(String i_sFilePath) {
	    	FileInputStream ip = null;
	    	 //Instantiating a list of Excel Data
	    	List<ExcelData> listMasterExcelData=new ArrayList<ExcelData>();
			try {				
				//Creating input stream
				ip = new FileInputStream(new File(i_sFilePath));
				//instantiating excel workbook
				XSSFWorkbook objExcelWorkbook = new XSSFWorkbook(ip);
				XSSFSheet objWorkingSheet = objExcelWorkbook.getSheet("BOM_CONNECTIONS");
				
			    //Getting size and dimension of range of data
				long rowCnt = 0;//row counter
			    rowCnt = objWorkingSheet.getPhysicalNumberOfRows();
			    int iFirstRow =objWorkingSheet.getFirstRowNum();
			    			   
			    //Iterating through each row
			    for (int iRowCnt=iFirstRow+2; iRowCnt <= rowCnt; iRowCnt++) {
			    	
			    	//instantiating new excelData class
			    	ExcelData clsExcelData = new ExcelData();
			    	Row objCurrentRow = objWorkingSheet.getRow(iRowCnt);
			    	String sDeleted=GetCellValue(objCurrentRow,17);//Deleted	
			    	if(sDeleted.equals("DELETED")==false){
			    		//Reading the required columns from the row
	    		 		clsExcelData.sBOMCode = GetCellValue(objCurrentRow,0);//BOM Code				
	    		 		//clsExcelData.sDeleted = GetCellValue(objCurrentRow,17);//Deleted			      		 					   
	    		 		clsExcelData.sSAPITComp = RemoveDotAndZero(GetCellValue(objCurrentRow,6));//SAP IT Component			    
	    		 		clsExcelData.sQuantity = RemoveDotAndZero(GetCellValue(objCurrentRow,8));//Quantity	
	    		 		clsExcelData.sMaterial = GetCellValue(objCurrentRow,19);//Quantity	
	    		 						
	    		 		//Assigning to the class object
				    	listMasterExcelData.add(clsExcelData);
			    	}
			    	
			    	
			    		    			          	
			    }
			    //Closing workbook
				objExcelWorkbook.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return listMasterExcelData;
			 
	    }  
	public static List<ExcelData> ReadExcelValuesCopperN185Mounted(String i_sFilePath,boolean isFlatpack) {
    	FileInputStream ip = null;
    	 //Instantiating a list of Excel Data
    	List<ExcelData> listMasterExcelData=new ArrayList<ExcelData>();
		try {				
			//Creating input stream
			ip = new FileInputStream(new File(i_sFilePath));
			//instantiating excel workbook
			XSSFWorkbook objExcelWorkbook = new XSSFWorkbook(ip);
			XSSFSheet objWorkingSheet = objExcelWorkbook.getSheet("BOM Mounted");
			
		    //Getting size and dimension of range of data
			long rowCnt = 0;//row counter
		    rowCnt = objWorkingSheet.getPhysicalNumberOfRows();
		    int iFirstRow =objWorkingSheet.getFirstRowNum();
		    			   
		    //Iterating through each row
		    for (int iRowCnt=iFirstRow+2; iRowCnt <= rowCnt; iRowCnt++) {
		    	
		    	//instantiating new excelData class
		    	ExcelData clsExcelData = new ExcelData();
		    	Row objCurrentRow = objWorkingSheet.getRow(iRowCnt);
		    	
		    	//Reading the required columns from the row
		    	//String sClassification=GetCellValue(objCurrentRow,20);
		    	String sMaterial=GetCellValue(objCurrentRow,21);
		    	if (sMaterial.equalsIgnoreCase("Standard") || sMaterial.equalsIgnoreCase("Copper")){
		    		String sDeleted=GetCellValue(objCurrentRow,19);//Deleted	
			    	if(sDeleted.equals("DELETED")==false){
			    		if(isFlatpack==true){
			    			clsExcelData.sBOMCode = GetCellValue(objCurrentRow,2);//BOM Code
			    		}else{
			    			clsExcelData.sBOMCode = GetCellValue(objCurrentRow,0);//BOM Code
			    		}			
				 		//clsExcelData.sDeleted = GetCellValue(objCurrentRow,19);//Deleted			      		 					   
				 		clsExcelData.sSAPITComp = GetCellValue(objCurrentRow,7);//SAP IT Component			    
				 		clsExcelData.sQuantity = RemoveDotAndZero(GetCellValue(objCurrentRow,11));//Quantity	
				 		clsExcelData.sMaterial = sMaterial;//GetCellValue(objCurrentRow,21);//Quantity	
				 		clsExcelData.sPDFLibLink = GetCellValue(objCurrentRow,23);//Link
				 		clsExcelData.sW=GetCellValue(objCurrentRow, 25);//Width
				 		clsExcelData.sLibraryText=GetCellValue(objCurrentRow, 22);//Library text
				 		clsExcelData.sD=GetCellValue(objCurrentRow, 26);//Depth
				 		clsExcelData.sLibraryText=GetCellValue(objCurrentRow, 22);
				 		//Assigning to the class object
				    	listMasterExcelData.add(clsExcelData);
			    	}
		    		
		    	}
		 		
		    		    			          	
		    }
		    //Closing workbook
			objExcelWorkbook.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listMasterExcelData;
		 
    }   
	
	public List<ExcelData> ReadExcelValuesCopperN185Flatpack(String i_sFilePath,String sSheetName) {
    	FileInputStream ip = null;
    	 //Instantiating a list of Excel Data
    	List<ExcelData> listMasterExcelData=new ArrayList<ExcelData>();
		try {				
			//Creating input stream
			ip = new FileInputStream(new File(i_sFilePath));
			//instantiating excel workbook
			XSSFWorkbook objExcelWorkbook = new XSSFWorkbook(ip);
			XSSFSheet objWorkingSheet = objExcelWorkbook.getSheet(sSheetName);
			
		    //Getting size and dimension of range of data
			long rowCnt = 0;//row counter
		    rowCnt = objWorkingSheet.getPhysicalNumberOfRows();
		    int iFirstRow =objWorkingSheet.getFirstRowNum();
		    			   
		    //Iterating through each row
		    for (int iRowCnt=iFirstRow+2; iRowCnt <= rowCnt; iRowCnt++) {
		    	
		    	//instantiating new excelData class
		    	ExcelData clsExcelData = new ExcelData();
		    	Row objCurrentRow = objWorkingSheet.getRow(iRowCnt);
		    	
		    	//Reading the required columns from the row
		    	String sClassification=GetCellValue(objCurrentRow,18);
		    	if (sClassification.toLowerCase().equals("copper connection")){
		    		String sDeleted=GetCellValue(objCurrentRow,17);//Deleted	
			    	if(sDeleted.equals("DELETED")==false){
			    		clsExcelData.sBOMCode = GetCellValue(objCurrentRow,0);//BOM Code				
				 		//clsExcelData.sDeleted = GetCellValue(objCurrentRow,17);//Deleted			      		 					   
				 		clsExcelData.sSAPITComp = RemoveDotAndZero(GetCellValue(objCurrentRow,5));//SAP IT Component			    
				 		clsExcelData.sQuantity = RemoveDotAndZero(GetCellValue(objCurrentRow,9));//Quantity	
				 		clsExcelData.sMaterial = GetCellValue(objCurrentRow,19);//Material
				 		clsExcelData.sPDFLibLink = GetCellValue(objCurrentRow,21);//Link
				 						
				 		//Assigning to the class object
				    	listMasterExcelData.add(clsExcelData);
			    	}
		    		
		    	}
		 		
		    		    			          	
		    }
		    //Closing workbook
			objExcelWorkbook.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listMasterExcelData;
		 
    }   
	
	public static List<ExcelData> ReadExcelValuesFromSyndicationExcel(String i_sFilePath) {
    	FileInputStream ip = null;
    	 //Instantiating a list of Excel Data
    	List<ExcelData> listMasterExcelData=new ArrayList<ExcelData>();
		try {				
			//Creating input stream
			ip = new FileInputStream(new File(i_sFilePath));
			//instantiating excel workbook
			XSSFWorkbook objExcelWorkbook = new XSSFWorkbook(ip);
			XSSFSheet objWorkingSheet = objExcelWorkbook.getSheet("ItemData");
			
		    //Getting size and dimension of range of data
			long rowCnt = 0;//row counter
		    rowCnt = objWorkingSheet.getPhysicalNumberOfRows();
		    int iFirstRow =objWorkingSheet.getFirstRowNum();
		    			   
		    //Iterating through each row
		    for (int iRowCnt=iFirstRow+2; iRowCnt <= rowCnt; iRowCnt++) {
		    	
		    	//instantiating new excelData class
		    	ExcelData clsExcelData = new ExcelData();
		    	Row objCurrentRow = objWorkingSheet.getRow(iRowCnt);
		    	
		    	//Reading the required columns from the row
		    			    			    					    			    	
		    			clsExcelData.sBOMCode =GetCellValue(objCurrentRow,6);//BOM Code		      		 					   
				 		clsExcelData.sInsmanText = GetCellValue(objCurrentRow,12);//Instruction Manual Number
				 		clsExcelData.sLength = GetCellValue(objCurrentRow,24);//Length
				 		clsExcelData.sW = GetCellValue(objCurrentRow,42);//Width
				 		clsExcelData.sD = GetCellValue(objCurrentRow,30);//Depth
				 		
				 		//Assigning to the class object
				    	listMasterExcelData.add(clsExcelData);
			    		    				    		 				    		    			          	
		    }
		    //Closing workbook
			objExcelWorkbook.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listMasterExcelData;
		 
    }   

	public static  List<ExcelData> ReadExcelValuesFromABBLibraryLinks(String i_sFilePath) {
    	FileInputStream ip = null;
    	 //Instantiating a list of Excel Data
    	List<ExcelData> listMasterExcelData=new ArrayList<ExcelData>();
		try {				
			//Creating input stream
			ip = new FileInputStream(new File(i_sFilePath));
			//instantiating excel workbook
			XSSFWorkbook objExcelWorkbook = new XSSFWorkbook(ip);
			XSSFSheet objWorkingSheet = objExcelWorkbook.getSheet("Documents");
			
		    //Getting size and dimension of range of data
			long rowCnt = 0;//row counter
		    rowCnt = objWorkingSheet.getPhysicalNumberOfRows();
		    int iFirstRow =objWorkingSheet.getFirstRowNum();
		    			   
		    //Iterating through each row
		    for (int iRowCnt=iFirstRow+2; iRowCnt <= rowCnt; iRowCnt++) {
		    	
		    	//instantiating new excelData class
		    	ExcelData clsExcelData = new ExcelData();
		    	Row objCurrentRow = objWorkingSheet.getRow(iRowCnt);
		    	
		    	//Reading the required columns from the row
		    			    			    					    			    	  				      		 					   
				 		clsExcelData.sInsmanText = GetCellValue(objCurrentRow,9);//Instruction Manual Number
				 		clsExcelData.sInsmanLink = GetCellValue(objCurrentRow,31);//Instruction Manual Link
				 						 		
				 		//Assigning to the class object
				    	listMasterExcelData.add(clsExcelData);
			    		    				    		 				    		    			          	
		    }
		    //Closing workbook
			objExcelWorkbook.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listMasterExcelData;
		 
    }   
	
	
	
	 //**********************************************************************************End of Function***********************************************************************************************
	//**********************************************************************Function to form the final data structure for exporting to PDF************************************************************
	private void FormDataStructureForExportStandard(HashMap<Integer, List<String>> SortedMap,List<ExcelData> listMasterExcelData){
		//Creating a dictionary of column number as key and another dictionary of code and excel data as value
		
		
		dicMasterDataColumnStandard = new HashMap<>();
		 listMasterExcelDataStandard=new ArrayList<>();
		//Reading the second excel
		 if(com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterStandardFileData==null) {
			 listMasterStandardFileData=ReadStandardExcelFile2(objProperties.sInputPath + objProperties.sStandardFileNameIn);
			 com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterStandardFileData=listMasterStandardFileData;
		 }else listMasterStandardFileData=com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterStandardFileData;
		
	
		if(SortedMap != null && listMasterExcelData!=null ){//Checking if any of the passed data structure is null
			for (Map.Entry<Integer, List<String>> entry : SortedMap.entrySet()) {//Iterating through each item in Sorted Map
			    int colNumber=entry.getKey();//Column Number
			    List<ExcelData> listMasterListExcelDataColumn=new ArrayList<>();
			    
			    List<String> listCurrent =entry.getValue();//List of copper codes with quantity zero
			    HashMap<String, List<ExcelData>> MapCodes = new HashMap<>();
			    for(int iCount=0 ; iCount<listCurrent.size();iCount++ ){//Iterating through each code in list of each column
			    	String sCurrentCode = listCurrent.get(iCount);//Getting current code
			    	List<ExcelData> listExcelData=FetchDetailsOfCode(listMasterExcelData,sCurrentCode,"Standard");//Getting the rows corresponding to the code
			    	if(listExcelData!=null){//Checking if the fetched list is null
			    		for(int jCount=0;jCount< listExcelData.size();jCount++){
			    			ExcelData objCurrentExcelData=listExcelData.get(jCount);
			    			objCurrentExcelData=UpdateExcelDataStandardParts(listMasterStandardFileData,objCurrentExcelData);
			    		}
			    		
			    		if(!listExcelData.isEmpty()){
			    			MapCodes.put(sCurrentCode, listExcelData);	
			    			listMasterListExcelDataColumn.addAll(listExcelData);
			    			listMasterExcelDataStandard.addAll(listExcelData);
			    		}			    				    		
			    	}
			    }
			    
			    dicMasterDataColumnStandard.put(colNumber, listMasterListExcelDataColumn);
			}
		}
		
	}
	//**************************************************************************************************End of Function******************************************************************************
	
	private ExcelData UpdateExcelDataStandardParts(List<ExcelData> ListExcelData,ExcelData objExcelData){
		if(ListExcelData!=null){
			String sCode=objExcelData.sSAPITComp;
			for(int iCount=0;iCount< ListExcelData.size();iCount++){
				ExcelData objCurrentExcelData=ListExcelData.get(iCount);
				if(sCode.equals(objCurrentExcelData.sBOMCode)){
					objExcelData.sDesc=objCurrentExcelData.sDesc;
					objExcelData.sBech=objCurrentExcelData.sBech;
					objExcelData.sM=objCurrentExcelData.sM;
					objExcelData.sL=objCurrentExcelData.sL;
					objExcelData.sDIN=objCurrentExcelData.sDIN;
					objExcelData.sISO=objCurrentExcelData.sISO;
					objExcelData.sUNI=objCurrentExcelData.sUNI;
					return objExcelData;
				}
			}
		}
		return null;
	}
	
	private ExcelData UpdateExcelDataFromSyndication(List<ExcelData> ListExcelData,ExcelData objExcelData){
		if(ListExcelData!=null){
			String sCode=objExcelData.sBOMCode;
			for(int iCount=0;iCount< ListExcelData.size();iCount++){
				ExcelData objCurrentExcelData=ListExcelData.get(iCount);
				if(sCode.equals(objCurrentExcelData.sBOMCode)){
					objExcelData.sInsmanText=objCurrentExcelData.sInsmanText;
					objExcelData.sInsmanLink=GetABBLibraryLinks(listMasterExcelABBLibraryLinks,objCurrentExcelData.sInsmanText);
					if(isNumeric(objCurrentExcelData.sLength)) {
						objExcelData.sLength=objCurrentExcelData.sLength;	
					}else objExcelData.sLength=null;
					if(isNumeric(objCurrentExcelData.sW)) {
						objExcelData.sW=objCurrentExcelData.sW;	
					}else objExcelData.sW=null;
					if(isNumeric(objCurrentExcelData.sD)) {
						objExcelData.sD=objCurrentExcelData.sD;	
					}else objExcelData.sD=null;
									
					//objExcelData.sW=objCurrentExcelData.sW;
					//objExcelData.sD=objCurrentExcelData.sD;	
					objExcelData.sSection=objExcelData.sW + " x " + objExcelData.sD;
					return objExcelData;
				}
			}
		}
		return null;
	}
	private String GetABBLibraryLinks(List<ExcelData> ListExcelData,String sInsManNumber){
		String sLink="";
		if(ListExcelData!=null){
			String sCode=sInsManNumber;
			
			for(int iCount=0;iCount< ListExcelData.size();iCount++){
				ExcelData objCurrentExcelData=ListExcelData.get(iCount);
				if(sCode.equals(objCurrentExcelData.sInsmanText)){
					sLink=objCurrentExcelData.sInsmanLink;					
				}
			}
		}
		return sLink;
	}
	//===================================================================================STANDARD PARTS END===========================================================================================
	
	public static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
	
	
	
	//*****************************************************Function to Search for a code and fetch corresponding details*******************************************************************************
	private List<ExcelData> FetchDetailsOfCode(List<ExcelData> listMasterExcelData,String sSearchCode,String sValue){
		List<ExcelData> listFiltered = new ArrayList<>();
		int iNumber=0;
		String sCode=sSearchCode;
		String sQty="";
		if(sSearchCode.contains("_")){
			sCode=sSearchCode.split("_")[0];
			sQty=sSearchCode.split("_")[1];
		}
		if (listMasterExcelData!=null){
			int iSize=listMasterExcelData.size();
			for (int iCount=0;iCount <iSize;iCount++){
				try{
					ExcelData objCurrentData=listMasterExcelData.get(iCount);
					if (objCurrentData!=null){
						if ((objCurrentData.sBOMCode.equalsIgnoreCase(sCode)) && (objCurrentData.sMaterial.equals(sValue)))
				           {
				             String sQtyTotal = sQty;
				             if (objCurrentData.sMaterial.equalsIgnoreCase("standard"))
				             {
				               String sQuantityExcel = objCurrentData.sQuantity;
				               if (sQuantityExcel.contains(".")) {
				                 sQuantityExcel = RemoveDotAndZero(sQuantityExcel);
				               }
				               int iQty = Integer.parseInt(sQuantityExcel);
				               int iQuantityBOM = Integer.parseInt(sQty);
				               int iQtyTotal = iQty * iQuantityBOM;
				               sQtyTotal = String.valueOf(iQtyTotal);
				             }
				             objCurrentData.sQuantity = sQtyTotal;
				             listFiltered.add(objCurrentData);
				           }
				           iNumber++;
					}
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return listFiltered;
	}
	
	
	//******************************************************************************************End of Function****************************************************************************************
	
	//**************************Identifying the copper parts with quantity zero and arrange it according to column number*************************************************
	private  HashMap<Integer, List<String>> IdentifyCopperCodesWithQuantityZero( List<MyDataNode> listNodesForCopper){
		 HashMap<Integer, List<String>> SortedMap = new HashMap<>(); //Creating Hashmap to store column number and list of codes
		if (listNodesForCopper!= null){//Checking if the passed list is null
			for(int iCount=0;iCount<listNodesForCopper.size();iCount++){//Iterating through each item in list
				MyDataNode objCurrentNode = listNodesForCopper.get(iCount);//Getting the current node
				int colNumber=objCurrentNode.getColumnNumber();
				String sCode=objCurrentNode.getArticleNumber();
				int iQty=(int)Math.round(objCurrentNode.getQuantity());
				if (objProperties.sMountingType.startsWith("mounted")){
					if (sCode.endsWith("A")){
						sCode = sCode.substring(0, sCode.length() - 1);
					}
				}
				List<String> listCode;
				//Checking if it comes under copper parts
				if (objCurrentNode.getColumnTitle().toLowerCase().equals("copper parts")){
					//Checking if the quantity=0	
					sCode=sCode + "_" + String.valueOf(iQty);
						if (SortedMap.containsKey(colNumber)){//Checking if the Hashmap already contains column number as key
							listCode=SortedMap.get(colNumber);//Getting the list of codes by passing the key
							listCode.add(sCode);//Updating the list with Product code
							
						}else{
							listCode=new ArrayList<String>();
							listCode.add(sCode);
							SortedMap.put(colNumber, listCode);
						
						}		
				}
			}
		}
		return SortedMap;
	}
	//*************************************************************End of Function****************************************************************************************
	
	
	
	 //*************************************Function to read value from a cell irrespective of value type***************************************************************************
	private static String GetCellValue(Row i_row,int iCol){
		 String sValue="";
		 try{
			 
		 //Checking if the row is null
		 if (i_row!= null){
			 //Identifying the cell corresponding to row and column
			 Cell cell = i_row.getCell(iCol);			 
			 if (cell!= null){
				 //Checking if the value is string
				 if(cell.getCellTypeEnum() == CellType.STRING){
		    		  	sValue=cell.getStringCellValue();
		    	//Checking if the value is numeric	  	
		    	}else if (cell.getCellTypeEnum() == CellType.NUMERIC){
		    			sValue = Double.toString(cell.getNumericCellValue());
		    	}else if (cell.getCellTypeEnum() == CellType.FORMULA){
		    		CellType sItem= cell.getCachedFormulaResultTypeEnum();
		    		if(sItem.name().equals("NUMERIC")){
		    			sValue = Double.toString(cell.getNumericCellValue());
		    		}else{
		    			sValue=cell.getStringCellValue();
		    		}
		    	}
			 }
		 }
	} catch (Throwable  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 return sValue.trim();
		 }
	//******************************************************************End Of Function************************************************************************************************	 
	
	
	//====================================================================RBBS Copper Parts============================================================================================
	//***************************************************Exporting Copper Parts*******************************************************************************
		private void ExportCopperPartsRBBS( HashMap<Integer, List<String>> SortedMap) throws SecurityException, IOException{
		
			if (SortedMap!= null){
				
				if(com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataRBBS==null) {
					String sFilePath=objProperties.sInputPath + objProperties.sRBBSCopperFileNameIn;
					listMasterExcelDataRBBS=ReadExcelValuesForCopperPartsRBBS(sFilePath);
					com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataRBBS=listMasterExcelDataRBBS;
				}else listMasterExcelDataRBBS=com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataRBBS;
				
				
				FormDataStructureForExportCopper(SortedMap,listMasterExcelDataRBBS);	
				
			}		
		}
		
		private void ExportCopperPartsN185( HashMap<Integer, List<String>> SortedMap) throws SecurityException, IOException{
			
			if (SortedMap!= null){
				//List<ExcelData> listMasterExcelData;
				String sFilePath=objProperties.sInputPath + objProperties.sN185CopperFileNameIn;
					if(objProperties.sMountingType.startsWith("flatpack")){
						if(com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataN185Flatpack==null) {
							listMasterExcelDataN185=ReadExcelValuesCopperN185Mounted(sFilePath, true);
							com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataN185Flatpack=listMasterExcelDataN185;
						}else listMasterExcelDataN185=com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataN185Flatpack;
						
					}	else {
						if(com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataN185Mounted==null) {
							listMasterExcelDataN185=ReadExcelValuesCopperN185Mounted(sFilePath, false);
							com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataN185Mounted=listMasterExcelDataN185;
						}else listMasterExcelDataN185=com.virtubuild.services.clientgui.custom.TreeTableMain.listMasterExcelDataN185Mounted;
						
					}
					FormDataStructureForExportCopper(SortedMap,listMasterExcelDataN185);							
			}		
		}
		
		
		
		//*************************************************************End of Function******************************************************************************
	
		//********************************Function to form the final data structure for exporting to PDF************************************************************
		private void FormDataStructureForExportCopper(HashMap<Integer, List<String>> SortedMap,List<ExcelData> listMasterExcelData){
			//Creating a dictionary of column number as key and another dictionary of code and excel data as value
			
			dicMasterDataColumnCopper = new HashMap<>();
			 listMasterExcelDataCopper=new ArrayList<>();
		
			if(SortedMap != null && listMasterExcelData!=null ){//Checking if any of the passed data structure is null
				for (Map.Entry<Integer, List<String>> entry : SortedMap.entrySet()) {//Iterating through each item in Sorted Map
				    int colNumber=entry.getKey();//Column Number
				    List<ExcelData> listMasterListExcelDataColumn=new ArrayList<>();
				    
				    List<String> listCurrent =entry.getValue();//List of copper codes with quantity zero
				    HashMap<String, List<ExcelData>> MapCodes = new HashMap<>();
				    for(int iCount=0 ; iCount<listCurrent.size();iCount++ ){//Iterating through each code in list of each column
				    	String sCurrentCode = listCurrent.get(iCount);//Getting current code
				    	List<ExcelData> listExcelData=FetchDetailsOfCode(listMasterExcelData,sCurrentCode,"Copper");//Getting the rows corresponding to the code
				    	if(listExcelData!=null){//Checking if the fetched list is null
				    		for(int jCount=0;jCount< listExcelData.size();jCount++){
				    			ExcelData objCurrentExcelData=listExcelData.get(jCount);
				    			objCurrentExcelData=UpdateExcelDataFromSyndication(listMasterExcelSyndication,objCurrentExcelData);
				    		}
				    		
				    		if(listExcelData.size()!=0){
				    			MapCodes.put(sCurrentCode, listExcelData);
				    			listMasterListExcelDataColumn.addAll(listExcelData);
				    			listMasterExcelDataCopper.addAll(listExcelData);
				    		}			    		
				    	}
				    }
				   
				    dicMasterDataColumnCopper.put(colNumber, listMasterListExcelDataColumn);
				}
			}
			
		}
		//*************************************************************End of Function******************************************************************************
		
		//*************************************************************Read All the data from the excel for Copper parts***********************************************************************
		public static List<ExcelData> ReadExcelValuesForCopperPartsRBBS(String i_sFilePath) {
		    	FileInputStream ip = null;
		    	 //Instantiating a list of Excel Data
		    	List<ExcelData> listMasterExcelData=new ArrayList<ExcelData>();;
				try {				
					//Creating input stream
					ip = new FileInputStream(new File(i_sFilePath));
					//instantiating excel workbook
					XSSFWorkbook objExcelWorkbook = new XSSFWorkbook(ip);
					XSSFSheet objWorkingSheet = objExcelWorkbook.getSheet("BOM_CONNECTIONS");
					
				    //Getting size and dimension of range of data
					long rowCnt = 0;//row counter
				    rowCnt = objWorkingSheet.getPhysicalNumberOfRows();
				    int iFirstRow =objWorkingSheet.getFirstRowNum();
				    			   
				    //Iterating through each row
				    for (int iRowCnt=iFirstRow+2; iRowCnt <= rowCnt; iRowCnt++) {
				    	
				    	//instantiating new excelData class
				    	ExcelData clsExcelData = new ExcelData();
				    	Row objCurrentRow = objWorkingSheet.getRow(iRowCnt);
				    	String sDeleted=GetCellValue(objCurrentRow,17);//Deleted
				    	String sMaterial=GetCellValue(objCurrentRow,19);
				    	if(sDeleted.equals("DELETED")==false && (sMaterial.equalsIgnoreCase("Standard") ||sMaterial.equalsIgnoreCase("Copper"))){
				    		//Reading the required columns from the row
		    		 		clsExcelData.sBOMCode = GetCellValue(objCurrentRow,0);//BOM Code				
		    		 		//clsExcelData.sDeleted = GetCellValue(objCurrentRow,17);//Deleted			   
		    		 		clsExcelData.sMaterial = sMaterial;//GetCellValue(objCurrentRow,19);//Material			   
		    		 		clsExcelData.sSAPITComp = GetCellValue(objCurrentRow,6);//SAP IT Component			    
		    		 			
		    		 		clsExcelData.sLibraryText = GetCellValue(objCurrentRow,20);
		    		 		clsExcelData.sDXFLibLink = GetCellValue(objCurrentRow,21);//ABB Library Document Link in DXF			   
		    		 		clsExcelData.sPDFLibLink = GetCellValue(objCurrentRow,23);//ABB Library Document Link in PDF
		    		 		clsExcelData.sSection=clsExcelData.sW + " x " + clsExcelData.sD;
		    		 		clsExcelData.sQuantity = GetCellValue(objCurrentRow, 8);
		    		 		
		    		 		// Fetching instruction manual data using BOM 8 digit code
		    		 		
		    		 		String oneHyperlink = null;
		    		 		
		    		 		SyndicateBean syndicateBean =  com.virtubuild.services.clientgui.custom.TreeTableMain.dataSyndicate.get(clsExcelData.sBOMCode);
		    		 		
		    		 		String instructionManualID = syndicateBean.getInstructionManualID();
		    		 		
		    		 		List<String> hyperLinks  = (List<String>) com.virtubuild.services.clientgui.custom.TreeTableMain.dataABBLinks.get(instructionManualID);
		    		 		
		    		 		if(null != hyperLinks && !hyperLinks.isEmpty()){
								 
								 for (String hyperlink : hyperLinks){
									 oneHyperlink = hyperlink;
								 }
		    		 		
		    		 		}
		    		 		
		    		 		clsExcelData.sInsmanText = instructionManualID;
		    		 		clsExcelData.sInsmanLink = oneHyperlink;
		    		 		
		    		 		//Assigning to the class object
					    	listMasterExcelData.add(clsExcelData);
				    	}
				    	
				    					    		    			          	
				    }
				    //Closing workbook
					objExcelWorkbook.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return listMasterExcelData;
				 
		    }   
		 //***************************************************************End of Function***********************************************************************************************
		
	
		public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    BigDecimal bd = new BigDecimal(value);
		    bd = bd.setScale(places, RoundingMode.HALF_UP);
		    return bd.doubleValue();
		}
		
		@SuppressWarnings("deprecation")
		private static String RemoveDotAndZero(String sText){
		//return sText;
		double number;
		try{
			if(NumberUtils.isNumber(sText)) {
				if(nf == null) {
					DecimalFormat decFormat = new DecimalFormat(); 
					DecimalFormatSymbols decSymbols = decFormat.getDecimalFormatSymbols();
					Character c=new Character(decSymbols.getDecimalSeparator());
					Character c1=new Character('.');
					if(c.equals(c1)) {
						nf=NumberFormat.getInstance(Locale.US);
					}else nf=NumberFormat.getInstance(Locale.GERMAN);
				}
				nf.setMaximumFractionDigits(0);
				number=Double.parseDouble(sText);
				sText=nf.format(number);
			}
			return sText;
		}catch (Exception e) {
			e.printStackTrace();
			return sText;
		}
		
		
	}	
		
		
		
		
		
	//==================================================================End Of RBBS Copper Parts========================================================================================
}
