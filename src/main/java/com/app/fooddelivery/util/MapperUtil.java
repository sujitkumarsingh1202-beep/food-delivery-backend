package com.app.fooddelivery.util;

public class MapperUtil {

    public static <T> T map(Object source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Mapping error", e);
        }
    }
}
