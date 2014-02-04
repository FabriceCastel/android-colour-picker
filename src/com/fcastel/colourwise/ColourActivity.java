package com.fcastel.colourwise;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ColourActivity extends Activity implements OnTouchListener {
	
	SeekBar r_bar, g_bar, b_bar;
	TextView red, green, blue, redval, greenval, blueval, hexcolour;
	View picker, picker2, inverted;
	ImageView spectrum;
	ViewFlipper screen_select;
	Button switchB1, switchB2, specButton;
	int newcol;
	String hexcol;
	Bitmap specmap;
	int xcrd, ycrd, screenWidth, screenHeight, p2offset;
	float screenScale;
	
	

	private void updatePickerColour()
	{
		newcol = Color.rgb(Integer.parseInt(redval.getText().toString()),
						   Integer.parseInt(greenval.getText().toString()),
						   Integer.parseInt(blueval.getText().toString()));
		picker.setBackgroundColor(newcol);
		picker2.setBackgroundColor(newcol);
		hexcol = String.format("#%06X", (0xFFFFFF & newcol));
		hexcolour.setText(hexcol);
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_colour);
        
        picker   	  = (View)     	  findViewById(R.id.picker);
        picker2		  = (View)		  findViewById(R.id.picker2);
        r_bar    	  = (SeekBar)  	  findViewById(R.id.r_bar);
        g_bar    	  = (SeekBar)  	  findViewById(R.id.g_bar);
        b_bar    	  = (SeekBar)  	  findViewById(R.id.b_bar);
        red      	  = (TextView) 	  findViewById(R.id.red);
        green    	  = (TextView) 	  findViewById(R.id.green);
        blue     	  = (TextView) 	  findViewById(R.id.blue);
        redval   	  = (TextView) 	  findViewById(R.id.redval);
        greenval 	  = (TextView) 	  findViewById(R.id.greenval);
        blueval 	  = (TextView) 	  findViewById(R.id.blueval);
        hexcolour	  = (TextView)    findViewById(R.id.hexcolour);
        screen_select = (ViewFlipper) findViewById(R.id.screen_select);
        switchB1  	  = (Button)   	  findViewById(R.id.switchButton1);
        switchB2  	  = (Button)   	  findViewById(R.id.switchButton2);
        specButton	  = (Button)	  findViewById(R.id.specButton);
        spectrum	  = (ImageView)   findViewById(R.id.spectrum);
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        
        screenScale = getResources().getDisplayMetrics().density;
        p2offset = (int) (100 * screenScale + 0.5f);
        
        // Load the spectrum bitmap into specmap
        AssetManager manager = getAssets();
        InputStream open = null;
        try {
          open = manager.open("spectrummm.png");
          specmap = BitmapFactory.decodeStream(open);
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (open != null) {
            try {
              open.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        } 
        
        		
        specButton.setOnTouchListener(this);
        
        
        r_bar.setOnSeekBarChangeListener(
        	new OnSeekBarChangeListener()
        	{
        		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        		{
        			redval.setText(Integer.toString(progress));
        			updatePickerColour();
        		}
        		public void onStartTrackingTouch(SeekBar seekBar) {}
        		public void onStopTrackingTouch(SeekBar seekBar) {}
        	});
        
        g_bar.setOnSeekBarChangeListener(
            	new OnSeekBarChangeListener()
            	{
            		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            		{
            			greenval.setText(Integer.toString(progress));
            			updatePickerColour();
            		}
            		public void onStartTrackingTouch(SeekBar seekBar){}
            		public void onStopTrackingTouch(SeekBar seekBar){}
            	});
        
        b_bar.setOnSeekBarChangeListener(
            	new OnSeekBarChangeListener()
            	{
            		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            		{
            			blueval.setText(Integer.toString(progress));
            			updatePickerColour();
            		}
            		public void onStartTrackingTouch(SeekBar seekBar){}
            		public void onStopTrackingTouch(SeekBar seekBar){}
            	});
    
        switchB1.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		screen_select.showNext();
        		picker.setBackgroundColor(newcol);
        	}
        });
        
        switchB2.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		screen_select.showNext();
        	}
        });
        
        specButton.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		ycrd = ycrd - p2offset;
        		
        		xcrd = (int)(1000.0f*xcrd/screenWidth);
        		ycrd = (int)(1000.0f*ycrd/(screenHeight-(p2offset + p2offset)));
        		
        		if(xcrd > 1000) xcrd = 1000;
        		if(ycrd > 1000) ycrd = 1000;
        		if(xcrd < 0) xcrd = 0;
        		if(ycrd < 0) ycrd = 0;
        		
        		if(xcrd > 1000) xcrd = 1000;
        		int pixel = specmap.getPixel(xcrd, ycrd);
        		redval.setText(Integer.toString(Color.red(pixel)));
        		greenval.setText(Integer.toString(Color.green(pixel)));
        		blueval.setText(Integer.toString(Color.blue(pixel)));
        		updatePickerColour();
        	}
        });
    }
	
    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
    	xcrd = (int) event.getX();
    	ycrd = (int) event.getY();
    	return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_colour, menu);
        return true;
    }
    
}
