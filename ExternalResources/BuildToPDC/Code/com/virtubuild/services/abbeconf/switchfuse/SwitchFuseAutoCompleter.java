/******************************************************************
 * FuseSwitchAutoCompleter                                        *
 * It has the methods to calculate the 50/100 device compensator  *
 * kits position in the	rack/kit frames and sets the max possible *
 * values for the kits for Fuse Switch Disconnector column in N185*                                      	  *                                         
 ******************************************************************/
package com.virtubuild.services.abbeconf.switchfuse;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;

public class SwitchFuseAutoCompleter { 
	private Component column;
	Configuration configurator;
	private int p=1;

    /**
     * Constructor
     * @param column         column Component
     * @param configManager  manager object with functionality for accessing the model configuration
    */
    public SwitchFuseAutoCompleter(Component column, Configuration configurator) {
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
    	
    	for (int i = 1; i<14; i++) {   				
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
   /* if (column.getStateVector().getVar(str).getLegalValues().size() >=4 ){
	  	if( column.getStateVector().getRole(str).getValue() == StateValue.NONE )
	  		{
	  			 Create a component for rack controller 
		  		ComponentState componentdevicecompensator100 = (ComponentState) configurator.createComponent("device_compensator_100");
		  		componentdevicecompensator100.finishState();
		  		
		  		//controller.setRackElement(Position.of(i,1), componentdevicecompensator100);
		  		controller.insert(Position.of(i,1), componentdevicecompensator100,false);
	  		}
    	}*/
    
    if (column.getStateVector().getVar(str).getLegalValues().size() >= 3 ){
	  	if( column.getStateVector().getRole(str).getValue() == StateValue.NONE )
	  		{
	  			/* Create a component for rack controller */
	  			ComponentState componentdevicecompensator50 = (ComponentState) configurator.createComponent("device_compensator_50");
	  			componentdevicecompensator50.finishState();
  			
	  			//controller.setRackElement(Position.of(i,1), componentdevicecompensator50);
	  			controller.insert(Position.of(i,1), componentdevicecompensator50,false);
		   	}
    	}
    }	
   	  	
}
