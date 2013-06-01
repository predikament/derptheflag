package no.nith.predikament.entity.pet;

import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;

public class Cat extends Pet 
{
	public Cat(Level level, PhysicsEntity target) 
	{
		super(level, target, 1);
		
		setFollowRange(200.0);
	}
}
