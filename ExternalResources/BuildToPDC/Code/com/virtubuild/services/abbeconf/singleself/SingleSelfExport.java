/**************************************************************************
 * Single Self Export                                					  *
 * It calculates the count of the variable xline_compartment_kit_qty  	  *
 * if spare and breaker or combination of both are in a sequence then it  *
 * counts it as one.													  *
 **************************************************************************/
package com.virtubuild.services.abbeconf.singleself;

import java.util.List;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.api.StateValue;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.core.frame.rack.Position;

public class SingleSelfExport {
	private List<Component> component;
	Configuration configurator;

	/**
	 * Constructor
	 * 
	 * @param allComponents List of all the available Components
	 * @param configurator manager object with functionality for accessing the model
	 *                     configuration
	 */
	public SingleSelfExport(List<Component> cmp, Configuration configurator) {
		this.component = cmp;
		this.configurator = configurator;
	}

	/**
	 * Checks if the current column is XLH and traverses over it to update the xline
	 * Compartment Kit Qty
	 */
	public void loadKits() {
		for (Component cmp : component) {
			if (cmp.getID().matches("column_xline_hor_rbbs\\d+")) {
				String xlineCompartmentKitQty = traverseKitPositionInRackXLH(cmp);
				System.out.println("Initial:" + xlineCompartmentKitQty);
				cmp.getStateVector().getVar("xline_compartment_kit_qty").setValue(xlineCompartmentKitQty);
			}
		}
	}

	// XLH
	/**
	 * Traverses XLH column rack and returns xline Compartment Kit Qty
	 */
	private String traverseKitPositionInRackXLH(Component cmp) {

		int qtyCount = 0; // xline_compartment_kit_qty

		for (int i = 1; i < 41; i++) {
			boolean brk = true;
			String elePosItr = "element." + i;

			if (brk && checkValidKit(cmp, elePosItr)) {
				String kitHeight = getKitHeight(cmp,i);

				if (kitHeight.matches("150")) {
					if (i + 3 > 40) {
						qtyCount++;
						brk = false;
					} else {
						String elePos = "element." + (i + 3);
						if (checkPosEmpty(cmp, elePos) || !checkValidKit(cmp, elePos)) {
							qtyCount++;
							brk = false;
						} else {
							continue;
						}
					}

				} else if (kitHeight.matches("200")) {
					if (i + 4 > 40) {
						qtyCount++;
						brk = false;
					} else {

						String elePos = "element." + (i + 4);
						if (checkPosEmpty(cmp, elePos) || !checkValidKit(cmp, elePos)) {
							qtyCount++;
							brk = false;
						} else {
							continue;
						}
					}
				} else if (kitHeight.matches("250")) {
					if (i + 5 > 40) {
						qtyCount++;
						brk = false;
					} else {

						String elePos = "element." + (i + 5);
						if (checkPosEmpty(cmp, elePos) || !checkValidKit(cmp, elePos)) {
							qtyCount++;
							brk = false;
						} else {
							continue;
						}
					}
				} else if (kitHeight.matches("300")) {
					if (i + 6 > 40) {
						qtyCount++;
						brk = false;
					} else {
						String elePos = "element." + (i + 6);
						if (checkPosEmpty(cmp, elePos) || !checkValidKit(cmp, elePos)) {
							qtyCount++;
							brk = false;
						} else {
							continue;
						}
					}
				} else if (kitHeight.matches("350")) {
					if (i + 7 > 40) {
						qtyCount++;
						brk = false;
					} else {
						String elePos = "element." + (i + 7);
						if (checkPosEmpty(cmp, elePos) || !checkValidKit(cmp, elePos)) {
							qtyCount++;
							brk = false;
						} else {
							continue;
						}
					}
				}
			}

		}
		return Integer.toString(qtyCount);
	}

	/**
	 * Checks if the element.x pos is empty or not
	 */
	private boolean checkPosEmpty(Component cmp, String elePos) {
		boolean posEmpty = false;
		if (cmp.getStateVector().getRole(elePos).getValue() == StateValue.NONE
				&& cmp.getStateVector().getRole(elePos).getLegalValues().size() > 1) {
			posEmpty = true;
		}
		return posEmpty;
	}

	/**
	 * Checks if the kit is Breaker or Spare
	 */
	private boolean checkValidKit(Component cmp, String elePos) {
		boolean validKit = false;
		if (cmp.getStateVector().getVar(elePos).getValue().getID().matches("kit_xline_comp_breaker_rbbs")
				|| cmp.getStateVector().getVar(elePos).getValue().getID()
						.matches("kit_xline_comp_spare_rbbs")) {
			validKit = true;
		}
		return validKit;
	}

	/**
	 * Returns the kit height
	 * @param cmp 
	 */
	private String getKitHeight(Component cmp, int i) {
		// get component with rack controller
		ComponentState comp = (ComponentState) configurator.getComponent(cmp.getID());
		// Get rack controller
		RackController rackController = (RackController) comp.getFrameController(FrameControllerType.RACK);
		String currentRackCmp = rackController.getOccupyingComponents(Position.of(1, i)).toString();

		Component currentCmp = configurator.getComponent(currentRackCmp.substring(1, currentRackCmp.length() - 1));
		return currentCmp.getStateVector().getVar("kit_height").getValue().toString();
	}

}
