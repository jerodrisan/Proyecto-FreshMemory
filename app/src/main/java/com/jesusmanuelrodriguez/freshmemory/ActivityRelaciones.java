package com.jesusmanuelrodriguez.freshmemory;

import java.util.Timer;
import java.util.TimerTask;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
*/
import com.jesusmanuelrodriguez.freshmemory.R;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.support.v4.app.FragmentActivity;
//import android.widget.GridLayout;
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ActivityRelaciones extends FragmentActivity{//Ver diferencia entre FragmentActivity y Activity: http://stackoverflow.com/questions/10609268/difference-between-fragment-and-fragmentactivity

	Context contexto;	
	int viewactual; 
	LinearLayout linear;
	ImageView imagenactual, imagenactual2, imagePreview;
	RadioGroup rg;
	RadioButton rb43, rb54, rb74, rb85;
	String clicado;
	Button botonPlay;
	MediaPlayer mp1, mp2;
	int MAX_VOLUME = 100; //volumen m�ximo de referencia
	int previews [] = {R.drawable.saample11, R.drawable.saample12, R.drawable.saample13, R.drawable.saample14};
	//AdView adView;
	SharedPreferences soundPrefs;
	SharedPreferences.Editor editor;
	public static final String SONIDO = "sonido";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_relaciones);		
		//Modo pantalla completa:
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		 //Preferencias para el sonido. Cuando creamos la actividad , por defecto el sonido estar� activado y asi lo guardaremos 
		soundPrefs = this.getSharedPreferences(SONIDO, 0);
		editor = soundPrefs.edit();
		editor.putBoolean("sonido", soundPrefs.getBoolean("sonido", true)); 
		editor.commit();
		
		//publicidad
		/*
		adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		*/
		//Peque�a animacion boton play
		botonPlay = (Button)this.findViewById(R.id.boton_playrelaciones);
		AlphaAnimation anim = new AlphaAnimation(0.7f, 1);
		anim.setDuration(1000);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setRepeatMode(Animation.REVERSE);
		botonPlay.setAnimation(anim);
		
		contexto = this;
		
		imagePreview = (ImageView)this.findViewById(R.id.imagenpreviewRelaciones);
		
		rg = (RadioGroup)this.findViewById(R.id.radiogrupo2);		
		rb43 = (RadioButton)this.findViewById(R.id.radiodif11);
		rb54= (RadioButton)this.findViewById(R.id.radiodif22);
		rb74 = (RadioButton)this.findViewById(R.id.radiodif33);
		rb85 = (RadioButton)this.findViewById(R.id.radiodif44);
		
		//creamos un bucle solo para poner sonido al click de los elementos del radiogrupo
		for (int i=0; i<rg.getChildCount();++i){
			final RadioButton rboton = (RadioButton)rg.getChildAt(i);
			rboton.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					setImagePreview();
					if(soundPrefs.getBoolean("sonido", true))
						//mp1.start();
						ActivityModosSeleccion.soundClick1.start();
				}
			});
		}
		
		//Tratamiento de los elementos del grid:
		final GridLayout grid = (GridLayout)this.findViewById(R.id.idGrid2);
		
		LinearLayout first = (LinearLayout)grid.getChildAt(0);
		ImageView im1 =(ImageView)first.getChildAt(0);	
		ImageView im2 =(ImageView)first.getChildAt(1);
		imagenactual=im1;
		imagenactual2=im2;
		
		clicado = grid.getChildAt(0).getTag().toString();
		setClicado(clicado);
		
		for (int i=0; i<grid.getChildCount();++i){
			
			final LinearLayout linear = (LinearLayout)grid.getChildAt(i);			
			linear.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View view) {	
					setImagePreview();
					if(soundPrefs.getBoolean("sonido", true))
						//mp1.start();
						ActivityModosSeleccion.soundClick1.start();
					ImageView aux, aux2;
					ImageView v1 = (ImageView)linear.getChildAt(0);
					ImageView v2 = (ImageView)linear.getChildAt(1);
					//Usamos un auxiliar para asignarsela a la imagen actual
					aux = v1;
					aux2= v2;					
					//Solo nos interesa el estado Gone. En caso de que pulsemos un boton cuyo estado sea Visible
					//no se hara nada
					if( v1.getVisibility()==View.GONE){
						v1.setVisibility(View.VISIBLE);
						v2.setVisibility(View.GONE);						
						imagenactual.setVisibility(View.GONE);
						imagenactual2.setVisibility(View.VISIBLE);												
					}					
					//De esta forma ya teneoms guardada cual es la imagenactual que esta activada como visible
					//para usarla posteriormente. 
						imagenactual=aux;
						imagenactual2 = aux2;			
						clicado = view.getTag().toString();
						setClicado(clicado);
					}				
			});			
		}//fin for				
		
	}//fin onCreate()
	
	
	
	@Override
	protected void onPause() {		
		super.onPause();
		//adView.pause();
		/*
		mp1.release();
		new Timer().schedule(new TimerTask(){		
			@Override
			public void run() {	
				//soundAnimator.unloadAll();
				mp2.release();
			}			
		}, mp2.getDuration());
		*/
	}



	@Override
	protected void onResume() {		
		super.onResume();
		//adView.resume();
		//Sonidos botones
		/*
		mp1=MediaPlayer.create(this, R.raw.button54);
		mp1.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		mp2=MediaPlayer.create(this, R.raw.keyboardtypesngl);
		mp2.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		*/
	}
	//funcion para establecer volumen customizado		
	public float setCustomVolumen(int soundVolume){  //volumen que queremos poner (la mitad del total de volumen) o  lo que queramos!!!
		return (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME))); // ver:http://stackoverflow.com/questions/5215459/android-mediaplayer-setvolume-function
	}


	private void setClicado (String clicado){
		this.clicado= clicado;
	}
	
	private String getClicado(){
		return this.clicado;
	}
	
	private void setImagePreview(){
		int rb = rg.getCheckedRadioButtonId();		
		if (rb == R.id.radiodif11) {
			setImageResource(0);
		} else if (rb == R.id.radiodif22) {
			setImageResource(1);
		} else if (rb == R.id.radiodif33) {
			setImageResource(2);
		} else if (rb == R.id.radiodif44) {
			setImageResource(3);
		}
	}
	
	private void setImageResource(int i){
		
		if(getClicado().equals("tema7")|| getClicado().equals("tema8")|| getClicado().equals("tema9")||
				getClicado().equals("tema10")|| getClicado().equals("tema11") || getClicado().equals("tema12") || getClicado().equals("tema13")){
			 imagePreview.setImageResource(previews[i]);
		}		
	}
	
	
	public void botonPlayRelaciones (View v){
		if(soundPrefs.getBoolean("sonido", true))
			//mp2.start();
			ActivityModosSeleccion.soundPlay.start();
		int rb = rg.getCheckedRadioButtonId();		
		Intent i = new Intent(contexto, ActivityBoard.class);
		i.putExtra("modo", "relaciones");
		
		if (rb == R.id.radiodif11) {
			String[] str43 = rb43.getTag().toString().split("x");
			i.putExtra("numFilas", Integer.parseInt(str43[0]));
			i.putExtra("numColum", Integer.parseInt(str43[1]));
			i.putExtra("nivel", "nivel1"); 	//variable usada para poner el nivel el bloque de prefs y luego sacarla 
			i.putExtra("tematica", clicado);
			if(clicado.equals("tema7"))  startActivity(i);
			if(clicado.equals("tema8"))	startActivity(i);
			if(clicado.equals("tema9"))  startActivity(i);
			if(clicado.equals("tema10")) startActivity(i);
			if(clicado.equals("tema11"))  startActivity(i);
			if(clicado.equals("tema12"))  startActivity(i);
			if(clicado.equals("tema13"))  startActivity(i);
		} else if (rb == R.id.radiodif22) {
			String[] str54 = rb54.getTag().toString().split("x");
			i.putExtra("numFilas", Integer.parseInt(str54[0]));
			i.putExtra("numColum", Integer.parseInt(str54[1]));
			i.putExtra("tematica", clicado);
			i.putExtra("nivel", "nivel2");
			if(clicado.equals("tema7"))  startActivity(i);
			if(clicado.equals("tema8"))	startActivity(i);
			if(clicado.equals("tema9"))  startActivity(i);
			if(clicado.equals("tema10")) startActivity(i);
			if(clicado.equals("tema11"))  startActivity(i);
			if(clicado.equals("tema12"))  startActivity(i);
			if(clicado.equals("tema13"))  startActivity(i);
		} else if (rb == R.id.radiodif33) {
			String[] str74 = rb74.getTag().toString().split("x");
			i.putExtra("numFilas", Integer.parseInt(str74[0]));
			i.putExtra("numColum", Integer.parseInt(str74[1]));
			i.putExtra("tematica", clicado);
			i.putExtra("nivel", "nivel3");
			if(clicado.equals("tema7"))  startActivity(i);
			if(clicado.equals("tema8"))	startActivity(i);
			if(clicado.equals("tema9"))  startActivity(i);
			if(clicado.equals("tema10")) startActivity(i);
			if(clicado.equals("tema11"))  startActivity(i);
			if(clicado.equals("tema12"))  startActivity(i);
			if(clicado.equals("tema13"))  startActivity(i);
		} else if (rb == R.id.radiodif44) {
			String[] str85 = rb85.getTag().toString().split("x");
			i.putExtra("numFilas", Integer.parseInt(str85[0]));
			i.putExtra("numColum", Integer.parseInt(str85[1]));
			i.putExtra("tematica", clicado);
			i.putExtra("nivel", "nivel4");
			if(clicado.equals("tema7"))  startActivity(i);
			if(clicado.equals("tema8"))	startActivity(i);
			if(clicado.equals("tema9"))  startActivity(i);
			if(clicado.equals("tema10")) startActivity(i);
			if(clicado.equals("tema11"))  startActivity(i);
			if(clicado.equals("tema12"))  startActivity(i);
			if(clicado.equals("tema13"))  startActivity(i);
		}
		
	}

}
