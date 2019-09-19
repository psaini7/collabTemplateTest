package com.virtubuild.custom.abbeconf.general;

public enum ColumnType {

    UNKNOWN("Unknown"), 
    BREAKER("breaker"),
    BREAKER_RBBS("breaker_rbbs"),
    CABLECOMPARTMENT("cable_compartment"),
    CABINET_TWINLINE("cabinet_twinline"), 
    CENTRALEARTHINGPOINT("column_central_earthing_point"),
    COMBILINE_N185("column_combiline_n185"),
    COMBILINE_RBBS("column_combiline_rbbs"),
    COMBILINE_TBBS("column_combiline_tbbs"),
    CORNER_N185("column_corner_n185"), 
    CORNER_TBBS("column_corner_tbbs"), 
    COUPLER_N185("column_coupler_n185"), 
    COUPLER_RBBS("column_coupler_rbbs"),
    COUPLER_TBBS("column_coupler_tbbs"),
    EMPTY("column_empty"), 
    EMPTY_N185("column_empty_n185"), 
    EMPTY_TBBS("column_empty_tbbs"),
    EXT_CABLE_COMPARTMENT_RBBS("column_ext_cable_compartment_rbbs"), 
    EXT_CABLE_COMPARTMENT_TBBS_LATERAL("column_ext_lateral_cable_compartment_tbbs"), 
    EXT_CABLE_COMPARTMENT_TBBS_REAR("column_ext_rear_cable_compartment_tbbs"), 
    FUSESWITCHDISCONNECTOR_N185("column_fuse_switch_disconnector_n185"),
    INCOMING_N185("column_incoming_n185"),
    INCOMING_RBBS("column_incoming_rbbs"),
    INCOMING_TBBS("column_incoming_tbbs"),
    INLINE_RBBS("column_inline2_rbbs"),
    INLINE_TBBS("column_inline_tbbs"),
    OFFSET("column_offset"),
    OFFSET_TBBS("column_offset_tbbs"),
    OUTGOING_N185("column_outgoing_n185"),
    OUTGOING_RBBS("column_outgoing_rbbs"),
    OUTGOING_TBBS("column_outgoing_tbbs"),
    SWITCHDISCONNECTORFUSE_N185("column_switch_disconnector_fuse_n185"),
    XLINE_HORIZONTAL_RBBS("column_xline_hor_rbbs"),
    XLINE_VERTICAL_RBBS("column_xline_vert_rbbs")
    ;

    private final String value;

    private ColumnType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ColumnType getColumnType(final String value) {
        for (ColumnType type : ColumnType.values()) {
            if (!type.equals(UNKNOWN) && type.toString().equals(value)) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
