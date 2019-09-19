/******************************************************************
 * ModelNames                                                     *
 * List of components, variables, values etc used in the model.   *
 ******************************************************************/
package com.virtubuild.services.abbeconf.frames;

public class ModelNames {

    public static final String RACK_AVAIL_COLUMNS             = "availableColumns";
    public static final String RACK_AVAIL_ROWS                = "availableRows";
    public static final String RACK_COLUMN_WIDTH              = "width.column1";
    public static final String RACK_ROW_HEIGHT                = "height.row1";

    public static final String CABINET_ENCLOSURE_TYPE         = "cabinet_enclosure_type_toa"; // variable
    public static final String CABINET_ENCLOSURE_TYPE_FLOOR   = "Floorstanding";              // value
    public static final String CABINET_ENCLOSURE_TYPE_WALL    = "Wallmounting";               // value
    public static final String CABINET_ENCLOSURE_TYPE_DEFAULT = "Floorstanding";              // value

    public static final String KIT_PREFIX                     = "kit_";
    public static final String KIT_WIDTH                      = "width";
    public static final String KIT_HEIGHT                     = "height";
    public static final String KIT_POS_X                      = "kit_posx";
    public static final String KIT_POS_Y                      = "kit_posy";

    public static final String FRAME_TOA                      = "kit_frame";
    public static final String FRAME_COMP                     = "kit_frames";
    public static final String FRAME_BASE_PREFIX              = "frame";
    public static final String FRAME_PROFILE_PREFIX           = "profile";
    public static final String FRAME_SUPPORT_PREFIX           = "support";
    public static final String FRAME_TYPE_SUFFIX              = "_type";
    public static final String FRAME_HEIGHT_SUFFIX            = "_height";
    public static final String FRAME_WIDTH_SUFFIX             = "_width";
    public static final String FRAME_LENGTH_SUFFIX            = "_length";
    public static final String FRAME_POS_X_SUFFIX             = "_pos_x";
    public static final String FRAME_POS_Y_SUFFIX             = "_pos_y";
    public static final String FRAME_ORIENTATION_SUFFIX       = "_orientation";
    public static final String FRAME_ATTACHMENT_SUFFIX        = "_attachment";
    public static final String FRAME_IS_LEGAL_SUFFIX          = "_legal";
    public static final String FRAME_ALL_LEGAL                = "frames_legal";
    public static final String FRAME_LEGAL_LENGTH_HOR_VAR     = "frames_legal_length_hor";
    public static final String FRAME_LEGAL_LENGTH_VER_VAR     = "frames_legal_length_ver";

    public static final String FRAME_MOUNTING_TYPE            = "cabinet_mounting_frame_toa"; // variable
    public static final String FRAME_MOUNTING_TYPE_DEFAULT    = "WR";                         // value
    public static final String FRAME_MOUNTING_TYPE_WR         = "WR";                         // value
    public static final String FRAME_MOUNTING_TYPE_EDF        = "EDF";                        // value

    public static final int    MAX_COLUMN_HEIGHT_EDF_FLOOR    = 1350;                         // max height of floor
                                                                                              // standing cabinet with
                                                                                              // EDF before support
                                                                                              // frame is needed
    public static final int    DISTANCE_NO_SUPPORT_TOP_BOTTOM = 450;                          // support frame should be
                                                                                              // at least 450 from top
                                                                                              // or bottom of frame
    public static final String SWITCHBOARD_TWINLINE           = "switchboard_twinline";
    public static final String KIT_FRAME_TWINLINE             = "kit_frames_twinlineN";
}
