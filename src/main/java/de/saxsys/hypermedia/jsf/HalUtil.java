package de.saxsys.hypermedia.jsf;

public class HalUtil {

    public static String replaceParam(String href, String param) {
        return href.replaceFirst("\\{.+\\}", param);
    }

}
