package io.github.laskowski.shell.utils;

public class OperatingSystem {
    private static OperatingSystem.OS os;

    public enum OS {
        WINDOWS,
        MAC
    }

    public static OperatingSystem.OS get() {
        if (os != null) {
            return os;
        } else {
            String os = System.getProperty("os.name");
            if (os.equals("Windows 10")) {
                return OperatingSystem.os = OperatingSystem.OS.WINDOWS;
            } else {
                return OperatingSystem.os = OperatingSystem.OS.MAC;
            }
        }
    }

    public static String getStringBasedOnOS(String windows, String mac) {
        return OperatingSystem.get().equals(OperatingSystem.OS.WINDOWS) ? windows : mac;
    }
}
