package ivanp.hellogithub;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ivanp.hellogithub.mvp.UsersPresenter;
import ivanp.hellogithub.utils.Picasso;

@SuppressWarnings("WeakerAccess")
@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext() { return context; }

    @Provides
    @Singleton
    public Picasso providePicasso() { return new Picasso(context); }

    @Provides
    @Singleton
    public UsersPresenter provideUsersPresenter() {
        return new UsersPresenter();
    }
}
