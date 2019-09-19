package com.virtubuild.services.abbeconf.frames;

import java.util.Comparator;

import com.virtubuild.custom.abbeconf.general.Orientation;
import com.virtubuild.custom.abbeconf.general.Point;

public class SupportFrame extends FrameElement implements Comparable<FrameElement> {
    
    private int priority;

    SupportFrame(FrameType type, Point startPoint, Orientation direction, int length, AttachmentType attachment){
        super(type, startPoint, direction, length, attachment);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
 
    /**
     * Sorts supportFrames on their priority
     * @param supportFrame
     * @return
     */
    public int compareTo( SupportFrame supportFrame ) {
//        return Integer.compare( this.priority, supportFrame.getPriority() );
        return super.compareTo( supportFrame );
    }
    
    /**
     * Compare on length and y position
     */
    public static Comparator<SupportFrame> PriorityComparator = new Comparator<SupportFrame>() {
        public int compare(SupportFrame elem1, SupportFrame elem2) {
            return Integer.compare(elem1.getPriority(), elem2.getPriority() );
        }
    };    
}
