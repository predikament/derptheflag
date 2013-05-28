package no.nith.predikament.entity;

import no.nith.predikament.Bitmap;
import no.nith.predikament.util.Vector2;

public abstract class Entity 
{
	private boolean removed;
	private Vector2 position;

	public Entity()
	{
		this(new Vector2());
	}
	
	public Entity(Vector2 position)
	{
		removed = false;
		
		setPosition(position);
	}
	
	public void remove() 
	{
		removed = true;
	}
	
	public boolean wasRemoved() 
	{
		return removed;
	}

	public Vector2 getPosition() 
	{
		return position;
	}

	public void setPosition(Vector2 position) 
	{
		this.position = position;
	}
	
	public abstract void update(double dt);
	public abstract void render(Bitmap screen);
}
