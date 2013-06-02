package no.nith.predikament.player;

import java.util.Random;

import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.entity.pet.*;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Vector2;

public class Player 
{
	private final Random random = new Random();
	
	private static final double SPEED_INCREMENT = 1;
	@SuppressWarnings("unused")
	private Level level;
	private PhysicsEntity target;
	private Pet pet;

	public Player(Level level, PhysicsEntity target)
	{
		this.level = level;
		this.target = target;
		this.pet = Pet.create(level, target, random.nextInt(Pet.TOTAL_PETS));
	}
	
	public void moveTargetLeft()
	{
		Vector2 vel = target.getVelocity();
		vel.x -= SPEED_INCREMENT;
	}
	
	public void moveTargetRight()
	{
		Vector2 vel = target.getVelocity();
		vel.x += SPEED_INCREMENT;
	}
	
	public boolean hasTarget()
	{
		return target != null;
	}
	
	public boolean hasPet()
	{
		return pet != null;
	}
	
	public final Pet getPet()
	{
		return pet;
	}
	
	public final PhysicsEntity getTarget() 
	{
		return target;
	}
}
