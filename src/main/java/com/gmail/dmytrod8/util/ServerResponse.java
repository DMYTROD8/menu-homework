package com.gmail.dmytrod8.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ServerResponse {

    private static synchronized String buildJsonResponse(String[] array) {
        JsonObject jsonObj = new JsonObject();

        if (array.length > 0) {

            for (int i = 0; i < array.length; i++) {
                try {
                    String[] arr = array[i].split(":");
                    jsonObj.addProperty(arr[0], arr[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return new Gson().toJson(jsonObj);
    }

    private static synchronized String buildJsonResponse(JsonElement jsonElement, String[] array) {
        if (array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                try {
                    String[] arr = array[i].split(":");
                    jsonElement.getAsJsonObject().addProperty(arr[0], arr[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return new Gson().toJson(jsonElement);
    }

    public static synchronized void sendResponse(HttpServletResponse resp, String... array) throws IOException {
        String json = buildJsonResponse(array);

        OutputStream os = resp.getOutputStream();
        byte[] buf = json.getBytes(StandardCharsets.UTF_8);
        os.write(buf);
    }

    public static synchronized void sendResponse(HttpServletResponse resp, JsonElement jsonElement, String... array)
            throws IOException {
        String json = buildJsonResponse(jsonElement, array);

        OutputStream os = resp.getOutputStream();
        byte[] buf = json.getBytes(StandardCharsets.UTF_8);
        os.write(buf);
    }
}