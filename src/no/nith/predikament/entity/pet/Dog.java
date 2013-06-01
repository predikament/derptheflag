package no.nith.predikament.entity.pet;

import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;

public class Dog extends Pet 
{
	public Dog(Level level, PhysicsEntity target) 
	{
		super(level, target, 0);
		
		setFollowRange(50.0);
	}
}
