package no.nith.predikament.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import no.nith.predikament.Art;
import no.nith.predikament.Bitmap;
import no.nith.predikament.player.Player;
import no.nith.predikament.entity.*;
import no.nith.predikament.entity.unit.*;
import no.nith.predikament.entity.weapon.*;
import no.nith.predikament.entity.particle.*;
import no.nith.predikament.util.Vector2;

public class Level 
{
	private final Random random = new Random();
	private static final boolean DRAW_COLLISION_BOX = true;
	public Vector2 GRAVITY = new Vector2(0, 0.95);
	public final Vector2 FRICTION = new Vector2(0.991, 1);
	private Player player;
	private final int width;
	private final int height;
	
	private List<Entity> entities;
	private List<Weapon> bullets;
	private List<Particle> particles;
	
	public Level(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		entities = Collections.synchronizedList(new ArrayList<Entity>());
		bullets = Collections.synchronizedList(new ArrayList<Weapon>());
		particles = Collections.synchronizedList(new ArrayList<Particle>());
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
		bullets.clear();
		particles.clear();

		if (player.hasTarget()) addEntity(player.getTarget());
		if (player.hasPet()) addEntity(player.getPet());
	}
	
	public synchronized void addEntity(Entity entity)
	{
		if (entity != null)
		{
			if (entity instanceof Weapon) bullets.add((Weapon) entity);
			else if (entity instanceof Particle) particles.add((Particle) entity);
			else entities.add(entity);
		}
	}
	
	public synchronized void update(double dt)
	{
		// Update entities
		Iterator<Entity> ents = entities.iterator();
		
		while (ents.hasNext())
		{
			Entity e = ents.next();
			
			if (e.wasRemoved() == false)
			{
				// If current entity is a PhysicsEntity apply gravity and friction
				if (e instanceof PhysicsEntity)
				{
					PhysicsEntity pe = (PhysicsEntity) e;
					Vector2 pe_vel = pe.getVelocity();

					pe_vel.x += GRAVITY.x;
					pe_vel.y += GRAVITY.y;
					pe_vel.x *= FRICTION.x;
					pe_vel.y *= FRICTION.y;
					
					pe.update(dt);
				}
				else e.update(dt);
			}
			else ents.remove();
		}
		
		// Update bullets
		Iterator<Weapon> buls = bullets.iterator();
		
		while (buls.hasNext())
		{
			Weapon w = buls.next();
			
			if (w.wasRemoved() == false) w.update(dt);
			else buls.remove();
		}
		
		// Update particles
		Iterator<Particle> pars = particles.iterator();
		
		while (pars.hasNext())
		{
			Particle p = pars.next();
			
			if (p.wasRemoved() == false)
			{
				Vector2 particle_vel = p.getVelocity();
				particle_vel.x += GRAVITY.x;
				particle_vel.y += GRAVITY.y * 0.2;
				
				((Particle)p).update(dt);
				
				if (p.getPosition().y > getHeight()) 
				{ 
					p.remove(); 
				}
			}
			else pars.remove();
		}
	}
	
	public synchronized void render(Bitmap screen)
	{
		// Render level background
		screen.draw(Art.instance.background[0][0], 0, 0);

		// Render entities
		for (Entity e : entities) if (e.wasRemoved() == false) e.render(screen);

		// Render bullets
		for (Weapon w : bullets) if (w.wasRemoved() == false) w.render(screen);

		// Render particles
		for (Particle p : particles) if (p.wasRemoved() == false) p.render(screen);

		// Render the pet over all other entities
		if (player.hasPet()) player.getPet().render(screen);

		// Render the player last
		if (player.hasTarget()) player.getTarget().render(screen);
		
		// If enabled, draw collision boxes last
		if (DRAW_COLLISION_BOX) renderHitbox(screen);
	}
	
	public void renderHitbox(Bitmap screen)
	{
		// Render entities
		for (Entity e : entities) if (e.wasRemoved() == false) e.renderHitbox(screen);
		
		// Render bullets
		for (Weapon w : bullets) if (w.wasRemoved() == false) w.renderHitbox(screen);
		
		// Render particles
		for (Particle p : particles) if (p.wasRemoved() == false) p.renderHitbox(screen);
		
		// Render the pet over all other entities
		if (player.hasPet()) player.getPet().renderHitbox(screen);
		
		// Render the player last
		if (player.hasTarget()) player.getTarget().renderHitbox(screen);
	}

	public final int getWidth() 
	{
		return width;
	}

	public final int getHeight() 
	{
		return height;
	}

	public final Player getPlayer() 
	{
		return player;
	}

	public void setPlayer(Player player) 
	{
		this.player = player;
	}
}
