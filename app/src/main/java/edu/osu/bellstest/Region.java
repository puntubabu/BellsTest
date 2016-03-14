package edu.osu.bellstest;

import java.util.ArrayList;

public class Region {
	private int regionNum;
	private ArrayList<Cell> top;
	private ArrayList<Cell> bottom;
	
	public Region(int n){
		this.regionNum = n;
		this.top = new ArrayList<Cell>();
		this.bottom = new ArrayList<Cell>();
	}
	
	public int size(){
		return this.top.size() + this.bottom.size();
	}
	
	public int getRegionNum(){
		return this.regionNum;
	}
	
	public void addCellToTop(Cell c){
		this.top.add(c);
	}
	
	public void addCellToBottom(Cell c){
		this.bottom.add(c);
	}
	
	public ArrayList<Cell> getRegionTop(){
		return this.top;
	}
	
	public ArrayList<Cell> getRegionBottom(){
		return this.bottom;
	}
	
}
