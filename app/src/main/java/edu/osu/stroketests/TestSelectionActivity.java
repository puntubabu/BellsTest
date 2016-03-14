package edu.osu.stroketests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import edu.osu.bellstest.BellsPreTestActivity;
import edu.osu.bellstest.BellsTestActivity;

public class TestSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_selection);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    
    public void onClick(View v){
    	switch(v.getId()){
    	case R.id.btnBellsPreTest:
    		startActivity(new Intent(this, BellsPreTestActivity.class));
    		break;
    	case R.id.btnBellsTest:
    		startActivity(new Intent(this, BellsTestActivity.class));
    		break;
    	}
    }
}
