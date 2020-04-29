package hello.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Input;

public class MyHelloNo1 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Music s;
	private Stage stage;
	private Table t;
	private Image i;
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		s = Gdx.audio.newMusic(Gdx.files.internal("bomb2.mp3"));
	}
	int x = 0;
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//x = x + 1;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			//s.play();
			x++;
		}
		batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
		//batch.end();
		batch.draw(img, x, 10, 100,100);
		batch.end();
	}
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		s.dispose();
	}
}