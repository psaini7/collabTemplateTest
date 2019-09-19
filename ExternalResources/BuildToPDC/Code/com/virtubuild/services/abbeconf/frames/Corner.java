/******************************************************************
 * Corner                                                         *
 * Represents a corner of a kit as the slot location of the corner*
 * and which corner it is (e.g TOP_LEFT).                         *
 * The corner also has a reference to the Kit it belongs to       *
 ******************************************************************/
package com.virtubuild.services.abbeconf.frames;

import java.util.Collection;

import com.virtubuild.custom.abbeconf.general.Orientation;
import com.virtubuild.custom.abbeconf.general.Point;

public class Corner {

    private CornerType type;
    private Kit parent;
    private Point point;

    /**
     * Creates a new instance of type Corner
     * @param type  The corner position  ( top left, top right, bottom right, bottom left)
     * @param point  The corner point
     * @param parent The kit to which the corner belongs
     */
    public Corner(CornerType type, Point point, Kit parent ) {
        this.type = type;
        this.point = point;
        this.parent = parent;
    }

    public CornerType getType() {
        return type;
    }

    public Point getPoint() {
        return point;
    }

    public Frame getFrame() {
        return parent.getFrame();
    }

    /**
     * Checks if this corner can be covered with an existing FrameElement
     * @return  true / false on whether the corner is attached to a FrameElement
     */
    public boolean isAttached() {
        Collection <FrameElement> elements = getFrame().getElements( Orientation.VERTICAL );
        for ( FrameElement e : elements ) { // only vertical elements can cover a corner
            if ( e.getOrientation() == Orientation.VERTICAL && isCoveredBy( e ) ) {
                if( e.getType() == FrameType.INTERNAL) {
                    //update attachment side of element if needed
                    updateAttachment( e );
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Overrides object toString() method
     */
    public String toString() {
        return type + " " + point.toString();
    }

    /**
     * Checks whether a given FrameElement covers this Corner
     * @param element  FrameElement to check if covers this Corner
     * @return         true / false
     */
    private boolean isCoveredBy( FrameElement element ) {

        return element.includes( point ); /*&& isAttachable( element )*/
    }
    
    /**
     * Updates the element attachment side to BOTH if the corner attaches to 
     * the opposite side of the element, than kits are currently attached to.
     * @param element   element to update attachment on.
     */
    private void updateAttachment(FrameElement element) {
        FrameElement.AttachmentType attachment = element.getAttachment();
        switch ( attachment ) {
            case LEFT: // vertical internal frames only
                if( type == CornerType.BOTTOM_LEFT || type == CornerType.TOP_LEFT ) {
                    element.setAttachement( FrameElement.AttachmentType.BOTH );
                }
                break;
            case RIGHT: // vertical internal frames only
                if( type == CornerType.BOTTOM_RIGHT || type == CornerType.TOP_RIGHT ) {
                    element.setAttachement( FrameElement.AttachmentType.BOTH );
                }
                break;
            case BOTTOM: // horizontal internal frames only
                if( type == CornerType.BOTTOM_RIGHT || type == CornerType.BOTTOM_LEFT ) {
                    element.setAttachement( FrameElement.AttachmentType.BOTH );
                }
                break;
            case TOP:  // horizontal internal frames only
                if( type == CornerType.TOP_RIGHT || type == CornerType.TOP_LEFT ) {
                    element.setAttachement( FrameElement.AttachmentType.BOTH );
                }
                break;
            default: // middle
                // Do nothing       
        }
    }

}
