package com.virtubuild.services.clientgui.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.util.IOUtils;
import com.virtubuild.core.complete.CompleterSkeleton;

public class InstructionManual extends CompleterSkeleton {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InstructionManual.class);
    
    private static final int BUFFER_SIZE = 4096;
    static int attachmentNumber = 0;
    private File fileToSave;
    String filePath = null;
    
    HashSet <String> downloadURLs = new HashSet <String>(); 
    static List<String> listSaveFileName = new ArrayList<>();
    
    private static boolean saveSucess = false;
    
    public static void downloadFile(String saveFileName, String fileURL, String fileExtension, String saveDir) throws IOException {
        try {
            
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            
            String newLocation = httpConn.getHeaderField("Location");
            
            URL urlNew = new URL(newLocation);
            
            HttpURLConnection newHttpConn = (HttpURLConnection) urlNew.openConnection();
            
            int responseCode = newHttpConn.getResponseCode();
            // always check HTTP response code first
            
            
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                //String contentType = httpConn.getContentType();
                //int contentLength = httpConn.getContentLength();

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10, disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
                }

                // System.out.println("Content-Type = " + contentType);
                /// System.out.println("Content-Dispo sition = " + disposition);
                // System.out.println("Content-Length = " + contentLength);
                // System.out.println("fileName = " + fileName);

                String fileNameWithExtn[] = fileName.split("\\.");
                
                // opens input stream from the HTTP connection
                InputStream inputStream = newHttpConn.getInputStream();
                
                //String fileNameWithExtension = saveFileName + "." + fileNameWithExtn[1];
                String fileNameWithExtension = saveFileName + "." + fileExtension;
                
                String saveFilePath = saveDir + File.separator + fileNameWithExtension;

                listSaveFileName.add(fileNameWithExtension);
                
                File file = new File(saveFilePath);
                file.getParentFile().mkdirs(); // Will create parent directories if not exists
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file,false);
                
                IOUtils.copy(inputStream, outputStream);
                

                // opens an output stream to save into file
                //FileOutputStream outputStream = new FileOutputStream(saveFilePath);

               /* int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }*/
                
                saveSucess = true;
                
                outputStream.close();
                inputStream.close();
            }
            newHttpConn.disconnect();
        } catch (UnknownHostException un) {
        	saveSucess = false;
        	LOGGER.error("Error downloading. Network issue. " + un.getMessage());
        } catch (Exception e) {
        	saveSucess = false;
        	LOGGER.error("Error downloading. Network issue. " + e.getMessage());
        }
    }
    
    @Override
    public boolean doComplete() {
    	
    	// Get Unique URLs to download
    	
    	//test links
    	
    	//downloadURLs.add("RFGA15#http://sparshv2/portals/tpd/Documents/The%20Team_May%202017.pdf");
    	//downloadURLs.add("RFGA17#http://sparshv2/portals/tpd/Documents/The%20Team_May%202017.pdf");
    	
    	UrlResponse obj1=new UrlResponse();
    	
    	//String str = obj1.callURL("https://ws.library.e.abb.com/AbbLibraryBasicWebServices/AbbLibraryWebServicesLightweight.svc/GetDocumentsFromABBLibraryByIdentities/?fields=BasicSet,DocumentPartID&documentId=1STS100196R0001&documentPart=&documentLanguage=en&DOCUMENT_SECURITY_LEVEL=INTERNAL&returnExtendedMetadataSet=true");
    	
    	//String str = obj1.callURL("https://ws.library.e.abb.com/AbbLibraryBasicWebServices/AbbLibraryWebServicesLightweight.svc/GetDocumentsFromABBLibraryByIdentities/?fields=BasicSet,DocumentPartID&documentId=1STD000053&documentPart=&documentLanguage=en&DOCUMENT_SECURITY_LEVEL=INTERNAL&returnExtendedMetadataSet=true");
    	
    	
    	
    	try {
    		
			/*JSONObject json = new JSONObject(str);
			
			System.out.println("JSON response: " + json);
			
			JSONArray jsonarr = json.getJSONArray("DocumentContainer");
			String firstName = jsonarr.getJSONObject(0).getString("DocumentURL");
			String documentID = jsonarr.getJSONObject(0).getString("DocumentID");
			
			System.out.println("DocumentURL: " + firstName.replace("search-ext", "search"));
			
			String documentURL1=firstName.replace("search-ext", "search");
			
			downloadURLs.add(documentID + "#" + documentURL1);*/
			
			//downloadURLs.add("TestFile" + "#" + "http://search.abb.com/library/Download.aspx?DocumentID=1STD000053&LanguageCode=de&LanguageCode=en&LanguageCode=it&DocumentPartId=dxf&Action=Launch");
			
			
			//downloadURLs.add("RFGA15#" + documentURL1.replace("http:", "https:"));
			
		} catch (Exception e1) {
			LOGGER.error("Error accessing webservice: " + e1.getMessage());
		}
    	
    	
    	//downloadURLs.add("RFGA15#http://search.abb.com/library/Download.aspx?DocumentID=1STC803001D0201&LanguageCode=en&DocumentPartId=&Action=Launch");
    	//downloadURLs.add("RFGA16#http://search.abb.com/library/Download.aspx?DocumentID=1STC803001D0201&LanguageCode=en&DocumentPartId=&Action=Launch");
    	
    	// Storing the hyper links in the set with pattern TypeCode#Link
    	
    	for(Map.Entry<String, String> entry : TreeTableMain.mapTypeCodeURL.entrySet()){
    		
    		String[] links = entry.getValue().split(",");
    		
    		for(String link : links){
    			
    			if(link.contains("PartId=dxf")){
    				downloadURLs.add(entry.getKey()+ "#" +link+"#dxf");
    			}
    			else{
    				downloadURLs.add(entry.getKey()+ "#" +link+"#pdf");
    			}
    			
    		}
    		
    	}
    	
    	
    	for(Map.Entry<String, String> entry : TreeTableMain.mapTypeCodeDrawingURL.entrySet()){
    		
    		String[] links = entry.getValue().split(",");
    		
    		for(String link : links){
    			
    			if(link.contains("PartId=dxf")){
    				downloadURLs.add(entry.getKey()+ "#" +link+"#dxf");
    			}
    			else{
    				downloadURLs.add(entry.getKey()+ "#" +link+"#pdf");
    			}
    			
    			
    		}
    		
    	}
    	
    	
        //URL url; 
        //InputStream in;
         
        JFrame saveDialogBox = new JFrame();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save instruction manuals");
        fileChooser.setSelectedFile(new File("eConfigure_instruction_manuals"));

        int userSelection = fileChooser.showSaveDialog(saveDialogBox);

        fileToSave = fileChooser.getSelectedFile();
        
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {

            filePath = fileToSave.getAbsolutePath();
            
            Iterator<String> setIterator = downloadURLs.iterator();
            
            String filenameAndLink[];
            
            while(setIterator.hasNext()){
                
                try {
                    
                		
                	filenameAndLink = setIterator.next().split("#");
                	
            		downloadFile(filenameAndLink[0], filenameAndLink[1], filenameAndLink[2], filePath);
                		
                    
                } catch (MalformedURLException e) {
                	saveSucess = false;
                    LOGGER.error("MalformedURLException: " + e);
                    saveSucess = false;
                } catch (IOException e) {
                	LOGGER.error("IOException " + e);
                }
                catch(Exception e){
                	saveSucess = false;
                	LOGGER.error("Error: " + e);
                }
                
            }
            
            zipFiles();

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
        return true;
    }
    
    private void zipFiles(){
    	
    	if (saveSucess) {
    		
    		byte[] buffer = new byte[1024];
            
            try{
                
                FileOutputStream fos = new FileOutputStream(fileToSave.getParentFile().getAbsolutePath() + "\\" + fileToSave.getName() +".zip");
                ZipOutputStream zos = new ZipOutputStream(fos);
                
                for(String fileName : listSaveFileName){
                	
                	ZipEntry ze= new ZipEntry(fileName);
                    zos.putNextEntry(ze);
                    
                    FileInputStream in = new FileInputStream(filePath +"\\" + fileName);
               
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
        
                    in.close();
                	
                }
                
                zos.closeEntry();
               
                zos.close();
                
                FileUtils.deleteDirectory(fileToSave);
              

            }catch(IOException ex){
            	LOGGER.error("Error archiving files as .zip file. " + ex.getMessage());
            }
    	}
        
        
    }
    
    

}
