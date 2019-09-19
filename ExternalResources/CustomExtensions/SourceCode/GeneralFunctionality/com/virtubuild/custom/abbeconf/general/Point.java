package com.virtubuild.custom.abbeconf.general;

/**
 * Point
 * Simple (x,y) coordinate and operations.
 * @author cafot - Configit A/S
 *
 */
public class Point implements Comparable<Point>{
    
    private int posX;
    private int posY;
    
    /**
     * Creates a new Point object given the x and y input coordinates
     * @param posX  x coordinate of Point in integer
     * @param posY  y coordinate of Point in integer
     */
    public Point(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }

    public int getX() {
        return posX;
    }
    public int getY() {
        return posY;
    }

    /**
     * Calculates a new Point from this Point given a direction and a length
     * @param direction  the direction in which the new Point should be calculated
     * @param length     how far the new Point should be from this Point
     * @return           the newly calculated Point
     **/
    public Point calcNewPoint( Direction direction, int length ) {
        int newPosX = calculatePosX( direction, length );
        int newPosY = calculatePosY( direction, length );
        
        return new Point( newPosX, newPosY );
    }

    /**
     * Calculates a new (diagonal) Point from this Point given the horizontal and vertical heights
     * @param lengthH  horizontal length
     * @param lengthV  vertical length
     * @return         the newly calculated Point
     **/
    public Point calcNewPoint( int lengthH, int lengthV ) {
        int newPosX = calculatePosX( Direction.RIGHT, lengthH );
        int newPosY = calculatePosY( Direction.UP, lengthV );
        
        return new Point( newPosX, newPosY );
    } 
    
    
    /**
    * Compares the X and Y position of the input Point to the Point. X point is compared first.
    * @param point  the Point to compare to
    * @return       whether the Points are equal (0), larger than (1) or smaller than this point (-1s)
    */
   public int compareTo(Point point) {
       int x = point.getX();
       int compareX = Integer.compare(posX, x);
       if(compareX != 0 ) {
           return compareX;
       }
       // compare on Y
       int y = point.getY();
       return Integer.compare(posY, y);
   }
   
   /**
    * Overrides object toString() method
    */
   public String toString() {
       return posX + "," + posY;
   }

   /**
    * Calculates the distance between two points
    * @param point1  start point
    * @param point2  end point
    * @return        the calculated distance between the two points in integer
    */
   public static int calcDistance( Point point1, Point point2 ) {
       return (int) Math.hypot( point2.getX() - point1.getX() , point2.getY() - point1.getY() );
   }

 /* *** Private methods ** */
   
   /**
     * Calculates new X position for the given direction and length. 
     * The return value can be negative
     * @param direction  the direction in which the new Point should be calculated
     * @param length     how far the new Point should be from this Point
     * @return           the newly calculated x position
     */
    private int calculatePosX( Direction direction, int length ) {

        switch( direction ) {
            case RIGHT:
                return posX + length;
            case LEFT:
                return posX - length;
            default:
                return posX;
        }
        
    }
 
    
    /**
     * Calculates new Y position for the given direction and length
     * The return value can be negative
     * @param direction  the direction in which the new Point should be calculated
     * @param length     how far the new Point should be from this Point
     * @return           the newly calculated y position
     */
    private int calculatePosY( Direction direction, int length ) {
        switch ( direction ) {
            case DOWN:
                return posY - length;
            case UP:
                return posY + length;
            default:
                return posY;
        }
    }
    
}
