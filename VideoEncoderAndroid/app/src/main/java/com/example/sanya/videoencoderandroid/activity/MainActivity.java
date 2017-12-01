package com.example.sanya.videoencoderandroid.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sanya.videoencoderandroid.R;
import com.example.sanya.videoencoderandroid.adapters.ImageAdapter;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageView mImageView;
    Button mGetButton, mVideoButton;
    GridView mImageGrid;
    static final int REQUEST_IMAGE_GET = 1;
    ArrayList<Uri> path;
    private String savedPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mGetButton = (Button) findViewById(R.id.btnGetImage);
        mVideoButton = (Button) findViewById(R.id.btnVideo);
        mImageGrid = (GridView) findViewById(R.id.imageGrid);
        mGetButton.setVisibility(View.VISIBLE);
        mVideoButton.setVisibility(View.GONE);
        mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, VideoActivity.class);
                i.putExtra("Path", savedPath);
                startActivity(i);
            }
        });
        path = new ArrayList<>();
    }

    private void selectImage() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, REQUEST_IMAGE_GET);
//        }
        FishBun.with(MainActivity.this)
                .MultiPageMode()
                .setMaxCount(100)
                .setMinCount(1)
                .setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .setAllViewTitle("All")
                .setActionBarTitle("Image Library")
                .textOnImagesSelectionLimitReached("Limit Reached!")
                .textOnNothingSelected("Nothing Selected")
                .startAlbum();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    SeekableByteChannel out = null;
                    try {
                        Date date = new Date() ;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
                        savedPath = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DCIM) + dateFormat.format(date) + ".mp4";
                        out = NIOUtils.writableFileChannel(savedPath);
                        AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(25, 1));
                        path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                        for (Uri paths: path) {
                            Bitmap thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(),paths);
                            Log.d("Bitmaps", thumbnail.toString());
                            mImageGrid.setAdapter(new ImageAdapter(this, path));
                            encoder.encodeImage(thumbnail);
                            mVideoButton.setVisibility(View.VISIBLE);
                            mGetButton.setVisibility(View.GONE);
                            //mImageView.setImageBitmap(thumbnail);
                        }
                        encoder.finish();
                        Toast.makeText(MainActivity.this, "Video Saved in " + savedPath, Toast.LENGTH_LONG).show();

                        break;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        NIOUtils.closeQuietly(out);
                    }


                }
            case REQUEST_IMAGE_GET:
                if(resultCode == RESULT_OK && data != null)
                {
                    Uri fullPhotoUri = data.getData();
                    Bitmap thumbnail = null;
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(),fullPhotoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mImageView.setImageBitmap(thumbnail);
                }
        }
    }


}
