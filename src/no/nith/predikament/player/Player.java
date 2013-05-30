package no.nith.predikament.player;

import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.Bullet;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.entity.unit.Unit;
import no.nith.predikament.level.Level;
import no.nith.predikament.pet.Dog;
import no.nith.predikament.pet.Pet;
import no.nith.predikament.util.Vector2;

public class Player 
{
	private static final double SPEED_INCREMENT = 1;
	private static final Vector2 JUMP_VECTOR = new Vector2(0, -300);
	
	private Level level;
	private PhysicsEntity target;
	private Pet pet;
	private boolean jumping;
	
	public Player(Level level, PhysicsEntity target)
	{
		this.level = level;
		this.target = target;
		this.pet = new Dog(level, target);
		
		jumping = false;
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
		if (!jumping)
		{
			target.setVelocity(Vector2.add(target.getVelocity(), JUMP_VECTOR));
			
			jumping = true;
		}
	}
	
	public void shoot(final Vector2 target)
	{
		Vector2 mousePos = new Vector2(target);
		Vector2 playerPos = new Vector2(getTarget().getPosition());
		
		playerPos.x += getTarget().getHitbox().getWidth() / 2.0;
		playerPos.y += getTarget().getHitbox().getHeight() / 2.0;
		
		Vector2 difference = new Vector2(mousePos.x - playerPos.x, mousePos.y - playerPos.y);
		Vector2 angle = Vector2.radianToVector(Math.toRadians(Math.atan2(difference.y, difference.x) * 180 / Math.PI));
		
		level.addEntity(new Bullet(playerPos, angle));
		Unit unit = (Unit) getTarget();
		unit.setShooting(true);
	}
	
	public void render(Bitmap screen)
	{
		target.render(screen);
	}
	
	public void update(double dt)
	{
		target.update(dt);
		
		if (jumping && target.getVelocity().y == 0)
		{
			jumping = false;
		}
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
