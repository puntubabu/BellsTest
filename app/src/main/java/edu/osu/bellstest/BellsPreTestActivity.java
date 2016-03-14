package edu.osu.bellstest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.osu.stroketests.R;

public class BellsPreTestActivity extends Activity {
	
	private BellImageView bell, icon;
	private ArrayList<BellImageView> icons;
	
	private int[] images = {R.drawable.car, R.drawable.music, R.drawable.cow, R.drawable.guitar
		, R.drawable.horse, R.drawable.house, R.drawable.leaf, R.drawable.plane
		, R.drawable.seahorse, R.drawable.tree, R.drawable.vespa, R.drawable.pagoda};
	
	private Stack<Integer> stackImages;
	private HashMap<Integer, float[]> positionCoordinates;
	private RelativeLayout layout;
	private TextView findBellMessage;
	private int[] iconIds;
	private TextView tvBellNotFoundMessage, tvBellFoundMessage;
	private Handler uiHandler = new Handler();

	
	private void reInitVars(){
		layout = (RelativeLayout) findViewById(R.id.layoutDisplayIcons);
		icons = new ArrayList<BellImageView>();
		iconIds = new int[2];
		
		//Instantiate Stack
		stackImages = new Stack<Integer>();
		for(int i = 0; i < images.length; i++){
			stackImages.push(images[i]);
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bells_pre_test);
		
		//Initialize misc variables
		positionCoordinates = new HashMap<Integer, float[]>();
		layout = (RelativeLayout) findViewById(R.id.layoutDisplayIcons);
		icons = new ArrayList<BellImageView>();
		findBellMessage = (TextView) findViewById(R.id.tvSelectBellMessage);
		iconIds = new int[2];

		//Instantiate Stack
		stackImages = new Stack<Integer>();
		for(int i = 0; i < images.length; i++){
			stackImages.push(images[i]);
		}
		
		//Set Position Per Cell
		float x = 720f;
		float y = 400f;
		for (int i = 0; i < 3; i++){
			float[] xy = new float[2];
			xy[0] = x;
			xy[1] = y;
			positionCoordinates.put(i, xy);
			x += 230f;
		}

		//Randomize images and Positions
		resetGame();
	}

	private void resetGame(){
		
		//Instantiate bell
		BellImageView bell = new BellImageView(this);
		bell.setImageResource(R.drawable.bell);
		bell.setId(R.drawable.bell);
		bell.setOnClickListener(iconOnClickListener);
		bell.setPadding(10, 10, 10, 10);
		icons.add(bell);
		
		//Set First Icon
		int id = 0;
		BellImageView firstIcon = new BellImageView(this);
		id = getRandomImageId();
		firstIcon.setImageResource(id);
		firstIcon.setId(id);
		firstIcon.setPadding(10, 10, 10, 10);
		iconIds[0] = id;
		firstIcon.setOnClickListener(iconOnClickListener);
		icons.add(firstIcon);
		

		//Set Second Icon
		BellImageView secondIcon = new BellImageView(this);
		id = getRandomImageId();
		secondIcon.setImageResource(id);
		secondIcon.setId(id);
		secondIcon.setPadding(10, 10, 10, 10);
		iconIds[1] = id;
		secondIcon.setOnClickListener(iconOnClickListener);
		icons.add(secondIcon);
		
		//Shuffle arraylist (random)
		Collections.shuffle(icons);
		
		//Set Position
		for (int i = 0; i < 3; i++){
			float[] position = positionCoordinates.get(i);
			icons.get(i).setPosition(position[0], position[1]);
			layout.addView(icons.get(i));
		}
	}
	
	private final OnClickListener iconOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if (v.getId() == R.drawable.bell){
				v.setOnClickListener(null);
				bellFound();
			}
			else {
				bellNotFound();
			}
		}
	};
	
	private void bellFound(){
		
		tvBellFoundMessage = new TextView(this);
		tvBellFoundMessage.setText("Yes, that is the bell");
		tvBellFoundMessage.setX(880f);
		tvBellFoundMessage.setY(450f);
		tvBellFoundMessage.setTextAppearance(this, android.R.style.TextAppearance_Large);

		layout.addView(tvBellFoundMessage);
		for (int i = layout.getChildCount()-1; i > 0; i--){
			if (layout.getChildAt(i).getId() == iconIds[0]){
				layout.removeView(layout.getChildAt(i));
			}
			else if(layout.getChildAt(i).getId() == iconIds[1]){
				layout.removeView(layout.getChildAt(i));
			}
			else if(layout.getChildAt(i).getId() == 1234){
				layout.removeView(layout.getChildAt(i));
			}
		}

		uiHandler.postDelayed(makeTextGone, 1500);
	}
	
	Runnable makeTextGone = new Runnable(){
		   @Override
		   public void run(){
			   layout.removeView(tvBellFoundMessage);
			   for (int i = 0; i < layout.getChildCount(); i++){
					if (layout.getChildAt(i).getId() == R.drawable.bell){
						layout.removeView(layout.getChildAt(i));
					}
				}
			   reInitVars();
			   resetGame();
			   
		   }
		};
	
	private void bellNotFound(){
		tvBellNotFoundMessage = new TextView(this);
		tvBellNotFoundMessage.setText("That was not the bell. Please try again.");
		tvBellNotFoundMessage.setX(790f);
		tvBellNotFoundMessage.setY(450f);
		tvBellNotFoundMessage.setId(1234);
		tvBellNotFoundMessage.setTextAppearance(this, android.R.style.TextAppearance_Large);

		layout.addView(tvBellNotFoundMessage);
		uiHandler.postDelayed(makeNotTextGone, 1500);
	}
	
	Runnable makeNotTextGone = new Runnable(){
		   @Override
		   public void run(){
			   layout.removeView(tvBellNotFoundMessage);
		   }
		};
	
	private int getRandomImageId(){
		Collections.shuffle(this.stackImages);
		return this.stackImages.pop();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

}
