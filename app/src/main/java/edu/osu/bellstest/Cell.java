package edu.osu.bellstest;

public class Cell {
	private int num;
	private int region;
	private int left;
	private int right;
	private int up;
	private int down;
	private int center;
	
	//Constructors
	
	public Cell(int n, int r){
		this.num = n;
		this.region = r;
	}
	
	public Cell(int n){
		this.num = n;
	}
	
	public Cell(){
		
	}
	
	public Cell(int l, int r, int u, int d){
		setCellBoundaries(l, r, u ,d);
	}
	
	//Getters
	
	public int getRegion(){
		return this.region;
	}
	public int getUp(){
		return this.up;
	}
	public int getDown(){
		return this.down;
	}
	public int getRight(){
		return this.right;
	}
	public int getLeft(){
		return this.left;
	}
	public int getCenter(){
		return this.center;
	}
	public int getCellNum(){
		return this.num;
	}
	
	//Setters
	
	public void setCellNum(int c){
		this.num = c;
	}
	
	private void setCellBoundaries(int l, int r, int u, int d){
		this.left = l;
		this.right = r;
		this.up = u;
		this.down = d;
		this.center = (this.right - this.left)/2;
	}

}
