package com.virtubuild.services.abbeconf.device;

/******************************************************************
 * DynamicDeviceQuantity                                 		  *
 * Dynamically change the drop down values of the device quantity *
 * in RBBS and N185 product line based on the available empty 	  *
 * slots for FuseSwitch Inline and XLV, XLH and SwitchFuse columns*
 ******************************************************************/

import java.util.ArrayList;
import java.util.List;

import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;

public class DynamicDeviceQuantity {
	private List<Element> listElement;
	private Component cmp;
	Configuration configurator;
	private BaseComponent arg0;

	private static final String ELEMENT = "element.";
	private static final String AVAILABLE_ROWS = "availableRows";
	private static final String AVAILABLE_COLUMNS = "availableColumns";
	private static final String NONE = "none";
	private static final String NOT_SET = "not set";
	private static final String DEVICE_QTY_50MM = "device_qty_50mm";
	private static final String DEVICE_QTY_100MM = "device_qty_100mm";
	private static final String DEVICE_QTY_50MM_LIMIT = "device_qty_50mm_limit";
	private static final String DEVICE_QTY_100MM_LIMIT = "device_qty_100mm_limit";
	private static final String DEVICE_COMPENSATOR_50 = "device_compensator_50";
	private static final String DEVICE_COMPENSATOR_100 = "device_compensator_100";
	private static final String DEVICE_SLIMLINE_50 = "device_SlimLine_50";
	private static final String DEVICE_SLIMLINE_100 = "device_SlimLine_100";
	private static final String KIT_XLINE_COMP_BLIND_RBBS = "kit_xline_comp_blind_rbbs";
	private static final String KIT_XLINE_COMP_SPARE_RBBS = "kit_xline_comp_spare_rbbs";
	private static final String KIT_XLINE_COMP_BREAKER_RBBS = "kit_xline_comp_breaker_rbbs";
	private static final String DEVICE_SLIMLINE_XR3 = "device_SlimLine_200_XR3";
	private static final String DEVICE_SLIMLINE_XR2 = "device_SlimLine_200_XR2";
	
	private static final String DEVICE_QTY_SLIMLINEXR0 = "device_qty_slimlineXR0";
	private static final String DEVICE_QTY_SLIMLINEXR1 = "device_qty_slimlineXR1";
	private static final String DEVICE_QTY_SLIMLINEXR2 = "device_qty_slimlineXR2";
	private static final String DEVICE_QTY_SLIMLINEXR3 = "device_qty_slimlineXR3";

	private static final String DEVICE_QTY_BLINDKIT_LIMIT = "device_qty_blindkit_limit";
	private static final String DEVICE_QTY_BREAKERKIT_LIMIT = "device_qty_breakerkit_limit";
	private static final String DEVICE_QTY_SPAREKIT_LIMIT = "device_qty_sparekit_limit";
	private static final String DEVICE_QTY_BLINDKIT = "device_qty_blindkit";
	private static final String DEVICE_QTY_BREAKERKIT = "device_qty_breakerkit";
	private static final String DEVICE_QTY_SPAREKIT = "device_qty_sparekit";

	private static final String DEVICE_QTY_SLIMLINEXR0_LIMIT = "device_qty_slimlineXR0_limit";
	private static final String DEVICE_QTY_SLIMLINEXR1_LIMIT = "device_qty_slimlineXR1_limit";
	private static final String DEVICE_QTY_SLIMLINEXR2_LIMIT = "device_qty_slimlineXR2_limit";
	private static final String DEVICE_QTY_SLIMLINEXR3_LIMIT = "device_qty_slimlineXR3_limit";
	private static final String DBBS_POLES = "DBBS_poles";
	private static final String KIT_XLINE_VERT_RBBS = "kit_xline_vert_rbbs";
	private static final String COLUMN_XLINE_HOR_RBBS = "column_xline_hor_rbbs";
	private static final String KIT_XLINE_COMP = "kit_xline_comp";
	private static final String SWITCHBOARD_RBBS = "switchboard_rbbs";
	private static final String THREE_POLES = "3 (3 poles)";
	private static final String COLUMN = "column";
	private static final String DEVICE = "device";
	private static final String KIT_SWITCHDISCONNECTOR_N185 = "kit_switchdisconnector_n185";
	private static final String KIT_INLINE_RBBS = "kit_inline_rbbs";
	private static final String KIT_FUSESWITCHDISCONNECTOR_N185 = "kit_fuseswitchdisconnector_n185";
	private static final String DEVICE_QTY_NH0 = "device_qty_NH0";
	private static final String DEVICE_QTY_NH1 = "device_qty_NH1";
	private static final String DEVICE_QTY_NH2 = "device_qty_NH2";
	private static final String DEVICE_QTY_NH3 = "device_qty_NH3";
	private static final String DEVICE_QTY_NH0_LIMIT = "device_qty_NH0_limit";
	private static final String DEVICE_QTY_NH1_LIMIT = "device_qty_NH1_limit";
	private static final String DEVICE_QTY_NH2_LIMIT = "device_qty_NH2_limit";
	private static final String DEVICE_QTY_NH3_LIMIT = "device_qty_NH3_limit";
	private static final String DEVICE_INLINE_NH00 = "device_InLine2_NH00";
	/**
	 * Constructor
	 * 
	 * @param cmp          List of all the available Components
	 * @param configurator manager object with functionality for accessing the model
	 *                     configuration
	 * @param arg0
	 */
	public DynamicDeviceQuantity(Component cmp, Configuration configurator, BaseComponent arg0) {
		this.cmp = cmp;
		this.configurator = configurator;
		this.arg0 = arg0;
	}

	public List<Element> getListElement() {
		return listElement;
	}

	public void setListElement(List<Element> listElement) {
		this.listElement = listElement;
	}

	/**
	 * Calculate the max available dropdown values that can be inserted in the rack.
	 */
	public void run() {

		if (cmp.getTypeOfActor().startsWith(DEVICE)) {
			if (arg0.getTypeID() != null && arg0.getTypeID().matches(KIT_INLINE_RBBS)
					|| arg0.getTypeID().matches(KIT_FUSESWITCHDISCONNECTOR_N185)) {
				traverseKitPositionInRackInlineFuseSwitch((Component) arg0);
			} else if (arg0.getTypeID() != null && arg0.getTypeID().matches(KIT_XLINE_VERT_RBBS)
					&& arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLV((Component) arg0);
			} else if (arg0.getTypeID() != null && arg0.getTypeID().matches(KIT_XLINE_VERT_RBBS)
					&& !arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLVFourPoles((Component) arg0);
			} else if (arg0.getTypeID() != null && arg0.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)
					&& arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLH((Component) arg0);
			} else if (arg0.getTypeID() != null && arg0.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)
					&& !arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLHFourPoles((Component) arg0);
			} else if (arg0.getTypeID() != null && arg0.getTypeID().matches(KIT_SWITCHDISCONNECTOR_N185)) {
				traverseKitPositionInRackSwitchFuse((Component) arg0);
			}

		} else {
			if (cmp.getTypeID().matches(KIT_INLINE_RBBS)
					|| cmp.getTypeID().matches(KIT_FUSESWITCHDISCONNECTOR_N185)) {
				traverseKitPositionInRackInlineFuseSwitch(cmp);
			} else if (cmp.getTypeID().matches(KIT_SWITCHDISCONNECTOR_N185)) {
				traverseKitPositionInRackSwitchFuse(cmp);
			}

			else if ((cmp.getTypeID().startsWith(COLUMN) || cmp.getTypeID().startsWith(SWITCHBOARD_RBBS))
					&& (arg0.getTypeID().matches(KIT_INLINE_RBBS)
							|| arg0.getTypeID().matches(KIT_FUSESWITCHDISCONNECTOR_N185))) {
				traverseKitPositionInRackInlineFuseSwitch((Component) arg0);
			} else if ((cmp.getTypeID().startsWith(COLUMN) || cmp.getTypeID().startsWith(SWITCHBOARD_RBBS))
					&& arg0.getTypeID().matches(KIT_SWITCHDISCONNECTOR_N185)) {
				traverseKitPositionInRackSwitchFuse((Component) arg0);
			}

			else if (cmp.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)
					&& cmp.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLH(cmp);
			} else if (cmp.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)
					&& !cmp.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLHFourPoles(cmp);
			} else if ((cmp.getTypeID().startsWith(COLUMN) || cmp.getTypeID().startsWith(SWITCHBOARD_RBBS))
					&& arg0.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)
					&& arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLH((Component) arg0);
			} else if ((cmp.getTypeID().startsWith(COLUMN) || cmp.getTypeID().startsWith(SWITCHBOARD_RBBS))
					&& arg0.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)
					&& !arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLHFourPoles((Component) arg0);
			} else if (cmp.getTypeID().startsWith(KIT_XLINE_COMP) && arg0.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)
					&& arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLH((Component) arg0);
			} else if (cmp.getTypeID().startsWith(KIT_XLINE_COMP) && arg0.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)
					&& !arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLHFourPoles((Component) arg0);
			}

			else if (cmp.getTypeID().matches(KIT_XLINE_VERT_RBBS)
					&& cmp.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLV(cmp);
			} else if (cmp.getTypeID().matches(KIT_XLINE_VERT_RBBS)
					&& !cmp.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLVFourPoles(cmp);
			} else if ((cmp.getTypeID().startsWith(COLUMN) || cmp.getTypeID().startsWith(SWITCHBOARD_RBBS))
					&& arg0.getTypeID().matches(KIT_XLINE_VERT_RBBS)
					&& arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLV((Component) arg0);
			} else if ((cmp.getTypeID().startsWith(COLUMN) || cmp.getTypeID().startsWith(SWITCHBOARD_RBBS))
					&& arg0.getTypeID().matches(KIT_XLINE_VERT_RBBS)
					&& !arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLVFourPoles((Component) arg0);
			} else if (cmp.getTypeID().startsWith(KIT_XLINE_COMP) && arg0.getTypeID().matches(KIT_XLINE_VERT_RBBS)
					&& arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLV((Component) arg0);
			} else if (cmp.getTypeID().startsWith(KIT_XLINE_COMP) && arg0.getTypeID().matches(KIT_XLINE_VERT_RBBS)
					&& !arg0.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)) {
				traverseKitPositionInRackXLVFourPoles((Component) arg0);
			}

		}
	}

	/**
	 * Returns the maximum slots available in the rack based on column width.
	 */
	public int getMaxAvailableSlots(Component cmp) {
		if (cmp.getTypeID().matches(COLUMN_XLINE_HOR_RBBS)) {
			return Integer.parseInt(cmp.getStateVector().getVar(AVAILABLE_ROWS).getValue().toString());
		} else {
			return Integer.parseInt(cmp.getStateVector().getVar(AVAILABLE_COLUMNS).getValue().toString());
		}
	}

	/**
	 * Traverse the rack for FussSwitch and Inline column and calculates the max
	 * possible dropdown values for each kits, devices and compensators.
	 **/
	private void traverseKitPositionInRackInlineFuseSwitch(Component cmp) {
		IdentifyEmptySlotsInlineFuseSwitch(cmp);
		// For NH0
		int iNH0LimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_NH0).getValue().toString());
		UpdateElementListInlineFuseSwitch(iNH0LimitSelected, 1, cmp);
		// For NH1
		int iNH1LimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_NH1).getValue().toString());
		UpdateElementListInlineFuseSwitch(iNH1LimitSelected, 2, cmp);
		// For NH2
		int iNH2LimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_NH2).getValue().toString());
		UpdateElementListInlineFuseSwitch(iNH2LimitSelected, 2, cmp);
		// For NH3
		int iNH3LimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_NH3).getValue().toString());
		UpdateElementListInlineFuseSwitch(iNH3LimitSelected, 2, cmp);
		// For 50mm
		int i50mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString());
		UpdateElementListInlineFuseSwitch(i50mmLimitSelected, 1, cmp);
		// For 100mm
		int i100mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_100MM).getValue().toString());
		UpdateElementListInlineFuseSwitch(i100mmLimitSelected, 2, cmp);

		// Getting and setting drop down list values
		int iNH0Limit = GetMaxLimitInDropDownInlineFuseSwitch(1, cmp, iNH0LimitSelected);
		int iNH1Limit = GetMaxLimitInDropDownInlineFuseSwitch(2, cmp, iNH1LimitSelected);
		int iNH2Limit = GetMaxLimitInDropDownInlineFuseSwitch(2, cmp, iNH2LimitSelected);
		int iNH3Limit = GetMaxLimitInDropDownInlineFuseSwitch(2, cmp, iNH3LimitSelected);
		int i50mmLimit = GetMaxLimitInDropDownInlineFuseSwitch(1, cmp, i50mmLimitSelected);
		int i100mmLimit = GetMaxLimitInDropDownInlineFuseSwitch(2, cmp, i100mmLimitSelected);

		cmp.getStateVector().getVar(DEVICE_QTY_NH0_LIMIT).setValue(String.valueOf(iNH0Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_NH1_LIMIT).setValue(String.valueOf(iNH1Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_NH2_LIMIT).setValue(String.valueOf(iNH2Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_NH3_LIMIT).setValue(String.valueOf(iNH3Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_50MM_LIMIT).setValue(String.valueOf(i50mmLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_100MM_LIMIT).setValue(String.valueOf(i100mmLimit));

	}

	/**
	 * Identifies the empty slots
	 **/
	private void IdentifyEmptySlotsInlineFuseSwitch(Component cmp) {
		listElement = new ArrayList<>();
		int iTotalSlots = getMaxAvailableSlots(cmp);
		boolean doubleWidthDevice = false;
		for (int i = 1; i <= iTotalSlots; i++) {
			Element objElement = new Element();
			String str = ELEMENT + i;
			if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE) && !doubleWidthDevice) {
				objElement.isInserted = false;
				objElement.isAvailable = true;
				objElement.isReserved = false;
			} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE)
					&& doubleWidthDevice) {
				objElement.isInserted = true;
				objElement.isAvailable = false;
				objElement.isReserved = false;
				doubleWidthDevice = false;
			} else {
				String element = cmp.getStateVector().getVar(str).getValue().getID();

				if (element.matches(DEVICE_INLINE_NH00) || element.matches(DEVICE_COMPENSATOR_50)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
				} else {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					doubleWidthDevice = true;
				}
			}
			listElement.add(objElement);
		}
	}

	/**
	 * Calculates the available and reserved values for the dropdown
	 **/
	private void UpdateElementListInlineFuseSwitch(int iLimit, int iDeviceSize, Component cmp) {
		int iCounter = iLimit;
		if (iDeviceSize == 1) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				Element objCurrentElement = listElement.get(i);
				if (objCurrentElement.isAvailable) {
					objCurrentElement.isAvailable = false;
					objCurrentElement.isReserved = true;
					iCounter = iCounter - 1;
				}
			}
		} else if (iDeviceSize == 2) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices > 3 && i < (listElement.size() - 1)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		}

	}

	/**
	 * Calculates the max value for the limit variables
	 **/
	private int GetMaxLimitInDropDownInlineFuseSwitch(int iDeviceSize, Component cmp, int iAlreadySelectedValue) {
		int iMaxNumber = 0;
		if (iDeviceSize == 1) {
			for (int i = 0; i < listElement.size(); i++) {
				Element objCurrentElement = listElement.get(i);
				if (objCurrentElement.isAvailable) {

					iMaxNumber = iMaxNumber + 1;
				}
			}
		} else if (iDeviceSize == 2) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices > 3 && i < (listElement.size() - 1)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 1;
					}
				}
			}

		}

		return iMaxNumber + iAlreadySelectedValue;
	}

	// XLV
	private void traverseKitPositionInRackXLV(Component cmp) {
		IdentifyEmptySlots(cmp);
		// For XR0
		int iXR0LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0).getValue().toString());
		UpdateElementListXLV(iXR0LimitSelected, 1, cmp);
		// For XR1
		int iXR1LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1).getValue().toString());
		UpdateElementListXLV(iXR1LimitSelected, 2, cmp);
		// For XR2
		int iXR2LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2).getValue().toString());
		UpdateElementListXLV(iXR2LimitSelected, 4, cmp);
		// For NH3
		int iXR3LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3).getValue().toString());
		UpdateElementListXLV(iXR3LimitSelected, 4, cmp);
		// For 50mm
		int i50mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString());
		UpdateElementListXLV(i50mmLimitSelected, 1, cmp);
		// For 100mm
		int i100mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_100MM).getValue().toString());
		UpdateElementListXLV(i100mmLimitSelected, 2, cmp);
		// For Blind
		int iBlindLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT).getValue().toString());
		UpdateElementListXLV(iBlindLimitSelected, 3, cmp);
		// For Breaker
		int iBreakerLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT).getValue().toString());
		UpdateElementListXLV(iBreakerLimitSelected, 3, cmp);
		// For Spare
		int iSpareLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT).getValue().toString());
		UpdateElementListXLV(iSpareLimitSelected, 3, cmp);

		// Getting and setting drop down list values
		int iXR0Limit = GetMaxLimitInDropDownXLV(1, cmp, iXR0LimitSelected);
		int iXR1Limit = GetMaxLimitInDropDownXLV(2, cmp, iXR1LimitSelected);
		int iXR2Limit = GetMaxLimitInDropDownXLV(4, cmp, iXR2LimitSelected);
		int iXR3Limit = GetMaxLimitInDropDownXLV(4, cmp, iXR3LimitSelected);
		int i50mmLimit = GetMaxLimitInDropDownXLV(1, cmp, i50mmLimitSelected);
		int i100mmLimit = GetMaxLimitInDropDownXLV(2, cmp, i100mmLimitSelected);
		int iBlindLimit = GetMaxLimitInDropDownXLV(3, cmp, iBlindLimitSelected);
		int iBreakerLimit = GetMaxLimitInDropDownXLV(3, cmp, iBreakerLimitSelected);
		int iSpareLimit = GetMaxLimitInDropDownXLV(3, cmp, iSpareLimitSelected);

		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0_LIMIT).setValue(String.valueOf(iXR0Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1_LIMIT).setValue(String.valueOf(iXR1Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2_LIMIT).setValue(String.valueOf(iXR2Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3_LIMIT).setValue(String.valueOf(iXR3Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_50MM_LIMIT).setValue(String.valueOf(i50mmLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_100MM_LIMIT).setValue(String.valueOf(i100mmLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT_LIMIT).setValue(String.valueOf(iBlindLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT_LIMIT).setValue(String.valueOf(iBreakerLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT_LIMIT).setValue(String.valueOf(iSpareLimit));

	}

	/**
	 * Calculates the available and reserved values for the drop down
	 **/
	private void UpdateElementListXLV(int iLimit, int iDeviceSize, Component cmp) {
		int iCounter = iLimit;
		if (iDeviceSize == 1) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				Element objCurrentElement = listElement.get(i);
				if (objCurrentElement.isAvailable) {
					objCurrentElement.isAvailable = false;
					objCurrentElement.isReserved = true;
					iCounter = iCounter - 1;
				}
			}
		} else if (iDeviceSize == 2) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 5 && i < (listElement.size() - 1)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		} else if (iDeviceSize == 3) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 8 && i < (listElement.size() - 2)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						objNextNextElement.isAvailable = false;
						objNextNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		} else if (iDeviceSize == 4) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 10 && i < (listElement.size() - 3)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						objNextNextElement.isAvailable = false;
						objNextNextElement.isReserved = true;
						objNextNextNextElement.isAvailable = false;
						objNextNextNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		} else if (iDeviceSize == 5) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 10 && i < (listElement.size() - 4)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					Element objNextNextNextNextElement = listElement.get(i + 4);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable && objNextNextNextNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						objNextNextElement.isAvailable = false;
						objNextNextElement.isReserved = true;
						objNextNextNextElement.isAvailable = false;
						objNextNextNextElement.isReserved = true;
						objNextNextNextNextElement.isAvailable = false;
						objNextNextNextNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		}

	}

	/**
	 * Calculates the max value for the limit variables
	 **/
	private int GetMaxLimitInDropDownXLV(int iDeviceSize, Component cmp, int iAlreadySelectedValue) {
		int iMaxNumber = 0;
		if (iDeviceSize == 1) {
			for (int i = 0; i < listElement.size(); i++) {
				Element objCurrentElement = listElement.get(i);
				if (objCurrentElement.isAvailable) {
					iMaxNumber = iMaxNumber + 1;
				}
			}
		} else if (iDeviceSize == 2) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 5 && i < (listElement.size() - 1)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 1;
					}
				}
			}
		} else if (iDeviceSize == 3) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 8 && i < (listElement.size() - 2)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 2;
					}
				}
			}
		} else if (iDeviceSize == 4) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 10 && i < (listElement.size() - 3)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 3;
					}
				}
			}
		} else if (iDeviceSize == 5) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 10 && i < (listElement.size() - 4)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					Element objNextNextNextNextElement = listElement.get(i + 4);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable && objNextNextNextNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 4;
					}
				}
			}
		}

		return iMaxNumber + iAlreadySelectedValue;
	}

	// XLV
	private void traverseKitPositionInRackXLVFourPoles(Component cmp) {
		IdentifyEmptySlotsWithPoles(cmp);
		// For XR0
		int iXR0LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0).getValue().toString());
		UpdateElementListXLV(iXR0LimitSelected, 2, cmp);
		// For XR1
		int iXR1LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1).getValue().toString());
		UpdateElementListXLV(iXR1LimitSelected, 3, cmp);
		// For XR2
		int iXR2LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2).getValue().toString());
		UpdateElementListXLV(iXR2LimitSelected, 5, cmp);
		// For NH3
		int iXR3LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3).getValue().toString());
		UpdateElementListXLV(iXR3LimitSelected, 5, cmp);
		// For 50mm
		int i50mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString());
		UpdateElementListXLV(i50mmLimitSelected, 1, cmp);
		// For 100mm
		int i100mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_100MM).getValue().toString());
		UpdateElementListXLV(i100mmLimitSelected, 2, cmp);
		// For Blind
		int iBlindLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT).getValue().toString());
		UpdateElementListXLV(iBlindLimitSelected, 3, cmp);
		// For Breaker
		int iBreakerLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT).getValue().toString());
		UpdateElementListXLV(iBreakerLimitSelected, 3, cmp);
		// For Spare
		int iSpareLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT).getValue().toString());
		UpdateElementListXLV(iSpareLimitSelected, 3, cmp);

		// Getting and setting drop down list values
		int iXR0Limit = GetMaxLimitInDropDownXLV(2, cmp, iXR0LimitSelected);
		int iXR1Limit = GetMaxLimitInDropDownXLV(3, cmp, iXR1LimitSelected);
		int iXR2Limit = GetMaxLimitInDropDownXLV(5, cmp, iXR2LimitSelected);
		int iXR3Limit = GetMaxLimitInDropDownXLV(5, cmp, iXR3LimitSelected);
		int i50mmLimit = GetMaxLimitInDropDownXLV(1, cmp, i50mmLimitSelected);
		int i100mmLimit = GetMaxLimitInDropDownXLV(2, cmp, i100mmLimitSelected);
		int iBlindLimit = GetMaxLimitInDropDownXLV(3, cmp, iBlindLimitSelected);
		int iBreakerLimit = GetMaxLimitInDropDownXLV(3, cmp, iBreakerLimitSelected);
		int iSpareLimit = GetMaxLimitInDropDownXLV(3, cmp, iSpareLimitSelected);

		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0_LIMIT).setValue(String.valueOf(iXR0Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1_LIMIT).setValue(String.valueOf(iXR1Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2_LIMIT).setValue(String.valueOf(iXR2Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3_LIMIT).setValue(String.valueOf(iXR3Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_50MM_LIMIT).setValue(String.valueOf(i50mmLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_100MM_LIMIT).setValue(String.valueOf(i100mmLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT_LIMIT).setValue(String.valueOf(iBlindLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT_LIMIT).setValue(String.valueOf(iBreakerLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT_LIMIT).setValue(String.valueOf(iSpareLimit));

	}

	// XLH
	private void traverseKitPositionInRackXLH(Component cmp) {
		IdentifyEmptySlots(cmp);
		// For XR0
		int iXR0LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0).getValue().toString());
		UpdateElementListXLH(iXR0LimitSelected, 1, cmp);
		// For XR1
		int iXR1LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1).getValue().toString());
		UpdateElementListXLH(iXR1LimitSelected, 2, cmp);
		// For XR2
		int iXR2LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2).getValue().toString());
		UpdateElementListXLH(iXR2LimitSelected, 4, cmp);
		// For NH3
		int iXR3LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3).getValue().toString());
		UpdateElementListXLH(iXR3LimitSelected, 4, cmp);
		// For 50mm
		int i50mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString());
		UpdateElementListXLH(i50mmLimitSelected, 1, cmp);
		// For Blind
		int iBlindLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT).getValue().toString());
		UpdateElementListXLH(iBlindLimitSelected, 3, cmp);
		// For Breaker
		int iBreakerLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT).getValue().toString());
		UpdateElementListXLH(iBreakerLimitSelected, 3, cmp);
		// For Spare
		int iSpareLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT).getValue().toString());
		UpdateElementListXLH(iSpareLimitSelected, 3, cmp);

		// Getting and setting drop down list values
		int iXR0Limit = GetMaxLimitInDropDownXLH(1, cmp, iXR0LimitSelected);
		int iXR1Limit = GetMaxLimitInDropDownXLH(2, cmp, iXR1LimitSelected);
		int iXR2Limit = GetMaxLimitInDropDownXLH(4, cmp, iXR2LimitSelected);
		int iXR3Limit = GetMaxLimitInDropDownXLH(4, cmp, iXR3LimitSelected);
		int i50mmLimit = GetMaxLimitInDropDownXLH(1, cmp, i50mmLimitSelected);
		int iBlindLimit = GetMaxLimitInDropDownXLH(3, cmp, iBlindLimitSelected);
		int iBreakerLimit = GetMaxLimitInDropDownXLH(3, cmp, iBreakerLimitSelected);
		int iSpareLimit = GetMaxLimitInDropDownXLH(3, cmp, iSpareLimitSelected);

		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0_LIMIT).setValue(String.valueOf(iXR0Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1_LIMIT).setValue(String.valueOf(iXR1Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2_LIMIT).setValue(String.valueOf(iXR2Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3_LIMIT).setValue(String.valueOf(iXR3Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_50MM_LIMIT).setValue(String.valueOf(i50mmLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT_LIMIT).setValue(String.valueOf(iBlindLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT_LIMIT).setValue(String.valueOf(iBreakerLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT_LIMIT).setValue(String.valueOf(iSpareLimit));

	}

	// XLH with 4 poles
	private void traverseKitPositionInRackXLHFourPoles(Component cmp) {
		IdentifyEmptySlotsWithPoles(cmp);
		// For XR0
		int iXR0LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0).getValue().toString());
		UpdateElementListXLH(iXR0LimitSelected, 2, cmp);
		// For XR1
		int iXR1LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1).getValue().toString());
		UpdateElementListXLH(iXR1LimitSelected, 3, cmp);
		// For XR2
		int iXR2LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2).getValue().toString());
		UpdateElementListXLH(iXR2LimitSelected, 5, cmp);
		// For NH3
		int iXR3LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3).getValue().toString());
		UpdateElementListXLH(iXR3LimitSelected, 5, cmp);
		// For 50mm
		int i50mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString());
		UpdateElementListXLH(i50mmLimitSelected, 1, cmp);
		// For Blind
		int iBlindLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT).getValue().toString());
		UpdateElementListXLH(iBlindLimitSelected, 3, cmp);
		// For Breaker
		int iBreakerLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT).getValue().toString());
		UpdateElementListXLH(iBreakerLimitSelected, 3, cmp);
		// For Spare
		int iSpareLimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT).getValue().toString());
		UpdateElementListXLH(iSpareLimitSelected, 3, cmp);

		// Getting and setting drop down list values
		int iXR0Limit = GetMaxLimitInDropDownXLH(2, cmp, iXR0LimitSelected);
		int iXR1Limit = GetMaxLimitInDropDownXLH(3, cmp, iXR1LimitSelected);
		int iXR2Limit = GetMaxLimitInDropDownXLH(5, cmp, iXR2LimitSelected);
		int iXR3Limit = GetMaxLimitInDropDownXLH(5, cmp, iXR3LimitSelected);
		int i50mmLimit = GetMaxLimitInDropDownXLH(1, cmp, i50mmLimitSelected);
		int iBlindLimit = GetMaxLimitInDropDownXLH(3, cmp, iBlindLimitSelected);
		int iBreakerLimit = GetMaxLimitInDropDownXLH(3, cmp, iBreakerLimitSelected);
		int iSpareLimit = GetMaxLimitInDropDownXLH(3, cmp, iSpareLimitSelected);

		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0_LIMIT).setValue(String.valueOf(iXR0Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1_LIMIT).setValue(String.valueOf(iXR1Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2_LIMIT).setValue(String.valueOf(iXR2Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3_LIMIT).setValue(String.valueOf(iXR3Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_50MM_LIMIT).setValue(String.valueOf(i50mmLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_BLINDKIT_LIMIT).setValue(String.valueOf(iBlindLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_BREAKERKIT_LIMIT).setValue(String.valueOf(iBreakerLimit));
		cmp.getStateVector().getVar(DEVICE_QTY_SPAREKIT_LIMIT).setValue(String.valueOf(iSpareLimit));

	}

	/**
	 * Calculates the available and reserved values for the drop down
	 **/
	private void UpdateElementListXLH(int iLimit, int iDeviceSize, Component cmp) {
		int iCounter = iLimit;
		if (iDeviceSize == 1) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				Element objCurrentElement = listElement.get(i);
				if (objCurrentElement.isAvailable) {
					objCurrentElement.isAvailable = false;
					objCurrentElement.isReserved = true;
					iCounter = iCounter - 1;
				}
			}
		} else if (iDeviceSize == 2) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 4 && i < (listElement.size() - 1)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		} else if (iDeviceSize == 3) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 7 && i < (listElement.size() - 2)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						objNextNextElement.isAvailable = false;
						objNextNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		} else if (iDeviceSize == 4) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 9 && i < (listElement.size() - 3)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						objNextNextElement.isAvailable = false;
						objNextNextElement.isReserved = true;
						objNextNextNextElement.isAvailable = false;
						objNextNextNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		} else if (iDeviceSize == 5) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 9 && i < (listElement.size() - 4)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					Element objNextNextNextNextElement = listElement.get(i + 4);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable && objNextNextNextNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						objNextNextElement.isAvailable = false;
						objNextNextElement.isReserved = true;
						objNextNextNextElement.isAvailable = false;
						objNextNextNextElement.isReserved = true;
						objNextNextNextNextElement.isAvailable = false;
						objNextNextNextNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		}

	}

	/**
	 * Calculates the max value for the limit variables
	 **/
	private int GetMaxLimitInDropDownXLH(int iDeviceSize, Component cmp, int iAlreadySelectedValue) {
		int iMaxNumber = 0;
		if (iDeviceSize == 1) {
			for (int i = 0; i < listElement.size(); i++) {
				Element objCurrentElement = listElement.get(i);
				if (objCurrentElement.isAvailable) {
					iMaxNumber = iMaxNumber + 1;
				}
			}
		} else if (iDeviceSize == 2) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 4 && i < (listElement.size() - 1)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 1;
					}
				}
			}
		} else if (iDeviceSize == 3) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 7 && i < (listElement.size() - 2)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 2;
					}
				}
			}
		} else if (iDeviceSize == 4) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 9 && i < (listElement.size() - 3)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 3;
					}
				}
			}
		} else if (iDeviceSize == 5) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 9 && i < (listElement.size() - 4)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					Element objNextNextNextNextElement = listElement.get(i + 4);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable && objNextNextNextNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 4;
					}
				}
			}
		}

		return iMaxNumber + iAlreadySelectedValue;
	}

	// SwitchFuse
	private void traverseKitPositionInRackSwitchFuse(Component cmp) {
		IdentifyEmptySlots(cmp);
		// For XR0
		int iXR0LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0).getValue().toString());
		UpdateElementListSwitchFuse(iXR0LimitSelected, 1, cmp);
		// For XR1
		int iXR1LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1).getValue().toString());
		UpdateElementListSwitchFuse(iXR1LimitSelected, 2, cmp);
		// For XR2
		int iXR2LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2).getValue().toString());
		UpdateElementListSwitchFuse(iXR2LimitSelected, 4, cmp);
		// For NH3
		int iXR3LimitSelected = Integer
				.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3).getValue().toString());
		UpdateElementListSwitchFuse(iXR3LimitSelected, 4, cmp);
		// For 50mm
		int i50mmLimitSelected = Integer.parseInt(cmp.getStateVector().getVar(DEVICE_QTY_50MM).getValue().toString());
		UpdateElementListSwitchFuse(i50mmLimitSelected, 1, cmp);

		// Getting and setting drop down list values
		int iXR0Limit = GetMaxLimitInDropDownSwitchFuse(1, cmp, iXR0LimitSelected);
		int iXR1Limit = GetMaxLimitInDropDownSwitchFuse(2, cmp, iXR1LimitSelected);
		int iXR2Limit = GetMaxLimitInDropDownSwitchFuse(4, cmp, iXR2LimitSelected);
		int iXR3Limit = GetMaxLimitInDropDownSwitchFuse(4, cmp, iXR3LimitSelected);
		int i50mmLimit = GetMaxLimitInDropDownSwitchFuse(1, cmp, i50mmLimitSelected);

		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR0_LIMIT).setValue(String.valueOf(iXR0Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR1_LIMIT).setValue(String.valueOf(iXR1Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR2_LIMIT).setValue(String.valueOf(iXR2Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_SLIMLINEXR3_LIMIT).setValue(String.valueOf(iXR3Limit));
		cmp.getStateVector().getVar(DEVICE_QTY_50MM_LIMIT).setValue(String.valueOf(i50mmLimit));

	}

	/**
	 * Calculates the available and reserved values for the drop down
	 **/
	private void UpdateElementListSwitchFuse(int iLimit, int iDeviceSize, Component cmp) {
		int iCounter = iLimit;
		if (iDeviceSize == 1) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				Element objCurrentElement = listElement.get(i);
				if (objCurrentElement.isAvailable) {
					objCurrentElement.isAvailable = false;
					objCurrentElement.isReserved = true;
					iCounter = iCounter - 1;
				}
			}
		} else if (iDeviceSize == 2) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 4 && i < (listElement.size() - 1)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		} else if (iDeviceSize == 4) {
			for (int i = 0; i < listElement.size(); i++) {
				if (iCounter == 0) {
					break;
				}
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 6 && i < (listElement.size() - 3)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable) {
						objCurrentElement.isAvailable = false;
						objCurrentElement.isReserved = true;
						objNextElement.isAvailable = false;
						objNextElement.isReserved = true;
						objNextNextElement.isAvailable = false;
						objNextNextElement.isReserved = true;
						objNextNextNextElement.isAvailable = false;
						objNextNextNextElement.isReserved = true;
						iCounter = iCounter - 1;
					}
				}
			}
		}

	}

	/**
	 * Calculates the max value for the limit variables
	 **/
	private int GetMaxLimitInDropDownSwitchFuse(int iDeviceSize, Component cmp, int iAlreadySelectedValue) {
		int iMaxNumber = 0;
		if (iDeviceSize == 1) {
			for (int i = 0; i < listElement.size(); i++) {
				Element objCurrentElement = listElement.get(i);
				if (objCurrentElement.isAvailable) {
					iMaxNumber = iMaxNumber + 1;
				}
			}
		} else if (iDeviceSize == 2) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 4 && i < (listElement.size() - 1)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 1;
					}
				}
			}
		} else if (iDeviceSize == 4) {
			for (int i = 0; i < listElement.size(); i++) {
				String str = ELEMENT + String.valueOf(i + 1);
				int iNoOfPossibleDevices = cmp.getStateVector().getVar(str).getLegalValues().size();
				if (iNoOfPossibleDevices >= 6 && i < (listElement.size() - 3)) {
					Element objCurrentElement = listElement.get(i);
					Element objNextElement = listElement.get(i + 1);
					Element objNextNextElement = listElement.get(i + 2);
					Element objNextNextNextElement = listElement.get(i + 3);
					if (objCurrentElement.isAvailable && objNextElement.isAvailable && objNextNextElement.isAvailable
							&& objNextNextNextElement.isAvailable) {
						iMaxNumber = iMaxNumber + 1;
						i = i + 3;
					}
				}
			}
		}

		return iMaxNumber + iAlreadySelectedValue;
	}

	/**
	 * Identifies the empty slots
	 **/
	private void IdentifyEmptySlots(Component cmp) {
		listElement = new ArrayList<>();
		int iTotalSlots = getMaxAvailableSlots(cmp);
		boolean doubleWidthDevice = false;
		boolean tripleWidthDevice = false;
		boolean quadrupalWidthDevice = false;
		int countTriple = 2;
		int countQuad = 3;
		for (int i = 1; i <= iTotalSlots; i++) {
			Element objElement = new Element();
			String str = ELEMENT + i;
			if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE) && !doubleWidthDevice
					&& !tripleWidthDevice && !quadrupalWidthDevice) {
				objElement.isInserted = false;
				objElement.isAvailable = true;
				objElement.isReserved = false;
			} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE)
					&& doubleWidthDevice) {
				objElement.isInserted = true;
				objElement.isAvailable = false;
				objElement.isReserved = false;
				doubleWidthDevice = false;
			} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE)
					&& tripleWidthDevice) {
				objElement.isInserted = true;
				objElement.isAvailable = false;
				objElement.isReserved = false;
				countTriple--;
				if (countTriple == 0) {
					tripleWidthDevice = false;
					countTriple = 2;
				}
			} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE)
					&& quadrupalWidthDevice) {
				objElement.isInserted = true;
				objElement.isAvailable = false;
				objElement.isReserved = false;
				countQuad--;
				if (countQuad == 0) {
					quadrupalWidthDevice = false;
					countQuad = 3;
				}
			} else {
				String element = cmp.getStateVector().getVar(str).getValue().getID();

				if (element.matches(DEVICE_COMPENSATOR_50) || element.matches(DEVICE_SLIMLINE_50)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
				} else if (element.matches(DEVICE_COMPENSATOR_100) || element.matches(DEVICE_SLIMLINE_100)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					doubleWidthDevice = true;
				} else if (element.matches(KIT_XLINE_COMP_SPARE_RBBS) || element.matches(KIT_XLINE_COMP_BLIND_RBBS)
						|| element.matches(KIT_XLINE_COMP_BREAKER_RBBS)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					tripleWidthDevice = true;
				} else if (element.matches(DEVICE_SLIMLINE_XR2) || element.matches(DEVICE_SLIMLINE_XR3)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					quadrupalWidthDevice = true;
				} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NOT_SET)) {
					objElement.isInserted = false;
					objElement.isAvailable = true;
					objElement.isReserved = false;
				}
			}
			listElement.add(objElement);
		}
	}

	/**
	 * Identifies the empty slots with poles
	 **/
	public void IdentifyEmptySlotsWithPoles(Component cmp) {
		listElement = new ArrayList<>();
		int iTotalSlots = getMaxAvailableSlots(cmp);
		boolean doubleWidthDevice = false;
		boolean tripleWidthDevice = false;
		boolean quadrupalWidthDevice = false;
		boolean fiveWidthDevice = false;
		int countTriple = 2;
		int countQuad = 3;
		int countFive = 4;
		for (int i = 1; i <= iTotalSlots; i++) {
			Element objElement = new Element();
			String str = ELEMENT + i;
			if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE) && !doubleWidthDevice
					&& !tripleWidthDevice && !quadrupalWidthDevice && !fiveWidthDevice) {
				objElement.isInserted = false;
				objElement.isAvailable = true;
				objElement.isReserved = false;
			} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE)
					&& doubleWidthDevice) {
				objElement.isInserted = true;
				objElement.isAvailable = false;
				objElement.isReserved = false;
				doubleWidthDevice = false;
			} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE)
					&& tripleWidthDevice) {
				objElement.isInserted = true;
				objElement.isAvailable = false;
				objElement.isReserved = false;
				countTriple--;
				if (countTriple == 0) {
					tripleWidthDevice = false;
					countTriple = 2;
				}
			} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE)
					&& quadrupalWidthDevice) {
				objElement.isInserted = true;
				objElement.isAvailable = false;
				objElement.isReserved = false;
				countQuad--;
				if (countQuad == 0) {
					quadrupalWidthDevice = false;
					countQuad = 3;
				}
			} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NONE)
					&& fiveWidthDevice) {
				objElement.isInserted = true;
				objElement.isAvailable = false;
				objElement.isReserved = false;
				countFive--;
				if (countFive == 0) {
					fiveWidthDevice = false;
					countFive = 4;
				}

			} else {
				String element = cmp.getStateVector().getVar(str).getValue().getID();

				if (element.matches(DEVICE_COMPENSATOR_50)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
				} else if (element.matches(DEVICE_SLIMLINE_50) && cmp.getStateVector().getVar(DBBS_POLES).getValue()
						.toString().equalsIgnoreCase(THREE_POLES)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
				} else if (element.matches(DEVICE_SLIMLINE_50) && !cmp.getStateVector().getVar(DBBS_POLES).getValue()
						.toString().equalsIgnoreCase(THREE_POLES)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					doubleWidthDevice = true;
				} else if (element.matches(DEVICE_COMPENSATOR_100)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					doubleWidthDevice = true;
				} else if (element.matches(DEVICE_SLIMLINE_100) && cmp.getStateVector().getVar(DBBS_POLES).getValue()
						.toString().equalsIgnoreCase(THREE_POLES)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					doubleWidthDevice = true;
				} else if (element.matches(DEVICE_SLIMLINE_100) && !cmp.getStateVector().getVar(DBBS_POLES).getValue()
						.toString().equalsIgnoreCase(THREE_POLES)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					tripleWidthDevice = true;
				} else if (element.matches(KIT_XLINE_COMP_SPARE_RBBS) || element.matches(KIT_XLINE_COMP_BLIND_RBBS)
						|| element.matches(KIT_XLINE_COMP_BREAKER_RBBS)) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					tripleWidthDevice = true;
				} else if (cmp.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)
						&& (element.matches(DEVICE_SLIMLINE_XR2) || element.matches(DEVICE_SLIMLINE_XR3))) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					quadrupalWidthDevice = true;
				} else if (!cmp.getStateVector().getVar(DBBS_POLES).getValue().toString().equalsIgnoreCase(THREE_POLES)
						&& (element.matches(DEVICE_SLIMLINE_XR2) || element.matches(DEVICE_SLIMLINE_XR3))) {
					objElement.isInserted = true;
					objElement.isAvailable = false;
					objElement.isReserved = false;
					fiveWidthDevice = true;
				} else if (cmp.getStateVector().getVar(str).getValue().toString().equalsIgnoreCase(NOT_SET)) {
					objElement.isInserted = false;
					objElement.isAvailable = true;
					objElement.isReserved = false;
				}
			}
			listElement.add(objElement);
		}
	}

}
