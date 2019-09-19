package com.virtubuild.services.abbeconf.edfprofiles;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.core.state.StateVar;

public class EDFProfileCompleter extends CompleterSkeleton {

	private Configuration configuration;
	public static final String PROFILE_PREFIX = "profile";
	public static final String PROFILE_LENGTH_SUFFIX = "_length";
	public static final String PROFILE_ORIENTATION = "_orientation";
	public static final String PROFILE_ATTACHMENT = "_attachment";
	private static final String CABINET_MOUNTING_FRAME_TOA = "cabinet_mounting_frame_toa";
	private static final String MOUNTED = "mounted (Mounted)";
	private static final String FLATPACK = "flatpack (Flatpack)";
	private static final String SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA = "switchboard_delivery_mounting_type_toa";
	private static final String HORIZONTAL = "horizontal";
	private static final String VERTICAL = "vertical";
	private static final String EDF = "EDF";
	private static final String BOTH = "BOTH";
	private static final String LEFT = "LEFT";
	private static final String RIGHT = "RIGHT";
	private static final String TOP = "TOP";
	private static final String BOTTOM = "BOTTOM";
	
	//ConfigAPI cfgAPI = new ConfigAPI(GUIController.getInstance().getManager());

	Component kitFrames;
	
	public boolean doComplete() {

		configuration = getConfiguration();
		

        List<Component> listComponent =  configuration.getAllComponents();
        
        for (Component component : listComponent){
            
            if (component.toString().contains("kit_frames_twinlineN")){
                
                kitFrames = component;
                
            }
            
        }

		List<BaseComponent> compList = configuration.getTOAComponents("kit_frames.kit_frames_twinlineN");
		for (BaseComponent comp : compList) {

			int EDF_prof_horz_mounted_oneside_500 = 0;
			int EDF_prof_horz_mounted_bothsides_500 = 0;
			int EDF_prof_horz_mounted_oneside_750 = 0;
			int EDF_prof_horz_mounted_bothsides_750 = 0;
			int EDF_prof_horz_flatpack_oneside_500 = 0;
			int EDF_prof_horz_flatpack_bothsides_500 = 0;
			int EDF_prof_horz_flatpack_oneside_750 = 0;
			int EDF_prof_horz_flatpack_bothsides_750 = 0;

			int EDF_prof_vert_mounted_150 = 0;
			int EDF_prof_vert_mounted_300 = 0;
			int EDF_prof_vert_mounted_450 = 0;
			int EDF_prof_vert_mounted_600 = 0;
			int EDF_prof_vert_mounted_750 = 0;
			int EDF_prof_vert_mounted_900 = 0;
			int EDF_prof_vert_mounted_1050 = 0;
			int EDF_prof_vert_mounted_1200 = 0;
			int EDF_prof_vert_mounted_1350 = 0;
			// int EDF_prof_vert_mounted_1500=0;
			// int EDF_prof_vert_mounted_1650=0;
			int EDF_prof_vert_mounted_1800 = 0;
			// int EDF_prof_vert_mounted_1950=0;
			int EDF_prof_vert_mounted_2100 = 0;
			int EDF_prof_vert_flatpack_150 = 0;
			int EDF_prof_vert_flatpack_300 = 0;
			int EDF_prof_vert_flatpack_450 = 0;
			int EDF_prof_vert_flatpack_600 = 0;
			int EDF_prof_vert_flatpack_750 = 0;
			int EDF_prof_vert_flatpack_900 = 0;
			int EDF_prof_vert_flatpack_1050 = 0;
			int EDF_prof_vert_flatpack_1200 = 0;
			int EDF_prof_vert_flatpack_1350 = 0;
			// int EDF_prof_vert_flatpack_1500=0;
			// int EDF_prof_vert_flatpack_1650=0;
			int EDF_prof_vert_flatpack_1800 = 0;
			// int EDF_prof_vert_flatpack_1950=0;
			int EDF_prof_vert_flatpack_2100 = 0;

			//List<String> varList = (List<String>) cfgAPI.getAllVars(comp.toString());
			
			List<String> varList = new ArrayList<>();
			
			if ( kitFrames != null ){
                
                Object[] components =  kitFrames.getStateVector().getAllVariables().toArray();

                for (Object datum : components){
                    
                    varList.add(((StateVar)datum).getID().toString());
                    
                }
                
            }

			List<String> profilesList = getProfilesList(varList);
			List<String> profilesOrientationList = getProfilesOrientationList(varList);
			List<String> profilesAttachmentList = getProfilesAttachmentList(varList);

			for (String v : profilesList) {

				String p = orientationVariable(v, profilesOrientationList);
				String a = attachmentVariable(v, profilesAttachmentList);

				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("500")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(HORIZONTAL)
						&& (comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(LEFT)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(RIGHT)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(TOP)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(BOTTOM))
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_horz_mounted_oneside_500++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("500")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(HORIZONTAL)
						&& comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(BOTH)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_horz_mounted_bothsides_500++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("750")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(HORIZONTAL)
						&& (comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(LEFT)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(RIGHT)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(TOP)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(BOTTOM))
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_horz_mounted_oneside_750++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("750")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(HORIZONTAL)
						&& comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(BOTH)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_horz_mounted_bothsides_750++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("500")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(HORIZONTAL)
						&& (comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(LEFT)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(RIGHT)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(TOP)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(BOTTOM))
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_horz_flatpack_oneside_500++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("500")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(HORIZONTAL)
						&& comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(BOTH)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_horz_flatpack_bothsides_500++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("750")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(HORIZONTAL)
						&& (comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(LEFT)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(RIGHT)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(TOP)
								|| comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(BOTTOM))
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_horz_flatpack_oneside_750++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("750")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(HORIZONTAL)
						&& comp.getStateVector().getVar(a).getValue().toString().equalsIgnoreCase(BOTH)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_horz_flatpack_bothsides_750++;
				}
				// For Vertical
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("150")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_150++;

				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("300")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_300++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("450")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_450++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("600")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_600++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("750")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_750++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("900")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_900++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("1050")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_1050++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("1200")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_1200++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("1350")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_1350++;
				}
				/*
				 * if(comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase(
				 * "1500") &&
				 * comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(
				 * VERTICAL) &&
				 * comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).
				 * getValue().toString().equalsIgnoreCase(MOUNTED) &&
				 * comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString(
				 * ).equalsIgnoreCase(EDF) ){ EDF_prof_vert_mounted_1500++; }
				 * if(comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase(
				 * "1650") &&
				 * comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(
				 * VERTICAL) &&
				 * comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).
				 * getValue().toString().equalsIgnoreCase(MOUNTED) &&
				 * comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString(
				 * ).equalsIgnoreCase(EDF) ){ EDF_prof_vert_mounted_1650++; }
				 */
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("1800")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_1800++;
				}
				/*
				 * if(comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase(
				 * "1950") &&
				 * comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(
				 * VERTICAL) &&
				 * comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).
				 * getValue().toString().equalsIgnoreCase(MOUNTED) &&
				 * comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString(
				 * ).equalsIgnoreCase(EDF) ){ EDF_prof_vert_mounted_1950++; }
				 */
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("2100")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(MOUNTED)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_mounted_2100++;
				}

				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("150")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_150++;

				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("300")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_300++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("450")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_450++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("600")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_600++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("750")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_750++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("900")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_900++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("1050")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_1050++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("1200")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_1200++;
				}
				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("1350")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_1350++;
				}

				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("1800")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_1800++;
				}

				if (comp.getStateVector().getVar(v).getValue().toString().equalsIgnoreCase("2100")
						&& comp.getStateVector().getVar(p).getValue().toString().equalsIgnoreCase(VERTICAL)
						&& comp.getStateVector().getVar(SWITCHBOARD_DELIVERY_MOUNTING_TYPE_TOA).getValue().toString()
								.equalsIgnoreCase(FLATPACK)
						&& comp.getStateVector().getVar(CABINET_MOUNTING_FRAME_TOA).getValue().toString()
								.equalsIgnoreCase(EDF)) {
					EDF_prof_vert_flatpack_2100++;
				}

			}

			comp.getStateVector().getVar("EDF_prof_horz_mounted_oneside_500")
					.setValue(String.valueOf(EDF_prof_horz_mounted_oneside_500));
			comp.getStateVector().getVar("EDF_prof_horz_mounted_bothsides_500")
					.setValue(String.valueOf(EDF_prof_horz_mounted_bothsides_500));
			comp.getStateVector().getVar("EDF_prof_horz_mounted_oneside_750")
					.setValue(String.valueOf(EDF_prof_horz_mounted_oneside_750));
			comp.getStateVector().getVar("EDF_prof_horz_mounted_bothsides_750")
					.setValue(String.valueOf(EDF_prof_horz_mounted_bothsides_750));
			comp.getStateVector().getVar("EDF_prof_horz_flatpack_oneside_500")
					.setValue(String.valueOf(EDF_prof_horz_flatpack_oneside_500));
			comp.getStateVector().getVar("EDF_prof_horz_flatpack_bothsides_500")
					.setValue(String.valueOf(EDF_prof_horz_flatpack_bothsides_500));
			comp.getStateVector().getVar("EDF_prof_horz_flatpack_oneside_750")
					.setValue(String.valueOf(EDF_prof_horz_flatpack_oneside_750));
			comp.getStateVector().getVar("EDF_prof_horz_flatpack_bothsides_750")
					.setValue(String.valueOf(EDF_prof_horz_flatpack_bothsides_750));

			comp.getStateVector().getVar("EDF_prof_vert_mounted_150")
					.setValue(String.valueOf(EDF_prof_vert_mounted_150));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_300")
					.setValue(String.valueOf(EDF_prof_vert_mounted_300));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_450")
					.setValue(String.valueOf(EDF_prof_vert_mounted_450));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_600")
					.setValue(String.valueOf(EDF_prof_vert_mounted_600));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_750")
					.setValue(String.valueOf(EDF_prof_vert_mounted_750));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_900")
					.setValue(String.valueOf(EDF_prof_vert_mounted_900));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_1050")
					.setValue(String.valueOf(EDF_prof_vert_mounted_1050));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_1200")
					.setValue(String.valueOf(EDF_prof_vert_mounted_1200));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_1350")
					.setValue(String.valueOf(EDF_prof_vert_mounted_1350));

			comp.getStateVector().getVar("EDF_prof_vert_mounted_1800")
					.setValue(String.valueOf(EDF_prof_vert_mounted_1800));
			// comp.getStateVector().getVar("EDF_prof_vert_mounted_1950").setValue(String.valueOf(EDF_prof_vert_mounted_1950));
			comp.getStateVector().getVar("EDF_prof_vert_mounted_2100")
					.setValue(String.valueOf(EDF_prof_vert_mounted_2100));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_150")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_150));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_300")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_300));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_450")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_450));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_600")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_600));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_750")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_750));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_900")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_900));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_1050")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_1050));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_1200")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_1200));
			comp.getStateVector().getVar("EDF_prof_vert_flatpack_1350")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_1350));

			comp.getStateVector().getVar("EDF_prof_vert_flatpack_1800")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_1800));

			comp.getStateVector().getVar("EDF_prof_vert_flatpack_2100")
					.setValue(String.valueOf(EDF_prof_vert_flatpack_2100));

		}

		return true;
	}

	public List<String> getProfilesList(List<String> varList) {

		List<String> profilesList = new ArrayList<String>();
		String pattern = "^(" + PROFILE_PREFIX +

				")\\d*(" + PROFILE_LENGTH_SUFFIX + ")$";

		for (String var : varList) {
			if (Pattern.matches(pattern, var)) {
				profilesList.add(var);
			}
		}
		return profilesList;

	}

	public List<String> getProfilesOrientationList(List<String> varList) {

		List<String> profilesOrientationList = new ArrayList<String>();
		String pattern = "^(" + PROFILE_PREFIX +

				")\\d*(" + PROFILE_ORIENTATION + ")$";

		for (String var : varList) {
			if (Pattern.matches(pattern, var)) {
				profilesOrientationList.add(var);
			}
		}
		return profilesOrientationList;

	}

	public List<String> getProfilesAttachmentList(List<String> varList) {

		List<String> profilesAttachmentList = new ArrayList<String>();
		String pattern = "^(" + PROFILE_PREFIX +

				")\\d*(" + PROFILE_ATTACHMENT + ")$";

		for (String var : varList) {
			if (Pattern.matches(pattern, var)) {
				profilesAttachmentList.add(var);
			}
		}
		return profilesAttachmentList;

	}

	public String orientationVariable(String var, List<String> profilesOrientationList) {
		String str = "";
		String newVar = var.replace(PROFILE_LENGTH_SUFFIX, " ").trim();
		String c = newVar + PROFILE_ORIENTATION;
		for (String s : profilesOrientationList) {
			if (s.matches(c)) {
				str = s;
			}
		}

		return str;

	}

	public String attachmentVariable(String var, List<String> profilesAttachmentList) {
		String str1 = "";
		String newVar1 = var.replace(PROFILE_LENGTH_SUFFIX, " ").trim();
		String c1 = newVar1 + PROFILE_ATTACHMENT;
		for (String s1 : profilesAttachmentList) {
			if (s1.matches(c1)) {
				str1 = s1;
			}
		}

		return str1;

	}

	@Override
	public boolean doCompleteSupported() {
		return true;
	}

	@Override
	protected void init() {
		super.init();
	}

}
