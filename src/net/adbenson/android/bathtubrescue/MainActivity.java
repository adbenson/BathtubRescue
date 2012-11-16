package net.adbenson.android.bathtubrescue;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	
	private ToyRescue game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.canvas_layout);
                               
        CustomSurface surface = (CustomSurface) findViewById(R.id.surface);

        game = new ToyRescue(surface);
        
        surface.setInputReciever(game);
        
        game.start();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onMenuOpened(int i, Menu m) {
    	game.setPause(true);
    	return super.onMenuOpened(i, m);
    }
    
    @Override
    public void onOptionsMenuClosed(Menu m) {
       game.setPause(false);
    }
}
