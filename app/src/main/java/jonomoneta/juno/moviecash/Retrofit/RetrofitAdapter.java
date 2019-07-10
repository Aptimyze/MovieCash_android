package jonomoneta.juno.moviecash.Retrofit;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Dilip on 27-02-2017.
 */

public class RetrofitAdapter {
    //    local: http://192.168.0.3:81/
//    live: "http://arjuntours.co.in
//    private static String BASE_URL = "http://192.168.0.3:81/api/";
    private static String BASE_URL = "https://moviecashapi.junomoneta.io/api/ApplicationApi/";
    private static String BASE_URL_PLACE = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
    String nearBySearchAPIKey = "AIzaSyDilAKJ_vJ3rCCxR34RsEa2nyK3S2h6gL0";

    private static String BASE_URL_VIDEO = "http://3edc5f2d.ngrok.io/api/ApplicationApi/";

    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(interceptor).build();

    public static Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    public static Retrofit retrofitPlace =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL_PLACE)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    public static Retrofit retrofitVideo =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL_VIDEO)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

}
