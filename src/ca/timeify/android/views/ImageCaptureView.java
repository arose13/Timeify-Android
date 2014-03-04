package ca.timeify.android.views;

import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

public class ImageCaptureView extends BaseActivity {
	
	private static final int IMAGECAPTURE_CODE = 0;
	private static final String IMAGECAPTURE_KEY = "data";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_capture_view);
	}
	
	private void cameraIntent() {
		Intent cameraStartIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraStartIntent, IMAGECAPTURE_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// The image is now below for modification
		Bitmap recievedCameraImage = (Bitmap) data.getExtras().get(IMAGECAPTURE_KEY);
	}
	
}
