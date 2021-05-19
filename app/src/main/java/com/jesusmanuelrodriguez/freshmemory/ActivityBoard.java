
package com.jesusmanuelrodriguez.freshmemory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridLayout.LayoutParams;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
*/
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.ObjectAnimator;
//import android.widget.GridLayout;


public class ActivityBoard extends Activity{

	MediaPlayer musicaFondo;
	SharedPreferences classicScores, relacionesScores;
	static final String HighScoresLevelClassic = "scoresclassic";	
	static final String HighScoresLevelRelaciones ="scoresrelaciones";
	
	static final int time = 500; //tiempo que dura la animacion 
	static final int timeAnimationLong = 3000; //Tiemmpo que tarda en ocultarse la animacion
	ArrayList<String> lista;
	ArrayList<String> listaRelac,listaPalab;
	ArrayList<Integer> indice;
	
	ArrayList<LinearLayout> parlinear; 
	//bundles que vienen de la actividad de configuracion:
	int numFilas ;
	int numCol ;
	String tematica;
	String nivel;
	String modo;
	
	boolean  pausado=false, gameOver= false;
	
	int ids=0,ids1=0,ids2=0;
	int numClicks = 0;
	int aciertos = 0; //Contador de aciertos cada vez que se encuentra una pareja
	int combo=1; 	// Contador de combos, cada vez que se encuentran dos o mas parejas consecutivas
	int comboAux=0;
	int totalCombos; //total de combos
	int i=0; 		//Contador para sistema de puntuacion
	float total=0, sumaTotal=0;
	
	long timeWhenStopped = 0;
		
	Context contexto; //pasamos el contexto a la clase creada. 	
	TextView textClicks, textAciertos;
	Chronometer crono;
	
	DisplayMetrics dm;
	int gap, anchoIm, altoIm, publi;
	ImageView ImageViewSonido, imageViewPausa;
	GridLayout gridlay;	
	LinearLayout linearUp;	
	Bundle extras;
	int MAX_VOLUME = 100; //volumen m�ximo de referencia	
	//AdView adView;
	SharedPreferences soundPrefs, conexionScores;
	SharedPreferences.Editor editor, editorScores;
	public static final String SONIDO = "sonido";
	public static final String CONEXION = "conexion";
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
		
		setContentView(R.layout.activity_board);
		//requestWindowFeature(Window.FEATURE_NO_TITLE); no va por codigo??  O en el manifest: android:theme="@android:style/Theme.NoTitleBar"/>
				
		//Ponemos la pantalla full screen:
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		 //Preferencias para el sonido. Cuando creamos la actividad , por defecto el sonido estar� activado y asi lo guardaremos 
		soundPrefs = this.getSharedPreferences(SONIDO, 0);		
		editor = soundPrefs.edit();
		editor.putBoolean("sonido", soundPrefs.getBoolean("sonido", true)); 
		editor.commit();
											
		//Preferencias conexion 
		conexionScores = this.getSharedPreferences(CONEXION, 0);
		
		//Preferencias puntuaciones
		classicScores = getSharedPreferences(HighScoresLevelClassic, 0);	
		relacionesScores =	 getSharedPreferences(HighScoresLevelRelaciones, 0);
		
		//publicidad
		/*
		adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		*/
		//Pillamos los datos de la actividad de la pantalla principal para configurar el juego:		
		getExtras();
		
		//Ponemos fondo dependiendo de si usamos relaciones o clasico
		RelativeLayout relativ = (RelativeLayout)this.findViewById(R.id.linlayout);
		if(modo.equals("clasico")){
			relativ.setBackgroundResource(R.drawable.fondoclasico);
		}else if(modo.equals("relaciones")){
			relativ.setBackgroundResource(R.drawable.fondorelaciones);
		}
						
		imageViewPausa = (ImageView)this.findViewById(R.id.classicmode_botonpause);		
		ImageViewSonido = (ImageView)this.findViewById(R.id.classicmode_botonsonido);
		if(soundPrefs.getBoolean("sonido", true))
			ImageViewSonido.setImageResource(R.drawable.boton_sonidoonfondo);
		else
			ImageViewSonido.setImageResource(R.drawable.boton_sonidoofffondo);
		
					
		setUpSounds();
		/*
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {		
				if(soundPrefs.getBoolean("sonido", true))
					musicafondo.start();		
			}			
		}, 1000);
		*/
		
		gridlay = (GridLayout)findViewById(R.id.gridpadre);	
		linearUp = (LinearLayout)findViewById(R.id.mainlinear1);
		
		// array para los pares que creamos. Este array solo tendra dos posiciones 0 y 1
		parlinear = new ArrayList<LinearLayout>(); 
		contexto = this; 		
		//creamos pares de valores iguales para asignarlos a las ids de los LinearLayouts que crearemos en Modo clasico
		lista = new ArrayList<String>();
		for (int i = 0; i< (numFilas*numCol)/2; ++i){			
			lista.add(String.valueOf(i)); 
			lista.add(String.valueOf(i));			
		}	
		Collections.shuffle(lista); // desordenamos los pares para que luego los asignemos de forma aleatoria a los LinearLayouts	
		
		//Valores para caso de relaciones
		listaRelac=new ArrayList<String>();
		listaPalab=new ArrayList<String>();	
		indice = new ArrayList<Integer>();
		
		for (int i = 0; i< (numFilas*numCol)/2; ++i){			
			listaRelac.add(String.valueOf(i)); 
			listaPalab.add(String.valueOf(i));		
			indice.add(0);
			indice.add(1);
		}	
		Collections.shuffle(listaRelac);
		Collections.shuffle(listaPalab);
		Collections.shuffle(indice);
		
		
	
		//Tomamos las dimensiones de la pantalla: 		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//Reservamos publicidad en la parte de abajo, por ejemplo una x parte de la altura en pixeles del panel
		//(sin tener en cuenta el espacio del tiempo, intentos de arriba)
		//Ojo por defecto se reservo espacio en un principio para publi en relacion: publi = dm.heightPixels/6;
		publi = dm.heightPixels/6;				
		
		//Usamos metodo setGaps aunque se puede usar el de abajo:
		setGaps(numCol,numFilas);
		/*
		 Si hacemos este metodo no queda ese peque�o espacio a la derecha como en el metodo setGaps
		 gap =getGap(numCol);
		//Espacio a restar relativo a la suma de los huecos entre fichas tanto horiz como vert:
		int sumaHorizGap = (numCol+1)*gap;
		int sumaVertGap = (numFilas+1)*gap;		
		//Alto y ancho de cada uno de las fichas dependiendo del numero de col y filas seleccionada:
		anchoIm = (dm.widthPixels-sumaHorizGap)/numCol ;		
		altoIm = ((dm.heightPixels-publi) - sumaVertGap)/numFilas;
		*/			
				
		for (int i =0; i<numFilas;++i){
			for (int j=0; j<numCol; ++j){				
				LinearLayout linear = new LinearLayout(this);
				AutoResizeTextView ima1 = new AutoResizeTextView(this);	
				AutoResizeTextView ima2 = new AutoResizeTextView(this);									
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();
				//Podiamos haberlo hecho en vez de ids , con tags que seria quizas menos problematico 
				
				if(modo.equals("relaciones")){				
				  	if (indice.get(ids)==0){	//imagenes								  		  
				  		linear.setId(Integer.parseInt(listaPalab.get(ids1)));				  		
				  		ima2.setBackgroundResource(getTematicaRelac()[0][linear.getId()]);				  		
				  		++ids1;
				  					
				  	}if(indice.get(ids)==1){	//palabras			
				  		linear.setId(Integer.parseInt(listaRelac.get(ids2)));
				  		ima2.setBackgroundResource(R.drawable.agris);		
				  		if(tematica.equals("tema11")){ //si es el tema 11 de sumas ponemos en generador de sumas
				  			ima2.setText(Imagenes.genNumAleatorios()[linear.getId()]);
				  		}else{
				  			ima2.setText(getTematicaRelac()[1][linear.getId()]);
				  		}	
				  		ima2.setTextColor(contexto.getResources().getColor(R.color.black));				  		
				  		//ima2.setTextSize(30);
				  		ima2.resizeText();
				  		//ima2.setMaxLines(1);
				  		ima2.setTypeface(Typeface.DEFAULT_BOLD);
				  		ima2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				  		//ima2.setImageResource(Imagenes.banderasRelaciones[linear.getId()]);
				  		++ids2;				  		
				  	}
				}if(modo.equals("clasico")){
					linear.setId(Integer.parseInt(lista.get(ids)));	
					ima2.setBackgroundResource(getTematicaClasico()[linear.getId()]);
					
				}
				
				params.height = altoIm;
				params.width =anchoIm;
				params.columnSpec = GridLayout.spec(j);
				params.rowSpec =GridLayout.spec(i);					
				if(j==numCol-1)
					params.setMargins(gap, gap, gap, 0);					
				else
					params.setMargins(gap, gap, 0, 0);				
				linear.setLayoutParams(params);									
				
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(anchoIm,altoIm);	
			//	LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
											
				//ponemos un paddig para el icono de informacion:
				ima1.setPadding(20, 20, 20, 20); 
				ima1.setLayoutParams(params2);				
				//ima1.setBackgroundResource(getTematicaClasico()[linear.getId()]);
				ima1.setBackgroundResource(R.drawable.iinterr);				
				//ima1.setBackgroundResource(R.drawable.interro);	
				
				ima1.setVisibility(View.VISIBLE);					
				linear.addView(ima1,0);															 				
				
				ima2.setLayoutParams(params2);				
				//No pondremos el padding para imagenes peque�as queda mucho mejor 
				//ima2.setPadding(10, 10, 10, 10);										
				//ima2.setBackgroundResource(R.drawable.agris);
				ima2.setVisibility(View.GONE);	
				 //Ponemos un tag a ima2 para que en caso de pulsar dos veces sobre la misma imagen no contabilice como acertado				
				ima2.setTag(String.valueOf(ids));
				linear.addView(ima2, 1);
																
				linear.setOnClickListener(new linearListener(linear )); 
				gridlay.addView(linear);				
				++ids;		
			}				
		}	

		crono = (Chronometer) findViewById(R.id.cronometro);		
		crono.start();		
		textClicks= (TextView)findViewById(R.id.textClicks);
		textAciertos = (TextView)findViewById(R.id.textEncontradas);
		
		//Conectamos en la activity con google play services
		//if(conexionScores.getBoolean(CONEXION, true))
			//beginUserInitiatedSignIn();
					
	}// FIN ONCREATE
	/*
	//METODOS PARA MARCADORES SCORES	
	
	@Override
	public void onSignInFailed() {
		//Mejor no ponemos nada
		//Toast.makeText(this, "Offline", Toast.LENGTH_LONG).show();		
	}

	@Override
	public void onSignInSucceeded() {			
		Toast.makeText(this, getResources().getString(R.string.you_are_connected), Toast.LENGTH_LONG).show();
	}
	
	public void subirPuntuacion(){		
		//solo subimos puntuacion si estamos conectados:
		if(isSignedIn()){
		  if(modo.equals("clasico")){	
			if(numFilas==4 & numCol==3){	
				String[] savedScore = classicScores.getString("nivel1", "").split("\\|");
				String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));	
				Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_classic_4x3), Integer.parseInt(punt));				
			}else if(numFilas==6 & numCol==4){
				String[] savedScore = classicScores.getString("nivel2", "").split("\\|");
				String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));	
				Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_classic_6x4),  Integer.parseInt(punt));
				//Desbloqueamos logro:
				if((100*(numFilas*numCol/2)/numClicks)>=50){
					Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_pointer));	
				}
			}else if(numFilas==8 & numCol==5){
				String[] savedScore = classicScores.getString("nivel3", "").split("\\|");
				String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));	
				Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_classic_8x5), Integer.parseInt(punt));
			}else if(numFilas==10 & numCol==7){
				String[] savedScore = classicScores.getString("nivel4", "").split("\\|");
				String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));	
				Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_classic_10x7), Integer.parseInt(punt));
			}
		  }else if(modo.equals("relaciones")){
			    if(numFilas==4 & numCol==3){	
			    	String[] savedScore = relacionesScores.getString("nivel1", "").split("\\|");
					String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));
					Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_connections_4x3),  Integer.parseInt(punt));
				}else if(numFilas==5 & numCol==4){
					String[] savedScore = relacionesScores.getString("nivel2", "").split("\\|");
					String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));
					Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_connections_5x4),  Integer.parseInt(punt));
					if((100*(numFilas*numCol/2)/numClicks)>=50){
						Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_pointer));	
					}
				}else if(numFilas==7 & numCol==4){
					String[] savedScore = relacionesScores.getString("nivel3", "").split("\\|");
					String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));
					Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_connections_7x4),  Integer.parseInt(punt));
				}else if(numFilas==8 & numCol==5){
					String[] savedScore = relacionesScores.getString("nivel4", "").split("\\|");
					String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));
					Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_connections_8x5),  Integer.parseInt(punt));
				}
		  }
		}
	}
	//Para ver marcadores , ver boton botonGlobalScores
	*/
	
	
	//---FIN GOOGLE PLAY GAMES
	
	public void setUpSounds (){
		
		musicaFondo = MediaPlayer.create(this, R.raw.prueba53);			
		musicaFondo.setLooping(true);
		musicaFondo.setVolume(setCustomVolumen(70), setCustomVolumen(70));	
		/*
		click1Sound = MediaPlayer.create(this, R.raw.keyboardtypesngl);
		click1Sound.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		
		click2Sound = MediaPlayer.create(this, R.raw.keyboardtypesngl);
		click2Sound.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		
		click3Sound = MediaPlayer.create(this, R.raw.ropecreck);
		click3Sound.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		
		aplausos = MediaPlayer.create(this, R.raw.crwdbugl);
		aplausos.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		
		sonidoCombo = MediaPlayer.create(this, R.raw.sonidobutton25); //sonido para combos
		sonidoCombo.setVolume(setCustomVolumen(80), setCustomVolumen(80));
		
		musicafondo = MediaPlayer.create(this, R.raw.prueba53);				
		musicafondo.setLooping(true);
		musicafondo.setVolume(setCustomVolumen(70), setCustomVolumen(70));	
		
		mp3 = MediaPlayer.create(this, R.raw.button54);	//Sonido que se pasara a MarcadoresClasico.java
		mp3.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		*/
	}	
	
	
	
	public float setCustomVolumen(int soundVolume){  //volumen que queremos poner (la mitad del total de volumen) o  lo que queramos!!!
		return (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME))); // ver:http://stackoverflow.com/questions/5215459/android-mediaplayer-setvolume-function
	}
		
		
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		//adView.destroy();
		musicaFondo.release();
		
	}
	
	@Override //si pulsamos el boton Home hay que interrumpir el juego 
	protected void onPause() {				
		super.onPause();		
		//adView.pause();
		pausado=true;		
		timeWhenStopped = crono.getBase() - SystemClock.elapsedRealtime(); //Ver http://stackoverflow.com/questions/5594877/android-chronometer-pause
		crono.stop();	
		
		if(soundPrefs.getBoolean("sonido", true) && !gameOver)
			musicaFondo.pause();				
	}
		

	@Override //si volvemos de tener la actividad desde el foreground
	protected void onResume() {		
		super.onResume();
		//adView.resume();
		pausado=false;		
		crono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
		crono.start();
		if(soundPrefs.getBoolean("sonido", true)){						
			musicaFondo.start();	
		}		
		
	}

	//Pulsacion del boton back durante el juego.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	      if(!gameOver){		    	
	        AlertDialog.Builder alertbox = new AlertDialog.Builder(contexto);
	        alertbox.setIcon(R.drawable.boton_tick);
	        alertbox.setTitle(R.string.deseasalir);
	        alertbox.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) { 	            	      			
	        			gameOver=true;	    
	        			//Solo pondremos puntuacion si terminamos el juego	        			
	        			if(soundPrefs.getBoolean("sonido", true)){
	        				musicaFondo.release();	     
	        				
	        			}	        				   			
	    				finish();			        		
	            }
	        });
	        alertbox.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {}
	          
	        });
	        	alertbox.show();
	        	
	      	}else{	      
	      	//POnemos la puntuacion si volvemos atras. Dependiendo del nivel asignamos en el bloque de preferencias HighScoresLevelClassic o HighScoresLevelRelaciones	      		
	       // un String de nivel u otro ya que en cada String esta el nivel que sale mostrado en los marcadores:
	      		/*
	      		if(modo.equals("clasico"))
	      			new ScoresHigh(contexto, HighScoresLevelClassic ,scoreSystem(), nivel).setHighScores() ;
	      		if(modo.equals("relaciones"))
	      			new ScoresHigh(contexto, HighScoresLevelRelaciones ,scoreSystem(), nivel).setHighScores() ;
	      		*/
	      		finish();		      				      	
	      	}
	      }	  	
	  return super.onKeyDown(keyCode, event);	  
	}
	
	
	
	

	public void botonSonido (View v){
	    if(soundPrefs.getBoolean("sonido", true)){	    	
	    	editor.putBoolean("sonido", false);
	    	editor.commit();
	    	ImageViewSonido.setImageResource(R.drawable.boton_sonidoofffondo);
	    	musicaFondo.pause();
	    }else{	    	
	    	editor.putBoolean("sonido", true);
	    	editor.commit();
	    	ImageViewSonido.setImageResource(R.drawable.boton_sonidoonfondo);
	    	musicaFondo.start();
	    }
	}
	
	public void botonPause(View v) {
		if (!pausado){
			pausado=true;
			imageViewPausa.setImageResource(R.drawable.boton_reanudar);
			//Para API=>11 :gridlay.setAlpha(0.5f); Para <11 usamos libreria NineOldAndroids
			ViewHelper.setAlpha(gridlay, 0.5f);
			timeWhenStopped = crono.getBase() - SystemClock.elapsedRealtime(); //Ver http://stackoverflow.com/questions/5594877/android-chronometer-pause
			crono.stop();				
			if(soundPrefs.getBoolean("sonido", true))
				musicaFondo.pause();
			
			
		}else{
			pausado=false;
			imageViewPausa.setImageResource(R.drawable.boton_pausa);
			//gridlay.setAlpha(1); pero al usar nineoldandroids ponemos:
			ViewHelper.setAlpha(gridlay, 1f);
			crono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
			crono.start();				
			if(soundPrefs.getBoolean("sonido", true))
				musicaFondo.start();
			
		}
	}
	
	//Metodo que calcula los huecos de forma proporcional
	public void setGaps (int numC, int numF){
		
		//Tomamos como referencia de distancias un tablero de 3colx4filas:
		final int gapRefer =  dm.widthPixels/62; // = 6dp = 11.61 pixels
		final int sumHorizGapRef =  (3+1)*gapRefer; // Para 3 columnas calculamos 4 huecos y sumamos el total de los 4. (46)
		final int sumVertGapRef = (4+1)*gapRefer; //58
		
		final int sumAnchoImRef = dm.widthPixels-sumHorizGapRef ; //Espacio total de las fichas juntas restando la suma de los huecos horizontales
		final int sumAltoImRef = (dm.heightPixels-publi) - sumVertGapRef; //Idem para filas pero restando la publicidad
			
		gap = sumHorizGapRef/(numC+1); // hueco dependiendo del numero de col
		anchoIm = (sumAnchoImRef/numC) ; //ancho de cada ficha dependiendo del num col	
		altoIm = (sumAltoImRef/numF);	//idem para filas
		//comprobacion de que coincide:
		//System.out.println("total ancho "+dm.widthPixels+ " suma gap horiz :" + sumHorizGapRef+	" suma fichas ancho :"+ sumAnchoImRef);		
	}
	//pillamos el hueco entre fichas proporcional dependiendo del tama�o de las fichas
	//En este caso tomamos los huecos un poco a ojo . Mejor el metodo setGaps ya que establece las distancias de forma proporcional	
	public int getGap(int numColumnas){		
		int gap=0;
		final int dpref =720/377;		
		switch (numColumnas){
		case 3:
			gap=dm.widthPixels/62; // = 6dp = 11.61 pixels
			break;
		case 4:
			gap=(dm.widthPixels/62)-(dpref*4);			
			break;
		case 5:
			gap=(dm.widthPixels/62)-(dpref*4);			
			break;
		case 6:
			gap=(dm.widthPixels/62)-(dpref*6);
			break;
		}
		return gap;		
	}
	
	
	//TEMATICA MODO CLASICO
	public int[] getTematicaClasico(){	
		
		if(tematica.equals("paises"))
			return Imagenes.banderas;
		if(tematica.equals("deportes"))
			return Imagenes.deportes;
		if(tematica.equals("smiles"))
			return Imagenes.emoticonos;
		if(tematica.equals("cosas"))
			return Imagenes.cosas;
		if(tematica.equals("marcas"))
			return Imagenes.marcas;	
		if(tematica.equals("objetos"))
			return Imagenes.objetos;
			
		return null;
	}
	
	//TEMATICA MODORELACIONES
	public int[][] getTematicaRelac(){	
		if(tematica.equals("tema7")){
			int [][] img = { Imagenes.deportes, Imagenes.deportespalabra};			
			return img;
		}
		if(tematica.equals("tema8")){
			int [][] img = { Imagenes.profesiones, Imagenes.profesionesPalabras};
			return img;
		}			
		if(tematica.equals("tema9")){			
			int [][] img = { Imagenes.varios, Imagenes.variosPalabras};
			return img;
		}		
		if(tematica.equals("tema11")){
			int [][] img = { Imagenes.numeros, Imagenes.numerospalabras};
			return img;
		}			
		if(tematica.equals("tema12")){
			int [][] img = { Imagenes.banderasRelaciones, Imagenes.banderasPalabras};
			return img;
		}
		if(tematica.equals("tema13")){
			int [][] img = { Imagenes.numerosRomanos, Imagenes.numerosRomanosPalabras};
			return img;
		}		
		return null;		
	}
	
	
	public void getExtras(){
		extras = this.getIntent().getExtras();
		modo = extras.getString("modo");		
		numFilas= extras.getInt("numFilas");		
		numCol = extras.getInt("numColum");					
		nivel = extras.getString("nivel");
		tematica= extras.getString("tematica");			
	}
	
	//creamos clase para el onclickListener de cada LinearLayout:	
		class linearListener implements View.OnClickListener{			
			
			int OK = 123456; // id que asignaremos cuando al pulsar dos linerlayouts su id sean iguales  		
			Timer timer;
			LinearLayout linear;
			AutoResizeTextView  im1, im2, im3, im4, im5, im6;   
		    ObjectAnimator goneToVisible,  visibleToGone, goneToVisible2, visibleToGone2, anim1,anim2 ;		
			
			public linearListener (LinearLayout layout){
				this.linear=layout;					
			}		
			@Override
			public void onClick(View v) {	
			if(!pausado){  
				
			  if(parlinear.size() <2){			  
								
				if(linear.getId()!=OK){		
					parlinear.add(linear);					
					
					 if(parlinear.size()==1){	
						if(soundPrefs.getBoolean("sonido", true)) ActivityModosSeleccion.soundPlay.start();
						 
						im1 = (AutoResizeTextView) parlinear.get(0).getChildAt(0);
						im2 = (AutoResizeTextView) parlinear.get(0).getChildAt(1);									
						
						girarImagen(im1, im2, 150 );	
						/* Si queremos que en la primera pulsacion se de la vuelta automaticamente usariamos
						 * este codigo y quitariamos el girarImagen2(im1,im2,time);	 que hay dentro del Listener 
						 * de la segunda pulsacion. 
						 * Pienso que es algo mas sencillo que tras la primera pulsacion no se de la vuelta 
						 * y quede la imagen visible hasta que se pulse la segunda vez, aunque visualmente 
						 * queda mas bonito. 
						 * 
						goneToVisible.addListener(new AnimatorListenerAdapter(){ //Bucle para repetir una y otra vez que se gire la imagen
								@Override
								public void onAnimationEnd(Animator animation) {								
									girarImagen(im1,im2,time);								
								}			
							});
						 	*/ 
					 }
					
					if (parlinear.size() ==2 ){	
						//Descartamos que no pulsemos la misma ficha dos veces:
						if(parlinear.get(0).getChildAt(1).getTag() != parlinear.get(1).getChildAt(1).getTag()){					
							
								if(soundPrefs.getBoolean("sonido", true))  ActivityModosSeleccion.soundPlay2.start();							
								
								im1 = (AutoResizeTextView) parlinear.get(0).getChildAt(0);
								im2 = (AutoResizeTextView) parlinear.get(0).getChildAt(1);	
								im3 = (AutoResizeTextView) parlinear.get(1).getChildAt(0);
								im4 = (AutoResizeTextView) parlinear.get(1).getChildAt(1);							
																
								girarImagen(im3, im4, 150);	
								
								new Timer().schedule(new TimerTask(){ //tiempo total operacion: 150x2 + 700 = timex2
									@Override
									public void run() {
										handler.post(new Runnable(){
											@Override
											public void run() {			
												//quitamos el interrogante 
												im1.setBackgroundResource(R.drawable.agris);			
												im3.setBackgroundResource(R.drawable.agris);												
												girarImagen2(im1, im2, 150);
												girarImagen(im3, im4, 150);
											}													
										});
									}											
								}, 700);
								
								/*
								goneToVisible.addListener(new AnimatorListenerAdapter(){ //Bucle para repetir una y otra vez que se gire la imagen
									@Override
									public void onAnimationEnd(Animator animation) {										
										im3.setBackgroundResource(R.drawable.agris);
										im1.setBackgroundResource(R.drawable.agris);
										girarImagen(im3,im4,time);	
										girarImagen2(im1, im2, time ); 								
									}			
								});										
								*/	
								
								//Si las ids son iguales se han emparejado los elementos: 	 
								if(parlinear.get(0).getId() == parlinear.get(1).getId() ){	
									// y le ponemos id = OK a los dos (lo ponemos en el final del if,
									//ya que necesitamos el id del linearlayout para saber que elemento del array se ha pulsado)	
									final int idlinear= parlinear.get(1).getId();
									comboAux++;
									if (comboAux>1){
										combo++;	
										totalCombos++;
										//Pasamos la info del combo para que aparezca en pantalla
										if(soundPrefs.getBoolean("sonido", true)) ActivityModosSeleccion.soundCombo.start();
										new ComboPantalla1(contexto,
												"Combo"+"\n    X"+String.valueOf(combo-1),
												R.id.linlayout,
												contexto.getResources().getColor(android.R.color.white), true,
												1,3,1,3,1000, R.drawable.fondo_combotransparente).run();
										//Desbloqueamos logro de los 3 combos seguidos
										/*
										if(isSignedIn())
										  if(totalCombos==3){
											Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_combo));					
										}
										*/
									}
									else
										combo=1;								
									aciertos++;//Contador de aciertos	
																		
									//Ponemos un timer para que el numero de acertados salte 2*time segundos despues de que se pulse la segunda ficha
									new Timer().schedule(new TimerTask(){
										@Override
										public void run() {											
											handler.post(new Runnable(){
												@Override
												public void run() {												
													textAciertos.setText(String.valueOf(aciertos));
													ScaleAnimation scal = new ScaleAnimation(1,2,1,2);		
													scal.setDuration(1000);
													textAciertos.startAnimation(scal);
												}											
											});										
										}									
									}, time*2);
										
									
									//new MarcadoresClasico(contexto,scoreSystem(),numClicks,	aciertos, numFilas*numCol/2, crono.getText()).run();		
									//Estableceoms un Timer para de 2*time segundos para hacer las animaciones:
									timer = new Timer(); //Pasado el tiempo 2*time se ejecuta lo que hay dentro del run					
									timer.schedule(new TimerTask(){
										int idImagen;
										@Override
										public void run() {										
											//Mostramos una tostada personalizada con la info de la foto de la pareja encontrada, puntos y aciertos/total	
											if(modo.equals("clasico")){
												 idImagen= getTematicaClasico()[idlinear];
											}else if(modo.equals("relaciones")){
												 idImagen = getTematicaRelac()[0][idlinear];
											}													
											handler.post(new TostadaClasico(contexto, idImagen, 
													scoreSystem(), aciertos, (numFilas*numCol)/2));
										
											if(soundPrefs.getBoolean("sonido", true)) ActivityModosSeleccion.soundRapeCreek.start();
																						
											handler.post(new runa(im1, im2, im3, im4, idlinear));									 
										
											parlinear.remove(1);
											parlinear.remove(0);
											
											
											//FIN DE PARTIDOA, MUESTRA DE RESULTADOS:
											if (aciertos==(numFilas*numCol)/2){	
												gameOver=true;												
												if(modo.equals("clasico"))
									      			new ScoresHigh(contexto, HighScoresLevelClassic ,scoreSystem(), nivel).setHighScores() ;
									      		if(modo.equals("relaciones"))
									      			new ScoresHigh(contexto, HighScoresLevelRelaciones ,scoreSystem(), nivel).setHighScores() ;
									      	//Subimos marcador a internet en caso de estar conectado
												/*
												if(isSignedIn()){													
													subirPuntuacion();
												}										
												*/
												if(soundPrefs.getBoolean("sonido", true)){
													musicaFondo.stop();																										
												}								
												new Timer().schedule(new TimerTask(){
														@Override
														public void run() {
															if(soundPrefs.getBoolean("sonido", true)) ActivityModosSeleccion.SoundAplausos.start();														
														}													
													}, time);
												
												crono.stop(); //paramos el crono
												//Hacemos que los marcadores salgan despues de la ultima animacion, 1 segundo despues
												new Timer().schedule(new TimerTask(){
													@Override
													public void run() {
														//handler.post(new Finpantalla1()); //Metodo elegante ViewStub
														handler.post(new MarcadoresClasico(contexto,
																scoreSystem(),numClicks,aciertos, numFilas*numCol/2, crono.getText(),totalCombos, extras,  soundPrefs.getBoolean("sonido", true)));	 
														handler.post(new Runnable(){
															@Override
															public void run() {
																linearUp.setVisibility(View.INVISIBLE);															
															}														
														});
														
													}											 
												},timeAnimationLong);
												}									 												
											}						
										}, time*2);	
									parlinear.get(0).setId(OK);
									parlinear.get(1).setId(OK); 
								}else{	//Ponemos un timer para que solo podamos pulsar dos veces en lo que dura la animacion: 4*time	
									new Timer().schedule(new TimerTask(){
										@Override
											public void run() {
												parlinear.remove(1);
												parlinear.remove(0);							
											}							
									},time*2);				
									
									combo=1; //reseteamos el combo a 1 cuando no hay mas de dos parejas encontradas consecutivas	
									comboAux=0;
																	
								}						
								numClicks++;							
								textClicks.setText(String.valueOf(numClicks));					
							
						}else {						 
							parlinear.remove(1);
						}
					}//fin if 				 
				}			
			  }	
			}
			}//fin onClickview	
			
					
			public void girarImagen( TextView imagen1, TextView imagen2, int duracion){	
				
				final TextView visibleImage;
				final TextView invisibleImage;		
				
				if (imagen1.getVisibility() == View.GONE) {
					visibleImage = imagen2;
					invisibleImage = imagen1;
				} else {
					invisibleImage = imagen2;
					visibleImage = imagen1;
				}		
				goneToVisible = ObjectAnimator.ofFloat(invisibleImage, "rotationY", -90f, 0f);
				visibleToGone = ObjectAnimator.ofFloat(visibleImage,"rotationY", 0f, 90f);	
				goneToVisible.setDuration(duracion);		
				visibleToGone.setDuration(duracion);			
				visibleToGone.start();
				
				visibleToGone.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator anim) {
									
						// cuando se acabe de ocultar, comenzar a mostrar la nueva
						visibleImage.setVisibility(View.GONE);
						invisibleImage.setVisibility(View.VISIBLE);
						goneToVisible.start();		
					}
				});		
				
				
			}
			
			public void girarImagen2( TextView imagen1, TextView imagen2, int duracion){	
				
				final TextView visibleImage;
				final TextView invisibleImage;		
				
				if (imagen1.getVisibility() == View.GONE) {
					visibleImage = imagen2;
					invisibleImage = imagen1;
				} else {
					invisibleImage = imagen2;
					visibleImage = imagen1;
				}		
				goneToVisible2 = ObjectAnimator.ofFloat(invisibleImage, "rotationY", -90f, 0f);
				visibleToGone2= ObjectAnimator.ofFloat(visibleImage,"rotationY", 0f, 90f);	
				goneToVisible2.setDuration(duracion);		
				visibleToGone2.setDuration(duracion);			
				visibleToGone2.start();
				
				visibleToGone2.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator anim) {
									
						// cuando se acabe de ocultar, comenzar a mostrar la nueva
						visibleImage.setVisibility(View.GONE);
						invisibleImage.setVisibility(View.VISIBLE);
						goneToVisible2.start();		
					}
				});				
			}
					
			//Handler como enlace para vistas de UI que hay que tratarlos fuera de los hilos
			final Handler handler = new Handler();
			
			
			class runa implements Runnable{	
				
				AutoResizeTextView im1, im2, im3, im4;			
				int id;
				
				runa(AutoResizeTextView im1, AutoResizeTextView im2, AutoResizeTextView im3, AutoResizeTextView im4, int id){	
					this.im1=im1;
					this.im2=im2;
					this.im3= im3;
					this.im4= im4;
					this.id=id;
				}
				
				@Override
				public void run() {			
					
					//Ponemos la imagen (o la imagen y las palabras) despues de haberse dado la vuelta para posteriormente
					//hacer una animacion alpha al 0.5
					if(modo.equals("clasico")){					
						im3.setBackgroundResource(getTematicaClasico()[id]);
						im1.setBackgroundResource(getTematicaClasico()[id]);
					}else if(modo.equals("relaciones")){
						if(im2.getText().equals("")){ //el no hay texto, es la imagen con dibujo
							im1.setBackgroundResource(getTematicaRelac()[0][id]);
							im3.setBackgroundResource(R.drawable.agris);
							im3.setText(getTematicaRelac()[1][id]);
							im3.setTextColor(contexto.getResources().getColor(R.color.black));
							im3.resizeText();
							im3.setTypeface(Typeface.DEFAULT_BOLD);
					  		//im3.setTextSize(30);
					  		im3.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
						}else{
							im3.setBackgroundResource(getTematicaRelac()[0][id]);
							im1.setBackgroundResource(R.drawable.agris);
							im1.setText(getTematicaRelac()[1][id]);
							im1.setTextColor(contexto.getResources().getColor(R.color.black));
							im1.resizeText();
							im1.setTypeface(Typeface.DEFAULT_BOLD);
					  		//im1.setTextSize(30);
					  		im1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
						}
						
						
					}
					
					/*Usando TransitionDrawables para hacer un fade:		
					 Ojo si hubiesemos sacado las imagenes a traves de Bitmap tal que asi: 
					 
					bm1 = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.nene2);
					bm2 = BitmapFactory.decodeResource(contexto.getResources(), imagenes[parlinear.get(0).getId()]);
					drawables =  new BitmapDrawable[2];								
					drawables[0] = new BitmapDrawable(contexto.getResources(), bm1);
					drawables[1] = new BitmapDrawable(contexto.getResources(), bm2);
					
					hubiesemos tenido prblemas de memoria. 
					Ver apuntes SoftReference.txt */	
					
					
					// movimiento en el ejeX, si no usamos la libreria NineOldAndroids en vez de poner "translationX", pondriamos View.TRANSLATION_X			
					anim1 = ObjectAnimator.ofFloat(im1, "translationX",
					10,-10,10,-10,10,-10,10,-10,10,-10,10,-10,10,-10,10,-10,10,-10,0);
					anim1.setDuration(1000);
					anim1.start();
					
					//movimento en el eje Y 
					anim2 = ObjectAnimator.ofFloat(im3, "translationX",
					10,-10,10,-10,10,-10,10,-10,10,-10,10,-10,10,-10,10,-10,10,-10,0);
					anim2.setDuration(1000);
					anim2.start();
									
					/*
					//cambio de imagen 1 a 2 (Transicion)
					Drawable[] drawables = new Drawable[2];
					drawables[0] = im1.getBackground();	drawables[1] = im2.getBackground();
					Drawable[] drawables2 = new Drawable[2];
					drawables2[0] = im3.getBackground();	drawables2[1] = im4.getBackground();				
					
					TransitionDrawable trans = new TransitionDrawable(drawables);
					TransitionDrawable trans2 = new TransitionDrawable(drawables2);					
					
					im1.setBackground(trans);
					trans.startTransition(1);	//1 milisegundo de duracion				
					m3.setBackground(trans2);
					trans2.startTransition(1);
					*/
					
					//Usando animacion alpha
					if(modo.equals("clasico")){
						setUpAlphaAnimation(0f);	// o lo que queramos 0.5f por ejemplo					
					}else if(modo.equals("relaciones")){
						setUpAlphaAnimation(0f);						
					}
					/*
					//Usando un Translate animation para ocultar las imagenes:
					TranslateAnimation translateAnimation = new TranslateAnimation(
				        		Animation.ABSOLUTE, 0,  //  de 0 a 1 en la coor X (eje horizontal) siendo 1 valor relativo a view Parent
				                Animation.ABSOLUTE, 0, //en este caso la mitad del eje horizontal 
				                Animation.ABSOLUTE, 0, // de 0 a 200 en la coordenada Y (eje vertical) empezando 0 como el punto incial donde esta el boton. 
				                Animation.ABSOLUTE, 400);
					translateAnimation.setFillAfter(true);
					translateAnimation.setDuration(2000);				
					translateAnimation.setInterpolator(new LinearInterpolator());			       
					im1.startAnimation(translateAnimation);				
					im3.startAnimation(translateAnimation);
					*/
							
				}
				
				public void setUpAlphaAnimation(final float value){
					AlphaAnimation alphaAnimation = new AlphaAnimation(1, value); //1 total opaco. 0 es total transparente
					alphaAnimation.setDuration(timeAnimationLong);
					alphaAnimation.setFillAfter(true);				
					im1.startAnimation(alphaAnimation);
					im3.startAnimation(alphaAnimation);	
					alphaAnimation.setAnimationListener(new AnimationListener(){
						@Override
						public void onAnimationEnd(Animation arg0) {
							ViewHelper.setAlpha(im1, value);
							ViewHelper.setAlpha(im3, value);								
						}
						@Override
						public void onAnimationRepeat(Animation arg0) {	
						}

						@Override
						public void onAnimationStart(Animation arg0) {	
						}
						
					});
				}				
			}
						
			
		}
	
	
	//Sistema de puntuaciones
			public float scoreSystem(){							
				//Base de cada nivel. Eje para un panel 4x3 la base son 6. 
				float base = numFilas*numCol/2;		
				//Amplificamos la base x 10. Ej para un panel 4x3 cada punto acertado vale 60, pero para uno de 6x4 cada punto vale 120
				//A mayor panel, cada punto tiene mas valor:			
				float punto = base*10;
				//Aplicamosm un porcentaje a cada intento, partiendo de la base:
				float porcent = base/((numClicks-aciertos)+base);
				
				//Aunemtamos el total cada vez que acertemos			
				while (i<aciertos){				
					total = total +(punto*combo);
					i++;
				}				
				//En caso de acertar o no acertar mutiplicamos por el total por el porcentaje.
				sumaTotal = total*porcent;			
				return sumaTotal;
				
			}

	class MarcadoresClasico implements Runnable{
		
		
		Context contexto;
		DisplayMetrics dm;
		int aciertos, numClicks, base, combos;
		CharSequence tiempo;
		float puntos;
		Bundle extras;		
		boolean sonido;
		
		
		public	MarcadoresClasico(Context contexto, float puntos, int numClicks
				,int aciertos, int base, CharSequence tiempo, int combos, Bundle extras, boolean sonido){
			this.contexto=contexto;
			this.numClicks=numClicks;
			this.puntos=puntos;
			this.aciertos=aciertos;
			this.base=base;
			this.tiempo=tiempo;		
			this.combos=combos;
			this.extras= extras;			
			this.sonido = sonido;
			
		}

		@Override
		public void run() {
			mostrarMarcadores();	
			
		}
			
		public void mostrarMarcadores(){
			 dm = new DisplayMetrics();
			((Activity) contexto).getWindowManager().getDefaultDisplay().getMetrics(dm);		
			
				
			//Creamos una view tipo LinearLayout que sera agregada al RelativeLayout para que quede por encima
			LinearLayout panta = new LinearLayout(contexto){				
				LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view =li.inflate(R.layout.marcadores_clasico, this, true);				
			};
					
			RelativeLayout llay = (RelativeLayout)((Activity) contexto).findViewById(R.id.linlayout);	
			RelativeLayout.LayoutParams relatparams = new RelativeLayout.LayoutParams
				(LayoutParams.MATCH_PARENT, android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
		
			relatparams.setMargins(dm.widthPixels/15,dm.heightPixels/10,
				dm.widthPixels/15, dm.heightPixels/10);
			panta.setLayoutParams(relatparams);
			llay.addView(panta);
					
			TextView textPuntos =(TextView)panta.findViewById(R.id.marcador_puntos);
			TextView textAciertos =(TextView)panta.findViewById(R.id.marcador_aciertos);
			TextView textCombos =(TextView)panta.findViewById(R.id.marcador_combos);
			TextView textTiempo =(TextView)panta.findViewById(R.id.marcador_tiempo);			
				
			
			Button backTomenu = (Button)panta.findViewById(R.id.backTomenu_finpantalla1);
			Button repeatGame =(Button)panta.findViewById(R.id.repeatGame_finpantalla1);
			Button botonCompartir = (Button)panta.findViewById(R.id.compartir_nivel);
			Button botonGlobalScores = (Button)panta.findViewById(R.id.global_scores);
			/*
			if(isSignedIn()){ //solo se ve si estamos conectados				
				botonGlobalScores.setVisibility(View.VISIBLE);
			}
			*/
			//Ponemos un alphaanimation para que aparezcabn los marcadores.
			AlphaAnimation alphaanim = new AlphaAnimation(0.5f,1);
			alphaanim.setDuration(2000);
			panta.startAnimation(alphaanim);		
			
			
			//Vemos los marcadores del modo que hayamos jugado
			/*
			botonGlobalScores.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					if(modo.equals("clasico")){
						if(numFilas==4 & numCol==3){
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
							,getResources().getString(R.string.leaderboard_classic_4x3))
							,500);
						}else if(numFilas==6 & numCol==4){
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
								,getResources().getString(R.string.leaderboard_classic_6x4))
								,500);
						}else if(numFilas==8 & numCol==5){
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
									,getResources().getString(R.string.leaderboard_classic_8x5))
									,500);
						}else if(numFilas==10 & numCol==7){
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
									,getResources().getString(R.string.leaderboard_classic_10x7))
									,500);
						}
					}else if (modo.equals("relaciones")){
						if(numFilas==4 & numCol==3){
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
							,getResources().getString(R.string.leaderboard_connections_4x3))
							,500);
						}else if(numFilas==5 & numCol==4){
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
								,getResources().getString(R.string.leaderboard_connections_5x4))
								,500);
						}else if(numFilas==7 & numCol==4){
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
									,getResources().getString(R.string.leaderboard_connections_7x4))
									,500);
						}else if(numFilas==8 & numCol==5){
							startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
									,getResources().getString(R.string.leaderboard_connections_8x5))
									,500);
						}
					}
				}
			});
			*/
			
			botonCompartir.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);					
					if(sonido) ActivityModosSeleccion.soundClick1.start();						
					sharingIntent.setType("text/plain"); //Pasaremos solo 					
					String tipopanel=null, tipomodo=null;				
					
						if(base==6){
							if(extras.getString("modo").equals("clasico")){
								tipomodo=contexto.getResources().getString(R.string.botonmodoclasico);
								tipopanel = "4x3";
							}
							else if(extras.getString("modo").equals("relaciones")){
								tipomodo=contexto.getResources().getString(R.string.botonmodorelaciones);
								tipopanel = "4x3";
							}
						}else if(base==12){
							tipomodo=contexto.getResources().getString(R.string.botonmodoclasico);
							tipopanel = "6x4";
						}else if(base==10){
							tipomodo=contexto.getResources().getString(R.string.botonmodorelaciones);
							tipopanel = "5x4";
						}else if(base==20){
							if(extras.getString("modo").equals("clasico")){
								tipomodo=contexto.getResources().getString(R.string.botonmodoclasico);
								tipopanel = "8x5";
							}
							else if(extras.getString("modo").equals("relaciones")){
								tipomodo=contexto.getResources().getString(R.string.botonmodorelaciones);
								tipopanel = "8x5";
							}
						}else if(base==14){
							tipomodo=contexto.getResources().getString(R.string.botonmodorelaciones);
							tipopanel = "7x4";
						}else if(base==35){
							tipomodo=contexto.getResources().getString(R.string.botonmodoclasico);
							tipopanel = "10x7";
						}
												
						
					String shareBody = contexto.getResources().getString(R.string.sharecontent_puntos1)+" "+
							contexto.getResources().getString(R.string.sharecontent_puntos2)+" "+tipomodo+								
							contexto.getResources().getString(R.string.sharecontent_puntos6)+" "+tipopanel+
							contexto.getResources().getString(R.string.sharecontent_puntos4)+" "+String.format("%.2f", puntos)+						
							contexto.getResources().getString(R.string.sharecontent_puntos5);
					sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, contexto.getResources().getString(R.string.sharesubject_puntos)); 
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
					contexto.startActivity(Intent.createChooser(sharingIntent,contexto.getResources().getString(R.string.compartirvia)));	
					
				}
				
			});
			
			
			
				
			backTomenu.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {	
					if(sonido) ActivityModosSeleccion.soundClick1.start();
					/*	
					//cuando pulsamos boton back , guardamos las puntuaciones en sharedPreferencias dependiend del modo
					if(extras.getString("modo").equals("clasico"))
						new ScoresHigh(contexto, ActivityBoard.HighScoresLevelClassic, puntos, extras.getString("nivel") ).setHighScores();
					if(extras.getString("modo").equals("relaciones"))
						new ScoresHigh(contexto, ActivityBoard.HighScoresLevelRelaciones, puntos, extras.getString("nivel") ).setHighScores();
					*/
					((Activity)contexto).finish();			
				}				
			});
			repeatGame.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(sonido) ActivityModosSeleccion.soundClick1.start();
					/*	
					//Tambien ponemos los marcadores en caso de querer repetir el juego
					if(extras.getString("modo").equals("clasico"))
						new ScoresHigh(contexto, ActivityBoard.HighScoresLevelClassic, puntos, extras.getString("nivel")).setHighScores();
					if(extras.getString("modo").equals("relaciones"))
						new ScoresHigh(contexto, ActivityBoard.HighScoresLevelRelaciones, puntos, extras.getString("nivel") ).setHighScores();
					*/
					Intent inten = new Intent(contexto, ActivityBoard.class);	
					inten.putExtra("modo", extras.getString("modo"));
					inten.putExtra("tematica", extras.getString("tematica"));
					inten.putExtra("numFilas", extras.getInt("numFilas"));
					inten.putExtra("numColum", extras.getInt("numColum"));
					inten.putExtra("nivel", extras.getString("nivel"));
					contexto.startActivity(inten);		
					((Activity)contexto).finish();
					
				}
			});
			//Puntos totales:
			textPuntos.setText(String.format("%.2f", puntos));
			//Aciertos y porcentaje de aciertos:
			textAciertos.setText(String.valueOf(aciertos)+"/"+String.valueOf(numClicks)+
					" ("+String.valueOf((100*base/numClicks)+"%)"));
			//Tiempo empleado:
			textTiempo.setText(tiempo);	 
			textCombos.setText(String.valueOf(combos));                                                                                                 
			
		}                   
	}		
}

