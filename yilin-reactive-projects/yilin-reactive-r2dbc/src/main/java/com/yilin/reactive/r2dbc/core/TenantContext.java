package com.yilin.reactive.r2dbc.core;

/**
 * Copyright: Copyright (c) 2022 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/17:23:25
 * @since 2023.0.1
 */
public interface TenantContext {

    String getCurrentTenantId();

    default String defaultGetCurrentTenantId() {
        return "000000";
    }
}
