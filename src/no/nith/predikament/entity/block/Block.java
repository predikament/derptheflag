package no.nith.predikament.entity.block;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.util.Vector2;

public class Block extends PhysicsEntity 
{
	public static final int WIDTH	= 16;
	public static final int HEIGHT	= 16;
	
	private int xSpriteIndex, ySpriteIndex;
	private boolean collidable;
	
	public Block(Vector2 position, int xSpriteIndex, int ySpriteIndex, boolean collidable)
	{
		super(position, 16, 16);
		
		this.xSpriteIndex = xSpriteIndex;
		this.ySpriteIndex = ySpriteIndex;
		this.collidable = collidable;
	}
	
	public void update(double dt) 
	{
		// Nada
	}

	public void render(Bitmap screen) 
	{
		screen.draw(Art.instance.blocks[xSpriteIndex][ySpriteIndex], getPosition().x, getPosition().y);
	}

	public final boolean isCollidable()
	{
		return collidable;
	}
}
