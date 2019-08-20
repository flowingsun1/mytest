package com.transwarp.nxms.elasticsearch.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Author zgw
 * @Date 2019/03/15
 */
public class LoadResourceUtils {
    public static InputStream getReader(String path) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        return inputStream;
    }

    public static JsonObject getJsonObject(Gson gson, String path) {
        InputStream reader = getReader(path);
        return gson.fromJson(new InputStreamReader(reader), JsonObject.class);
    }

    public static Properties getProperties(Properties properties, String path) throws IOException {
        properties.load(getReader(path));
        return properties;
    }

}