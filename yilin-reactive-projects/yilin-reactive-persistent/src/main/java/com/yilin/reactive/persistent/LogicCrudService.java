package com.yilin.reactive.persistent;

import org.reactivestreams.Publisher;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/20:14:51
 * @since 2023.0.1
 */
public interface LogicCrudService<T, ID> extends CrudService<T, ID> {

	/**
	 * 根据主键查询并判断此数据是否被逻辑删除.
	 * @param id 主键
	 * @return 查询结果, 无结果时返回{@code null}
	 */
	Publisher<T> getNotDeleted(ID id);

	/**
	 * 根据所有未被逻辑删除的实体.
	 * @return 查询结果, 无结果时返回{@code null}
	 */
	Publisher<T> getAllNotDeleted();

	/**
	 * 根据主键删除记录,逻辑删除.
	 * @param id 主键
	 */
	void logicDelete(ID id);

	/**
	 * 根据主键删除记录,逻辑删除.
	 * @param ids 主键集合
	 */
	void logicDelete(Iterable<ID> ids);

}
