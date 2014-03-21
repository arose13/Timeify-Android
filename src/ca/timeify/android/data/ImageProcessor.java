package ca.timeify.android.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

public class ImageProcessor {
	
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
		return bitmapOutput;
		
	}
	
	/* To overly an image over another */
	public static Bitmap overlayBitmap(Bitmap inputBitmap, Bitmap overlayBitmap) {
		Bitmap finalBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
		Canvas canvas = new Canvas(finalBitmap);
		canvas.drawBitmap(inputBitmap, new Matrix(), null);
		canvas.drawBitmap(overlayBitmap, new Matrix(), null);
		return finalBitmap;
	}
	
}
