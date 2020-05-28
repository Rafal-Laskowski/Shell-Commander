package io.github.laskowski.os;

public class DefaultOperatingSystemDiscoveryStrategy implements OperatingSystemDiscoveryStrategy {
    private static DefaultOperatingSystemDiscoveryStrategy instance;
    private static final String OPERATING_SYSTEM_NAME = System.getProperty("os.name").toLowerCase();

    public static DefaultOperatingSystemDiscoveryStrategy getInstance() {
        if (instance == null) {
            instance = new DefaultOperatingSystemDiscoveryStrategy();
        }

        return instance;
    }

    private DefaultOperatingSystemDiscoveryStrategy() {}

    @Override
    public OperatingSystem getOS() {
        if (OPERATING_SYSTEM_NAME.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (OPERATING_SYSTEM_NAME.contains("mac")) {
            return OperatingSystem.MAC;
        } else if (OPERATING_SYSTEM_NAME.contains("nix") || OPERATING_SYSTEM_NAME.contains("nux") || OPERATING_SYSTEM_NAME.contains("aix")) {
            return OperatingSystem.UNIX;
        } else {
            throw new IllegalArgumentException(String.format("Unrecognized Operating System: %s", OPERATING_SYSTEM_NAME));
        }
    }
}
