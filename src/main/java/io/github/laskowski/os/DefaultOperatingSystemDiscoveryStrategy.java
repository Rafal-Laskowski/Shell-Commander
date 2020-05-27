package io.github.laskowski.os;

public class DefaultOperatingSystemDiscoveryStrategy implements OperatingSystemDiscoveryStrategy {
    private static DefaultOperatingSystemDiscoveryStrategy instance;
    private String os = System.getProperty("os.name");

    public static DefaultOperatingSystemDiscoveryStrategy getInstance() {
        if (instance == null) {
            instance = new DefaultOperatingSystemDiscoveryStrategy();
        }

        return instance;
    }

    private DefaultOperatingSystemDiscoveryStrategy() {}

    @Override
    public OperatingSystem getOS() {
        if (os.contains("Windows")) {
            return OperatingSystem.WINDOWS;
        } else if (os.contains("mac")) {
            return OperatingSystem.MAC;
        } else if (os.contains("Linux")) {
            return OperatingSystem.LINUX;
        } else {
            throw new IllegalArgumentException(String.format("Unrecognized Operating System: %s", os));
        }
    }
}
