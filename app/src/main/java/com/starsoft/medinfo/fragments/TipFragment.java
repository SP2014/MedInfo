package com.starsoft.medinfo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.starsoft.medinfo.R;
import com.starsoft.medinfo.asyncrssclient.RssItem;

/**
 * Created by Aashish on 9/7/2016.
 */
public class TipFragment extends Fragment {

    private static RssItem item;
    private Context context;
    private static final String TAG = TipFragment.class.getSimpleName();
    private String title = "";
    private String browse_link = "";

    public static TipFragment newInstance(RssItem item){
        TipFragment tipFragment = new TipFragment();
        Bundle args = new Bundle();

        args.putString("title", item.getTitle());
        args.putString("imgLink", item.getMediaLink());
        args.putString("browseLink", item.getLink().toString());
        tipFragment.setArguments(args);
        return tipFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title").trim();
        browse_link = getArguments().getString("browseLink").trim();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tip_layout, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.tipImage);
        TextView tv = (TextView)view.findViewById(R.id.tipTitle);
        Log.d(TAG, title);
        tv.setText(title);
        if(getArguments().getString("imgLink")!=null){
            Picasso.with(getContext()).load(getArguments().getString("imgLink")).into(imageView);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(browse_link));
                getActivity().startActivity(intent);
            }
        });
    }
}
