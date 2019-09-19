/******************************************************************
 * AutoCompleterNPE                                 *
 * Represents a Auto Completer class for insertion of 	          *
 * number of kits/devices specified by the user in the empty 	  *
 * slots for current column.		  							  *
 ******************************************************************/
package com.virtubuild.services.abbeconf.npeoffsetn185;

import java.util.List;

import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.state.StateVar;
import com.virtubuild.custom.abbeconf.general.Point;
import com.virtubuild.custom.abbeconf.utils.RackUtils;
import com.virtubuild.services.abbeconf.warning.ContradictionPopUp;

public class NpeOffsetN185Manager {

	private Configuration configurator;
	private static final String SWITCHBOARD_TOA = "switchboard_n185";
	private static final String OFFSET_N185 = "offset_n185";
	private static final String NEXTLINK = "next_link";
	private static final String PREVLINK = "prev_link";
	private static final String WIDTH_COLUMN_EXTERNAL = "width_column_external";
	private static final String BOTTOM = "bottom";
	private static final String TOP = "top";
	private static final String NO = "No";
	private static final String RIGHT = "right";
	private static final String NONE = "NONE";
	private static final String CONNECTION = "connection";
	private static final String COLUMN_CORNER_N185 = "column_corner_n185";
	private static final String SWITCH_YELLOW_BOX = "switch_yellow_box";
	private static final String IS_SET_BY_COMPLETER = "is_set_by_completer";

	/**
	 * Constructor
	 * 
	 * @param cmp          List of all the available Components
	 * @param configurator manager object with functionality for accessing the model
	 *                     configuration
	 */
	public NpeOffsetN185Manager(Configuration configurator) {
		this.configurator = configurator;
	}

	/**
	 * Initilize all the required methods
	 */
	public void run() {
		if (configurator.getComponentFromToa(SWITCHBOARD_TOA, 0) != null) {

			if (setOffsetN185PopUp()) {
				setOffsetN185WithKits1900();
				setOffsetN185WithKits2200();
				removeOffsetN185();
			}
		}
	}

	/**
	 * Gets all the component having TOA column
	 */
	private List<Component> getAllColumn() {
		return ((Component) configurator.getComponentFromToa(SWITCHBOARD_TOA, 0)).getLinkMesh();

	}

	/**
	 * Checks for valid columns
	 */
	private boolean checkToa(BaseComponent baseComponent) {
		boolean status = true;
		if (baseComponent.isTypeOfActor("column_empty_n185") || baseComponent.isTypeOfActor("column_combiline_n185")) {
			status = false;
		}
		return status;
	}

	/**
	 * Checks if racks are empty or not when the column height is set to 2200
	 */
	private boolean checkWithKitsHeight2200(BaseComponent column) {
		boolean status = false;

		if (!column.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)) {
			status = true;
		} else if (column.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)) {

			String string1 = column.getStateVector().getVar("kit_frames_n185_rbbs_1").toString();
			String string2 = column.getStateVector().getVar("kit_frames_n185_rbbs_2").toString();
			String array1[] = string1.split(":");
			String array2[] = string2.split(":");
			String curentKitFrame1 = array1[2].substring(0, array1[2].length() - 1).replaceAll("\\s", "");
			String curentKitFrame2 = array2[2].substring(0, array2[2].length() - 1).replaceAll("\\s", "");

			Component kitFrame1 = configurator.getComponent(curentKitFrame1);
			Component kitFrame2 = configurator.getComponent(curentKitFrame2);

			Point X1Y09 = new Point(1, 9);
			Point X1Y10 = new Point(1, 10);
			Point X1Y11 = new Point(1, 11);
			Point X1Y12 = new Point(1, 12);

			Point X2Y09 = new Point(2, 9);
			Point X2Y10 = new Point(2, 10);
			Point X2Y11 = new Point(2, 11);
			Point X2Y12 = new Point(2, 12);

			Point X3Y09 = new Point(3, 9);
			Point X3Y10 = new Point(3, 10);
			Point X3Y11 = new Point(3, 11);
			Point X3Y12 = new Point(3, 12);

			Point X1Y1 = new Point(1, 1);
			Point X1Y2 = new Point(1, 2);
			Point X1Y3 = new Point(1, 3);

			Point X2Y1 = new Point(2, 1);
			Point X2Y2 = new Point(2, 2);
			Point X2Y3 = new Point(2, 3);

			Point X3Y1 = new Point(3, 1);
			Point X3Y2 = new Point(3, 2);
			Point X3Y3 = new Point(3, 3);

			if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(TOP)) {

				if (column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("600")) {
					if (RackUtils.isOccupied(kitFrame1, X1Y1) || RackUtils.isOccupied(kitFrame1, X1Y2)
							|| RackUtils.isOccupied(kitFrame1, X1Y3)

							|| RackUtils.isOccupied(kitFrame1, X2Y1) || RackUtils.isOccupied(kitFrame1, X2Y2)
							|| RackUtils.isOccupied(kitFrame1, X2Y3)) {
						status = true;
					}
				} else if (column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("850")) {
					if (RackUtils.isOccupied(kitFrame1, X1Y1) || RackUtils.isOccupied(kitFrame1, X1Y2)
							|| RackUtils.isOccupied(kitFrame1, X1Y3)

							|| RackUtils.isOccupied(kitFrame1, X2Y1) || RackUtils.isOccupied(kitFrame1, X2Y2)
							|| RackUtils.isOccupied(kitFrame1, X2Y3)

							|| RackUtils.isOccupied(kitFrame1, X3Y1) || RackUtils.isOccupied(kitFrame1, X3Y2)
							|| RackUtils.isOccupied(kitFrame1, X3Y3)) {
						status = true;
					}
				} else if (column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("1100")) {
					if (RackUtils.isOccupied(kitFrame1, X1Y1) || RackUtils.isOccupied(kitFrame1, X1Y2)
							|| RackUtils.isOccupied(kitFrame1, X1Y3)

							|| RackUtils.isOccupied(kitFrame1, X2Y1) || RackUtils.isOccupied(kitFrame1, X2Y2)
							|| RackUtils.isOccupied(kitFrame1, X2Y3)

							|| RackUtils.isOccupied(kitFrame2, X1Y1) || RackUtils.isOccupied(kitFrame2, X1Y2)
							|| RackUtils.isOccupied(kitFrame2, X1Y3)

							|| RackUtils.isOccupied(kitFrame2, X2Y1) || RackUtils.isOccupied(kitFrame2, X2Y2)
							|| RackUtils.isOccupied(kitFrame2, X2Y3)) {
						status = true;
					}
				} else if (column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("1350")) {
					if (RackUtils.isOccupied(kitFrame1, X1Y1) || RackUtils.isOccupied(kitFrame1, X1Y2)
							|| RackUtils.isOccupied(kitFrame1, X1Y3)

							|| RackUtils.isOccupied(kitFrame1, X2Y1) || RackUtils.isOccupied(kitFrame1, X2Y2)
							|| RackUtils.isOccupied(kitFrame1, X2Y3)

							|| RackUtils.isOccupied(kitFrame1, X3Y1) || RackUtils.isOccupied(kitFrame1, X3Y2)
							|| RackUtils.isOccupied(kitFrame1, X3Y3)

							|| RackUtils.isOccupied(kitFrame2, X1Y1) || RackUtils.isOccupied(kitFrame2, X1Y2)
							|| RackUtils.isOccupied(kitFrame2, X1Y3)

							|| RackUtils.isOccupied(kitFrame2, X2Y1) || RackUtils.isOccupied(kitFrame2, X2Y2)
							|| RackUtils.isOccupied(kitFrame2, X2Y3)) {
						status = true;
					}
				}

			}

			else if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(BOTTOM)) {

				if (column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("600")) {
					if (RackUtils.isOccupied(kitFrame1, X1Y09) || RackUtils.isOccupied(kitFrame1, X1Y10)
							|| RackUtils.isOccupied(kitFrame1, X1Y11) || RackUtils.isOccupied(kitFrame1, X1Y12)

							|| RackUtils.isOccupied(kitFrame1, X2Y09) || RackUtils.isOccupied(kitFrame1, X2Y10)
							|| RackUtils.isOccupied(kitFrame1, X2Y11) || RackUtils.isOccupied(kitFrame1, X2Y12)) {
						status = true;
					}
				} else if (column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("850")) {
					if (RackUtils.isOccupied(kitFrame1, X1Y09) || RackUtils.isOccupied(kitFrame1, X1Y10)
							|| RackUtils.isOccupied(kitFrame1, X1Y11) || RackUtils.isOccupied(kitFrame1, X1Y12)

							|| RackUtils.isOccupied(kitFrame1, X2Y09) || RackUtils.isOccupied(kitFrame1, X2Y10)
							|| RackUtils.isOccupied(kitFrame1, X2Y11) || RackUtils.isOccupied(kitFrame1, X2Y12)

							|| RackUtils.isOccupied(kitFrame1, X3Y09) || RackUtils.isOccupied(kitFrame1, X3Y10)
							|| RackUtils.isOccupied(kitFrame1, X3Y11) || RackUtils.isOccupied(kitFrame1, X3Y12)) {
						status = true;
					}
				} else if (column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("1100")) {
					if (RackUtils.isOccupied(kitFrame1, X1Y09) || RackUtils.isOccupied(kitFrame1, X1Y10)
							|| RackUtils.isOccupied(kitFrame1, X1Y11) || RackUtils.isOccupied(kitFrame1, X1Y12)

							|| RackUtils.isOccupied(kitFrame1, X2Y09) || RackUtils.isOccupied(kitFrame1, X2Y10)
							|| RackUtils.isOccupied(kitFrame1, X2Y11) || RackUtils.isOccupied(kitFrame1, X2Y12)

							|| RackUtils.isOccupied(kitFrame2, X1Y09) || RackUtils.isOccupied(kitFrame2, X1Y10)
							|| RackUtils.isOccupied(kitFrame2, X1Y11) || RackUtils.isOccupied(kitFrame2, X1Y12)

							|| RackUtils.isOccupied(kitFrame2, X2Y09) || RackUtils.isOccupied(kitFrame2, X2Y10)
							|| RackUtils.isOccupied(kitFrame2, X2Y11) || RackUtils.isOccupied(kitFrame2, X2Y12)) {
						status = true;
					}
				} else if (column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("1350")) {
					if (RackUtils.isOccupied(kitFrame1, X1Y09) || RackUtils.isOccupied(kitFrame1, X1Y10)
							|| RackUtils.isOccupied(kitFrame1, X1Y11) || RackUtils.isOccupied(kitFrame1, X1Y12)

							|| RackUtils.isOccupied(kitFrame1, X2Y09) || RackUtils.isOccupied(kitFrame1, X2Y10)
							|| RackUtils.isOccupied(kitFrame1, X2Y11) || RackUtils.isOccupied(kitFrame1, X2Y12)

							|| RackUtils.isOccupied(kitFrame1, X3Y09) || RackUtils.isOccupied(kitFrame1, X3Y10)
							|| RackUtils.isOccupied(kitFrame1, X3Y11) || RackUtils.isOccupied(kitFrame1, X3Y12)

							|| RackUtils.isOccupied(kitFrame2, X1Y09) || RackUtils.isOccupied(kitFrame2, X1Y10)
							|| RackUtils.isOccupied(kitFrame2, X1Y11) || RackUtils.isOccupied(kitFrame2, X1Y12)

							|| RackUtils.isOccupied(kitFrame2, X2Y09) || RackUtils.isOccupied(kitFrame2, X2Y10)
							|| RackUtils.isOccupied(kitFrame2, X2Y11) || RackUtils.isOccupied(kitFrame2, X2Y12)) {
						status = true;
					}
				}
			}
		}

		return status;

	}

	/**
	 * Checks if racks are empty or not when the column height is set to 1900
	 */
	private boolean checkWithKitsHeight1900(BaseComponent column) {
		boolean status = false;

		if (!column.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)) {
			status = true;
		} else if (column.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)) {

			String string1 = column.getStateVector().getVar("kit_frames_n185_rbbs_1").toString();
			String string2 = column.getStateVector().getVar("kit_frames_n185_rbbs_2").toString();
			String array1[] = string1.split(":");
			String array2[] = string2.split(":");
			String curentkitFrame1 = array1[2].substring(0, array1[2].length() - 1).replaceAll("\\s", "");
			String curentkitFrame2 = array2[2].substring(0, array2[2].length() - 1).replaceAll("\\s", "");

			Component kitFrame1 = configurator.getComponent(curentkitFrame1);
			Component kitFrame2 = configurator.getComponent(curentkitFrame2);

			Point X1Y10 = new Point(1, 10);
			Point X1Y11 = new Point(1, 11);
			Point X1Y12 = new Point(1, 12);

			Point X2Y10 = new Point(2, 10);
			Point X2Y11 = new Point(2, 11);
			Point X2Y12 = new Point(2, 12);

			Point X3Y10 = new Point(3, 10);
			Point X3Y11 = new Point(3, 11);
			Point X3Y12 = new Point(3, 12);

			Point X1Y1 = new Point(1, 1);
			Point X1Y2 = new Point(1, 2);
			Point X1Y3 = new Point(1, 3);
			Point X1Y4 = new Point(1, 4);

			Point X2Y1 = new Point(2, 1);
			Point X2Y2 = new Point(2, 2);
			Point X2Y3 = new Point(2, 3);
			Point X2Y4 = new Point(2, 4);

			Point X3Y1 = new Point(3, 1);
			Point X3Y2 = new Point(3, 2);
			Point X3Y3 = new Point(3, 3);
			Point X3Y4 = new Point(3, 4);

			if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(BOTTOM)
					&& column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("600")) {
				if (RackUtils.isOccupied(kitFrame1, X1Y10) || RackUtils.isOccupied(kitFrame1, X1Y11)
						|| RackUtils.isOccupied(kitFrame1, X1Y12)

						|| RackUtils.isOccupied(kitFrame1, X2Y10) || RackUtils.isOccupied(kitFrame1, X2Y11)
						|| RackUtils.isOccupied(kitFrame1, X2Y12)) {
					status = true;
				}

			} else if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(BOTTOM)
					&& column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("850")) {
				if (RackUtils.isOccupied(kitFrame1, X1Y10) || RackUtils.isOccupied(kitFrame1, X1Y11)
						|| RackUtils.isOccupied(kitFrame1, X1Y12)

						|| RackUtils.isOccupied(kitFrame1, X2Y10) || RackUtils.isOccupied(kitFrame1, X2Y11)
						|| RackUtils.isOccupied(kitFrame1, X2Y12)

						|| RackUtils.isOccupied(kitFrame1, X3Y10) || RackUtils.isOccupied(kitFrame1, X3Y11)
						|| RackUtils.isOccupied(kitFrame1, X3Y12)) {
					status = true;
				}
			} else if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(BOTTOM)
					&& column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("1100")) {
				if (RackUtils.isOccupied(kitFrame1, X1Y10) || RackUtils.isOccupied(kitFrame1, X1Y11)
						|| RackUtils.isOccupied(kitFrame1, X1Y12)

						|| RackUtils.isOccupied(kitFrame1, X2Y10) || RackUtils.isOccupied(kitFrame1, X2Y11)
						|| RackUtils.isOccupied(kitFrame1, X2Y12)

						|| RackUtils.isOccupied(kitFrame2, X1Y10) || RackUtils.isOccupied(kitFrame2, X1Y11)
						|| RackUtils.isOccupied(kitFrame2, X1Y12)

						|| RackUtils.isOccupied(kitFrame2, X2Y10) || RackUtils.isOccupied(kitFrame2, X2Y11)
						|| RackUtils.isOccupied(kitFrame2, X2Y12)) {
					status = true;
				}
			} else if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(BOTTOM)
					&& column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("1350")) {
				if (RackUtils.isOccupied(kitFrame1, X1Y10) || RackUtils.isOccupied(kitFrame1, X1Y11)
						|| RackUtils.isOccupied(kitFrame1, X1Y12)

						|| RackUtils.isOccupied(kitFrame1, X2Y10) || RackUtils.isOccupied(kitFrame1, X2Y11)
						|| RackUtils.isOccupied(kitFrame1, X2Y12)

						|| RackUtils.isOccupied(kitFrame1, X3Y10) || RackUtils.isOccupied(kitFrame1, X3Y11)
						|| RackUtils.isOccupied(kitFrame1, X3Y12)

						|| RackUtils.isOccupied(kitFrame2, X1Y10) || RackUtils.isOccupied(kitFrame2, X1Y11)
						|| RackUtils.isOccupied(kitFrame2, X1Y12)

						|| RackUtils.isOccupied(kitFrame2, X2Y10) || RackUtils.isOccupied(kitFrame2, X2Y11)
						|| RackUtils.isOccupied(kitFrame2, X2Y12)) {
					status = true;
				}
			} else if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(TOP)
					&& column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("600")) {

				if (RackUtils.isOccupied(kitFrame1, X1Y1) || RackUtils.isOccupied(kitFrame1, X1Y2)
						|| RackUtils.isOccupied(kitFrame1, X1Y3) || RackUtils.isOccupied(kitFrame1, X1Y4)

						|| RackUtils.isOccupied(kitFrame1, X2Y1) || RackUtils.isOccupied(kitFrame1, X2Y2)
						|| RackUtils.isOccupied(kitFrame1, X2Y3) || RackUtils.isOccupied(kitFrame1, X2Y4)) {
					status = true;
				}
			} else if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(TOP)
					&& column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("850")) {
				if (RackUtils.isOccupied(kitFrame1, X1Y1) || RackUtils.isOccupied(kitFrame1, X1Y2)
						|| RackUtils.isOccupied(kitFrame1, X1Y3) || RackUtils.isOccupied(kitFrame1, X1Y4)

						|| RackUtils.isOccupied(kitFrame1, X2Y1) || RackUtils.isOccupied(kitFrame1, X2Y2)
						|| RackUtils.isOccupied(kitFrame1, X2Y3) || RackUtils.isOccupied(kitFrame1, X2Y4)

						|| RackUtils.isOccupied(kitFrame1, X3Y1) || RackUtils.isOccupied(kitFrame1, X3Y2)
						|| RackUtils.isOccupied(kitFrame1, X3Y3) || RackUtils.isOccupied(kitFrame1, X3Y4)) {
					status = true;
				}

			} else if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(TOP)
					&& column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("1100")) {
				if (RackUtils.isOccupied(kitFrame1, X1Y1) || RackUtils.isOccupied(kitFrame1, X1Y2)
						|| RackUtils.isOccupied(kitFrame1, X1Y3) || RackUtils.isOccupied(kitFrame1, X1Y4)

						|| RackUtils.isOccupied(kitFrame1, X2Y1) || RackUtils.isOccupied(kitFrame1, X2Y2)
						|| RackUtils.isOccupied(kitFrame1, X2Y3) || RackUtils.isOccupied(kitFrame1, X2Y4)

						|| RackUtils.isOccupied(kitFrame2, X1Y1) || RackUtils.isOccupied(kitFrame2, X1Y2)
						|| RackUtils.isOccupied(kitFrame2, X1Y3) || RackUtils.isOccupied(kitFrame2, X1Y4)

						|| RackUtils.isOccupied(kitFrame2, X2Y1) || RackUtils.isOccupied(kitFrame2, X2Y2)
						|| RackUtils.isOccupied(kitFrame2, X2Y3) || RackUtils.isOccupied(kitFrame2, X2Y4)) {
					status = true;
				}
			} else if (column.getStateVector().getVar(CONNECTION).getValue().getID().matches(TOP)
					&& column.getStateVector().getVar(WIDTH_COLUMN_EXTERNAL).getValue().getID().equals("1350")) {
				if (RackUtils.isOccupied(kitFrame1, X1Y1) || RackUtils.isOccupied(kitFrame1, X1Y2)
						|| RackUtils.isOccupied(kitFrame1, X1Y3) || RackUtils.isOccupied(kitFrame1, X1Y4)

						|| RackUtils.isOccupied(kitFrame1, X2Y1) || RackUtils.isOccupied(kitFrame1, X2Y2)
						|| RackUtils.isOccupied(kitFrame1, X2Y3) || RackUtils.isOccupied(kitFrame1, X2Y4)

						|| RackUtils.isOccupied(kitFrame1, X3Y1) || RackUtils.isOccupied(kitFrame1, X3Y2)
						|| RackUtils.isOccupied(kitFrame1, X3Y3) || RackUtils.isOccupied(kitFrame1, X3Y4)

						|| RackUtils.isOccupied(kitFrame2, X1Y1) || RackUtils.isOccupied(kitFrame2, X1Y2)
						|| RackUtils.isOccupied(kitFrame2, X1Y3) || RackUtils.isOccupied(kitFrame2, X1Y4)

						|| RackUtils.isOccupied(kitFrame2, X2Y1) || RackUtils.isOccupied(kitFrame2, X2Y2)
						|| RackUtils.isOccupied(kitFrame2, X2Y3) || RackUtils.isOccupied(kitFrame2, X2Y4)) {
					status = true;
				}
			}
		}

		return status;
	}

	/**
	 * Adds Offsets whenever required
	 */
	private void setOffsetN185WithKits1900() {
		List<Component> allColumns = getAllColumn();

		BaseComponent column1 = allColumns.get(0);

		for (BaseComponent baseComponent : allColumns) {

			if (!baseComponent.getStateVector().getVar(PREVLINK).getValue().toString().matches(NONE)) {

				Component prevColumn = baseComponent.getStateVector().getRole(PREVLINK).getComponentValue();

				// Slide 12 and 13
				if (checkToa(baseComponent) && checkToa(prevColumn)) {

					if (column1.getStateVector().getVar(CONNECTION).getValue().getID().matches(BOTTOM)) {
						if (!baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
							if (!checkWithKitsHeight1900(baseComponent)) {
								if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
										.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {
									if (baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString()
											.matches(NO)
											&& prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString()
													.matches(NO)) {

										baseComponent.getStateVector().getVar(OFFSET_N185).setValue(RIGHT);
										updateIsSetByCompleter((Component) baseComponent);
									}
								}
							}
						} else if (baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
							continue;
						}
					} else if (column1.getStateVector().getVar(CONNECTION).getValue().getID().matches(TOP)) {
						if (!baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
							if (!checkWithKitsHeight2200(baseComponent)) {
								if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
										.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {

									if (baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString()
											.matches(NO)
											&& prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString()
													.matches(NO)) {

										baseComponent.getStateVector().getVar(OFFSET_N185).setValue(RIGHT);
										updateIsSetByCompleter((Component) baseComponent);
									}
								}
							}
						} else if (baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
							continue;
						}
					}
				}
			}
		}
	}

	/**
	 * Adds Offsets whenever required
	 */
	private void setOffsetN185WithKits2200() {
		List<Component> allColumns = getAllColumn();

		BaseComponent column1 = allColumns.get(0);

		for (BaseComponent baseComponent : allColumns) {

			if (!baseComponent.getStateVector().getVar(PREVLINK).getValue().toString().matches(NONE)) {

				Component prevColumn = baseComponent.getStateVector().getRole(PREVLINK).getComponentValue();

				// Slide 14 and 15
				if (checkToa(baseComponent) && checkToa(prevColumn)) {

					if (column1.getStateVector().getVar(CONNECTION).getValue().getID().matches(BOTTOM)) {
						if (!prevColumn.isTypeOfActor(COLUMN_CORNER_N185)
								&& !baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
							if (checkWithKitsHeight1900(baseComponent)) {
								if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
										.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {
									if (baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString()
											.matches(NO)
											&& prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString()
													.matches(NO)) {

										prevColumn.getStateVector().getVar(OFFSET_N185).setValue(RIGHT);
										updateIsSetByCompleter((Component) baseComponent);
									}
								}
							}
						} else if (prevColumn.isTypeOfActor(COLUMN_CORNER_N185)
								&& !baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
							if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
									.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {
								if (baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
										&& prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString()
												.matches(NO)) {

									baseComponent.getStateVector().getVar(OFFSET_N185).setValue(RIGHT);
									updateIsSetByCompleter((Component) baseComponent);
								}
							}
						}
					} else if (column1.getStateVector().getVar(CONNECTION).getValue().getID().matches(TOP)) {
						if (!prevColumn.isTypeOfActor(COLUMN_CORNER_N185)
								&& !baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
							if (checkWithKitsHeight2200(baseComponent)) {
								if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
										.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {
									if (baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString()
											.matches(NO)
											&& prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString()
													.matches(NO)) {

										prevColumn.getStateVector().getVar(OFFSET_N185).setValue(RIGHT);
										updateIsSetByCompleter((Component) baseComponent);
									}
								}
							}
						} else if (prevColumn.isTypeOfActor(COLUMN_CORNER_N185)
								&& !baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
							if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
									.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {

								if (baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
										&& prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString()
												.matches(NO)) {

									baseComponent.getStateVector().getVar(OFFSET_N185).setValue(RIGHT);
									updateIsSetByCompleter((Component) baseComponent);
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Checks the required condition and calls the pop up class method
	 * 
	 * @param i
	 */
	// Slide 16 and 23
	private boolean setOffsetN185PopUp() {

		String multiLineMsg[] = { "There is no available space to add an offset and solve the illegal condition.,",
				"Please remove the optional kits in one of the 2 yellow areas and select again N/PE Auto Completer." };

		List<Component> allColumns = getAllColumn();
		BaseComponent column1 = allColumns.get(0);
		boolean val = false;
		for (BaseComponent baseComponent : allColumns) {

			if (!baseComponent.getStateVector().getVar(PREVLINK).getValue().toString().matches(NONE)) {
				Component prevColumn = baseComponent.getStateVector().getRole(PREVLINK).getComponentValue();

				if (column1.getStateVector().getVar(CONNECTION).getValue().getID().matches(BOTTOM)) {
					if (!baseComponent.isTypeOfActor(COLUMN_CORNER_N185)
							&& !prevColumn.isTypeOfActor(COLUMN_CORNER_N185)) {
						if (checkToa(baseComponent) && checkToa(prevColumn) && checkWithKitsHeight1900(baseComponent)
								&& checkWithKitsHeight1900(prevColumn)) {
							if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
									.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {
								if (baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
										&& prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString()
												.matches(NO)) {

									ContradictionPopUp.showPopUp(multiLineMsg);
									baseComponent.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("0");
									prevColumn.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("0");
									val = false;
									break;
								} else {
									val = true;
								}

							} else {
								val = true;
							}
						} else {
							baseComponent.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("-1");
							prevColumn.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("-1");
							val = true;
						}
					} else if (baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
						val = true;
					}
				} else if (column1.getStateVector().getVar(CONNECTION).getValue().getID().matches(TOP)) {
					if (!baseComponent.isTypeOfActor(COLUMN_CORNER_N185)
							&& !prevColumn.isTypeOfActor(COLUMN_CORNER_N185)) {
						if (checkToa(baseComponent) && checkToa(prevColumn) && checkWithKitsHeight2200(baseComponent)
								&& checkWithKitsHeight2200(prevColumn)) {
							if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
									.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {
								if (baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
										&& prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString()
												.matches(NO)) {

									ContradictionPopUp.showPopUp(multiLineMsg);
									baseComponent.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("0");
									prevColumn.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("0");
									val = false;
									break;
								} else {
									val = true;
								}
							} else {
								val = true;
							}
						} else {
							baseComponent.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("-1");
							prevColumn.getStateVector().getVar(SWITCH_YELLOW_BOX).setValue("-1");
							val = true;
						}
					} else if (baseComponent.isTypeOfActor(COLUMN_CORNER_N185)) {
						val = true;
					}
				}
			}
		}
		return val;

	}

	/**
	 * Checks for conditions and deletes required column
	 */
	private void removeOffsetN185() {
		List<Component> allColumns = getAllColumn();

		for (BaseComponent baseComponent : allColumns) {
			// slide 28 and 29
			if (baseComponent.getStateVector().getVar(NEXTLINK).getValue().toString().matches(NONE)) {

				if (!baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)) {

					Component prevColumn = baseComponent.getStateVector().getRole(PREVLINK).getComponentValue();

					if (!prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
							&& !baseComponent.getStateVector().getVar(CONNECTION).getValue()
									.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())
							|| prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
									&& baseComponent.getStateVector().getVar(CONNECTION).getValue()
											.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())
									&& !checkIsSetByUser((Component) baseComponent)) {

						baseComponent.getStateVector().getVar(OFFSET_N185).setValue(NO);
					}
				}
			}

			// slide 30 and 35
			if (!baseComponent.getStateVector().getVar(NEXTLINK).getValue().toString().matches(NONE)
					&& !baseComponent.getStateVector().getVar(PREVLINK).getValue().toString().matches(NONE)) {

				if (!baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)) {

					Component prevColumn = baseComponent.getStateVector().getRole(PREVLINK).getComponentValue();
					Component nextColumn = baseComponent.getStateVector().getRole(NEXTLINK).getComponentValue();

					if (baseComponent.getStateVector().getVar(CONNECTION).getValue()
							.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())
							&& baseComponent.getStateVector().getVar(CONNECTION).getValue()
									.equals(nextColumn.getStateVector().getVar(CONNECTION).getValue())
							&& !checkIsSetByUser((Component) baseComponent)) {

						baseComponent.getStateVector().getVar(OFFSET_N185).setValue(NO);

					}
				}
			}

			// slide 26 And 27(UPDATED)
			else if (baseComponent.getStateVector().getVar(NEXTLINK).getValue().toString().matches(NONE)) {
				if (!baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)) {

					Component prevColumn = baseComponent.getStateVector().getRole(PREVLINK).getComponentValue();

					if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
							.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())) {
						if (!prevColumn.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
								&& !checkIsSetByUser((Component) baseComponent)) {

							baseComponent.getStateVector().getVar(OFFSET_N185).setValue(NO);
						}
					} else if (baseComponent.getStateVector().getVar(CONNECTION).getValue()
							.equals(prevColumn.getStateVector().getVar(CONNECTION).getValue())
							&& !checkIsSetByUser((Component) baseComponent)) {

						baseComponent.getStateVector().getVar(OFFSET_N185).setValue(NO);

					}
				}
			}

			// slide 24 and 25(UPDATED)
			else if (baseComponent.getStateVector().getVar(PREVLINK).getValue().toString().matches(NONE)) {
				if (!baseComponent.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)) {

					Component nextColumn = baseComponent.getStateVector().getRole(NEXTLINK).getComponentValue();
					if (!baseComponent.getStateVector().getVar(CONNECTION).getValue()
							.equals(nextColumn.getStateVector().getVar(CONNECTION).getValue())) {
						if (!nextColumn.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
								&& !checkIsSetByUser((Component) baseComponent)) {

							baseComponent.getStateVector().getVar(OFFSET_N185).setValue(NO);
						}
					} else if (baseComponent.getStateVector().getVar(CONNECTION).getValue()
							.equals(nextColumn.getStateVector().getVar(CONNECTION).getValue())
							&& !checkIsSetByUser((Component) baseComponent)) {

						baseComponent.getStateVector().getVar(OFFSET_N185).setValue(NO);

					} else if (nextColumn.getStateVector().getVar(OFFSET_N185).getValue().toString().matches(NO)
							&& baseComponent.getStateVector().getVar(CONNECTION).getValue()
									.equals(nextColumn.getStateVector().getVar(CONNECTION).getValue())
							&& !checkIsSetByUser((Component) baseComponent)) {

						baseComponent.getStateVector().getVar(OFFSET_N185).setValue(NO);

					}
				}
			}

		}
	}

	/**
	 * Checks if the offset variable is user set or completer set
	 * 
	 * @return
	 */
	private boolean checkIsSetByUser(Component cmp) {
		boolean setByUser = false;
		String isSetByCompleter = cmp.getStateVector().getVar(IS_SET_BY_COMPLETER).getValue().getID();
		if (isSetByCompleter.equalsIgnoreCase("false") && checkVariableIsUserSet(cmp)) {
			setByUser = true;
		}
		return setByUser;
	}

	/**
	 * Updates the isSetByCompleter to true
	 * 
	 * @return
	 */
	private void updateIsSetByCompleter(Component cmp) {
		cmp.getStateVector().getVar(IS_SET_BY_COMPLETER).setValue("true");
	}

	private boolean checkVariableIsUserSet(Component cmp) {
		StateVar offset = cmp.getStateVector().getVar(OFFSET_N185);
		return offset.isUserSet();
	}

}
