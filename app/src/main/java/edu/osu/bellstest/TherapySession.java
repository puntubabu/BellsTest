package edu.osu.bellstest;

import java.util.ArrayList;
import java.util.Random;

public class TherapySession {
	
	//Collections
	private ArrayList<Region> bellsRegions;
	private ArrayList<Region> shapesRegions;
	
	//Arrays
	private BellImageView[] bells;
	private BellImageView[] shapes;

	//Static final variables
	private static final int NUM_OF_REGIONS = 7;
	private static final int BUFFER = 15;
	private static final int BELLS_PER_REGION = 5;
	private static final int NUM_OF_SHAPES = 280;
	private static final int NUM_OF_CELLS = 336;
	private static final float X_OFFSET = 15f;
	private static final float Y_OFFSET = 15f;
	private static final float CELL_DIM = 53f;
	private static final String TAG = "TherapySession";

	
	public TherapySession(){
		//Instantiate variables
		bells = new BellImageView[BELLS_PER_REGION * NUM_OF_REGIONS];
		bellsRegions = new ArrayList<Region>();
		shapes = new BellImageView[NUM_OF_SHAPES];
		shapesRegions = new ArrayList<Region>();
		
		//Initialize Array of regions
		for (int i = 1; i < NUM_OF_REGIONS+1; i++){
			Region r = new Region(i);
			this.bellsRegions.add(r);
		}
		fillCellsPerRegion();
	}
	
	private void fillCellsPerRegion(){
		int region = 1;
		
		//Fill top cells
			for (int i = 1; i < NUM_OF_CELLS/2 + 1; i++){
				Cell c = new Cell(i, region);
				this.bellsRegions.get(region-1).addCellToTop(c);
				if (i%3 == 0) region++;
				if (i%21 == 0) region = 1;
			}
		region = 1;
		
		//Fill bottom cells
			for (int j = NUM_OF_CELLS/2 + 1; j < NUM_OF_CELLS+1; j++){
				Cell c = new Cell(j, region);
				this.bellsRegions.get(region-1).addCellToBottom(c);
				if (j%3 == 0) region++;
				if (j%21 == 0) region = 1;
			}
		}
	
	private Cell getRandomCell(boolean bell){
		Region region = getRandomRegion(bell);
		Cell retCell = new Cell();
		

		if(region.getRegionTop().size() >= region.getRegionBottom().size()){
			retCell = region.getRegionTop().remove(
					new Random().nextInt(region.getRegionTop().size()));
		}
		else if(region.getRegionTop().size() < region.getRegionBottom().size()){
			retCell = region.getRegionBottom().remove(
					new Random().nextInt(region.getRegionBottom().size()));
		}
		
		//If we are adding bells to the screen
		if (bell) {
			if (region.size() == 43) {
				this.shapesRegions.add(region);
				this.bellsRegions.remove(region);
			} else
				this.bellsRegions.add(region);
		}
		//Else, we are adding images
		else{
			if (region.size() == 3){
				this.shapesRegions.remove(region);
			}else{
				this.shapesRegions.add(region);
			}
		}
		
		return retCell;
	}
	
	private Region getRandomRegion(boolean bell){
			if (bell){
				return this.bellsRegions.remove(new Random().nextInt(bellsRegions.size()));
			}
			else{
				return this.shapesRegions.remove(new Random().nextInt(shapesRegions.size()));
			}
		}
	
	public float[] staggerImagePlacementInCell(float[] image){
		for (int i = 0; i < 2; i++){
			boolean pos = Math.random() < 0.5;
			if (pos)
				image[i] = image[i] + new Random().nextInt(BUFFER);
			else if(!pos)
				image[i] = image[i] - new Random().nextInt(BUFFER);
		}
		return image;
	}
	
	public float[] getRandomImagePosition(boolean bell){
		Cell cell = getRandomCell(bell);
		float[] ret = new float[3];
		int column = cell.getCellNum() % 21;
		
		if(column == 0)
			column = (cell.getCellNum()-1) % 21 + 1;
		
		int mult = 163;
		int row = cellRow(cell.getCellNum());
		int retRegion = getRegion(column);
		
		switch (retRegion){
		case 1:
			mult = 0;
			break;
		case 2:
			mult *= retRegion-1;
			break;
		case 3:
			mult *= retRegion-1;
			break;
		case 4:
			mult *= retRegion-1;
			break;
		case 5:
			mult *= retRegion-1;
			break;
		case 6:
			mult *= retRegion-1;
			break;
		case 7:
			mult *= retRegion-1;
			break;
		}
		
		float columnOffsetPixels = mult + CELL_DIM*(column % 3);
		
		if (cell.getCellNum() > 21)
		{
			float rowOffsetPixels = (row - 1) * CELL_DIM;
			ret[0] = X_OFFSET + columnOffsetPixels;
			ret[1] = Y_OFFSET + rowOffsetPixels;
			ret[2] = (float) retRegion;
		}
		else
		{
			ret[0] = X_OFFSET + columnOffsetPixels;
			ret[1] = Y_OFFSET;
			ret[2] = (float) retRegion;
		}
		
		ret = staggerImagePlacementInCell(ret);
		
		return ret;
	}
	
	private int cellRow (int cellNum){
		return (int) Math.floor(((cellNum - 1)/21)+1);
	}
	
	public int getRegion(int column){
		if (column > 3 && column < 7 ){
			return 2;
		}
		else if (column > 6 && column < 10){
			return 3;
		}
		else if (column > 9 && column < 13){
			return 4;
		}
		else if (column > 12 && column < 16){
			return 5;
		}
		else if (column > 15 && column < 19){
			return 6;
		}
		else if (column > 18 && column < 22){
			return 7;
		}
		else if (column > 0 && column < 4){
			return 1;
		}
		else{
			return -1;
		}
	}
	
	public BellImageView[] getBells(){
		return this.bells;
	}
	
	public BellImageView[] getShapes(){
		return this.shapes;
	}
	
}
