package com.joshuaking.combat;

import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuaking.entity.Entity;
import com.joshuaking.gamemap.GameMap;
import com.joshuaking.renderer.Sprite;

public class Attack extends Entity{

	private double dirX;
	private double dirY;
	private double maxDistance;
	private double distanceTraveled;
	private boolean alive;
	private double width;
	private int attackPower;
	private Polygon hitBox;
	public Attack(double dirX, double dirY, double speed, double maxDistance,double posX, double posY,double width,int attackPower){
		super(posX,posY,speed,0);
		this.dirX=dirX;
		this.dirY=dirY;
		this.maxDistance=maxDistance;
		this.distanceTraveled=0;
		this.alive = true;
		this.width=width;
		this.attackPower=attackPower;
		this.sprite = new Sprite();
	}
	public void update(GameMap map){
		double lastX = xPos;
		double lastY = yPos;
		xPos += dirX*moveSpeed;
		yPos += dirY*moveSpeed;
		distanceTraveled+=moveSpeed;
		if(map.getTile((int)xPos, (int)yPos).isBlocked()){
			alive=false;
		}else{
			hitBox = new Polygon();
			hitBox.addPoint((int)(((lastX-(width*dirY))*1000)), (int)(((lastY + (width*dirX))*1000)));
			hitBox.addPoint((int)(((lastX+(width*dirY))*1000)), (int)(((lastY - (width*dirX))*1000)));
			hitBox.addPoint((int)(((xPos-(width*dirY))*1000)), (int)(((yPos + (width*dirX))*1000)));
			hitBox.addPoint((int)(((xPos+(width*dirY))*1000)), (int)(((yPos - (width*dirX))*1000)));
			for(int x=0;x<map.getNumberOfCreatures();x++){
				//Cycle through all the enemies
				if(hitBox.contains((int)(map.getCreature(x).getxPos()*1000), (int)(map.getCreature(x).getyPos()*1000))){
					System.out.println("HIT!");
					map.getCreature(x).damage(attackPower);
					this.alive=false;
				}
				
			}
		}
		if(distanceTraveled>maxDistance){
			this.alive = false;
		}
		if(this.alive == false){
			map.removeEntity(this);
		}
	}
	public int getXPos(){
		return (int)xPos;
	}
	public int getYPos(){
		return (int) yPos;
	}
	public Polygon getHitBox(){
		return hitBox;
	}
}
