package org.bluk.auth.api;

import org.bluk.auth.AuthPlugin;
import org.bluk.auth.api.exceptions.ApiException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;

public class APIServer {
    private static final String defaultApiUrl = AuthPlugin.getInstance().getConfig().getString("api-url");

    public String sendRequest(String path, HTTPRequestMethod requestMethod, String data, Map<String, String> headers) throws ApiException {
        // Create a get request to server-api
        URL obj;
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();

        try {
            obj = new URL(defaultApiUrl.concat(path));
            conn = (HttpURLConnection) obj.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);

            // Headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            switch(requestMethod) {
                case GET: {
                    conn.setRequestMethod("GET");
                    break;
                }

                case POST: {
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);
                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = data.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                    break;
                }
            }

            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

                String line;
                while((line = in.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch(Throwable cause) {
            AuthPlugin.getInstance().getLogger().log(Level.WARNING, String.format("[APIServer] Error while making request: %s", cause.getMessage()));
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return sb.toString();
    }
}