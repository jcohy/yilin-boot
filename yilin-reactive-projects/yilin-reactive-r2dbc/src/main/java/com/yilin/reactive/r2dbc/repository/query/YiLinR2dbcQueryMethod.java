package com.yilin.reactive.r2dbc.repository.query;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.r2dbc.repository.query.R2dbcQueryMethod;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.repository.core.RepositoryMetadata;

import com.yilin.reactive.persistent.annotations.LogicDelete;
import com.yilin.reactive.persistent.annotations.Status;
import com.yilin.reactive.persistent.annotations.TenantId;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/8/3:16:57
 * @since 2023.0.1
 */
public class YiLinR2dbcQueryMethod extends R2dbcQueryMethod {

	private final Optional<LogicDelete> logicDelete;

	private final Optional<TenantId> tenantId;

	private final Optional<Status> status;

	public YiLinR2dbcQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory projectionFactory, MappingContext<? extends RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> mappingContext) {
		super(method, metadata, projectionFactory, mappingContext);
		this.logicDelete = Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(method, LogicDelete.class));
		this.tenantId = Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(method, TenantId.class));
		this.status = Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(method, Status.class));
	}

	public Optional<LogicDelete> getLogicDelete() {
		return logicDelete;
	}

	public Optional<TenantId> getTenantId() {
		return tenantId;
	}

	public Optional<Status> getStatus() {
		return status;
	}

	/**
	 * @return 返回 {@literal true} ,则方法使用 {@link LogicDelete} 注解.
	 */
	public boolean hasAnnotatedLogicDelete() {
		return getLogicDelete().isPresent();
	}

	/**
	 * @return 返回 {@literal true} ,则方法使用 {@link Status} 注解.
	 */
	public boolean hasAnnotatedStatus() {
		return getStatus().isPresent();
	}

	/**
	 * @return 返回 {@literal true} ,则方法使用 {@link TenantId} 注解.
	 */
	public boolean hasAnnotatedTenant() {
		return getTenantId().isPresent();
	}
}
