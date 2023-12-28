package com.yilin.reactive.persistent;

import org.reactivestreams.Publisher;

/**
 * 描述: 插入.
 *
 * <p>
 * Copyright © 2023 <a href="https://www.jcohy.com" target= "_blank">https://www.jcohy.com</a>
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/13 14:49
 * @since 2023.0.1
 */
public interface InsertService<T,ID> {

	/**
	 * 添加一条数据.
	 * @param domain 实体
	 * @return /
	 */
	Publisher<T> insert(T domain);
}
