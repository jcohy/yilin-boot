package com.yilin.reactive.persistent;

import org.reactivestreams.Publisher;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/20:14:49
 * @since 2023.0.1
 */
public interface UpdateService<T, ID> {

	/**
	 * 修改记录信息.
	 * @param domain 要修改的对象
	 * @return 返回修改的记录
	 */
	Publisher<T> update(T domain);

}
