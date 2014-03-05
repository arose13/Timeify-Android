package ca.timeify.android.data;

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

public class CustomAnimation {
	
	public long secondsToMilis(long seconds) {
		return (seconds*1000);
	}
	
	public Animation loopAnimation(Animation inputAnimation) {
		inputAnimation.setRepeatMode(Animation.RESTART);
		inputAnimation.setRepeatCount(Animation.INFINITE);
		return inputAnimation;
	}
	
	public Animation fromRightToLeftAnimation(long milis, Interpolator animationInterpolator) {
		Animation fromRightToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
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
	
}
