package com.joshuaking.renderer;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Sprite {

	private HashMap<String,BufferedImage> sprites;
	private BufferedImage currSprite;
	public Sprite(){
		sprites = new HashMap<String, BufferedImage>();
	}
	public void addSprite(String name, BufferedImage sprite){
		this.sprites.put(name, sprite);
	}
	public BufferedImage getSprite(String name){
		if(sprites.containsKey(name)){
			return this.sprites.get(name);
		}
		return null;
	}
	public BufferedImage getCurrentSprite(){
		return currSprite;
	}
	public void setCurrentSprite(String name){
		if(sprites.containsKey(name)){
			currSprite = sprites.get(name);
		}
	}
}
