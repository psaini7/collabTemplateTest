/******************************************************************
 * AutoCompleterDeviceQuantity                                 *
 * Represents a Auto Completer class for insertion of 	          *
 * number of kits/devices specified by the user in the empty 	  *
 * slots for current column.		  							  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityxlv;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;

public class AutoCompleterDeviceQuantityXlv {

	private Component cmp;
	Configuration configurator;
	private int counterXlv = 1;

	private int counterXR0Xlv = 0;
	private int counterXR1Xlv = 0;
	private int counterXR2Xlv = 0;
	private int counterXR3Xlv = 0;
	private int counter50mmXlv = 0;
	private int counter100mmXlv = 0;
	private int counterSparekitXlv = 0;
	private int counterBlindkitXlv = 0;
	private int counterBreakerXlv = 0;

	private static final String DEVICE_QTY_50MM = "device_qty_50mm";
	private static final String DEVICE_QTY_100MM = "device_qty_100mm";
	private static final String DEVICE_QTY_SPAREKIT = "device_qty_sparekit";
	private static final String DEVICE_QTY_BLINDKIT = "device_qty_blindkit";
	private static final String DEVICE_QTY_BREAKERKIT = "device_qty_breakerkit";
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
	public AutoCompleterDeviceQuantityXlv(Component cmp, Configuration configurator) {
		this.cmp = cmp;
		this.configurator = configurator;
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

	public int getDeviceQtySparekit(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQtyBlindkit(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT).getValue().toString();
		int deviceQty = 0;
		deviceQty = Integer.parseInt(str1);
		return deviceQty;
	}

	public int getDeviceQtyBreakerkit(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT).getValue().toString();
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
		cmp.getStateVector().getVar(DEVICE_QTY_100MM).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT).setValue("0");

	}

	/**
	 * Calculate and insert kits on the current components
	 */
	public void loadKits() {
		if (cmp.toString().matches("kit_xline_vert_rbbs\\d+")) {
			traverseKitPositionInRackXLV(cmp);
			resetQuantity(cmp);
		}
	}

	// XLV
	/**
	 * Gets the kit position on the rack/Kit_frames
	 */
	private void traverseKitPositionInRackXLV(Component cmp) {

		for (int i = 1; i < 19; i++) {
			insertKitInRackXLV(cmp, i);
		}
		counterXlv = 1;
		counterXR0Xlv = 0;
		counterXR1Xlv = 0;
		counterXR2Xlv = 0;
		counterXR3Xlv = 0;
		counter50mmXlv = 0;
		counter100mmXlv = 0;
		counterSparekitXlv = 0;
		counterBlindkitXlv = 0;
		counterBreakerXlv = 0;
	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackXLV(Component cmp, int i) {
		int deviceQtySlimlineXR0 = getDeviceQtySlimlineXR0(cmp);
		int deviceQtySlimlineXR1 = getDeviceQtySlimlineXR1(cmp);
		int deviceQtySlimlineXR2 = getDeviceQtySlimlineXR2(cmp);
		int deviceQtySlimlineXR3 = getDeviceQtySlimlineXR3(cmp);
		int deviceQty50mm = getDeviceQty50mm(cmp);
		int deviceQty100mm = getDeviceQty100mm(cmp);
		int deviceQtySparekit = getDeviceQtySparekit(cmp);
		int deviceQtyBlindkit = getDeviceQtyBlindkit(cmp);
		int deviceQtyBreakerkit = getDeviceQtyBreakerkit(cmp);

		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + counterXlv;
		counterXlv++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!

		if (deviceQtySlimlineXR3 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 10) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR3 + counterXR3Xlv) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine200XR3 = (ComponentState) configurator
								.createComponent("device_SlimLine_200_XR3");
						deviceSlimLine200XR3.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceSlimLine200XR3);
						controller.insert(Position.of(i, 1), deviceSlimLine200XR3,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR3Xlv = counterXR3Xlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() != 10) {
				counterXR3Xlv = counterXR3Xlv + 1;
			}
		}

		if (deviceQtySlimlineXR2 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 10) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR2 + counterXR2Xlv) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine200XR2 = (ComponentState) configurator
								.createComponent("device_SlimLine_200_XR2");
						deviceSlimLine200XR2.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceSlimLine200XR2);
						controller.insert(Position.of(i, 1), deviceSlimLine200XR2,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR2Xlv = counterXR2Xlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() != 10) {
				counterXR2Xlv = counterXR2Xlv + 1;
			}
		}

		if (deviceQtySlimlineXR1 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 5) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR1 + counterXR1Xlv) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine100 = (ComponentState) configurator
								.createComponent("device_SlimLine_100");
						deviceSlimLine100.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceSlimLine100);
						controller.insert(Position.of(i, 1), deviceSlimLine100,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR1Xlv = counterXR1Xlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 5) {
				counterXR1Xlv = counterXR1Xlv + 1;
			}
		}

		if (deviceQtySlimlineXR0 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR0 + counterXR0Xlv) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine50 = (ComponentState) configurator
								.createComponent("device_SlimLine_50");
						deviceSlimLine50.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceSlimLine50);
						controller.insert(Position.of(i, 1), deviceSlimLine50,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR0Xlv = counterXR0Xlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1) {
				counterXR0Xlv = counterXR0Xlv + 1;
			}
		}

		if (deviceQtyBreakerkit > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 8) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtyBreakerkit + counterBreakerXlv) {
						/* Create a component for rack controller */
						ComponentState kitXlineCompBreakerRbbs = (ComponentState) configurator
								.createComponent("kit_xline_comp_breaker_rbbs");
						kitXlineCompBreakerRbbs.finishState();
						//controller.setRackElement(Position.of(i, 1), kitXlineCompBreakerRbbs);
						controller.insert(Position.of(i, 1), kitXlineCompBreakerRbbs,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterBreakerXlv = counterBreakerXlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 8) {
				counterBreakerXlv = counterBreakerXlv + 1;
			}
		}

		if (deviceQtySparekit > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 8) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySparekit + counterSparekitXlv) {
						/* Create a component for rack controller */
						ComponentState kitXlineCompSpareRbbs = (ComponentState) configurator
								.createComponent("kit_xline_comp_spare_rbbs");
						kitXlineCompSpareRbbs.finishState();
						//controller.setRackElement(Position.of(i, 1), kitXlineCompSpareRbbs);
						controller.insert(Position.of(i, 1), kitXlineCompSpareRbbs,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterSparekitXlv = counterSparekitXlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 8) {
				counterSparekitXlv = counterSparekitXlv + 1;
			}
		}

		if (deviceQtyBlindkit > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 8) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtyBlindkit + counterBlindkitXlv) {
						/* Create a component for rack controller */
						ComponentState kitXlineCompBlindRbbs = (ComponentState) configurator
								.createComponent("kit_xline_comp_blind_rbbs");
						kitXlineCompBlindRbbs.finishState();
						//controller.setRackElement(Position.of(i, 1), kitXlineCompBlindRbbs);
						controller.insert(Position.of(i, 1), kitXlineCompBlindRbbs,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterBlindkitXlv = counterBlindkitXlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 8) {
				counterBlindkitXlv = counterBlindkitXlv + 1;
			}
		}

		if (deviceQty100mm > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 5) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQty100mm + counter100mmXlv) {
						/* Create a component for rack controller */
						ComponentState deviceCompensator100 = (ComponentState) configurator
								.createComponent("device_compensator_100");
						deviceCompensator100.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceCompensator100);
						controller.insert(Position.of(i, 1), deviceCompensator100,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counter100mmXlv = counter100mmXlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 5) {
				counter100mmXlv = counter100mmXlv + 1;
			}
		}

		if (deviceQty50mm > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQty50mm + counter50mmXlv) {
						/* Create a component for rack controller */
						ComponentState deviceCompensator50 = (ComponentState) configurator
								.createComponent("device_compensator_50");
						deviceCompensator50.finishState();
						//controller.setRackElement(Position.of(i, 1), deviceCompensator50);
						controller.insert(Position.of(i, 1), deviceCompensator50,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counter50mmXlv = counter50mmXlv + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1) {
				counter50mmXlv = counter50mmXlv + 1;
			}

		}

	}

}
