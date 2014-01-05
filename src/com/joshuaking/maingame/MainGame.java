package com.joshuaking.maingame;

import javax.swing.JFrame;

import com.joshuaking.entity.Player;
import com.joshuaking.gamemap.GameMap;
import com.joshuaking.gamemap.MapMaker;
import com.joshuaking.input.InputManager;
import com.joshuaking.renderer.Renderer;

public class MainGame extends Thread{

	private GameMap map;
	private Renderer renderer;
	private JFrame frame;
	private final int WIDTH = 640;
	private final int HEIGHT = 480;
	private final double timeBetweenUpdates = 1000000000/30.0;
	private boolean running;
	
	public MainGame(){
		super();
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(100, 100);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		map = MapMaker.makeMap(0, 3);
		renderer = new Renderer(WIDTH,HEIGHT);
		frame.add(renderer);
		renderer.initilize();
		frame.addKeyListener(InputManager.GetInstance());
		renderer.addKeyListener(InputManager.GetInstance());
	}
	@Override
	public void run(){
		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);
		running = true;
		double startTime = System.nanoTime();
		int counter = 0;
		int fps = 60;
		while(running){
			double currentTime = System.nanoTime();
			int updateCount = 0;
			while(currentTime - lastUpdateTime > timeBetweenUpdates && updateCount <5){
				map.update();
				lastUpdateTime += timeBetweenUpdates;
				updateCount++;
			}
			if(currentTime - lastUpdateTime > timeBetweenUpdates){
				lastUpdateTime = currentTime - timeBetweenUpdates;
			}
			renderer.openRenderer();
			renderer.renderMap(map);
			renderer.closeRenderer();
			counter++;
			lastRenderTime = currentTime;
			int thisSecond = (int) (lastUpdateTime / 1000000000);
			System.out.println("counter: "+counter);
			if(thisSecond > lastSecondTime){
				System.out.println("NEW SECOND " + thisSecond + " " + counter);
				fps=counter;
				counter = 0;
				lastSecondTime = thisSecond;
			}
			while(currentTime - lastRenderTime < 1000000000/60 && currentTime - lastUpdateTime < timeBetweenUpdates){
				Thread.yield();
				try{
					Thread.sleep(1);
				}catch(Exception e){
					e.printStackTrace();
				}
				currentTime = System.nanoTime();
			}

		}
	}
}
