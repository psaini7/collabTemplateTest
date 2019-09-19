/****************************************************************************
 * NpeOffsetRbbsCompleter                                 				*
 * Represents a Auto Completer class for insertion and deletion of 	        *
 * NPE Offset to solve all the illegal offset positions on a single click 	*
 ***************************************************************************/
/**
    @author Configit
*/
package com.virtubuild.services.abbeconf.npeoffsetrbbs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.custom.abbeconf.general.Column;
import com.virtubuild.custom.abbeconf.general.Switchboard;
import com.virtubuild.custom.abbeconf.utils.VariableUtils;

public class NpeOffsetRbbsCompleter  extends CompleterSkeleton {

    private static final Logger logger = LoggerFactory.getLogger( NpeOffsetRbbsCompleter.class);

    private static final String SWITCHBOARD_RBBS_TOA ="switchboard_rbbs";
    private static final String DEFAULT_CONNECTION_DIRECTION = "bottom";

    private Configuration configuration;
    private NpeOffsetRbbsManager offsetManager;
    
    private int previousColumnCount = 0;
    private String previousConnectionDirection = "";
    
    private Map<String, String> previousColumnConnections = new HashMap<>();
   
    @Override
	public boolean doComplete() {
    	initCompleter();
    	if( canRun() ) {
    	    logger.info("Running NPE Offset RBBS completer.");
    	    offsetManager.run();
    	    // update number of columns
    	    previousColumnCount = getCurrentColumnCount();
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
        configuration = getConfiguration();
        previousConnectionDirection = DEFAULT_CONNECTION_DIRECTION;
    }

    private void initCompleter() {
	    offsetManager = new NpeOffsetRbbsManager( configuration );
    }
    
    /**
     * Only run in certain cases
     * @return
     */
    private boolean canRun() {

        BaseComponent switchboard = getSwitchboard();
        if( switchboard == null || !isRbbs(switchboard) ) return false;
        
        int currentColumnCount = getCurrentColumnCount();
        if( currentColumnCount != previousColumnCount ) {
            logger.info("Running on change of column count.");
            previousColumnCount = currentColumnCount;
            updatePreviousColumnConnectionList(switchboard);
            return true;
        }
        
        String currentConnectionDirection = getConnectionDirection(switchboard);
        if( !currentConnectionDirection.equalsIgnoreCase(previousConnectionDirection) ) {
            logger.info("Running on change of switchboard connection direction.");
            previousConnectionDirection  = currentConnectionDirection;
            updatePreviousColumnConnectionList( switchboard );
            return true;
        } 
      
        return columnConnectionDirectionChanged( switchboard );
    }
    
    /**
     * Gets the current number of columns in the configuration
     */
    private int getCurrentColumnCount() {
        return configuration.getTOAComponents(Column.COLUMN_TOA).size();
    }
    
    /**
     * updats the list of column connections to match the current columns
     * @param switchboard
     */
    private void updatePreviousColumnConnectionList( BaseComponent switchboard ) {
        List<Component> columns = ((Component) switchboard).getLinkMesh();
        previousColumnConnections.clear();
        for(Component column : columns ) {
            previousColumnConnections.put( column.getID(), getConnectionDirection(column) );
        }
    }
 
    /**
     * gets the connection direction for the input component
     * @param component
     * @return
     */
    private String getConnectionDirection(BaseComponent component) {
        String connection = null;
        if( component != null ) {
            connection =  VariableUtils.getStringValue( (Component)component, Switchboard.CONNECTION_VAR );
        }
        return (connection != null) ? connection : DEFAULT_CONNECTION_DIRECTION;
    }
    
    /**
     * gets the current switchboard
     * @return
     */
    private BaseComponent getSwitchboard() {
        List<BaseComponent> switchboards = configuration.getTOAComponents(Switchboard.SWITCHBOARD_TOA);
        if( !switchboards.isEmpty() ) {
            return switchboards.get(0);
        }
        
        logger.info("No switchboards in current configuration.");
        return null;
    }
    
    /**
     * checks whether the input BaseComponent is rbbs switchboard
     * @param switchboard
     * @return
     */
    private boolean isRbbs(BaseComponent switchboard) {
        return switchboard.isTypeOfActor(SWITCHBOARD_RBBS_TOA);
    }

    /**
     * Checks that the direction connection has changed direction
     * @param switchboard
     * @return
     */
    private boolean columnConnectionDirectionChanged(BaseComponent switchboard) {

        List<Component> columns = ((Component) switchboard).getLinkMesh();
        for( Component column : columns ) {
            String id = column.getID();
            String connection = getConnectionDirection(column);
    
            if( !previousColumnConnections.containsKey( id ) ) {
                // add to previous connections map and assume no change in connection direction
                previousColumnConnections.put( id, connection );
            }
            else {
                // check if connection direction has changed
                String prevConnection = previousColumnConnections.get(id);
                if( !connection.equalsIgnoreCase(prevConnection) ) {
                    previousColumnConnections.replace(id, connection );
                    logger.info("Running on change of column connection direction");
                    return true;
                    
                }
            }
        }
        
        return false;
    }
   
}


