package ca.timeify.android.activities;

import java.util.Calendar;

import ca.timeify.android.R;
import ca.timeify.android.data.CustomAnimation;
import ca.timeify.android.data.Fonts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public abstract class BaseActivity extends Activity {
	
	/* Inits */
	protected Fonts customFonts = new Fonts();
	protected CustomAnimation customAnimation = new CustomAnimation();
	
	/* Constants */
	protected static final String IMAGECAPTURE_KEY = "data";
	protected static final String IMAGE_URI_KEY = "imageuri";
	
	/* Methods */	
	protected void exportImageIntent(Bitmap image, Uri imageUri, Context classDotThis, Class<?> classDotClass) {
		Intent imagePreviewIntent = new Intent(classDotThis, classDotClass);
		imagePreviewIntent.putExtra(IMAGECAPTURE_KEY, image);
		imagePreviewIntent.putExtra(IMAGE_URI_KEY, imageUri);
		//Log.i("ImageCaptureBundle", imageUri.toString());
		startActivity(imagePreviewIntent);
		overridePendingTransition(R.anim.activity_transition_anim_enter, R.anim.activity_transition_anim_exit);
	}
	
	protected Uri stringToUri(String inputString) {
		Uri parsedUri = Uri.parse(inputString);
		return parsedUri;
	}
	
	protected int getCurrentTime_Miliseconds() {
		Calendar calendar = Calendar.getInstance();
		int milis = calendar.get(Calendar.MILLISECOND);
		return milis;
	}
	
}
