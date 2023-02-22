package com.rusmansur.srgminstaller;
import java.io.File;
import java.net.URISyntaxException;

public class GetJarPath {
    static File getPath() {
        File file;
        try {
            file = new File(
                    SRGMInstaller.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
                            .getPath())
                    .getParentFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
