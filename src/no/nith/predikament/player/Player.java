package no.nith.predikament.player;

import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.util.Vector2;

public class Player 
{
	private static final Vector2 DECELERATION_FACTOR = new Vector2(1, 1);
	public static final Vector2 JUMP_VECTOR = new Vector2(0, 300);
	public static final double VELOCITY_MAX = 200;
	private static final double SPEED = 20;
	private PhysicsEntity target;
	private boolean jumping;
	
	public Player(PhysicsEntity target)
	{
		this.target = target;
		
		jumping = false;
	}
	
	public void moveLeft()
	{
		Vector2 vel = target.getVelocity();
		vel.x -= SPEED;
	}
	
	public void moveRight()
	{
		Vector2 vel = target.getVelocity();
		vel.x += SPEED;
	}
	
	public void jump()
	{
		if (!jumping)
		{
			
		}
	}
	
	public void render(Bitmap screen)
	{
		target.render(screen);
	}
	
	public void update(double dt)
	{
		// Vector2 vel = target.getVelocity();
		// vel.x *= DECELERATION_FACTOR.x;
		// vel.y *= DECELERATION_FACTOR.y;
		
		target.update(dt);
	}
	
	public PhysicsEntity getTarget() 
	{
		return target;
	}
}
