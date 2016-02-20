package com.awds333.slushalka;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AudioRecord record;
    ImageView im;
    Context c;
    Handler h;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainl);
        c = this;
        record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, 88000);
        Toast.makeText(c,record.getState()+"",Toast.LENGTH_LONG).show();
        record.startRecording();
        im = (ImageView) findViewById(R.id.imageView);
        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] buf = new byte[880];
                        record.read(buf, 0, buf.length);
                        Bitmap bit = BitmapFactory.decodeResource(c.getResources(),R.drawable.defolt);
                        for(int i =0;i<440;i++){
                            int x = (int) (buf[i]*256+buf[i+1]*0.00671);
                            for(int j = x-1;j>-1;j++){
                                bit.setPixel(i*2,346, Color.BLACK);
                                bit.setPixel(i*2+1,222, Color.BLACK);
                            }
                        }
                        bm = bit;
                        h.sendEmptyMessage(1);
                    }
                });
                t.start();
            }
        });
        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.stop();
            }
        });
        h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                im.setImageBitmap(bm);

            }
        };

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        record.release();
    }
}
