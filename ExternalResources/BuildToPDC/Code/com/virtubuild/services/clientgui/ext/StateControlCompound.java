package com.virtubuild.services.clientgui.ext;

import java.util.*;

import org.slf4j.*;

import com.virtubuild.clientgui.*;
import com.virtubuild.core.*;
import com.virtubuild.core.api.*;
import com.virtubuild.core.constants.*;
import com.virtubuild.core.state.*;
import com.virtubuild.core.state.StateConstants.*;

/**
 * Used to control multiple {@link StateControl}s through a single control. This
 * should generally allow us to use existing items to control multiple variables
 * at once (multi selection).
 *
 * @author ahoejmark
 */
public class StateControlCompound implements StateControl {
	private static final Logger LOGGER = LoggerFactory.getLogger(StateControlCompound.class);

	private Collection<StateControl> stateControls;
	private final StateValue differ = new DifferValue(); // Special value that denotes a difference in the values
															// between the controls
	private BaseComponent finishedComponentPick;

	public StateControlCompound() {
		stateControls = new HashSet<StateControl>();
	}

	public StateControlCompound(Collection<StateControl> controls) {
		stateControls = new HashSet<StateControl>(controls);
	}

	public boolean isEmpty() {
		return stateControls.isEmpty();
	}

	public boolean add(StateControl control) {
		return stateControls.add(control);
	}

	private boolean isDifferValue(StateValue value) {
		return differ.equals(value);
	}

	@Override
	public boolean isLegalValue(String s) {
		for (StateControl sc : stateControls) {
			if (!sc.isLegalValue(s)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getID() {
		StringBuilder sb = new StringBuilder();
		sb.append("StateControlCompound-");
		int length = sb.length();
		for (StateControl sc : stateControls) {
			sb.append(sc.getID() + "_");
		}
		boolean didAppend = sb.length() > length;
		if (didAppend) {
			sb.append("?");
		} else {
			sb.replace(sb.length() - 1, sb.length(), "");
		}
		// LOGGER.debug(sb.toString());
		// throw new RuntimeException("Method not implemented");
		return sb.toString();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Method not implemented");
		// return "name";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		throw new RuntimeException("Method not implemented");
		// return "desc";
	}

	@Override
	public StateValue getValue() {
		StateValue value = null;
		for (StateControl sc : stateControls) {
			if (value == null) {
				value = sc.getValue();
			} else if (!sc.getValue().equals(value)) {
				value = differ;
			}
		}

		if (value == null) {
			LOGGER.error("Could not get a value for the statecontrol '" + getID() + "'");
		}

		return value;
	}

	/**
	 * Legal values are the intersection of all provided {@link StateControl}'s
	 * legal value domains. This prevents contradiction.
	 */
	@Override
	public List<StateValue> getLegalValues() {
		Set<StateValue> intersection = null;
		for (StateControl sc : stateControls) {
			if (intersection == null) {
				intersection = new HashSet<StateValue>();
				intersection.addAll(sc.getLegalValues());
			} else {
				intersection.retainAll(sc.getLegalValues()); // Intersection
			}
		}

		// Differ value is legal, but we only want to show it if value actually differs
		if (isDifferValue(getValue())) {
			intersection.add(differ);
		}

		return intersection == null ? new ArrayList<StateValue>() : new ArrayList<StateValue>(intersection);
	}

	@Override
	public List<StateValue> getDomain() {
		List<StateValue> domain = new ArrayList<StateValue>();
		for (StateControl sc : stateControls) {
			domain.addAll(sc.getDomain());
			if (isDifferValue(getValue())) {
				domain.add(differ);
			}
			break;
		}

		return domain;
	}

	private void repickComponent() {
		if (finishedComponentPick != null && !((ComponentState) finishedComponentPick).isDeleting()) {
			GUIController.getInstance().getConfigurationAPI().componentPicked(finishedComponentPick);
		}
	}

	@Override
	public boolean setValue(StateValue value) {
		if (isDifferValue(value)) {
			return false;
		}

		boolean success = true;
		for (StateControl sc : stateControls) {
			boolean scSuccess = sc.setValue(value);
			if (scSuccess == false) {
				success = false; // TODO Not sure if we should continue or stop trying setting other values if
									// one failed
			}
		}

		if (!success) {
			LOGGER.error("Not all controls could be set to '" + value + "'");
		}

		repickComponent();

		return success;
	}

	@Override
	public boolean setValue(String value) {
		boolean success = true;
		for (StateControl sc : stateControls) {
			boolean scSuccess = sc.setValue(value);
			if (scSuccess == false) {
				success = false;
			}
		}

		if (!success) {
			LOGGER.error("Not all controls could be set to '" + value + "'");
		}

		repickComponent();

		return success;
	}

	@Override
	public ControlType getControlType() {
		throw new RuntimeException("Method not implemented");
		// return null;
	}

	@Override
	public ValueType getValueType() {
		ValueType firstValue = null;
		for (StateControl sc : stateControls) {
			if (firstValue == null) {
				firstValue = sc.getValueType();
			}

			if (firstValue != sc.getValueType()) {
				return ValueType.UNKNOWN;
			}
		}

		return firstValue;
	}

	@Override
	public ValueStatus getValueStatus() {
		ValueStatus firstStatus = null;
		for (StateControl sc : stateControls) {
			if (firstStatus == null) {
				firstStatus = sc.getValueStatus();
			}

			if (sc.getValueStatus() == ValueStatus.USER) {
				return ValueStatus.USER; // (At least) one control was user set
			} else if (firstStatus != sc.getValueStatus()) {
				return ValueStatus.UNKNOWN; // All controls did not have same value status
			}
		}

		return firstStatus; // All controls had the same value status
	}

	@Override
	public void unsetValue() {
		LOGGER.debug("Unsetting " + stateControls.size() + " controls in compound");
		for (StateControl sc : stateControls) {
			sc.unsetValue();
		}
	}

	/**
	 * Checks if all statecontrols added are illegal
	 */
	@Override
	public boolean isIllegal() {
		for (StateControl sc : stateControls) {
			if (sc.isIllegal()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if any statecontrol added is invalid
	 */
	@Override
	public boolean isInvalid() {
		for (StateControl sc : stateControls) {
			if (sc.isInvalid()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if any statecontrol added is active
	 */
	@Override
	public boolean isActive() {
		for (StateControl sc : stateControls) {
			if (sc.isActive()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if value is in domain of all statecontrols added.
	 */
	@Override
	public boolean isInDomain(String value) {
		for (StateControl sc : stateControls) {
			if (!sc.isInDomain(value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if all statecontrols added are none
	 */
	@Override
	public boolean isNone() {
		for (StateControl sc : stateControls) {
			if (!sc.isNone()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * NOT IMPLEMENTED
	 */
	@Override
	public void addStateListener(StateListener listener) {
		throw new RuntimeException("Method not implemented");
	}

	/**
	 * NOT IMPLEMENTED
	 */
	@Override
	public void removeStateListener(StateListener listener) {
		throw new RuntimeException("Method not implemented");
	}

	private class DifferValue implements StateValue {

		@Override
		public String getName() {
			return "(DIFFERS)";
		}

		@Override
		public String getID() {
			return "differ_id";
		}

		@Override
		public String getDescription() {
			return "The values for multiple selected components differs";
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof DifferValue)) {
				return false;
			}
			return true; // All differ values are the same...
		}

		// TODO hashCode
	}

	/**
	 * Picks a component after {@link #setValue()} has been called. Calling
	 * {@link #setValue()} might result in another component than current being
	 * picked. Setting a component through this method, will ensure it ends up being
	 * picked.
	 *
	 * @param component Component that should be picked
	 */
	public void setFinishedComponentPick(BaseComponent component) {
		finishedComponentPick = component;
	}

	@Override
	public String toString() {
		return super.toString() + "[stateControlCount=" + stateControls.size() + "]";
	}

	@Override
	public void resetValue() {
		// TODO Auto-generated method stub

	}
}