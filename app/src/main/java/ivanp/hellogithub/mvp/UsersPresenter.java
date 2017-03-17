package ivanp.hellogithub.mvp;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ivanp.hellogithub.api.Api;
import ivanp.hellogithub.api.User;

public class UsersPresenter extends MvpBasePresenter<UsersView> {
    private static final int LIMIT = 20;
    private int page;

    private ObservableEmitter<Integer> emitter;
    private Observable<List<User>> cache;
    private Disposable subscription;
    private boolean refresh;
    private String query;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (subscription != null) subscription.dispose();
    }

    public void load(String query, boolean refresh) {
        this.refresh = refresh | !query.equals(this.query);
        if (refresh) {
            page = 0;
            emitter = null;
            if (cache != null) cache.onTerminateDetach();
            cache = null;
        }
        this.query = query;
        if (getView() != null) {
            getView().showLoading(refresh);
        }
        subscription = Observable.create(
                (ObservableOnSubscribe<Integer>) subscriber -> {
                    this.emitter = subscriber;
                    subscriber.onNext(0);
                })
                .concatMap(page -> Api.service.users(query, page, LIMIT))
                //.takeUntil(response -> response.totalCount > page * LIMIT)
                .map(response -> response.users)
                .cache()
                .doOnError(Api::logError)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        users -> {
                            if (getView() != null) {
                                if (this.refresh) {
                                    this.refresh = false;
                                    getView().setItems(users);
                                } else {
                                    getView().appendItems(users);
                                }
                                getView().showContent();
                            }
                        },
                        throwable -> {
                            if (getView() != null) {
                                getView().showError();
                            }
                        });
    }

    public void loadNext() {
        if (emitter != null) {
            page++;
            emitter.onNext(page);
        }
    }
}
