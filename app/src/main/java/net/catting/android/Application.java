package net.catting.android;

import net.catting.android.api.IApi;
import net.catting.android.data.S;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Application extends android.app.Application {
    public static IApi api;
    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(S.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api=retrofit.create(IApi.class);
    }
}
