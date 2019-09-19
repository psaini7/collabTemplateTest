package com.virtubuild.services.abbeconf.frames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.api.BaseComponent;
import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.StateControl;
import com.virtubuild.custom.abbeconf.general.Orientation;
import com.virtubuild.custom.abbeconf.general.Point;
import com.virtubuild.custom.abbeconf.utils.VariableUtils;

/******************************************************************
 * Configuration Manager - for communication with configuration 
 * Responsible for retrieving and updating configuration data.
 * Relies on the ModelNames class for model specific names.
 ******************************************************************/
class ConfigurationManager {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
	private Configuration configuration;

	private int profileIndex = 1;
	private int frameIndex = 1;
	private String currentColumnID;
	private int countTopMountingRail = 0;
	private int countBottomMountingRail = 0;
	List<Integer> listFrameTopMountingRail = new ArrayList<>();
	List<Integer> listFrameBottomMountingRail = new ArrayList<>();

	private boolean allFramesLegal = true; // whether the frame calculation resulted in any illegal frames

	public ConfigurationManager(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Get the frame Components of the current configuration
	 */
	public Collection<Component> getColumns() {
		List<BaseComponent> cols = configuration.getTOAComponents(ModelNames.FRAME_COMP);
		Collection<Component> columns = new LinkedList<>();
		for (BaseComponent c : cols) {
			columns.add((Component) c);
		}
		return columns;
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
		List<Component> kits = new LinkedList<>();
		
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
	 * Gets the position of the kit as the position of the bottom left corner of the
	 * Frame
	 * 
	 * @param kit
	 *            kitComponent to get the position from
	 * @return FramePoint containing the kit position as the position of the bottom
	 *         left corner
	 */
	public Point getKitPosition(Component kit) {
	    int x = getIntegerModelValue( kit, ModelNames.KIT_POS_X);
	    int y = getIntegerModelValue(kit, ModelNames.KIT_POS_Y);
	    return new Point(x,y);
//		Point topLeftPos = calcTopLeftKitRackPosition(kit);
//		return translateRackSlotToFrameRackSlot(topLeftPos, getRackHeight((Component) kit.getParent()));
	}

	/**
	 * Gets the id of the input kit Component
	 * 
	 * @param kit
	 *            Component to get id for
	 * @return the id of the kit Component
	 */
	public String getKitId(Component kit) {
		return kit.getID();
	}

	/**
	 * Gets the width of the slots of the input rack Component
	 */
	public int getSlotWidth(Component rack) {
		return getIntegerModelValue(rack, ModelNames.RACK_COLUMN_WIDTH);
	}

	/**
	 * Gets the height of the slot of the input rack Component
	 */
	public int getSlotHeight(Component rack) {
		return getIntegerModelValue(rack, ModelNames.RACK_ROW_HEIGHT);
	}

	/**
	 * Resets all frame lengths and positions
	 * 
	 * @param rack
	 *            rack Component to clear frames on
	 */
	public void clearFrames(Component rack) {
		if (rack == null) {
			return;
		}
		for (StateControl var : rack.getStateVector().getAllVariables()) {
			// clear frames
			if (isVariableToClear(var)) {
				var.setValue("0");
			}
		}
	}

	/**
	 * Inserts a Collection of FrameElements into the provided component
	 * 
	 * @param frames
	 *            Collection of frames to insert
	 * @param rack
	 *            rack Component to insert frames on
	 */
	public void insertFrames(Collection<FrameElement> frames, Component rack) {

		resetVariableCounters(rack);

		String mountingType = getMountingType(rack);

		switch (mountingType) {
		case ModelNames.FRAME_MOUNTING_TYPE_EDF:
			insertFramesEDF(frames, rack);
			break;
		case ModelNames.FRAME_MOUNTING_TYPE_WR:
			insertFramesWR(frames, rack);
			break;
		default:
			logger.error("Mounting type {} unknown", mountingType);
			// Do nothing
		}

		// tell the configuration if all frames calculated are legal
		setAllFramesLegalVariable(rack);

	}

	/**
	 * Checks whether a Component is a column type which can have frames
	 * 
	 * @param component
	 *            Component to check
	 * @return true / false
	 */
	public boolean canInsertFrames(Component component) {
		String toa = component.getTypeOfActor();
		return toa.startsWith(ModelNames.FRAME_TOA);
	}

	/**
	 * Checks whether center supports are necessary.
	 * 
	 * @param rack
	 *            rack Component to check on
	 * @return true if center support frames are needed, false otherwise
	 */
	public boolean needCenterSupport(Component rack) {
		// only EDF in floor standing cabinets needs center support frames if the height
		// of the column is heigher than 1350 ( 1350 units = 9 slots * slot height: 150
		int allowedHeight = ModelNames.MAX_COLUMN_HEIGHT_EDF_FLOOR / getSlotHeight(rack);
		if (getMountingType(rack) == ModelNames.FRAME_MOUNTING_TYPE_EDF
				&& getCabinetType(rack) == ModelNames.CABINET_ENCLOSURE_TYPE_FLOOR
				&& getRackHeight(rack) > allowedHeight) {
			return true;
		}
		return false;
	}

	/**
	 * Add warning message to configurator log
	 * 
	 * @param warning
	 *            String text of warning message
	 */
	public void logWarning(String warning) {
		logger.warn(warning);
	}

	/* ***** private methods ***** */

	/**
	 * Inserts WR frames into the configuraiton
	 * 
	 * @param frames
	 *            Collection of FrameElements to insert
	 * @param rack
	 *            rack Component to insert frames on
	 */
	private void insertFramesWR(Collection<FrameElement> frames, Component rack) {
		int height = getRackHeight(rack);
		for (FrameElement element : frames) {
			insertFrameWR(element, rack);
			mountingRailCalculation(element, height);
		}
	}

	/**
	 * Inserts a single WR frame into the configuration
	 * 
	 * @param element
	 *            FrameElement to insert
	 * @param rack
	 *            rack Component to insert on
	 */
	private void insertFrameWR(FrameElement element, Component rack) {
		String cabinetType = getCabinetType(rack);
		Collection<FrameElement> framesToInsert = determineFramesToInsert(element, rack);

		for (FrameElement e : framesToInsert) {
			switch (cabinetType) {
			case ModelNames.CABINET_ENCLOSURE_TYPE_WALL:
				insertAsProfilesOnly(e, rack);
				break;
			case ModelNames.CABINET_ENCLOSURE_TYPE_FLOOR:
				insertFrame(e, rack);
				break;
			default:
				logger.error("Cabinet type {} unknown for component {}", cabinetType, rack.getID());
			}
		} // END for
	}

	/**
	 * Insert frames in a WR frame type cabinet
	 * 
	 * @param frames
	 *            Collection of FrameElements to insert
	 * @param rack
	 *            rack Component to insert the frames on
	 */
	private void insertFramesEDF(Collection<FrameElement> frames, Component rack) {
		int height = getRackHeight(rack);
		for (FrameElement element : frames) {

			Collection<FrameElement> framesToInsert = determineFramesToInsert(element, rack);

			// insert the frame element(s) - one or two
			for (FrameElement e : framesToInsert) {
				// only insert frame elements which are not a horizontal base frame
				insertAsProfilesOnly(e, rack);
				mountingRailCalculation(element, height);
			}
		}
	}

	/**
	 * Insert all frames which are not Horizontal base frames, as profiles only
	 * 
	 * @param element
	 *            FrameElement to insert into configuration
	 * @param rack
	 *            rack Component to insert on
	 */
	private void insertAsProfilesOnly(FrameElement element, Component rack) {
		// only insert frame elements which are not a horizontal base frame
		if (!(element.getType() == FrameType.BASE && element.getOrientation() == Orientation.HORIZONTAL)) {
			// change BASE frame to INTERNAL (profiles only)
			if (element.getType() == FrameType.BASE) {
				element.setType(FrameType.INTERNAL);
			}
			insertFrame(element, rack);
		}
	}

	/**
	 * Inserts the FrameElement into the configuration
	 * 
	 * @param element
	 *            FrameElement to insert into the configuration
	 * @param rack
	 *            rack Component to insert FrameElement on
	 */
	private void insertFrame(FrameElement element, Component rack) {

		switch (element.getType()) {
		case BASE:
			// if no legal width -> mark frame element as illegal
			checkLegal(element, rack);
			setFrameVariables(element, rack);
			frameIndex++;
			break;
		case SUPPORT:
			setFrameVariables(element, rack);
			break;
		case INTERNAL:
			checkLegal(element, rack);
			setFrameVariables(element, rack);
			profileIndex++;
			break;
		default:
			logger.warn("FrameElement type {} unknown. FrameElement not inserted.", element.getType());
			// do nothing
		}
	}

	/**
	 * Determine if one or more frames are needed per FrameElement
	 * 
	 * @param element
	 *            FrameElement to check
	 * @param rack
	 *            rack Component to check on
	 * @return Collection of FrameElements to insert
	 */
	private Collection<FrameElement> determineFramesToInsert(FrameElement element, Component rack) {
		Collection<FrameElement> framesToInsert = new LinkedList<>();

		if (needDoubleFrames(element, rack)) {
			framesToInsert = createDoubleFrames(element);
		} else {
			// only one frame needed
			framesToInsert.add(element);
		}
		return framesToInsert;
	}

	/**
	 * Double the input element
	 * 
	 * @param element
	 * @return Collection containing the new elements
	 */
	private Collection<FrameElement> createDoubleFrames(FrameElement element) {
		Collection<FrameElement> newFrameElements = new LinkedList<>();
		if (element.getOrientation() == Orientation.VERTICAL) {
			newFrameElements.add(new FrameElement(element.getType(), element.getStartPoint(), element.getOrientation(),
					element.getLength(), FrameElement.AttachmentType.LEFT));
			newFrameElements.add(new FrameElement(element.getType(), element.getStartPoint(), element.getOrientation(),
					element.getLength(), FrameElement.AttachmentType.RIGHT));
		}
		if (element.getOrientation() == Orientation.HORIZONTAL) {
			newFrameElements.add(new FrameElement(element.getType(), element.getStartPoint(), element.getOrientation(),
					element.getLength(), FrameElement.AttachmentType.TOP));
			newFrameElements.add(new FrameElement(element.getType(), element.getStartPoint(), element.getOrientation(),
					element.getLength(), FrameElement.AttachmentType.BOTTOM));
		}
		return newFrameElements;
	}

	/**
	 * Resets the counters needed for inserting frame variables (frameIndex and
	 * profileIndex)
	 * 
	 * @param currentComponent
	 *            component to reset
	 */
	private void resetVariableCounters(Component currentComponent) {
		if (currentComponent.getID() != currentColumnID) {
			currentColumnID = currentComponent.getID();
			frameIndex = 1;
			profileIndex = 1;
		}
	}

	/**
	 * Checks whether a given variable is to be cleared
	 * 
	 * @param variable
	 *            variable StateControl to be cleared
	 * @return true if input variable should be cleared. False otherwise
	 */
	private boolean isVariableToClear(StateControl variable) {
		// checks for a frame variable prefix, followed by
		// zero or more digits and ending in a frame variable suffix
		String pattern = "^(" + ModelNames.FRAME_BASE_PREFIX + "|" + ModelNames.FRAME_PROFILE_PREFIX + "|"
				+ ModelNames.FRAME_SUPPORT_PREFIX + ")\\d*(" + ModelNames.FRAME_LENGTH_SUFFIX + "|"
				+ ModelNames.FRAME_POS_X_SUFFIX + "|" + ModelNames.FRAME_POS_Y_SUFFIX + ")$";

		return Pattern.matches(pattern, variable.getID());
	}

	/**
	 * Checks whether the component is a kit
	 * 
	 * @return Boolean true / false
	 **/
	private boolean isKitComponent(Component component) {
		String componentName = component.getTypeID();
		return componentName.startsWith(ModelNames.KIT_PREFIX);
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

		// position is legal if the position is within the rack
		return !(posX < 0 || posX > rackWidth || posY < 0 || posY > rackHeight);

	}

	/**
	 * Checks whether a given FrameElement size in a direction is legal.
	 * 
	 * @param length
	 *            length of FrameElement to check if is legal
	 * @param orientation
	 *            orientation of frame element
	 * @param rack
	 *            rack Component to check legal size on
	 * @return true if length is of legal size for the rack, otherwise false
	 */
	public boolean isLegalSize(int length, Orientation orientation, Component rack) {

		List<Integer> legalSizes = getLegalFrameLengths(rack, orientation);
		return legalSizes.contains(length);
	}

	/**
	 * Check whether two frames should be inserted for the frame element
	 * 
	 * @param element
	 *            element to check
	 * @param rack
	 *            rack to check on
	 * @return returns true if double frames are needed, false otherwise
	 */
	private boolean needDoubleFrames(FrameElement element, Component rack) {
		String mountingType = getMountingType(rack);
		// create double frame for attachment = BOTH in WR vertical and horizontal & EDF
		// vertical only
		// do not create double frame if attachment != BOTH for EDF horizontal and
		// support frame
		if (element.getType() != FrameType.SUPPORT && element.getAttachment() == FrameElement.AttachmentType.BOTH
				&& (mountingType == ModelNames.FRAME_MOUNTING_TYPE_WR
						|| (mountingType == ModelNames.FRAME_MOUNTING_TYPE_EDF
								&& element.getOrientation() == Orientation.VERTICAL))) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether a Frame Element is of legal size. If it is not legal, the
	 * Frame element will be marked as not legal.
	 * 
	 * @param element
	 *            Element to check
	 * @param rack
	 *            rack Component to check on
	 */
	private void checkLegal(FrameElement element, Component rack) {
		if (!isLegalSize(element.getLength(), element.getOrientation(), rack)) {
			// set frame element to being illegal
			element.setLegal(false);
			// indicate that not all frames are legal
		}
	}

	/**
	 * Calculates the needed offset of the frame element based on the frame element
	 * type and the attachment side of the frame
	 * 
	 * @param attachment
	 *            Which side of the frame element the kits are attached for
	 *            determining the needed offset
	 * @return the offset for the frame
	 */
	private int calcOffset(FrameElement.AttachmentType attachment) {
		// TODO: more than one visual for frames: EDF might have diff. frame visuals
		// than WR
		switch (attachment) {
		case TOP:
			return -25;
		case BOTTOM:
			return 0;
		case LEFT:
			return -25;
		case RIGHT:
			return 0;
		default: // BOTH
			return -12;
		}
	}

	/**
	 * Calculates the position of the FrameElement in configurator units
	 * 
	 * @param element
	 *            FrameElment to calculate position for
	 * @param rack
	 *            rack Component to calculate position in
	 * @return start position Point for the frame elements in configurator units
	 */
	private Point calcFramePosition(FrameElement element, Component rack) {
	    Point startPoint = element.getStartPoint();
	    // if the frame element is vertical we need to use the end point, because
	    // the frame insert points start at top left corner of the rack column
	    if( element.getOrientation() == Orientation.VERTICAL) {
	        startPoint = element.getEndPoint();
	    }
	    int xPos = startPoint.getX() * getMultiplierValue(Orientation.HORIZONTAL, rack);
		int yPos = invertFrameYPos(startPoint.getY(), rack)* getMultiplierValue(Orientation.VERTICAL, rack);

		int offset = calcOffset(element.getAttachment());

		// apply offset based on placement
		switch (element.getAttachment()) {
		case TOP:
		case BOTTOM:
			yPos = yPos + offset;
			break;
		case LEFT:
		case RIGHT:
			xPos = xPos + offset;
			break;
		default: // both
			if (element.getOrientation() == Orientation.HORIZONTAL) {
				yPos = yPos + offset;
			} else {
				xPos = xPos + offset;
			}
		}
		return new Point(xPos, yPos);
	}
	
	/**
	 * Calculates the display length of a frame element.
	 * 
	 * @param element
	 *            FrameElement to calculate the length for
	 * @param rack
	 *            rack Component to calculate the length for
	 * @return integer with the display length of the frame element
	 */
	private int calcFrameLength(FrameElement element, Component rack) {
		int lengthMultiplier = getMultiplierValue(element.getOrientation(), rack);
		return element.getLength() * lengthMultiplier;
	}

	/**
	 * Finds the variable name for the given FrameType
	 * 
	 * @param type
	 *            FrameType to find variable name for
	 * @return variable name to insert frame for
	 */
	private String findFrameVariableName(FrameType type) {

		switch (type) {
		case BASE:
			return ModelNames.FRAME_BASE_PREFIX + frameIndex;
		case SUPPORT:
			return ModelNames.FRAME_SUPPORT_PREFIX;
		default: // INTERNAL
			return ModelNames.FRAME_PROFILE_PREFIX + profileIndex;
		}
	}

	/**
	 * Gets the type of mounting frame to calculate for the column (WR / EDF). If no
	 * mounting type exist then the default frame type will be returned.
	 * 
	 * @param rack
	 *            rack Component to get the mounting type for
	 */
	// TODO: this may need to change when a column can have multiple frame types?
	public String getMountingType(Component rack) {
		// get column Component associated with this frame Component
		String type = getStringModelValue(rack, ModelNames.FRAME_MOUNTING_TYPE);

		if (type == null) {
			type = ModelNames.FRAME_MOUNTING_TYPE_DEFAULT;
		}

		return type;
	}

	/**
	 * Gets the Cabinet type, if any. If the cabinet type cannot be found the
	 * default cabinet type will be returned
	 * 
	 * @param rack
	 *            rack Component to find cabinet type on
	 * @return the cabinet type for this component if found, otherwise default
	 *         cabinet type
	 */
	private String getCabinetType(Component rack) {
		String type = getStringModelValue(rack, ModelNames.CABINET_ENCLOSURE_TYPE);

		if (type == null) {
			type = ModelNames.CABINET_ENCLOSURE_TYPE_DEFAULT;
		}
		return type;
	}

	/**
	 * Returns the multiplier needed for converting from rack coordinates into units
	 * 
	 * @param orentation
	 *            Orientation to get the multiplier for
	 * @param rack
	 *            rack Component to get the multiplier for
	 * @return The multiplier to use for the input direction
	 */
	private int getMultiplierValue(Orientation orentation, Component rack) {
		if (orentation == Orientation.HORIZONTAL) {
			return getSlotWidth(rack);
		} else {
			return getSlotHeight(rack);
		}
	}

	/**
	 * Insert frame into configuration using the proper variables, specified by the
	 * index (e.g. frame1)
	 * 
	 * @param element
	 *            FrameElement to set variables for
	 * @param rack
	 *            rack Component to set variables for
	 * @param column
	 *            column
	 */
	private void setFrameVariables(FrameElement element, Component rack) {
		String varName = findFrameVariableName(element.getType());
		Point position = calcFramePosition(element, rack);

		if (element.getType() == FrameType.BASE) {
			setFrameType(varName, element.getType(), rack);
		}

		setFramePosition(varName, position.getX(), position.getY(), rack);
		setFrameLength(varName, calcFrameLength(element, rack), rack);
		setFrameDirection(varName, element.getOrientation(), rack);
		setFrameAttachmentDirection(varName, element.getAttachment(), rack);
		setFrameIsLegal(varName, element.isLegal(), rack);

		if (!element.isLegal()) {
			allFramesLegal = false;
		}
	}

	/**
	 * Set the legal variable on the rack variable. Writes a warning in the log if
	 * no variable of that name is found.
	 * 
	 * @param variableName
	 *            name of variable to set
	 * @param isLegal
	 *            Boolean value to set the variable to
	 * @param rack
	 *            rack component for the variable
	 */
	private void setFrameIsLegal(String variableName, Boolean isLegal, Component rack) {
		String legalVar = variableName + ModelNames.FRAME_IS_LEGAL_SUFFIX;

		if (rack.getStateVector().existVar(legalVar)) {
			rack.getStateVector().getVar(legalVar).setValue(isLegal.toString());
		} else {
			logger.warn("Variable {} does not exist. Value will not be set.", legalVar);
		}
	}

	/**
	 * Sets the type variable of the specified variableName
	 * 
	 * @param variableName
	 *            variable to set to FrameType
	 * @param frameType
	 *            FrameType to set
	 * @param rack
	 *            rack Component to set the type on
	 */
	private void setFrameType(String variableName, FrameType frameType, Component rack) {
		String typeVar = variableName + ModelNames.FRAME_TYPE_SUFFIX;
		String type = "";
		if (frameType == FrameType.BASE) {
			type = getMountingType(rack);
		}

		if (rack.getStateVector().existVar(typeVar)) {
			rack.getStateVector().getVar(typeVar).setValue(type);
		} else {
			logger.warn("Variable {} does not exist. Value will not be set.", typeVar);
		}
	}

	/**
	 * Sets the position variable of the input frame variable
	 * 
	 * @param variableName
	 *            Name of variable to set
	 * @param posX
	 *            x coordinate of position
	 * @param posY
	 *            y coordinate of position
	 * @param rack
	 *            rack component to set the position variables on
	 */
	private void setFramePosition(String variableName, int posX, int posY, Component rack) {
		String posXVar = variableName + ModelNames.FRAME_POS_X_SUFFIX;
		String posYVar = variableName + ModelNames.FRAME_POS_Y_SUFFIX;

		if (rack.getStateVector().existVar(posXVar) && rack.getStateVector().existVar(posYVar)) {
			rack.getStateVector().getVar(posXVar).setValue(Integer.toString(posX));
			rack.getStateVector().getVar(posYVar).setValue(Integer.toString(posY));
		} else {
			logger.warn(
					"Variable {} or variable {} does not exist. Value will not be set.", posXVar, posYVar);
		}
	}

	/**
	 * Sets the length variable of the input frame variable
	 * 
	 * @param variableName
	 *            Name of variable to set
	 * @param length
	 *            length to set the variable to
	 * @param rack
	 *            rack Component to set the length variable on
	 */
	private void setFrameLength(String variableName, int length, Component rack) {
		String lengthVar = variableName + ModelNames.FRAME_LENGTH_SUFFIX;

		if (rack.getStateVector().existVar(lengthVar)) {
			rack.getStateVector().getVar(lengthVar).setValue(Integer.toString(length));
		} else {
			logger.warn("Variable {} does not exist. Value will not be set.", lengthVar);
		}
	}

	/**
	 * Sets the direction variable of the input frame variable
	 * 
	 * @param variableName
	 *            Name of variable to set
	 * @param orientaion
	 *            Orientation to set for the variable
	 * @param rack
	 *            rack Component to set the length variable on
	 */
	private void setFrameDirection(String variableName, Orientation orientaion, Component rack) {
		String directionVar = variableName + ModelNames.FRAME_ORIENTATION_SUFFIX;

		if (rack.getStateVector().existVar(directionVar)) {
			rack.getStateVector().getVar(directionVar).setValue(orientaion.toString().toLowerCase());
		} else {
			logger.warn("Variable {} does not exist. Value will not be set.", directionVar);
		}
	}

	/**
	 * Sets the attachment variable of the input frame variable. The attachment
	 * variable shows which side of the frame element kits attach to (LEFT, RIGHT,
	 * TOP, BOTTOM, BOTH - both means attaching from both sides
	 * 
	 * @param variableName
	 *            name of variable to set
	 * @param AttachmentType
	 *            attachment type to set
	 * @param rack
	 *            rack Component to set the attachment variable on
	 */
	private void setFrameAttachmentDirection(String variableName, FrameElement.AttachmentType attachment,
			Component rack) {
		String attachmentVar = variableName + ModelNames.FRAME_ATTACHMENT_SUFFIX;

		if (rack.getStateVector().existVar(attachmentVar)) {
			rack.getStateVector().getVar(attachmentVar).setValue(attachment.toString().toLowerCase());
		} else {
			logger.warn("Variable {} does not exist. Value will not be set.", attachmentVar);
		}
	}

	/**
	 * Sets the legal value of the kit frame component. If set to true all calculated frames are
	 * legal. If value is false one or more calculated frames are illegal.
	 * 
	 * @param rack
	 *            kit frame component to set the value on
	 */
	private void setAllFramesLegalVariable(Component rack) {
		String boolToString = allFramesLegal ? "1" : "0";
		if (rack.getStateVector().existVar(ModelNames.FRAME_ALL_LEGAL)) {
			rack.getStateVector().getVar(ModelNames.FRAME_ALL_LEGAL).setValue(boolToString);
		} else {
			logger.warn("Variable {} does not exist. Value will not be set.", ModelNames.FRAME_ALL_LEGAL);
		}
	}

	/**
	 * Gets the String value of the given component variable
	 * 
	 * @param component
	 *            Component to get variable on
	 * @param variableName
	 *            String with name of variable to get value for
	 * @return String value of variable, null if the variable does not exist
	 */
	private String getStringModelValue(Component component, String variableName) {
		if (!component.getStateVector().existVar(variableName)) {
			logger.warn("No variable {} for Component {}", variableName, component.getID());
			return null;
		}
		return component.getStateVector().getVar(variableName).getValue().toString();
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
	private Integer getIntegerModelValue(Component component, String variableName) {
		if (!component.getStateVector().existVar(variableName)) {
            logger.warn("No variable {} for Component {}", variableName, component.getID());
			return 0;
		}
		return Integer.parseInt(component.getStateVector().getVar(variableName).getValue().toString());
	}

	/**
	 * inverts the y coordinate from starting at bottom of column, to start at top
	 * of column
	 * @param posY     position Y to invert
	 * @return         inverted rack number
	 */
	private int invertFrameYPos(int posY, Component rack) {
	  // invert frame point (from bottom up to top down)
		return getRackHeight(rack) - posY; 
	}
	
    /**
     * Returns a list of legal frame lengths (in slots) for the given direction.
     * @param rack      rack Component to get the legal frame lengths for
     * @param orientation  which direction the legal frame 
     * @return           List of Integers containing the valid frame lengths in units. Sorted smallest to largest. otherwise the empty list
     */
    public List<Integer> getLegalFrameLengths(Component rack, Orientation orientation){
        List<Integer> legalValues = new LinkedList<>();
        switch( orientation ) {
            case HORIZONTAL:
                legalValues = (List<Integer>) VariableUtils.getLegalValues(rack, ModelNames.FRAME_LEGAL_LENGTH_HOR_VAR);
                legalValues = convertUnitsToSlots(rack, legalValues, orientation);
                break;
            case VERTICAL:
                legalValues = (List<Integer>)VariableUtils.getLegalValues(rack, ModelNames.FRAME_LEGAL_LENGTH_VER_VAR);
                legalValues = convertUnitsToSlots(rack, legalValues, orientation);
                break;
            default:
                // do nothing;
        }
        
        // no legal values where found in the model
        if( legalValues.isEmpty() ) {
            logger.warn("No legal frame values found for {}. Calculating default values.", rack.getID());
           legalValues = getDefaultLegalFrameLengths(rack, orientation);
        }
        
        Collections.sort(legalValues);
        return legalValues;
    }
 
    /**
     * Converts a list of model Unit values to a list of the corresponding slot values
     * @param rack          rack Component to use for conversion
     * @param unitList      Integer List of unit values to convert
     * @param orientation   Orientation to convert in
     * @return              Integer List of corresponding slot values.
     */
    private List<Integer> convertUnitsToSlots(Component rack, List<Integer> unitList, Orientation orientation){
        List<Integer> slotList = new LinkedList<>();
        int slotSize = getSlotSize(rack, orientation);
        
        for( int value : unitList ) {
            slotList.add( translateUnitToSlot(rack, value, slotSize) );
        }
        
        return slotList;
    }
    
    /** get the Size of a slot depending on the 
     * 
     * @param rack          rack Component to get the slot size for
     * @param orientation     Orientation to get the slot size in (horizontal = slot width, vertical = slot height)
     * @return              int of slot size in units
     */
    private int getSlotSize(Component rack, Orientation orientation) {
        int slotSize = 0;
        switch( orientation ) {
            case VERTICAL:
                slotSize = getSlotHeight(rack);
                break;
            case HORIZONTAL:
            default:
                    slotSize = getSlotWidth(rack);
                
        }
        return slotSize;
    }

    /**
     * Returns an Integer List containing default legal lengths in slots. Default legal lengths are assumed to be
     * all possible lengths from 1 to width / height of the column.
     * @param rack          rack Component to get the default legal frame length for 
     * @param orientation    Orientation to get legal lengths for.
     * @return              Integer List containing the default legal lengths.
     */
    private List<Integer> getDefaultLegalFrameLengths(Component rack, Orientation orientation){
        List<Integer> legals = new LinkedList<>();
        // use height/width of rack to determine longest possible value
        int length = 14; // tallest height of a column
        switch(orientation) {
            case VERTICAL:
                length = getRackHeight(rack);
                break;
            case HORIZONTAL:
                length = getRackWidth(rack);
            default: // unknown
                // use default init value
        }
        
        // calculate legal frame lengths - 
        for( int i = 1 ; i <= length; i++) {
            legals.add(i);
        }
        return legals;
    }

    /**
     * Translates a value from model Units to rack slots
     * @param rack      rack Component to use for conversion
     * @param value     value to convert
     * @param conversionValue  value to use for conversion from unit to slot
     * @return          the corresponding value in slots
     */
    private int translateUnitToSlot(Component rack, int value, int conversionValue) {
        return value / conversionValue;
    }
 
	
	// *** MOUNTING RAIL CALCULATION ***
	private void mountingRailCalculation(FrameElement element, int height) {
	
		int additional_mounting_set = 0;
		int countDuplicatesTop = 0;
		int countDuplicatesBottom = 0;
		Point p1 = element.getStartPoint();
		Point p2 = element.getEndPoint();
		
		if (element.getOrientation() == Orientation.VERTICAL) {
			if (p1.getY() == 0) {
				countTopMountingRail++;
				listFrameTopMountingRail.add(p1.getX());
			}
			if (p2.getY() == height) {
				countBottomMountingRail++;
				listFrameBottomMountingRail.add(p2.getX());
			}			
			 countDuplicatesTop = countDuplicates((ArrayList) listFrameTopMountingRail);
			 countDuplicatesBottom= countDuplicates((ArrayList) listFrameBottomMountingRail);
		
			if (configuration.getComponentFromToa(ModelNames.SWITCHBOARD_TWINLINE, 0) != null) {
				if (configuration.getComponent(currentColumnID).toString().matches(ModelNames.KIT_FRAME_TWINLINE + "[0-9]+")) {
					String[] string = configuration.getComponent(currentColumnID).getStateVector()
							.getVar("factory_assembled_mounting_set").toString().split(":");
					String string2 = string[2].replaceAll("\\s+", "");
					String string3 = string2.replaceAll("\\p{P}", "");
					int factory_set = Integer.parseInt(string3);
					additional_mounting_set = countTopMountingRail + countBottomMountingRail - factory_set-countDuplicatesTop-countDuplicatesBottom;
					String string4 = Integer.toString(additional_mounting_set);
					configuration.getComponent(currentColumnID).getStateVector().getVar("additional_mounting_set")
							.setValue(string4);

				}
			}
		}
	}
	
	//Method for counting duplicates values of x position.
	private static int countDuplicates(ArrayList list){
		 
		List<Integer> distinctList = (List<Integer>)list.stream().distinct().collect(Collectors.toList());
		int duplicates = list.size()-distinctList.size();
		 
		return duplicates;
		}
}
