package com.virtubuild.services.abbeconf.warning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.virtubuild.core.World;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;

public class ConnectionDirectionChanged extends CompleterSkeleton {

	private Configuration configuration;
	
	private static String connection_direction_previous = "bottom";
	
	private static String connection;
	
	private static Component currentlyPickedComponent;
	
	private static Component currrentColumn; 
	
	private static String column_width;
	
	private static Component kit_frame; 
	
	private static int no_count = 0;
	
	private static boolean isDelete;
	
	private static HashMap<String, Component> componentsToDelete = new HashMap<>();
	
	
	 
	@Override
	public boolean doComplete() {
	    
	    
		configuration = getConfiguration();
		
			
		currentlyPickedComponent = ((World) configuration).getCurrentComponent();
		
		if(currentlyPickedComponent.getName().contains("Section") && !currentlyPickedComponent.getName().contains("Coupler")){
			
			currrentColumn = currentlyPickedComponent;
			
			connection = currrentColumn.getStateVector().getVar("connection").toString();
			connection = connection.substring(connection.indexOf("Value :") + 8, connection.length());
			connection = connection.replace(")", "");
			
			
			column_width = currentlyPickedComponent.getStateVector().getVar("width_column_external").toString();
			column_width = column_width.substring(column_width.indexOf("Value :") + 8, column_width.length());
			column_width = column_width.replace(")", "");
			
			
			if(null != column_width){
				
				if(Integer.valueOf(column_width) <= 850){
					
					String string1 = currentlyPickedComponent.getStateVector().getVar("kit_frames_n185_rbbs_1").toString();
					String array1[] = string1.split(":");
					String curent_kit_frame1 = array1[2].substring(0, array1[2].length() - 1).replaceAll("\\s", "");
					Component kit_frame1 = configuration.getComponent(curent_kit_frame1);
					
					
					
					kit_frame = kit_frame1;
					
	
				}
				else{
					
					String string2 = currentlyPickedComponent.getStateVector().getVar("kit_frames_n185_rbbs_2").toString();
					String array2[] = string2.split(":");
					String curent_kit_frame2 = array2[2].substring(0, array2[2].length() - 1).replaceAll("\\s", "");
					Component kit_frame2 = configuration.getComponent(curent_kit_frame2);
					
					
					
					kit_frame = kit_frame2;
					
				} 
				
			}
				
		}
		
		
		displayComponents();
		
		
		// Checking for connection direction change
	
		
		if (null!= connection && !connection.toString().equals(connection_direction_previous) && (no_count == 0) && !componentsToDelete.isEmpty()){
			
			int n = JOptionPane.showConfirmDialog(
	                new JFrame(), "<html>Changing the connection direction, the available space for optional kits is reduced of 150 mm <br> and the system will remove all the inserted kits in this column. <br> Please confirm before proceeding. </html>",
			"Warning!",
	                JOptionPane.YES_NO_OPTION);
			
				if (n == JOptionPane.YES_OPTION) {
					
					// Proceed 
					
					connection_direction_previous = connection;
					
					// Delete the kits/ components in the kit frame
					
					
					try{
					    
					    Iterator<Entry<String, Component>> componentNameAndObject = componentsToDelete.entrySet().iterator();
                            
				        while (componentNameAndObject.hasNext()){
                            
				            Entry<String, Component> entry = componentNameAndObject.next();
                            
                            if (entry.getValue().canDelete()){
                                entry.getValue().delete();
                            }
                            
                            componentNameAndObject.remove();
                            
                        }
				        
					    
					   
					}
					catch(Exception e){
					    System.out.println(e.getMessage());
					}
					
					
					
				
				} else if (n == JOptionPane.NO_OPTION) {
					
					// Rollback variable value
					
					currrentColumn.getStateVector().getVar("connection").setValue(connection_direction_previous);
					no_count = 11;
					
				} 
				
			
		}
		
		
		if(no_count > 0){
			no_count --;
		}
			
		    
			return true;
			
	}
	
	
	 
	
	@Override
    public boolean isComplete() {
	    
        return true;
        
    }


	/**
	 * Adding the optional kits/ components into a HashMap componentsToDelete
	 */
	private void displayComponents(){
	    
	    List<Component> listComponents = currentlyPickedComponent.getChildren();
        
	    
        List<Component> listSubComponents = new ArrayList<>();
        
        for (Component component : listComponents){
            
            
            System.out.println("Component test: " + component);
            
            
            if ( component.toString().contains("kit") && !component.toString().contains("frames") ){
                
                System.out.println("Can delete " + component.toString() + " ? " + component.getStateVector().getVar("mandatory").getValue());
                
            }
            
            
            if (!component.toString().contains("kit_fuseswitchdisconnector_n185") && !component.toString().contains("kit_npebusbarsystem_n185") && !component.toString().contains("column") && !component.toString().contains("door") && !component.toString().contains("column") && !component.toString().contains("kit_frames_n185_rbbs") ){
                
                Set<String> keys = componentsToDelete.keySet();
                
                boolean keyFound = false;
                
                String componentName = component.toString();
                
                for(String key: keys){
                    
                    if (key.contains(componentName)){
                        
                        keyFound = true;
                        break;
                    }
                    
                }
                
                if(!keyFound){
                    
                    componentsToDelete.put(componentName, component);
                    
                }
                
                
            }
            
            
            if(component.toString().contains("kit") && !component.toString().contains("frames") &&  (Integer.valueOf(component.getStateVector().getVar("mandatory").getValue().toString()) != null ) && ( Integer.valueOf(component.getStateVector().getVar("mandatory").getValue().toString()) == 0) ){ // Check if not Mandatory then delete
                
                
                System.out.println("Mandatory ? " + Integer.valueOf(component.getStateVector().getVar("mandatory").getValue().toString()));
                
                Set<String> keys = componentsToDelete.keySet();
                
                boolean keyFound = false;
                
                String componentName = component.toString();
                
                for(String key: keys){
                    
                    if (key.contains(componentName)){
                        
                        keyFound = true;
                        break;
                    }
                    
                }
                
                if(!keyFound){
                    
                    componentsToDelete.put(componentName, component);
                    
                }
                
                
            }
 
                
            
            if (component.toString().contains("kit_frames_n185_rbbs")){
                
                listSubComponents = component.getChildren();
                
            }
            
        }
        
        
         for(Component component : listSubComponents){
                    
                System.out.println("Sub components: " + component);
                System.out.println("Can delete " + component.toString() + " ? " + component.getStateVector().getVar("mandatory").getValue());
                    
        }
        
        
	}


    

	@Override
    public boolean isCompleteSupported() {
        return true;
    }



    @Override
    public boolean doCompleteSupported() {
        return true;
    }

    @Override
    protected void init() {
        
        super.init();
        
        configuration = getConfiguration();
        
    }


}
