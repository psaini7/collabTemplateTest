/******************************************************************
 * Kit                                                            *
 * Represents a component inserted into a rack                    *
 * The size of a kit is measured in the number of slots it covers *
 * in width and height.                                           *
 * The point of the kit is x,y coordinate of the top left corner  *
 * of the kit using rack slot coordinates (top-left bottom-right)  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.frames;

import com.virtubuild.core.api.Component;
import com.virtubuild.custom.abbeconf.general.Direction;
import com.virtubuild.custom.abbeconf.general.Orientation;
import com.virtubuild.custom.abbeconf.general.Point;

public class Kit implements Comparable<Kit> {

    private ConfigurationManager configManager;

    private Corner[] corners = new Corner[4]; // top left, top right, bottom right, bottom left
    private Column parent;
    private Point insertPosition;       // insert position of the kit in rack (1 indexed)
    private int width;                  // width of kit in slot units (e.g 1, 2 etc)
    private int height;                 // height of kit in slot units (e.g 1, 2 etc)
    private String id;

    /**
     * Constructor
     * @param parent         Column element which this kit belongs to
     * @param kit            Component to create Kit for
     * @param configManager  ConfigurationManager to insert
     */
    public Kit(Column parent, Component kit, ConfigurationManager configManager) {
        this.configManager = configManager;
        this.parent = parent;
        setDimensions( kit );
        setPosition( kit );
        setCorners();
        setId(kit);
    }

    /**
     * Gets the position of the kit (top left corner of kit)
     * @return  FramePoint with position of kit
     */
    public Point getPosition() {
        return insertPosition;
    }

    /**
     * Gets the width of the kit
     * @return  width of the kit in Integer
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the kit
     * @return  height of the kit in Integer
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the frame this Kit is placed in
     * @return  Frame
     */
    public Frame getFrame() {
        return parent.getFrame();
    }
    
    /**
     * Gets the model id of the kit
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the x coordinate of the right border of the kit
     */
    public int getRightBorder() {
      return corners[2].getPoint().getX();  // bottom_right corner X coordinate
    }

    /**
     * Covers a Corner with a FrameElement. If no FrameElements exist which covers the corner, 
     * then a new FrameElement will be created to cover the corner.
     */
    public void attach() {

        for ( Corner corner : corners ) {
            if( !corner.isAttached() ) {
                Point startPoint = findFrameStartPoint(corner);
                parent.createFrameElement( FrameType.INTERNAL, startPoint, Orientation.VERTICAL, height, calcAttachmentType( corner.getType() ) );
            }
        }
    }
    
    private Point findFrameStartPoint(Corner corner) {
        CornerType type = corner.getType();
        switch(type) {
            case TOP_LEFT:
//                return corners[3].getPoint(); // bottom left
                return corner.getPoint();
            case TOP_RIGHT:
                return corner.getPoint();
//                return corners[2].getPoint(); // bottom right
            case BOTTOM_RIGHT:
                return corners[1].getPoint(); // top right
//                return corner.getPoint();
            case BOTTOM_LEFT:
                return corners[0].getPoint(); // top left
//                return corner.getPoint();
            default:
                return corner.getPoint();
        }
    }
        
    /**
     * Custom comparator for Kits - compares first on width, then height - descending
     */
    @Override
    public int compareTo( Kit kit ) {
        int widthCompare = Integer.compare(kit.width, this.width);
        if(widthCompare != 0) {
            return widthCompare;
        }
        int heightCompare = Integer.compare(kit.height, this.height);
        return heightCompare;
    }
    

    /** 
     * Overrides object toString() method
     */
    public String toString() {
        return this.id + "[" + corners[0] + ";" +corners[1] + ";" + corners[2] + ";" + corners[3]+"]" ;
    }

/* *** Private methods *** */
    
    /**
     * Calculates where side of a frame element kits should be attached compared to the corner type. TOP, BOTTOM, LEFT, RIGHT, BOTH
     * @param type  The type of corner to find the attachment type of frame element for
     * @return      The attachment type for the given corner type
     */
    private FrameElement.AttachmentType calcAttachmentType( CornerType type) {
        switch( type ) {
            case BOTTOM_LEFT: 
            case TOP_LEFT:
                return FrameElement.AttachmentType.RIGHT;
            case BOTTOM_RIGHT: 
            case TOP_RIGHT:
                return FrameElement.AttachmentType.LEFT;
            default:
                return FrameElement.AttachmentType.BOTH;
        }
    }
    
    /**
     * sets the width and height of the Kit based on the kit Component.
     * @param kit  kit Component to get the position from
     */
    private void setDimensions( Component kit ) {
        width = configManager.getKitWidth( kit );
        height = configManager.getKitHeight( kit );
    }

    /**
     * Sets the position of the Kit based on the kit Component
     * @param kit  kit Component to get the position from
     */
    private void setPosition( Component kit ) {
        insertPosition = configManager.getKitPosition( kit );
    }

    /**
     * Sets the corners of this kit
     */
    private void setCorners() {
        Point insertCornerPoint = calcInsertCorner();
        corners[0] = calcTopLeftCorner(insertCornerPoint);
        corners[1] = calcTopRightCorner(insertCornerPoint);
        corners[2] = calcBottomRightCorner(insertCornerPoint);
        corners[3] = calcBottomLeftCorner(insertCornerPoint);
    }

    private void setId( Component kit ) {
        id = configManager.getKitId( kit );
    }
    
    /**
     * Calculates the top left corner of this kit based on the kit's insert position
     * @return  the calculated Corner
     */
    private Corner calcTopLeftCorner(Point insertCorner) {
        return new Corner( CornerType.TOP_LEFT, insertCorner, this );
//        Point cornerPoint = insertCorner.calcNewPoint(Direction.UP, height);
//        return new Corner( CornerType.TOP_LEFT , cornerPoint, this);
    }
    
    /**
     * Calculates the top right corner of this kit based on the kit's insert position
     * @return  the calculated Corner
     */
    private Corner calcTopRightCorner(Point insertCorner) {
        Point cornerPoint = insertCorner.calcNewPoint(Direction.RIGHT, width);
//        Point cornerPoint = insertCorner.calcNewPoint(width, height);
        return new Corner( CornerType.TOP_RIGHT, cornerPoint, this );
    }
 
    /**
     * Calculates the top left corner of this kit based on the kit's insert position
     * @return  the calculated Corner
     */
    private Corner calcBottomRightCorner( Point insertCorner ) {
//        Point cornerPoint = insertCorner.calcNewPoint( Direction.RIGHT,width );
        Point cornerPoint = insertCorner.calcNewPoint(width, height);
        return new Corner( CornerType.BOTTOM_RIGHT, cornerPoint, this );
    }

    /**
     * Calculates the bottom left corner of this kit based on the kit's insert position. 
     * @return  the calculated Corner
     */
    private Corner calcBottomLeftCorner( Point insertCorner ) {
        Point cornerPoint = insertCorner.calcNewPoint(Direction.UP, height);
        return new Corner( CornerType.BOTTOM_LEFT, cornerPoint, this);
//        return new Corner( CornerType.BOTTOM_LEFT, insertCorner, this );
    }

    /**
     * Calculates the insert corner from the insert slot position
     * @return  Insert corner point (Bottom left corner)
     */
    private Point calcInsertCorner() {
        return new Point(insertPosition.getX()-1,insertPosition.getY()-1);
    }
    
}
