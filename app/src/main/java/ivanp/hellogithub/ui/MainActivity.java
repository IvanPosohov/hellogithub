package ivanp.hellogithub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ivanp.hellogithub.R;

public class MainActivity extends BaseToolbarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fix for https://jira.rutube.ru/browse/NPAN-57
        // based on http://stackoverflow.com/questions/19545889/app-restarts-rather-than-resumes
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        String tag = UsersFragment.class.getName();
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            Fragment fragment = UsersFragment.create();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, fragment, tag)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
        }
    }
}
