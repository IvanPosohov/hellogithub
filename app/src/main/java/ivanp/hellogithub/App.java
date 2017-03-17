package ivanp.hellogithub;

import android.app.Application;

public class App extends Application {
    /**
     * Something like preprocessor directive, used for enable/disable unstable
     * code or debugging features like logging. If true all debug stuff is
     * enabled
     */
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private static AppComponent appComponent;
    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

}
