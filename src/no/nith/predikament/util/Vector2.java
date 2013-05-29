package no.nith.predikament.util;

public class Vector2 
{
	public double x, y;
	
	public Vector2()
	{
		this(0, 0);
	}
	
	public Vector2(final double x, final double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		return String.format("X = %f, Y = %f", x, y); 
	}
	
	public static Vector2 add(Vector2 u, Vector2 v)
	{
		Vector2 result = new Vector2();
		
		result.x = u.x + v.x;
		result.y = u.y + v.y;
		
		return result;
	}
	
	public static double distanceBetween(Vector2 u, Vector2 v)
	{
		Vector2 result = new Vector2();
		
		result.x = u.x - v.x;
		result.y = u.y - v.y;
		
		return Math.sqrt(result.x * result.x + result.y * result.y);
	}
	
	public static Vector2 zero()
	{
		return new Vector2();
	}
}
