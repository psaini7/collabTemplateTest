/******************************************************************
 * AutoCompleterDeviceQuantity                                 *
 * Represents a Auto Completer class for insertion of 	          *
 * number of kits/devices specified by the user in the empty 	  *
 * slots for current column.		  							  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityinline;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;

public class AutoCompleterDeviceQuantityInline {
	private Component cmp;
	Configuration configurator;
	private int counterInline = 1;

	private int counterNH0Inline = 0;
	private int counterNH1Inline = 0;
	private int counterNH2Inline = 0;
	private int counterNH3Inline = 0;
	private int counter50mmInline = 0;
	private int counter100mmInline = 0;

	private static final String DEVICE_QTY_NH0 = "device_qty_NH0";
	private static final String DEVICE_QTY_NH1 = "device_qty_NH1";
	private static final String DEVICE_QTY_NH2 = "device_qty_NH2";
	private static final String DEVICE_QTY_NH3 = "device_qty_NH3";
	private static final String DEVICE_QTY_50MM = "device_qty_50mm";
	private static final String DEVICE_QTY_100MM = "device_qty_100mm";

	/**
	 * Constructor
	 * 
	 * @param cmp          List of all the available Components
	 * @param configurator manager object with functionality for accessing the model
	 *                     configuration
	 */
	public AutoCompleterDeviceQuantityInline(Component cmp, Configuration configurator) {
		this.cmp = cmp;
		this.configurator = configurator;
	}

	public int getDeviceQtyNH0(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_NH0).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQtyNH1(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_NH1).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQtyNH2(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_NH2).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQtyNH3(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_NH3).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQty50mm(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQty100mm(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_100MM).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	/**
	 * Resets device qty to 0
	 */
	private void resetQuantity(Component cmp) {
		cmp.getStateVector().getVar(DEVICE_QTY_NH0).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_NH1).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_NH2).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_NH3).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_50MM).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_100MM).setValue("0");
	}

	/**
	 * Calculate and insert kits on the current components
	 */
	public void loadKits() {
		if (cmp.toString().matches("kit_inline_rbbs\\d+")) {
			traverseKitPositionInRackInline(cmp);
			resetQuantity(cmp);
		}
	}

	// Inline
	private void traverseKitPositionInRackInline(Component cmp) {
		int deviceQtyNH0 = getDeviceQtyNH0(cmp);
		int deviceQtyNH1 = getDeviceQtyNH1(cmp);
		int deviceQtyNH2 = getDeviceQtyNH2(cmp);
		int deviceQtyNH3 = getDeviceQtyNH3(cmp);
		int deviceQty50mm = getDeviceQty50mm(cmp);
		int deviceQty100mm = getDeviceQty100mm(cmp);

		for (int i = 1; i < 19; i++) {
			insertKitInRackInline(cmp, i, deviceQtyNH0, deviceQtyNH1, deviceQtyNH2, deviceQtyNH3, deviceQty50mm,
					deviceQty100mm);
		}
		counterInline = 1;
		counterNH1Inline = 0;
		counterNH2Inline = 0;
		counterNH3Inline = 0;
		counter50mmInline = 0;
		counter50mmInline = 0;

	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackInline(Component cmp, int i, int deviceQtyNH0, int deviceQtyNH1, int deviceQtyNH2,
			int deviceQtyNH3, int deviceQty50mm, int deviceQty100mm) {

		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + counterInline;
		counterInline++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!

		if (deviceQtyNH3 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtyNH3 + counterNH3Inline) {
						/* Create a component for rack controller */
						ComponentState deviceInLine2NH3 = (ComponentState) configurator
								.createComponent("device_InLine2_NH3");
						deviceInLine2NH3.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceInLine2NH3);
						controller.insert(Position.of(i, 1), deviceInLine2NH3,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterNH3Inline = counterNH3Inline + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1
					|| cmp.getStateVector().getVar(str).getLegalValues().size() == 3) {
				counterNH3Inline = counterNH3Inline + 1;
			}
		}

		if (deviceQtyNH2 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtyNH2 + counterNH2Inline) {
						/* Create a component for rack controller */
						ComponentState deviceInLine2NH2 = (ComponentState) configurator
								.createComponent("device_InLine2_NH2");
						deviceInLine2NH2.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceInLine2NH2);
						controller.insert(Position.of(i, 1), deviceInLine2NH2,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterNH2Inline = counterNH2Inline + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1
					|| cmp.getStateVector().getVar(str).getLegalValues().size() == 3) {
				counterNH2Inline = counterNH2Inline + 1;
			}
		}

		if (deviceQtyNH1 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtyNH1 + counterNH1Inline) {
						/* Create a component for rack controller */
						ComponentState deviceInLine2NH1 = (ComponentState) configurator
								.createComponent("device_InLine2_NH1");
						deviceInLine2NH1.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceInLine2NH1);
						controller.insert(Position.of(i, 1), deviceInLine2NH1,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterNH1Inline = counterNH1Inline + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1
					|| cmp.getStateVector().getVar(str).getLegalValues().size() == 3) {
				counterNH1Inline = counterNH1Inline + 1;
			}
		}

		if (deviceQtyNH0 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtyNH0 + counterNH0Inline) {
						/* Create a component for rack controller */
						ComponentState deviceInLine2NH00 = (ComponentState) configurator
								.createComponent("device_InLine2_NH00");
						deviceInLine2NH00.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceInLine2NH00);
						controller.insert(Position.of(i, 1), deviceInLine2NH00,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterNH0Inline = counterNH0Inline + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1) {
				counterNH0Inline = counterNH0Inline + 1;
			}
		}

		if (deviceQty100mm > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQty100mm + counter100mmInline) {
						/* Create a component for rack controller */
						ComponentState deviceCompensator100 = (ComponentState) configurator
								.createComponent("device_compensator_100");
						deviceCompensator100.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceCompensator100);
						controller.insert(Position.of(i, 1), deviceCompensator100,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counter100mmInline = counter100mmInline + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1
					|| cmp.getStateVector().getVar(str).getLegalValues().size() == 3) {
				counter100mmInline = counter100mmInline + 1;
			}
		}

		if (deviceQty50mm > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQty50mm + counter50mmInline) {
						/* Create a component for rack controller */
						ComponentState deviceCompensator50 = (ComponentState) configurator
								.createComponent("device_compensator_50");
						deviceCompensator50.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceCompensator50);
						controller.insert(Position.of(i, 1), deviceCompensator50,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counter50mmInline = counter50mmInline + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1) {
				counter50mmInline = counter50mmInline + 1;
			}

		}

	}

}
