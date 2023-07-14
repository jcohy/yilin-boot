package com.yilin.reactive.persistent.enums;

/**
 * 描述: 业务状态.
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2023/7/13 14:47
 * @since 1.0.0
 */
public enum ServiceStatus {

    /**
     * 禁止.
     */
    DISABLE("false"),

    /**
     * 启用.
     */
    ENABLE("true");

    private final String label;

    ServiceStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
