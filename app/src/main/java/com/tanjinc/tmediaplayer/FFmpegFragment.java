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
import android.widget.Toast;

import com.tanjinc.tmediaplayer.utils.FFmpegUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tanjinc on 17-2-22.
 */
public class FFmpegFragment extends Fragment {
    private static final String TAG = "FFmpegFragment";
    @BindView(R.id.cmd_et) EditText mCmdEditText;
    @BindView(R.id.start_btn) Button mStartBtn;
    @BindView(R.id.info_btn) Button mInfoBtn;
    @BindView(R.id.transcode_btn) Button mTranscodeBtn;
    @BindView(R.id.video_info_tv)
    TextView mInfoTextView;

//    String cmdline = "ffmpeg -t 5 -ss 00:00:00 -r 10 -i /storage/emulated/0/12.爱神的箭.avi -s 320x240 -b:v 1500k /sdcard/1.gif";
    String cmdline = "ffmpeg -t 2 -ss 00:00:00 -r 10 -i /storage/emulated/0/12.爱神的箭.avi -s 320x240 -o /sdcard/12.gif";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ffmpeg_layout, container, false);
        ButterKnife.bind(this, view);
        mCmdEditText.setText(cmdline);
        return view;
    }

    @OnClick({R.id.info_btn, R.id.start_btn, R.id.transcode_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                FFmpegUtils.getInstance().setOnCompleteListener(new FFmpegUtils.OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getActivity(), "gif 转换成功", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "video onComplete: ");
                    }
                });
                FFmpegUtils.getInstance().performFFmpeg(mCmdEditText.getText().toString());
                break;
            case R.id.info_btn:
                mInfoTextView.setText(FFmpegUtils.getInstance().getVideoFormatInfo());
                break;
            case R.id.transcode_btn:
                break;
            default:
                break;
        }
    }
}
