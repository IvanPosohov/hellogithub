package ivanp.hellogithub.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;

    public GridSpacingDecoration(Context context, @DimenRes int id) {
        spacing = (int) context.getResources().getDimension(id);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(spacing, spacing, spacing, spacing);
    }
}