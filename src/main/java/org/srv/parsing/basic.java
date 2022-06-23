package org.srv.parsing;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface basic {
    int start = -1;
    int length = -1;

    String type = "UTF-8";

    int start();

    int length();

}
