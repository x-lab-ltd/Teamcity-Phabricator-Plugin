package uk.xlab.teamcity.phabricator;

public final class CommonUtils {

    public static Boolean isNullOrEmpty(String str) {
        return str == null || str.equals("");
    }
    
    public static Boolean isNull(Object obj) {
        return obj == null;
    }
}
