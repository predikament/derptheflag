package no.nith.predikament.entity.weapon;

import java.awt.Color;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.util.Vector2;

public class Bullet extends Weapon 
{
	private static final Color color = Color.black;
	private static final double SPEED = 50;
	
	public Bullet(Vector2 position, Vector2 velocity)
	{
		super(position.x, position.y, 2, 2);
		this.setPosition(position);
		this.setVelocity(velocity);
	}
	
	public void render(Bitmap screen)
	{
		screen.setPixel(getPosition().x, getPosition().y, Color.yellow.getRGB());
		screen.drawCircle(getPosition().x, getPosition().y, 1, color.getRGB());
		screen.multiply(Art.instance.lights[0][0], getPosition().x - 8, getPosition().y - 8);
	}
	
	public void setVelocity(Vector2 velocity)
	{
		velocity.x *= SPEED;
		velocity.y *= SPEED;
		
		super.setVelocity(velocity);
	}
}
