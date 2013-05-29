package no.nith.predikament;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
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

public class Game extends Canvas implements Runnable
{
	private final Random random = new Random();
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH	= 320;
	public static final int HEIGHT	= 240;
	public static final int SCALE	= 3;
	
	private boolean keepRunning;
	
	private BufferedImage screenImage;
	private Bitmap screenBitmap;
	
	private InputHandler inputHandler;
	
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
		
		while (keepRunning)
		{
			long thisTime		= System.currentTimeMillis();
			double thisFrameNs	= (double) System.nanoTime();
			double deltaTime	= (thisFrameNs - lastFrameNs) / 1.0E9; //1000000000;
			
			update(deltaTime);
			render(screenBitmap);
			
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
		
		public InputHandler(Game game)
		{
			this.game = game;
			
			pressedKeys = Collections.synchronizedSet(new HashSet<Integer>(0));
			
			game.addKeyListener(this);
			game.addMouseListener(this);
		}
		
		// MouseListener
		public synchronized void mousePressed(MouseEvent event) 
		{	
			
		}
		
		public synchronized void mouseReleased(MouseEvent event) 
		{	
			
		}
		
		public synchronized void mouseClicked(MouseEvent event) 
		{
			Unit unit = Unit.create(game.level, random.nextInt(Unit.TOTAL_UNITS));
			
			double mouseX = (event.getX() / SCALE) - (unit.getHitbox().getWidth() / 2);
			double mouseY = (event.getY() / SCALE) - (unit.getHitbox().getHeight() / 2);
			
			unit.setPosition(mouseX, mouseY);
						
			level.addEntity(unit);
		}

		public synchronized void mouseEntered(MouseEvent event) 
		{
			
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
						level.getPlayer().moveLeft();
						break;
						
					case KeyEvent.VK_RIGHT:
						level.getPlayer().moveRight();
						break;
						
					case KeyEvent.VK_UP:
						break;
					
					case KeyEvent.VK_DOWN:
						break;
						
					case KeyEvent.VK_SPACE:
						level.getPlayer().jump();
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