package com.suhoi.demo.annotation;

import com.suhoi.demo.model.AccessType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAccessByBoard {
    AccessType value();
}

