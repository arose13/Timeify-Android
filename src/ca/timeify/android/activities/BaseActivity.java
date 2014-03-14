package ca.timeify.android.activities;

import ca.timeify.android.data.CustomAnimation;
import ca.timeify.android.data.Fonts;

import android.app.Activity;

public abstract class BaseActivity extends Activity {
	
	protected Fonts customFonts = new Fonts();
	protected CustomAnimation customAnimation = new CustomAnimation();
	
}
