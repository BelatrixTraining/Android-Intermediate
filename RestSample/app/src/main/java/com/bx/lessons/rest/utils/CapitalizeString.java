package com.bx.lessons.rest.utils;

/**
 * Created by eduardomedina on 9/11/16.
 */
public class CapitalizeString extends Object {
    private final String value;

    public CapitalizeString(String value) {
        this.value = value;
    }

    public  String first()
    {
        if(this.value.isEmpty())return "";
        int length= this.value.length();
        String first =this.value.substring(0,1);
        first= first.toUpperCase();
        String capitalize= first+this.value.substring(1,length);
        return capitalize;
    }
}
