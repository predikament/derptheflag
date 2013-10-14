package no.nith.predikament.entity;

import no.nith.predikament.util.Vector2;

public class Tile extends Entity 
{
	public static enum Type
	{
		FLAT,
		MEDIUM_LOW,
		MEDIUM,
		MEDIUM_HIGH,
		HIGH
	}
	private Type type;
	
	public Tile(Vector2 position, Type type)
	{
		super(position);
	}

	public Type getType() 
	{
		return type;
	}

	public void setType(Type type) 
	{
		this.type = type;
	}
}
