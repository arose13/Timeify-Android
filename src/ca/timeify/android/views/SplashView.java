package ca.timeify.android.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;
import ca.timeify.android.data.CustomAnimation;

public class SplashView extends BaseActivity {
	
	private static int splashViewTimer = 2000;
	private static long splashIconAnimationDuration = 500;
	private static long splashIconAnimationDelay = 250;
	
	private CustomAnimation customAnimation = new CustomAnimation();
	private Animation splashIconAnimation;
	private ImageView splashIcon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_view);
		
		splashIcon = (ImageView) findViewById(R.id.splashIcon);
		
		splashIconAnimation = customAnimation.inFromTopAnimation(splashIconAnimationDuration, new OvershootInterpolator(1.5f));
		splashIconAnimation.setStartOffset(splashIconAnimationDelay);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		splashIcon.startAnimation(splashIconAnimation);
		
		// End SplashView and move to the camera intent
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent cameraIntent = new Intent(SplashView.this, ImageCaptureView.class);
				startActivity(cameraIntent);
				overridePendingTransition(R.anim.activity_transition_anim_enter, R.anim.activity_transition_anim_exit);
				finish();
			}
			
		}, splashViewTimer);
	}

}
