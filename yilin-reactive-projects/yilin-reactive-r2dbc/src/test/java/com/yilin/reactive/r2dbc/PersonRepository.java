package com.yilin.reactive.r2dbc;

import org.springframework.stereotype.Repository;

import com.yilin.reactive.r2dbc.domain.Person;
import com.yilin.reactive.r2dbc.repository.ReactiveRepository;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/31:14:26
 * @since 2023.0.1
 */
@Repository
interface PersonRepository extends ReactiveRepository<Person, Long> {}
