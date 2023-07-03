package com.yilin.boot.configuration.processor;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

/**
 * Copyright: Copyright (c) 2022 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/3:23:20
 * @since 2023.0.1
 */
@SupportedAnnotationTypes({ "com.yilin.boot.configuration.processor.annotations.YiLinAutoService" })
public class YiLinAutoServiceProcessor extends AbstractConfigureAnnotationProcessor {


    @Override
    protected boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}
