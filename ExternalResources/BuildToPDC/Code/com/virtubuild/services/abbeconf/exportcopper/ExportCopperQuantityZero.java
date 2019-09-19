package com.virtubuild.services.abbeconf.exportcopper;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.WorldComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.services.clientgui.custom.TreeTableMain;


public class ExportCopperQuantityZero extends CompleterSkeleton {
	private ConfigProperties objProperties;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportCopperQuantityZero.class);
	
	public boolean doComplete() {
		//XMLOperations clsXMLOps=new XMLOperations();
		//clsXMLOps.ReadXML("D:\\Test\\XML\\input.xml");
		//TestOfflineExec();
		//Getting properties of the configuration
		if(InitializeConfiguration()==true){
			if(objProperties.bCopperRequired==false){
				
				CopperCheck clsCopperCheck= new CopperCheck(objProperties);

				try {
					clsCopperCheck.DoExportOperations(objProperties);					
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				WriteToText("Copper with cu");
			}
		}
		//System.exit(0);	
        return true;
    }
	private String selectFileAndValidate() {
		String sPath="";
		JFrame saveDialogBox = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save PDF file");
		fileChooser.setSelectedFile(new File("eConfigure_Copper_Export"));

		int userSelection = fileChooser.showSaveDialog(saveDialogBox);

		File fileToSave = fileChooser.getSelectedFile();
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			
			if(new File(fileToSave.getPath() +".pdf").exists()) {
				JOptionPane.showMessageDialog( new JFrame(), "File already exists!",  "ABB e-Configure", JOptionPane.INFORMATION_MESSAGE);
				String sAnotherPath=selectFileAndValidate();
				if(sAnotherPath!="") {
					sPath=sAnotherPath;
				}
			}else sPath=fileToSave.getPath();
		}
		return sPath;
	}
	
	private void TestOfflineExec() {
		String osDir = "C:\\econftemp\\";
		Runtime re = Runtime.getRuntime();
		try {
			Process command = re.exec(
					"cmd /c start /wait javaw -jar " + osDir + "\\ReportGenerator\\ReportGenerator.jar -i "
							+ osDir + "\\ReportGenerator\\input\\output.xml -t " + osDir
							+ "\\ReportGenerator\\document -o " + osDir + "\\ReportGenerator\\output");
			
			command.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void WriteToText(String sPath){
		/* BufferedWriter writer = null;
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
	
	private boolean InitializeConfiguration(){
		String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		//Initializing ConfigProperties class
		 objProperties=new ConfigProperties();
		
		//Getting details of current configuration
		Configuration objConfiguration=getConfiguration();
		
		WorldComponent objWorldComponent=objConfiguration.getWorldComponent();
		String sProductLine=objWorldComponent.getStateVector().getVar("system").getValue().toString();
		Component linkOwner = (Component) objConfiguration.getComponentFromToa("switchboard", 0);
		if(linkOwner==null){
			return false;
		}
		String sOrderType=linkOwner.getStateVector().getVar("order").getValue().toString();
		String sMountingType=linkOwner.getStateVector().getVar("delivery_mounting_type").getValue().toString();
		
		//Assigning values to ConfigProperties class object
		if(sOrderType.startsWith("with_cu")){
			objProperties.bCopperRequired=true;
			return false;
		}else{
			objProperties.bCopperRequired=false;
		}
		
		objProperties.sUsCustomerCountry=objWorldComponent.getStateVector().getVar("country_customer").getValue().toString();
		objProperties.sCustomerCity=objWorldComponent.getStateVector().getVar("city_customer").getValue().toString();
		objProperties.sCustomerZip=objWorldComponent.getStateVector().getVar("zip_code_customer").getValue().toString();
		objProperties.sCustomerAddress=objWorldComponent.getStateVector().getVar("address_customer").getValue().toString();
		objProperties.sCustomername=objWorldComponent.getStateVector().getVar("name_customer").getValue().toString();
		objProperties.sUserDept=objWorldComponent.getStateVector().getVar("department_user").getValue().toString();
		objProperties.sUsername=objWorldComponent.getStateVector().getVar("name_user").getValue().toString();
		objProperties.sDate=objWorldComponent.getStateVector().getVar("date").getValue().toString();
		objProperties.sDescription=objWorldComponent.getStateVector().getVar("description").getValue().toString();
		objProperties.sMountingType=sMountingType;
		objProperties.sNumber=objWorldComponent.getStateVector().getVar("number").getValue().toString();
		objProperties.sOutputPath=GetUserSelectedPath();
		if(objProperties.sOutputPath.equals("")){
			return false;
		}
		objProperties.sProductLine=sProductLine;
		if(sProductLine.startsWith("rbbs")){
			objProperties.sOutputFileName="Design Document RBBS_" +fileName;
			objProperties.sProductLineName="rbbs";
		}else if(sProductLine.startsWith("n185")){
			objProperties.sOutputFileName="Design Document SPEP N185_" +fileName;
			objProperties.sProductLineName="n185";
		}
		objProperties.sProjectName=objWorldComponent.getStateVector().getVar("name").getValue().toString();
		objProperties.sValidity=objWorldComponent.getStateVector().getVar("validity").getValue().toString();
		
		//objProperties.sInputPath=GetPathOfDesignDocument() + "\\";
		//objProperties.sCustomPath=GetPathOfCustomFolder() + "\\";
		
		//String buildResourceFolderPath = TreeTableMain.buildResourceFolderPath;
		
		objProperties.sInputPath= TreeTableMain.buildDesignDocumentPath + "/";
		objProperties.sCustomPath= TreeTableMain.buildCustomPath + "/";
		
		//LOGGER.error("Build res folder: " + objProperties.sCustomPath);
		System.out.println("Build res folder: " + objProperties.sCustomPath);
		
		//objProperties.sResourcePath= GetPathOfResourceFolder() + "\\";
		//objProperties.sCustomPath="C:\\Users\\aagreet.sinha\\BuildStudioWorkspace\\workspace\\12th-April-1041am\\Studio\\Resources\\custom\\";
		//objProperties.sInputPath="C:\\Users\\aagreet.sinha\\BuildStudioWorkspace\\workspace\\12th-April-1041am\\Studio\\Resources\\design document\\";
		objProperties.sRBBSCopperFileNameIn="BOM SPEP RBBS V9.xlsx";
		objProperties.sN185CopperFileNameIn="BOM_SPEP_185_Rev6.xlsx";
		objProperties.sStandardFileNameIn="Standardized components SPEP RBBS_Rev1.xlsx";
		
		WriteToText(objProperties.sInputPath + " *** " + objProperties.sCustomPath + "\n");
		
		return true;
	}
	
	private String GetPathOfCustomFolder(){
		String jarDir="";
		CodeSource codeSource = CopperCheck.class.getProtectionDomain().getCodeSource();
		File jarFile;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
			jarDir = jarFile.getParentFile().getPath();
			WriteToText(jarDir);
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		    int lastSlash = jarDir.lastIndexOf("\\");		   
		    String result = jarDir.substring(0, lastSlash)+ "\\custom";		   
		    return result;
	}
	
	private String GetPathOfDesignDocument(){
		String jarDir="";
		CodeSource codeSource = CopperCheck.class.getProtectionDomain().getCodeSource();
		File jarFile;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
			jarDir = jarFile.getParentFile().getPath();
			WriteToText(jarDir);
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		    int lastSlash = jarDir.lastIndexOf("\\");		   
		    String result = jarDir.substring(0, lastSlash)+ "\\design document";		   
		    return result;
	}
	private String GetPathOfResourceFolder(){
		String jarDir="";
		CodeSource codeSource = CopperCheck.class.getProtectionDomain().getCodeSource();
		File jarFile;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
			jarDir = jarFile.getParentFile().getPath();
			//WriteToText(jarDir);
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}	   
		 int lastSlash = jarDir.lastIndexOf("\\");		   
		    String result = jarDir.substring(0, lastSlash)+ "\\pdfconversion";	   
		    return result;
	}
	private String GetUserSelectedPath(){
		String sOutputPath=selectFileAndValidate();
		//Getting user selected output path
		/*JFrame saveDialogBox = new JFrame();
		JFileChooser fileChooser = new JFileChooser();
		
		
		fileChooser.setDialogTitle("Select target folder");
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
		int userSelection = fileChooser.showSaveDialog(saveDialogBox);
		if(userSelection!=1){
			File fileToSave = fileChooser.getSelectedFile();
			if (fileToSave.exists()==true){
				sOutputPath=fileToSave.getAbsolutePath() + "\\";
				return sOutputPath;
			}
		}*/
		
		return sOutputPath;
	}
    
    @Override
    public boolean doCompleteSupported() {
        return true;
    }

    @Override
    protected void init() {
        super.init();
    }
}

