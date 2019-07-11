package com.example.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.PostAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;

    private SwipeRefreshLayout swipeContainer;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate((R.layout.fragment_home),container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvPosts = view.findViewById(R.id.rvPosts);

        posts = new ArrayList<>();

        postAdapter = new PostAdapter(posts);

        LinearLayoutManager lin = new LinearLayoutManager(getContext());


        rvPosts.setLayoutManager(lin);


        rvPosts.setAdapter(postAdapter);

        loadTopPosts();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvPosts.getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        rvPosts.addItemDecoration(dividerItemDecoration);


        swipeContainer =  view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                reloadTopPosts();

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



    }



    private void reloadTopPosts()
    {

        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().decendingTime();


        posts.clear();
        postAdapter.notifyDataSetChanged();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {


                if(e==null)
                {
                    for(int i = 0; i < objects.size();i++)
                    {
                        Log.d("HomeActivity","Post: "+i +" "+ objects.get(i).getDescription()+" "+objects.get(i).getUser().getUsername());
                        Post post = objects.get(i);
                        posts.add(post);
                        postAdapter.notifyItemInserted(posts.size()-1);
                    }
                    // end refresh icon
                    swipeContainer.setRefreshing(false);
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });

    }


    private void loadTopPosts()
    {

        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().decendingTime();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e==null)
                {
                    for(int i = 0; i < objects.size();i++)
                    {
                        Log.d("HomeActivity","Post: "+i +" "+ objects.get(i).getDescription()+" "+objects.get(i).getUser().getUsername());
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
