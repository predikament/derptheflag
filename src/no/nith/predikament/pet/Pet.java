package no.nith.predikament.pet;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Vector2;

public class Pet extends PhysicsEntity 
{
	@SuppressWarnings("unused")
	private Level level;
	private PhysicsEntity target;
	private int ySpriteIndex;
	private Vector2 direction;
	private int frame;
	
	public Pet(Level level, PhysicsEntity target, int ySpriteIndex)
	{
		this.level = level;
		this.target = target;
		this.ySpriteIndex = ySpriteIndex;
		
		direction = new Vector2();
		frame = 0;
	}

	public void update(double dt) 
	{
		
	}

	public void render(Bitmap screen) 
	{
		boolean flip = false;
		
		if (direction.x == 0) frame = 0;
		else
		{
			frame = 1;
			
			if (direction.x == -1) flip = true;
		}

		screen.draw(Art.instance.pets[frame][ySpriteIndex], (int) getPosition().x, (int) getPosition().y, flip);
	}

	public PhysicsEntity getTarget() 
	{
		return target;
	}

	public void setTarget(PhysicsEntity target) 
	{
		this.target = target;
	}
}
