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
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		s = Gdx.audio.newMusic(Gdx.files.internal("bomb2.mp3"));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
		/*
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			s.play();
			
		}
		batch.end();*/
		int si = 0;
		setup(si);
		if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT))
		{
			setup(100);
		}
	}
	
	private void setup(int si) {
		// TODO Auto-generated method stub
		Stage stage = new Stage();
		Table t = new Table();
		t.setBounds(si, 0, 100, 100);
		Image i = new Image(img);
		t.add(i);
		stage.addActor(t);
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		s.dispose();
	}
}
