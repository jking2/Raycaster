package com.joshuaking.entity;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuaking.combat.Attack;
import com.joshuaking.gamemap.GameMap;
import com.joshuaking.input.InputManager;

public class Player extends Creature {

	private double planeX;
	private double planeY;
	private int cooldown;
	public Player(){
		super(6, 6, 0.08, 0.06);
		planeX=0;
		planeY=0.66;
		attackPower = 50;
		cooldown = 6;
	}
	@Override
	public void update(GameMap map){
		
		if (InputManager.GetInstance().IsKeyDown(KeyEvent.VK_UP)) {
			if (map.getTile((int)(xPos+xDir*moveSpeed), (int)yPos).isBlocked()==false)
				xPos += xDir * moveSpeed;
			if (map.getTile((int)xPos, (int)(yPos + yDir * moveSpeed)).isBlocked()==false)
				yPos += yDir * moveSpeed;
		}
		else if (InputManager.GetInstance().IsKeyDown(KeyEvent.VK_DOWN)) {
			if (map.getTile((int)(xPos - xDir * moveSpeed), (int)yPos).isBlocked()==false) {
				xPos -= xDir * moveSpeed;
			}
			if (map.getTile((int)xPos, (int)(yPos - yDir * moveSpeed)).isBlocked()==false) {
				yPos -= yDir * moveSpeed;
			}
		}
		if(InputManager.GetInstance().IsKeyDown(KeyEvent.VK_LEFT)){
			double oldDirX = xDir;
			xDir = xDir * Math.cos(rotSpeed) - yDir * Math.sin(rotSpeed);
			yDir = oldDirX * Math.sin(rotSpeed) + yDir * Math.cos(rotSpeed);
			double oldPlaneX = planeX;
			planeX = planeX * Math.cos(this.getRotSpeed()) - planeY * Math.sin(this.getRotSpeed());
			planeY = oldPlaneX * Math.sin(this.getRotSpeed()) + planeY * Math.cos(this.getRotSpeed());
		}
		if(InputManager.GetInstance().IsKeyDown(KeyEvent.VK_RIGHT)){
			double oldDirX = xDir;
			xDir = xDir * Math.cos(-rotSpeed) - yDir * Math.sin(-rotSpeed);
			yDir = oldDirX * Math.sin(-rotSpeed) + yDir * Math.cos(-rotSpeed);
			double oldPlaneX = planeX;
			planeX = planeX * Math.cos(-this.getRotSpeed()) - planeY * Math.sin(-this.getRotSpeed());
			planeY = oldPlaneX * Math.sin(-this.getRotSpeed()) + planeY * Math.cos(-this.getRotSpeed());
		}
		if(InputManager.GetInstance().IsKeyDown(KeyEvent.VK_SPACE)){
			if(cooldown == 6){
				Attack a = new Attack(xDir,yDir,0.06,10,xPos,yPos,0.5,50);
				BufferedImage img = null;
				try {
					img = ImageIO.read(this.getClass().getResource("/Assets/Weapons/Bullet.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				a.getSprite().addSprite("normal", img);
				a.getSprite().setCurrentSprite("normal");
				map.addEntity(a);
				cooldown=0;
			}else{
				cooldown++;
				if(cooldown>6){
					cooldown=6;
				}
			}
		}
	}
	public double getPlaneX() {
		return planeX;
	}
	public void setPlaneX(double planeX) {
		this.planeX = planeX;
	}
	public double getPlaneY() {
		return planeY;
	}
	public void setPlaneY(double planeY) {
		this.planeY = planeY;
	}
}
