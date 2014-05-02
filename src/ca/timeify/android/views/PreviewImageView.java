package ca.timeify.android.views;

import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;
import ca.timeify.android.data.CustomAnimation;
import ca.timeify.android.data.Fonts;
import ca.timeify.android.data.ImageProcessor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PreviewImageView extends BaseActivity implements OnClickListener {
	
	private static final String CLASSTAG = "PreviewImageView";
	private static final long ANIMATION_DURATION = 300;
	private static final long OVERALL_DELAY = 150;
	private static final int KILL_BACK_BTN = 0;
	private static final int KILL_NO_BTN = 1;
	private static final int KILL_YES_BTN = 2;
	
	private Uri imageUri;
	private Bitmap completedBitmap;
	private Intent receivedIntent;
	private Animation yesBtnAnimation;
	private Animation noBtnAnimation;
	private Animation yesBtnExitAnimation;
	private Animation noBtnExitAnimation;
	private ImageView previewImageView;
	private ImageView yesButton;
	private ImageView noButton;
	private TextView loadingTextView;
	
	private String[] LOADING_STEPS_ARRAY;
	
	private int pathType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview_image_view);
		Log.d("PreviewImageView", "PreviewImageView started");
		
		// Starting all Variables
		LOADING_STEPS_ARRAY = getResources().getStringArray(R.array.loadingTextSteps);
		
		// Find Views
		yesButton = (ImageView) findViewById(R.id.yesUIBtn);
		noButton = (ImageView) findViewById(R.id.noUIBtn);
		loadingTextView = (TextView) findViewById(R.id.loadingTextView);
		previewImageView = (ImageView) findViewById(R.id.previewImageView);
		previewImageView.setVisibility(View.VISIBLE);
		loadingTextView.setText(LOADING_STEPS_ARRAY[0]);
		
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
		
		// Get path type from ImageCaptureView
		pathType = receivedIntent.getExtras().getInt(PATHTYPE_KEY);
		
		imageUri = receivedIntent.getParcelableExtra(IMAGE_URI_KEY);
		
		Log.i("JUSTIN-DEBUG", "MEOW MEOW MEOW");
		Log.i("JUSTIN-DEBUG", "imageUri: " + imageUri);
		
		// Being Image Processing
		new ProcessImageASync().execute(imageUri);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		customFonts.typeFaceConstructor(loadingTextView, Fonts.Roboto.LIGHT, getAssets());
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
	
	private void uiStartButtonAnimation() {
		// Make Buttons Visible
		noButton.setVisibility(View.VISIBLE);
		yesButton.setVisibility(View.VISIBLE);
		// Start Animation
		noButton.startAnimation(noBtnAnimation);
		yesButton.startAnimation(yesBtnAnimation);
	}
	
	/* On back or Delete Button Select */
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
			setupConfirmAnimations();
			noBtnExitAnimation.setStartOffset(OVERALL_DELAY);
			yesBtnExitAnimation.setStartOffset(0);
			startExitAnimation();
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
	
	private void setupConfirmAnimations() {
		noBtnExitAnimation = customAnimation.outToTopAnimation(CustomAnimation.SHORT_ANIMATION_DURATION, new AnticipateInterpolator(1.0f));
		yesBtnExitAnimation = customAnimation.outToTopAnimation(CustomAnimation.SHORT_ANIMATION_DURATION, new AnticipateInterpolator(1.0f));
		yesBtnExitAnimation.setAnimationListener(new AnimationListener() {
			
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
				shareDialog();
				Log.d(CLASSTAG, "next activity called");
			}
		});
	}
	
	/* Share Dialog */
	private void shareDialog() {
		AlertDialog.Builder shareOptionList = new AlertDialog.Builder(PreviewImageView.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		shareOptionList.setCancelable(true);
		shareOptionList.setItems(R.array.shareDialog, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					// Share Button was selected
					shareImage(completedBitmap);
					break;
					
				case 1:
					// Cancel button  was selected
					Intent imageCaptureViewIntent = new Intent(PreviewImageView.this, ImageCaptureView.class);
					startActivity(imageCaptureViewIntent);
					overridePendingTransition(R.anim.activity_transition_anim_enter, R.anim.activity_transition_anim_exit);
					finish();
					break;

				default:
					break;
				}
			}
		});
		shareOptionList.show();
	}
	
	private void shareImage(Bitmap mytimeBitmap) {
		String mytimePath = Images.Media.insertImage(
				getContentResolver(), 
				mytimeBitmap, 
				mytimeFilenameGenerator(), 
				mytimeFileDescriptionGenerator(""));
		Uri mytimeURI = stringToUri(mytimePath);
		if (mytimeBitmap != null) {
			Intent mytimeIntent = new Intent(Intent.ACTION_SEND);
			mytimeIntent.setType("image/*");
			mytimeIntent.putExtra(Intent.EXTRA_STREAM, mytimeURI);
			mytimeIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.sharedFrom));
			startActivity(mytimeIntent);
		}
	}
	
	private String mytimeFilenameGenerator() {
		String filename = "MyTime" + getCurrentTime_Miliseconds();
		return filename;
	}
	
	private String mytimeFileDescriptionGenerator(String goalString) {
		String fileDescription = getResources().getString(R.string.myTimeDescription) + " " + goalString;
		return fileDescription;
	}
	
	/* Universally get content path from URI */
	private String getPathFromUri(Uri contentUri) {
		String path = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		Log.i("Justin-Debug", "contentUri: " + contentUri.toString());
		Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
		if (cursor.moveToFirst()) {
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			path = cursor.getString(columnIndex);
		}
		cursor.close();
		return path;
	}
	
	/* Image Processing Task */
	private class ProcessImageASync extends AsyncTask<Uri, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(Uri... uris) {
			
			Log.i("JUSTIN-DEBUG", "uris: " + uris.toString() + "\turi: " + uris[0]);
			
			Log.d(CLASSTAG, "async task ran");
			Bitmap inputBitmap;
			//String contentPath = getPathFromUri(uris[0]);
			String contentPath = "";
			if (pathType == 0) {
				// get uri path regularly
				contentPath = uris[0].getPath();
			} else if (pathType == 1) { 
				// transform content:// path and get it universally
				contentPath = getPathFromUri(uris[0]);
			} else {
				Log.i("ERROR", "Invalid path type passed by ImagePreview!");
			}
			
			Log.i("JUSTIN-DEBUG", "contentPath " + contentPath);
			// Down sampling
			inputBitmap = ImageProcessor.downsampleBitmap(
					ImageProcessor.getImageWidthFromContentPath(contentPath), 
					ImageProcessor.IMAGE_WIDTH_PIXELS, 
					contentPath);
			Log.d(CLASSTAG, "downsampling complete");
			
			// Check for image dimensions: height and width
			Log.i("JUSTIN-DEBUG", "Bitmap width: " + inputBitmap.getWidth() + "\theight: " + inputBitmap.getHeight() );
			
			// Check to see if image is in portrait or landscape by comparing dimensions
			boolean isPortrait = (inputBitmap.getHeight() > inputBitmap.getWidth());
			Log.i("JUSTIN-DEBUG", "Is image portrait orientation? " + isPortrait );
			
			// Portrait image check and rotation
			/*int angle = ImageProcessor.getPhotoOrientation(getApplicationContext(), imageUri, imageUri.getPath());
			Log.d(CLASSTAG, "found rotation angle");
			inputBitmap = ImageProcessor.rotateImage(angle, inputBitmap);
			Log.d(CLASSTAG, "image rotated");*/
			
			// GrayScaling
			inputBitmap = ImageProcessor.convertGrayScale(inputBitmap);
			Log.d(CLASSTAG, "gray scaling complete");
			
			// Progress Update
			publishProgress(1);
			
			// Overlaying
			Bitmap overlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_frame_text);
			overlayBitmap = ImageProcessor.resizeToReferenceBitmap(overlayBitmap, inputBitmap.getHeight());
			inputBitmap = ImageProcessor.overlayBitmap(inputBitmap, overlayBitmap);
			Log.d(CLASSTAG, "overlaying complete");
			
			// Text Overlaying
			// TODO add text to the image
			Log.d(CLASSTAG, "text overlaying complete");
			
			return inputBitmap;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			switch (progress[0]) {
			case 0:
				// Show Please Wait
				break;
			case 1:
				// Show Almost There
				loadingTextView.setText(LOADING_STEPS_ARRAY[1]);
				loadingTextView.startAnimation(customAnimation.inFromRightAnimation(CustomAnimation.SHORT_ANIMATION_DURATION, new OvershootInterpolator(1.0f)));
				break;
			case 2:
				// Show Nearly Done
				loadingTextView.setText(LOADING_STEPS_ARRAY[2]);
				loadingTextView.startAnimation(customAnimation.inFromRightAnimation(CustomAnimation.SHORT_ANIMATION_DURATION, new OvershootInterpolator(1.0f)));
				break;

			default:
				break;
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap resultImage) {
			super.onPostExecute(resultImage);
			Log.d(CLASSTAG, "onPostExecute ran");
			loadingTextView.setVisibility(View.INVISIBLE);
			previewImageView.setImageBitmap(resultImage);
			completedBitmap = resultImage;
			uiStartButtonAnimation();
		}
		
	}
	
}