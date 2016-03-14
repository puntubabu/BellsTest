package edu.osu.bellstest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BellImageView extends ImageView{

	private int bellNumber;
	private int region;
	
	public BellImageView(Context context) {
		super(context);

	}
	
	public BellImageView(Context context, AttributeSet attrs){
		super(context);
	}
	
	public void setPosition(float xPos, float yPos){
		this.setX(xPos);
		this.setY(yPos);
	}
	
	public void setRegion(int r){
		this.region = r;
	}
	
	public int getRegion(){
		return this.region;
	}
	
	public int getBellNumber(){
		return this.bellNumber;
	}
	public void setBellNumber(int num){
		this.bellNumber = num;
	}
}
