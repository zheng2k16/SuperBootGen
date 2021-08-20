package com.ssx.utils;

public enum  PomLevel {
    PARENT("    - [Top Level]"),
    CHILDREN("    - [Sub Level]"),
    ;

    private final String pomLevel;
    PomLevel(String pomLevel) {
        this.pomLevel = pomLevel;
    }

    @Override
    public String toString() {
        return this.pomLevel;
    }
}
