package com.dreamteam.arriendatufinca.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Estado {
    INACTIVE("Inactive"),
    ACTIVE("Active");

    private String value;


    Estado(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }
}
