package no.nith.predikament.player;

import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;
import no.nith.predikament.pet.Dog;
import no.nith.predikament.pet.Pet;
import no.nith.predikament.util.Vector2;

public class Player 
{
	private static final double SPEED_INCREMENT = 1;
	private static final Vector2 JUMP_VECTOR = new Vector2(0, -100);
	
	private Level level;
	private PhysicsEntity target;
	private Pet pet;
	
	public Player(Level level, PhysicsEntity target)
	{
		this.level = level;
		this.target = target;
		this.pet = new Dog(level, target);
	}
	
	public void moveLeft()
	{
		Vector2 vel = target.getVelocity();
		vel.x -= SPEED_INCREMENT;
	}
	
	public void moveRight()
	{
		Vector2 vel = target.getVelocity();
		vel.x += SPEED_INCREMENT;
	}
	
	public void jump()
	{
		if (target.getHitbox().getMaxY() == level.getHeight())
		{
			target.setVelocity(Vector2.add(target.getVelocity(), JUMP_VECTOR));
		}
	}
	
	public void render(Bitmap screen)
	{
		target.render(screen);
	}
	
	public void update(double dt)
	{
		target.update(dt);
	}
	
	public boolean hasPet()
	{
		return pet != null;
	}
	
	public Pet getPet()
	{
		return pet;
	}
	
	public PhysicsEntity getTarget() 
	{
		return target;
	}
}
