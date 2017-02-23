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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ffmpeg_layout, container, false);
        ButterKnife.bind(this, view);
        mCmdEditText.setText("ffmpeg -i /storage/emulated/0/Movies/243819777-2.mp4 /sdcard/111.mkv");
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
                String cmdline=mCmdEditText.getText().toString();
                String[] argv=cmdline.split(" ");
                Integer argc=argv.length;
                new FFmpegTask(argv, argc).execute();
                break;
            default:
                break;
        }
    }

    class FFmpegTask extends AsyncTask {
        String[] argv = null;
        int argc = 0;
        public FFmpegTask(String[] argv, int argc) {
            this.argv = argv;
            this.argc = argc;
        }
        @Override
        protected Object doInBackground(Object[] params) {
            Log.d(TAG, "video doInBackground() ");
            return FFmpegUtils.getInstance().performFFmpeg(argc,argv);
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d(TAG, "video onPostExecute() "+o.toString());
        }
    }

}
