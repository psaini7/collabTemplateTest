package com.virtubuild.services.abbeconf.reportgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.Exporter;
import com.virtubuild.core.WorldComponent;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.StateValue;

public class AbbOfflineReportGenerator extends Exporter {
	static final Class<?> referenceClass = AbbOfflineReportGenerator.class;
	private static final Logger LOGGER = LoggerFactory.getLogger(AbbOfflineReportGenerator.class);
	JarExecutor jr = new JarExecutor();
	static String osDir = System.getenv("SystemDrive") + "\\econftemp\\";
	private static final String SWITCHBOARD_TWINLINE = "switchboard_twinline";
	private static final String SWITCHBOARD_RBBS = "switchboard_rbbs";
	private static final String SWITCHBOARD_N185 = "switchboard_n185";
	private Configuration configuration;
	private String outputDir;

	public void prepareData() {
		this.outputDir = selectFileAndValidate();
		if (outputDir == "") {
			LOGGER.info("You choose cancle.");
		} else {
			String exporterXml = "";
			this.configuration = getConfiguration();
			try {
				if (this.configuration.getComponentFromToa(SWITCHBOARD_TWINLINE, 0) != null) {
					exporterXml = getManager().getExporterController().runExporter("eConfigure_twinline_tender_text");
					stringToDom(exporterXml);
					this.jr.executeJar(this.configuration, this.outputDir);
				} else if (this.configuration.getComponentFromToa(SWITCHBOARD_RBBS, 0) != null) {
					exporterXml = getManager().getExporterController().runExporter("eConfigure_rbbs_tender_text");
					stringToDom(exporterXml);
					this.jr.executeJar(this.configuration, this.outputDir);
				} else if (this.configuration.getComponentFromToa(SWITCHBOARD_N185, 0) != null) {
					exporterXml = getManager().getExporterController().runExporter("eConfigure_n185_tender_text");
					stringToDom(exporterXml);
					this.jr.executeJar(this.configuration, this.outputDir);
				} else {
					exporterXml = getManager().getExporterController().runExporter("exporter_tender");
					stringToDom(exporterXml);
					this.jr.executeJar(this.configuration, this.outputDir);
				}
			} catch (Exception e) {
				LOGGER.error("Exception caught " + e.getMessage());
			}
		}
	}

	public void prepareData(Collection<String> arg0) {
	}

	public static void stringToDom(String exporterXml) throws IOException, URISyntaxException {
		URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();

		File jarPath = new File(url.toURI()).getParentFile();

		File sourceDirectory = new File(jarPath + "\\ReportGenerator\\");
		File targetDirectory = new File(osDir + "\\ReportGenerator\\");

		directoryCopy(sourceDirectory, targetDirectory);

		FileWriter fw = new FileWriter(osDir + "\\ReportGenerator\\input\\input.xml");
		fw.write(exporterXml);
		fw.close();
	}

	/*private String getUserPath() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a directory to save your file: ");
		jfc.setFileSelectionMode(1);
		int returnValue = jfc.showSaveDialog(null);
		String str = null;
		if ((returnValue == 0) && (jfc.getSelectedFile().isDirectory())) {
			str = jfc.getSelectedFile().getAbsolutePath();
		} else if (returnValue == 1) {
			str = "";
		}
		return str;
	}*/
	
	private String selectFileAndValidate() {
		String sPath="";
		JFrame saveDialogBox = new JFrame();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");  
		this.configuration = getConfiguration();
		if (this.configuration.getComponentFromToa(SWITCHBOARD_TWINLINE, 0) != null) {
			fileChooser.setSelectedFile(new File("eConfigure_twinline_tender_text"));
		} 
		else if (this.configuration.getComponentFromToa(SWITCHBOARD_RBBS, 0) != null) {
			fileChooser.setSelectedFile(new File("eConfigure_rbbs_tender_text"));
		} 
		else if (this.configuration.getComponentFromToa(SWITCHBOARD_N185, 0) != null) {
			fileChooser.setSelectedFile(new File("eConfigure_n185_tender_text"));
		} else {
			fileChooser.setSelectedFile(new File("eConfigure_tender_text"));
		}

		int userSelection = fileChooser.showSaveDialog(saveDialogBox);

		File fileToSave = fileChooser.getSelectedFile();
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			WorldComponent world = configuration.getWorldComponent();
			StateValue doc_format = world.getStateVector().getVar("doc_format").getValue();
			if (doc_format.toString().equalsIgnoreCase("pdf (.pdf)")) {
				if( new File(fileToSave.getPath() +".pdf").exists()) {
					JOptionPane.showMessageDialog( new JFrame(), "File already exists!",  "ABB e-Configure", JOptionPane.INFORMATION_MESSAGE);
					String sAnotherPath=selectFileAndValidate();
					if(sAnotherPath!="") {
						sPath=sAnotherPath;
					}
				}else sPath=fileToSave.getPath();
			}
			else if (doc_format.toString().equalsIgnoreCase("docx (.docx)")) {
				if( new File(fileToSave.getPath() +".docx").exists()) {
					JOptionPane.showMessageDialog( new JFrame(), "File already exists!",  "ABB e-Configure", JOptionPane.INFORMATION_MESSAGE);
					String sAnotherPath=selectFileAndValidate();
					if(sAnotherPath!="") {
						sPath=sAnotherPath;
					}
				}else sPath=fileToSave.getPath();
			}
		}
		return sPath;	 
	}


	private static void directoryCopy(File source, File dest) {
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public String getDataString() {
		try {
			prepareData();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}
}
