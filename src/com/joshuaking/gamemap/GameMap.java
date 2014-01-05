package com.joshuaking.gamemap;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.joshuaking.combat.Attack;
import com.joshuaking.entity.Creature;
import com.joshuaking.entity.Entity;
import com.joshuaking.entity.Player;
import com.joshuaking.input.InputManager;
import com.joshuaking.renderer.Sprite;

public class GameMap {

	private int width;
	private int height;
	private ArrayList<ArrayList<Tile>> map;
	private ArrayList<Entity> entities;
	private ArrayList<Creature>creatures;
	private Player player;
	private BufferedImage floor;
	private BufferedImage ceiling;
	
	protected GameMap(int width, int height){
		this.width = width;
		this.height = height;
		map = new ArrayList<ArrayList<Tile>>();
		entities = new ArrayList<Entity>();
		creatures = new ArrayList<Creature>();
	}
	public Tile getTile(int x, int y){
		return map.get(y).get(x);
	}
	protected void setTile(int x, int y,Tile newTile){
		map.get(y).set(x,newTile);
	}
	protected void setMap(ArrayList<ArrayList<Tile>> map){
		this.map = map;
	}
	public void setPlayer(Player player){
		this.player=player;
	}
	public Player getPlayer(){
		return player;
	}
	public void update(){
		for(int x=0;x<entities.size();x++){
			entities.get(x).update(this);
		}
		this.playerInput();
		player.update(this);
	}
	private void playerInput(){
		
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public BufferedImage getFloor() {
		return floor;
	}
	protected void setFloor(BufferedImage floor) {
		this.floor = floor;
	}
	public BufferedImage getCeiling() {
		return ceiling;
	}
	protected void setCeiling(BufferedImage ceiling) {
		this.ceiling = ceiling;
	}
	public int getNumberOfCreatures(){
		return creatures.size();
	}
	public Creature getCreature(int index){
		return creatures.get(index);
	}
	public void addCreature(Creature creature){
		creatures.add(creature);
		entities.add(creature);
	}
	public void removeCreature(Creature removeMe){
		this.creatures.remove(removeMe);
		this.entities.remove(removeMe);
	}
	public int getNumberOfEntities(){
		return entities.size();
	}
	public Entity getEntity(int index){
		return entities.get(index);
	}
	public void addEntity(Entity entity){
		entities.add(entity);
	}
	public void removeEntity(Entity removeMe){
		this.entities.remove(removeMe);
	}
}
