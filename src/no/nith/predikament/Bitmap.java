package no.nith.predikament;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Bitmap 
{
	public final int[] pixels;
	public final int w, h;
	
	public Bitmap(int w, int h) 
	{
		this.w = w;
		this.h = h;
		this.pixels = new int[w * h];
	}
	
	public Bitmap(int w, int h, int[] pixels) 
	{
		this.w = w;
		this.h = h;
		this.pixels = pixels;
	}

	public Bitmap(BufferedImage img) 
	{
		this.w = img.getWidth();
		this.h = img.getHeight();
		this.pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	public void clear(int color)
	{
		//int color = ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff); //65536 * r + 256 * g + b;
		Arrays.fill(pixels, color);
	}
	
	public void replaceColor(int c0, int c1)
	{
		for (int y = 0; y < h; ++y)
		{
			for (int x = 0; x < w; ++x)
			{
				if (pixels[x + y * w] == c0) pixels[x + y * w] = c1;
			}
		}
	}
	
	public void setPixel(double x, double y, int color) 
	{
		setPixel((int) x, (int) y, color);
	}
	
	public void setPixel(int x, int y, int color)
	{
		if (x >= 0 && x < w && y >= 0 && y < h) pixels[x + y * w] = color;
	}	
	
	public void drawLine(double x0, double y0, double x1, double y1, int color)
	{
		drawLine((int) x0, (int) y0, (int) x1, (int) y1, color);
	}

	public void drawLine(int x0, int y0, int x1, int y1, int color)
	{
		boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		
		if (steep)
		{
			// Swap X's with Y's
			int temp = x0;
			x0 = y0;
			y0 = temp;
			temp = x1;
			x1 = y1;
			y1 = temp;
		}
		if (x0 > x1)
		{
			// Swap X's and X's and Y's with Y's
			int temp = x0;
			x0 = x1;
			x1 = temp;
			temp = y0;
			y0 = y1;
			y1 = temp;
		}
		
		int deltaX = x1 - x0;
		int deltaY = Math.abs(y1 - y0);
		int error = deltaX / 2;
		int ystep = 0;
		int y = y0;
		if (y0 < y1) ystep = 1;
		else ystep = -1;
		
		for (int x = x0; x < x1; ++x)
		{
			if (steep) setPixel(y, x, color);
			else setPixel(x, y, color);
			
			error = error - deltaY;
			
			if (error < 0)
			{
				y = y + ystep;
				error += deltaX;
			}
		}
	}
	
	public void drawCircle(double x, double y, int radius, int color)
	{
		drawCircle((int) x, (int) y, radius, color);
	}
	
	public void drawCircle(int x, int y, int radius, int color)
	{
		int x0 = radius;
		int y0 = 0;
		int radiusError = 1-x0;
		
		while (x0 >= y0)
		{
			setPixel(x0 + x, y0 + y, color);
			setPixel(y0 + x, x0 + y, color);
			setPixel(-x0 + x, y0 + y, color);
			setPixel(-y0 + x, x0 + y, color);
			setPixel(-x0 + x, -y0 + y, color);
			setPixel(-y0 + x, -x0 + y, color);
			setPixel(x0 + x, -y0 + y, color);
			setPixel(y0 + x, -x0 + y, color);
			
			y0++;
			
			if (radiusError < 0) radiusError += 2 * y0 + 1;
			else radiusError += 2 * (y0 - --x0) + 1;
		}
	}
	
	public void drawSquare(double x0, double y0, double x1, double y1, int color)
	{
		drawSquare((int) x0, (int) y0, (int) x1, (int) y1, color);
	}
	
	public void drawSquare(int x0, int y0, int x1, int y1, int color)
	{
		drawLine(x0, y0, x1, y0, color);
		drawLine(x0, y1, x1, y1, color);
		drawLine(x0, y0, x0, y1, color);
		drawLine(x1, y0, x1, y1, color);
	}
	
	public void fill(int x0, int y0, int x1, int y1, int color)
	{
		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 > w) x1 = w;
		if (y1 > h) y1 = h;
		
		for (int y = y0; y < y1; ++y)
		{
			for (int x = x0; x < x1; ++x)
			{
				pixels[x + y * w] = color;
			}
		}
	}
	
	public void multiply(Bitmap other, double x0, double y0)
	{
		multiply(other, (int) x0, (int) y0);
	}
	
	public void multiply(Bitmap other, int x0, int y0)
	{
		for (int y = 0; y < other.h; ++y)
		{
			for (int x = 0; x < other.w; ++x)
			{
				int xa = x + x0;
				int ya = y + y0;
				
				if (xa >= 0 && xa < w && ya >= 0 && ya < h) pixels[xa + ya * w] |= other.pixels[x + y * other.w] << 32;
			}
		}
	}
	
	public void draw(Bitmap b, double xp, double yp)
	{
		draw(b, (int) xp, (int) yp);
	}
	
	public void draw(Bitmap b, int xp, int yp)
	{
		draw(b, xp, yp, false);
	}
	
	public void draw(Bitmap b, int xp, int yp, boolean xFlip) 
	{
		int x0 = xp;
		int x1 = xp + b.w;
		int y0 = yp;
		int y1 = yp + b.h;
		
		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 > w) x1 = w;
		if (y1 > h) y1 = h;
		
		if (xFlip)
		{
			for (int y = y0; y < y1; ++y)
			{
				int sp = (y - y0) * b.w + xp + b.w - 1;
				int dp = y * w;
				
				for (int x = x0; x < x1; ++x)
				{
					int c = b.pixels[sp - x];
					
					if (c < 0) pixels[dp + x] = b.pixels[sp - x]; // Transparency
				}
			}
		}
		else
		{
			for (int y = y0; y < y1; ++y)
			{
				int sp = (y - y0) * b.w - xp;
				int dp = y * w;
				
				for (int x = x0; x < x1; ++x)
				{
					int c = b.pixels[sp + x];
					
					if (c < 0) pixels[dp + x] = b.pixels[sp + x]; // Transparency
				}
			}
		}
	}
}