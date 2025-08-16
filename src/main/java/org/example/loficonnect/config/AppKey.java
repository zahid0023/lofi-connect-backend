package org.example.loficonnect.config;

import java.lang.annotation.*;

@Target(ElementType.METHOD)   // can be used on methods
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppKey {
}
