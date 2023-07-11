package com.yilin.reactive.utils.classpath;

import com.yilin.reactive.utils.UrlUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

/**
 * 描述: 处理文件系统类路径资源.
 * <p>
 * Copyright © 2023
 * <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2023/3/7 10:23
 * @since 1.0.0
 */
public class FileSystemClassPathResourceScanner implements ClassPathResourceScanner {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemClassPathResourceScanner.class);

	@Override
	public Set<String> findResourceNames(String location, URL locationUrl) {
		String filePath = UrlUtils.toFilePath(locationUrl);

		File folder = new File(filePath);
		if (!folder.isDirectory()) {
			logger.debug("Skipping path as it is not a directory: {}", filePath);
			return new TreeSet<>();
		}

		String classPathRootOnDisk = filePath.substring(0, filePath.length() - location.length());

		if (!classPathRootOnDisk.endsWith(File.separator)) {
			classPathRootOnDisk = classPathRootOnDisk + File.separator;
		}

		logger.debug("Scanning starting at classpath root in filesystem: {}", classPathRootOnDisk);

		return findResourceNamesFromFileSystem(classPathRootOnDisk, location, folder);
	}

	/**
	 * 查找此文件系统文件夹中包含的所有资源名称
	 * @param classPathRootOnDisk 类路径根位置，后面带斜杠
	 * @param scanRootLocation 扫描在类路径上的根位置，不带前面或后面的斜杠
	 * @param folder 在磁盘下查找资源的文件夹
	 * @return 资源名称
	 */
	private Set<String> findResourceNamesFromFileSystem(String classPathRootOnDisk, String scanRootLocation,
			File folder) {
		logger.debug("Scanning for resources in path: " + folder.getPath() + " (" + scanRootLocation + ")");

		Set<String> resourceNames = new TreeSet<>();

		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.canRead()) {
				if (file.isDirectory()) {
					if (file.isHidden()) {
						// #1807: Skip hidden directories to avoid issues with Kubernetes
						logger.debug("Skipping hidden directory: " + file.getAbsolutePath());
					}
					else {
						resourceNames
								.addAll(findResourceNamesFromFileSystem(classPathRootOnDisk, scanRootLocation, file));
					}
				}
				else {
					resourceNames.add(toResourceNameOnClasspath(classPathRootOnDisk, file));
				}
			}
		}

		return resourceNames;
	}

	/**
	 * 将此文件转换为类路径上的资源名.
	 * @param classPathRootOnDisk 类路径根位置，后面带斜杠.
	 * @param file 文件.
	 * @return 类路径的资源.
	 */
	private String toResourceNameOnClasspath(String classPathRootOnDisk, File file) {
		String fileName = file.getAbsolutePath().replace("\\", "/");

		// Cut off the part on disk leading to the root of the classpath
		// This leaves a resource name starting with the scanRootLocation,
		// with no leading slash, containing subDirs and the fileName.
		return fileName.substring(classPathRootOnDisk.length());
	}

}
