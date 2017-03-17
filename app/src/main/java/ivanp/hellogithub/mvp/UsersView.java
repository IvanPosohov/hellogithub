package ivanp.hellogithub.mvp;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

import ivanp.hellogithub.api.User;

public interface UsersView extends MvpView {
    void showLoading(boolean pullToRefresh);

    void showError();

    void showContent();

    void setItems(List<User> items);

    void appendItems(List<User> items);
}
