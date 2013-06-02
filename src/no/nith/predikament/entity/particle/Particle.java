package no.nith.predikament.entity.particle;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.util.Stopwatch;
import no.nith.predikament.util.Vector2;

public abstract class Particle extends PhysicsEntity
{
	private final int xSpriteIndex;
	private final int ySpriteIndex;
	private final Stopwatch lifetimer;
	private final double lifetime_ms;
	
	public Particle(int xSpriteIndex, int ySpriteIndex, double lifetime_ms, Vector2 position, Vector2 velocity)
	{
		super(position, 16, 16);
		
		setVelocity(velocity);
		
		this.xSpriteIndex = xSpriteIndex;
		this.ySpriteIndex = ySpriteIndex;
		
		this.lifetime_ms = lifetime_ms;
		this.lifetimer = new Stopwatch(true);
	}
	
	public void render(Bitmap screen) 
	{
		screen.draw(Art.instance.particles[xSpriteIndex][ySpriteIndex], (int) getPosition().x, (int) getPosition().y);
	}
	
	public void update(float dt)
	{
		super.update(dt);
		
		if (lifetimer.getElapsedTime() >= lifetime_ms) remove();
	}
}
