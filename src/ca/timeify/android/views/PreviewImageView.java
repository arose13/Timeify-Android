package ca.timeify.android.views;

import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class PreviewImageView extends BaseActivity {
	
	private Bitmap image;
	
	private ImageView previewImageView;
	private ImageView yesButton;
	private ImageView noButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview_image_view);
		Log.d("PreviewImageView", "PreviewImageView started");
		
		// Find Views
		previewImageView = (ImageView) findViewById(R.id.previewImageView);
		previewImageView.setVisibility(View.INVISIBLE);
		
		// Find received Data
		image = (Bitmap) getIntent().getParcelableExtra(IMAGECAPTURE_KEY);
		previewImageView.setImageBitmap(image); 
	}
	
}