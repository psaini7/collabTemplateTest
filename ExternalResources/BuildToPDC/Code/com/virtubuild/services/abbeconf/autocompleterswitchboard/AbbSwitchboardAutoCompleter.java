/******************************************************************
 * ABBAutoCompleterSwitchboard                                    *
 * Represents a Auto Completer class for insertion of 	          *
 * minimum number of kits in the empty slots for the 	   		  *
 * all the column at once from switchboard.						  *
 ******************************************************************/
/**@author Aagreet Sinha*/

package com.virtubuild.services.abbeconf.autocompleterswitchboard;

import java.util.List;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;

public class AbbSwitchboardAutoCompleter  extends CompleterSkeleton {

    private Configuration configurator;
    private SwitchboardAutoCompleterManager autoManager;
    
    public boolean doComplete() {
    	List<Component> cmp = initAutoManager();
    	autoManager.loadKits(cmp,configurator);
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
    private List<Component> initAutoManager() {
    	configurator = getConfiguration();
        List<Component> allComponents = configurator.getAllComponents();
	    autoManager = new SwitchboardAutoCompleterManager();
	    return allComponents;
    }      
}
