package com.backend.hrms.helpers.auth;

import jakarta.servlet.http.HttpServletRequest;

public class DeviceDetector {

    public static class DeviceInfo {
        public enum DeviceType {
            MOBILE, DESKTOP, UNKNOWN
        }

        private DeviceType deviceType;
        private String os;
        private String browser;

        public DeviceInfo(DeviceType deviceType, String os, String browser) {
            this.deviceType = deviceType;
            this.os = os;
            this.browser = browser;
        }

        public DeviceType getDeviceType() {
            return deviceType;
        }

        public String getOs() {
            return os;
        }

        public String getBrowser() {
            return browser;
        }

        @Override
        public String toString() {
            return "DeviceInfo{" +
                    "deviceType=" + deviceType +
                    ", os='" + os + '\'' +
                    ", browser='" + browser + '\'' +
                    '}';
        }
    }

    /**
     * Detects device information from an HttpServletRequest.
     *
     * @param request The HttpServletRequest object from which to extract the
     *                User-Agent.
     * @return A DeviceInfo object containing the detected device details.
     */
    public static DeviceInfo detectDevice(HttpServletRequest request) {
        // Get the User-Agent header from the request
        String userAgent = request.getHeader("User-Agent");

        // Handle cases where userAgent might be null or empty
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return new DeviceInfo(DeviceInfo.DeviceType.UNKNOWN, "Unknown", "Unknown");
        }

        // Initialize variables
        DeviceInfo.DeviceType deviceType = DeviceInfo.DeviceType.UNKNOWN;
        String os = "Unknown";
        String browser = "Unknown";

        // Determine device type
        if (userAgent.matches("(?i).*Mobi.*|(?i).*Android.*")) {
            deviceType = DeviceInfo.DeviceType.MOBILE;
        } else {
            deviceType = DeviceInfo.DeviceType.DESKTOP;
        }

        // Detect operating system
        if (userAgent.matches("(?i).*Windows NT.*")) {
            os = "Windows";
        } else if (userAgent.matches("(?i).*Macintosh.*|(?i).*Mac OS X.*")) {
            os = "Mac OS";
        } else if (userAgent.matches("(?i).*Linux.*")) {
            os = "Linux";
        } else if (userAgent.matches("(?i).*iPhone.*")) {
            os = "iPhone";
        } else if (userAgent.matches("(?i).*Android.*")) {
            os = "Android";
        }

        // Detect browser
        if (userAgent.matches("(?i).*Chrome.*")) {
            browser = "Chrome";
        } else if (userAgent.matches("(?i).*Safari.*") && !userAgent.matches("(?i).*Chrome.*")) {
            browser = "Safari";
        } else if (userAgent.matches("(?i).*Firefox.*")) {
            browser = "Firefox";
        } else if (userAgent.matches("(?i).*MSIE.*|(?i).*Trident.*")) {
            browser = "Internet Explorer";
        }

        return new DeviceInfo(deviceType, os, browser);
    }
}