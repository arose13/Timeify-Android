package ca.timeify.android.views;

import android.os.Bundle;
import android.widget.TextView;
import ca.timeify.android.R;
import ca.timeify.android.activities.BaseActivity;
import ca.timeify.android.data.Fonts;

public class SplashView extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_view);
	}

}
