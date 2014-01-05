package com.joshuaking.renderer;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import com.joshuaking.gamemap.GameMap;
import com.joshuaking.gamemap.Tile;
import com.joshuaking.utility.Utility;

public class RayCaster {

	/**
	 * This is the function to display the map from the players perspective
	 * @param map The map we are using, includes the player and enemies
	 * @param renderer The canvas we are using to display the world
	 */
	public static void cast(GameMap map, Renderer renderer){
		//This is the Image that the player will ultimately see on the screen
		BufferedImage scene = new BufferedImage(renderer.getWidth(), renderer.getHeight(), BufferedImage.TYPE_INT_ARGB);
		//These are the pixels that make up the image
		int[] scenePixels = ((DataBufferInt)scene.getRaster().getDataBuffer()).getData();
		//Height and widths of the floor textures
        int fWidth = map.getFloor().getWidth();
        int fHeight = map.getFloor().getHeight();
        int cWidth = map.getCeiling().getWidth();
        int cHeight = map.getCeiling().getHeight();
        //The pixels of the floor and ceiling textures. The reason we use the pixels themselves is because BufferedImage.setRGB is 
        //very very slow
        int[] floorTexture;
        int[] ceilingTexture;
        //The pixels in byte form
        byte[] floorB = ((DataBufferByte)map.getFloor().getRaster().getDataBuffer()).getData();
        byte[] ceilingB = ((DataBufferByte)map.getCeiling().getRaster().getDataBuffer()).getData();
        //Convert the byte arrays into int arrays
        floorTexture = Utility.convertByteArrayToInt(floorB);
        ceilingTexture = Utility.convertByteArrayToInt(ceilingB);
        //This is the zBuffer. this keeps track of the depth of all entitys rendered in the scene
		ArrayList<Double>zBuffer = new ArrayList<Double>();
		//An int array and byte array for use by the current texture we are drawing
		int[] texturePixels = null;
		byte[] textureB = null;
		//Send out a ray at each column of pixels to determine what will be displayed on that column
		for(int screenX=0;screenX<renderer.getMyWidth();screenX++){
			//The X coordinate of the camera plane. The far right of the screen is 2,
			//the center is 1, and the far left is 0
			double cameraX = 2 * screenX / (double) renderer.getMyWidth()-1; 
			//The position of the ray in space. This starts at the player's position
			double rayPosX = map.getPlayer().getxPos();
			double rayPosY = map.getPlayer().getyPos();
			//The direction this ray will be sent out
			/*
			 *      screenX=30
			 *      cameraX is about here
			 *        |
			 *  0     v                              180
			 *  |-----X-------------------------------| <-What we see
			 *  \     \  <--Ray   |                  /
			 *   \     \          |                 /   <---|
			 * 	  \ 	 \		 Dir               /        |
			 *     \      \       |               / <------FOV
			 * 		\   	\	  |              /          |
			 * 		 \		 \  Player          /   <-------|
			 */
			double rayDirX = map.getPlayer().getxDir() + map.getPlayer().getPlaneX() * cameraX;
			double rayDirY = map.getPlayer().getyDir() + map.getPlayer().getPlaneY() * cameraX;
			//The position of the ray on the game "grid"
			//A ray may be at 3.213455554364364, 5.328563285634 but it needs to be
			//relateable to the square grid of integers
			int mapX = (int)rayPosX;
			int mapY = (int)rayPosY;
			//The side distances are the distances that a ray must travel to reach the first walls
			//on the both and x and y axis
			double sideDistX;
			double sideDistY;
			//The delta distances are the distances the ray would have to travel
			//to reach the next grid square going in both the x and y directions
			double deltaDistX = Math.sqrt(1+(rayDirY*rayDirY)/(rayDirX*rayDirX));
			double deltaDistY = Math.sqrt(1+(rayDirX * rayDirX)/(rayDirY * rayDirY));
			/*
			 *A sample grid
			 *O = start of the ray
			 * ------> = Direction ray is head
			 * ---------------^---------------------------- a
			 * |             /             |              |
			 * |            /|             |              |
			 * |           / |             |              |
			 * |          /  |             |              |
			 * ----------/--------------------------------- b
			 * |        /    |             |              |
			 * |       /     |             |              |
			 * |      /      |             |              |
			 * |     /       |             |              |
			 * -----/-------------------------------------- c
			 * |   /         |             |              |
			 * |  /          |             |              |
			 * | /           |             |              |
			 * |O            |             |              |
			 * -------------------------------------------- d
			 * 0             1             2              3
			 * 
			 * sideDistX will start out as the distance from the origin of the raycast
			 * to the its first collision on the X axis, which in this case, will be
			 * at 1.
			 * 
			 * sideDist Y will start as the distance from origin to the first
			 * collision on the Y axis, which is right away at c
			 * 
			 * deltaDistX is the distance required to increase from one x to the next x.
			 * In this case, it would be the distance to go from one number, to the next,
			 * such as 0 to 1, or 1 to 2 etc.
			 * 
			 * deltaDistY is the distance required to go from one y to the next y.
			 * In this case, it would the distance between the letters such as a to b,
			 * or c to d.
			 * 
			 */
			
			//This will simply be how long the ray traveled from its origin to the wall 
			//it collided with
			double wallDist;
			//The direction of the next square on the grid we will "step into". 
			//These will either be +1 or -1
			int stepX;
			int stepY;
			//Whether or not we hit a wall yet. Since we will assume that a ray must hit, 
			//all maps must be closed.
			int hit = 0;
			//The side that the wall was hit on. Think of it as which way the wall is going away
			//from us.This allows us to "shade" walls as well. 0 is going left, 1 is going right. 
			/**
			 * 
			 * \                 /
			 *  \               /
			 *   \            /
			 *    \          /
			 *     \       /
			 *      \     /
			 *       \  /
			 *    0   \/     1
			 * 		   |
			 *  	   |         /
			 *  \      |        /
			 *   \     |      /
			 *    \    |     /
			 * 	   \   |   /
			 *      \  |  /
			 *       \ |/
			 *        \/
			 *      A corner of a wall.
			 *      The side going away to the
			 *      left is marked as 0
			 *      while the side going away
			 *      to the right is marked 1  
			 *        
			 *        */
			int side = 0;
			//Now we calculate the starting values of the stepX and stepY by determining
			//which way our ray is headed. Then we find the sideDistX and sideDistY
			//by measuring it, like we did with our little grid earlier.
			if(rayDirX<0){
				stepX = -1;
				sideDistX = (rayPosX - mapX)*deltaDistX;
			}else{
				stepX = 1;
				sideDistX = (mapX+1 - rayPosX)*deltaDistX;
			}
			if(rayDirY<0){
				stepY = -1;
				sideDistY = (rayPosY - mapY)*deltaDistY;
			}else{
				stepY = 1;
				sideDistY = (mapY +1 - rayPosY)*deltaDistY;
			}
			//The main loop for finding when the ray hit a wall.
			//So long as it didnt hit a wall yet, it will keep looping
			//Lets start at our origin and try a raycast out to the far right of the screen
			while(hit==0){
				//Lets check if we are going left. if the distane to the next x is
				//less than the next y, we are going left.Remember the grid.
				if(sideDistX < sideDistY){
					//first we increment the side distance by how far apart the x's are
					sideDistX += deltaDistX;
					//Next we step ourselves into the next square on the grid
					mapX+=stepX;
					//We are going left so the side is 0
					side = 0;
				//If we arent going left then I suppose theres a good chance
				//we are going right. if so(yes we will be) then we go here instead
				}else{
					//Increment the side distance by the delta as we would as a left, but as
					//a right in this case
					sideDistY += deltaDistY;
					//..step into the next square..
					mapY +=stepY;
					//and set the side to 1 because its going right
					side = 1;
				}
				//Now we check if a wall was hit. If the new square we ended up in is blocked
				// ie its a wall, then we set hit to 1 to end the loop. We found our wall !!
				if(map.getTile(mapX, mapY).isBlocked()){
					hit = 1;
				}
			}
			//Now we need to calculate how far away the wall is. This is the 
			//absolute value of the number of square we crossed
			if(side == 0){
				wallDist = Math.abs((mapX-rayPosX+(1-stepX)/2)/rayDirX);
			}else{
				wallDist = Math.abs((mapY-rayPosY+(1-stepY)/2)/rayDirY);
			}
			//Calculate the height of the line
			int lineHeight = Math.abs((int)(renderer.getMyHeight()/wallDist));
			//Calculate where on the screen the wall begins and ends at
			int renderStart = -lineHeight / 2 + renderer.getMyHeight() / 2;
			int renderEnd = lineHeight / 2 + renderer.getMyHeight() / 2;
			if(renderStart<0){
				renderStart=0;
			}else if(renderStart>renderer.getMyHeight()-1){
				renderStart = renderer.getMyHeight()-1;
			}
			if(renderEnd<0){
				renderStart=0;
			}else if(renderEnd>renderer.getMyHeight()-1){
				renderEnd = renderer.getMyHeight()-1;
			}
			//A reference to the square on the grid we hit
			Tile tile = map.getTile(mapX, mapY);
			//This is where on the wall we struck with out ray. Think of this as the X coord
			//on the texture of the wall
			double wallX;
			//Calculate where the ray hit the wall
			//This is calculated by finding how far we went times the direction we sent
			if(side == 1){
				wallX = rayPosX + ((mapY-rayPosY + (1-stepY)/2)/rayDirY )*rayDirX;
			}else{
				wallX = rayPosY + ((mapX - rayPosX + (1 - stepX) / 2) / rayDirX) * rayDirY;
			}
			//Now  we make it equal to what it is minus the floor.
			wallX -= Math.floor(wallX);
			//Get the texture we wish to place on this wall.
			BufferedImage texture = tile.getTexture();
			//Get the X pos of the the "strip" of the texture we want to use.
			//Think of wallX as a percentage. if it is 0.3, we want the x coord of the texture
			//that is 30 percent of the way across.
			int textureX = (int)(wallX*texture.getWidth());
			//Sets the textureX to the opposite side if the wall is on a certain side
			//but the ray was sent the other way. Think of it as sending rays to the left
			//and we hit the back wall and the left wall. We need to adjust the way the texture
			//is displayed because even though the texture is going the same direction on those walls
			//the way we hit them is different, so we must calculate accordingly
			if((side ==0 && rayDirX>0)||(side == 1 && rayDirY<0)){
				textureX = texture.getWidth() - textureX -1;
			}
			//This is where we FINALLY draw the wall. Lets go over how this works 
			//First calculate the x position of the texture
			int texX = (int)(wallX * (texture.getWidth()));
			//loop through all the y positions on the screen where the wall exists
			for(int y = renderStart; y < renderEnd; y++)
		    {
				//this is a safety measure to make sure we draw the right y on the texture
				int d = y * 256 - renderer.getMyHeight() * 128 + lineHeight * 128;
				//calculate the y position on the texture
				int texY = ((d * texture.getHeight()) / lineHeight) / 256;
				if(texY<0){
					texY = 0;
				}
				//Set the pixel on the scene to the color of the texture
				//Set rgb is faster here because we if we used the pixels themselves we would have to constantly assign the
				//arrayand remake it because we could change the texture we are using many many times
				scenePixels[(scene.getWidth()*y)+screenX] = texture.getRGB(texX, texY);
		    }
			//Add the wall dist for this stripe into the zBuffer
			zBuffer.add(wallDist);
			//******************************
			//******THE FLOOR AND CEILING***
			//******************************
			//Where the floor meets with the wall
			double floorXWall = 0;
        	double floorYWall = 0;
        	//We must calculate where the floor meets the wall based on which way it is facine.
            if(side == 0 && rayDirX > 0)
            {
              floorXWall = mapX;
              floorYWall = mapY + wallX;
            }
            else if(side == 0 && rayDirX < 0)
            {
              floorXWall = mapX + 1.0;
              floorYWall = mapY + wallX;
            }
            else if(side == 1 && rayDirY > 0)
            {
              floorXWall = mapX + wallX;
              floorYWall = mapY;
            }
            else
            {
              floorXWall = mapX + wallX;
              floorYWall = mapY + 1.0;
            } 
            //The distance the wall is to the player
            double distPlayer, currentDist;  
            //
            distPlayer = 0.0;
            //This is the loop where we will draw the floor and the ceiling
            for(int y = renderEnd +1; y < renderer.getMyHeight(); y++)
            {
            	//This is the distance the section of floor and ceiling is away from the player
            	currentDist = renderer.getMyHeight() / (2.0 * y - renderer.getMyHeight()); 
            	//The weight is then calculated based on how far we are from the position and the wall
				double weight = (currentDist - distPlayer) / (wallDist - distPlayer);
				//This is exactly where we on the floor and ground we are setting
				double currentX = weight * floorXWall + (1.0 - weight) * rayPosX;
				double currentY = weight * floorYWall + (1.0 - weight) * rayPosY;
				//The x and y positions on the textures
				int floorTexX, floorTexY;
				int ceilingTexX, ceilingTexY;
				//Calculate where on the textures we need to draw from
				floorTexX = (int) ((currentX * fWidth) % fWidth);
				floorTexY = (int) ((currentY * fHeight) % fHeight);
				//Set the pixel of the scene for the floor
				scenePixels[(scene.getWidth() * (y-1)) + screenX] = floorTexture[(fWidth * floorTexY) + floorTexX];
				//repeat for the ceiling
				ceilingTexX = (int) ((currentX * cWidth) % cWidth);
				ceilingTexY = (int) ((currentY * cHeight) % cHeight);
				//remember, the ceiling starts at the top of the screen and goes down
				scenePixels[((scene.getWidth() * (scene.getHeight() - y))) + screenX] = ceilingTexture[(cWidth * ceilingTexY) + ceilingTexX];
				//This is for custom parts of the ceiling or ground based on if the tile has one set. This kills framerate
				/*if(map.getTile((int)currentX, (int)currentY).getCeilingTexture()!=null){
					scenePixels[((scene.getWidth()*(scene.getHeight()-y)))+screenX] = map.getTile((int)currentX, (int)currentY).getCeilingTexture().getRGB(ceilingTexX, ceilingTexY);
				}*/
        	}
		}
		//SPRITES
		ArrayList<Double> distances = new ArrayList<Double>();
		for(int x = 0;x<map.getNumberOfEntities();x++){
			double d = ((map.getPlayer().getxPos() - map.getEntity(x).getxPos()) * (map.getPlayer().getxPos() - map.getEntity(x).getxPos()) 
					+ (map.getPlayer().getyPos() - map.getEntity(x).getyPos()) * (map.getPlayer().getyPos() - map.getEntity(x).getyPos()));
			distances.add(d);
		}
		//sort?
		int[] spritePixels = null;
		byte[] spriteB = null;
		 for(int x = 0; x < map.getNumberOfEntities(); x++)
		    {
			 
			 spriteB = ((DataBufferByte)map.getEntity(x).getSprite().getCurrentSprite().getRaster().getDataBuffer()).getData();
			 spritePixels = Utility.convertByteArrayToInt(spriteB);
		      //translate sprite position to relative to camera
		      double spriteX = map.getEntity(x).getxPos() - map.getPlayer().getxPos();
		      double spriteY = map.getEntity(x).getyPos() - map.getPlayer().getyPos();
		     
		      double invDet = 1.0 / (map.getPlayer().getPlaneX() * map.getPlayer().getyDir() - map.getPlayer().getxDir() * map.getPlayer().getPlaneY());
		      
		      double transformX = invDet * (map.getPlayer().getyDir() * spriteX - map.getPlayer().getxDir() * spriteY);
		      double transformY = invDet * (-map.getPlayer().getPlaneY() * spriteX + map.getPlayer().getPlaneX() * spriteY);       
		            
		      int spriteScreenX = (int)((renderer.getMyWidth() / 2) * (1 + transformX / transformY));
		      
		      //ADD NUMBERS TO THE END OF DRAWSTARTS AND ENDS TO MOVE IT AROUND
		      //DIVIDE NUMBERS TO END OF SPRITEHEIGHT AND SPRITEWIDTH TO CHANGE SIZE
		      //calculate height of the sprite on screen
		      int vMove = (int) (0 / transformY);
		      int vDiv = 2;
		      int uDiv =2;
		      int spriteHeight = Math.abs(((int)(renderer.getMyHeight() / (transformY)))) /vDiv; 
		      //calculate lowest and highest pixel to fill in current stripe
		      int drawStartY = -spriteHeight / 2 + renderer.getMyHeight() / 2 + vMove;
		      if(drawStartY < 0) drawStartY = 0;
		      int drawEndY = spriteHeight / 2 + renderer.getMyHeight() / 2 + vMove;
		      if(drawEndY >= renderer.getMyHeight()) drawEndY = renderer.getMyHeight() - 1;
		      
		      //calculate width of the sprite
		      int spriteWidth = Math.abs(( (int) (renderer.getMyHeight() / (transformY))))/uDiv;
		      int drawStartX = -spriteWidth / 2 + spriteScreenX;
		      if(drawStartX < 0) drawStartX = 0;
		      int drawEndX = spriteWidth / 2 + spriteScreenX;
		      if(drawEndX >= renderer.getMyWidth()) drawEndX = renderer.getMyWidth() - 1;
		      
		      //loop through every vertical stripe of the sprite on screen
		      for(int stripe = drawStartX; stripe < drawEndX; stripe++)
		      {
		        int texX = (int)(256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * map.getEntity(x).getSprite().getCurrentSprite().getWidth() / spriteWidth) / 256;
		        //the conditions in the if are:
		        //1) it's in front of camera plane so you don't see things behind you
		        //2) it's on the screen (left)
		        //3) it's on the screen (right)
		        //4) ZBuffer, with perpendicular distance
		        if(transformY > 0 && stripe > 0 && stripe < renderer.getMyWidth() && transformY < zBuffer.get(stripe)) {
			        for(int y = drawStartY; y < drawEndY; y++) 
			        {
			          int d = (y-vMove) * 256 - renderer.getMyHeight() * 128 + spriteHeight * 128; 
			          int texY = ((d * map.getEntity(x).getSprite().getCurrentSprite().getHeight()) / spriteHeight) / 256;
			          
			          //AVERAGE THE OLD COLOR AND THE NEW COLOR TO MAKE TRANSPARENT
			          //CAN ALSO BE WEIGHTED
			          if(spritePixels[(map.getEntity(x).getSprite().getCurrentSprite().getWidth() * texY )+texX]!=-16777216){
			        	  scenePixels[(scene.getWidth()*(y))+stripe] = spritePixels[map.getEntity(x).getSprite().getCurrentSprite().getWidth()*texY+texX];
			          }
			        }
		        }
		      }
		    }
		renderer.renderImage(scene, 0, 0);
		
	}
}
