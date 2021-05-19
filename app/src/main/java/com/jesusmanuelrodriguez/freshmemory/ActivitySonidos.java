package com.jesusmanuelrodriguez.freshmemory;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ActivitySonidos extends Activity{
	
	MediaPlayer click1Sound,click2Sound, estrellitasSound,
	aplausosSound1, OhhSounds1, mp2, mp3, mp4, crowdSound, musicafondo,
	prueba1, prueba2, prueba3, prueba4, prueba5, prueba6, prueba7, prueba8, prueba9, prueba10;
	
	Button botonclick;
	SoundPool click1;
	int  click11;
	AssetFileDescriptor afd;
	int sonido = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activitysonidos);		
		//System.out.println("Ejecutada activity nueva");
		Log.e("SONIDOS", "INICIADO");
		
		//setUpSounds(); //Configuracion de todos los sonidos
		//setUpSounds2();
		setUpSounds3();		
		
		botonclick = (Button)this.findViewById(R.id.buutonsoniduu);
		botonclick.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(ActivitySonidos.this, ActivitySonidos.class);
				ActivitySonidos.this.startActivity(i);		
				
				//crearSonido(R.raw.applau22).release();
				ActivitySonidos.this.finish();
			}
		});
		
	}
	public void setUpSounds(){ //wav
		//Sonidos:
				musicafondo = MediaPlayer.create(this, R.raw.prueba53);		
				//musicafondo.start();
				click1Sound = MediaPlayer.create(this, R.raw.keyboardtypesngl);
				click1Sound.start();
				click2Sound = MediaPlayer.create(this, R.raw.keyboardtypesngl);
				estrellitasSound = MediaPlayer.create(this, R.raw.estrellitas);
				//estrellitasSound.start();
				aplausosSound1 = MediaPlayer.create(this, R.raw.applau22);							
				OhhSounds1 = MediaPlayer.create(this, R.raw.ohh1);				
				mp2=MediaPlayer.create(this, R.raw.sonidoplay);			
				mp3 = MediaPlayer.create(this, R.raw.button54);	
				//mp3.start();
				mp4 = MediaPlayer.create(this, R.raw.latchmetalclick1);							
				crowdSound = MediaPlayer.create(this, R.raw.crwdbugl);				
	}
	
	
	
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		/*
		musicafondo.release();
		click1Sound.release();
		click2Sound.release();
		estrellitasSound.release();
		aplausosSound1.release();
		OhhSounds1.release();
		mp2.release();
		mp3.release();
		mp4.release();
		crowdSound.release();
		*/
		
		crearSonido(R.raw.ohh1).release();
	}
	
	
	
	public void setUpSounds3(){
		/*
		click1 = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		try{
			//afd = getAssets().openFd("keyboardtypesngl.WAV");		
			afd = getResources().openRawResourceFd(R.raw.applau22);		
			click11 = click1.load(afd,0);
		    afd.close();
		} catch (IOException e) {
		    e.printStackTrace();
		        }
		//PARA REPRODUCIRLO EN BUCLE ES
		if(click11 != -1){ // AQUI COMPRUEBA SI HA SIDO CARGADO BIEN
			click1.play(click11,0.5f, 0.5f, 1, 0, 1);
		}
		//else{
			//Toast.makeText(this, "No cargado ", Toast.LENGTH_SHORT).show();
		//}
		*/
		
		
	}
	
	public SoundPool crearSonido(int id){		
		SoundPool soundpool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		
		try{	
			AssetFileDescriptor asset = getAssets().openFd("applau22.WAV");
			//AssetFileDescriptor asset = getResources().openRawResourceFd(id);		
			sonido = soundpool.load(asset,0);			
		    asset.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		if(sonido != -1){ // AQUI COMPRUEBA SI HA SIDO CARGADO BIEN
			return soundpool;			
		}
		return null;
	}
	
	public void boton1111(View v){
		crearSonido(R.raw.ohh1).setOnLoadCompleteListener(new OnLoadCompleteListener(){
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				crearSonido(R.raw.ohh1).play(sonido,1, 1, 1, 0, 1);				
			}			
		});		
	}
	
	public void boton2222(View v){
		click2Sound.start();
	}
	
	public void boton3333(View v){
		estrellitasSound.start();
	}
	
	public void boton4444(View v){
		aplausosSound1.start();
	}
	
	public void boton5555 (View v){
		mp2.start();
	}
	public void boton6666 (View v){
		mp3.start();
	}
	
	public void boton7777(View v){
		crowdSound.start();
	}
}
