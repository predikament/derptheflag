package no.nith.predikament.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.Entity;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.entity.unit.Unit;
import no.nith.predikament.entity.weapon.Bullet;
import no.nith.predikament.entity.weapon.Lazer;
import no.nith.predikament.entity.weapon.Weapon;
import no.nith.predikament.player.Player;
import no.nith.predikament.util.Vector2;

public class Level 
{
	private final Random random = new Random();
	public Vector2 GRAVITY = new Vector2(0, 0.60);
	public final Vector2 FRICTION = new Vector2(0.991, 1);
	private Player player;
	private final int width;
	private final int height;
	
	private List<Entity> entities;
	private List<Weapon> bullets;
	private List<Weapon> particles;
	
	public Level(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		entities = Collections.synchronizedList(new ArrayList<Entity>());
		bullets = Collections.synchronizedList(new ArrayList<Weapon>());
		particles = Collections.synchronizedList(new ArrayList<Weapon>());
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

		if (player.hasPet()) addEntity(player.getPet());
		
		// Testing
		for (int x = 0; x < 3; ++x) addEntity(Unit.create(this, random.nextInt(Unit.TOTAL_UNITS)));
	}
	
	public synchronized void addEntity(Entity entity)
	{
		if (entity != null)
		{
			if (entity instanceof Weapon) bullets.add((Weapon) entity);
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
		
		Iterator<Entity> ei = entities.iterator();
		
		while (ei.hasNext())
		{
			Entity e = ei.next();
			
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
					
					if (p instanceof Unit)
					{
						Unit pu = (Unit) p;
						Entity eu = pu;
						
						while (eu.equals(pu)) eu = entities.get(random.nextInt(entities.size()));
						
						pu.lookAt(entities.get(random.nextInt(entities.size())).getPosition());
						
						int rand = random.nextInt(2000);
						
						if (rand <= 10)
						{
							if (rand == 10) particles.clear();
							else if (rand < 2)
							{
								if (pu.getPosition().x < eu.getPosition().x) pu.setVelocity(Vector2.add(pu.getVelocity(), new Vector2(2000, 0)));
								else if (pu.getPosition().x > eu.getPosition().x) pu.setVelocity(Vector2.add(pu.getVelocity(), new Vector2(-2000, 0)));
							}
							else pu.shoot();
						}
					}
					
					Iterator<Weapon> b = bullets.iterator();
					
					while (b.hasNext())
					{
						Weapon w = b.next();
						
						if (w.wasRemoved() == false)
						{
							w.update(dt);
							
							boolean hasCollision = w.getHitbox().intersects(p.getHitbox());
							
							if (hasCollision)
							{
								for (int i = 0; i < 15; ++i)
								{
									double rxVel = (random.nextInt(200) - 100) * 0.001;
									double ryVel = (random.nextInt(200) - 100) * 0.001;
									if (random.nextDouble() >= 0.5) rxVel *= -1;
									if (random.nextDouble() >= 0.5) ryVel *= -1;
									
									Vector2 particle_vel = new Vector2(rxVel, ryVel);
									
									particles.add(new Lazer(w.getPosition(), particle_vel));
								}
								
								w.remove();
							}
							if (w.getPosition().x < -1000 || w.getPosition().x > 1000) w.remove();
						}
						else b.remove();
					}
				}
				else e.update(dt);
			}
			else ei.remove();
		}
		
		Iterator<Weapon> p = particles.iterator();
		
		while (p.hasNext())
		{
			Weapon w = p.next();
			
			if (w.wasRemoved() == false)
			{
				w.update(dt);
				
				if (w.getPosition().x < -1000) w.remove();
			}
			else p.remove();
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
		
		for (Weapon w : bullets)
		{
			w.render(screen);
		}
		
		for (Weapon p : particles)
		{
			p.render(screen);
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
