package com.yilin.reactive.utils.classpath;


import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 描述: .
 * <p>
 * Copyright © 2023
 * <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jcohy
 * @version 2024.0.1 2023/3/7 10:50
 * @since 2024.0.1
 */
public class JarFileClassPathResourceScanner implements ClassPathResourceScanner {

	private static final Logger logger = LoggerFactory.getLogger(JarFileClassPathResourceScanner.class);

	/**
	 * 分隔符，在 URL 中分隔 jar 文件名和 jar 中的文件
	 */
	private final String separator;

	public JarFileClassPathResourceScanner(String separator) {
		this.separator = separator;
	}

	@Override
	public Set<String> findResourceNames(String location, URL locationUrl) {

		JarFile jarFile;

		try {
			jarFile = getJarFromUrl(locationUrl);
		}
		catch (IOException e) {
			logger.warn("Unable to determine jar from url (" + locationUrl + "): " + e.getMessage());
			return Collections.emptySet();
		}

		try {
			// For Tomcat and non-expanded WARs.
			String prefix = jarFile.getName().toLowerCase(Locale.ENGLISH).endsWith(".war") ? "WEB-INF/classes/" : "";
			return findResourceNamesFromJarFile(jarFile, prefix, location);
		}
		finally {
			try {
				jarFile.close();
			}
			catch (IOException e) {
				// Ignore
			}
		}
	}

	/**
	 * 查找 Jar 文件中指定目录包含的所有资源名称
	 * @param jarFile jar 文件
	 * @param prefix jar 文件中忽略的前缀
	 * @param location 位置
	 * @return 资源名
	 */
	private Set<String> findResourceNamesFromJarFile(JarFile jarFile, String prefix, String location) {
		String toScan = prefix + location + (location.endsWith("/") ? "" : "/");
		Set<String> resourceNames = new TreeSet<>();

		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			String entryName = entries.nextElement().getName();
			if (entryName.startsWith(toScan)) {
				resourceNames.add(entryName.substring(prefix.length()));
			}
		}

		return resourceNames;
	}

	private JarFile getJarFromUrl(URL locationUrl) throws IOException {

		URLConnection con = locationUrl.openConnection();
		if (con instanceof JarURLConnection) {
			JarURLConnection jarCon = (JarURLConnection) con;
			jarCon.setUseCaches(false);
			return jarCon.getJarFile();
		}

		// 没有 JarURLConnection ->需要使用 URL 文件解析。
		// 假设 URLs 格式为 "jar:path!/entry", 带有协议
		// 可以是任意的，只要遵循输入格式。同时也会处理有或没有 "file:" 前缀的路径.
		String urlFile = locationUrl.getFile();

		int separatorIndex = urlFile.indexOf(separator);

		if (separatorIndex != -1) {
			String jarFileUrl = urlFile.substring(0, separatorIndex);
			if (jarFileUrl.startsWith("file:")) {
				try {
					return new JarFile(new URL(jarFileUrl).toURI().getSchemeSpecificPart());
				}
				catch (URISyntaxException ex) {
					// Fallback for URLs that are not valid URIs (should hardly ever
					// happen).
					return new JarFile(jarFileUrl.substring("file:".length()));
				}
			}
			return new JarFile(jarFileUrl);
		}
		return new JarFile(urlFile);
	}

}
