package com.example.myapplication;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.myapplication.importedFiles.EndlessRecyclerViewScrollListener;
import com.example.myapplication.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileDetailsActivity extends AppCompatActivity {



    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;
    TextView tvUsername;


    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    int maxPosts = 20;
    ParseUser targetUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        rvPosts = findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);



        // add dividers on posts
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvPosts.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rvPosts.addItemDecoration(dividerItemDecoration);

        targetUser = Parcels.unwrap(getIntent().getParcelableExtra(ParseUser.class.getSimpleName()));

        // set username under photo
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername.setText(targetUser.getUsername());

        // populate timeline with top 20 posts
        loadAllUserPosts(targetUser);


    }

    private void loadAllUserPosts(ParseUser user)
    {

        final Post.Query postQuery = new Post.Query();
        postQuery.withUser().decendingTime().onlyUser(user);

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e==null)
                {
                    for(int i = 0; i < objects.size();i++)
                    {
                        Post post = objects.get(i);
                        posts.add(post);
                        postAdapter.notifyItemInserted(posts.size()-1);
                    }
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });
    }

}
