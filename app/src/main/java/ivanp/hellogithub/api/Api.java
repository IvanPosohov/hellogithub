package ivanp.hellogithub.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ivanp.hellogithub.App;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Api {
    private static final String PROD = "https://api.github.com/";

    public static final ApiService service;

    static {
        Gson gson = new GsonBuilder().create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (App.DEBUG) {
            httpClient.interceptors().add(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PROD)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient.build())
                .build();

        service = retrofit.create(ApiService.class);
    }

    public interface ApiService {
        @GET("search/users")
        Observable<User.Response> users(@Query("q") String query, @Query("page") int page, @Query("per_page") int perPage);
    }

    public static int getErrorCode(Throwable throwable, int defaultCode) {
        if (throwable instanceof HttpException) {
            return ((HttpException) throwable).code();
        }
        return defaultCode;
    }

    public static void logError(Throwable throwable) {
        if (App.DEBUG) Log.w("Api", "Request failed", throwable);
    }
}