package com.joshuaking.gamemap;

import java.awt.image.BufferedImage;

import com.joshuaking.entity.Entity;

public class Tile {

	private BufferedImage texture;
	private BufferedImage ceilingTexture;
	private BufferedImage floorTexture;
	private int xPos;
	private int yPos;
	private boolean isBlocked;
	
	public Tile(int x, int y, boolean isBlocked){
		this.xPos=x;
		this.yPos=y;
		this.isBlocked=isBlocked;
		this.texture=null;
		this.ceilingTexture=null;
		this.floorTexture=null;
	}
	public BufferedImage getTexture() {
		return texture;
	}
	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	public BufferedImage getCeilingTexture() {
		return ceilingTexture;
	}
	public void setCeilingTexture(BufferedImage ceilingTexture) {
		this.ceilingTexture = ceilingTexture;
	}
	public BufferedImage getFloorTexture() {
		return floorTexture;
	}
	public void setFloorTexture(BufferedImage floorTexture) {
		this.floorTexture = floorTexture;
	}
	
}
