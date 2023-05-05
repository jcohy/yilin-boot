package com.saga.boot.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 描述: .
 * <p>
 * Copyright © 2022 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 * </p>
 *
 * @author jiac
 * @version 1.0.0 2022/12/20 13:10
 * @since 1.0.0
 */
public class UrlUtils {

    private UrlUtils() {
        // Do nothing
    }

    /**
     * url 转文件路径，删除最后一个斜杠，如果存在.
     *
     * @param url url.
     * @return The file path.
     */
    public static String toFilePath(URL url) {
        String filePath = new File(decodeURL(url.getPath().replace("+", "%2b"))).getAbsolutePath();
        if (filePath.endsWith("/")) {
            return filePath.substring(0, filePath.length() - 1);
        }
        return filePath;
    }

    /**
     * url 解码.
     *
     * @param url url.
     * @return 解码 url.
     */
    public static String decodeURL(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }

}
