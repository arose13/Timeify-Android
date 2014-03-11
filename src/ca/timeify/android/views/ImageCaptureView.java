package ca.timeify.android.views;

import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;
import ca.timeify.android.data.Fonts.Roboto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImageCaptureView extends BaseActivity implements OnClickListener {
	
	private static final int IMAGECAPTURE_CODE = 0;
	private static final int BROWSEIMAGE_CODE = 1;
	private static final String IMAGECAPTURE_KEY = "data";
	
	private ImageView captureButton;
	private Button browseButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_capture_view);
		
		browseButton = (Button) findViewById(R.id.browseImg_btn);
		captureButton = (ImageView) findViewById(R.id.capture_btn);
		
		browseButton.setOnClickListener(this);
		captureButton.setOnClickListener(this);
		
		customFonts.typeFaceConstructor(browseButton, Roboto.LIGHT, getAssets());
	}
	
	private void browseImageIntent() {
		Intent browseImageIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		browseImageIntent.setType("image/*");
		startActivityForResult(Intent.createChooser(browseImageIntent, getString(R.string.selectImage)), BROWSEIMAGE_CODE);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.capture_btn:
			/* CaptureBtn has been clicked */
			cameraIntent();
			break;
			
		case R.id.browseImg_btn:
			/* BrowseBtn has been clicked */
			browseImageIntent();
			break;

		default: // Do Nothing
			break;
		}
	}
	
}
