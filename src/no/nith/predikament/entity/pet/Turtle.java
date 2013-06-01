package no.nith.predikament.entity.pet;

import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;

public class Turtle extends Pet 
{
	public Turtle(Level level, PhysicsEntity target) 
	{
		super(level, target, 2);
		
		setFollowRange(100.0);
	}
}
