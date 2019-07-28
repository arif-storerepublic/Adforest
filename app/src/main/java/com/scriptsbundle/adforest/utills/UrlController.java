package com.scriptsbundle.adforest.utills;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.scriptsbundle.adforest.utills.Network.AuthenticationInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UrlController {
        static OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
    private static String Purchase_code = "Your Purchase code is here";
    private static String Custom_Security = "your Custom Security is here/ Can be any random String";
    private static String IP_ADDRESS = "http://YourDomainName.com/";
    private static String Base_URL = IP_ADDRESS + "wp-json/adforest/v1/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password, Context context) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken, context);
        }
        return createService(serviceClass, null, null, context);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken, Context context) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken, context);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

    public static Map<String, String> AddHeaders(Context context) {
        Map<String, String> map = new HashMap<>();
        if (SettingsMain.isSocial(context)) {
            map.put("AdForest-Login-Type", "social");
        }
        map.put("Purchase-Code", Purchase_code);
        map.put("custom-security", Custom_Security);
        map.put("Adforest-Request-From", "android");
        map.put("Adforest-Lang-Locale", SettingsMain.getLanguageCode());
        map.put("Content-Type", "application/json");
        map.put("Cache-Control", "max-age=640000");
        return map;
    }

    public static Map<String, String> UploadImageAddHeaders(Context context) {
        Map<String, String> map = new HashMap<>();
        if (SettingsMain.isSocial(context)) {
            map.put("AdForest-Login-Type", "social");
        }
        map.put("Purchase-Code", Purchase_code);
        map.put("custom-security", Custom_Security);
        map.put("Adforest-Lang-Locale", SettingsMain.getLanguageCode());
        map.put("Adforest-Request-From", "android");
        map.put("Cache-Control", "max-age=640000");
        return map;
    }
}
