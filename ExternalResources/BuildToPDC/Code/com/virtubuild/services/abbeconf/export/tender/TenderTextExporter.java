package com.virtubuild.services.abbeconf.export.tender;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.Exporter;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.exporter.ExporterController;

public class TenderTextExporter extends Exporter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TenderTextExporter.class);

	private Configuration configuration;
    private ExporterController controller;
	
	private static final String SWITCHBOARD_TOA        = "switchboard";
    private static final String SWITCHBOARD_N185       = "switchboard_n185";
	private static final String SWITCHBOARD_RBBS       = "switchboard_rbbs";
    private static final String SWITCHBOARD_TWINLINE   = "switchboard_twinline";
	private static final String N185_EXPORTER          = "eConfigure_n185_tender_text";
	private static final String RBBS_EXPORTER          = "eConfigure_rbbs_tender_text";
	private static final String TWINLINE_EXPORTER      = "eConfigure_twinline_tender_text";
	private static final String DEFAULT_EXPORTER       = "exporter_tender";
	
    private String exporterData = "";
    
    /**
     * Initialise Exporter 
     */
    private void initConfiguration() {
        this.controller = getManager().getExporterController();
        this.configuration = getConfiguration();
    }
    
    /**
     * Prepare the data to be exported. Will run different exporters
     * based on the switchboard type. If 
     */
    @Override
	public void prepareData(final Collection<String> notUsed) {
	    initConfiguration();
	    
	    String switchboardType = getSwitchboardType();
	    switch( switchboardType ) {
	        case SWITCHBOARD_N185 :
                exporterData = runExporter(N185_EXPORTER);
	            break;
	        case SWITCHBOARD_RBBS :
                exporterData = runExporter(RBBS_EXPORTER);
	            break;
	        case SWITCHBOARD_TWINLINE:
                exporterData = runExporter(TWINLINE_EXPORTER);
	            break;
            default:
                exporterData = runExporter(DEFAULT_EXPORTER);
	    }
	}
	
	private String getSwitchboardType() {

	    BaseComponent switchboard = getSwitchboard();
        if(switchboard != null) {
            return switchboard.getTypeID();
        }
        
        LOGGER.warn( "Switchboard does not exist. Exporting default tender.");
        return "unknown";
	}
	
	/**
	 * Get current switchboard
	 * @return  BaseComponent with the current switchboard
	 */
	private BaseComponent getSwitchboard() {
	    BaseComponent switchboard = null;
	    List<BaseComponent> switchboardList = configuration.getTOAComponents(SWITCHBOARD_TOA);
	    
	    if( !switchboardList.isEmpty() ) {
	        switchboard = switchboardList.iterator().next();
	    }
	    
	    return switchboard;
	}

	/**
	 * Run the specified exporter id. Logs an error if the exporter does not exist
	 * @param exporterId
	 */
	private String runExporter( String exporterId ) {
	    
	    if( controller.existExporter(exporterId) ) {
	       return controller.runExporter(exporterId);
	    }

	    LOGGER.error("Exporter does not exist: {}", exporterId );
	    return "";
	}

	/** 
	 * return exporter data when asked
	 */
	@Override
	public String getDataString() {
		return exporterData;
	}
}
