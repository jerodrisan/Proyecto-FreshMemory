package com.jesusmanuelrodriguez.freshmemory;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SonidosSimon {
	
	private Context contexto;
	private SoundPool sndPool;
	private float rate = 1.0f;
	private float masterVolume = 1.0f;
	private float leftVolume = 1.0f;
	private float rightVolume = 1.0f;
    private float balance = 0.5f;
	
	public SonidosSimon(Context contexto){
		this.sndPool = new SoundPool(9, AudioManager.STREAM_MUSIC, 0);
		this.contexto=contexto;		
	}
	
	public int load (int resourceId){		
		return sndPool.load(contexto, resourceId, 1);		
	}
	
	//ese valor devuelto lo podemos usar posteriormente por ejemplo para pausar un sonido.
	public int play(int soundId){ //soundId es el sonido devuelto por load
		return sndPool.play(soundId, leftVolume, rightVolume, 1, 0, rate);
	}
	
	 public void stop(int soundID){
		 sndPool.stop(soundID);	
		
	    }
	
	// Set volume values based on existing balance value
	public void setVolume(float vol){
		masterVolume = vol;
		if(balance < 1.0f){
			leftVolume = masterVolume;
			rightVolume = masterVolume * balance;
		}
		else{
			rightVolume = masterVolume;
			leftVolume = masterVolume * ( 2.0f - balance );
		}
	}

	public void setSpeed(float speed){
		rate = speed;
		// Speed of zero is invalid 
		if(rate < 0.01f)
			rate = 0.01f;

		// Speed has a maximum of 2.0
		if(rate > 2.0f)
			rate = 2.0f;
	}

	public void setBalance(float balVal){
		balance = balVal;
		// Recalculate volume levels
		setVolume(masterVolume);
	}
	// Free ALL the things!
	public void unloadAll(){
		sndPool.release();		
	}
	
	
}
