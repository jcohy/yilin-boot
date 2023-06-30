package com.yilin.boot.utils.classpath;

import com.yilin.boot.utils.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * 描述: .
 *
 * <p>
 * Copyright © 2022 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2022/12/ 10:34
 * @since 1.0.0
 */
public class ClassPathUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassPathUtils.class);

    /**
     * 根据不同的协议创建文件扫描器.
     *
     * @param protocol 协议.
     * @return 扫描器.
     */
    public static ClassPathResourceScanner createLocationScanner(String protocol) {

        if ("file".equals(protocol)) {
            FileSystemClassPathResourceScanner locationScanner = new FileSystemClassPathResourceScanner();
            return locationScanner;
        }

        if ("jar".equals(protocol)) {
            String separator = isTomcat(protocol) ? "*/" : "!/";
            JarFileClassPathResourceScanner locationScanner = new JarFileClassPathResourceScanner(separator);
            return locationScanner;
        }

        return null;
    }


    public static Set<ClassPathResource> findClassPathResource(String location) {
        Set<ClassPathResource> resources = new TreeSet<>();
        for (String resourceName : findResourceNames(location)) {
            resources.add(new ClassPathResource(location, resourceName));
        }
        return resources;
    }

    /**
     * 在类路径中查找在此位置及以下位置的资源名称。
     *
     * @param location location
     * @return /
     */
    public static Set<String> findResourceNames(String location) {

        Set<String> resourceNames = new TreeSet<>();

        List<URL> locationUrls = getResourceUrlsForPath(location);

        locationUrls.forEach(locationUrl -> {
            logger.info("Scanning URL: {}", locationUrl.toExternalForm());

            String protocol = locationUrl.getProtocol();

            ClassPathResourceScanner classPathLocationScanner = createLocationScanner(protocol);

            if (classPathLocationScanner == null) {
                String scanRoot = UrlUtils.toFilePath(locationUrl);
                logger.info("Unable to scan location: {}  (unsupported protocol: {} )", scanRoot, protocol);
            } else {
                Set<String> names = classPathLocationScanner.findResourceNames(location, locationUrl);
                resourceNames.addAll(names);
            }
        });
        return resourceNames;
    }

    /**
     * 获取此类路径上的逻辑物理地址
     *
     * @param location location
     * @return /
     */
    public static List<URL> getResourceUrlsForPath(String location) {
        List<URL> locationUrls = new ArrayList<>();
        Enumeration<URL> urls;
        try {
            urls = Thread.currentThread().getContextClassLoader().getResources(location);
            while (urls.hasMoreElements()) {
                locationUrls.add(urls.nextElement());
            }
        } catch (IOException e) {
            logger.error("Unable to resolve location " + location + ": " + e.getMessage() + ".");
        }
        return locationUrls;
    }

    public static boolean isTomcat(String protocol) {
        return "war".equals(protocol);
    }

    public static boolean isFelix(String protocol) {
        return "bundle".equals(protocol);
    }

    public static boolean isEquinox(String protocol) {
        return "bundleresource".equals(protocol);
    }
}
