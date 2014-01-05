package com.joshuaking.gamemap;

import java.util.ArrayList;

import com.joshuaking.entity.EnemyTest;
import com.joshuaking.entity.Entity;
import com.joshuaking.entity.Player;
import com.joshuaking.renderer.Sprite;

public class MapMaker {

	public static GameMap makeMap(int type, int difficulty){
		switch(type){
		case 0 : return testMap(difficulty);
		default : return testMap(difficulty);
		}
	}
	private static GameMap testMap(int d){
		TextureManager textures = new TextureManager();
		int width = 11*d;
		int height = 14*d;
		GameMap map = new GameMap(width,height);
		ArrayList<ArrayList<Tile>> list = new ArrayList<ArrayList<Tile>>();
		for(int y=0;y<height;y++){
			ArrayList<Tile> row = new ArrayList<Tile>();
			for(int x=0;x<width;x++){
				Tile tile = new Tile(x,y,false);
				tile.setTexture(textures.getTexture("floor"));
				tile.setCeilingTexture(textures.getTexture("ceiling"));
				row.add(tile);
			}
			list.add(row);
		}
		
		map.setMap(list);
		for(int x=0;x<height;x++){
			map.getTile(0, x).setTexture(textures.getTexture("bluewall"));
			map.getTile(0, x).setBlocked(true);
			map.getTile(width-1, x).setTexture(textures.getTexture("bluewall"));
			map.getTile(width-1, x).setBlocked(true);
		}
		for(int x=0;x<width;x++){
			map.getTile(x, 0).setTexture(textures.getTexture("bluewall"));
			map.getTile(x, 0).setBlocked(true);
			
			map.getTile(x, height-1).setTexture(textures.getTexture("bluewall"));
			map.getTile(x, height-1).setBlocked(true);
		}
		for(int x=2;x<height-2;x++){
			map.getTile(4, x).setTexture(textures.getTexture("redwall"));
			map.getTile(4, x).setBlocked(true);
		}
		map.getTile(1, 1).setCeilingTexture(textures.getTexture("redwall"));
		map.setCeiling(textures.getTexture("ceiling"));
		map.setFloor(textures.getTexture("floor"));
		map.setPlayer(new Player());
		map.getPlayer().setxPos(10);
		map.getPlayer().setyPos(10);
		EnemyTest e = new EnemyTest(2,2);
		map.addCreature(e);
		return map;
	}
}
