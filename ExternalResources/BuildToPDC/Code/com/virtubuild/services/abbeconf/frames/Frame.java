/******************************************************************
 * Frame                                                          *
 * Collection of calculated frame elements. Is responsible for    *
 * the creation of new FrameElements.                             *
 ******************************************************************/
package com.virtubuild.services.abbeconf.frames;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.virtubuild.custom.abbeconf.general.*;
import com.virtubuild.services.abbeconf.frames.FrameElement.AttachmentType;

public class Frame {

    private List<FrameElement> horizontalElements;
    private List<FrameElement> verticalElements;
    private Column parent;
    
    public static final String FRAME_TOA = "kit_frames";
    
//    private Point supportFramePosition;
    
    public Frame( Column parent ) {
        this.parent = parent;
        initFrameElements();
    }

   
    /**
     * Returns the Collection of frameElements of either horizontal or vertical elements
     * @param orientation   whether to return horizontal or vertical elements
     */
    public Collection<FrameElement> getElements( Orientation orientation ){
        initFrameElements();
        if( orientation == Orientation.VERTICAL ) {
            return verticalElements;
        }
        return horizontalElements;
    }
    
    /**
     * Returns the Collection of all frame elements in the frame. Both vertical and horizontal
     */
    public Collection<FrameElement> getElements(){
        initFrameElements();
        return concatFrameElements();
    }
    
    /**
     * Adds a FrameElement to this frame given a point and the orientation to create in.
     * @param type         type of FrameElement to add
     * @param point        start point of the FrameElement
     * @param orientation  orientation of the frame element (Horizontal, vertical)
     * @param length       length of frame element to add
     * @param attachment   which direction the frame element attaches from
     */
    public void createFrameElement(FrameType type, Point point, Orientation orientation, int length, FrameElement.AttachmentType attachment) {
        FrameElement temp = new FrameElement( type, point, orientation, length, attachment );
        FrameElement element = trace( temp );
        addFrameElement( element );
    }
    
    /**
     * Adds a FrameElement to this frame given a point and the orientation to create in.
     * @param type       type of FrameElement to add
     * @param point      start point of the FrameElement
     * @param orientation  orientation of the frame element (Horizontal, vertical)
     * @param attachment  which direction the frame element attaches from (left, right, top, bottom) in relation to the slot elements (eg. left border of slot)
     */
    public FrameElement addFrameElement(FrameType type, Point point, Orientation orientation, int length, FrameElement.AttachmentType attachment) {
        FrameElement element = new FrameElement(type, point, orientation, length, attachment );
        addFrameElement( element );
        return element;
    } 
    
    /**
     * Adds a FrameElement to this frame
     * @param element  the FrameElement to add
     */
    public void addFrameElement(FrameElement element) {
        if( element.getOrientation() == Orientation.HORIZONTAL ) {
            horizontalElements.add( element );
        }else {
            verticalElements.add( element );
        }
    }

    /**
     * Adds FrameElements for a BASE frame. 
     * @param startX  x coordinate of start point
     * @param startY  y coordinate of start point
     * @param width   width of BASE frame
     * @param height  height of BASE frame
     */
    public void addBaseFrame(int startX, int startY, int width, int height ) {
        addFrameElement( FrameType.BASE, new Point( startX, startY), Orientation.HORIZONTAL, width, FrameElement.AttachmentType.BOTTOM ); // top - attachment to bottom of frame
        addFrameElement( FrameType.BASE, new Point( startX, height), Orientation.HORIZONTAL, width, FrameElement.AttachmentType.TOP ); // bottom - attachment to top of frame
        addFrameElement( FrameType.BASE, new Point( startX, startY), Orientation.VERTICAL, height, FrameElement.AttachmentType.RIGHT ); // left  - attachment to left of frame
        addFrameElement( FrameType.BASE, new Point( startX + width, startY), Orientation.VERTICAL, height, FrameElement.AttachmentType.LEFT ); // right - attachment to right of frame
    }
    
    /**
     * Traces a frame element to a blocking kit or the frame   
     * @param element  FrameElement to trace
     * @return         FrameElement traced to blocking kit or frame
     */
    public FrameElement trace( FrameElement element ) {
        if( element.getOrientation() == Orientation.VERTICAL ) {
            return traceVerticalFrameElement( element );
        }
        return traceHorizontalFrameElement( element );
    }
    
    /**
     * Trace from a point in the given direction until encountering a kit or the edges
     * @param point      Point to trace from
     * @param direction  Grid.Direction to trace in
     * @return           new Point traced
     */
    public Point trace( Point point, Direction direction ) {
        
//       return parent.traceToEnd(point, direction);
        switch( direction ) {
            case LEFT:
                return parent.traceLeft( point );
            case RIGHT:
                return parent.traceRight( point );
            case UP:
                return parent.traceUp( point );
            default: // down
                return parent.traceDown( point );                
        }
        
    }
    
    /**
     * Tries to anchor a vertical FrameElement to horizontal FrameElements. If no horizontal
     * FrameElements exist that can anchor the end points, a new horizontal FrameElement
     * will be created for each unanchored end point.
     * @param  element  FrameElement to anchor
     */
    public void anchorFrameElement( FrameElement element ) {
        boolean startPointIsCovered = false;
        boolean endPointIsCovered   = false;
        
        if(element.getOrientation() != Orientation.VERTICAL) {
           return;
        }
        
        for(FrameElement hElement : horizontalElements) {
            if( startPointIsCovered && endPointIsCovered ) { 
                break;
            }
            
            if( !startPointIsCovered ) {
                // only attach from the correct side
                startPointIsCovered = hElement.includes( element.getStartPoint() );
                // update attachment side of element if element is currently attached on top
                if( startPointIsCovered && hElement.getAttachment() != AttachmentType.BOTTOM) {
                    hElement.setAttachement( AttachmentType.BOTH);
                }
            }
            if( !endPointIsCovered ) {
                endPointIsCovered = hElement.includes( element.getEndPoint() );
                // update attachment side of element if element is currently attached on BOTTOM
                if( endPointIsCovered && hElement.getAttachment() != AttachmentType.TOP) {
                    hElement.setAttachement( AttachmentType.BOTH);
                }
            }
        }

        // create new frame element which has attachments from bottom
        if ( !startPointIsCovered ) {
            createFrameElement(FrameType.INTERNAL, element.getStartPoint(), Orientation.HORIZONTAL, 1, FrameElement.AttachmentType.BOTTOM);
        }
        // create a new frame element which has attachments from top
        if( !endPointIsCovered ) {
            createFrameElement(FrameType.INTERNAL, element.getEndPoint(), Orientation.HORIZONTAL, 1, FrameElement.AttachmentType.TOP);
        }
    }
    
/* ***** Private methods ***** */
 
    private void initFrameElements() {
        if( horizontalElements == null ) {
            horizontalElements = new LinkedList<FrameElement>();
        }
        if( verticalElements == null ) {
            verticalElements = new LinkedList<FrameElement>();
        }
    }
    
    /**
     * Returns a collection of both vertical and horizontal frame elements
     */
    private Collection<FrameElement> concatFrameElements(){
        List<FrameElement> allElements = new LinkedList<FrameElement>();
        allElements.addAll( verticalElements );
        allElements.addAll( horizontalElements );
        return allElements;
    }

    /**
     * Traces a vertical FrameElement to first occupied slot or base frame.
     * @param element  FrameElement to trace
     * @return         FrameElement traced vertically to first occupied slot or the frame.
     */
    private FrameElement traceVerticalFrameElement( FrameElement element ) {
        // trace down from frame element to kit or WR frame
        Point startPoint = traceDown( element.getStartPoint() );
        Point endPoint = traceUp( element.getEndPoint() );
        // calculate length from the two points
        int length = Point.calcDistance( startPoint , endPoint ) ;
        return new FrameElement( FrameType.INTERNAL, startPoint, Orientation.VERTICAL, length, element.getAttachment() );
    }
    
    /**
     * Traces a horizontal FrameElement to first occupied slot or base frame.
     * @param element  FrameElement to trace
     * @return         FrameElement traced vertically to first occupied slot or the frame.
     */
    private FrameElement traceHorizontalFrameElement ( FrameElement element ) {
        Point startPoint = traceLeft( element.getStartPoint() );
        Point endPoint = traceRight( element.getEndPoint() );

        int length = Point.calcDistance( startPoint, endPoint );
        
        return new FrameElement( element.getType(), startPoint, element.getOrientation(), length, element.getAttachment() );
    }
    
    /**
     * Trace up from the Point until blocking kit is encountered
     * @param point     Point to trace from
     * @return          Point traced to
     */
    private Point traceUp( Point point ) {
        return parent.traceUp( point );
    }

    /**
     * Trace down from the Point until blocking kit is encountered
     * @param point     Point to trace from
     * @return          Point traced to
     */
    private Point traceDown( Point point ) {
        return parent.traceDown( point );
    }
    
    /**
     * Traces a point horizontally to the right.
     * Assumes the elements to trace to are sorted by X coordinate.
     * @param point  Point to trace
     * @return       
     */
    private Point traceRight( Point point ) {
        int tracedX = point.getX();
        for( FrameElement element : verticalElements ) {
            // skip elements placed to the left of the point to trace 
            if ( element.getStartPoint().getX() < point.getX() ) {
                continue;
            }

            // stop at first found blocking element, since the elements are sorted by x descending
            if( canTraceTo( point, element) ) {
                tracedX = element.getStartPoint().getX();
                break; 
            }
        }
        return new Point( tracedX, point.getY() );
    }
    
    /**
     * Traces a point horizontally to the left
     * Assumes the elements to trace to are sorted by X coordinate.
     * @param point  Point to trace
     * @return       Traced point
     */
     private Point traceLeft( Point point ) {
        int tracedX = point.getX();
        for( FrameElement element : verticalElements ) {
            // skip elements placed to the right of the endpoint since we are tracing left
            if ( element.getStartPoint().getX() > point.getX() ) {
                continue;
            }

            if( canTraceTo( point, element) ) {
                tracedX = element.getStartPoint().getX();
            }
        }
        return new Point( tracedX, point.getY() );
    }
    
    /**
     * Checks whether a given point can be traced horizontally to the given frame element
     * @param point    point to check 
     * @param element  element to check on
     * @return         returns whether the point can be traced to the FrameElement
     */
    private boolean canTraceTo( Point point, FrameElement element) {
        int tempX = element.getStartPoint().getX();
        Point tempPoint = new Point (tempX, point.getY() );
        
        // element end point should not be considered
        if( element.includes( tempPoint, false ) ) {
            return true;
        }
        return false;
    }
   
}
