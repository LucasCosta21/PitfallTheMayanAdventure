package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Movel {

	public int vida = 100;
	public int vidas = 3;
	public int nPedras = 10;

	public Player(int Posx, int Posy, int w, int h) {
		super(Posx, Posy, w, h);
		// TODO Auto-generated constructor stub

		Anima();
	}

	public int Jump(int forca) {
		hitbox.setY(hitbox.getY() + forca * Gdx.graphics.getDeltaTime());
		forca -= 20;
		//System.out.println(forca);

		return forca;
	}

	public void Anima() {
		spritesheet = new Texture[4];
		animationFrames = new TextureRegion[4][];
		animacoes = new Animation[4];
		
		spritesheet[0] = new Texture("spritesheet/player/idle_left.png");
		spritesheet[1] = new Texture("spritesheet/player/idle_right.png");
		spritesheet[2] = new Texture("spritesheet/player/running_left.png");
		spritesheet[3] = new Texture("spritesheet/player/running_right.png");

		TextureRegion[][] tmpFrames = TextureRegion.split(spritesheet[0], 164, 244);
		TextureRegion[][] tmpFrames2 = TextureRegion.split(spritesheet[1], 164, 244);
		TextureRegion[][] tmpFrames3 = TextureRegion.split(spritesheet[2], 270, 220);
		TextureRegion[][] tmpFrames4 = TextureRegion.split(spritesheet[3], 270, 220);

		animationFrames[0] = new TextureRegion[7];
		animationFrames[1] = new TextureRegion[7];
		animationFrames[2] = new TextureRegion[9];
		animationFrames[3] = new TextureRegion[9];

		int index = 0;

		for (int i = 0; i < 7; i++) {
			animationFrames[0][index++] = tmpFrames[0][i];
		}

		index = 0;
		for (int i = 0; i < 7; i++) {
			animationFrames[1][index++] = tmpFrames2[0][i];
		}

		index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				animationFrames[2][index++] = tmpFrames3[j][i];
			}
		}

		index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				animationFrames[3][index++] = tmpFrames4[j][i];
			}
		}
		animacoes[0] = new Animation(0.15f, animationFrames[0]);
		animacoes[1] = new Animation(0.15f, animationFrames[1]);
		animacoes[2] = new Animation(0.10f, animationFrames[2]);
		animacoes[3] = new Animation(0.10f, animationFrames[3]);
	}
}
