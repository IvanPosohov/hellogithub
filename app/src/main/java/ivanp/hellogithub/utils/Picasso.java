package ivanp.hellogithub.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import ivanp.hellogithub.R;

public final class Picasso {
    private com.squareup.picasso.Picasso picasso;
    private Drawable placeholderDrawable;

    public Picasso(Context context) {
        picasso = new com.squareup.picasso.Picasso.Builder(context)
                //.indicatorsEnabled(true)
                .build();
        placeholderDrawable = ContextCompat.getDrawable(context, R.drawable.ic_image_placeholder);
    }

    public void display(ImageView target, String url) {
        if (url != null && url.trim().length() == 0) url = null;
        picasso.load(url)
                .placeholder(placeholderDrawable)
                .into(target);
    }
}
