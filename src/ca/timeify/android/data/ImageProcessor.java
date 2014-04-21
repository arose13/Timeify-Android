package ca.timeify.android.data;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

public class ImageProcessor {
	
	private static final String CLASSTAG = "ImageProcessor";
	public static final int IMAGE_WIDTH_PIXELS = 960;
	
	public static Bitmap convertGrayScale(Bitmap inputImage) {
		/* Manipulation Factors */
		final double GRAY_RED = 0.299;
		final double GRAY_GREEN = 0.587;
		final double GRAY_BLUE = 0.114;
		
		// Input image dimensions
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		
		/* Output bitmap */
		Bitmap bitmapOutput = Bitmap.createBitmap(width, height, inputImage.getConfig());
		/* Pixel Information */
		int A;
		int R;
		int G;
		int B;
		int pixel;
		
		/* Pixel scanning and processing */
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixel = inputImage.getPixel(x, y);
				// New pixel values
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				
				// RGB conversion transformation
				R = G = B = (int) (GRAY_RED * R + GRAY_GREEN * G + GRAY_BLUE * B);
				
				// Apply pixel information to bitmap
				bitmapOutput.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		
		/* Return grayScaled Image */
		Log.d(CLASSTAG, "grayscaler ran");
		return bitmapOutput;
		
	}
	
	/* DownSampler */
	public static Bitmap downsampleBitmap(int imageWidth, int targetWidth, String imageContentPath) {
		final Options bitmapOptions = new Options();
		bitmapOptions.inDensity = sampleSizer(imageWidth, targetWidth);
		bitmapOptions.inTargetDensity = 1;
		Bitmap scaledBitmap = BitmapFactory.decodeFile(imageContentPath, bitmapOptions);
		scaledBitmap.setDensity(Bitmap.DENSITY_NONE);
		Log.d(CLASSTAG, "DownSampler ran");
		return scaledBitmap;
	}
	
	public static int getImageWidthFromContentPath(String imageContentPath) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imageContentPath, options);
		int imageWidth = options.outWidth;
		return imageWidth;
	}
	
	/* Photo Orientation */
	public static Bitmap rotateImage(int angle, Bitmap inputBitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap rotatedBitmap = Bitmap.createBitmap(
										inputBitmap,
										0,
										0,
										inputBitmap.getWidth(),
										inputBitmap.getHeight(),
										matrix,
										true
										);
		return rotatedBitmap;
	}
	
	public static int getPhotoOrientation(Context context, Uri imageUri, String imagePath) {
		Log.d(CLASSTAG, "imageURI: " + imageUri.toString());
		Log.d(CLASSTAG, "imagePath: " + imageUri.getPath());
		
		// Begin processing
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);
			
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;

			default:
				break;
			}
			
			Log.i(CLASSTAG, "Exif orientation: " + orientation);
			Log.i(CLASSTAG, "Rotate value: " + rotate);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rotate;
	}
	
	/* Required for DownSampler */
	private static int sampleSizer(int width, int target) {
		int result = 1;
		for (int i = 0; i < 10; i++) {
			if (width < target * 2) {
				break;
			}
			width = width / 2;
			result = result * 2;
		}
		return result;
	}
	
	/* Resize image to background image */
	public static Bitmap resizeToReferenceBitmap(Bitmap toResizeBitmap, int referenceImageHeight) {
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(toResizeBitmap, referenceImageHeight, referenceImageHeight, false);
		return resizedBitmap;
	}
	
	/* To overly an image over another */
	public static Bitmap overlayBitmap(Bitmap inputBitmap, Bitmap overlayBitmap) {
		Bitmap finalBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
		Canvas canvas = new Canvas(finalBitmap);
		canvas.drawBitmap(inputBitmap, new Matrix(), null);
		canvas.drawBitmap(overlayBitmap, new Matrix(), null);
		return finalBitmap;
	}
	
	/* To overlay text over an image */
//	public static Bitmap overlayTextToBitmap(Bitmap inputBitmap, String watermark, Point location, Context context) {
//		int width = inputBitmap.getWidth();
//		int height = inputBitmap.getHeight();
//		Bitmap finalBitmap = Bitmap.createBitmap(width, height, inputBitmap.getConfig()); 
//		Canvas canvas = new Canvas(finalBitmap);
//		canvas.drawBitmap(inputBitmap, new Matrix(), null);
//		
//		// Generate text
//		Paint paint = new Paint();
//		float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, context.getResources().getDisplayMetrics());
//		paint.setColor(Color.rgb(255, 255, 255));
//		paint.setTextSize(textSize);
//		paint.setAntiAlias(true);
//		canvas.drawt
//	}

}
