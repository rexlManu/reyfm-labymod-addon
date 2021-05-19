package de.rexlmanu.reyfm.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.net.Socket;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class Utils {

    public static SSLContext sslContext;

    static {
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509ExtendedTrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }}, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static String streamUrl(String sender, Quality quality) {
        return String.format("https://listen.reyfm.de/%s_%skbps.mp3", sender, quality.getUrl());
    }

    public static String substring(String value, int maximum) {
        if (value.length() > maximum) {
            value = value.substring(0, maximum).trim() + "...";
        }
        return value;
    }

    public static void allowCerts(URLConnection urlConnection) {
        if (urlConnection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslContext.getSocketFactory());
        }
    }

    

}
