package com.dm;

import java.util.Timer;
import java.util.TimerTask;

import com.dm.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class IndexActivity extends Activity {

	private int index = 0;
	private Timer timer;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
				timer.cancel();
			Intent intent = new Intent(IndexActivity.this,
					MainActivty.class);
			startActivity(intent);
			finish();
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.index_main);
		
		ImageView myImage = (ImageView) findViewById(R.id.index_animation);
		/*myImage.setBackgroundResource(R.anim.index_anim);
		AnimationDrawable frameAnimation = (AnimationDrawable) myImage.getBackground();
		frameAnimation.start();*/
		
		myImage.setBackgroundResource(R.drawable.index_icon);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				index = index+1;
				handler.sendEmptyMessage(0);
			}
		},2000,1000);
	}
				
	
}
