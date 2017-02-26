package com.tanjinc.tmediaplayer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.utils.FFmpegUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dxjia.ffmpeg.library.FFmpegNativeHelper;

/**
 * Created by tanjinc on 17-2-22.
 */
public class FFmpegFragment extends Fragment {
    private static final String TAG = "FFmpegFragment";
    @BindView(R.id.cmd_et) EditText mCmdEditText;
    @BindView(R.id.start_btn) Button mStartBtn;
    @BindView(R.id.info_btn) Button mInfoBtn;
    @BindView(R.id.video_info_tv)
    TextView mInfoTextView;

    String cmdline = "ffmpeg -t 5 -ss 00:00:00 -i /storage/emulated/0/DingTalk/1_3_37.mov -s 320x240 /sdcard/1_3_37.gif";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ffmpeg_layout, container, false);
        ButterKnife.bind(this, view);
        mCmdEditText.setText(cmdline);
//        mCmdEditText.setText(cmdline);
//        Button startButton= (Button) view.findViewById(R.id.start_btn);
//
//        startButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0){
//                String cmdline=mCmdEditText.getText().toString();
//                String[] argv=cmdline.split(" ");
//                Integer argc=argv.length;
//                new FFmpegTask(argv, argc).execute();
//            }
//        });
        return view;
    }

    @OnClick({R.id.info_btn, R.id.start_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_btn:
//                Log.d(TAG, "video onClick() " + FFmpegUtils.getInstance().getVideoFormatInfo());
                mInfoTextView.setText(FFmpegUtils.getInstance().getVideoFormatInfo());
                break;
            case R.id.start_btn:
                FFmpegUtils.getInstance().setOnCompleteListener(new FFmpegUtils.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "video onComplete: ");
                    }
                });
                FFmpegUtils.getInstance().performFFmpeg(mCmdEditText.getText().toString());
                break;
            default:
                break;
        }
    }
}
