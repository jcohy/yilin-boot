package com.yilin.reactive.persistent;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: 基础的 CURD.
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/20 14:51
 * @since 2023.0.1
 */
public interface CrudService<T, ID>
		extends InsertService<T, ID>, UpdateService<T, ID>, DeleteService<ID>, SelectService<T, ID> {

}
