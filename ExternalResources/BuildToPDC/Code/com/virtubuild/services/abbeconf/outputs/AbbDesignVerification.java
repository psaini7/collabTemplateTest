/******************************************************************
 * AbbDesignVerification				                    	  *
 * Represents a Exporter class for filling the  				  *
 * excel sheet that is used for design verification.			  *
 ******************************************************************/
/**@author Aagreet Sinha*/
package com.virtubuild.services.abbeconf.outputs;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.Exporter;

public class AbbDesignVerification extends Exporter {
	final static Class<?> referenceClass = AbbDesignVerification.class;
	static String osDir = System.getenv("SystemDrive") + "\\econftemp\\";
	String designVerification = "";
	private static final Logger LOGGER = LoggerFactory.getLogger(AbbDesignVerification.class);
	
	public static String designVerificationPath = null;
	
	
	private void setPath(){
		designVerificationPath = getManager().getSystem().getURL("custom/DesignVerification").toString().replaceAll("file:/", "");
		designVerificationPath = designVerificationPath.replaceAll("%20", " ");
		
	}
	
	public void prepareData() {

		setPath();
		
		designVerification = getManager().getExporterController().runExporter("custom_output_DesignVerification");

		try {

			final File file = new File(osDir + "\\DesignVerification\\DesignVerification.xlsx");
			if (file.exists() && !file.renameTo(file)) {
				infoBox("Please close the running excel file and run again.", "Warning : File already open");
			} else {
				final URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();
				
				File jarPath = new File(url.toURI()).getParentFile();

				//File sourceDirectory = new File(jarPath + "\\DesignVerification\\");
				File sourceDirectory = new File(designVerificationPath);
				File targetDirectory = new File(osDir + "\\DesignVerification\\");

				directoryCopy(sourceDirectory, targetDirectory);
				
				ReadXml readXml = new ReadXml();
				readXml.prepareData(designVerification);
				infoBox("Design Verification Excel Generated Successfully", "Success Message");

				Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + osDir
						+ "\\DesignVerification\\DesignVerification.xlsx");
				p.waitFor();
			}

		} catch (URISyntaxException | InterruptedException | IOException e) {
			LOGGER.error(e.getMessage());
		}

	}

	@Override
	public void prepareData(Collection<String> arg0) {
		
	}

	private static void directoryCopy(File source, File dest) {
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public String getDataString() {
		try {
			prepareData();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

}
