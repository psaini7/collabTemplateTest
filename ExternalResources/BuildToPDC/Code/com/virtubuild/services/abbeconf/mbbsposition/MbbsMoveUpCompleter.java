package com.virtubuild.services.abbeconf.mbbsposition;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.core.state.ValueStatus;
import com.virtubuild.custom.abbeconf.general.Column;
import com.virtubuild.custom.abbeconf.general.Switchboard;
import com.virtubuild.custom.abbeconf.utils.VariableUtils;

public class MbbsMoveUpCompleter extends CompleterSkeleton {
    
    private static final Logger logger = LoggerFactory.getLogger( MbbsMoveUpCompleter.class);
    
    private static final String MBBS_UP_LOCK_VAR   = "main_busbar_up_lock";
    
    private Configuration configuration;

    @Override
    public boolean doComplete() {
        //  entry point when called from configurator 
        
        configuration = getConfiguration(); 
        
         if( canRun() ) {
             logger.info("Running mbb position up completer");
             
             MbbsMoveUpManager manager = new MbbsMoveUpManager( configuration, logger );
             manager.run();         
         }
        //result
        return true;
    }
              
    @Override
    public boolean doCompleteSupported() {
                
        return true;
    }

    @Override
    protected void init() {
        // This init method runs at startup of the configurator
        super.init();
    }
    
    private boolean canRun() {
        List<BaseComponent> columns = configuration.getTOAComponents( Column.COLUMN_COMP );
        // FIXIT: may be multiple switchboards in future
        BaseComponent switchboard = configuration.getTOAComponents(Switchboard.SWITCHBOARD_TOA).iterator().next();

        return !columns.isEmpty() && !VariableUtils.getBooleanValue((Component) switchboard, MBBS_UP_LOCK_VAR) ;
    }

}
