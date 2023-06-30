package com.yilin.boot.utils.classpath;

import com.yilin.boot.utils.UrlUtils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 描述: .
 *
 * <p>
 * Copyright © 2022 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2022/12/29 19:01
 * @since 1.0.0
 */
public class ClassPathResource implements Comparable<ClassPathResource> {

    private final String rootPath;

    private final String fileNameWithAbsolutePath;

    private final String fileNameWithRelativePath;

    private final ClassLoader classLoader;

    private final Charset encoding;


    public ClassPathResource(String path) {
        this(path.substring(0, path.lastIndexOf(File.separator)), path);
    }

    public ClassPathResource(String rootPath, String fileNameWithAbsolutePath) {
        this(rootPath, fileNameWithAbsolutePath, Thread.currentThread().getContextClassLoader(), StandardCharsets.UTF_8);
    }

    public ClassPathResource(String rootPath, String fileNameWithAbsolutePath, ClassLoader classLoader, Charset encoding) {
        this.rootPath = rootPath;
        this.fileNameWithAbsolutePath = fileNameWithAbsolutePath;
        this.fileNameWithRelativePath = rootPath == null ? fileNameWithAbsolutePath : getPathRelativeToThis(fileNameWithAbsolutePath);
        this.classLoader = classLoader;
        this.encoding = encoding;
    }

    public String getRelativePath() {
        return fileNameWithRelativePath;
    }

    public String getAbsolutePath() {
        return fileNameWithAbsolutePath;
    }

    public String getAbsolutePathOnDisk() {
        URL url = getUrl();
        if (url == null) {
            throw new NullPointerException("Unable to find resource on disk: " + fileNameWithAbsolutePath);
        }
        return new File(UrlUtils.decodeURL(url.getPath())).getAbsolutePath();
    }

    private URL getUrl() {
        return classLoader.getResource(fileNameWithAbsolutePath);
    }


    public Reader read() {
        InputStream inputStream = classLoader.getResourceAsStream(fileNameWithAbsolutePath);
        if (inputStream == null) {
            throw new RuntimeException("Unable to obtain inputstream for resource: " + fileNameWithAbsolutePath);
        }
        return new InputStreamReader(inputStream, encoding.newDecoder());
    }

    public String getFilename() {
        return fileNameWithAbsolutePath.substring(fileNameWithAbsolutePath.lastIndexOf("/") + 1);
    }

    public boolean exists() {
        return getUrl() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassPathResource that = (ClassPathResource) o;

        return fileNameWithAbsolutePath.equals(that.fileNameWithAbsolutePath);
    }

    @Override
    public int hashCode() {
        return fileNameWithAbsolutePath.hashCode();
    }

    private String getPathRelativeToThis(String path) {
        return rootPath.length() > 0 ? path.substring(rootPath.length() + 1) : path;
    }

    @Override
    public int compareTo(ClassPathResource o) {
        return getRelativePath().compareTo(o.getRelativePath());
    }
}
