package com.example.sanya.videoencoderandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;
import com.example.sanya.videoencoderandroid.R;

/**
 * Created by Sanya on 12/2/2017.
 */

public class VideoActivity extends Activity{

    VideoView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String path = getIntent().getStringExtra("Path");
        v = (VideoView) findViewById(R.id.videoView);
        v.setVideoPath(path);
        v.start();
    }
}
