package GameObjects;

import com.badlogic.gdx.graphics.Texture;

public class Tiro extends Movel{
	
	public boolean podeDestruir;
	public boolean podeAtirar;
	public int direcao;

	public Tiro(int w, int h, Movel player) {
		super(player.hitbox.x, player.hitbox.y, w, h);
		// TODO Auto-generated constructor stub
		
		podeDestruir = false;
		podeAtirar = false;
		
		textura = new Texture("spritesheet/pedra.png");
	}

	public void Atirado() {
		if(hitbox.x < 5000 && hitbox.x > 0 && podeAtirar) {
			if(direcao == 1 || direcao == 3) {
				hitbox.x += 15;
			}else {
				hitbox.x -= 15;
			}
		}else if(hitbox.x >= 5000 || hitbox.x <= 0) {
			podeDestruir = true;
			podeAtirar = false;
		}
	}
	
}
