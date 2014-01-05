package com.joshuaking.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class InputManager implements KeyListener{

	private int[] keys = new int[256];
	private boolean[] key_state_up = new boolean[256]; 
	private boolean[] key_state_down = new boolean[256]; 
	
	private boolean key_pressed = false;
	
	private boolean key_released= false; 
	
	private String key_cache = "";
	
	private static InputManager instance = new InputManager();
	
	
	private InputManager() {
	}

	
	public static InputManager GetInstance() {
		if(instance == null){
			instance = new InputManager();
		}
		return instance;
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		if( e.getKeyCode() >= 0 && e.getKeyCode() < 256 ) {
			keys[e.getKeyCode()] = (int) System.currentTimeMillis();
			key_state_down[e.getKeyCode()] = true;
			key_state_up[e.getKeyCode()] = false;
			key_pressed = true;
			key_released = false;
		}
	}

	
	@Override
	public void keyReleased(KeyEvent e) {
		if( e.getKeyCode() >= 0 && e.getKeyCode() < 256 ) {
			keys[e.getKeyCode()] = 0;
			key_state_up[e.getKeyCode()] = true;
			key_state_down[e.getKeyCode()] = false;
			key_pressed = false;
			key_released = true;
		}
	}

	
	@Override
	public void keyTyped(KeyEvent e) {	
		key_cache += e.getKeyChar();
		
	}
	
	
	public boolean IsKeyDown( int key ) {
		return key_state_down[key];
	}
	
	
	public boolean IsKeyUp( int key ) {
		return key_state_up[key];
	}

	
	public boolean IsAnyKeyDown() {
		return key_pressed;
	}
	
	
	public boolean IsAnyKeyUp() {
		return key_released;
	}
	
	
	public void Update() {
		//clear out the key up states
		key_state_up = new boolean[256];
		key_released = false;
		if( key_cache.length() > 1024 ) {
			key_cache = "";
		}
	}
}
