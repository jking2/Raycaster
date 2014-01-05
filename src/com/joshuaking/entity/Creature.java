package com.joshuaking.entity;

import com.joshuaking.gamemap.GameMap;


public class Creature extends Entity{
	
	protected int maxHealth;
	protected int currHealth;
	protected int attackPower;
	protected double hitBox;
	protected boolean alive;
	protected String name;
	
	public Creature(double startPosX, double startPosY, double moveSpeed,
			double rotSpeed) {
		super(startPosX, startPosY, moveSpeed, rotSpeed);
	}
	@Override
	public void update(GameMap map){
		if(currHealth<=0){
			alive=false;
		}
		if(alive==false){
			map.removeCreature(this);
		}
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	public int getCurrHealth() {
		return currHealth;
	}
	public void setCurrHealth(int currHealth) {
		this.currHealth = currHealth;
	}
	public int getAttackPower() {
		return attackPower;
	}
	public void setAttackPower(int attackPower) {
		this.attackPower = attackPower;
	}
	public double getHitBox() {
		return hitBox;
	}
	public void setHitBox(double hitBox) {
		this.hitBox = hitBox;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void damage(int amount){
		currHealth-=amount;
	}
}
