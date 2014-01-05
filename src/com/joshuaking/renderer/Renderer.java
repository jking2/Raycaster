package com.joshuaking.renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.joshuaking.combat.Attack;
import com.joshuaking.gamemap.GameMap;
import com.joshuaking.input.InputManager;

public class Renderer extends Canvas{

	private Graphics2D graphics;
	private BufferStrategy buffer;
	private int width;
	private int height;
	public Renderer(int width, int height){
		super();
		this.width=width;
		this.height=height;
	}
	public void initilize(){
		this.setBounds(0, 0, width, height);
		this.setIgnoreRepaint(true);
		this.addKeyListener(InputManager.GetInstance());
		this.createBufferStrategy(2);
	    buffer = this.getBufferStrategy();                    
	    this.setBackground(Color.black);
	}
	public int getMyWidth(){
		return this.width;
	}
	public int getMyHeight(){
		return this.height;
	}
	public void openRenderer(){
		graphics = (Graphics2D) buffer.getDrawGraphics();
		graphics.clearRect(0, 0, width, height);
	}
	public void closeRenderer(){
		graphics.dispose();
		buffer.show();
	}
	public void renderMap(GameMap map){
		RayCaster.cast(map,this);
	}
	public void renderImageExact(BufferedImage image,int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2){
		graphics.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}
	public void renderImage(BufferedImage image, int x, int y){
		graphics.drawImage(image, x, y, null);
	}
	public void renderSquare(Color c){
		graphics.setColor(c);
		graphics.fillRect(80, 80, 80, 80);
	}
	
}
