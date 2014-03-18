package ca.timeify.android.views;

import java.io.FileNotFoundException;
import java.io.IOException;

import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;
import ca.timeify.android.data.CustomAnimation;
import ca.timeify.android.data.ImageProcessor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

public class PreviewImageView extends BaseActivity implements OnClickListener {
	
	private static final long ANIMATION_DURATION = 300;
	private static final long OVERALL_DELAY = 150;
	private static final int KILL_BACK_BTN = 0;
	private static final int KILL_NO_BTN = 1;
	private static final int KILL_YES_BTN = 2;
	
	private Uri imageUri;
	private Intent receivedIntent;
	private Animation yesBtnAnimation;
	private Animation noBtnAnimation;
	private Animation yesBtnExitAnimation;
	private Animation noBtnExitAnimation;
	private ImageView previewImageView;
	private ImageView yesButton;
	private ImageView noButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview_image_view);
		Log.d("PreviewImageView", "PreviewImageView started");
		
		// Find Views
		yesButton = (ImageView) findViewById(R.id.yesUIBtn);
		noButton = (ImageView) findViewById(R.id.noUIBtn);
		previewImageView = (ImageView) findViewById(R.id.previewImageView);
		previewImageView.setVisibility(View.VISIBLE);
		
		// Set OnClickListners
		yesButton.setOnClickListener(this);
		noButton.setOnClickListener(this);
		
		// Setup Animations
		noBtnAnimation = customAnimation.scaleInAnimation(ANIMATION_DURATION, new OvershootInterpolator(1.0f), this);
		noBtnAnimation.setStartOffset(OVERALL_DELAY);
		yesBtnAnimation = customAnimation.scaleInAnimation(ANIMATION_DURATION, new OvershootInterpolator(1.0f), this);
		yesBtnAnimation.setStartOffset(OVERALL_DELAY + 150);
		setupExitAnimations();
		
		// Find received Data
		receivedIntent = getIntent();
		imageUri = receivedIntent.getParcelableExtra(IMAGE_URI_KEY);
		
		// Being Image Processing
		new ProcessImageASync().execute(getBitmapFromUri(imageUri));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// Start Animations
		noButton.startAnimation(noBtnAnimation);
		yesButton.startAnimation(yesBtnAnimation);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		killActivityProcedure(KILL_BACK_BTN);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yesUIBtn:
			Log.d("PreviewImageView", "yes btn pushed");
			killActivityProcedure(KILL_YES_BTN);
			break;
			
		case R.id.noUIBtn:
			Log.d("PreviewImageView", "no btn pushed");
			killActivityProcedure(KILL_NO_BTN);
			break;

		default:
			break;
		}
	}
	
	// On back or Delete Button Select
	private void killActivityProcedure(int killConstant) {
		switch (killConstant) {
		case KILL_BACK_BTN:
			// Set Delays
			noBtnExitAnimation.setStartOffset(0);
			yesBtnExitAnimation.setStartOffset(OVERALL_DELAY);
			startExitAnimation();
			break;
			
		case KILL_NO_BTN:
			// Set Delays
			noBtnExitAnimation.setStartOffset(0);
			yesBtnExitAnimation.setStartOffset(OVERALL_DELAY);
			startExitAnimation();
			break;
			
		case KILL_YES_BTN:
			// Set Delays
			noBtnExitAnimation.setStartOffset(OVERALL_DELAY);
			yesBtnExitAnimation.setStartOffset(0);
			startExitAnimation();
			// To Next Activity
			// TODO THE FINAL ACTIVITY!
			break;

		default:
			break;
		}
	}

	private void startExitAnimation() {
		noButton.startAnimation(noBtnExitAnimation);
		yesButton.startAnimation(yesBtnExitAnimation);
	}

	private void setupExitAnimations() {
		noBtnExitAnimation = customAnimation.outToTopAnimation(CustomAnimation.SHORT_ANIMATION_DURATION, new AnticipateInterpolator(1.0f));
		yesBtnExitAnimation = customAnimation.outToTopAnimation(CustomAnimation.SHORT_ANIMATION_DURATION, new AnticipateInterpolator(1.0f));
		yesBtnExitAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				//Do Nothing
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// Do Nothing
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				finish();
			}
		});
	}
	
	private Bitmap getBitmapFromUri(Uri imageUri) {
		Bitmap bitmap = null;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/* Image Processing Task */
	private class ProcessImageASync extends AsyncTask<Bitmap, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Bitmap... bitmaps) {
			Log.d("previewImageView", "async task ran");
			Bitmap processedBitmap = ImageProcessor.convertGrayScale(bitmaps[0]);
			return processedBitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap resultImage) {
			super.onPostExecute(resultImage);
			previewImageView.setImageBitmap(resultImage);
		}
		
	}
	
}