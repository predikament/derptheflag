package no.nith.predikament.entity.unit;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.entity.weapon.*;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Stopwatch;
import no.nith.predikament.util.Vector2;

public abstract class Unit extends PhysicsEntity
{
	private final Level level;
	private int ySpriteIndex;
	private int frame;
	private Vector2 direction;
	private static final Vector2 JUMP_VECTOR = new Vector2(0, -200);
	private static final Vector2 VELOCITY_MAX =  new Vector2(100, 400);
	private static final Vector2 BULLET_OFFSET = new Vector2(8.0f, 0);
	public static final int TOTAL_UNITS = 5;
	private boolean running;
	private boolean jumping;
	private boolean shooting;
	private Vector2 lookingAtPosition;
	private Stopwatch walkTimer, shootTimer;
	
	public Unit(Level level, int ySpriteIndex)
	{
		super(0, 0, 16, 16);
		
		this.level = level;
		this.ySpriteIndex = ySpriteIndex;
		this.direction = new Vector2();
		
		frame = 0;
		running = false;
		jumping = false;
		shooting = false;
		
		lookingAtPosition = new Vector2(0, 0);
		
		walkTimer = new Stopwatch();
		shootTimer = new Stopwatch();
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
	
	public void update(double dt)
	{
		super.update(dt);
		
		if (getVelocity().x != 0 && !running) setRunning(true);
		else if (getVelocity().x == 0) setRunning(false);
		
		if (jumping && getVelocity().y == 0) jumping = false;
		
		if (shooting && shootTimer.getElapsedTime() >= 200)
		{
			setShooting(false);
		}
		
		if (running && walkTimer.getElapsedTime() >= 600) walkTimer.reset();
	}
	
	public void render(Bitmap screen) 
	{
		boolean flip = getVelocity().x < 0;
		
		frame = 0;
		
		if (shooting)
		{
			flip = direction.x == -1;
			frame = 4;
		}
		else if (running)
		{
			if (walkTimer.getElapsedTime() < 200) frame = 1;
			else if (walkTimer.getElapsedTime() < 400) frame = 2; 
			else frame = 3;
		}
		
		screen.draw(Art.instance.characters[frame][ySpriteIndex], (int) getPosition().x, (int) getPosition().y, flip);
	}
	
	public synchronized void shoot()
	{
		setShooting(true);
		
		Vector2 mousePos = new Vector2(lookingAtPosition);
		Vector2 playerPos = new Vector2(getHitbox().getCenterX(), getHitbox().getCenterY());
		
		if (direction.x == -1) playerPos.x -= BULLET_OFFSET.x;
		else if (direction.x == 1) playerPos.x += BULLET_OFFSET.x;

		Vector2 difference = new Vector2(mousePos.x - playerPos.x, mousePos.y - playerPos.y);
		Vector2 angle = Vector2.radianToVector(Math.toRadians(Math.atan2(difference.y, difference.x) * 180 / Math.PI));
		
		level.addEntity(new Bullet(playerPos, angle));
	}
	
	public void lookAt(final Vector2 position)
	{
		lookingAtPosition = new Vector2(position);
		
		if (lookingAtPosition.x <= getHitbox().getCenterX()) direction.x = -1;
		else if (lookingAtPosition.x > getHitbox().getCenterX()) direction.x = 1;
		else direction.x = 0;
		
		if (lookingAtPosition.y <= getHitbox().getCenterY()) direction.y = -1;
		else if (lookingAtPosition.y > getHitbox().getCenterY()) direction.y = 1;
		else direction.y = 0;
	}
	
	public void jump()
	{
		if (!jumping)
		{
			setVelocity(Vector2.add(getVelocity(), JUMP_VECTOR));
			
			jumping = true;
		}
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
			position.y = level.getHeight() - getHitbox().height;
			setVelocity(new Vector2(getVelocity().x, 0));
		}
		
		// X-axis
		if (position.x < 0)
		{
			position.x = 0;
			setVelocity(new Vector2(0, getVelocity().y));
		}
		else if (level != null && position.x > level.getWidth() - getHitbox().width)
		{
			position.x = level.getWidth() - getHitbox().width;
			setVelocity(new Vector2(0, getVelocity().y));
		}
		
		super.setPosition(position);
	}
	
	public void setVelocity(Vector2 velocity) 
	{
		if (velocity.x > VELOCITY_MAX.x) velocity.x = VELOCITY_MAX.x;
		if (velocity.y > VELOCITY_MAX.y) velocity.y = VELOCITY_MAX.y;
		if (-velocity.x > VELOCITY_MAX.x) velocity.x = -VELOCITY_MAX.x;
		if (-velocity.y > VELOCITY_MAX.y) velocity.y = -VELOCITY_MAX.y;
		
		super.setVelocity(velocity);
	}

	public Level getLevel() 
	{
		return level;
	}
	
	private void setRunning(final boolean running)
	{
		this.running = running;
		
		if (running) walkTimer.start();
		else 
		{
			walkTimer.stop();
			walkTimer.reset();
		}
	}
	
	private void setShooting(final boolean shooting)
	{
		this.shooting = shooting;
		
		if (shooting) shootTimer.start();
		else 
		{
			shootTimer.stop();
			shootTimer.reset();
		}
	}
}
