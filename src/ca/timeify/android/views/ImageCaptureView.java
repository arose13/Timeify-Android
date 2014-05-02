package ca.timeify.android.views;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;
import ca.timeify.android.data.Fonts.Roboto;

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
	
	private String mCurrentPhotoPath;
	
	private Uri imageUri;
	
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
		
		imageUri = null;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		instructionsTextView.startAnimation(instructionsDelayOvershoot);
		browseButton.startAnimation(browseDelayOvershoot);
	}
	
	private void browseImageIntent() {
		//Intent browseImageIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		//browseImageIntent.setType("image/*");
		//startActivityForResult(Intent.createChooser(browseImageIntent, getString(R.string.selectImage)), BROWSEIMAGE_CODE);
		
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, BROWSEIMAGE_CODE);   
	}
	
	private void cameraIntent() {
		Intent cameraStartIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		/* Notice that the startActivityForResult() method is protected by a condition that calls resolveActivity(), 
		 * which returns the first activity component that can handle the intent. Performing this check is important because
		 *  if you call startActivityForResult() 
		 * using an intent that no app can handle, your app will crash. So as 
		 * long as the result is not null, it's safe to use the intent.
		 */
		if (cameraStartIntent.resolveActivity(getPackageManager()) != null) {
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (Exception e) {
				Log.i("ImageCaptureView", "" + e);
			}
			if (photoFile != null) {
				imageUri = Uri.fromFile(photoFile);
				cameraStartIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				Log.i(CLASSTAG, "Successfully created photoFile, URI: " + Uri.fromFile(photoFile));
				startActivityForResult(cameraStartIntent, IMAGECAPTURE_CODE);
			}
		}
	}
	
	/* --added by Justin [Apr 23, 14 - 1:08am]
	 * This creates a file to save the raw image taken by the user
	 * Source: http://developer.android.com/training/camera/photobasics.html */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			Log.d(CLASSTAG, "result ok");
			
			// Figure where the image is coming from.
			switch (requestCode) {
			case IMAGECAPTURE_CODE:
				/* Received image is now ready for modifications */
				//receivedImage = (Bitmap) data.getExtras().get(IMAGECAPTURE_KEY);
				//Bitmap photo = (Bitmap) data.getExtras().get("data"); 
				//Uri u = (Uri) data.getExtras().get("data");
				//Log.i("ImageCaptureView", "photo " + u);
				if (imageUri != null) {
					/* If our camera intent successfully runs, it will make a path for the image taken. 
					 * Intent data returns null for whatever reason so we manually parse from the image created.
					 * By doing so, users will have an additional copy of raw image on their devices
					 * -- added by Justin [Apr 23, 14 - 1:08am]
					 */
					Log.i(CLASSTAG, "imageUri.toString() : " + imageUri.getPath());
					Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
					exportImageIntent(receivedImage, imageUri, 0, ImageCaptureView.this, PreviewImageView.class);
				}
				break;
				
			case BROWSEIMAGE_CODE:
				/* Received Image from browsing.*/
				//receivedImage = (Bitmap) data.getExtras().get(IMAGECAPTURE_KEY);
				imageUri = data.getData();
				Log.i(CLASSTAG, ""+imageUri);
				exportImageIntent(receivedImage, imageUri, 1, ImageCaptureView.this, PreviewImageView.class);
				break;

			default:
				Log.e(CLASSTAG, "nothing ran");
				break;
			}
		} else {
			// Result code,
			// Delete the picture created on camera startup
			File n = new File(mCurrentPhotoPath);
			n.delete();
			Log.e(CLASSTAG, "result failed, temp file deleted");
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
				}
			});
			captureButton.startAnimation(cameraClickAnimation);
			break;
			
		case R.id.browseImg_btn:
			/* BrowseBtn has been clicked */
			Animation browseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.browse_btn_click);
			browseAnimation.setAnimationListener(new AnimationListener() {
				
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
					browseImageIntent();
				}
			});
			browseButton.startAnimation(browseAnimation);
			break;

		default: // Do Nothing
			break;
		}
	}
	
}
