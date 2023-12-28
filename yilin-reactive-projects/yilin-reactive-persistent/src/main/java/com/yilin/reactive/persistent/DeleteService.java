package com.yilin.reactive.persistent;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description: Delete.
 *
 * @author jcohy
 * @version 2023.0.1 2023/7/20 14:50
 * @since 2023.0.1
 */
public interface DeleteService<ID> {

	/**
	 * 根据主键删除记录,物理删除.
	 * @param id 主键
	 */
	void delete(ID id);

	/**
	 * 根据主键删除记录,物理删除.
	 * @param ids 主键集合
	 */
	void delete(Iterable<ID> ids);

	/**
	 * 清空表数据.
	 */
	void clear();

}
