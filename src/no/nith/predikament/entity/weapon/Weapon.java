package no.nith.predikament.entity.weapon;

import no.nith.predikament.entity.PhysicsEntity;

public abstract class Weapon extends PhysicsEntity 
{
	public Weapon(double x, double y, int width, int height)
	{
		this((int) x, (int) y, width, height);
	}
	
	public Weapon(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}
}
