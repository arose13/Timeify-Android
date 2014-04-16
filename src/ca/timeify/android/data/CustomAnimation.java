package ca.timeify.android.data;

import ca.timeify.android.R;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

public class CustomAnimation {
	
	public static final long VERYSHORT_ANIMATION_DURATION = 100;
	public static final long SHORT_ANIMATION_DURATION = 150;
	public static final long MEDIUM_ANIMATION_DURATION = 300;
	public static final long LONG_ANIMATION_DURATION = 400;
	
	public long secondsToMilis(long seconds) {
		return (seconds*1000);
	}
	
	public Animation delayAnimation(Animation inputAnimation) {
		inputAnimation.setStartOffset(200);
		return inputAnimation;
	}
	
	public Animation loopAnimation(Animation inputAnimation) {
		inputAnimation.setRepeatMode(Animation.RESTART);
		inputAnimation.setRepeatCount(Animation.INFINITE);
		return inputAnimation;
	}
	
	/* Translation Animation */
	public Animation fromRightToLeftAnimation(long milis, Interpolator animationInterpolator) {
		Animation fromRightToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		fromRightToLeft.setDuration(milis);
		fromRightToLeft.setInterpolator(animationInterpolator);
		return fromRightToLeft;
	}
	
	public Animation inFromTopAnimation(long milis, Interpolator animationInterpolator) {
		Animation inFromTop = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f, 
				Animation.RELATIVE_TO_PARENT, 0.0f, 
				Animation.RELATIVE_TO_PARENT, -1.0f, 
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromTop.setDuration(milis);
		inFromTop.setInterpolator(animationInterpolator);
		return inFromTop;
	}
	
	public Animation inFromRightAnimation(long milis, Interpolator animationInterpolator) {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(milis);
		inFromRight.setInterpolator(animationInterpolator);
		return inFromRight;
	}
	
	public Animation outToLeftAnimation(long milis, Interpolator animationInterpolator) {
		Animation outToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outToLeft.setDuration(milis);
		outToLeft.setInterpolator(animationInterpolator);
		return outToLeft;
	}
	
	public Animation outToTopAnimation(long milis, Interpolator animationInterpolator) {
		Animation outToTop = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f);
		outToTop.setDuration(milis);
		outToTop.setInterpolator(animationInterpolator);
		outToTop.setFillAfter(true);
		return outToTop;
	}
	
	/* Scale Animation */
	public Animation scaleInAnimation(long milis, Interpolator animationInterpolator, Context context) {
		Animation scaleIn = AnimationUtils.loadAnimation(context, R.anim.scale_in_anim);
		scaleIn.setDuration(milis);
		scaleIn.setInterpolator(animationInterpolator);
		return scaleIn;
	}	
}
