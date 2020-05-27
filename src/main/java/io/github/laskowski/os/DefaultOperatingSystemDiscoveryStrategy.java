package io.github.laskowski.os;

public class DefaultOperatingSystemDiscoveryStrategy implements OperatingSystemDiscoveryStrategy {
    private String os = System.getProperty("os.name");

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
