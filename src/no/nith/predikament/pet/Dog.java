package no.nith.predikament.pet;

import java.util.Random;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Vector2;

public class Dog extends Pet 
{
	private Random random = new Random();
	
	public Dog(Level level, PhysicsEntity target) 
	{
		super(level, target, 0);
		
		setFollowRange(random.nextInt(100) + 10);
	}
	
	public void jump()
	{
		Vector2 vel = new Vector2();
		
		vel = Vector2.add(getVelocity(), new Vector2(0, -200));
		setVelocity(vel);
	}
		
	public void render(Bitmap screen)
	{
		super.render(screen);
	}
}
