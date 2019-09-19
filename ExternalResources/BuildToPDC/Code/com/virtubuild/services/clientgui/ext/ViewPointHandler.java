package com.virtubuild.services.clientgui.ext;

import com.virtubuild.clientgui.*;
import com.virtubuild.core.Manager;
import com.virtubuild.core.api.*;
import com.virtubuild.core.event.*;

/**
 * This class contains methods to perform view point navigation in VirtuBUILD
 * Core (World class). ViewPointHandler contains no GUI.
 */
public class ViewPointHandler {
	private final static boolean ZOOM_ENABLED = true;
	private final static boolean RELATIVE_VIEWPOINTS_ENABLED = true;

	private ViewpointController vpctr = null;
	private int zoomdowny = 0;
	private boolean initRelativeToCurrent;

	// TODO: This should be configurable, either globally or from gui setup.
	private final static float SENSITIVITY = 1 / 100f;

	public ViewPointHandler() {
		SystemListener waitForWorld = new SystemListener() {
			@Override
			public void eventFired(SystemEvent e) {
				Manager.removeSystemListener(this);
				switch (e.getType()) {
				case SystemEvent.SYSTEM_STARTED:
				case SystemEvent.SYSTEM_RESTARTED:
					init();
					break;
				default:
					break;
				}
			}
		};
		Manager.addSystemListener(waitForWorld);

		if (GUIController.getInstance().getManager().isReady()) {
			init();
			Manager.removeSystemListener(waitForWorld);
		}
	}

	private synchronized void init() {
		if (isReady()) {
			return;
		}
		vpctr = GUIController.getInstance().getConfigurationAPI().getViewpointController();
		setRelativeToCurrentComponent(initRelativeToCurrent);
	}

	public boolean isReady() {
		// *ab 29/5-2007 Review* Meaningles! Uses lazy initialization in 'World', so
		// vpctr cannot be null without crashing World!.
		return vpctr != null;
	}

	/**
	 * Changes the viewpoint-mode between being absolute and relative to the current
	 * component.
	 * 
	 * @param relative
	 */
	public void setRelativeToCurrentComponent(boolean relative) {
		if (!RELATIVE_VIEWPOINTS_ENABLED) {
			return;
		}
		if (!isReady()) {
			// NOTE: *hod 08/12/2006* - Delaying initialisation until the system is
			// accessible
			initRelativeToCurrent = relative;
			return;
		}
		BaseComponent curcomp = GUIController.getInstance().getCurrentComponent();
		if (curcomp.getHolderType() == BaseComponent.STATE_COMPONENT) {
			vpctr.setRelative((Component) curcomp, relative);
		} else {
			// Do nothing if 'world'.
			// Group presently not implemented.
		}
	}

	/** Sets a starting 'y'-value for 'doZoom'. */
	public void setZoomPoint(int y) {
		zoomdowny = y;
	}

	/** Zoom the current viewpoint 'y'*Sensitivity relative to last point. */
	public void doZoom(int y) {
		if (ZOOM_ENABLED) {
			float rel = (y - zoomdowny) * SENSITIVITY;
			vpctr.setZoom(rel);
			zoomdowny = y;
		}
	}

	/** Select viewpoint 'vpName'. */
	public void setViewpoint(String vpName) {
		vpctr.setViewpoint(vpName);
	}

	public void nextViewpoint() {
		vpctr.nextViewpoint();
	}

	public void prevViewpoint() {
		vpctr.prevViewpoint();
	}
}
