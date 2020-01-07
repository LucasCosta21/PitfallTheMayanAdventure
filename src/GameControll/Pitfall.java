package GameControll;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import GameObjects.Boss;
import GameObjects.Enemy;
import GameObjects.Movel;
import GameObjects.Player;
import GameObjects.Tiro;

public class Pitfall extends ApplicationAdapter {

	// componentes audiovisuais do jogo
	private Player player;
	private Boss onca;
	private Enemy[] cobras;
	private Array<Tiro> pedras;
	private Music theme;
	private Music menuSong;
	private Texture map;
	private Texture map2;
	private Texture arvore;
	private Texture gameOver;
	private Texture tituloFase;
	private Movel[] sacoPedras;
	private Texture[] icons;
	private Movel[] buraco;
	private SpriteBatch batch;
	private Texture menu;
	private Texture menu2;
	private Texture telaVitoria;
	private OrthographicCamera camera;
	private BitmapFont font;

	// controladores de loop dos objetos interativos do mapa
	private float elapsedTime = 0f;
	private float tempoBuraco = 0f;
	private float tempoTiro = 0f;
	private float tempoAux = 0f;
	private float tempoTitulo = 0f;

	// booleanos para auxilio no controle do fluxo do jogo
	private boolean jump = false;
	private boolean isOpen = true;
	private boolean isOnMenu = true;
	private boolean HasTouched = false;
	private boolean podeAtirar = false;
	private boolean isControlable = true;
	private int jumpForce = 0;

	// uteis(overlap contribui no efeito do buraco)
	private Texture overlap1;
	private Texture overlap2;
	private int pontos = 0;
	private int i = 350;
	private int j = 35;
	private int k = 0;

	@Override
	public void create() {

		// carrega Todas as texturas que serão usadas no jogo
		carregaTextura();

		// carrega as musicas;
		theme = Gdx.audio.newMusic(Gdx.files.internal("sons/fase.mp3"));
		menuSong = Gdx.audio.newMusic(Gdx.files.internal("sons/menu.mp3"));

		// start the playback of the background music immediately
		theme.setLooping(true);
		menuSong.setLooping(true);
		menuSong.play();

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		pedras = new Array<Tiro>();

		tempoTitulo = 0;
	}

	// controle de construcao do array de tiros
	public void spawnTiro() {
		Tiro pedra = new Tiro(10, 10, player);
		pedra.direcao = player.animState;
		pedras.add(pedra);
		pedra.podeAtirar = true;
	}

	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		elapsedTime += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		if (!isOnMenu) {

			if (player.hitbox.getY() < 100 && isControlable) {
				jump = false;
				player.hitbox.setY(100);
			}

			controlaTiro();

			desenhaMapa();

			// limites da camera
			if (player.hitbox.x > 400 && player.hitbox.x < 4600 && onca.vida > 0) {
				camera.position.x = player.hitbox.x;
			}

			// controla a chamada do pulo do personagem
			if (jump)
				jumpForce = player.Jump(jumpForce);

			// controle de criacao da pedra
			for (Tiro pedra : pedras) {
				batch.draw(pedra.textura, pedra.hitbox.x, pedra.hitbox.y + 90, 10, 10);
				pedra.Atirado();
			}

			if (player.hitbox.overlaps(buraco[0].hitbox) || player.hitbox.overlaps(buraco[1].hitbox)) {
				isControlable = false;
				player.hitbox.y -= 3;
			}

			controlaGameOver();
			
			if (tempoTitulo < 3) {
				batch.draw(tituloFase, camera.position.x - 200, camera.position.y + 100, 400, 60);
				tempoTitulo += Gdx.graphics.getDeltaTime();
			} else {
				batch.draw(tituloFase, 10000, 1000, 350, 40);
			}

			animaPlayer();

			batch.draw(overlap1, 0, 0, 2500, 85);
			batch.draw(overlap2, 2500, 0, 2500, 85);

			controlaOnca();

			controlaInimigos();

			batch.draw(sacoPedras[0].textura, sacoPedras[0].hitbox.x, sacoPedras[0].hitbox.y, 64, 64);
			batch.draw(sacoPedras[1].textura, sacoPedras[1].hitbox.x, sacoPedras[1].hitbox.y, 64, 64);
			batch.draw(sacoPedras[2].textura, sacoPedras[2].hitbox.x, sacoPedras[2].hitbox.y, 64, 64);
			batch.draw(sacoPedras[3].textura, sacoPedras[3].hitbox.x, sacoPedras[3].hitbox.y, 64, 64);

			if (player.hitbox.x < 2900) {
				batch.draw(arvore, 1440, 0, 500, 610);
			} else {
				batch.draw(arvore, 3980, 0, 500, 610);
			}

			controlaHud();

			controlaColetavel();

			if (onca.vida <= 0) {
				player.animState = 2;
				batch.draw(telaVitoria, camera.position.x - 640, camera.position.y - 310, 1280, 620);
				player.hitbox.x = camera.position.x + 200;
				player.hitbox.y = camera.position.y;
				isControlable = false;
			}

		} else {
			if (player.hitbox.x < 900) {
				player.hitbox.x += 5;
				batch.draw(menu2, 0, 0, 800, 500);
				batch.draw(player.animacoes[3].getKeyFrame(elapsedTime, true), player.hitbox.x, 260, 120, 180);
			} else {
				batch.draw(menu, 0, 0, 800, 500);
			}
		}

		batch.end();

		if (isControlable) {
			pegaEntrada();
		}

	}

	public void controlaGameOver() {
		if (player.hitbox.y < 50 || (player.vida <= 0 && player.vidas > 0)) {

			tempoAux += Gdx.graphics.getDeltaTime();
			player.animState = 1;

			if (tempoAux < 3) {
				isControlable = false;
			} else if (tempoAux >= 3) {
				player.hitbox.x = 401;
				player.hitbox.y = 90;
				player.vida = 100;
				player.vidas--;
				isControlable = true;
				tempoAux = 0;
			}
		} else if (player.vidas <= 0) {
			player.animState = 1;
			pontos = 0;
			sacoPedras[0].hitbox.y = 90;
			sacoPedras[1].hitbox.y = 90;
			sacoPedras[2].hitbox.y = 90;
			sacoPedras[3].hitbox.y = 90;
			if (tempoAux <= 5) {
				batch.draw(gameOver, camera.position.x - 200, camera.position.y + 100, 400, 60);
				tempoAux += Gdx.graphics.getDeltaTime();
				isControlable = false;
			} else if (tempoAux > 5) {
				player.hitbox.x = 401;
				player.hitbox.y = 90;
				isControlable = true;
				tempoAux = 0;
				player.vidas = 3;
				player.vida = 100;
				onca.vida = 100;
			}
		}
	}

	public void controlaInimigos() {
		// controla inimigos
		cobras[0].ataca();
		batch.draw(cobras[0].animacoes[cobras[0].animState].getKeyFrame(elapsedTime, true), cobras[0].hitbox.x,
				cobras[0].hitbox.y, 150, 70);
		cobras[1].ataca();
		batch.draw(cobras[1].animacoes[cobras[1].animState].getKeyFrame(elapsedTime, true), cobras[1].hitbox.x,
				cobras[1].hitbox.y, 150, 70);
	}

	public void controlaColetavel() {
		if (player.hitbox.overlaps(sacoPedras[0].hitbox)) {
			player.nPedras += 5;
			pontos += 50;
			sacoPedras[0].hitbox.y = 1000;
		}
		if (player.hitbox.overlaps(sacoPedras[1].hitbox)) {
			player.nPedras += 5;
			pontos += 50;
			sacoPedras[1].hitbox.y = 1000;
		}
		if (player.hitbox.overlaps(sacoPedras[2].hitbox)) {
			player.nPedras += 5;
			pontos += 50;
			sacoPedras[2].hitbox.y = 1000;
		}
		if (player.hitbox.overlaps(sacoPedras[3].hitbox)) {
			player.nPedras += 5;
			pontos += 50;
			sacoPedras[3].hitbox.y = 1000;
		}
	}

	public void controlaHud() {
		// desenha fontes
		batch.draw(icons[0], camera.position.x + 230, camera.position.y + 160, 64, 64);
		batch.draw(icons[1], camera.position.x - 400, camera.position.y - 200, 80, 80);
		font.draw(batch, player.vida + "%", camera.position.x + 300, camera.position.y - 200);
		font.draw(batch, " x " + player.vidas, camera.position.x + 300, camera.position.y + 200);
		font.draw(batch, "0000" + pontos, camera.position.x - 380, camera.position.y + 200);
		font.draw(batch, " x " + player.nPedras, camera.position.x - 330, camera.position.y - 170);
	}

	private void animaPlayer() {

		// controle de estados de animacao do player
		if (player.animState == 0 || player.animState == 1) {
			batch.draw(player.animacoes[player.animState].getKeyFrame(elapsedTime, true), player.hitbox.getX() - 25,
					player.hitbox.getY(), 95, 170);
		} else if (player.animState == 2 || player.animState == 3) {
			batch.draw(player.animacoes[player.animState].getKeyFrame(elapsedTime, true), player.hitbox.getX() - 10,
					player.hitbox.getY(), 120, 180);
		}

	}

	private void pegaEntrada() {

		if (Gdx.input.isKeyPressed(Keys.A)) {
			player.hitbox.x -= 5;
			player.animState = 2;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			player.hitbox.x += 5;
			player.animState = 3;
		}
		if (Gdx.input.isKeyPressed(Keys.W) && player.hitbox.getY() == 100 && jump == false) {
			jumpForce = 500;
			jump = true;
		}

		if (Gdx.input.isKeyPressed(Keys.SPACE) && podeAtirar && player.nPedras > 0) {
			podeAtirar = false;
			player.nPedras--;
			spawnTiro();
		}

		if (!Gdx.input.isKeyPressed(Keys.D) && !Gdx.input.isKeyPressed(Keys.A)) {
			if (player.animState == 3) {
				player.animState = 1;
			}

			if (player.animState == 2) {
				player.animState = 0;
			}
		}

		// make sure the player stays within the screen bounds
		if (player.hitbox.x < 0)
			player.hitbox.x = 0;
		if (player.hitbox.x > 5000 - 64)
			player.hitbox.x = 5000 - 64;

		if (isOnMenu && Gdx.input.isKeyPressed(Keys.SPACE)) {
			elapsedTime = 0;
			isOnMenu = false;
			player.hitbox.x = 400;
			theme.play();
			menuSong.pause();
		}

	}

	private void desenhaMapa() {
		tempoBuraco += Gdx.graphics.getDeltaTime();

		if (isOpen && tempoBuraco >= 2) {
			i -= 10;
			j -= 1;
			k += 5;

			if (i <= 0 && j <= 0) {
				tempoBuraco = 0;
				isOpen = false;
			}
		} else if (!isOpen && tempoBuraco >= 2) {
			i += 10;
			j += 1;
			k -= 5;

			if (i >= 350 && j >= 35) {
				tempoBuraco = 0;
				isOpen = true;
			}
		}

		// desenho do mapa e buraco
		batch.draw(map, 0, 0, 2500, 480);
		batch.draw(map2, 2500, 0, 2500, 480);
		buraco[0].hitbox.height = j;
		buraco[0].hitbox.width = i;
		buraco[1].hitbox.height = j;
		buraco[1].hitbox.width = i;
		buraco[0].hitbox.x = 2300;
		buraco[0].hitbox.y = 90;
		buraco[1].hitbox.x = 2800;
		buraco[1].hitbox.y = 90;
		batch.draw(buraco[0].textura, 2300 + k, 90, i + 20, j + 5);
		batch.draw(buraco[1].textura, 2800 + k, 90, i + 20, j + 5);
	}

	public void controlaOnca() {
		if (player.hitbox.overlaps(onca.hitbox) || player.hitbox.overlaps(cobras[0].hitbox)
				|| player.hitbox.overlaps(cobras[1].hitbox)) {

			if (!HasTouched) {
				player.vida -= 25;
				HasTouched = true;
			}
		} else if (!player.hitbox.overlaps(onca.hitbox) || !player.hitbox.overlaps(cobras[0].hitbox)
				|| !player.hitbox.overlaps(cobras[1].hitbox)) {
			HasTouched = false;
		}
		// ia e animações do boss
		onca.ataca();
		batch.draw(onca.animacoes[onca.animState].getKeyFrame(elapsedTime, true), onca.hitbox.getX(),
				onca.hitbox.getY(), 150, 120);
	}

	public void carregaTextura() {
		// carrega o menu e a fonte que mostra os pontos
		menu = new Texture("menu/menubg.png");
		menu2 = new Texture("menu/menubg0.png");
		tituloFase = new Texture("spritesheet/mapa/title.png");
		font = new BitmapFont();
		font.setColor(Color.LIGHT_GRAY);
		map = new Texture("spritesheet/mapa/map.png");
		map2 = new Texture("spritesheet/mapa/map.png");
		overlap1 = new Texture("spritesheet/mapa/overlap.png");
		overlap2 = new Texture("spritesheet/mapa/overlap.png");
		telaVitoria = new Texture("spritesheet/mapa/telaVitoria.png");
		icons = new Texture[2];
		cobras = new Enemy[2];
		sacoPedras = new Movel[4];
		sacoPedras[0] = new Movel(50, 90, 64, 64);
		sacoPedras[1] = new Movel(1700, 300, 64, 64);
		sacoPedras[2] = new Movel(3900, 300, 64, 64);
		sacoPedras[3] = new Movel(4600, 300, 64, 64);
		sacoPedras[0].textura = new Texture("spritesheet/hud/pedras.png");
		sacoPedras[1].textura = new Texture("spritesheet/hud/pedras.png");
		sacoPedras[2].textura = new Texture("spritesheet/hud/pedras.png");
		sacoPedras[3].textura = new Texture("spritesheet/hud/pedras.png");
		gameOver = new Texture("spritesheet/mapa/GameOver.png");
		icons[0] = new Texture("spritesheet/hud/vida.png");
		icons[1] = new Texture("spritesheet/hud/pedras.png");
		arvore = new Texture("spritesheet/mapa/arvore.png");
		Texture.setEnforcePotImages(false);

		// carrega as imgens do inimigo e do player;
		player = new Player(0, 90, 50, 150);
		onca = new Boss(4960, 90, 50, 80);
		cobras[0] = new Enemy(700, 90, 150, 70);
		cobras[1] = new Enemy(2900, 90, 150, 70);
		buraco = new Movel[2];
		buraco[0] = new Movel(2300, 90, 350, 35);
		buraco[1] = new Movel(2700, 90, 350, 35);
		buraco[0].textura = new Texture("spritesheet/mapa/buraco.png");
		buraco[1].textura = new Texture("spritesheet/mapa/buraco.png");
	}

	public void controlaTiro() {
		// da um tempo ate a proxima vez que o player pode atirar
		if (!podeAtirar) {
			tempoTiro += Gdx.graphics.getDeltaTime();

			if (tempoTiro > 0.9f) {
				podeAtirar = true;
				tempoTiro = 0;
			}
		}

		Iterator<Tiro> iter = pedras.iterator();

		while (iter.hasNext()) {

			Tiro pedra = iter.next();

			if (pedra.hitbox.overlaps(onca.hitbox)) {
				onca.tomaDano(10);
				pontos += 100;
				iter.remove();
				pedra.podeAtirar = false;
			}

			if (pedra.hitbox.x > camera.position.x + 400) {
				iter.remove();
				pedra.podeAtirar = false;
			}
		}
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		font.dispose();
		pedras.clear();
		menu.dispose();
		sacoPedras[0].textura.dispose();
		sacoPedras[1].textura.dispose();
		sacoPedras[2].textura.dispose();
		sacoPedras[3].textura.dispose();
		icons[0].dispose();
		icons[1].dispose();
		buraco[0].textura.dispose();
		buraco[1].textura.dispose();
		menu2.dispose();
		map.dispose();
		map2.dispose();
		theme.dispose();
		batch.dispose();
		System.out.println("Fim do Jogo!");
	}
}