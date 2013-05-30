package no.nith.predikament.pet;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Vector2;

public class Pet extends PhysicsEntity 
{
	/*private static final Color CHANGEABLE_COLOR = Color.MAGENTA;
	private static final Color[] CHOICES =
		{
			Color.BLACK,
			Color.DARK_GRAY,
			Color.YELLOW,
			Color.ORANGE,
			Color.PINK,
			Color.RED,
			Color.LIGHT_GRAY
		};
	private final Color color;*/
	private Level level;
	private PhysicsEntity owner;
	private int ySpriteIndex;
	private double follow_range;
	protected Vector2 direction;
	protected int frame;
	
	public Pet(Level level, PhysicsEntity owner, int ySpriteIndex)
	{
		super(0, 0, 16, 16);
		
		this.level = level;
		this.owner = owner;
		this.ySpriteIndex = ySpriteIndex;
		
		setFollowRange(5.0);
		
		direction = new Vector2();
		frame = 0;
	}

	public void update(double dt) 
	{
		// Keeps within follow_range pixels on the X-axis from target
		Vector2 pNullY = new Vector2(getPosition().x, 0);
		Vector2 oNullY = new Vector2(owner.getPosition().x, 0);
		
		if (Vector2.distanceBetween(pNullY, oNullY) > getFollowRange())
		{
			Vector2 vel = getVelocity();
			
			if (getPosition().x < getOwner().getPosition().x) vel.x += 0.8;
			else if (getPosition().x > getOwner().getPosition().x) vel.x -= 0.8;
		}
		
		super.update(dt);
	}

	public void render(Bitmap screen) 
	{
		boolean flip = false;
		
		if (direction.x == 0)
		{
			frame = 0;
		}
		else if (direction.x == 1)
		{
			frame = 1;
		}
		else if (direction.x == -1)
		{
			frame = 1;
			
			flip = true;
		}
		
		if (direction.y == 1) frame = 2;
		else if (direction.y == -1) frame = 2;

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
