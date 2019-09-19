package com.virtubuild.custom.abbeconf.general;

import java.util.Collection;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;
import com.virtubuild.core.frame.rack.Size;

public class Rack {

    private Size size;
    private Component rackComponent;
    private RackController rackController;
    
    public Rack( Component rackComponent ){
        this.rackComponent = rackComponent;
        setRackController(rackComponent);
        if( rackController != null ) {
            this.size = rackController.getAvailableRackSize();
        } 
    }
    
    /**
     * gets the available width of the Rack
     * @return   rack width in integer
     */
    public int getWidth() {
        return size.width;
    }
    
    /**
     * Gets the available height of the rack
     * @return    rack height in integer
     */
    public int getHeight() {
        return size.height;
    }
    
    /**
     * Checks whether a specific rack slot is occupied
     * @param slot      x,y position of slot to check
     * @return          boolean true if occupied, false if not
     */
    public boolean isOccupied( Point slot ) {
       return rackController.isOccupied( Position.of(slot.getX(), slot.getY() ) );
    }
    
    /**
     * Get the component occupying a specific slot in the rack
     * @param slot      x,y position of the rack
     * @return          Component occupying the slot. null if none
     */
    public Component getOccupyingComponent(Point slot) {
        
        Collection<BaseComponent> list = rackController.getOccupyingComponents(Position.of(slot.getX(),slot.getY()));
        // FIXIT: what to do if there is MORE than one element in the list?
        if( !list.isEmpty() ) {
            return (Component) list.iterator().next();
        }
        return null;
    }
    
    /**
     * Trace to end of available rack size from the given slot position
     * @param start         x,y Point of the slot position to trace from
     * @param direction     Direction to trace in
     * @return              x,y Point of the first occupied slot in the trace direction. 
     *                      null if the input Point is out of bounds of the rack.
     *                      -1 in either x or y of the returned point if traced to boundary without finding occupied slot.
     */
    public Point traceToEnd(Point start, Direction direction ) {
        Point endPoint;
        switch(direction) {
            case LEFT:
                endPoint = traceLeft( start, 1 );
                break;
            case RIGHT:
                // check if -1 should be subtracted from width
                endPoint = traceRight( start, getWidth() );
                break;
            case DOWN:
                endPoint = traceDown( start, 1 );
                break;
            default: // case UP
                // TODO: check if -1 should be subtracted from height
                endPoint = traceUp( start, getHeight() );
                    
        }
        return endPoint;
    }
    
    /**
     * Trace up from a position to the end Y coordinate. Trace includes the start point!
     * @param start Point of slot to start trace from
     * @param endY  Y coordinate to trace to - assumes that it is higher than the start Y coordinate
     * @return    Point of first occurrence of occupied slot. returns null if not able to trace (e.g out of bounds)
     */
    private Point traceUp(Point start, int endY) {
        if( isOutOfBounds( start ) || start.getY() > endY ) {
            return null;
        }
        
        for( int i = start.getY() ; i <= endY; i ++ ) {
            Position pos = Position.of(start.getX(), i);
            if ( rackController.isOccupied( pos ) ) {
                return new Point(start.getX(), i);
            }
        }

        // reached end without finding occupied slot
        return new Point( start.getX(), -1);
    }
    
    /**
     * Trace down from a position to the end Y coordinate. Trace includes the start point!
     * @param start Point of slot to start trace from
     * @param endY  Y coordinate to trace to - assumes it is lower than the start Y coordinate
     * @return    Point of first occurrence of occupied slot. returns null if not able to trace (e.g out of bounds)
     */    
    private Point traceDown(Point start, int endY) {
        if( isOutOfBounds(start)  || start.getY() < endY ) {
            return null;
        }
        
        for( int i = start.getY() ; i >= endY ; i-- ) {
            Position pos = Position.of( start.getX(), i );
            if(rackController.isOccupied(pos) ) {
                return new Point(start.getX(), i);
            }
        }

        // TODO: how to determine rack edge reached without blocking element?
        return new Point(start.getX(), -1);
    }
    
    /**
     * Trace left from a position to the end X coordinate. Trace includes the start point!
     * @param start Point of slot to start trace from
     * @param endY  Y coordinate to trace to
     * @return    Point of first occurrence of occupied slot. returns null if not able to trace (e.g out of bounds)
     */
    private Point traceLeft(Point start, int endX) {
        
        if( isOutOfBounds(start) || start.getX() < endX ) {
            return null;
        }
        
        for( int i = start.getX() ; i >= endX ; i-- ) {
            Position pos = Position.of(i, start.getY() );
            if( rackController.isOccupied( pos ) ) {
                return new Point( i, start.getY() );
            }
        }
        
        return new Point( -1, start.getY() );
    }
    
    /**
     * Trace right from a position to the end X coordinate. Trace includes the start point!
     * @param start Point of slot to start trace from
     * @param endX  X coordinate to trace to
     * @return    Point of first occurrence of occupied slot. 
     *            returns Point with x coordinate -1 if traced to endX and no occupied slots found.
     *            returns null if not able to trace (e.g out of bounds)
     */
    private Point traceRight(Point start, int endX) {
        if( isOutOfBounds( start ) || start.getX() > endX ) {
            return null;
        }
        
        for (int i = start.getX() ; i <= endX ; i++ ) {
            Position pos = Position.of(i, start.getY() );
            if( rackController.isOccupied(pos) ) {
                return new Point( i, start.getY() );
            }
        }
        
        return new Point( -1, start.getY() );
    }
    
    /**
     * Sets the RackController of the input Component
     * @param rackComponent     Component set the RackController for
     */
    private void setRackController(Component rackComponent) {
        ComponentState compState = (ComponentState) rackComponent;
        rackController = (RackController) compState.getFrameController(FrameControllerType.RACK);
    }
    
    /**
     * Checks if a given x,y position is out of bounds for the Rack
     * @param position  x,y position to check
     * @return          Boolean true if out of bounds. false if not
     */
    private boolean isOutOfBounds(Point position) {
        int x = position.getX();
        int y = position.getY();
        return x < 0 || x > size.width || y < 0 || y > size.height;
    }
    
}
