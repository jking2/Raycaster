package com.joshuaking.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuaking.gamemap.GameMap;

public class EnemyTest extends Creature{

	private boolean goingForward,goingBackward;
	public EnemyTest(double startPosX, double startPosY) {
		super(startPosX, startPosY, 0.08, 0.08);
		goingForward = true;
		goingBackward=false;
		this.currHealth=100;
		this.maxHealth=100;
		this.alive=true;
		this.hitBox=0.3;
		this.name="Chuck";
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource("/Assets/Entity/Test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.sprite.addSprite("normal", img);
		this.sprite.setCurrentSprite("normal");
	}
	@Override
	public void update(GameMap map){
		super.update(map);
		if (goingForward) {
			if (map.getTile((int)(xPos+xDir*moveSpeed), (int)yPos).isBlocked()==false)
				xPos += xDir * moveSpeed;
			else{
				goingForward = false;
				goingBackward = true;
			}
			if (map.getTile((int)xPos, (int)(yPos + yDir * moveSpeed)).isBlocked()==false)
				yPos += yDir * moveSpeed;
			else{
				goingForward = false;
				goingBackward = true;
			}
		}
		else if (goingBackward) {
			if (map.getTile((int)(xPos - xDir * moveSpeed), (int)yPos).isBlocked()==false) {
				xPos -= xDir * moveSpeed;
			}
			else{
				goingForward = true;
				goingBackward = false;
			}
			if (map.getTile((int)xPos, (int)(yPos - yDir * moveSpeed)).isBlocked()==false) {
				yPos -= yDir * moveSpeed;
			}
			else{
				goingForward = true;
				goingBackward = false;
			}
		}
	}

}
