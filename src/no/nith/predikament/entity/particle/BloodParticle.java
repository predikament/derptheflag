package no.nith.predikament.entity.particle;

import no.nith.predikament.util.Vector2;

public class BloodParticle extends Particle 
{
	public BloodParticle(Vector2 position, Vector2 velocity, double lifetime_ms) 
	{
		super(0, 0, lifetime_ms, position, velocity);
	}
}
