package com.virtubuild.custom.abbeconf.utils;

import java.util.Collection;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;
import com.virtubuild.core.frame.rack.Size;
import com.virtubuild.core.api.RackController;
import com.virtubuild.custom.abbeconf.general.Point;

/**
 * RackUtils - Utility functions for accessing a rack component.
 */
public class RackUtils {
    
    private static Logger logger = LoggerFactory.getLogger( RackUtils.class );
   
    /**
     * Checks whether a given position in a rack is already occupied
     * @param rackComp  Component with rack to check
     * @param position  Point (x,y) to check if occupied
     * @return          true if the position is occupied, false if not
     */
    public static boolean isOccupied( Component rackComp, Point position ) {
        RackController controller = getRackController( rackComp );
        if( controller != null ) {
            // return !controller.getValidator().isUnoccupied( Position.of(position.getX(), position.getY() ) );
            return controller.isOccupied( Position.of(position.getX(), position.getY()) );
        }
        return true;
    }
    
    /**
     * Inserts a component in a specific position in a rack. Returns true if possible to insert. Otherwise false.
     * @param rack       rack Component to insert into
     * @param position   position to insert in
     * @param component  Component to insert
     * @return           True if component was inserted. False otherwise
     */
    public static boolean insertComponentInRack( Component rack, Point position, Component component ) {
        RackController controller = getRackController( rack );
        Position pos = Position.of(position.getX(), position.getY() );
        if( controller != null ) {
//            ComponentState result = controller.setRackElement( Position.of(position.getX(), position.getY() ), (ComponentState) component);
            controller.insert(pos, (ComponentState) component, false);
            // TODO find better way of checking if component has been inserted
            return controller.isOccupied( pos );
        } 
        logger.error( "Not able to insert " + component + " on rack component " + rack );
        return false;
    }
    
    /**
     * Check whether a component can be inserted into the rack at the specified position
     * @param rack       rack Component to try inserting into
     * @param position   position Point to insert in
     * @param component  Component to insert
     * @return           true if the Component can be inserted at the given position, otherwise false.
     */
    public static boolean canInsert(Component rack, Point position, Component component ) {
        RackController controller = getRackController( rack );
        if( controller != null ) {
            // TODO: need to use get height and width of component
//            return controller.getValidator().canInsert( Rectangle.of( 0,0), Position.of( position.getX(), position.getY()), (ComponentState) component );
            return controller.canInsert( Size.of( 0,0), Position.of( position.getX(), position.getY()), (ComponentState) component );
        }
        return false;
    }

    /**
     * Gets all components in the rack
     * @param rack  rack Component to get elements for
     * @return      a Collection of all Components in the rack
     */
    public static Collection<Component> getRackComponents( Component rack ) {
        RackController controller = getRackController( rack );
        Collection<BaseComponent> baseComponents = controller.getComponents();
        Collection<Component> rackComponents = new LinkedList<>();
        for( BaseComponent comp : baseComponents) {
            rackComponents.add( (Component) comp );
        }
        return rackComponents;
    }
    
    /** 
     * gets the rack controller object associated with the input component. 
     * If the component does not have a rack component associated with it
     * null is returned.
     * @param component  Component with rack controller associated
     * @return           RackController if exist. Otherwise null
     */
    private static RackController getRackController( Component component ) {
        ComponentState compState = (ComponentState) component;
        return (RackController) compState.getFrameController(FrameControllerType.RACK);
    }
}
