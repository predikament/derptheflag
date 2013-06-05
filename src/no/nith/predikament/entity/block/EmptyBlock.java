package no.nith.predikament.entity.block;

import no.nith.predikament.Bitmap;
import no.nith.predikament.util.Vector2;

public class EmptyBlock extends Block 
{
	public EmptyBlock(Vector2 position) 
	{
		super(position, -1, -1, false);
	}
	
	public void render(Bitmap screen)
	{
		// Render nothing
	}
}
