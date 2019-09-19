/******************************************************************
 * ABB Frame Completer                                            *
 * Calculates and inserts frames on a rack controller depending   *
 * on the position and size of kits placed in the rack            *
 ******************************************************************/
package com.virtubuild.services.abbeconf.frames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.core.frame.FrameComplete;

public class AbbFrameCompleter extends CompleterSkeleton implements FrameComplete {

    private Configuration configuration;
    private FrameManager frameManager;
    
    // logger
    private static final Logger logger = LoggerFactory.getLogger(AbbFrameCompleter.class);


    public boolean doComplete() {

        return true;
    }

    // implements FrameComplete
    /**
     * Entry point for frame completer
     * @param arg0  picked Component at time of calling
     */
    public boolean doComplete( BaseComponent rackComponent ) {
        
        logger.info("Calculating frames for Rack Component "+ rackComponent.getID() );
        
        initFrameCompleter();
        frameManager.loadFrames( (Component) rackComponent );
        
        return true; 
    }
    
    @Override
    public boolean doCompleteSupported() {

        return true;
    }

    @Override
    protected void init() {
        // Auto-generated method stub
        super.init();
    }

    // get configuration
    private void initFrameCompleter() {
        configuration = getConfiguration();
        frameManager = new FrameManager( configuration );
    }
        
}
