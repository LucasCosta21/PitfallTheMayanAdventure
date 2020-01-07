package GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Movel {
	public Texture textura;
	public Rectangle hitbox;
	public Animation[] animacoes;
	protected TextureRegion[][] animationFrames;
	protected Texture[] spritesheet;
	public int animState = 1; //refere o estado de animação atual
	
	public Movel(float Posx, float Posy, int w, int h) {
		hitbox = new Rectangle();
		hitbox.x = Posx;
		hitbox.y = Posy;
		hitbox.width = w;
		hitbox.height = h;
	}
}
