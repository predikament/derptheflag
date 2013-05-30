package no.nith.predikament.entity;

import java.awt.Color;
import no.nith.predikament.Bitmap;
import no.nith.predikament.util.Vector2;

public class Bullet extends PhysicsEntity 
{
	private static final Color color = Color.black;
	private static final Vector2 SPEED = new Vector2(500, 500);
	
	public Bullet(Vector2 position, Vector2 velocity)
	{
		this.setPosition(position);
		this.setVelocity(velocity);
	}
	
	public void render(Bitmap screen)
	{
		screen.setPixel(getPosition().x, getPosition().y, color.getRGB());
	}
	
	public void setVelocity(Vector2 velocity)
	{
		velocity.x *= SPEED.x;
		velocity.y *= SPEED.y;
		
		super.setVelocity(velocity);
	}
}
