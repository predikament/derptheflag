package no.nith.predikament.entity;

import no.nith.predikament.util.Vector2;

public class Entity 
{
	private Vector2 position;

	public Entity()
	{
		position = new Vector2();
	}
	
	public Entity(Vector2 position)
	{
		this.position = position;
	}
	
	public Vector2 getPosition() 
	{
		return position;
	}

	public void setPosition(Vector2 position) 
	{
		this.position = position;
	}
}
