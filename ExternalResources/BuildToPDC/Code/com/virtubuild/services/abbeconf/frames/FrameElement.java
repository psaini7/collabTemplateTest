/******************************************************************
 * Frame Element                                                  *
 * Represents a horizontal or vertical frame element, where it    *
 * is positioned and from which side kits or frameElements can    *
 * attach to it.                                                  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.frames;

import java.util.Comparator;

import com.virtubuild.custom.abbeconf.general.*;

public class FrameElement  implements Comparable<FrameElement> {
    
    private FrameType type;
    private Point startPoint;
    private Orientation orientation;
    private int length; // in slot units (e.g 1, 2 etc)
    private AttachmentType attachment; // top, bottom, left, right, both
    private boolean isLegal = true; // whether the frame element is legal in the configuration (can be illegal size or position etc)

    /* Which side of the frame an element is attached , e.g. elements attached from LEFT side of frame. */
    public enum AttachmentType{
        TOP,    
        BOTTOM, 
        LEFT,
        RIGHT,
        BOTH
    }
    
    /**
     * Creates a new FrameElement of the specified FramePoint, orientation and length
     * @param point        start FramePoint of the FrameElement
     * @param orientation  orientation of the FrameElement (horizontal or vertical)
     * @param length       length of the frame element in slot units (e.g 1, 2)
     * @param attachment   which side of the frame an element is attached to
     */
    public FrameElement(FrameType type, Point point,  Orientation orientation, int length, AttachmentType attachment) {
        this.type = type;
        this.orientation = orientation;
        this.length = length;
        this.startPoint = point;
        this.attachment = attachment;
    }

    /**
     * Retrieves the FrameType of this FrameElement
     * @return the FrameType of this FrameElement
     */
    public FrameType getType() {
        return type;
    }
    
    /**
     * Set the type of the FrameElement
     */
    public void setType(FrameType type) {
        this.type = type;
    }
    
    /**
     * Set whether the frame element is legal
     */
    public void setLegal( boolean isLegal) {
        this.isLegal = isLegal;
    }
   /**
     * Retrieves the orientation of the FrameElement (horizontal or vertical)
     * @return  the orientation of the FrameElement
     */
    public Orientation getOrientation() {
        return orientation;
    }
    
    /**
     * Set the start point of this FrameElment
     * @param point  new start Point 
     */
    public void setStartPoint(Point point) {
        this.startPoint = point;
    }
    
    /**
     * Gets the starting point of the FrameElement
     * @return  FramePoint for the FrameElement
     */
    public Point getStartPoint() {
        return startPoint;
    }
    
    /**
     * Gets the end point of the FrameElement
     * @return  end Point for the FrameElement
     */
    public Point getEndPoint() {
        Direction direction = Direction.RIGHT; // horizontal frame element
        if( orientation == Orientation.VERTICAL ) {
            direction = Direction.UP;
        }
        return startPoint.calcNewPoint( direction, length );
    }

    /**
     * Sets the length of this FrameElement
     * @param length  new length of FrameElement
     */
    public void setLength(int length) {
        this.length = length;
    }
    
    /**
     * Gets the length of this FrameElement
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Set the attachment orientation of the element
     * @param attachment   attachment orientation to set
     */
    public void setAttachement( AttachmentType attachment) {
        this.attachment = attachment;
    }
    
    /**
     * Returns which side of the frame element a kit can be attached
     */
    public AttachmentType getAttachment() {
        return attachment;
    }
    
    /**
     * Returns whether this frame element is legal in the configurtion
     */
    public boolean isLegal() {
        return isLegal;
    } 
    /**
     * Whether the given FramePoint is a point on the FrameElement
     * @param   framepoint   the FramePoint to check is on the FrameElement
     * @return  true / false on whether the given FramePoint is a point on the FrameElement
     */
    public boolean includes( Point framepoint ) {
        Point endPoint = getEndPoint();
        
        if( orientation == Orientation.VERTICAL && startPoint.getX() == framepoint.getX() &&
                startPoint.getY() <= framepoint.getY() && endPoint.getY() >= framepoint.getY()) {
            return true;
        }
        
        if( orientation == Orientation.HORIZONTAL && startPoint.getY() == framepoint.getY() &&
                startPoint.getX() <= framepoint.getX() && endPoint.getX() >= framepoint.getX()) {
            return true;
        }
        return false;
    }

    public boolean includes( Point point, boolean includeEdgePoints ) {
        boolean includes = includes( point );
        if( includeEdgePoints ) {
            return includes;
        }
        // if exclude endpoint and the point is included on the line, check that the point is NOT an edge point
        if( includes ) {
            return !isEdgePoint( point );
        }
        return false;
    }
    
    /**
     * Checks whether a given point is either start or end point of the frame element
     * @param point
     * @return
     */
    public boolean isEdgePoint( Point point ) {
        int isStartPoint = point.compareTo( startPoint );
        if ( isStartPoint == 0 ) {
            return true;
        }
        int isEndPoint = point.compareTo( getEndPoint() );
        if ( isEndPoint == 0 ) {
            return true;
        }
        return false;
    }
    
    /**
     * Custom comparator - compares on start point, first x then y
     */
    public int compareTo( FrameElement element ) {
        return this.getStartPoint().compareTo( element.getStartPoint() );
    }
    
    /**
     * Compare on length of FrameElements
     */
        public static Comparator<FrameElement> FrameLengthComparator = new Comparator<FrameElement>() {
            public int compare(FrameElement elem1, FrameElement elem2) {
                return Integer.compare(elem1.getLength(), elem2.getLength());
            }
        };
        
        /**
         * Compare on start point of FrameElement (x then y)
         */
        public static Comparator<FrameElement> StartPointComparator = new Comparator<FrameElement>() {
            public int compare(FrameElement elem1, FrameElement elem2) {
                return elem1.compareTo(elem2);
            }
        };
        
        public static Comparator<FrameElement> YPositionComparator = new Comparator<FrameElement>() {
            public int compare( FrameElement elem1, FrameElement elem2) {
                return Integer.compare( elem1.getStartPoint().getY(), elem2.getStartPoint().getY() );
            }
        };

        /**
         * Compare on length and y position
         */
        public static Comparator<FrameElement> FrameLengthYPosComparator = new Comparator<FrameElement>() {
            public int compare(FrameElement elem1, FrameElement elem2) {
                int lengthCompare = Integer.compare(elem1.getLength(), elem2.getLength());
                if( lengthCompare != 0) {
                    return lengthCompare;
                }
                int posYCompare = Integer.compare(elem1.getStartPoint().getY(), elem2.getStartPoint().getY());
                return posYCompare;
            }
        }; 
        
        @Override
        public String toString() {
            return orientation + " " +startPoint + " " + length;
        }
}
