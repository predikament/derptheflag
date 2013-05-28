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
	public final double friction = 0.98;
	public Vector2 GRAVITY = new Vector2(0, 0.5);
	public final Vector2 FRICTION = new Vector2(0.999, 1);
	private final Random random = new Random();
	private Player player;
	private final int width;
	private final int height;
	
	private List<Entity> entities;
	
	public Level(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		// Generate random player unit
		Unit unit = Unit.create(this, random.nextInt(Unit.TOTAL_UNITS));
		
		// Set start position for player unit
		Vector2 unitPosition = new Vector2(	(getWidth() - unit.getHitbox().getWidth()) / 2.0, 
											(getHeight() - unit.getHitbox().getHeight()) / 2.0);
		unit.setPosition(unitPosition);
		
		player = new Player(unit);
		
		entities = Collections.synchronizedList(new ArrayList<Entity>());
	}
	
	public void init()
	{
		entities.clear();
	}
	
	public synchronized void addEntity(Entity entity)
	{
		if (entity != null) entities.add(entity);
	}
	
	public synchronized void update(double dt)
	{
		player.update(dt);
		
		for (Entity e : entities)
		{
			if (e.wasRemoved() == false)
			{
				if (e instanceof PhysicsEntity)
				{
					// Apply level gravity here somewhere
					@SuppressWarnings("unused")
					PhysicsEntity p = (PhysicsEntity) e;
				}
				
				e.update(dt);
			}
			else entities.remove(e);
		}
	}
	
	public synchronized void render(Bitmap screen)
	{
		for (Entity e : entities)
		{
			if (e.wasRemoved() == false) e.render(screen);
		}
		
		player.render(screen);
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
