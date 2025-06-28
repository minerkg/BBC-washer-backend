package com.bcc.washer.domain;

public enum TemplateTYPE {
    RESERVATION_CREATED("src/main/resources/templates/rezervation_created.html"),
    WELCOME_MESSAGE("src/main/resources/templates/welcome_message.html"),
    RESERVATION_CANCELLED("src/main/resources/templates/rezervation_cancelled.html"),
    EMPTY("");

    private final String path;

    private TemplateTYPE(final String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
