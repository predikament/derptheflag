package no.nith.predikament.entity.weapon;

import java.awt.Color;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.util.Vector2;

public class Lazer extends Weapon 
{
	private static final Color color = Color.red;
	private static final double SPEED = 2000;
	private static final int length = 5;
	
	public Lazer(Vector2 position, Vector2 velocity)
	{
		super(position.x, position.y, length, length);
		this.setPosition(position);
		this.setVelocity(velocity);
	}
	
	public void render(Bitmap screen)
	{
		Vector2 from = new Vector2(getPosition());
		Vector2 to = Vector2.add(getPosition(), Vector2.multiply(getVelocity().normalized(), length));
		Vector2 midpoint = new Vector2(((from.x + to.x) / 2.0) - (Art.instance.lights[0][0].w / 2.0), ((from.y + to.y) / 2.0) - (Art.instance.lights[0][0].h / 2.0));
		
		screen.drawLine(from.x, from.y, to.x, to.y, color.getRGB());
		screen.multiply(Art.instance.lights[0][0], midpoint.x, midpoint.y);
	}
	
	public void setVelocity(Vector2 velocity)
	{
		velocity.x *= SPEED;
		velocity.y *= SPEED;
		
		super.setVelocity(velocity);
	}
}
