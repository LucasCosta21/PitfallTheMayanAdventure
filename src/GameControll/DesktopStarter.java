package GameControll;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopStarter{

	public static void main (String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Pitfall";
        cfg.useGL20 = false;
        cfg.width = 1280;
        cfg.height = 620;
        cfg.addIcon("spritesheet/icon.png", Files.FileType.Internal);
        new LwjglApplication(new Pitfall(), cfg);
	}
}