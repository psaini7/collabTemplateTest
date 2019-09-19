/******************************************************************
 * AutoCompleterSwitchboard                                       *
 * It has the methods to calculate the kits position in the		  * 
 * rack/kit frames and sets the max possible 					  *
 * values in the empty slots for all the columns from switchboard *
 * level.                                      	  				  *                                         
 ******************************************************************/
package com.virtubuild.services.abbeconf.autocompleterswitchboard;

import java.util.ArrayList;
import java.util.List;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;
import com.virtubuild.core.state.StateVar;

public class SwitchboardAutoCompleter {
	private List<Component> component;
	Configuration configurator;
	private int a = 1;
	private int b = 1;
	private int c = 1;
	private int d = 1;
	private int e = 1;
	private int f = 1;
	private int g = 1;

	private static final String DEVICE_COMPENSATOR_100 = "device_compensator_100";
	private static final String DEVICE_COMPENSATOR_50 = "device_compensator_50";
	private static final String KIT_TOUCHGUARD_COMBILINEN = "kit_touchguard_combilineN";

	/**
	 * Constructor
	 * 
	 * @param cmp          List of all the available Components
	 * @param configurator manager object with functionality for accessing the model
	 *                     configuration
	 */
	public SwitchboardAutoCompleter(List<Component> cmp, Configuration configurator) {
		this.component = cmp;
		this.configurator = configurator;
	}

	/**
	 * Calculate and insert kits on the current components
	 */
	public void loadKits() {
		for (Component cmp : component) {
			if (cmp.toString().matches("kit_xline_vert_rbbs\\d+")) {
				getKitPositionInRackXLV(cmp);
			} else if (cmp.toString().matches("kit_inline_rbbs\\d+")) {
				getKitPositionInRackInline(cmp);
			} else if (cmp.toString().matches("column_xline_hor_rbbs\\d+")) {
				getKitPositionInRackXLH(cmp);
			} else if (cmp.toString().matches("kit_frames_n185_rbbs\\d+")) {
				getKitPositionInRackCombiline(cmp);
			} else if (cmp.toString().matches("kit_switchdisconnector_n185\\d+")) {
				getKitPositionInRackSwitchFuseDisconnector(cmp);
			} else if (cmp.toString().matches("kit_fuseswitchdisconnector_n185\\d+")) {
				getKitPositionInRackFuseSwitchDisconnector(cmp);
			} else if (cmp.toString().matches("kit_frames_twinlineN\\d+")) {
				getKitPositionInRackTwinline(cmp);
			}
		}
	}

	/**
	 * Gets the kit position on the rack/Kit_frames
	 * 
	 * @param cmp
	 */
	private void getKitPositionInRackCombiline(Component cmp) {
		for (int i = 1; i < 6; i++) {
			for (int j = 1; j < 15; j++) {
				insertKitInRackCombiline(cmp, i, j);
			}
		}
		a = 1;
	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackCombiline(Component cmp, int i, int j) {
		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + a;
		a++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!
		if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
			if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
					&& !controller.isOccupied(Position.of(i, j))) {
				// Add a component to rack controller
				ComponentState componentKitTouchGuard = (ComponentState) configurator
						.createComponent(KIT_TOUCHGUARD_COMBILINEN);
				componentKitTouchGuard.finishState();
				controller.insert(Position.of(i, j), componentKitTouchGuard, false);
				assignHeightWidthValues(componentKitTouchGuard);

			}
		}
	}

	/**
	 * Assign maximum legal height and width values of the current kits
	 */
	private void assignHeightWidthValues(Component kitComponent) {
		StateVar height;
		StateVar width;
		height = kitComponent.getStateVector().getVar("height");
		width = kitComponent.getStateVector().getVar("width");

		List<StateValue> cmpHeight = height.getDomain();
		List<StateValue> cmpWidth = width.getDomain();

		List<StateValue> legatListHeight = new ArrayList<>();
		List<StateValue> legalListWidth = new ArrayList<>();

		for (int i = 0; i < cmpHeight.size(); i++) {
			if (height.isLegalValue(cmpHeight.get(i))) {
				legatListHeight.add(cmpHeight.get(i));
			}
		}

		for (int i = 0; i < cmpWidth.size(); i++) {
			if (width.isLegalValue(cmpWidth.get(i))) {
				legalListWidth.add(cmpWidth.get(i));
			}
		}

		int maxHeight = 0;
		for (StateValue st : legatListHeight) {
			int i = Integer.parseInt(st.toString());
			if (i > maxHeight) {
				maxHeight = i;
			}
		}

		int maxWidth = 0;
		for (StateValue st : legalListWidth) {
			int i = Integer.parseInt(st.toString());
			if (i > maxWidth) {
				maxWidth = i;
			}
		}

		height.setValue(String.valueOf(maxHeight));
		width.setValue(String.valueOf(maxWidth));
	}

	// Twinline
	/**
	 * Gets the kit position on the twinline rack/Kit_frames
	 */
	private void getKitPositionInRackTwinline(Component cmp) {

		for (int i = 1; i < 7; i++) {
			for (int j = 1; j < 13; j++) {
				insertKitInRackTwinline(cmp, i, j);
			}
		}
		b = 1;
	}

	/**
	 * Insert the kit on the twinline rack/kit_frames current position/role i.e
	 * element.x
	 */
	private void insertKitInRackTwinline(Component cmp, int i, int j) {
		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + b;
		b++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!
		if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
			if (cmp.getStateVector().getVar("frames_legal").getValue().getName().equals("TRUE")) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
						&& !controller.isOccupied(Position.of(i, j))) {
					// Add a component to rack controller
					ComponentState componentKitTouchGuard = (ComponentState) configurator
							.createComponent(KIT_TOUCHGUARD_COMBILINEN);
					componentKitTouchGuard.finishState();
					controller.insert(Position.of(i, j), componentKitTouchGuard, false);
					assignHeightWidthValues(componentKitTouchGuard);
				}
			}
		}
	}

	// SwitchFuseDisconnector
	/**
	 * Gets the kit position on the rack/Kit_frames
	 */
	private void getKitPositionInRackSwitchFuseDisconnector(Component cmp) {
		for (int i = 1; i < 14; i++) {
			insertKitInRackSwitchFuseDisconnector(cmp, i);
		}
		c = 1;
	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackSwitchFuseDisconnector(Component cmp, int i) {
		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());

		if (null != comp) {

			// Get rack controller
			RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

			String str = "element." + c;
			c++;

			// add the new component to the specified position in the rack controller -
			// check for null on rack first!
			/*if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 4) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					 Create a component for rack controller 
					ComponentState componentdevicecompensator100 = (ComponentState) configurator
							.createComponent(DEVICE_COMPENSATOR_100);
					componentdevicecompensator100.finishState();
					controller.insert(Position.of(i, 1), componentdevicecompensator100, false);
				}
			}*/

			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 3) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
						&& !controller.isOccupied(Position.of(i, 1))) {
					/* Create a component for rack controller */
					ComponentState componentdevicecompensator50 = (ComponentState) configurator
							.createComponent(DEVICE_COMPENSATOR_50);
					componentdevicecompensator50.finishState();
					controller.insert(Position.of(i, 1), componentdevicecompensator50, false);
				}
			}

		}

	}

	// FuseSwitchDisconnector
	/**
	 * Gets the kit position on the rack/Kit_frames
	 */
	private void getKitPositionInRackFuseSwitchDisconnector(Component cmp) {
		for (int i = 1; i < 15; i++) {
			insertKitInRackFuseSwitchDisconnector(cmp, i);
		}
		d = 1;
	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackFuseSwitchDisconnector(Component cmp, int i) {
		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());

		if (null != comp) {

			// Get rack controller
			RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

			String str = "element." + d;
			d++;

			// add the new component to the specified position in the rack controller -
			// check for null on rack first!
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
						&& !controller.isOccupied(Position.of(i, 1))) {
					/* Create a component for rack controller */
					ComponentState componentdevicecompensator100 = (ComponentState) configurator
							.createComponent(DEVICE_COMPENSATOR_100);
					componentdevicecompensator100.finishState();
					controller.insert(Position.of(i, 1), componentdevicecompensator100, false);
				}
			}

			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 3) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
						&& !controller.isOccupied(Position.of(i, 1))) {
					/* Create a component for rack controller */
					ComponentState componentdevicecompensator50 = (ComponentState) configurator
							.createComponent(DEVICE_COMPENSATOR_50);
					componentdevicecompensator50.finishState();
					controller.insert(Position.of(i, 1), componentdevicecompensator50, false);
				}
			}

		}

	}

	// XLH
	/**
	 * Gets the kit position on the rack/Kit_frames
	 */
	private void getKitPositionInRackXLH(Component cmp) {

		for (int i = 1; i < 41; i++) {
			insertKitInRackXLH(cmp, i);
		}
		e = 1;
	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackXLH(Component cmp, int i) {
		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + e;
		e++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!
		if (cmp.getStateVector().getVar(str).getLegalValues().size() > 4) {
			if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
					&& !controller.isOccupied(Position.of(1, i))) {
				/* Create a component for rack controller */
				ComponentState kitxlinecompblindrbbs = (ComponentState) configurator
						.createComponent("kit_xline_comp_blind_rbbs");
				kitxlinecompblindrbbs.finishState();
				controller.insert(Position.of(1, i), kitxlinecompblindrbbs, false);
				assignHeightWidthValuesXLH(kitxlinecompblindrbbs);
			}
		}

		if (cmp.getStateVector().getVar(str).getLegalValues().size() <= 4
				&& cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
			if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
					&& !controller.isOccupied(Position.of(1, i))) {
				/* Create a component for rack controller */
				ComponentState componentdevicecompensator50 = (ComponentState) configurator
						.createComponent(DEVICE_COMPENSATOR_50);
				componentdevicecompensator50.finishState();
				controller.insert(Position.of(1, i), componentdevicecompensator50, false);
			}
		}
	}

	/**
	 * Assign maximum legal height and width values of the current kits
	 * 
	 * @param device_width
	 */
	private void assignHeightWidthValuesXLH(Component kitComponent) {
		StateVar height;
		height = kitComponent.getStateVector().getVar("device_width");
		List<StateValue> cmpHeight = height.getDomain();
		List<StateValue> legatListHeight = new ArrayList<>();
		for (int i = 0; i < cmpHeight.size(); i++) {
			if (height.isLegalValue(cmpHeight.get(i))) {
				legatListHeight.add(cmpHeight.get(i));
			}
		}

		int maxHeight = 0;
		for (StateValue st : legatListHeight) {
			int i = Integer.parseInt(st.toString());
			if (i > maxHeight) {
				maxHeight = i;
			}
		}

		height.setValue(String.valueOf(maxHeight));

	}

	// XLV
	/**
	 * Gets the kit position on the rack/Kit_frames
	 */
	private void getKitPositionInRackXLV(Component cmp) {

		for (int i = 1; i < 19; i++) {
			insertKitInRackXLV(cmp, i);
		}
		f = 1;
	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackXLV(Component cmp, int i) {
		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + f;
		f++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!
		if (cmp.getStateVector().getVar(str).getLegalValues().size() == 10
				|| cmp.getStateVector().getVar(str).getLegalValues().size() == 8) {
			if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
					&& !controller.isOccupied(Position.of(i, 1))) {
				/* Create a component for rack controller */
				ComponentState kitxlinecompblindrbbs = (ComponentState) configurator
						.createComponent("kit_xline_comp_blind_rbbs");
				kitxlinecompblindrbbs.finishState();
				controller.insert(Position.of(i, 1), kitxlinecompblindrbbs, false);
				assignHeightWidthValuesXLV(kitxlinecompblindrbbs);
			}
		}

		if (cmp.getStateVector().getVar(str).getLegalValues().size() == 5) {
			if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
					&& !controller.isOccupied(Position.of(i, 1))) {
				/* Create a component for rack controller */
				ComponentState componentdevicecompensator100 = (ComponentState) configurator
						.createComponent(DEVICE_COMPENSATOR_100);
				componentdevicecompensator100.finishState();
				controller.insert(Position.of(i, 1), componentdevicecompensator100, false);
			}
		}

		if (cmp.getStateVector().getVar(str).getLegalValues().size() == 3) {
			if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
					&& !controller.isOccupied(Position.of(i, 1))) {
				/* Create a component for rack controller */
				ComponentState componentdevicecompensator50 = (ComponentState) configurator
						.createComponent(DEVICE_COMPENSATOR_50);
				controller.insert(Position.of(i, 1), componentdevicecompensator50, false);
			}
		}
	}

	/**
	 * Assign maximum legal height and width values of the current kits
	 * 
	 * @param device_width
	 */
	private void assignHeightWidthValuesXLV(Component kitComponent) {
		StateVar height;
		height = kitComponent.getStateVector().getVar("device_width");

		List<StateValue> cmpHeight = height.getDomain();

		List<StateValue> legatListHeight = new ArrayList<>();

		for (int i = 0; i < cmpHeight.size(); i++) {
			if (height.isLegalValue(cmpHeight.get(i))) {
				legatListHeight.add(cmpHeight.get(i));
			}
		}

		int maxHeight = 0;
		for (StateValue st : legatListHeight) {
			int i = Integer.parseInt(st.toString());
			if (i > maxHeight) {
				maxHeight = i;
			}
		}

		height.setValue(String.valueOf(maxHeight));

	}

	// Inline
	/**
	 * Gets the kit position on the rack/Kit_frames
	 */
	private void getKitPositionInRackInline(Component cmp) {
		for (int i = 1; i < 19; i++) {
			insertKitInRackInline(cmp, i);
		}
		g = 1;
	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackInline(Component cmp, int i) {
		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + g;
		g++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!
		if (cmp.getStateVector().getVar(str).getLegalValues().size() == 7) {
			if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
					&& !controller.isOccupied(Position.of(i, 1))) {
				/* Create a component for rack controller */
				ComponentState componentdevicecompensator100 = (ComponentState) configurator
						.createComponent(DEVICE_COMPENSATOR_100);
				componentdevicecompensator100.finishState();
				controller.insert(Position.of(i, 1), componentdevicecompensator100, false);
			}
		}

		if (cmp.getStateVector().getVar(str).getLegalValues().size() == 3) {
			if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE
					&& !controller.isOccupied(Position.of(i, 1))) {
				/* Create a component for rack controller */
				ComponentState componentdevicecompensator50 = (ComponentState) configurator
						.createComponent(DEVICE_COMPENSATOR_50);
				componentdevicecompensator50.finishState();
				controller.insert(Position.of(i, 1), componentdevicecompensator50, false);
			}
		}
	}
}
