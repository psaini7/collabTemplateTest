/******************************************************************
 * FuseSwitchAutoCompleter                                        *
 * It has the methods to calculate the 50/100 device compensator  *
 * kits position in the	rack/kit frames and sets the max possible *
 * values for the kits for Fuse Switch Disconnector column in N185*                                      	  *                                         
 ******************************************************************/
package com.virtubuild.services.abbeconf.rbbsXlh;

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

public class RbbsXlhAutoCompleter { 
	private Component column;
	Configuration configurator;
	private int p=1;
	
    /**
     * Constructor
     * @param column         column Component
     * @param configManager  manager object with functionality for accessing the model configuration
    */
    public RbbsXlhAutoCompleter(Component column, Configuration configurator) {
        this.column = column;
        this.configurator = configurator;
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
    	
    	for (int i = 1; i<41; i++) {   				
    			insertKitInRack(column,i);
    	}
    }
    
    /**
     * Insert the kit on the rack/kit_frames current position/role i.e element.x
    */
    private void insertKitInRack(Component column, int i) {
	
	// get component with rack controller
    ComponentState comp = (ComponentState) configurator.getComponent(column.getID());
    // Get rack controller
    RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);
    
    String str = "element."+p;
	p++;
    
	// add the new component to the specified position in the rack controller - check for null on rack first!
    if (column.getStateVector().getVar(str).getLegalValues().size() > 4){
	  	if( column.getStateVector().getRole(str).getValue() == StateValue.NONE )
	  		{
		  		/* Create a component for rack controller */
		  		ComponentState kitxlinecompblindrbbs = (ComponentState) configurator.createComponent("kit_xline_comp_blind_rbbs");
		  		kitxlinecompblindrbbs.finishState();
	  		
	  			//controller.setRackElement(Position.of(1,i), kitxlinecompblindrbbs);
		  		controller.insert(Position.of(1,i), kitxlinecompblindrbbs,false);
	  			assignHeightWidthValues(kitxlinecompblindrbbs);
	  		}
    	}
    
    if (column.getStateVector().getVar(str).getLegalValues().size() <= 4 && column.getStateVector().getVar(str).getLegalValues().size() > 1){
	  	if( column.getStateVector().getRole(str).getValue() == StateValue.NONE )
	  		{
	  			/* Create a component for rack controller */
	  			ComponentState componentdevicecompensator50 = (ComponentState) configurator.createComponent("device_compensator_50");
	  			componentdevicecompensator50.finishState();
	  			
	  			//controller.setRackElement(Position.of(1,i), componentdevicecompensator50);
	  			controller.insert(Position.of(1,i), componentdevicecompensator50,false);
		   	}
    	}
    }	  	  	
    
    /**
     * Assign maximum legal height and width values of the current kits
     * @param device_width 
     */
	private void assignHeightWidthValues(Component kitComponent) {
		
		StateVar height = kitComponent.getStateVector().getVar("device_width");
		
		List<StateValue> lstHeight = height.getDomain();
		
		List<StateValue> legatListHeight = new ArrayList<>();
	
		for (int i = 0; i < lstHeight.size(); i++) {
			if (height.isLegalValue(lstHeight.get(i))) {
				legatListHeight.add(lstHeight.get(i));
			}
		}
		
		int maxHeight = 0;
		for(StateValue st :legatListHeight){
			int i = Integer.parseInt(st.toString());
			if(i>maxHeight){
				maxHeight = i;
			}
		}
		
		height.setValue(String.valueOf(maxHeight));

	}
}
