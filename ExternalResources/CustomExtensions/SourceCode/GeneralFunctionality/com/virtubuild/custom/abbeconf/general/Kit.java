package com.virtubuild.custom.abbeconf.general;

import java.util.Collection;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.api.Component;
import com.virtubuild.custom.abbeconf.utils.VariableUtils;

/**
 * ABB eConfigure Kit component Contains method to access relevant variables for
 * a Kit component as well as various methods for comparing kits
 */
public class Kit implements Comparable<Kit> {

    private Component           kitComponent;  // associated kit Component from configuration

    private static final Logger logger                    = LoggerFactory.getLogger(Kit.class);

    private static final String KIT_PREFIX                = "kit_";

    // Model variables
    private static final String WIDTH_VAR                 = "width";
    private static final String HEIGHT_VAR                = "height";
    private static final String IS_LEGAL_VAR              = "legal_check";
    private static final String POS_X_VAR                 = "kit_posx";
    private static final String POS_Y_VAR                 = "kit_posy";

    private static final String RACK_AVAILABLE_WIDTH_VAR  = "availableColumns";
    private static final String RACK_AVAILABLE_HEIGHT_VAR = "availableRows";

    /**
     * Takes the associated model component as input
     * 
     * @param kit
     */
    public Kit(Component kit) {
        this.kitComponent = kit;
    }

    /**
     * Get the Component associated to this Kit
     * 
     * @return configuration Component
     */
    public Component getModelComponent() {
        return kitComponent;
    }

    /**
     * Gets the ID of this Kit
     * 
     * @return String
     */
    public String getID() {
        return kitComponent.getID();
    }

    /**
     * Get the width of the kit
     * 
     * @return the width in int
     */
    public int getWidth() {
        return VariableUtils.getIntegerValue(kitComponent, WIDTH_VAR);
    }

    /**
     * Set the width of the kit
     * 
     * @param value
     *            new value of height
     */
    public void setWidth(int value) {
        VariableUtils.setIntegerValue(kitComponent, WIDTH_VAR, value);
    }

    /**
     * Get the height of the kit
     * 
     * @return the height in int
     */
    public int getHeight() {
        return VariableUtils.getIntegerValue(kitComponent, HEIGHT_VAR);
    }

    /**
     * Set the width of the kit
     * 
     * @param value
     *            new value of width
     */
    public void setHeight(int value) {
        VariableUtils.setIntegerValue(kitComponent, HEIGHT_VAR, value);
    }

    /**
     * Gets the Kit's position in the rack
     * 
     * @return x and y coordinate Point
     */
    public Point getPosition() {
        int posX = VariableUtils.getIntegerValue(kitComponent, POS_X_VAR);
        int posY = VariableUtils.getIntegerValue(kitComponent, POS_Y_VAR);
        return new Point(posX, posY);
    }

    /**
     * Get the String value of the specified variable
     * 
     * @param variableName
     *            String
     * @return String containing the value of the variable
     */
    public String getStringVariable(String variableName) {
        return VariableUtils.getStringValue(kitComponent, variableName);
    }

    /**
     * Get the int value of the specified variable
     * 
     * @param variableName
     *            String
     * @return int containing the value of the variable
     */
    public int getIntegerVariable(String variableName) {
        return VariableUtils.getIntegerValue(kitComponent, variableName);
    }

    /**
     * Get the boolean value of the specified variable
     * 
     * @param variableName
     *            String
     * @return boolean containing the value of the variable
     */
    public boolean getBooleanVariable(String variableName) {
        return VariableUtils.getBooleanValue(kitComponent, variableName);
    }

    /**
     * Gets the legal values for the height of this Kit
     * 
     * @return Collection of legal values in Integer
     */
    public Collection<Integer> getLegalHeights() {
        return VariableUtils.getLegalValues(kitComponent, HEIGHT_VAR);
    }

    /**
     * Gets the legal values for the width of this kit Kit
     * 
     * @return Collection of legal values in Integer
     */
    public Collection<Integer> getLegalWidths() {
        return VariableUtils.getLegalValues(kitComponent, WIDTH_VAR);
    }

    /**
     * Checks if the column is legal or not, and return a boolean
     * 
     * @return boolean
     */
    public boolean isLegal() {
        return VariableUtils.getBooleanValue(kitComponent, IS_LEGAL_VAR);

    }

    /**
     * Overrides object toString() method
     */
    @Override
    public String toString() {
        return this.getID();
    }

    @Override
    public int compareTo(Kit kit) {
        return this.kitComponent.getID().compareTo(kit.getID());
    }

    /**
     * Compare on size of kit, comparing first on width then height
     */
    public static Comparator<Kit> KitWidthHeightComparator = new Comparator<Kit>() {
                                                               public int compare(Kit elem1, Kit elem2) {
                                                                   int widthCompare = Integer.compare(elem1.getWidth(),
                                                                           elem2.getWidth());
                                                                   if (widthCompare != 0) {
                                                                       return widthCompare;
                                                                   }
                                                                   return Integer.compare(elem1.getHeight(),
                                                                           elem2.getHeight());
                                                               }
                                                           };

    /**
     * Compare on size of kit, comparing first on height then width
     */
    public static Comparator<Kit> KitHeightWidthComparator = new Comparator<Kit>() {
                                                               public int compare(Kit elem1, Kit elem2) {
                                                                   int heightCompare = Integer.compare(
                                                                           elem1.getHeight(), elem2.getHeight());
                                                                   if (heightCompare != 0) {
                                                                       return heightCompare;
                                                                   }
                                                                   return Integer.compare(elem1.getWidth(),
                                                                           elem2.getWidth());
                                                               }
                                                           };

    /**
     * Compare on area size of kit
     */
    public static Comparator<Kit> KitAreaComparator        = new Comparator<Kit>() {
                                                               public int compare(Kit elem1, Kit elem2) {
                                                                   int area1 = elem1.getWidth() * elem1.getHeight();
                                                                   int area2 = elem2.getWidth() * elem2.getHeight();
                                                                   return Integer.compare(area1, area2);
                                                               }
                                                           };

    /**
     * Checks whether the component is a kit
     * 
     * @return Boolean true / false
     **/
    public static boolean isKitComponent(Component component) {
        String componentName = component.getTypeID();
        return componentName.startsWith(KIT_PREFIX);
    }

    /**
     * Checks whether a kit is in a legal position for calculation of frames
     * 
     * @param kit
     *            kit Component to check on
     * @return true if kit is in a legal position, false otherwise
     */
    public static boolean isLegalPosition(Component kit, Component rack) {
        int posX = getIntegerModelValue(kit, POS_X_VAR);
        int posY = getIntegerModelValue(kit, POS_Y_VAR);
        int rackWidth = getIntegerModelValue(rack, RACK_AVAILABLE_WIDTH_VAR);
        int rackHeight = getIntegerModelValue(rack, RACK_AVAILABLE_HEIGHT_VAR);

        return !(posX < 0 || posX > rackWidth || posY < 0 || posY > rackHeight);
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
    private static Integer getIntegerModelValue(Component component, String variableName) {
        if (!component.getStateVector().existVar(variableName)) {
            logger.warn("No variable " + variableName + " for Component " + component.getID());
            return 0;
        }
        return Integer.parseInt(component.getStateVector().getVar(variableName).getValue().toString());
    }

}
