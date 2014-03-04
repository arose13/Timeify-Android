package ca.timeify.android.data;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class Fonts {
	
	public void typeFaceConstructor(TextView textView, String fontPath, AssetManager recievedAssetManager) {
		Typeface customTypeface = Typeface.createFromAsset(recievedAssetManager, fontPath);
		textView.setTypeface(customTypeface);
	}
	
	private static final String FRONT = "fonts/";
	private static final String BACK = ".ttf";
	
	public static final class Roboto {
		private static final String ROBOTOGROUP = "Roboto-";
		
		public static final String BLACK = FRONT + ROBOTOGROUP + "Black" + BACK;
		public static final String BOLD = FRONT + ROBOTOGROUP + "Bold" + BACK;
		public static final String LIGHT = FRONT + ROBOTOGROUP + "Light" + BACK;
		public static final String THIN = FRONT + ROBOTOGROUP + "Thin" + BACK;
		public static final String REGULAR = FRONT + ROBOTOGROUP + "Regular" + BACK;
		public static final String MEDIUM = FRONT + ROBOTOGROUP + "Medium" + BACK;
	}
	
}
