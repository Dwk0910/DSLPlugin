package org.dslofficial.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedData {
    public static class CustomSharedData<T> {
        private final Map<String, T> data = new ConcurrentHashMap<>();
        public void setData(String key, T data) {
            this.data.put(key, data);
        }

        public T getData(String key) {
            return this.data.get(key);
        }

        public void removeData(String key) {
            this.data.remove(key);
        }
    }

    private final Map<String, Boolean> booleanMap = new ConcurrentHashMap<>();

    public void setBoolean(String key, Boolean value) {
        booleanMap.put(key, value);
    }

    public Boolean getBoolean(String key) {
        return booleanMap.get(key);
    }
}
