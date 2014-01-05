package com.joshuaking.entity;


import com.joshuaking.gamemap.GameMap;
import com.joshuaking.renderer.Sprite;

public class Entity {

	protected double moveSpeed;
	protected double rotSpeed;
	protected double xPos;
	protected double yPos;
	protected double xDir;
	protected double yDir;
	protected Sprite sprite;
	public Entity(double startPosX, double startPosY, double moveSpeed, double rotSpeed){
		this.xPos=startPosX;
		this.yPos=startPosY;
		this.rotSpeed=rotSpeed;
		this.xDir=-1;
		this.yDir=0;
		this.moveSpeed=moveSpeed;
		sprite = new Sprite();
	}
	public void update(GameMap map) {
		
	}
	public double getMoveSpeed() {
		return moveSpeed;
	}
	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	public double getRotSpeed() {
		return rotSpeed;
	}
	public void setRotSpeed(double rotSpeed) {
		this.rotSpeed = rotSpeed;
	}
	public double getxPos() {
		return xPos;
	}
	public void setxPos(double xPos) {
		this.xPos = xPos;
	}
	public double getyPos() {
		return yPos;
	}
	public void setyPos(double yPos) {
		this.yPos = yPos;
	}
	public double getxDir() {
		return xDir;
	}
	public void setxDir(double xDir) {
		this.xDir = xDir;
	}
	public double getyDir() {
		return yDir;
	}
	public void setyDir(double yDir) {
		this.yDir = yDir;
	}
	public Sprite getSprite() {
		return sprite;
	}
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}
