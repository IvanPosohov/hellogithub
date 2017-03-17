package ivanp.hellogithub.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.transition.TransitionManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.List;

import javax.inject.Inject;

import ivanp.hellogithub.App;
import ivanp.hellogithub.R;
import ivanp.hellogithub.api.User;
import ivanp.hellogithub.data.UsersAdapter;
import ivanp.hellogithub.mvp.UsersPresenter;
import ivanp.hellogithub.mvp.UsersView;
import ivanp.hellogithub.utils.EndlessRecyclerOnScrollListener;
import ivanp.hellogithub.utils.GridSpacingDecoration;
import ivanp.hellogithub.utils.Keyboard;
import ivanp.hellogithub.utils.Picasso;
import ivanp.hellogithub.utils.SimpleTextWatcher;

public class UsersFragment
        extends MvpFragment<UsersView, UsersPresenter>
        implements UsersView {

    private ViewGroup container;
    private View contentLayout;
    private TextInputLayout queryTextInputLayout;
    private EditText queryEditText;
    private View searchButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private LoadingStateView loadingStateView;
    private View noContentLayout;
    private ImageView bigAvatarImageView;

    @Inject Picasso picasso;
    private String query;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        App.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        getActivity().setTitle(R.string.find_user);
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        contentLayout = view.findViewById(R.id.contentLayout);
        queryTextInputLayout = (TextInputLayout) view.findViewById(R.id.queryTextInputLayout);
        queryEditText = (EditText) view.findViewById(R.id.queryEditText);
        queryEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryTextInputLayout.setErrorEnabled(false);
            }
        });
        queryEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Keyboard.hideKeyboard(getActivity());
                search();
                return true;
            }
            return false;
        });
        searchButton = view.findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(v -> search());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.column_count));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingDecoration(getActivity(), R.dimen.card_margin));
        endlessRecyclerOnScrollListener.with(recyclerView);
        adapter = new UsersAdapter(inflater, this::showAvatar);
        recyclerView.setAdapter(adapter);
        noContentLayout = view.findViewById(R.id.noContentLayout);
        bigAvatarImageView = (ImageView) view.findViewById(R.id.bigAvatarImageView);
        bigAvatarImageView.setOnClickListener(v -> hideAvatar());
        bigAvatarImageView.setVisibility(View.GONE);
        loadingStateView = (LoadingStateView) view.findViewById(R.id.loadingStateView);

        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        loadingStateView.setOnRefreshListener(this::refresh);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(query)) {
            presenter.load(query, false);
        } else {
            showContent();
        }
    }

    private void showAvatar(User user) {
        picasso.display(bigAvatarImageView, user.avatarUrl);
        TransitionManager.beginDelayedTransition(container);
        bigAvatarImageView.setVisibility(View.VISIBLE);
    }

    private void hideAvatar() {
        TransitionManager.beginDelayedTransition(container);
        bigAvatarImageView.setVisibility(View.GONE);
    }

    private void search() {
        String query = queryEditText.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            queryEditText.requestFocus();
            queryTextInputLayout.setError(getString(R.string.please_enter_query));
            return;
        }
        this.query = query;
        endlessRecyclerOnScrollListener.reset();
        presenter.load(query, false);
    }

    private void refresh() {
        endlessRecyclerOnScrollListener.reset();
        if (!TextUtils.isEmpty(query)) {
            presenter.load(query, true);
        }
    }

    private final EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            presenter.loadNext();
        }
    };

    @NonNull
    @Override
    public UsersPresenter createPresenter() {
        return App.getAppComponent().usersPresenter();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        Keyboard.hideKeyboard(getActivity());
        queryEditText.setEnabled(false);
        searchButton.setEnabled(false);
        if (!pullToRefresh) {
            contentLayout.setVisibility(View.GONE);
            noContentLayout.setVisibility(View.GONE);
            loadingStateView.setLoading(true);
        }
        // otherwise the pull to refresh widget will already display a loading animation
    }

    @Override
    public void showError() {
        queryEditText.setEnabled(true);
        searchButton.setEnabled(true);
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        contentLayout.setVisibility(View.GONE);
        noContentLayout.setVisibility(View.GONE);
        loadingStateView.setError();
    }

    @Override
    public void showContent() {
        queryEditText.setEnabled(true);
        searchButton.setEnabled(true);
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        loadingStateView.setLoading(false);
        if (adapter.getItemCount() > 0) {
            contentLayout.setVisibility(View.VISIBLE);
            noContentLayout.setVisibility(View.GONE);
        } else {
            contentLayout.setVisibility(View.GONE);
            noContentLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setItems(List<User> items) {
        recyclerView.scrollToPosition(0);
        adapter.setUsers(items);
    }

    @Override
    public void appendItems(List<User> items) {
        adapter.appendUsers(items);
    }

    public static UsersFragment create() {
        return new UsersFragment();
    }
}