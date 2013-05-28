package no.nith.predikament.entity.unit;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Vector2;

public abstract class Unit extends PhysicsEntity
{
	private final Level level;
	private int ySpriteIndex;
	private int frame;
	private Vector2 direction;
	
	public static final int TOTAL_UNITS = 5;
	
	public Unit(Level level, int ySpriteIndex)
	{
		super(0, 0, 16, 16);
		
		this.level = level;
		this.ySpriteIndex = ySpriteIndex;
		this.direction = new Vector2();
		
		frame = 0;
	}
	
	public static Unit create(Level level, int type)
	{
		Unit newUnit = null;
		
		switch(type)
		{
		case 0:
			newUnit = new Soldier(level);
			break;
		case 1:
			newUnit = new Ninja(level);
			break;
		case 2:
			newUnit = new Farmer(level);
			break;
		case 3:
			newUnit = new Redneck(level);
			break;
		case 4:
			newUnit = new Scientist(level);
			break;
		default:
			newUnit = new Soldier(level);
			System.out.println("Unknown type, generating default.");
			break;
		}
		
		return newUnit;
	}
	
	public void render(Bitmap screen) 
	{
		boolean flip = false;
		
		if (direction.x == 0) frame = 0;
		else
		{
			frame = 1;
			
			if (direction.x == -1) flip = true;
		}

		screen.draw(Art.instance.characters[frame][ySpriteIndex], (int) getPosition().x, (int) getPosition().y, flip);
	}

	public void setPosition(Vector2 position)
	{
		// Check bounds of new position
		// Y-axis
		if (position.y < 0)
		{
			position.y = 0;
		}
		else if (level != null && position.y > level.getHeight() - getHitbox().height)
		{
			position.y = level.getHeight() - 16;
		}
		
		// X-axis
		if (position.x < 0)
		{
			position.x = 0;
		}
		else if (level != null && position.x > level.getWidth() - getHitbox().width)
		{
			position.x = level.getWidth() - getHitbox().width;
		}
		
		super.setPosition(position);
	}
	
	public void setVelocity(Vector2 velocity) 
	{
		if (direction != null)
		{
			if (velocity.x > 0) direction.x = 1;
			else if (velocity.x < 0) direction.x = -1;
			else direction.x = 0;
			
			if (velocity.y > 0) direction.y = 1;
			else if (velocity.y < 0) direction.y = -1;
		}
		
		super.setVelocity(velocity);
	}

	public Level getLevel() 
	{
		return level;
	}
}
