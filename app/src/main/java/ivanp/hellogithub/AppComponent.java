package ivanp.hellogithub;

import javax.inject.Singleton;

import dagger.Component;
import ivanp.hellogithub.data.UsersAdapter;
import ivanp.hellogithub.mvp.UsersPresenter;
import ivanp.hellogithub.ui.MainActivity;
import ivanp.hellogithub.ui.UsersFragment;

@SuppressWarnings("WeakerAccess")
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(UsersAdapter usersAdapter);

    UsersPresenter usersPresenter();

    void inject(UsersFragment usersFragment);
}
