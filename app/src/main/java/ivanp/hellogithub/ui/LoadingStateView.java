package ivanp.hellogithub.ui;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import ivanp.hellogithub.R;

public class LoadingStateView extends FrameLayout {
    private final View loadingProgressBar;
    private final TextView errorTextView;
    private final View refreshButton;
    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public LoadingStateView(Context context) {
        this(context, null);
    }

    public LoadingStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_loading_status, this, true);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(view -> {
            if (onRefreshListener != null) onRefreshListener.onRefresh();
        });
    }

    public void setLoading(boolean isLoading) {
        loadingProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
        errorTextView.setVisibility(GONE);
        refreshButton.setVisibility(GONE);
    }

    public void setError() {
        setError(R.string.data_load_error_check_network, true);
    }

    public void setError(@StringRes int errorMessage, boolean refreshable) {
        loadingProgressBar.setVisibility(GONE);
        errorTextView.setVisibility(VISIBLE);
        errorTextView.setText(errorMessage);
        refreshButton.setVisibility(refreshable ? VISIBLE :GONE);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}