package no.nith.predikament.entity.pet;

import java.util.Random;
import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Stopwatch;
import no.nith.predikament.util.Vector2;

public class Pet extends PhysicsEntity 
{
	public static final int TOTAL_PETS = 3;
	private static final Vector2 VELOCITY_MAX =  new Vector2(100, 400);
	private static final Vector2 JUMP_VECTOR = new Vector2(0, -300);
	private final Random random = new Random();
	private Level level;
	private PhysicsEntity owner;
	private int ySpriteIndex;
	private double follow_range;
	protected Vector2 direction;
	protected int frame;
	private boolean running;
	private Stopwatch walkTimer;
	private Stopwatch actionTimer;
	private long nextActionTime;
	
	public Pet(Level level, PhysicsEntity owner, int ySpriteIndex)
	{
		super(0, 0, 16, 16);
		
		this.level = level;
		this.owner = owner;
		this.ySpriteIndex = ySpriteIndex;
		
		setFollowRange(10.0);
		
		direction = new Vector2();
		running = false;
		frame = 0;
		
		walkTimer = new Stopwatch();
		actionTimer = new Stopwatch(true);
		nextActionTime = random.nextInt(2500) + 2500;
	}

	public static Pet create(Level level, PhysicsEntity owner, int type)
	{
		Pet pet = null;
		
		switch(type)
		{
		case 0:
			pet = new Dog(level, owner);
			break;
		case 1:
			pet = new Cat(level, owner);
			break;
		case 2:
			pet = new Turtle(level, owner);
			break;
		default:
			pet = new Dog(level, owner);
			System.out.println("Unknown type, generating default.");
			break;
		}
		
		return pet;
	}
	
	public void update(double dt) 
	{
		super.update(dt);
		
		if (getVelocity().x != 0 && !running) setRunning(true);
		else if (getVelocity().x == 0) setRunning(false);
		
		if (running && walkTimer.getElapsedTime() >= 400) walkTimer.reset();
		
		// If it's time for an action
		if (actionTimer.getElapsedTime() >= nextActionTime)
		{
			// Jump if not standing still
			if (getVelocity().x != 0)
			{
				setVelocity(Vector2.add(getVelocity(), JUMP_VECTOR));
			}
			else // Walk a bit
			{
				double xvel = (random.nextInt(100) + 1) * (random.nextInt(10) < 5 ? 1 : -1);
				
				setVelocity(Vector2.add(getVelocity(), new Vector2(xvel, 0)));
			}
			
			nextActionTime = random.nextInt(2500) + 2500;
			actionTimer.reset();
		}
		else // Keep in range
		{
			Vector2 pNullY = new Vector2(getPosition().x, 0);
			Vector2 oNullY = new Vector2(owner.getHitbox().getCenterX(), 0);
			
			if (Vector2.distanceBetween(pNullY, oNullY) > getFollowRange())
			{
				Vector2 vel = getVelocity();
				
				if (getPosition().x < getOwner().getPosition().x) vel.x += 0.9;
				else if (getPosition().x > getOwner().getPosition().x) vel.x -= 0.9;
			}
		}
	}

	public void render(Bitmap screen) 
	{
		boolean flip = getVelocity().x < 0;
		
		frame = 0;
		
		if (running)
		{
			if (walkTimer.getElapsedTime() < 200) frame = 1;
			else frame = 2; 
		}

		screen.draw(Art.instance.pets[frame][ySpriteIndex], (int) getPosition().x, (int) getPosition().y, flip);
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
		
		if (direction != null)
		{
			if (velocity.x < 0) direction.x = -1;
			else if (velocity.x > 0) direction.x = 1;
			else direction.x = 0;
			
			if (velocity.y > 0) direction.y = 1;
			else if (velocity.y < 0) direction.y = -1;
			else direction.y = 0;
		}
		
		super.setVelocity(velocity);
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
	
	public PhysicsEntity getOwner() 
	{
		return owner;
	}

	public void setOwner(PhysicsEntity owner) 
	{
		this.owner = owner;
	}

	public final double getFollowRange()
	{
		return follow_range;
	}

	public void setFollowRange(double follow_range) 
	{
		this.follow_range = follow_range;
	}
}
