package ivanp.hellogithub.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position before loading more.
    private static final int VISIBLE_THRESHOLD = 9;

    private LinearLayoutManager layoutManager;
    // The total number of items in the dataset after the last load
    private int previousTotal = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean isLoading = true;

    public void with(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(this);
        layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
    }

    public void reset() {
        previousTotal = 0;
        isLoading = true;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (isLoading) {
            if (totalItemCount > previousTotal) {
                isLoading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            // End has been reached
            onLoadMore();
            isLoading = true;
        }
    }

    public abstract void onLoadMore();
}
