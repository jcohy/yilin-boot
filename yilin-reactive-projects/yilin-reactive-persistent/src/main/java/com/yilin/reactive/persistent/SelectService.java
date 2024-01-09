package com.yilin.reactive.persistent;

import org.reactivestreams.Publisher;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jcohy
 * @version 2024.0.1 2023/7/20 14:49
 * @since 2024.0.1
 */
public interface SelectService<T, ID> {

	/**
	 * 根据主键查询.
	 * @param id 主键
	 * @return 查询结果, 无结果时返回{@code null}
	 */
	Publisher<T> get(ID id);

	/**
	 * 条件查询.
	 * @param criteria criteria
	 * @return 查询结果
	 */
	Publisher<T> get(Criteria criteria);

	/**
	 * 根据多个主键查询.
	 * @param ids 主键集合
	 * @return 查询结果, 如果无结果返回空集合
	 */
	Publisher<T> query(Iterable<ID> ids);

	/**
	 * 查询所有结果.
	 * @return 所有结果, 如果无结果则返回空集合
	 */
	Publisher<T> queryAll();

}
