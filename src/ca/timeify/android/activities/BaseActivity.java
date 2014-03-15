package ca.timeify.android.activities;

import ca.timeify.android.R;
import ca.timeify.android.data.CustomAnimation;
import ca.timeify.android.data.Fonts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public abstract class BaseActivity extends Activity {
	
	/* Inits */
	protected Fonts customFonts = new Fonts();
	protected CustomAnimation customAnimation = new CustomAnimation();
	
	/* Constants */
	protected static final String IMAGECAPTURE_KEY = "data";
	
	/* Methods */	
	protected void exportImageIntent(Bitmap image, Context classDotThis, Class<?> classDotClass) {
		Intent imagePreviewIntent = new Intent(classDotThis, classDotClass);
		imagePreviewIntent.putExtra(IMAGECAPTURE_KEY, image);
		startActivity(imagePreviewIntent);
		overridePendingTransition(R.anim.activity_transition_anim_enter, R.anim.activity_transition_anim_exit);
	}
	
}
