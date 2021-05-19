package de.rexlmanu.reyfm.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {

    public static final JsonParser JSON_PARSER = new JsonParser();

    public static JsonObject sendRequestAndReceiveJson(String url) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        Utils.allowCerts(urlConnection);
        return JSON_PARSER.parse(IOUtils.toString(urlConnection.getInputStream())).getAsJsonObject();
    }

}
