package com.psz.restdemo.security;

import org.springframework.util.Assert;

import lombok.Data;

@Data
public class SecurityLabel {
    public enum LabelType {TENANT, SOMETHING_ELSE};
    private final LabelType lableType;
    private final String label;

    public static final SecurityLabel getLabel(LabelType type, String label){
        Assert.notNull(type, "Type must be provided");
        Assert.notNull(label, "Label must be provided");
        return new SecurityLabel(type, label);
    }
}
