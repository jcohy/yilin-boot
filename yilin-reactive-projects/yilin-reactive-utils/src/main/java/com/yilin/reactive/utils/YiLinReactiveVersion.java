package com.yilin.reactive.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/**
 * 描述: .
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2023/7/13 11:04
 * @since 1.0.0
 */
public class YiLinReactiveVersion {

    private YiLinReactiveVersion() {}

    public static final long SERIAL_VERSION_UID = getVersion().hashCode();

    public static String getVersion() {
        return determineYiLinReactiveVersion();
    }

    private static String determineYiLinReactiveVersion() {
        return Optional.ofNullable(YiLinReactiveVersion.class.getPackage().getImplementationVersion()).orElseGet(YiLinReactiveVersion::getManifestVewrsion);
    }

    private static String getManifestVewrsion() {
        return Optional.ofNullable(YiLinReactiveVersion.class.getProtectionDomain().getCodeSource())
                .map(source -> {
            try {
                var location = source.getLocation();
                var urlConnection = location.openConnection();
                if (urlConnection instanceof JarURLConnection connection) {
                    return getImplementationVersion(connection.getJarFile());
                }
                JarFile jarFile = new JarFile(new File(location.toURI()));
                return getImplementationVersion(jarFile);
            } catch (Exception ex) {
                return "";
            }
        }).get();
    }

    private static String getImplementationVersion(JarFile jarFile) throws IOException {
        return jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }
}