package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Movel {

	public int vida = 100;
	private float elapsedTime;

	public Enemy(int Posx, int Posy, int w, int h) {
		super(Posx, Posy, w, h);
		// TODO Auto-generated constructor stub
		animState = 0;
		Anima();
	}

	public void tomaDano(int dano) {
		vida -= dano;
	}

	public void ataca() {

		elapsedTime += Gdx.graphics.getDeltaTime();

		if (animState == 0) {
			hitbox.x += 6;
			if (elapsedTime >= 2.5f) {
				animState = 1;
				elapsedTime = 0;
			}
		}

		if (animState == 1) {
			hitbox.x -= 6;

			if (elapsedTime >= 2.5f) {
				animState = 0;
				elapsedTime = 0;
			}
		}
	}

	public void Anima() {
		spritesheet = new Texture[2];
		animationFrames = new TextureRegion[2][];
		animacoes = new Animation[2];

		spritesheet[0] = new Texture("spritesheet/inimigo/inimigo.png");
		spritesheet[1] = new Texture("spritesheet/inimigo/inimigo2.png");

		TextureRegion[][] tmpFrames = TextureRegion.split(spritesheet[0], 468, 108);
		TextureRegion[][] tmpFrames2 = TextureRegion.split(spritesheet[1], 468, 108);

		animationFrames[0] = new TextureRegion[2];
		animationFrames[1] = new TextureRegion[2];

		int index = 0;
//	    index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 2; j++) {
				animationFrames[0][index++] = tmpFrames[j][i];
			}
		}

		index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 2; j++) {
				animationFrames[1][index++] = tmpFrames2[j][i];
			}
		}

		animacoes[0] = new Animation(0.15f, animationFrames[0]);
		animacoes[1] = new Animation(0.15f, animationFrames[1]);
	}
}
