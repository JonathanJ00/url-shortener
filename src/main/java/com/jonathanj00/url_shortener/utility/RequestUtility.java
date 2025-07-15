package com.jonathanj00.url_shortener.utility;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtility {

    /**
     * Utility method to find the base URL from the request
     *
     * @param request
     * @return
     */
    public static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();             // http or https
        String serverName = request.getServerName();     // hostname or IP
        int serverPort = request.getServerPort();        // port number
        String contextPath = request.getContextPath();   // application context path, usually ""

        // Build port segment (omit default ports)
        String portSegment = "";
        if ((scheme.equals("http") && serverPort != 80) ||
                (scheme.equals("https") && serverPort != 443)) {
            portSegment = ":" + serverPort;
        }

        return scheme + "://" + serverName + portSegment + contextPath + "/";
    }
}
