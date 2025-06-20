package com.bcc.washer.domain;

public enum TemplateTYPE {
    REZERVATION_CREATED("src/main/resources/templates/rezervation_created.html"),
    WELCOME_MESSAGE("src/main/resources/templates/welcome_message.html"),
    EMPTY("");

    private final String path;

    private TemplateTYPE(final String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
