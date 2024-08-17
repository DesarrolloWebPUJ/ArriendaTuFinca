package com.dreamteam.arriendatufinca.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Estado {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String value;


    Estado(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }
}
