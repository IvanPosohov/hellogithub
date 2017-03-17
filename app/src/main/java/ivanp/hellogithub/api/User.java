package ivanp.hellogithub.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    public String id;
    public String login;
    @SerializedName(value = "avatar_url")
    public String avatarUrl;

    public static class Response {
        @SerializedName(value = "total_count")
        public int totalCount;
        @SerializedName(value = "items")
        public List<User> users;
    }
}
