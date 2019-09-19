/******************************************************************
 * Column_AC                                                      *
 * It has the methods to calculate the kits position in the		  * 
 * rack/kit frames and sets the max possible 					  *
 * values for the kits.                                      	  *                                         
 ******************************************************************/
package com.virtubuild.services.abbeconf.blindpanels;

import java.util.ArrayList;
import java.util.List;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;
import com.virtubuild.core.state.StateVar;

public class BlindPanelAutoCompleter{ 
	private Component column;
	Configuration configurator;
	private int p=1;
	private StateVar width;
	
	private static final String KIT_TOUCHGUARD_COMBILINEN = "kit_touchguard_combilineN";
	
	/* Storing the width of the kit for AutoCompleter
	 	to trace back the next lowest valid value. 
	 */
	private int[] widthDomain;
	private int reducedWidth = 1;
	private Component prevBlindPanel;

    /**
     * Constructor
     * @param column         column Component
     * @param configManager  manager object with functionality for accessing the model configuration
    */
    public BlindPanelAutoCompleter(Component column, Configuration configurator) {
        this.column = column;
        this.configurator = configurator;
        
        widthDomain = new int[5];
    }

    /**
     * Calculate and insert kits on this column
    */
    public void loadKits() {
    	getKitPositionInRack();
    	
    }
    
    /**
     * Gets the kit position on the rack/Kit_frames
    */
    private void getKitPositionInRack(){
    	
    	for (int i = 1; i<6; i++) {
    		for(int j = 1; j<15; j++){   				
    			insertKitInRack(column,i,j);
    		}
    	}
    }
    
    /**
     * Insert the kit on the rack/kit_frames current position/role i.e element.x
    */
    private void insertKitInRack(Component column, int i, int j) {
   	  	
    	// get component with rack controller
        ComponentState comp = (ComponentState) configurator.getComponent(column.getID());
        // Get rack controller
        RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);
        
        String str = "element."+p;
   	  	p++;
        
   	  	// add the new component to the specified position in the rack controller - check for null on rack first!
        if (column.getStateVector().getVar(str).getLegalValues().size() > 1){
   	  		if( column.getStateVector().getRole(str).getValue() == StateValue.NONE )
   	  		{
	   	  		/* Add a component to rack controller */
	   	     	ComponentState componentKitTouchGuard = (ComponentState) configurator.createComponent(KIT_TOUCHGUARD_COMBILINEN);
	   	     	componentKitTouchGuard.finishState();
   	  			//controller.setRackElement(Position.of(i,j), componentKitTouchGuard);
	   	     	controller.insert(Position.of(i,j), componentKitTouchGuard,false);
   	  			assignHeightWidthValues(componentKitTouchGuard);
        	}
        }
    } 
    
    /**
     * Assign maximum legal height and width values of the current kits
     */
	private void assignHeightWidthValues(Component kitComponent) {

		StateVar height = kitComponent.getStateVector().getVar("height");
		width = kitComponent.getStateVector().getVar("width");
		
		List<StateValue> lstHeight = height.getDomain();
		List<StateValue> lstWidth = width.getDomain();
		
		List<StateValue> legatListHeight = new ArrayList<>();
		List<StateValue> legalListWidth = new ArrayList<>();
	
		for (int i = 0; i < lstHeight.size(); i++) {
			if (height.isLegalValue(lstHeight.get(i))) {
				legatListHeight.add(lstHeight.get(i));
			}
		}
		
		for (int i = 0; i < lstWidth.size(); i++) {
			if (width.isLegalValue(lstWidth.get(i))) {
				legalListWidth.add(lstWidth.get(i));
			}
		}

		int maxHeight = 0;
		for(StateValue st :legatListHeight){
			int i = Integer.parseInt(st.toString());
			if(i>maxHeight){
				maxHeight = i;
			}
		}
		
		int maxWidth = 0;
		for(StateValue st :legalListWidth){
			int i = Integer.parseInt(st.toString());
			if(i>maxWidth){
				maxWidth = i;
			}
		}
				
		height.setValue(String.valueOf(maxHeight));
		width.setValue(String.valueOf(maxWidth));

	}
	
	
	/**
	 * This method recursively reduces the width of the component (Blind panel) till a valid state is reached.
	 * 
	 * @param column
	 * @param posi
	 * @param posj
	 */
	private void reduceWidthByStep(Component column, int posi, int posj){
		
		reducedWidth ++;
		
		int count = 0;
		
		List<StateValue> lstWidth = width.getDomain();
		
		List<StateValue> legalListWidth = new ArrayList<>();
		
		for (int i = 0; i < lstWidth.size(); i++) {
			if (width.isLegalValue(lstWidth.get(i))) {
				legalListWidth.add(lstWidth.get(i));
			}
		}
		
		for(StateValue st :legalListWidth){
			
			int widthL = Integer.parseInt(st.toString());
			
			widthDomain[count++] = widthL;
			
		}
		
		
		prevBlindPanel.getStateVector().getVar("width").setValue(String.valueOf(widthDomain[count - reducedWidth]));
		
		insertKitInRackTwinline(column, posi, posj);
		
		
	}
	
	
	/**
     * Calculate and insert kits on twinline column
    */
    public void loadKitsTwinline() {
    	getKitPositionInRackTwinline();
    	
    }
    
    /**
     * Gets the kit position on the twinline rack/Kit_frames
    */
    private void getKitPositionInRackTwinline(){
    	
    	for (int i = 1; i<7; i++) {
    		for(int j = 1; j<13; j++){   				
    			insertKitInRackTwinline(column,i,j);
    		}
    	}
    	
    }
    
    /**
     * Insert the kit on the twinline rack/kit_frames current position/role i.e element.x
    */
    private void insertKitInRackTwinline(Component column, int i, int j) {
   	  	
    	// get component with rack controller
        ComponentState comp = (ComponentState) configurator.getComponent(column.getID());
        // Get rack controller
        RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);
        
        String str = "element."+p;
   	  	
   	  	// Creating a new blind panel which will be inserted
   	  	
        
   	  	// add the new component to the specified position in the rack controller - check for null on rack first!
        if (column.getStateVector().getVar("frames_legal").getValue().getName().equals("TRUE")){  //column.getStateVector().getVar(str).getLegalValues().size() > 1   column.getStateVector().getVar("frames_legal").getValue().getName().equals("TRUE")
        	if(column.getStateVector().getVar(str).getLegalValues().size() > 1){
	        	if( column.getStateVector().getRole(str).getValue() == StateValue.NONE )
	   	  		{
		   	  		/* Add a component to rack controller */
	        		ComponentState componentKitTouchGuard = (ComponentState) configurator.createComponent(KIT_TOUCHGUARD_COMBILINEN);
		   	     	componentKitTouchGuard.finishState();
	   	  			//controller.setRackElement(Position.of(i,j), componentKitTouchGuard);
		   	     	controller.insert(Position.of(i,j), componentKitTouchGuard,false);
	   	  			assignHeightWidthValues(componentKitTouchGuard);
	   	  		    prevBlindPanel = componentKitTouchGuard;
	   	  		    
	   	  		}
   	  		}
        	
        	p++;
        	
        }
        
        else{
    			
    		reduceWidthByStep(column, i, j);
    		reducedWidth = 1;
    	}
        
        
        
    }
   	  	
}
