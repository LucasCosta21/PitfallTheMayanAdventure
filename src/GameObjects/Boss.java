package GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Boss extends Movel {

	public int vida = 100;
	private Music rugido;
	private float elapsedTime;

	// elapsedTime += Gdx.graphics.getDeltaTime();

	public Boss(int Posx, int Posy, int w, int h) {
		super(Posx, Posy, w, h);
		// TODO Auto-generated constructor stub
		Anima();
		rugido = Gdx.audio.newMusic(Gdx.files.internal("sons/rugido.mp3"));
		elapsedTime = 0;
		animState = 0;
	}

	public void ataca() {
		
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		if (animState == 0) {
			
			rugido.play();

			if (elapsedTime >= 1.5f) {
				animState = 1;
				elapsedTime = 0;
				rugido.stop();
			}
		}

		if (animState == 1) {
			hitbox.x -= 5;

			if (elapsedTime >= 1.5f) {
				animState = 2;
				elapsedTime = 0;
			}
		}

		if (animState == 2) {

			
			rugido.play();
			
			if (elapsedTime >= 1.5f) {
				animState = 3;
				elapsedTime = 0;
				rugido.stop();
			}
		}

		if (animState == 3) {
			hitbox.x += 5;
			
			if (elapsedTime >= 1.5f) {
				animState = 0;
				elapsedTime = 0;
			}
		}
	}
	
	public void tomaDano(int dano) {
		vida -= dano;
	}

	public void Anima() {
		spritesheet = new Texture[4];
		animationFrames = new TextureRegion[4][];
		animacoes = new Animation[4];

		spritesheet[0] = new Texture("spritesheet/onca/oncaRugeEsquerda.png");
		spritesheet[1] = new Texture("spritesheet/onca/oncaAndaEsquerda.png");
		spritesheet[2] = new Texture("spritesheet/onca/oncaRugeDireita.png");
		spritesheet[3] = new Texture("spritesheet/onca/oncaAndaDireita.png");

		TextureRegion[][] tmpFrames = TextureRegion.split(spritesheet[0], 85, 52);
		TextureRegion[][] tmpFrames2 = TextureRegion.split(spritesheet[1], 90, 62);
		TextureRegion[][] tmpFrames3 = TextureRegion.split(spritesheet[2], 85, 52);
		TextureRegion[][] tmpFrames4 = TextureRegion.split(spritesheet[3], 90, 62);

		animationFrames[0] = new TextureRegion[10];
		animationFrames[1] = new TextureRegion[6];
		animationFrames[2] = new TextureRegion[10];
		animationFrames[3] = new TextureRegion[6];

		int index = 0;
//	    index = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				animationFrames[0][index++] = tmpFrames[j][i];
			}
		}

		index = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				animationFrames[1][index++] = tmpFrames2[j][i];
			}
		}
		
		index = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				animationFrames[2][index++] = tmpFrames3[j][i];
			}
		}
		
		index = 0;
		for (int i = 0; i < 2; i++) {
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
