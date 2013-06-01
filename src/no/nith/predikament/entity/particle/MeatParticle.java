package no.nith.predikament.entity.particle;

import java.util.Random;

import no.nith.predikament.util.Vector2;

public class MeatParticle extends Particle 
{
	private final static int TOTAL = 4;
	private final static Random random = new Random();
	
	public MeatParticle(Vector2 position, Vector2 velocity, double lifetime_ms) 
	{
		super(random.nextInt(TOTAL), 1, lifetime_ms, position, velocity);
	}
}
