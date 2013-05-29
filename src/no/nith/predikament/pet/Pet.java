package no.nith.predikament.pet;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Vector2;

public class Pet extends PhysicsEntity 
{
	private Level level;
	private PhysicsEntity target;
	private int ySpriteIndex;
	private double follow_range;
	private Vector2 direction;
	private int frame;
	
	public Pet(Level level, PhysicsEntity target, int ySpriteIndex)
	{
		super(0, 0, 16, 16);
		
		this.level = level;
		this.target = target;
		this.ySpriteIndex = ySpriteIndex;
		
		setFollowRange(5.0);
		
		direction = new Vector2();
		frame = 0;
	}

	public void update(double dt) 
	{
		if (Vector2.distanceBetween(getPosition(), getTarget().getPosition()) > getFollowRange())
		{
			Vector2 vel = getVelocity();
			
			if (getPosition().x < getTarget().getPosition().x) vel.x += 0.5;
			else if (getPosition().x > getTarget().getPosition().x) vel.x -= 0.5;
		}
		
		super.update(dt);
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

	public void setPosition(Vector2 position)
	{
		// Check bounds of new position
		// Y-axis
		if (position.y < 0)
		{
			position.y = 0;
		}
		else if (level != null && position.y > level.getHeight() - getHitbox().height)
		{
			position.y = level.getHeight() - getHitbox().height;
			setVelocity(new Vector2(getVelocity().x, 0));
		}
		
		// X-axis
		if (position.x < 0)
		{
			position.x = 0;
			setVelocity(new Vector2(0, getVelocity().y));
		}
		else if (level != null && position.x > level.getWidth() - getHitbox().width)
		{
			position.x = level.getWidth() - getHitbox().width;
			setVelocity(new Vector2(0, getVelocity().y));
		}
		
		super.setPosition(position);
	}
	
	public void setVelocity(Vector2 velocity)
	{
		if (direction != null)
		{
			if (velocity.x < 0) direction.x = -1;
			else if (velocity.x > 0) direction.x = 1;
			else direction.x = 0;
		}
		
		super.setVelocity(velocity);
	}
	
	public PhysicsEntity getTarget() 
	{
		return target;
	}

	public void setTarget(PhysicsEntity target) 
	{
		this.target = target;
	}

	public final double getFollowRange()
	{
		return follow_range;
	}

	public void setFollowRange(double follow_range) 
	{
		this.follow_range = follow_range;
	}
}
