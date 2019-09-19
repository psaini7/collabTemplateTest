package com.virtubuild.services.clientgui.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.clientgui.eventsystem.DFGUI;
import com.virtubuild.clientgui.eventsystem.UserAction;
import com.virtubuild.clientgui.reader.*;
import com.virtubuild.clientgui.units.*;
import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.api.Dataset;
import com.virtubuild.exporter.ExporterController;

/**
 * Base class for creating custom guiunits. All custom guiunits must extend this
 * class.
 * <p>
 * Note: This class corresponds to 'DFApplet' in version 4.4 and earlier.
 */
abstract public class CustomUnit extends GUIUnit {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUnit.class);

	private boolean waiting;

	/**
	 * Sets the basic data object used to create this guiunit. Meant to be called
	 * immediately after construction.
	 * <p>
	 * Design note: Necessary to allow customunits without releasing 'GUIUnitData'
	 * in the SDK.
	 */
	@Override
	public final void setUnitData(GUIUnitData data) {
		// data.getPanels().get(0).getTOA();
		super.setUnitData(data);
	}

	/**
	 * Empty constructor. NB: Do not access anything at this point, postpone until
	 * 'init' is called just after. Data will be available during init.
	 */
	public CustomUnit() {
	}

	/**
	 * API Method - access the unit data in the model ('Extradata')
	 *
	 * @return
	 */
	protected final Dataset getModelData() {

		LOGGER.debug(getUnitData().getDescription());
		LOGGER.debug(getUnitData().getCustomClassName());
		// getUnitData().getPanels().get(0).getTOA();
		return getUnitData().getExtraData();
	}

	/**
	 * API Method - access the Configurator OO-API.
	 *
	 * @return
	 */
	protected final Configuration getConfiguration() {
		return getGUIController().getManager().getConfiguration();
	}

	/**
	 * API Method - access the Exporter API.
	 *
	 * @return
	 */
	protected final ExporterController getExporterController() {
		return getGUIController().getManager().getExporterController();
	}

	/**
	 * API Method - is the UI waiting for the configurator to do something.
	 * 'refresh' is guarenteed to be called after this.
	 *
	 * @return
	 */
	protected final boolean isWaiting() {
		return waiting;
	}

	@Override
	public final void eventFired(int event, Object obj) {
		// if (!isShowing()) {
		// return;
		// }

		if (event == DFGUI.DFGUI_USERACTION) {
			UserAction ua = (UserAction) obj;
			if (ua.state == UserAction.STATE_START) {
				waiting = true;
			} else if (ua.state == UserAction.STATE_STOP || ua.state == UserAction.STATE_INSTANT) {
				waiting = false;
				if (isShowing()) {
					refresh();
				} else {

					refreshNonShowing();
				}
			}
			return;
		}

		// if (event == DFGUI.DFGUI_NEW_CURRENT || event == DFGUI.DFGUI_NO_CURRENT ||
		// event == DFGUI.DFGUI_DRAG_END) {
		// updateGui((BaseComponent) obj);
		// } else if (event == DFGUI.DFGUI_RESET_WORLD) {
		// updateGui();
		// }
	}

	/**
	 * CustomUnit extension point - implement this.
	 * <p>
	 * Called by the system, when the configuration may have changed, and the unit
	 * should refresh itself.
	 */
	protected abstract void refresh();

	/**
	 * CustomUnit extension point - override if useful.
	 * <p>
	 * Called by the system, when a useraction is finished, but this unit is not
	 * visible. The normal 'refresh' will always be called eventually.
	 */
	protected void refreshNonShowing() {
		// Empty default implementation.
	}

	/**
	 * CustomUnit extension point.
	 * <p>
	 * Called immediately after construction.
	 */
	@Override
	abstract public void init();

	/**
	 * API Method - called before system shutdown.
	 */
	@Override
	abstract public void cleanUp();

}
