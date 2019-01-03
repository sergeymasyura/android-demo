package com.masyura.demo.util;

import android.util.Log;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import com.masyura.demo.type.CustomType;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApolloClientCreator {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = ApolloClientCreator.class.getSimpleName();

    public static ApolloClient create(String serverUrl, String apiToken) {

        final HttpLoggingInterceptor.Logger logger = message -> {
            if (!message.isEmpty()) {
                Log.d(LOG_TAG, "HttpInterceptor: " + message);
            }
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final Interceptor authorizationInterceptor = chain -> {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder().method(original.method(), original.body());
            builder.header("Authorization", "bearer " + apiToken);
            return chain.proceed(builder.build());
        };

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(authorizationInterceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(serverUrl)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.DATETIME, new DateCustomTypeAdapter())
                .addCustomTypeAdapter(CustomType.URI, new UriCustomTypeAdapter())
                .build();

        return apolloClient;
    }

    private static class UriCustomTypeAdapter implements CustomTypeAdapter<String> {
        @Override
        public String decode(CustomTypeValue value) {
            return value.value.toString();
        }

        @NotNull
        @Override
        public CustomTypeValue encode(@NotNull String value) {
            return new CustomTypeValue.GraphQLString(value);
        }
    }

    private static class DateCustomTypeAdapter implements CustomTypeAdapter<Date> {

        private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        private static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

        static {
            FORMAT.setTimeZone(GMT_TIME_ZONE);
        }

        @Override
        public Date decode(CustomTypeValue value) {
            try {
                return FORMAT.parse(value.value.toString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        @NotNull
        @Override
        public CustomTypeValue encode(@NotNull Date value) {
            return new CustomTypeValue.GraphQLString(FORMAT.format(value));
        }
    }
}
