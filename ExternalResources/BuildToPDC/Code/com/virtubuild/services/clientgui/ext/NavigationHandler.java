package com.virtubuild.services.clientgui.ext;

import static com.virtubuild.util.debug.DebugController.*;

import org.slf4j.*;

import com.virtubuild.core.*;
import com.virtubuild.core.api.*;
import com.virtubuild.core.api.Manager;
import com.virtubuild.util.geometry.*;

/**
 * This class contains methods to perform world navigation in VirtuBUILD Core
 * (World class). WorldHandler contains no GUI.
 * <p>
 * todo *ab 9/3-2006* Rename to 'NavigationHandler' (or similar) - check
 * dop-stub etc first. todo *ab 25/4-2016* Done .. move to core? (Belongs to
 * Configurator-management module).
 *
 * @see ViewPointHandler
 */
public class NavigationHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger("com.virtubuild.clientgui.ext.WorldHandler");

	private final static boolean DEFAULT_DIRECTED_DRAG_ENABLED = true;
	private final static float DEFAULT_DRAG_SENSITIVITY = 1 / 100f;

	private ViewpointController vpctr;

	// Mouse drag coord's
	private int pandownx = 0;
	private int pandowny = 0;
	private final static Vector3f UNIT_VERTICAL = new Vector3f(0, 1, 0);
	// A unit vector which a lower projection of vertical than this, is considered
	// vertical.
	private static final float ALMOST_VERTICAL_THRESHOLD = 0.4f;
	private final Vector3f direction = new Vector3f(); // Workspace.

	private boolean directedDrag = DEFAULT_DIRECTED_DRAG_ENABLED;
	private float dragSensitivity = DEFAULT_DRAG_SENSITIVITY;

	private Manager manager;

	public NavigationHandler(Manager manager) {
		if (manager == null || !manager.isReady()) {
			LOGGER.error(DEV, "Timing mistake. The manager must be initialized at this point.");
		}
		this.manager = manager;
		// SystemListener waitForWorld = new SystemListener() {
		// @Override
		// public void eventFired(SystemEvent e) {
		// com.virtubuild.core.Manager.removeSystemListener(this);
		// switch (e.getType()) {
		// case SystemEvent.SYSTEM_STARTED:
		// case SystemEvent.SYSTEM_RESTARTED:
		// init();
		// break;
		// default:
		// break;
		// }
		// }
		// };
		// com.virtubuild.core.Manager.addSystemListener(waitForWorld);

		// if (GUIController.getInstance().getManager().isReady()) {
		init();
		// com.virtubuild.core.Manager.removeSystemListener(waitForWorld);
		// }
	}

	private synchronized void init() {
		if (isReady()) {
			return;
		}
		vpctr = manager.getConfiguration().getViewpointController();
	}

	/**
	 * Has init already been run?
	 */
	public boolean isReady() {
		return vpctr != null;
	}

	private Movable getCurrentMover() {
		return manager.getWorld().getCurrentMover();
	}

	/**
	 * Rotates the current mover 'dir' steps. The actual angle depends on the
	 * 'turnsteps' attribute on the component. Positive number is clockwise.
	 *
	 * @param dir Number of steps to rotate the current component clockwise.
	 */
	public void rotate(int dir) {
		try {
			getCurrentMover().turn(dir);
		} catch (Exception ex) {
			LOGGER.error("PROBLEM - Turn left{}", ex);
		}
	}

	/**
	 * Moves the current mover 'rel' modified by collision.
	 * <p>
	 * 'Rel' is a relative vector in world coordinates, so this method does NOT take
	 * the current viewpoint into account.
	 **/
	public void move(float[] rel) {
		if (rel == null) {
			LOGGER.error("called with null");
			return;
		}
		try {
			direction.set(rel);
			getCurrentMover().move(direction.toFloatArray());
		} catch (Exception ex) {
			LOGGER.error("PROBLEM - can not move : {}", ex);
		}
	}

	/**
	 * Moves the current mover 'rel' modified by collision.
	 * <p>
	 * If 'directionalDrag' is enabled this method will take 'rel' as a relative
	 * vector in the current viewpoint coordinate system, so this method does take
	 * the current viewpoint into account.
	 **/
	public void moveInViewDirection(float[] rel) {
		if (rel == null) {
			LOGGER.error("called with null");
			return;
		}
		try {
			direction.set(rel);
			if (directedDrag) {
				modifyDirectionForViewpoint(direction);
			}
			getCurrentMover().move(direction.toFloatArray());
		} catch (Exception ex) {
			LOGGER.error("PROBLEM - can not move", ex);
		}
	}

	// todo: Somehow make sensitivity and 'directed_drag' settable. Not certain if
	// set globally once, or just for every use from
	// the action.

	/**
	 * Set the sensitivity of the drag-function.
	 * <p>
	 * The default value is 1/100.
	 **/
	public void setDragSensitivity(float sen) {
		if (sen < 0) {
			LOGGER.error("out of range {}", sen);
			return;
		}
		dragSensitivity = sen;
	}

	/**
	 * Enable or disable drag in viewpoint-direction.
	 * <p>
	 * Directional drag is enabled by default.
	 */
	public void enableDirectedDrag(boolean enable) {
		directedDrag = enable;
	}

	/**
	 * Called before starting a pan (aka drag) operation, to set the initial point.
	 * <p>
	 * Typically called on 'mouseDown'.
	 *
	 * @param x
	 * @param y
	 */
	public void setPanStartPoint(int x, int y) {
		// - kt - 12/12/05 Semi-hack to lock movement after snapping to a component that
		// becomes master for the currently moving component
		manager.getWorld().lockOnSnap(true);
		pandownx = x;
		pandowny = y;
	}

	/**
	 * Called to indicate the a pan operation is finished.
	 * <p>
	 * Typically called on 'mouseUp'.
	 */
	public void stopPan() {
		// - kt - 12/12/05 Semi-hack to lock movement after snapping to a component that
		// becomes master for the currently moving component
		manager.getWorld().lockOnSnap(false);
	}

	public void doPan(int panX, int panY) {
		float x = (panX - pandownx) * dragSensitivity;
		float y = (panY - pandowny) * dragSensitivity;

		direction.set(x, 0, y);

		if (directedDrag) {
			modifyDirectionForViewpoint(direction);
		}

		getCurrentMover().move(direction.toFloatArray());
		pandownx = panX;
		pandowny = panY;
	}

	private void modifyDirectionForViewpoint(Vector3f dir) {
		Rotation rot = vpctr.getCurrentOrientation();
		if (rot == null) {
			rot = Rotation.IDENTITY;
		}
		float dot = rot.getAxis().dot(UNIT_VERTICAL);
		if (dot > ALMOST_VERTICAL_THRESHOLD) {
			dir.turn_y(-rot.angle);
		} else if (dot < -ALMOST_VERTICAL_THRESHOLD) {
			dir.turn_y(rot.angle);
		} else {
			// Dont modify if almost vertical
			// NB: In this case we should not use the viewpoint orientation, but instead the
			// 'role-amount'
			// Todo: Check if this number is available.
		}
		// LOGGER.trace(MARK_MARKER, "modifyDirection : "+rot+" and
		// ("+dir.x+","+dir.y+") and "+dir+" .. dot : "+dot);
	}

}
