package com.joshuaking.gamemap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class TextureManager {

	private HashMap<String, BufferedImage> textures;
	public TextureManager(){
		textures = new HashMap<String, BufferedImage>();
		BufferedImage addme = null;
		
		try {
			addme = ImageIO.read(this.getClass().getResource("/Assets/Textures/RedWall.png"));
			textures.put("redwall", addme);
			addme = ImageIO.read(this.getClass().getResource("/Assets/Textures/BlueWall.png"));
			textures.put("bluewall", addme);
			addme = ImageIO.read(this.getClass().getResource("/Assets/Textures/Floor.png"));
			textures.put("floor", addme);
			addme = ImageIO.read(this.getClass().getResource("/Assets/Textures/Ceiling.png"));
			textures.put("ceiling", addme);
			addme = ImageIO.read(this.getClass().getResource("/Assets/Entity/Test.png"));
			textures.put("enemy", addme);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public BufferedImage getTexture(String name){
		if(textures.containsKey(name)){
			return textures.get(name);
		}
		return null;
	}
}
