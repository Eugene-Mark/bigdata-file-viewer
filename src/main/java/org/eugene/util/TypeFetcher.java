package org.eugene.util;

public class TypeFetcher {
    public static String INT = "int";
    public static String LONG = "long";
    public static String DOUBLE = "double";
    public static String FLOAT = "float";
    public static String STRING = "string";

    public static String getType(String schema){
        if (schema.contains(INT))
            return INT;
        if (schema.contains(LONG))
            return LONG;
        if (schema.contains(DOUBLE))
            return DOUBLE;
        if (schema.contains(FLOAT))
            return FLOAT;
        if (schema.contains(STRING))
            return STRING;
        return "";
    }
}
