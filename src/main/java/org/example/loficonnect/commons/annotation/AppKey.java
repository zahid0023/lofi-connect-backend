package org.example.loficonnect.commons.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)   // can be used on methods
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppKey {
}
