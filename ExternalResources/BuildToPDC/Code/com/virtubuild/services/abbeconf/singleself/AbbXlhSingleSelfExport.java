/**************************************************************************
 * Single Self Export                                					  *
 * It calculates the count of the variable xline_compartment_kit_qty  	  *
 * if spare and breaker or combination of both are in a sequence then it  *
 * counts it as one.													  *
 **************************************************************************/
/**@author Aagreet Sinha*/
package com.virtubuild.services.abbeconf.singleself;


import java.util.List;

import com.virtubuild.core.World;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.services.abbeconf.autocompleterswitchboard.SwitchboardAutoCompleterManager;

public class AbbXlhSingleSelfExport  extends CompleterSkeleton {

    private Configuration configurator;
    
    @Override
	public boolean doComplete() {
    	initAutoManager();
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

    // get all components as a list
    private void initAutoManager() {
    	configurator = getConfiguration();
        List<Component> allComponents = configurator.getAllComponents();
	    SingleSelfExport c = new SingleSelfExport(allComponents, configurator);
		c.loadKits();
	    
    }      
}
