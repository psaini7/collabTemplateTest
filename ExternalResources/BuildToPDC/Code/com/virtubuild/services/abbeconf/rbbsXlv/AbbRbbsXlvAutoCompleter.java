/******************************************************************
 * AbbRbbsXlvAutoCompleter                                        *
 * Automatically inserts kits in the XLV column.	           	  *                                         
 ******************************************************************/
/**@author Aagreet Sinha*/
package com.virtubuild.services.abbeconf.rbbsXlv;

import com.virtubuild.core.World;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;

public class AbbRbbsXlvAutoCompleter  extends CompleterSkeleton {

    private Configuration configuration;
    private RbbsXlvAutoCompleterManager autoManager;
    
    public boolean doComplete() {
    	Component cmp = initAutoManager();
    	autoManager.loadKits(cmp,configuration);
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

    // get configuration
    private Component initAutoManager() {
        configuration = getConfiguration();
        /* get the currently picked component (i.e. the component currently selected in Configurator) */
	    Component currentlyPickedComponent = ((World) configuration).getCurrentComponent();
	    autoManager = new RbbsXlvAutoCompleterManager();
	    return currentlyPickedComponent;
    }      
}
