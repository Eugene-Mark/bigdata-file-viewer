package org.eugene.util;

public class TypeFetcher {
    private final static String INT = "int";
    private final static String LONG = "long";
    private final static String DOUBLE = "double";
    private final static String FLOAT = "float";
    private final static String STRING = "string";

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
