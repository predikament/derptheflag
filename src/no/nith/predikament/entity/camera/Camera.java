package no.nith.predikament.entity.camera;

import no.nith.predikament.Bitmap;
import no.nith.predikament.entity.PhysicsEntity;
import no.nith.predikament.util.Vector2;

public class Camera extends PhysicsEntity 
{
	public Camera(Vector2 position, int width, int height)
	{
		super(position, width, height);
	}
	
	public void render(Bitmap screen) {}
}
