package com.virtubuild.services.abbeconf.frames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.virtubuild.core.api.Component;
import com.virtubuild.custom.abbeconf.general.Direction;
import com.virtubuild.custom.abbeconf.general.Orientation;
import com.virtubuild.custom.abbeconf.general.Point;
import com.virtubuild.custom.abbeconf.general.Rack;
import com.virtubuild.services.abbeconf.frames.FrameElement.AttachmentType;

/******************************************************************
 * Column                                                         *
 * Represents a rack column component and has knowledge of the    *
 * position of kits (through Rack) as well as the calculated      * 
 * FrameElements.                                                 *
 * Calculates both BASE and INTERNAL frames for the rack column   *
 * it represents.                                                 *
 * Vertical frame elements are calculated first, then the         *
 * horizontal elements are calculated from the end points of the  *
 * vertical frame elements.                                       *                                         
 ******************************************************************/
public class Column {
    
    private ConfigurationManager configManager; 
    private Component            column;        
    private List<Kit>            kits;          
    private Frame                frame;         
    private Rack                 rack;          // representation of the rack
    private int                  width;         // width of column in slot units
    private int                  height;        // height of column in slot units

    /**
     * Constructor
     * @param column         column Component
     * @param configManager  manager object with functionality for accessing the model configuration
     */
    public Column(Component column, ConfigurationManager configManager) {
        this.column = column;
        this.configManager = configManager;
        rack = new Rack( column );
        frame = new Frame( this );
        setColumnDimensions();
        this.kits = new LinkedList<>();
        setKits();
    }

    /**
     * Calculate and insert frames on this column
     */
    public void loadFrames() {
        configManager.clearFrames(column);
        if( kits.isEmpty() ) {
            return;
        }
        calcFrames();
        configManager.insertFrames(frame.getElements(), column);
    }
    
    /**
     * Gets the Frame object connected with this column
     */
    public Frame getFrame() {
        if (null == frame) {
            frame = new Frame( this );
        }
        return frame;
    }

    /**
     * Gets the width of the Column
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the Column
     */
    public int getHeight() {
        return height;
    }

    /**
     * Creates a frame element with the specifications
     * @param type       type of element to create
     * @param point      the start point of the element
     * @param orientation  whether the frame element is vertical or horizontal
     * @param length     the length of the frame element
     * @param attachment  whether the frame element is placed left, right, top or bottom or both.
     */
    public void createFrameElement(FrameType type, Point point, Orientation orientation, int length,
            FrameElement.AttachmentType attachment) {
        frame.createFrameElement(type, point, orientation, length, attachment);
    }

    /**
     * Traces a frame point up through the column, returning the first point where
     * it finds a blocking kit or the top of the frame.
     * @param point    point to trace from
     * @return         Point x,y of first encounter of a blocking kit, or the top of the frame if no
     *                 blocking kits are found during trace.
     */
    public Point traceUp( Point point ) {
        // if X is zero or y = column height no need to trace as it is the left or right edge of column.
        if( point.getX() <= 0 || point.getY() >= height) {
            return new Point( point.getX(), height );
        }
        
        Point traceFrom = translateFramePointToRackPosition(point, CornerType.TOP_LEFT);
        Point traced = traceFrom;
        
        // run this until top of rack or blocking kit
        boolean stop = false;
        while(!stop) {
            traced = rack.traceToEnd( traceFrom, Direction.UP );
            // input point out of bounds or traced to frame
            if( traced == null || traced.getY() < 0 ) {
                traced = new Point(point.getX(), height+1 ); // add one to height to ensure the translated point will be top of column
                stop = true;
            }
            else {
                // check if point has blocking kit - by checking on neighbour to the right
                if( hasBlockingKit(traced,Direction.RIGHT) ) {
                    stop = true;
                }
                else {
                    // continue trace from next slot
                    traceFrom = new Point( traceFrom.getX(), traced.getY() + 1 );
                }
            }
        }
        
        // Translate from rack slot position to frame point
        return translateRackPosToFramePoint(traced, CornerType.BOTTOM_RIGHT);
    }
    
    /**
     * Traces a frame point down through the column, returning the first point where
     * it finds a blocking kit or the bottom of the frame.
     * @param point    point to trace from
     * @return         Point x,y of first encounter of a blocking kit, or the bottom of the frame if no
     *                 blocking kits are found during trace.
     */
    public Point traceDown(Point point) {
        // if X is zero or y = 0 no need to trace as it is the left or right edge of column.
        if( point.getX() <= 0 || point.getY() <= 0) {
            return new Point( point.getX(), 0 );
        }
        Point traceFrom = translateFramePointToRackPosition(point, CornerType.BOTTOM_LEFT);
        Point traced = traceFrom; // trace Y point until kit or bottom of column
        
        // run this until top of rack or blocking kit
        boolean stop = false;
        while(!stop) {
            traced = rack.traceToEnd( traceFrom, Direction.DOWN );
            // input point out of bounds or traced to end (frame)
            if( traced == null || traced.getY() < 0 ) {
                traced = new Point(point.getX(), 0 );
                stop = true;
            }
            else {
                // check if point has blocking kit - by checking on neighbour to the right
                if( hasBlockingKit(traced, Direction.RIGHT )) {
                    stop = true;
                }
                else {
                    // continue trace from next slot
                    traceFrom = new Point( traceFrom.getX(), traced.getY()-1);
                }
            }
        }

        // Translate from rack slot position to frame point
        return translateRackPosToFramePoint(traced, CornerType.TOP_RIGHT);
    }

    /**
     * Traces a frame point to the right through the column, returning the first point where
     * it finds a blocking kit or the edge of the frame.
     * @param point    point to trace from
     * @return         Point x,y of first encounter of a blocking kit, or the edge of the frame if no
     *                 blocking kits are found during trace.
     */
    public Point traceRight(Point point) {
        
        //if y is zero or x = column width no need to trace as it is the left or right edge of column.
        if( point.getY() <= 0 || point.getX() >= width) {
            return new Point( width, point.getY() );
        }
        
        Point traceFrom = translateFramePointToRackPosition(point, CornerType.BOTTOM_RIGHT);
        Point traced = traceFrom; // trace X point until kit or right of column
        // run this until top of rack or blocking kit
        boolean stop = false;
        while(!stop) {
            traced = rack.traceToEnd( traceFrom, Direction.RIGHT );
            // input point out of bounds or traced to frame
            if( traced == null || traced.getX() < 0 ) {
                traced = new Point(width+1, point.getY() ); // add one to width to ensure the translated point will be width of column
                stop = true;
            }
            else {
                // check if point has blocking kit - by checking on neighbour above
                if( hasBlockingKit(traced, Direction.UP) ) {
                    stop = true;
                }
                else {
                    // continue trace from next slot
                    traceFrom = new Point( traced.getX() +1, traceFrom.getY() );
                }
            }
        }

        // Translate from rack slot position to frame point
        return translateRackPosToFramePoint(traced, CornerType.TOP_LEFT);

    }
    
    /**
     * Traces a frame point left through the column, returning the first point where
     * it finds a blocking kit or the edge of the frame.
     * @param point    point to trace from
     * @return         Point x,y of first encounter of a blocking kit, or the edge of the frame if no
     *                 blocking kits are found during trace.
     */
    public Point traceLeft(Point point) {
        
        //if y is zero or x = column width no need to trace as it is the left or right edge of column.
        if( point.getY() <= 0 || point.getX() <= 0) {
            return new Point( 0, point.getY() );
        }
        
        Point traceFrom = translateFramePointToRackPosition(point, CornerType.BOTTOM_LEFT);
        Point traced = traceFrom; // trace X point until kit or left of column
        // run this until top of rack or blocking kit
        boolean stop = false;
        while(!stop) {
            traced = rack.traceToEnd( traceFrom, Direction.LEFT );
            // input point out of bounds or traced to frame
            if( traced == null || traced.getX() < 0 ) {
                traced = new Point(0, point.getY() );
                stop = true;
            }
            else {
                // check if point has blocking kit - by checking on neighbour to above
                if( hasBlockingKit(traced, Direction.UP) ) {
                    stop = true;
                }
                else{
                    // continue trace from next slot
                    traceFrom = new Point( traced.getX()-1, traceFrom.getY() );
                }
            }
        }

        // Translate from rack slot position to frame point
        return translateRackPosToFramePoint(traced, CornerType.TOP_RIGHT);
    }
    

    /**
     * Sets the height and width of the Column
     */
    private void setColumnDimensions() {
        width = configManager.getRackWidth( column );
        height = configManager.getRackHeight( column );
    }
    
    /**
     * Retrieves the kits of the column
     */
    private void setKits() {
        Collection<Component> kitComponents = configManager.getKits(column);
        for (Component kitComp : kitComponents) {
            Kit kit = new Kit(this, kitComp, configManager);
            kits.add(kit);
        }
        sortKits();
    }

    /**
     * Sorts the Kits on width, height descending.
     */
    private void sortKits() {
        Collections.sort(kits);
    }

    /**
     * Calculates the frame for this column based on the column kits
     */
    private void calcFrames() {
        calcBaseFrames();
        calcInternalFrames();
        calcSupportFrames();
    }

    /**
     * Calculates Base frames and creates Base FrameElements for this column
     */
    private void calcBaseFrames() {
            List<Integer> baseFramePositions = calcBaseFramePositions();
            baseFramePositions = getLegalBaseFramePositions(baseFramePositions);
            createBaseFrames( baseFramePositions );
    }
    
    /**
     * Calculates the internal frames of the column.
     */
    private void calcInternalFrames() {
        calcVerticalFrames();
        calcHorizontalFrames();
    }

    /**
     * Calculates the vertical frames of the column
     */
    private void calcVerticalFrames() {
        for (Kit kit : kits) {
            kit.attach();
        }
    }

    /**
     * Calculates the horizontal frames for the column
     */
    private void calcHorizontalFrames() {
        List<FrameElement> verticalFrames = (List<FrameElement>) frame.getElements(Orientation.VERTICAL);
        Collections.sort(verticalFrames); // sort by x-axis
        for (FrameElement element : verticalFrames) {
            // only check internal frames for being anchored
            if (element.getType() != FrameType.BASE) {
                frame.anchorFrameElement(element);
            }
        }
    }
    
    /**
     * Calculate support frames for this column
     */
    private void calcSupportFrames() {
        if( configManager.needCenterSupport( column ) ) {
            FrameElement support = calcSupportFrame( column );
            frame.addFrameElement( support );
        }
    }
    
    /**
     * Calculate Support Frame element using best fit
     * @param component   Component to calculate support frame for
     * @return
     */
    private FrameElement calcSupportFrame( Component component ) {
        
        Collection<FrameElement> horizontalFrames = frame.getElements( Orientation.HORIZONTAL );

        FrameElement support = null;
        if( horizontalFrames.isEmpty() ) {
            support = calcSupportFrameDefault( component );
        }
        else {
            support = calcSupportFrameFromHorizontal( component, horizontalFrames );
            
            if( support == null ){
                support = calcSupportFrameDefault( component );
            }
            else if( !support.isLegal() ) {
                // check whether there is a legal default support position
                FrameElement tempSupport = calcSupportFrameDefault( component );
                if( tempSupport.isLegal() ) {
                    support = tempSupport;
                }
            }
        }

        removeFramesOnSupportPosition( horizontalFrames, support.getStartPoint() );
        splitVerticalFrames( support.getStartPoint() );        
        return support;
    }

    /**
     * Calculate default center support frame if there are no horizontal frames
     * @param component   frame component to calculate center support frame for
     */
    private FrameElement calcSupportFrameDefault( Component component ) {
        
        // support frame needs to be at least 450 units from top or bottom of column (row height 150 x 3 rows = 450)
        int rowHeight = configManager.getSlotHeight( component );
        int topPosY = calcTopPosition( height, rowHeight ); // 450 from top of column
        int bottomPosY = calcBottomPosition ( height, rowHeight ); // 450 from bottom of column
        
        boolean foundLegalPos = false;

        int [] prioritizedPositions = calcPrioritiesPriorityAsKey( height, topPosY, bottomPosY );
        
        Point possiblePosition = null;
        for( int i = 1; i < prioritizedPositions.length ; i++ ) {
            int posY = prioritizedPositions[i];
            Point checkPosition = new Point( 0, posY );
            // check if it is possible to trace from one side of the cabinet to the other
            Point tracedPoint = traceRight( checkPosition );
            if (tracedPoint.getX() == width ) {
                // support frame position found
                possiblePosition = checkPosition;
                foundLegalPos = true;
                break;
            }
        }

        // set position to default
        if ( possiblePosition == null ) {
            possiblePosition = new Point (0, calcMiddlePosition( height ) );
            configManager.logWarning("No legal support position found. Using default of middle of column.");
        }
        
        // create horizontal frame element of type SUPPORT
        FrameElement supportFrame = new FrameElement( FrameType.SUPPORT, 
                possiblePosition, 
                Orientation.HORIZONTAL, 
                width, 
                AttachmentType.BOTH);
        supportFrame.setLegal( foundLegalPos );
        return supportFrame;
    }
    
    /**
     * Calculate the support frame from horizontal frames - assumes the horizontal frame input is NOT null
     * @param component          Component to calcaulate frames for
     * @param horizontalFrames   List of horizontal FrameElements to use for support calculation
     * @return                   best fit support FrameElement created from horizontal frames. Null if no possible support frame could be found
     */
    private FrameElement calcSupportFrameFromHorizontal( Component component, Collection<FrameElement> horizontalFrames ) {

        List<FrameElement> hFrames = (List<FrameElement>) horizontalFrames;
        Collections.sort(hFrames, FrameElement.FrameLengthYPosComparator.reversed());
        
        List<SupportFrame> possibleFrames = findPossibleSupportFrames( hFrames, component );
        FrameElement support = (FrameElement) pickBestSupportFrame( possibleFrames );
        
        return support;
    }
    
    /**
    * Calculates the x coordinates of where to place the base frames. The returned list includes both 
    * left and right border of the column. The list is sorted ascending.
    * @return      Integer List of x coordinates of base frame positions. Sorted ascending.
    */
   private List<Integer> calcBaseFramePositions() {
       List<Integer> baseFramePositions = new ArrayList<>();
       baseFramePositions.add(0);          // base frame always needed for left side of column
       baseFramePositions.add( width );    // base frame always needed for right border of column
       for( Kit kit : kits ) {             // kits are sorted from largest to smallest
           int posX = kit.getPosition().getX();
           // check if kit is already covered by a base frame position
           boolean canCoverLeft = canCoverByBaseFrame( posX, baseFramePositions );
           if ( canCoverLeft ) {
               baseFramePositions.add( posX );
           }
           boolean canCoverRight = canCoverByBaseFrame( kit.getRightBorder() , baseFramePositions );
           if ( canCoverRight ) {
               baseFramePositions.add( kit.getRightBorder() );
           }
       }
       Collections.sort( baseFramePositions ); // sort ascending: 0 -> width

       return baseFramePositions;
   }
      
    /**
     * Creates an array of possible support frame positions ordered by position (y coordinate)
     * @param rackHeight  height of the rack to find the possible positions of
     * @param maxPos      max legal position to use 
     * @param minPos      min legal position to use
     * @return            integer array with priorities sorted by position
     */
    private int[] calcPrioritiesPositionAsKey( int rackHeight, int maxPos, int minPos ){
        
        int frameHeight = rackHeight + 1;// we need the frame positions between slots, and column height is in slots

        // calculate start position (middle of column)
        int middlePos = calcMiddlePosition( frameHeight );

        int[] priorities = new int[frameHeight]; 

        int nextPos = middlePos;
        int counter = 1;
        
        while ( nextPos <= maxPos && nextPos >= minPos ) {
            priorities[nextPos] = counter;

            
            if ( counter % 2 == 0) {
                nextPos = nextPos - counter;
            }
            else {
                nextPos = nextPos + counter;
            }
            counter++;        
        }
        
        return priorities;
    } 
    
    /**
     * Creates an array of possible support frame positions ordered by priority
     * @param rackHeight  height of the rack to find the possible positions of
     * @param maxPos      max legal position to use 
     * @param minPos      min legal positin to use
     * @return            integer array with priorities sorted by priority
     */
    private int[] calcPrioritiesPriorityAsKey(int rackHeight, int maxPos, int minPos){
        int frameHeight = rackHeight + 1;// we need the frame positions between slots, and column height is in slots

        // calculate start position (middle of column)
        int middlePos = calcMiddlePosition( frameHeight );

        int[] priorities = new int[maxPos - minPos + 2]; // +1 for 0 indexed, +1 as frames are +1 from slot height
        
        int nextPos = middlePos;
        int counter = 1;
        
        while ( nextPos <= maxPos && nextPos >= minPos ) {
                priorities[counter] = nextPos;
            
            if ( counter % 2 == 0) {
                nextPos = nextPos - counter;
            }
            else {
                nextPos = nextPos + counter;
            }
            counter++;        
        }
        
        return priorities;
          
    }
    
    /**
     * Calculates the middle of the column
     * @param height  height of column
     * @return        middle position Y
     */
    private int calcMiddlePosition(int height) {
        return height / 2;
//      return height / 2 + ( ( height % 2 == 0 ) ? 0 : 1);

    }

    /**
     * Calculate the top position for possible support frames
     * @param height     height of column
     * @param rowHeight  height of rows of the column
     * @return           the Y coordinate of the max possible position for a support frame
     */
    private int calcTopPosition(int height, int rowHeight ) {
        return height - ModelNames.DISTANCE_NO_SUPPORT_TOP_BOTTOM / rowHeight; // 450 from top of column
    }

    /**
     * Calculate the bottom position for possible support frames
     * @param height     height of column
     * @param rowHeight  height of rows of the column
     * @return           the Y coordinate of the min possible position for a support frame
     */
    private int calcBottomPosition(int height, int rowHeight ) {
        return ModelNames.DISTANCE_NO_SUPPORT_TOP_BOTTOM / rowHeight; // 450 from bottom of column
    }
        
    /**
     * Create base FrameElements from a list of x coordinates for where to place
     * the base frames.
     * @param xPositions   x coordinates
     */
    private void createBaseFrames( List<Integer> xPositions ) {
        
        Collections.sort( xPositions ); // sort ascending 0 -> width
        for( int i = 0 ; i < xPositions.size(); i++) {
            
            int frameWidth = 0;
            int posX = xPositions.get( i );
            // stop if width of column has been reached
            if( posX == width ) {
                break;
            }
            
            if( i+1 >= xPositions.size()  ) {
                // calculate with from xPos to column width
                frameWidth = width - posX;
            }
            else {
                frameWidth = xPositions.get( i + 1 ) - posX;
            }
            createBaseFrame(posX, frameWidth);
        }
    }
    
    /** creates a base frame of the given parameters
     * @param posX          x coordinate of where to place the base frame
     * @param frameWidth    width of the base frame
     */
    private void createBaseFrame(int posX, int frameWidth) {
        frame.addBaseFrame( posX, 0, frameWidth, height);
    }
        
    /**
     * Finds a list of possible support frames from a list of horizontal frames
     * @param hFrames    List of horizontal FrameElements to check for possible support frames
     * @param component  column component to find support element for
     * @return           List of possible support frames. Null if no horizontal frames could be support element
     */
    private List<SupportFrame> findPossibleSupportFrames( List<FrameElement> hFrames, Component component ){
        List<SupportFrame> possibleFrames = new LinkedList<SupportFrame>();
        
        // support frame needs to be at least 450 units from top or bottom of column (row height 150 x 3 rows = 450)
        int rowHeight = configManager.getSlotHeight( component );
        int topPosY = calcTopPosition( height, rowHeight ); // 450 from top of column
        int bottomPosY = calcBottomPosition( height, rowHeight ); // 450 from bottom of column

        int[] positionPriorities = calcPrioritiesPositionAsKey( height, topPosY, bottomPosY );

        int lastPosY = -1;
        int lastLength = 0;
        for(FrameElement element : hFrames){
            int posY = element.getStartPoint().getY();
            if( lastPosY != posY ) { // we only want to look at FrameElements at different posY
                lastPosY = posY;
                if( bottomPosY <= posY && posY <= topPosY ) {
                    if( lastLength <= element.getLength() ) {
                        lastLength = element.getLength();
                        SupportFrame newSupFrame = 
                               new SupportFrame(FrameType.SUPPORT, element.getStartPoint(),element.getOrientation(), element.getLength(), AttachmentType.BOTH);
                        newSupFrame.setPriority( positionPriorities[posY] );
                        possibleFrames.add( newSupFrame );
                    }
                    else { // we have found the possible frame support frames
                        break;
                    }
                }
            }
        }
        return possibleFrames;
    }
    
    /**
     * Chooses the best support frame from a list of possible support frames
     * @return   SupportFrame with best fit
     */
    private SupportFrame pickBestSupportFrame( List<SupportFrame> possibleFrames ) {
        SupportFrame support = null;
        if( !possibleFrames.isEmpty() ) {
            Collections.sort( possibleFrames, SupportFrame.PriorityComparator );
            support =  possibleFrames.get(0); // first element = highest priority
            // support frame should cover the whole column width
            if( support.getLength() != width ) {
               
               if( !canTraceWidth( support ) ) {
                   support.setLegal( false );
               }
               
               support.setLength( width );
               
               if( support.getStartPoint().getX() != 0 ) {
                   support.setStartPoint( new Point(0, support.getStartPoint().getY() ) );
               }
            }
        }  
        return support;
    }
    
    /**
     * Removes any horizontal frames on same Y position as support frame
     * @param horizontalFrames
     * @param supportPosition
     */
    private void removeFramesOnSupportPosition(Collection<FrameElement> horizontalFrames, Point supportPosition) {
        
        Collections.sort( (List<FrameElement>) horizontalFrames, FrameElement.YPositionComparator);
        int supportPosY = supportPosition.getY();
        
        Iterator<FrameElement> iterator = horizontalFrames.iterator();
        while( iterator.hasNext() ) {
            FrameElement next = iterator.next();
            int elemPosY = next.getStartPoint().getY();
            
            if( elemPosY > supportPosY ) {
                break;
            }
            
            if ( elemPosY == supportPosY) {
                iterator.remove();
            }
        }
    }

    private void splitVerticalFrames( Point supportPosition ) {
        Collection<FrameElement> verticalFrames = frame.getElements(Orientation.VERTICAL);
        LinkedList<FrameElement> newFrames = new LinkedList<FrameElement>();
        
        Iterator<FrameElement> iterator = verticalFrames.iterator();
        while( iterator.hasNext() ) {
            FrameElement next = iterator.next();
            if( needToSplitFrame( next, supportPosition ) ) {
                newFrames.addAll( splitVerticalFrame( next, supportPosition ) );
                iterator.remove();
            }
        }
        verticalFrames.addAll( newFrames );
    }
    
    
    /**
     * Splits vertical frames into two new FrameElements and inserts them into the verticalElement Collection
     * @param element   element to split into two
     */
    private LinkedList<FrameElement> splitVerticalFrame ( FrameElement element, Point supportPosition ) {
        // find start positions of the new elements
        Point topElemStart = element.getStartPoint();
        Point bottomElemStart = new Point (element.getStartPoint().getX(), supportPosition.getY() );
        // find lengths of the new elements
        int topElemLength = Point.calcDistance(topElemStart, bottomElemStart);
        int bottomElemLength = element.getLength() - topElemLength;

        // create the new frame elements
        LinkedList<FrameElement> newFrames = new LinkedList<FrameElement>();
        FrameElement topElem = new FrameElement( element.getType(), topElemStart ,element.getOrientation(), topElemLength, element.getAttachment() );
        newFrames.add( topElem );
        FrameElement bottomElem = new FrameElement( element.getType(), bottomElemStart ,element.getOrientation(), bottomElemLength, element.getAttachment() );
        newFrames.add( bottomElem );
        return newFrames;
    }
    
    /**
     * Checks if the input rack slot and the left neighbour is occupied by the same kit - thus blocking a FrameElement
     * @param rackSlot rack slot to check blocking on
     * @param neigbour 
     * @return  true if the kit blocks. otherwise false
     */
    private boolean hasBlockingKit(Point rackSlot, Direction direction ) {
        Component occupying = rack.getOccupyingComponent( rackSlot );
        Component occupyingNeighbour = rack.getOccupyingComponent( rackSlot.calcNewPoint(direction, 1)  );
        if(occupying == null || occupyingNeighbour == null ) {
            return false;
        }
        String neighbourID = occupyingNeighbour.getID();
        return occupying.getID() == neighbourID;
    }
        
    /**
     * Checks whether it is possible to create a base frame vertical element at the x coordinate.
     * Will return true if the x coordinate is not already covered by a base frame and it is possible 
     * to create a vertical base frame element at the x coordinate.
     * @param xPos         x coordinate to check
     * @param xPositions   list of x coordinates already covered by a base frame
     * @return             true if base frame can be created
     */
    private boolean canCoverByBaseFrame( int xPos, List<Integer> xPositions ) {
        boolean covered = existsIn( xPos , xPositions );
        return ( !covered && canTraceUp( new Point( xPos,0 ) ) );
    }
    
    /**
     * Checks if it is possible to trace a FrameElement from left to right column border
     * without blocking kits in either direction.
     * @param element       FrameElement to trace to column border
     * @return              true if it was possible to trace to both left and right column border. False if not.
     */
    private boolean canTraceWidth( FrameElement element ) {
        // does not make sense to trace horizontally on vertical frame elements
        if( element.getOrientation() != Orientation.HORIZONTAL ) {
            return false;
        }
        
        Point traceRight = traceRight( element.getEndPoint() );
        Point traceLeft = traceLeft( element.getStartPoint() );
        
        if( traceLeft.getX() == 0 && traceRight.getX() == width ) {
            return true;
        }
        return false;
    }
    
    private boolean canTraceHeight( Point point ) {
        Point traceUp = traceUp( point );
        Point traceDown = traceDown( point );
        return traceDown.getY() == 0 && traceUp.getY() == height;
    }

    private boolean canTraceUp( Point from ) {
        Point traced = traceUp(from);
        return traced != null && traced.getY() >= height ;
    }
    
    /**
     * Checks whether a given x coordinate is covered by a one of the frame positions in the given list
     * @param x              x coordinate to check
     * @param items    list of x coordinates of frame positions
     * @return                  true if the x coordinate is covered, false otherwise
     */
    private boolean existsIn( int x , List<Integer> items ) {
        for( int item : items ) {
            if( item == x ) {
                return true;
            }
        }
        return false;
    }
      
    /**
     * Checks whether it is needed to split a FrameElement into two
     * @param element   the FrameElement to check
     * @return          true if the FrameElement should be split in two, false otherwise
     */
    private boolean needToSplitFrame( FrameElement element, Point supportPosition ) {
        // find possible intersect point with support frame
        Point intersect = new Point( element.getStartPoint().getX(), supportPosition.getY() );
        // check whether the intersect point is on the frame element, excluding end points
        return element.includes( intersect, false);
    }  

    /**
     * Translate a rack slot position into frame point.
     * @param rackPosition      Point of the rack skit position
     * @param corner            CoirnerType to translate into
     * @return                  The corresponding corner's Frame Point
     */
    private Point translateRackPosToFramePoint( Point rackPosition, CornerType corner) {
        // switch on which border to translate to
        switch( corner ) {
            
            case TOP_LEFT :
                return new Point( rackPosition.getX()-1, rackPosition.getY());
            case TOP_RIGHT :
                return rackPosition;
            case BOTTOM_RIGHT :
                return new Point( rackPosition.getX(), rackPosition.getY()-1);
            case BOTTOM_LEFT :
                return new Point( rackPosition.getX()-1, rackPosition.getY()-1);
            default:
                return rackPosition;
        }
    }
    
    /**
     * Translate a frame point into a rack slot position.
     * @param framePoint      Point of the rack slot position
     * @param corner            CornerType to translate into (e.g TOP_LEFT)
     * @return                  The corresponding corner's Frame Point
     */
    private Point translateFramePointToRackPosition( Point framePoint, CornerType corner) {
        // TODO: handle out of bounds (eg. frame 0,1 -> rack -1,0 - rack position cannot be less than 1
        switch( corner ) {
            case TOP_LEFT :
                return new Point( framePoint.getX(), framePoint.getY()+1);
            case TOP_RIGHT :
                return new Point( framePoint.getX()+1, framePoint.getY()+1);
            case BOTTOM_RIGHT :
                return new Point( framePoint.getX()+1, framePoint.getY());
            case BOTTOM_LEFT :
            default:
                return framePoint;
        }
    }
    
    /**
     * Checks whether the given positions are of legal length. Any illegal lengths
     * will be split into smaller legal lengths.
     * @param positions     Integer List of possible X positions
     * @return              Integer List of positions resulting in legal BASE frame elements
     */
    private List<Integer> getLegalBaseFramePositions(List<Integer> positions){
        List<Integer> newPositions = new LinkedList<>();
        int prevPosX = positions.get(0);
        for(int i = 1 ; i < positions.size() ; i++) {
            int currentPosX = positions.get(i);
            int length = currentPosX-prevPosX;
            if( !isLegalFrameLength( length, Orientation.HORIZONTAL ) && canSplitBaseFrameLength(length, Orientation.HORIZONTAL) ) {
                // try splitting. if not possible to split, then do nothing.
                // - the resulting element will be flagged as illegal at insert into configuration
                List<Integer> newPosXs = splitIntoValidBasePositions(length, prevPosX);
                newPositions.addAll( newPosXs );
            }
            prevPosX = currentPosX;
        }
        positions.addAll(newPositions);
        // get rid of duplicate values
        positions = getDistinct( positions );
        Collections.sort( positions );
        return positions;
    }
    
    /**
     * Returns valid x positions for splitting a base frame length into valid smaller lengths.
     * @param length        length value to split
     * @param startPosX     x position of where the frame with this length starts
     * @return              Integer List of valid base frame positions. Empty list if no positions are valid.
     */
    private List<Integer> splitIntoValidBasePositions(int length, int startPosX){
        List<Integer> validPositions = new LinkedList<>();
        
        List<Integer> newLengths = splitFrameLength(length, configManager.getLegalFrameLengths(column,Orientation.HORIZONTAL) );
        if( !newLengths.isEmpty() ) {
            List<Integer> newPosXs = this.convertLengthsToPosX( newLengths, startPosX );
            validPositions = getValidBasePositions(newPosXs);
        }       
        return validPositions;
    }
 
    /**
     * Returns the valid values of the input list where base frame positions can be placed.
     * @param list      list of position X values to check
     * @return          the valid values of the input list
     */
    private List<Integer> getValidBasePositions(List<Integer> list){
        List<Integer> validPositions = new LinkedList<>();
        for( int value : list ) {
            // check that trace can be done to bottom of column
            if( canTraceHeight( new Point(value,0) ) ) {
                validPositions.add(value);
            }
        }
        return validPositions;
    }
    
    private boolean canSplitBaseFrameLength(int length, Orientation direction) {
        List<Integer> legalValues = configManager.getLegalFrameLengths(column, direction);
        String mountingType = configManager.getMountingType(column);
        return length > Collections.min(legalValues) && !mountingType.equalsIgnoreCase( ModelNames.FRAME_MOUNTING_TYPE_EDF);
    }
    
    /**
     * returns a list of distinct values of the input list
     * @param list
     * @return
     */
    private List<Integer> getDistinct(List<Integer> list){
        Set<Integer> distinct = new HashSet<>(list);
        return new LinkedList<>(distinct);
    }
    
    /**
     * checks whether a given frame length (in slots) is legal.
     * @param length    length to check
     * @return          whether the length is a legal length according to the model
     */
    private boolean isLegalFrameLength(int length, Orientation direction) {
        List<Integer> legalLengths = configManager.getLegalFrameLengths(column,direction);
            
        return legalLengths.contains(length);
    }
    
    
    /**
     * Splits the input length into legal lengths and returns the lengths.
     * Returns the empty list if it was not possible to create legal lengths from the input length.
     * @param length
     * @param legalLengths
     * @return
     */
    private List<Integer> splitFrameLength(int length, List<Integer> legalLengths){
        List<Integer> newLengths = new LinkedList<>();
        int middleLength = calcMiddleValue(length);

        if( length <=  0) {
            return newLengths;
        }
        
        // if the input length is already a legal length, then just return it
        if(legalLengths.contains(length) ) {
            newLengths.add( length );
            return newLengths;
        }
        
        // calculate first length
        int firstLength = getClosestLegalFrameLength(middleLength, legalLengths);
        if( firstLength == -1 ) {
            firstLength = Collections.max(legalLengths);
        }
        // calculate/split second length
        int secondLength = length - firstLength;
        if( legalLengths.contains( secondLength ) ) {
            newLengths.add(firstLength);
            newLengths.add(secondLength);
        }
        else {
            // try to split second length using legal lengths
            List<Integer> secondSplits = splitFrameLength(secondLength, legalLengths);
            if( !secondSplits.isEmpty() ) {
                newLengths.add(firstLength);
                newLengths.addAll( secondSplits );
            }
        }
        
        return newLengths;
    }
    
    /**
     * Gets the closest and larger legal length to the input value
     * @param value         value to find closes legal length for
     * @param legalLengths  list of legal lengths
     * @return  the legal length value closest and larger to the input value 
     */
    private int getClosestLegalFrameLength(int value, List<Integer> legalLengths) {
        int closest = -1;
        int prevValue = -1;
        for( int legal : legalLengths ) {
                if( prevValue < value && value <= legal ) {
                    closest = legal;
                    break;
                }
                prevValue = legal;
        }
        return closest;
    }

    /**
     * Calculates the closest larger integer value to half the input value.
     * @param value  value to base calculation on
     * @return       closest larger int value for half the input value.
     */
    private int calcMiddleValue(int value) {
        return value/2+value%2;
    }
    
    /**
     * Converts an integer list of lengths into new x positions, from the given start position
     * @param startPosX
     * @param lengths
     * @return
     */
    private List<Integer> convertLengthsToPosX( List<Integer> lengths, int startPosX ){
        List<Integer> positions = new LinkedList<>();
        int currentPosX = startPosX;
        for (int length : lengths ) {
            int slotPosX = currentPosX + length;
            positions.add( slotPosX );
            currentPosX = slotPosX;
        }
        
        return positions;
    }
    
    @Override
    public String toString() {
        return this.column.getID();
    }
   
}
