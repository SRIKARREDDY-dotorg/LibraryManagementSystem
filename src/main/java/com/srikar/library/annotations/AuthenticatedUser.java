package com.srikar.library.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) // Support both method and class levels
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedUser {
}
