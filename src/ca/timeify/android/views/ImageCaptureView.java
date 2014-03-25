package ca.timeify.android.views;

import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;
import ca.timeify.android.data.Fonts.Roboto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageCaptureView extends BaseActivity implements OnClickListener {
	
	private static final String CLASSTAG = "ImageCaptureView";
	private static final long ANIMATION_DURATION = 300;
	private static final int IMAGECAPTURE_CODE = 2014;
	private static final int BROWSEIMAGE_CODE = 2015;
	
	private Animation browseDelayOvershoot;
	private Animation instructionsDelayOvershoot;
	
	private ImageView captureButton;
	private TextView instructionsTextView;
	private Button browseButton;
	
	private Bitmap receivedImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_capture_view);
		
		//View starters
		instructionsTextView = (TextView) findViewById(R.id.instructionsText);
		browseButton = (Button) findViewById(R.id.browseImg_btn);
		captureButton = (ImageView) findViewById(R.id.capture_btn);
		
		//On click listeners for the buttons
		browseButton.setOnClickListener(this);
		captureButton.setOnClickListener(this);
		
		//Custom Fonts Modifiers
		customFonts.typeFaceConstructor(browseButton, Roboto.LIGHT, getAssets());
		customFonts.typeFaceConstructor(instructionsTextView, Roboto.LIGHT, getAssets());
		
		//Animation Settings
		instructionsDelayOvershoot = customAnimation.inFromRightAnimation(ANIMATION_DURATION, new OvershootInterpolator(1.0f));
		instructionsDelayOvershoot.setStartOffset(200);
		browseDelayOvershoot = customAnimation.inFromRightAnimation(ANIMATION_DURATION, new OvershootInterpolator(1.0f));
		browseDelayOvershoot.setStartOffset(100);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		instructionsTextView.startAnimation(instructionsDelayOvershoot);
		browseButton.startAnimation(browseDelayOvershoot);
	}
	
	private void browseImageIntent() {
		Intent browseImageIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		browseImageIntent.setType("image/*");
		startActivityForResult(Intent.createChooser(browseImageIntent, getString(R.string.selectImage)), BROWSEIMAGE_CODE);
	}
	
	private void cameraIntent() {
		Log.d(CLASSTAG, "cameraIntent() ran");
		Intent cameraStartIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraStartIntent, IMAGECAPTURE_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Uri imageUri;
		
		if (resultCode == RESULT_OK) {
			Log.d(CLASSTAG, "result ok");
			
			// Figure where the image is coming from.
			switch (requestCode) {
			case IMAGECAPTURE_CODE:
				/* Received image is now ready for modifications */
				receivedImage = (Bitmap) data.getExtras().get(IMAGECAPTURE_KEY);
				imageUri = data.getData();
				exportImageIntent(receivedImage, imageUri, ImageCaptureView.this, PreviewImageView.class);
				Log.d(CLASSTAG, "camera successfully ran");
				break;
				
			case BROWSEIMAGE_CODE:
				/* Received Image from browsing.*/
				receivedImage = (Bitmap) data.getExtras().get(IMAGECAPTURE_KEY);
				imageUri = data.getData();
				exportImageIntent(receivedImage, imageUri, ImageCaptureView.this, PreviewImageView.class);
				Log.d(CLASSTAG, "browse successfully ran");
				break;

			default:
				Log.e(CLASSTAG, "nothing ran");
				break;
			}
		} else {
			// Result code,
			Log.e(CLASSTAG, "result failed");
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.capture_btn:
			/* CaptureBtn has been clicked */
			Animation cameraClickAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.camera_btn_click);
			cameraClickAnimation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// Do Nothing
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// Do Nothing
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					cameraIntent();
					Log.d(CLASSTAG, "cameraIntet() called");
				}
			});
			captureButton.startAnimation(cameraClickAnimation);
			break;
			
		case R.id.browseImg_btn:
			/* BrowseBtn has been clicked */
			browseButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.browse_btn_click));
			browseImageIntent();
			break;

		default: // Do Nothing
			break;
		}
	}
	
}
