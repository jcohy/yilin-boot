package com.yilin.reactive.persistent;

import java.util.List;

import org.reactivestreams.Publisher;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/20 14:50
 * @since 2023.0.1
 */
public interface StateService<T, ID> {

	/**
	 * 更新状态.
	 * @param ids ids
	 * @param status state
	 * @return /
	 */
	Publisher<Boolean> state(List<Long> ids, Integer status);

}
