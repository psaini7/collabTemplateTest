package com.virtubuild.services.abbeconf.mbbsposition;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.ComponentState;
import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.RackController;
import com.virtubuild.core.frame.FrameControllerData.FrameControllerType;
import com.virtubuild.services.abbeconf.frames.ModelNames;

public class MbbsN185MoveDownAutoCompleter {
	private static final Logger logger = LoggerFactory.getLogger(MbbsN185MoveDownAutoCompleter.class);
	private Configuration configurator;
	private static final String SWITCHBOARD_TOA = "switchboard_n185";
	// private static final String WIDTH_COLUMN_EXTERNAL = "width_column_external";
	private List<String> columnsToDelete = new LinkedList<String>(); // columns marked for deletion
	

	/**
	 * Constructor
	 * 
	 * @param cmp
	 *            List of all the available Components
	 * @param configurator
	 *            manager object with functionality for accessing the model
	 *            configuration
	 */
	public MbbsN185MoveDownAutoCompleter(Configuration configurator) {
		this.configurator = configurator;
	}

	/*
	 * Component rackComponent; ComponentState compState = (ComponentState)
	 * rackComponent; RackController controller = (RackController)
	 * compState.getFrameController(FrameControllerType.RACK);
	 */

	/**
	 * Initilize all the required methods
	 */
	String mainbus = null;

	public void load() {

		if (configurator.getComponentFromToa(SWITCHBOARD_TOA, 0) != null) {
			mainbus = configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector()
					.getVar("main_busbar_position").getValue().toString();
			getColumnKitFrame();
			if (configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector().getVar("height").getValue()
					.toString().matches("1800")) {
				reduceHeight1900();
				if (!columnsToDelete.isEmpty()) {
					deleteKits();
					moveDown1900();
				}
				else
				{
					moveDown1900();
				}
				/*deleteKits();
				moveDown1900();*/
			} else if (configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector().getVar("height").getValue()
					.toString().matches("2100")) {
				reduceHeight2200();
				if (!columnsToDelete.isEmpty()) {
					deleteKits();
					moveDown2200();
				}
				else
				{
					moveDown2200();
				}
			}
		}
	}

	private void reduceHeight1900() {
		/*String mainbus = configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector()
				.getVar("main_busbar_position").getValue().toString();
*/
		for (Component columnKitFrame : getColumnKitFrame()) {
			ComponentState compState = (ComponentState) columnKitFrame;
			RackController controller = (RackController) compState.getFrameController(FrameControllerType.RACK);
			Collection<BaseComponent> list = controller.getComponents();
			for (BaseComponent component : list) {
			if (component.getID().matches("kit_touchguard_combilineN\\d+")) {
				if (component.getStateVector().getVar("kit_posy").getValue().toString().matches("4")) {
					markColumnDeletion(component);
				}
			} else if (component.getID().matches("kit_npebusbarsystem_n185\\d+")) {
				if (component.getStateVector().getVar("height").getValue().toString().matches("4")) {
					component.getStateVector().getVar("height").setValue("3");
				}
			}
			}
			/*List<Component> rackKitsList = new ArrayList<>();
			Point X1Y4 = new Point(1, 4);
			Point X2Y4 = new Point(2, 4);
			Point X3Y4 = new Point(3, 4);

			for (Component kits : getKits(rackKit)) {
				rackKitsList.add(kits);
			}*/

			/*for (Component kits : getKits(rackKit)) {
				if (kits.getID().matches("kit_npebusbarsystem_n185\\d+")) {
					kits.getStateVector().getVar("height").setValue("3");

					for (Component component : rackKitsList) {
						if (RackUtils.isOccupied(rackKit, X1Y4) == true && RackUtils.isOccupied(rackKit, X2Y4) == true
								&& RackUtils.isOccupied(rackKit, X3Y4) == true) {
							if (component.getID().matches("kit_switchdisconnector_n185\\d+") && mainbus.matches("5")) {
								moveDown1900();
							}
						} else {
							if (component.getID().matches("kit_touchguard_combilineN\\d+")) {
								int posY1 = getIntegerModelValue(component, ModelNames.KIT_POS_Y);
								if (posY1 == 4) {
									markColumnDeletion(component);
								}
							}
						}
					}
				}

			}*/

		}
	}

	private void moveDown1900() {
		configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector().getVar("main_busbar_position")
				.setValue("4");
	}


	private void reduceHeight2200() {

		System.out.println("main_barbar_value:" + mainbus);

		for (Component columnKitFrame : getColumnKitFrame()) {

			ComponentState compState = (ComponentState) columnKitFrame;
			RackController controller = (RackController) compState.getFrameController(FrameControllerType.RACK);
			Collection<BaseComponent> list = controller.getComponents();
			for (BaseComponent component : list) {
				if (mainbus.matches("7")) {
					if (component.getID().matches("kit_touchguard_combilineN\\d+")) {
						if (component.getStateVector().getVar("kit_posy").getValue().toString().matches("6")) {
							markColumnDeletion(component);
						} else if (component.getStateVector().getVar("kit_posy").getValue().toString().matches("5")) {
							if (component.getStateVector().getVar("height").getValue().toString().matches("2")) {
								component.getStateVector().getVar("height").setValue("1");
							}
						} else if (component.getStateVector().getVar("kit_posy").getValue().toString().matches("4")) {
							if (component.getStateVector().getVar("height").getValue().toString().matches("3")) {
								component.getStateVector().getVar("height").setValue("2");
							}

						}
					} else if (component.getID().matches("kit_npebusbarsystem_n185\\d+")) {
						if (component.getStateVector().getVar("height").getValue().toString().matches("4")) {
							component.getStateVector().getVar("height").setValue("3");
						}
					}
				} else if (mainbus.matches("6")) {
					if (component.getID().matches("kit_touchguard_combilineN\\d+")) {
						if (component.getStateVector().getVar("kit_posy").getValue().toString().matches("5")) {
							markColumnDeletion(component);
						} else if (component.getStateVector().getVar("kit_posy").getValue().toString().matches("4")) {
							if (component.getStateVector().getVar("height").getValue().toString().matches("2")) {
								component.getStateVector().getVar("height").setValue("1");
							}
						}
					} else if (component.getID().matches("kit_npebusbarsystem_n185\\d+")) {
						if (component.getStateVector().getVar("height").getValue().toString().matches("4")) {
							component.getStateVector().getVar("height").setValue("3");
						}
					}
				} else if (mainbus.matches("5")) {
					if (component.getID().matches("kit_touchguard_combilineN\\d+")) {
						if (component.getStateVector().getVar("kit_posy").getValue().toString().matches("4")) {
							markColumnDeletion(component);
						}
					} else if (component.getID().matches("kit_npebusbarsystem_n185\\d+")) {
						if (component.getStateVector().getVar("height").getValue().toString().matches("4")) {
							component.getStateVector().getVar("height").setValue("3");
						}
					}
				}
			}

		}
	}
	

	private void moveDown2200() {
		String mainbus = configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector()
				.getVar("main_busbar_position").getValue().toString(); // 7,6,5,4 initial 5
		if (mainbus.matches("7")) {
			configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector().getVar("main_busbar_position")
					.setValue("6");
		} else if (mainbus.matches("6")) {
			configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector().getVar("main_busbar_position")
					.setValue("5");
		} else if (mainbus.matches("5")) {
			configurator.getComponentFromToa(SWITCHBOARD_TOA, 0).getStateVector().getVar("main_busbar_position")
					.setValue("4");
		}
	}


	
	private void deleteKits() {
		logger.debug("Number of columns to delete: " + columnsToDelete.size());
		for (String columnID : columnsToDelete) {
			Component column = configurator.getComponent(columnID);
			logger.debug("Deleting column " + columnID);
			column.delete();
		}
		columnsToDelete.clear();
	}

	/**
	 * mark the input column for deletion.
	 * 
	 * @param component
	 *            BaseComponent to mark for deletion
	 */
	private void markColumnDeletion(BaseComponent component) {
		String componentID = component.getID();
		// only add to delete list, if it is not already in the list.
		if (!columnsToDelete.contains(componentID)) {
			columnsToDelete.add(componentID);
		}
	}

	/**
	 * Get the frame Components of the current configuration
	 */
	public Collection<Component> getColumnKitFrame() {
		List<BaseComponent> cols = configurator.getTOAComponents(ModelNames.FRAME_COMP);
		Collection<Component> columnsKitFrame = new LinkedList<Component>();
		for (BaseComponent c : cols) {
			columnsKitFrame.add((Component) c);
		}
		return columnsKitFrame;
	}

	/**
	 * Get the width of the specified rack Component
	 */
	public int getRackWidth(Component rack) {
		return getIntegerModelValue(rack, ModelNames.RACK_AVAIL_COLUMNS);
	}

	/**
	 * Get the height of the specified rack Component
	 */
	public int getRackHeight(Component rack) {
		return getIntegerModelValue(rack, ModelNames.RACK_AVAIL_ROWS);
	}

	/**
	 * Get the kits Components for the specified column Component
	 */

	public Collection<Component> getKits(Component rack) {
		List<Component> kits = new LinkedList<Component>();
		for (Component child : rack.getChildren()) {
			// only get kit components that are legal
			if (isKitComponent(child) && isLegalPosition(child, rack)) {
				kits.add(child);
			}
		}
		return kits;
	}

	/**
	 * Get the width of the kit
	 * 
	 * @param kit
	 *            kit Component to get the width of
	 * @return width of kit
	 */
	public int getKitWidth(Component kit) {
		return getIntegerModelValue(kit, ModelNames.KIT_WIDTH);
	}

	/**
	 * Get the height of the kit
	 * 
	 * @param kit
	 *            kit Component to get the height of
	 * @return height of kit
	 */
	public int getKitHeight(Component kit) {
		return getIntegerModelValue(kit, ModelNames.KIT_HEIGHT);
	}

	/**
	 * Checks whether the component is a kit
	 * 
	 * @return Boolean true / false
	 **/
	private boolean isKitComponent(Component component) {
		String componentName = component.getTypeID();
		if (!componentName.startsWith(ModelNames.KIT_PREFIX)) {
			return false;
		}
		return true;
	}

	/**
	 * Checks whether a kit is in a legal position for calculation of frames
	 * 
	 * @param kit
	 *            kit Component to check on
	 * @return true if kit is in a legal position, false otherwise
	 */
	private boolean isLegalPosition(Component kit, Component rack) {
		int posX = getIntegerModelValue(kit, ModelNames.KIT_POS_X);
		int posY = getIntegerModelValue(kit, ModelNames.KIT_POS_Y);
		int rackWidth = getRackWidth(rack);
		int rackHeight = getRackHeight(rack);

		if (posX < 0 || posX > rackWidth || posY < 0 || posY > rackHeight) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the specified integer variable value for the specified Component
	 * 
	 * @param component
	 *            Component to get the variable value of
	 * @param variableName
	 *            Name of the variable to get the value of
	 * @return the variable value as integer, 0
	 */
	private Integer getIntegerModelValue(BaseComponent component, String variableName) {
		if (!component.getStateVector().existVar(variableName)) {
			logger.warn("No variable " + variableName + " for Component " + component.getID());
			return 0;
		}
		return Integer.parseInt(component.getStateVector().getVar(variableName).getValue().toString());
	}

}
