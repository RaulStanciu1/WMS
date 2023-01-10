package com.wms.data.misuse;

public record Misuse(int culprit, String details,MisuseType type) {
    public String toString(){
               return  "   Culprit ID: " + culprit + "\n"+
                "   Misuse Details: " + details;
    }
}
