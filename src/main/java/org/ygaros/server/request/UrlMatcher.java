package org.ygaros.server.request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlMatcher {
    public static UrlType getUrlType(String url){
        Pattern pattern = Pattern.compile("[/\\w]*(\\w*\\.\\w*)+");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches()? UrlType.FILE : UrlType.STANDARD;
    }

}
