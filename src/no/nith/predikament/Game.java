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
import java.util.Set;
import no.nith.predikament.util.Stopwatch;

public class Game extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE 	= "Plants vs Retards";	
	public static final int	FPS 		= 60;
	public static final int HEIGHT		= 480;
	public static final int WIDTH		= HEIGHT * 16 / 9;	
	public static final int SCALE		= 1;
	
	private boolean keepRunning;
	
	private BufferedImage screenImage;
	private Bitmap screenBitmap;
	
	private InputHandler inputHandler;
	private Stopwatch frameTimer;
	
	private int currentFrameCount;
	private int updatesPerSecond;
	
	private boolean displayFPS;
	
	public Game()
	{
		// Window size
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setFocusable(true);
		
		inputHandler = new InputHandler(this);
		displayFPS = true;
		frameTimer = new Stopwatch(false);
		
		currentFrameCount = 0;
		updatesPerSecond = 0;
		
		keepRunning = true;
	}
	
	private void init() 
	{
		Art.init();
		
		screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		screenBitmap = new Bitmap(screenImage);
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
	}
	
	private void render(Bitmap screen)
	{
		screen.clear(0xFFFFFFFF);
		
		++currentFrameCount;
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
		g.drawImage(screenImage, (screenWidth - w) / 2, (screenHeight - h) / 2, w, h, null);
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
			
			++updatesPerSecond;
			
			// Try to render at desired FPS
			if (frameTimer.getElapsedTime() >= 1000 / FPS) 
			{
				render(screenBitmap);
				
				frameTimer.reset();
			}
			
			boolean timeToResetCounters = thisTime - lastTime >= 1000;
			
			if (timeToResetCounters)
			{
				if (displayFPS)
				{
					String output = String.format("%d fps, %d ups", currentFrameCount, updatesPerSecond);
					
					System.out.println(output);
				}
				
				currentFrameCount = 0;
				updatesPerSecond = 0;
				
				lastTime = thisTime;
			}

			lastFrameNs = (double) System.nanoTime();
			
			swap();
			
			// Sleep 1 millisecond per update cycle to allow potential worker-threads to catch up
			try { Thread.sleep(1); } catch (InterruptedException ie) { ie.printStackTrace(); System.exit(1); }
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
			
			pressedKeys = Collections.synchronizedSet(new HashSet<Integer>());
			
			game.addKeyListener(this);
			game.addMouseListener(this);
			game.addMouseMotionListener(this);
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
					case KeyEvent.VK_A:
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_D:
					case KeyEvent.VK_UP:
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_SPACE:
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