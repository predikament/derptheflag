package no.nith.predikament.pet;

import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;

public class Dog extends Pet 
{
	public Dog(Level level, PhysicsEntity target) 
	{
		super(level, target, 0);
		
		setFollowRange(35.0);
	}
}
