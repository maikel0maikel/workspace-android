package com.example.soundemulator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    private final int duration = 1; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = 8000;//duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private double freqOfTone = 440; // hz

    private final byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Use a new tread as this can take a while
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                int i=0;
            	while(i<50)
                {
	            	genTone();
	            	if(freqOfTone==440) freqOfTone=3000;
	            	else
	            		freqOfTone=440;
	            	i++;
	                handler.post(new Runnable() {
	
	                    public void run() {
	                        playSound();
	                    }
	                });
                	try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        });
        thread.start();
    }

    void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }
}
