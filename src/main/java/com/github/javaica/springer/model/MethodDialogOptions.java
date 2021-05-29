package com.github.javaica.springer.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MethodDialogOptions {
    boolean get;
    boolean post;
    boolean put;
    boolean delete;
}
