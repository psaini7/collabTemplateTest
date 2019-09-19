/******************************************************************
 * AutoCompleterDeviceQuantity                                 *
 * Represents a Auto Completer class for insertion of 	          *
 * number of kits/devices specified by the user in the empty 	  *
 * slots for current column.		  							  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityswitchfuse;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;

public class AutoCompleterDeviceQuantitySwitchFuse {

	private Component cmp;
	Configuration configurator;
	private int counterSwitchFuse = 1;

	private int counterXR0SwitchFuse = 0;
	private int counterXR1SwitchFuse = 0;
	private int counterXR2SwitchFuse = 0;
	private int counterXR3SwitchFuse = 0;
	private int counter50mmSwitchFuse = 0;
	//private int counter100mmSwitchFuse = 0;

	private static final String DEVICE_QTY_50MM = "device_qty_50mm";
	//private static final String DEVICE_QTY_100MM = "device_qty_100mm";
	private static final String DEVICE_QTY_SLIMLINE_XR0 = "device_qty_slimlineXR0";
	private static final String DEVICE_QTY_SLIMLINE_XR1 = "device_qty_slimlineXR1";
	private static final String DEVICE_QTY_SLIMLINE_XR2 = "device_qty_slimlineXR2";
	private static final String DEVICE_QTY_SLIMLINE_XR3 = "device_qty_slimlineXR3";

	/**
	 * Constructor
	 * 
	 * @param cmp          List of all the available Components
	 * @param configurator manager object with functionality for accessing the model
	 *                     configuration
	 */
	public AutoCompleterDeviceQuantitySwitchFuse(Component cmp, Configuration configurator) {
		this.cmp = cmp;
		this.configurator = configurator;
	}

	public int getDeviceQty50mm(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	/*public int getDeviceQty100mm(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_100MM).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}*/

	public int getDeviceQtySlimlineXR0(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINE_XR0).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQtySlimlineXR1(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINE_XR1).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQtySlimlineXR2(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINE_XR2).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQtySlimlineXR3(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINE_XR3).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	/**
	 * Resets device qty to 0
	 */
	private void resetQuantity(Component cmp) {
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINE_XR0).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINE_XR1).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINE_XR2).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINE_XR3).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_50MM).setValue("0");
		//cmp.getStateVector().getVar(DEVICE_QTY_100MM).setValue("0");
	}

	/**
	 * Calculate and insert kits on the current components
	 */
	public void loadKits() {
		if (cmp.toString().matches("kit_switchdisconnector_n185\\d+")) {
			traverseKitPositionInRackSwitchFuse(cmp);
			resetQuantity(cmp);
		}
	}

	// XLV
	/**
	 * Gets the kit position on the rack/Kit_frames
	 */
	private void traverseKitPositionInRackSwitchFuse(Component cmp) {

		for (int i = 1; i < 14; i++) {
			insertKitInRackSwitchFuse(cmp, i);
		}
		counterSwitchFuse = 1;
		counterXR0SwitchFuse = 0;
		counterXR1SwitchFuse = 0;
		counterXR2SwitchFuse = 0;
		counterXR3SwitchFuse = 0;
		counter50mmSwitchFuse = 0;
		//counter100mmSwitchFuse = 0;

	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackSwitchFuse(Component cmp, int i) {
		int deviceQtySlimlineXR0 = getDeviceQtySlimlineXR0(cmp);
		int deviceQtySlimlineXR1 = getDeviceQtySlimlineXR1(cmp);
		int deviceQtySlimlineXR2 = getDeviceQtySlimlineXR2(cmp);
		int deviceQtySlimlineXR3 = getDeviceQtySlimlineXR3(cmp);
		int deviceQty50mm = getDeviceQty50mm(cmp);
		//int deviceQty100mm = getDeviceQty100mm(cmp);

		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + counterSwitchFuse;
		counterSwitchFuse++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!

		if (deviceQtySlimlineXR3 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR3 + counterXR3SwitchFuse) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine200XR3 = (ComponentState) configurator
								.createComponent("device_SlimLine_200_XR3");
						deviceSlimLine200XR3.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceSlimLine200XR3);
						controller.insert(Position.of(i, 1), deviceSlimLine200XR3,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR3SwitchFuse = counterXR3SwitchFuse + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() != 7) {
				counterXR3SwitchFuse = counterXR3SwitchFuse + 1;
			}
		}

		if (deviceQtySlimlineXR2 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR2 + counterXR2SwitchFuse) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine200XR2 = (ComponentState) configurator
								.createComponent("device_SlimLine_200_XR2");
						deviceSlimLine200XR2.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceSlimLine200XR2);
						controller.insert(Position.of(i, 1), deviceSlimLine200XR2,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR2SwitchFuse = counterXR2SwitchFuse + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() != 7) {
				counterXR2SwitchFuse = counterXR2SwitchFuse + 1;
			}
		}

		if (deviceQtySlimlineXR1 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 4) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR1 + counterXR1SwitchFuse) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine100 = (ComponentState) configurator
								.createComponent("device_SlimLine_100");
						deviceSlimLine100.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceSlimLine100);
						controller.insert(Position.of(i, 1), deviceSlimLine100,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR1SwitchFuse = counterXR1SwitchFuse + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 5) {
				counterXR1SwitchFuse = counterXR1SwitchFuse + 1;
			}
		}

		if (deviceQtySlimlineXR0 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR0 + counterXR0SwitchFuse) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine50 = (ComponentState) configurator
								.createComponent("device_SlimLine_50");
						deviceSlimLine50.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceSlimLine50);
						controller.insert(Position.of(i, 1), deviceSlimLine50,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR0SwitchFuse = counterXR0SwitchFuse + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1) {
				counterXR0SwitchFuse = counterXR0SwitchFuse + 1;
			}
		}

		/*if (deviceQty100mm > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 5) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQty100mm + counter100mmSwitchFuse) {
						 Create a component for rack controller 
						ComponentState devicecompensator100 = (ComponentState) configurator
								.createComponent("device_compensator_100");
						devicecompensator100.finishState();
						controller.setRackElement(Position.of(i, 1), devicecompensator100);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counter100mmSwitchFuse = counter100mmSwitchFuse + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 5) {
				counter100mmSwitchFuse = counter100mmSwitchFuse + 1;
			}
		}*/

		if (deviceQty50mm > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQty50mm + counter50mmSwitchFuse) {
						/* Create a component for rack controller */
						ComponentState devicecompensator50 = (ComponentState) configurator
								.createComponent("device_compensator_50");
						devicecompensator50.finishState();
						//controller.setRackElement(Position.of(i, 1), devicecompensator50);
						controller.insert(Position.of(i, 1), devicecompensator50,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counter50mmSwitchFuse = counter50mmSwitchFuse + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1) {
				counter50mmSwitchFuse = counter50mmSwitchFuse + 1;
			}

		}

	}

}
