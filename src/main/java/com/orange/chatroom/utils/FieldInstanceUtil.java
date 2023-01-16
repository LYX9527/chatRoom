package com.orange.chatroom.utils;

import java.lang.reflect.Field;

/**
 * @author : yilantingfeng
 * @version : v1.0
 * @projectName : chatroom
 * @package : com.orange.chatroom.utils
 * @className : FieldInstanceUtil
 * @description:
 * @date : 2023/1/11 12:41
 */
public class FieldInstanceUtil {
    public static Object getFieldInstance(Object obj, String fieldPath) {
        String[] fields = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }
        return obj;
    }

    private static Object getField(Object obj, Class<?> clazz, String fieldName) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }
}
