package no.nith.predikament.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.Bullet;
import no.nith.predikament.entity.Entity;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.entity.unit.Unit;
import no.nith.predikament.player.Player;
import no.nith.predikament.util.Vector2;

public class Level 
{
	public Vector2 GRAVITY = new Vector2(0, 0.60);
	public final Vector2 FRICTION = new Vector2(0.991, 1);
	private Player player;
	private final int width;
	private final int height;
	
	private List<Entity> entities;
	private List<Bullet> bullets;
	
	public Level(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		entities = Collections.synchronizedList(new ArrayList<Entity>());
		bullets = Collections.synchronizedList(new ArrayList<Bullet>());
	}
	
	public void init()
	{
		// Generate random player unit
		Unit unit = Unit.create(this, 0/*random.nextInt(Unit.TOTAL_UNITS)*/);
		
		// Set start position for player unit
		Vector2 unitPosition = new Vector2(	(getWidth() - unit.getHitbox().getWidth()) / 2.0, 
											(getHeight() - unit.getHitbox().getHeight()) / 2.0);
		unit.setPosition(unitPosition);
		
		player = new Player(this, unit);
		
		entities.clear();

		if (player.hasPet()) addEntity(player.getPet());
	}
	
	public synchronized void addEntity(Entity entity)
	{
		if (entity != null)
		{
			if (entity instanceof Bullet) bullets.add((Bullet) entity);
			else entities.add(entity);
		}
	}
	
	public synchronized void update(double dt)
	{
		// Apply gravity to player's entity
		PhysicsEntity playerEntity = (PhysicsEntity) player.getTarget();
		Vector2 p_vel = playerEntity.getVelocity();
		p_vel.x += GRAVITY.x;
		p_vel.y += GRAVITY.y;

		// Apply friction to player's entity
		p_vel.x *= FRICTION.x;
		p_vel.y *= FRICTION.y;
		
		// Update player
		player.update(dt);
		
		for (Entity e : entities)
		{
			if (e.wasRemoved() == false)
			{
				if (e instanceof PhysicsEntity)
				{
					// Apply gravity to the rest of the entities
					PhysicsEntity p = (PhysicsEntity) e;
					Vector2 pe_vel = p.getVelocity();
					pe_vel.x += GRAVITY.x;
					pe_vel.y += GRAVITY.y;

					// Apply friction to the rest of the entities
					pe_vel.x *= FRICTION.x;
					pe_vel.y *= FRICTION.y;
					
					p.update(dt);
				}
				else e.update(dt);
			}
			else entities.remove(e);
		}
		
		for (Bullet b : bullets)
		{
			b.update(dt);
		}
	}
	
	public synchronized void render(Bitmap screen)
	{
		// Render level background
		screen.draw(Art.instance.background[0][0], 0, 0);
		
		for (Entity e : entities)
		{
			if (e.wasRemoved() == false)
			{
				e.render(screen);
			}
		}
		
		for (Bullet b : bullets)
		{
			b.render(screen);
		}
		
		// Render the player last
		if (player != null) player.render(screen);
	}

	public int getWidth() 
	{
		return width;
	}

	public int getHeight() 
	{
		return height;
	}

	public Player getPlayer() 
	{
		return player;
	}

	public void setPlayer(Player player) 
	{
		this.player = player;
	}
}
