package no.nith.predikament.util;

import java.awt.Point;

public class Vector2 
{
	public double x, y;
	
	public Vector2()
	{
		this(0, 0);
	}
	
	public Vector2(Vector2 u)
	{
		this(u.x, u.y);
	}
	
	public Vector2(Point point)
	{
		this(point.getX(), point.getY());
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
		return new Vector2(u.x + v.x, u.y + v.y);
	}
	
	public void add(final Vector2 u)
	{
		add(this, u);
	}
	
	public static Vector2 multiply(Vector2 u, double scalar)
	{
		return new Vector2(u.x * scalar, u.y * scalar);
	}
	
	public static double distanceBetween(final Vector2 u, final Vector2 v)
	{
		Vector2 result = new Vector2();
		
		result.x = u.x - v.x;
		result.y = u.y - v.y;
		
		return Math.sqrt(result.x * result.x + result.y * result.y);
	}
	
	public double distanceTo(final Vector2 u)
	{
		return distanceBetween(this, u);
	}
	
	public static double angleBetween(final Vector2 u, final Vector2 v)
	{
		return Math.acos(u.normalized().dot(v.normalized()));
	}
	
	public double angleTo(final Vector2 u)
	{
		return angleBetween(this, u);
	}
	
	
	public static double cross(final Vector2 u, final Vector2 v)
	{
		return (u.x * v.y) - (u.y * v.x);
	}
	
	public double cross(final Vector2 u) 
	{
		return cross(this, u);
	}
	
	public static double dot(final Vector2 u, final Vector2 v)
	{
		return (u.x * v.x) + (u.y * v.y);
	}
	
	public double dot(final Vector2 u)
	{
		return dot(this, u);
	}
	
	public static double length(final Vector2 u)
	{
		return Math.sqrt(u.x * u.x + u.y * u.y);
	}
	
	public double length()
	{
		return length(this);
	}
	
	public static Vector2 normalized(final Vector2 u)
	{
		double length = length(u);
		
		if (length != 0) return new Vector2(u.x / length, u.y / length);
		
		return u;
	}
	
	public Vector2 normalized()
	{
		return normalized(this);
	}
	
	public static Vector2 zero()
	{
		return new Vector2();
	}

	public static Vector2 radianToVector(double rad)
	{
		return new Vector2(Math.cos(rad), Math.sin(rad));
	}
}
