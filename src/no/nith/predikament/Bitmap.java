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
	
	public void setPixel(int x, int y, int color)
	{
		if (x >= 0 && x < w && y >= 0 && y < h) pixels[x + y * w] = color;
	}
	
	public void fill(int x0, int y0, int x1, int y1, int color)
	{
		if (x0 < 0) x0 = 0;
		if (y0 < 0) y0 = 0;
		if (x1 > w) x1 = w;
		if (y1 > h) y1 = h;
		
		for (int y = y0; y <= y1; ++y)
		{
			for (int x = x0; x <= x1; ++x)
			{
				pixels[x + y * w] = color;
			}
		}
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