package edu.osu.bellstest;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.osu.stroketests.R;

public class BellsTestActivity extends Activity {

	
	//Layouts
	private RelativeLayout innerLayout;
	private RelativeLayout linesOverlayLayout;
	
	private BellImageView shape;
	private TherapySession therapySession;
	private BellImageView bell;
	private CountDownTimer timer;
	private String timeTaken;
	private Button btnGenReport, btnSaveScreenshot;
	
	//Arrays
	private HashMap<Integer, Integer> bellRegions;
	private ArrayList<BellImageView> seenBells;
	
	//Static vars
	private static final int TOUCH_PADDING = 0;
	private static final String PREFERENCES = "BellsTest";
	private static final int NUM_OF_BELLS = 35;
	private static final int NUM_OF_REGIONS = 7;
	private static final int NUM_OF_SHAPES = 280;
	private static final int CENTER_DOT_X= 570;
	private static final int CENTER_DOT_Y=858;
	private static final int CENTER_DOT_RADIUS = 4;
	private static final String TAG = "BellsTestActivity";
	private static final int[] images = {R.drawable.car, R.drawable.cow, R.drawable.guitar
		, R.drawable.horse, R.drawable.house, R.drawable.leaf, R.drawable.music, R.drawable.plane
		, R.drawable.seahorse, R.drawable.tree, R.drawable.vespa, R.drawable.pagoda};
	
	//Paints
	Paint p = new Paint();
	Paint textPaint = new Paint();

	//SharedPreferences
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bells_test);
		
		pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
		therapySession = new TherapySession();
		seenBells = new ArrayList<BellImageView>();
		linesOverlayLayout = (RelativeLayout) findViewById(R.id.linesOverlay);
		bellRegions = new HashMap<Integer, Integer>();
		
		//Report Buttons
		btnGenReport = (Button)findViewById(R.id.btnGenerateReport);
		btnGenReport.setX(890f);
		
		//Save screenshot button. Initially hidden
		btnSaveScreenshot = (Button) findViewById(R.id.btnSaveScreenshot);
		btnSaveScreenshot.setX(890f);
		btnSaveScreenshot.setVisibility(View.GONE);
		
		//Setup hashmap
		for (int i = 0; i < NUM_OF_REGIONS; i++){
			bellRegions.put(i, 0);
		}
		
		
		//Sequence of method calls
		setBellsTestLayout();
		instantiateBells();
		instantiateShapes();
		setTimer();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}
	
	@Override
	protected void onStop(){
		super.onPause();
		
		timer.cancel();
	}
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btnGenerateReport:
			openCompletedAlert();
			break;
		case R.id.btnSaveScreenshot:
			saveScreenshot();
			break;
		}
	}
	
	private void setTimer(){
		 timer = new CountDownTimer(300000, 1000) {

		     public void onFinish() {
		    	 openCompletedAlert();
		     }

			@Override
			public void onTick(long arg0) {
				long seconds = (300000-arg0/1000)%60;
				String sec = String.valueOf(seconds);
				if (sec.length() == 1) sec = "0"+sec;
				
				timeTaken = "Time: 0"+((300000-arg0)/60000)+
						":"+sec;
			}
		  }.start();
		 
	}
	
	private void setBellsTestLayout(){
		innerLayout = (RelativeLayout) findViewById(R.id.innerLayout);
		
		//Draw center circle
		innerLayout.addView(new InnerView(this));
	}
	
	private void instantiateShapes(){
		for (int i = 0; i < NUM_OF_SHAPES; i++){
			float[] pos = therapySession.getRandomImagePosition(false);
			shape = new BellImageView(this);
			shape.setImageResource(images[new Random().nextInt(images.length)]);
			shape.setPosition(pos[0], pos[1]);
			therapySession.getShapes()[i] = shape;
			innerLayout.addView(shape);
		}
		
	}
	
	private void instantiateBells(){
		for (int i=0; i< NUM_OF_BELLS; i++){
			float[] pos = therapySession.getRandomImagePosition(true);
			bell = new BellImageView(this);
			bell.setImageResource(R.drawable.bell);
			bell.setPosition(pos[0], pos[1]);
			bell.setPadding(TOUCH_PADDING, TOUCH_PADDING, TOUCH_PADDING, TOUCH_PADDING);
			bell.setBellNumber(i+1);
			bell.setRegion((int)pos[2]);
			therapySession.getBells()[i] = bell;
		}
		
		for (BellImageView b : therapySession.getBells()){
			b.setOnClickListener(bellOnClickListener);
			innerLayout.addView(b);
		}
	}
	
	private boolean isSeen(BellImageView b){
		boolean seen = false;
		for(BellImageView bell : seenBells){
			if (b.getBellNumber() == bell.getBellNumber()){
				seen = true;
			}
		}
		return seen;
	}
	
	private final OnClickListener bellOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			for (BellImageView b : therapySession.getBells()){
				if (b == v){
					b.setImageResource(R.drawable.found);
					if (!isSeen(b)) seenBells.add(b);
					
					//Disable button
					b.setEnabled(false);
					
					//Increment seen count of that region
					int bellCount = bellRegions.get(b.getRegion()-1);
					bellCount++;
					bellRegions.put(b.getRegion()-1, bellCount);
				}
			}	
			if (seenBells.size() == 35){
				openCompletedAlert();
			}
		}
	};
	
	private void openCompletedAlert(){
		//Cancel Timer
		timer.cancel();
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Would you like to generate a test report?");
		
		alert.setPositiveButton("Generate", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton){
				processCompletedTest();
			}
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				processCancel();
			}
		});
		
		alert.show();
	}
	
	private void processCancel(){
		Button btnGenerate = new Button(this);

		//These magic numbers are placing the buttons on the screen
		btnGenerate.setX((3*163)+50);
		btnGenerate.setY(950);
		btnGenerate.setText("Generate Report");
		btnGenerate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				openCompletedAlert();
			}
		});
	}
	
	private void processCompletedTest(){
		//Remove shapes
		for (int i = 0; i < NUM_OF_SHAPES; i++){
			innerLayout.removeViewAt(innerLayout.getChildCount()-1);
		}
		
		//Set remaining bells to disabled
		for(int i = 0; i < innerLayout.getChildCount(); i++){
			innerLayout.getChildAt(i).setEnabled(false);
		}
		
		float c = 163/2;

		TextView bellsPerRegion = new TextView(this);
		for (int i = 0; i < 7; i++){
			bellsPerRegion = new TextView(this);
			bellsPerRegion.setText(Integer.toString(bellRegions.get(i)));
			bellsPerRegion.setTypeface(Typeface.DEFAULT_BOLD);
			bellsPerRegion.setX(c-3f);
			bellsPerRegion.setY(950);
			linesOverlayLayout.addView(bellsPerRegion);
			c+=163;
		}
		String numOfBellsFound = "Total Bells Found: "+seenBells.size();
		TextView tvTimeTaken = new TextView(this);
		tvTimeTaken.setText(timeTaken+"   "+numOfBellsFound);
		tvTimeTaken.setX((3*163) - 20);
		tvTimeTaken.setY(975);
		
		btnGenReport.setVisibility(View.GONE);
		btnSaveScreenshot.setVisibility(View.VISIBLE);
		linesOverlayLayout.addView(tvTimeTaken);
		linesOverlayLayout.addView(new LinesOverlayView(this));
		
	}
	
	private void saveScreenshot(){
		Bitmap screenshot;
		
		//Save screenshot to variable
		btnSaveScreenshot.setVisibility(View.GONE);
		
		View root = (View) findViewById(R.id.outerLayout);
		root.setDrawingCacheEnabled(true);
		screenshot = root.getDrawingCache();
		
		//Save screenshot to device

		String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
		File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot-"+currentDateTimeString+".png");
		FileOutputStream fos;
		try{
			fos = new FileOutputStream(imagePath);
			screenshot.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		}catch(FileNotFoundException e){
			Log.e(TAG, e.getMessage(), e);
		}catch(IOException e){
			Log.e(TAG, e.getLocalizedMessage(), e);
		}
		btnSaveScreenshot.setVisibility(View.VISIBLE);
		Toast.makeText(this, "Screenshot Saved", Toast.LENGTH_SHORT).show();
	}
	
	public class InnerView extends View {

		public InnerView(Context context) {
			super(context);
			setFocusable(true);
		}
		
		@Override
		protected void onDraw(Canvas canvas){
			super.onDraw(canvas);
			p.setColor(Color.BLACK);
			p.setStrokeWidth(1f);
			canvas.drawCircle(CENTER_DOT_X, CENTER_DOT_Y, CENTER_DOT_RADIUS, p);
			
		}
		
	}

	
	public class LinesOverlayView extends View{
		public LinesOverlayView(Context context){
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas){
			super.onDraw(canvas);
			
			int c = 163;
			for (int i = 0; i < 6; i++){
				canvas.drawLine(c, 65, c, 935, p);
				c += 163;
			}
		}
	}

}
