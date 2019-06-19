package uk.xlab.teamcity.phabricator;

/**
 * A collection of helper methods that are used throughout the plugin
 *
 */
public final class CommonUtils {

    /**
     * Check if the given string is NULL or an empty string
     * 
     * @param str String to be checked
     * @return Whether the string is NULL or empty
     */
    public static Boolean isNullOrEmpty(String str) {
        return str == null || str.equals("");
    }

    /**
     * Check if the given object is NULL
     * 
     * @param obj Object to be checked for NULL
     * @return Whether the object is NULL
     */
    public static Boolean isNull(Object obj) {
        return obj == null;
    }
}
