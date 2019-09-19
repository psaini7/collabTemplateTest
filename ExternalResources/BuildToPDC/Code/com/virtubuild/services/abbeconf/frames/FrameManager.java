/******************************************************************
 * Frame Manager class                                            *
 * Initiates frame calculations based on the incoming rack        *
 * component or based on all rack components in the configuration *
 ******************************************************************/
package com.virtubuild.services.abbeconf.frames;


import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
//import com.virtubuild.custom.abbeconf.general.Point;

public class FrameManager {

    private ConfigurationManager configManager;
//    private Configuration configuration;
    
    public FrameManager( Configuration configuration ) {
//        this.configuration = configuration;
        this.configManager = new ConfigurationManager( configuration );
    }
    
    
    /** 
     * Calculates frames for only the changed column
     * @param column  column Component to calculate the frames on
     */
    public void loadFrames( Component component ) {
        // only calculate frames for a column Component with possibility of inserting frames
        
        if ( canCalculateFrames( component ) ) {
            Column c = new Column(component, configManager);
            c.loadFrames();
        }
    }

    /** Checks whether a Component is a column type which can have frames
     * @param component  Component to check
     * @return           true / false
     */
    private boolean canCalculateFrames( Component component ) {
        String toa = component.getTypeOfActor();
        return toa.startsWith( Frame.FRAME_TOA ) && hasRack( component );
    }
    
    /** 
     * Checks if a component has a rack controller
     * @param comp  component to check
     * @return      boolean true if it is a rack component. Otherwise false
     */
    private boolean hasRack(Component comp) {
        return ((ComponentState) comp).getFrameController(FrameControllerType.RACK) != null;
    }

}
