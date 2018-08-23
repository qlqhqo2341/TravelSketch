package com.livenoproblem.travelsketch.Struct;

import java.io.Serializable;

public class Space implements Serializable {
    public final String id, description;
    public Space(String id, String description){
        this.id=id; this.description=description;
    }

    @Override
    public String toString() {
        return description.toString();
    }
}
