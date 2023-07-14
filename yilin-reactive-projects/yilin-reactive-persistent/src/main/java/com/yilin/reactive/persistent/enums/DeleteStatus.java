package com.yilin.reactive.persistent.enums;

/**
 * 描述: 删除状态.
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jiac
 * @version 1.0.0 2023/7/13 14:46
 * @since 1.0.0
 */
public enum DeleteStatus {

    /**
     * 正常状态.
     */
    NORMAL(0),

    /**
     * 删除状态.
     */
    DELETED(1);

    private final int code;

    DeleteStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}