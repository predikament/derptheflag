package no.nith.predikament.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.Entity;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.entity.unit.Unit;
import no.nith.predikament.player.Player;
import no.nith.predikament.util.Vector2;

public class Level 
{
	public Vector2 GRAVITY = new Vector2(0, 0.20);
	public final Vector2 FRICTION = new Vector2(0.992, 1);
	private final Random random = new Random();
	private Player player;
	private final int width;
	private final int height;
	
	private List<Entity> entities;
	
	public Level(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		entities = Collections.synchronizedList(new ArrayList<Entity>());
	}
	
	public void init()
	{
		// Generate random player unit
		Unit unit = Unit.create(this, random.nextInt(Unit.TOTAL_UNITS));
		
		// Set start position for player unit
		Vector2 unitPosition = new Vector2(	(getWidth() - unit.getHitbox().getWidth()) / 2.0, 
											(getHeight() - unit.getHitbox().getHeight()) / 2.0);
		unit.setPosition(unitPosition);
		
		player = new Player(this, unit);
		
		entities.clear();

		entities.add(player.getTarget());
	}
	
	public synchronized void addEntity(Entity entity)
	{
		if (entity != null) entities.add(entity);
	}
	
	public synchronized void update(double dt)
	{
		for (Entity e : entities)
		{
			if (e.wasRemoved() == false)
			{
				if (e instanceof PhysicsEntity)
				{
					// Apply gravity
					PhysicsEntity p = (PhysicsEntity) e;
					Vector2 vel = p.getVelocity();
					vel.x += GRAVITY.x;
					vel.y += GRAVITY.y;

					// Apply friction
					vel.x *= FRICTION.x;
					vel.y *= FRICTION.y;
					
					p.setVelocity(vel);
					
					p.update(dt);
				}
				else e.update(dt);
			}
			else entities.remove(e);
		}
	}
	
	public synchronized void render(Bitmap screen)
	{
		for (Entity e : entities)
		{
			if (e.wasRemoved() == false)
			{
				if (player != null && e.equals(player.getTarget()) == false) e.render(screen);
			}
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
