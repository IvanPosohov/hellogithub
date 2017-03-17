package ivanp.hellogithub.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ivanp.hellogithub.App;
import ivanp.hellogithub.R;
import ivanp.hellogithub.api.User;
import ivanp.hellogithub.utils.Picasso;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Inject Picasso picasso;
    private final LayoutInflater inflater;
    private final OnItemClickListener onItemClickListener;
    private final List<User> items;

    public UsersAdapter(LayoutInflater inflater, OnItemClickListener onItemClickListener) {
        App.getAppComponent().inject(this);
        this.inflater = inflater;
        this.onItemClickListener = onItemClickListener;
        items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.view_user, parent, false);
        UserVH holder = new UserVH(view, picasso);
        view.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            User item = items.get(position);
            onItemClickListener.onItemClick(item);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserVH) holder).bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setUsers(List<User> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void appendUsers(List<User> items) {
        int positionStart = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(positionStart, items.size());
    }

    private static class UserVH extends RecyclerView.ViewHolder {
        private final Picasso picasso;
        private final ImageView avatarImageView;
        private final TextView loginTextView;

        UserVH(View view, Picasso picasso) {
            super(view);
            this.picasso = picasso;
            avatarImageView = (ImageView) view.findViewById(R.id.avatarImageView);
            loginTextView = (TextView) view.findViewById(R.id.loginTextView);
        }

        void bind(User item) {
            picasso.display(avatarImageView, item.avatarUrl);
            loginTextView.setText(item.login);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User item);
    }
}