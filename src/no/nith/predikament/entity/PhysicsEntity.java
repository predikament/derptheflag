package no.nith.predikament.entity;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import no.nith.predikament.util.Vector2;

public abstract class PhysicsEntity extends Entity 
{
	private Vector2 velocity;
	private Rectangle hitbox;
	
	public PhysicsEntity()
	{
		this(new Vector2(0, 0), 1, 1);
	}
	
	public PhysicsEntity(Vector2 position, int width, int height)
	{
		this(position.x, position.y, width, height);
	}
	
	public PhysicsEntity(int x, int y, int width, int height)
	{
		this(x, y, (double) width, (double) height);
	}
	
	public PhysicsEntity(double x, double y, double width, double height)
	{
		super(new Vector2(x, y));
		
		// Create hitbox
		Point p = new Point();
		p.x = (int) getPosition().x;
		p.y = (int) getPosition().y;
		
		Dimension d = new Dimension();
		d.setSize(width, height);
		
		this.hitbox = new Rectangle(p, d);
		
		// Set stuff
		setVelocity(Vector2.zero());
	}
	
	public void setPosition(Vector2 position)
	{
		// Set position
		super.setPosition(position);
		
		// Update hitbox
		Point p = new Point();
		Dimension d = new Dimension();
		if (getHitbox() == null) d.setSize(1, 1);
		else d.setSize(getHitbox().width, getHitbox().height);
		
		p.x = (int) getPosition().x;
		p.y = (int) getPosition().y;
		
		setHitbox(new Rectangle(p, d));
	}
	
	public void update(double dt)
	{
		setPosition(Vector2.add(getPosition(), new Vector2(getVelocity().x * dt, getVelocity().y * dt)));
		setVelocity(new Vector2(getVelocity().x * 0.90 * dt, getVelocity().y * 0.90 * dt));
		
		System.out.println("Current vel.x: " + getVelocity().x + "\nCurrent vel.y: " + getVelocity().y);
		// Clip velocity
		if (getVelocity().x < 10 && getVelocity().x > -10) setVelocity(new Vector2(0, getVelocity().y));
		/*if (-getVelocity().x <= 0.01) setVelocity(new Vector2(0, getVelocity().y));
		else if (-getVelocity().y <= 0.01) setVelocity(new Vector2(getVelocity().x, 0));*/
	}
	
	public Vector2 getVelocity() 
	{
		return velocity;
	}
	
	public void setVelocity(Vector2 velocity) 
	{
		this.velocity = velocity;
	}
	
	public final Rectangle getHitbox() 
	{
		return hitbox;
	}
	
	public void setHitbox(Rectangle hitbox) 
	{
		this.hitbox = hitbox;  
	}
}
