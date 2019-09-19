package com.virtubuild.services.clientgui.sendconfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.WorldComponent;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.complete.CompleterSkeleton;

public class SendConfigurationMail extends CompleterSkeleton {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SendConfigurationMail.class); 
	
	private Configuration configuration;
	private WorldComponent world;
	
	public String getOutlookPath(){
		
		String outlookPath = null;
		
		Process p = null;
		try {
			p = Runtime.getRuntime()
			         .exec(new String[] { "cmd.exe", "/c", "assoc", ".pst" });
		} catch (IOException e1) {
			LOGGER.error("Error: " + e1.getMessage());
		}
         BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
         String extensionType = null;
		try {
			extensionType = input.readLine();
		} catch (IOException e2) {
			LOGGER.error("Error: " + e2.getMessage());
		}
         try {
			input.close();
		} catch (IOException e1) {
			LOGGER.error("Error: " + e1.getMessage());
		}
         if (extensionType == null) {
        	 LOGGER.info("File type PST not associated with Outlook.");
         } else {
             String fileType[] = extensionType.split("=");

             try {
				p = Runtime.getRuntime().exec(
				         new String[] { "cmd.exe", "/c", "ftype", fileType[1] });
			} catch (IOException e) {
				LOGGER.error("Error: " + e.getMessage());
			}
             input = new BufferedReader(new InputStreamReader(p.getInputStream()));
             String fileAssociation = null;
			try {
				fileAssociation = input.readLine();
			} catch (IOException e) {
				LOGGER.error("File type PST not associated with Outlook. " + e.getMessage());
			}
			
			 LOGGER.info("Extracting path");
			
             Pattern pattern = Pattern.compile("\".*?\"");
             Matcher m = pattern.matcher(fileAssociation);
             if (m.find()) {
                 outlookPath = m.group(0);
                 LOGGER.info("Outlook path: " + outlookPath);
             } else {
                 LOGGER.error("Error parsing PST file association");
             }
         }
         
         return outlookPath;
		
	}
	
	
	@Override
	public boolean doComplete() {
		
		 configuration = getConfiguration();
		 world = configuration.getWorldComponent(); // get the World component
		 
		 StateValue bodyContent = null;
		 StateValue subjectContent = null;
		 StateValue recipientsContent = world.getStateVector().getVar("send_config_recipients").getValue();
		 
		 StateValue userLanguage = world.getStateVector().getVar("send_config_lang").getValue();
		 
		 switch(userLanguage.toString()){
		 
		 	case "EN":
		 		 bodyContent = world.getStateVector().getVar("send_config_body_en").getValue();
				 subjectContent = world.getStateVector().getVar("send_config_subject_en").getValue();
				 break;
				
		 	case "DE":
		 		 bodyContent = world.getStateVector().getVar("send_config_body_de").getValue();
				 subjectContent = world.getStateVector().getVar("send_config_subject_de").getValue();
				 break;
				 
			default: 
				LOGGER.error("Error - Invalid language");
		 	
		 }
		 
		 
		 String outlookPath = null;
		 
		 outlookPath = getOutlookPath();
		 
		 if (outlookPath != null){
			 
			 try {
				 	new ProcessBuilder(outlookPath,"/a","C:\\econftemp\\temp.3dc", "/m", recipientsContent.toString()+"&subject="+subjectContent.toString()+"&body="+bodyContent.toString()).start();
		        } 
		        catch ( IOException  ex )
		        {
		        	LOGGER.error("Error launching outlook" + ex.getMessage());
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
