/******************************************************************
 * AutoCompleterDeviceQuantity                                 *
 * Represents a Auto Completer class for insertion of 	          *
 * number of kits/devices specified by the user in the empty 	  *
 * slots for current column.		  							  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.devicequantityxlh;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;

public class AutoCompleterDeviceQuantityXlh {
	private Component cmp;
	Configuration configurator;
	private int counterXlh = 1;

	private int counter50mmXlh = 0;
	private int counterXR0Xlh = 0;
	private int counterXR1Xlh = 0;
	private int counterXR2Xlh = 0;
	private int counterXR3Xlh = 0;
	private int counterSparekitXlh = 0;
	private int counterBlindkitXlh = 0;
	private int counterBreakerXlh = 0;

	private static final String DEVICE_QTY_50MM = "device_qty_50mm";
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
	public AutoCompleterDeviceQuantityXlh(Component cmp, Configuration configurator) {
		this.cmp = cmp;
		this.configurator = configurator;
	}

	public int getDeviceQty50mm(Component cmp) {
		String str1 = cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString();
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
		cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT).setValue("0");
		cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT).setValue("0");

	}

	/**
	 * Calculate and insert kits on the current components
	 */
	public void loadKits() {
		if (cmp.toString().matches("column_xline_hor_rbbs\\d+")) {
			traverseKitPositionInRackXLH(cmp);
			resetQuantity(cmp);
		}
	}

	// XLH
	/**
	 * Gets the kit position on the rack/Kit_frames
	 */
	private void traverseKitPositionInRackXLH(Component cmp) {

		for (int i = 1; i < 41; i++) {
			insertKitInRackXLH(cmp, i);
		}
		counterXlh = 1;
		counter50mmXlh = 0;
		counterXR0Xlh = 0;
		counterXR1Xlh = 0;
		counterXR2Xlh = 0;
		counterXR3Xlh = 0;
		counterSparekitXlh = 0;
		counterBlindkitXlh = 0;
		counterBreakerXlh = 0;

	}

	/**
	 * Insert the kit on the rack/kit_frames current position/role i.e element.x
	 */
	private void insertKitInRackXLH(Component cmp, int i) {

		int deviceQty50mm = getDeviceQty50mm(cmp);
		int deviceQtySlimlineXR0 = getDeviceQtySlimlineXR0(cmp);
		int deviceQtySlimlineXR1 = getDeviceQtySlimlineXR1(cmp);
		int deviceQtySlimlineXR2 = getDeviceQtySlimlineXR2(cmp);
		int deviceQtySlimlineXR3 = getDeviceQtySlimlineXR3(cmp);
		int deviceQtySparekit = getDeviceQtySparekit(cmp);
		int deviceQtyBlindkit = getDeviceQtyBlindkit(cmp);
		int deviceQtyBreakerkit = getDeviceQtyBreakerkit(cmp);

		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController controller = (RackController) comp.getFrameController(FrameControllerType.RACK);

		String str = "element." + counterXlh;
		counterXlh++;

		// add the new component to the specified position in the rack controller -
		// check for null on rack first!

		if (deviceQtySlimlineXR3 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 9) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR3 + counterXR3Xlh) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine200XR3 = (ComponentState) configurator
								.createComponent("device_SlimLine_200_XR3");
						deviceSlimLine200XR3.finishState();
						//controller.setRackElement(Position.of(1, i), deviceSlimLine200XR3);
						controller.insert(Position.of(1, i), deviceSlimLine200XR3,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR3Xlh = counterXR3Xlh + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() != 9) {
				counterXR3Xlh = counterXR3Xlh + 1;
			}
		}

		if (deviceQtySlimlineXR2 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() == 9) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR2 + counterXR2Xlh) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine200XR2 = (ComponentState) configurator
								.createComponent("device_SlimLine_200_XR2");
						deviceSlimLine200XR2.finishState();
						//controller.setRackElement(Position.of(1, i), deviceSlimLine200XR2);
						controller.insert(Position.of(1, i), deviceSlimLine200XR2,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR2Xlh = counterXR2Xlh + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() != 9) {
				counterXR2Xlh = counterXR2Xlh + 1;
			}
		}

		if (deviceQtySlimlineXR1 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 4) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR1 + counterXR1Xlh) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine100 = (ComponentState) configurator
								.createComponent("device_SlimLine_100");
						deviceSlimLine100.finishState();
						//controller.setRackElement(Position.of(1, i), deviceSlimLine100);
						controller.insert(Position.of(1, i), deviceSlimLine100,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR1Xlh = counterXR1Xlh + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 4) {
				counterXR1Xlh = counterXR1Xlh + 1;
			}
		}

		if (deviceQtySlimlineXR0 > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySlimlineXR0 + counterXR0Xlh) {
						/* Create a component for rack controller */
						ComponentState deviceSlimLine50 = (ComponentState) configurator
								.createComponent("device_SlimLine_50");
						deviceSlimLine50.finishState();
						//controller.setRackElement(Position.of(1, i), deviceSlimLine50);
						controller.insert(Position.of(1, i), deviceSlimLine50,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterXR0Xlh = counterXR0Xlh + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1) {
				counterXR0Xlh = counterXR0Xlh + 1;
			}
		}

		if (deviceQtyBreakerkit > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtyBreakerkit + counterBreakerXlh) {
						/* Create a component for rack controller */
						ComponentState kitXlineCompBreakerRbbs = (ComponentState) configurator
								.createComponent("kit_xline_comp_breaker_rbbs");
						kitXlineCompBreakerRbbs.finishState();
						//controller.setRackElement(Position.of(1, i), kitXlineCompBreakerRbbs);
						controller.insert(Position.of(1, i), kitXlineCompBreakerRbbs,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterBreakerXlh = counterBreakerXlh + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 7) {
				counterBreakerXlh = counterBreakerXlh + 1;
			}
		}
		if (deviceQtySparekit > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtySparekit + counterSparekitXlh) {
						/* Create a component for rack controller */
						ComponentState kitXlineCompSpareRbbs = (ComponentState) configurator
								.createComponent("kit_xline_comp_spare_rbbs");
						kitXlineCompSpareRbbs.finishState();
						//controller.setRackElement(Position.of(1, i), kitXlineCompSpareRbbs);
						controller.insert(Position.of(1, i), kitXlineCompSpareRbbs,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterSparekitXlh = counterSparekitXlh + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 7) {
				counterSparekitXlh = counterSparekitXlh + 1;
			}
		}

		if (deviceQtyBlindkit > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() >= 7) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQtyBlindkit + counterBlindkitXlh) {
						/* Create a component for rack controller */
						ComponentState kitXlineCompBlindRbbs = (ComponentState) configurator
								.createComponent("kit_xline_comp_blind_rbbs");
						kitXlineCompBlindRbbs.finishState();
						//controller.setRackElement(Position.of(1, i), kitXlineCompBlindRbbs);
						controller.insert(Position.of(1, i), kitXlineCompBlindRbbs,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counterBlindkitXlh = counterBlindkitXlh + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() < 7) {
				counterBlindkitXlh = counterBlindkitXlh + 1;
			}
		}

		if (deviceQty50mm > 0) {
			if (cmp.getStateVector().getVar(str).getLegalValues().size() > 1) {
				if (cmp.getStateVector().getRole(str).getValue() == StateValue.NONE) {
					if (i <= deviceQty50mm + counter50mmXlh) {
						/* Create a component for rack controller */
						ComponentState deviceCompensator50 = (ComponentState) configurator
								.createComponent("device_compensator_50");
						deviceCompensator50.finishState();
						//controller.setRackElement(Position.of(1, i), deviceCompensator50);
						controller.insert(Position.of(1, i), deviceCompensator50,false);
					}
				} else if (cmp.getStateVector().getRole(str).getValue() != StateValue.NONE) {
					counter50mmXlh = counter50mmXlh + 1;
				}
			} else if (cmp.getStateVector().getVar(str).getLegalValues().size() == 1) {
				counter50mmXlh = counter50mmXlh + 1;
			}

		}

	}

}
