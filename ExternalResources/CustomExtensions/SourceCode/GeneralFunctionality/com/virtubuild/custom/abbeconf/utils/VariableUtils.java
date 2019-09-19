package com.virtubuild.custom.abbeconf.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtubuild.core.api.Component;
import com.virtubuild.core.api.StateValue;

/**
 * Utility class for accessing model elements of a configuration.
 * The class contains utility functions for accessing model elements in a configuration.
 * v. 1.0.0 - contains functions for getting and setting values of variables on components. 
 *
 */
public class VariableUtils {

    private static Logger logger = LoggerFactory.getLogger( VariableUtils.class );

    /**
     * Sets a component variable to the specified integer value. If the variable does not exist
     * a warning is written to the log.
     * @param component     component to set variable value for
     * @param variableName  name of variable to set
     * @param value         Integer value to set
     */
    public static void setIntegerValue( Component component, String variableName, Integer value ) {
        setVariableValue( component, variableName, value.toString() );
    }
    
    /**
     * Sets a component variable to the specified string value. If the variable does not exist
     * a warning is written to the log.
     * @param component     component to set variable value for
     * @param variableName  name of variable to set
     * @param value         String value to set
     */
    public static void setStringValue( Component component, String variableName, String value ) {
        setVariableValue( component, variableName, value );
    }
    
    /**
     * Sets a component variable to the specified boolean value. If the variable does not exist
     * a warning is written to the log.
     * @param component     component to set variable value for
     * @param variableName  name of variable to set
     * @param value         Boolean value to set
     */
    public static void setBooleanValue( Component component, String variableName, Boolean value ) {
        // 1 = true 0 = false - in Build the value must be set as a string representation of 1 or 0
        String boolToString = value ? "1" : "0";
        setVariableValue( component, variableName, boolToString);
    }
    
    /**
     * Gets the specified integer variable value for the specified Component
     * @param component     Component to get the variable value of
     * @param variableName  Name of the variable to get the value of
     * @return              the variable value as integer if it exists and can be parsed
     */
    public static Integer getIntegerValue( Component component, String variableName ) {

        String value = getStringValue( component, variableName );

        try {
            return Integer.parseInt( value );
        }
        catch (Exception e){
            logger.error( "Value '{}' of '{}' is not an integer.", value, variableName );
            return null;
        }
    }
    
    /** 
     * Gets the string representation of the value of the specified variable on the specified component. Returns null if no
     * variable with that name exist on the component.
     * @param component     component to find the variable on
     * @param variableName  name of variable to get the value of
     * @return              the value of the variable
     */
    public static String getStringValue(Component component, String variableName) {
        return getVariableValue(component, variableName );
    }
    
    /** 
     * checks whether a variable exists on the given component
     * @param component
     * @param variableName
     * @return  true if the variable exists on the component, false otherwise
     */
    public static boolean existVariable( Component component, String variableName ) {
        return component.getStateVector().existVar( variableName );
        
    }
    
    /** 
     * Gets the Boolean representation of the value of the specified variable on the specified component. Returns null if no
     * variable with that name exist on the component.
     * @param component     component to find the variable on
     * @param variableName  name of variable to get the value of
     * @return              the value of the variable
     */
    public static boolean getBooleanValue(Component component, String variableName) {
        String value = getVariableValue(component, variableName );
        return convertStringToBoolean( value );
    }
    
    /**
     * Gets the legal values of the specified variable on a components. Returns an empty
     * collection if the variable does not exist or there are no legal values. Assumes the legal values
     * are all integers.
     * @param component     Component with the variable
     * @param variableName  name of variable to get legal values of
     * @return              a Collection of legal values as Integer or the empty Collection.
     */
    public static Collection<Integer> getLegalValues( Component component, String variableName ){
        List<Integer> legals = new LinkedList<>();
        if( existVariable(component, variableName) ) {
            for( StateValue state : component.getStateVector().getVar( variableName ).getLegalValues() ) {
                legals.add( Integer.valueOf( state.toString() ) );
            }
        }
        else {
            logger.warn( "Variable {} does not exist on component {}", variableName, component.getID() );
        }
        return legals;
    }
    
    
    /**
     * Gets a list of legal values as String values. Returns the empty list if there are no values or the variable does not exist
     * @param component
     * @param variableName
     * @return
     */
    public static List<String> getLegalStringValues(Component component, String variableName){
        List<StateValue> stateVals = getLegalStateValues( component, variableName );
        List<String> values = new LinkedList<>();
        for( StateValue value : stateVals ) {
            values.add( value.getID() );
        }
        return values;
    }
 
    /**
     * Gets the legal values of the specified variable on a components. Returns an empty
     * List if the variable does not exist or there are no legal values.
     * @param component     Component with the variable
     * @param variableName  name of variable to get legal values of
     * @return              a List of legal values as StateVar or the empty List.
     */
    private static List<StateValue> getLegalStateValues( Component component, String variableName ){
        if( !existVariable( component, variableName ) ) {
            logger.warn( "Variable {} does not exist on component {}", variableName, component.getID() );
            return new LinkedList<>();
        }
        return component.getStateVector().getVar( variableName ).getLegalValues();
    }    

    /**
     * Gets a List of all domain values of the specified variable on a components - even illegal values. Returns an empty
     * List if the variable does not exist or there are no domain values.
     * @param component     Component with the variable
     * @param variableName  name of variable to get legal values of
     * @return              a List of legal values as StateVar or the empty List.
     */
    public static List<StateValue> getFullDomainValues( Component component, String variableName ){
        if( !existVariable( component, variableName ) ) {
            return new LinkedList<>();
        }
        return component.getStateVector().getVar( variableName ).getDomain();
    } 
    
    /* ** Private methods ** */
    
    /**
     * Converts a String to boolean. Returns false if not was possible to convert the input String.
     * @param value     String value to convert
     * @return          Returns the Boolean representation of the String value. Will return false if it was possible to convert.
     */
    public static boolean convertStringToBoolean( String value ) {
        switch( value.toUpperCase() ) {
            case "TRUE" :
            case "1" :
            case "YES" :
            case "Y":
                return true;
            case "FALSE":
            case "0":
            case "NO":
            case "N":
                return false;
            default:
                logger.error( "Cannot convert '{}' to a boolean.", value );
                return false;
        }
    }
    
    /**
     * Sets a component variable to the specified value. If the variable does not exist a warning is written to the log.
     * @param component     component to set variable value for
     * @param variableName  name of variable to set
     * @param value         value to set
     */
    private static void setVariableValue( Component component, String variableName, String value ) {
        
        if( existVariable(component, variableName) ) {
            component.getStateVector().getVar( variableName ).setValue( value );
        }
        else {
            logger.warn("Variable '{}' does not exist.", variableName );
        }
    }
    
    /**
     * Gets the model value of specific variable on a specific component.
     * @param component         component to get variable value from
     * @param variableName      name of variable to get value of
     * @return                  String representation of the variable value
     */
    private static String getVariableValue( Component component, String variableName) {
        if ( !existVariable(component, variableName) ){
            logger.warn( "Could not retrieve model value. No variable '{}' for Component '{}'.", variableName, component.getID() );
            return null;
        }
        return component.getStateVector().getVar( variableName ).getValue().getID();
    }
    
    /**
     * Converts a list of string to a list of Integers. Any non convertible strings will be skipped.
     * @param list
     * @return
     */
    public List<Integer> convertStringListToIntList(List<String> list){
        List<Integer> convertedList = new LinkedList<>();
        for( String str : list ) {
            Integer value = convertStringToInt(str);
            if(value != null) {
                convertedList.add(value);
            }
        }
        return convertedList;
    }

    /**
     * Converts string to integer if possible. returns null if not possible to convert.
     * @param str
     * @return
     */
    public Integer convertStringToInt(String str) {
        Integer value = null;
        try {
           value = Integer.parseInt(str); 
        }catch(Exception e) {
            logger.error("Cannot convert String {} to Integer", str);
        }
        return value;
    }
    

}
