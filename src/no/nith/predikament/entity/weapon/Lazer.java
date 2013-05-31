package no.nith.predikament.entity.weapon;

import java.awt.Color;
import no.nith.predikament.Bitmap;
import no.nith.predikament.util.Vector2;

public class Lazer extends Weapon 
{
	private static final Color color = Color.red;
	private static final Vector2 SPEED = new Vector2(750, 750);
	private int length = 5;
	
	public Lazer(Vector2 position, Vector2 velocity)
	{
		this.setPosition(position);
		this.setVelocity(velocity);
	}
	
	public void render(Bitmap screen)
	{
		screen.drawLine(getPosition().x, getPosition().y, 
				getPosition().x + getVelocity().normalized().x * length, 
				getPosition().y + getVelocity().normalized().y * length, 
				color.getRGB());
	}
	
	public void setVelocity(Vector2 velocity)
	{
		velocity.x *= SPEED.x;
		velocity.y *= SPEED.y;
		
		super.setVelocity(velocity);
	}
}
