package no.nith.predikament;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import no.nith.predikament.entity.unit.Unit;
import no.nith.predikament.level.Level;
import no.nith.predikament.util.Stopwatch;
import no.nith.predikament.util.Vector2;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	private final Random random = new Random();
	
	public static final int FPS 	= 60;
	public static final int WIDTH	= 320;
	public static final int HEIGHT	= 240;
	public static final int SCALE	= 4;
	
	private boolean keepRunning;
	
	private BufferedImage screenImage;
	private Bitmap screenBitmap;
	
	private InputHandler inputHandler;
	private Stopwatch frameTimer;
	
	private int currentFrameCount;
	
	private boolean displayFPS;
	
	private Level level;
	
	public Game()
	{
		// Window size
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setFocusable(true);
		
		inputHandler = new InputHandler(this);
		displayFPS = false;
		frameTimer = new Stopwatch(false);
	}
	
	private void init() 
	{
		Art.init();
		
		screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		screenBitmap = new Bitmap(screenImage);
		
		keepRunning = true;
		
		currentFrameCount = 0;
		
		level = new Level(WIDTH, HEIGHT);
		
		level.init();
	}
	
	public void start()
	{
		new Thread(this, "Game Thread").start();
	}
	
	public void stop()
	{
		keepRunning = false;
	}
	
	private void update(double dt)
	{
		inputHandler.update(dt);
		level.update(dt);
	}
	
	private void render(Bitmap screen)
	{
		screen.clear(0);
		level.render(screen);

		currentFrameCount++;
	}

	private void swap()
	{
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null)
		{
			this.createBufferStrategy(2);
			
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		int screenWidth	= getWidth();
		int screenHeight = getHeight();
		int w = WIDTH * SCALE;
		int h = HEIGHT * SCALE;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.drawImage(screenImage, (screenWidth-w) / 2, (screenHeight - h) / 2, w, h, null);
		g.dispose();
		
		bs.show();
	}
	
	public void run() 
	{
		init();
		
		long lastTime		= System.currentTimeMillis();
		double lastFrameNs	= (double) System.nanoTime();
		frameTimer.start();
		
		while (keepRunning)
		{
			long thisTime		= System.currentTimeMillis();
			double thisFrameNs	= (double) System.nanoTime();
			double deltaTime	= (thisFrameNs - lastFrameNs) / 1.0E9; //1000000000;
			
			// Update as often as possible
			update(deltaTime);
			
			// Try to render at desired FPS
			if (frameTimer.getElapsedTime() >= 1000 / FPS) 
			{
				render(screenBitmap);
				
				frameTimer.reset();
			}
			
			boolean timeToResetFrameCounter = thisTime - lastTime >= 1000;
			
			if (timeToResetFrameCounter)
			{
				if (displayFPS)	System.out.println(String.format("%d fps ", currentFrameCount));
				
				currentFrameCount = 0;
				lastTime = thisTime;
			}

			lastFrameNs = (double) System.nanoTime();
			
			swap();
		}
		
		System.exit(0);
	}
	
	// Input handler, needs to be remade to handle several keystrokes at once
	private class InputHandler implements KeyListener, MouseListener, MouseMotionListener
	{
		private final Game game;
		private final Set<Integer> pressedKeys;
		private static final int FIRE_BUTTON = MouseEvent.BUTTON1;
		
		public InputHandler(Game game)
		{
			this.game = game;
			
			pressedKeys = Collections.synchronizedSet(new HashSet<Integer>(0));
			
			game.addKeyListener(this);
			game.addMouseListener(this);
			game.addMouseMotionListener(this);
		}
		
		// MouseListener
		public synchronized void mousePressed(MouseEvent event) 
		{	
			try
			{
				switch (event.getButton())
				{
					case FIRE_BUTTON:
						if (level.getPlayer().hasTarget()) ((Unit)level.getPlayer().getTarget()).shoot();
						break;
					
					default:
						break;
				}
			}
			catch (Exception e)
			{
				// Eat exception
			}
		}
		
		public synchronized void mouseReleased(MouseEvent event) 
		{	
			
		}
		
		public synchronized void mouseClicked(MouseEvent event) 
		{
			if (level != null && level.getPlayer().hasTarget() == false)
			{
				Unit unit = Unit.create(level, random.nextInt(Unit.TOTAL_UNITS));
				
				double mouseX = (event.getX() / SCALE);
				double mouseY = (event.getY() / SCALE);
				
				Vector2 mousePos = new Vector2(mouseX, mouseY);
				
				mousePos.x -= unit.getHitbox().width / 2.0;
				mousePos.y -= unit.getHitbox().height / 2.0;
				
				unit.setPosition(mousePos);
				
				level.getPlayer().setTarget(unit);
			}
		}

		public synchronized void mouseEntered(MouseEvent event) 
		{
			try
			{
				if (level.getPlayer().hasTarget())
				{
					double mouseX = (event.getX() / SCALE);
					double mouseY = (event.getY() / SCALE);
					
					Vector2 mousePos = new Vector2(mouseX, mouseY);
					
					((Unit)level.getPlayer().getTarget()).lookAt(mousePos);
				}
			}
			catch (Exception e)
			{
				// Do nothing here, just eat up the exception
			}
		}

		public synchronized void mouseExited(MouseEvent event) 
		{
			
		}
		
		// MouseMotionListener
		public synchronized void mouseDragged(MouseEvent event) 
		{
			
		}

		public synchronized void mouseMoved(MouseEvent event) 
		{
			try
			{
				if (level.getPlayer().hasTarget())
				{
					double mouseX = (event.getX() / SCALE);
					double mouseY = (event.getY() / SCALE);
					
					Vector2 mousePos = new Vector2(mouseX, mouseY);
					
					((Unit)level.getPlayer().getTarget()).lookAt(mousePos);
				}
			}
			catch (Exception e)
			{
				// Do nothing here, just eat up the exception
			}
		}

		// KeyListener
		public synchronized void keyPressed(KeyEvent event) 
		{
			int keycode = event.getKeyCode();
			
			if (pressedKeys.contains(keycode) == false) pressedKeys.add(keycode);
		}

		public synchronized void keyReleased(KeyEvent event) 
		{
			int keycode = event.getKeyCode();
			
			if (pressedKeys.contains(keycode) == true) pressedKeys.remove(keycode);
		}

		public synchronized void keyTyped(KeyEvent event) 
		{
		}
		
		public synchronized void update(double dt)
		{
			for (int keycode : pressedKeys)
			{
				switch(keycode)
				{
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_A:
						if (level.getPlayer().hasTarget()) level.getPlayer().targetMoveLeft();
						break;
						
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_D:
						if (level.getPlayer().hasTarget()) level.getPlayer().targetMoveRight();
						break;
						
					case KeyEvent.VK_UP:
						break;
					
					case KeyEvent.VK_DOWN:
						break;
						
					case KeyEvent.VK_SPACE:
						// if (level.getPlayer().hasTarget()) level.getPlayer().getTarget().jump();
						break;
					
					case KeyEvent.VK_ESCAPE:
						game.stop();
						break;
						
					case KeyEvent.VK_F10:
						displayFPS = !displayFPS;
						break;
					
					default:
						break;
				}
			}
		}
	}
}