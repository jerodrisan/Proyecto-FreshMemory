package com.jesusmanuelrodriguez.freshmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
*/
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;


//CLASE PARA EL MODO DE JUEGO NUMEROS DEL TIPO SECUENCIA.
public class ActivitySecuencias extends Activity{
	
	SharedPreferences gamePrefs;
	public static final String GAME_PREFS = "series";	
	// Lo usaremos para que el vistainformacion solo salga solo una vez ( correspondiente al nivel que tocamos en el spinner)
	//Luego recuperamos el valor en sucesivos niveles y si el el nivel no es el nivel tocado en un principio entonces no saldra el stubview
	public static final String NIVELTOCADO = "niveltocado"; 
	private static final int DURACION_THREAD = 2000;
	
	int[] matrizTimeThread;
	
	DisplayMetrics dm;
	Context contexto;
	
	Handler handler = new Handler();

	ViewStub  vistaInicio,  vistainformacion, vistaFinalFracaso;
	ImageView   botonInformacion, botonSonido,  logoSecuenciaInicio, botonVolverajugar;
	
	GridLayout gridlay;
	Spinner spinnerNivel;
	TextView  textTiempo, marcadorNivelnosuperado, textoTituloSeries;	
	ProgressBar progressbar;
	
	String modo;
	int gap, gapLeft, gapTop, anchoIm, altoIm, publi;
	int base, numFilas, numCol;
	int primerElemento;
	
	
	ArrayList<Integer> lista, numbersMixed, posMixed, numbers, pos;
	ArrayList<String> levels, niveles ; 
	ArrayList<LinearLayout> parlinear;
	
	//Variables recibidas por Intenet:
	int vInicio, vBar,  vspin;
	
	static final int time = 1000; //tiempo que dura la animacion 
	
	int numberInicial;
	int index; //indice del elemento seleccionado del Spinner
	int nivel=1; //contador de niveles
	int indexTotalSecuencia; // total de elementos que se llenaran en el Spinner en modo normal
	int indexTotalAux;
	int ids=0;
	int elemSpinner;	
	
	int nivelTocado;
	
	int numClicks=1, aciertos=1;	
	long sumaTotal=0;
	long acumulado = 0; //Acumulado de puntuacion segun se avanza de nivel.
	int i=0;
			
	int retardo = (int)time/1000; // Retardo para poner marcadores
	int aditionalTime =0;
		
	int indexImagen =1;
	
	long timeCrono;
	
	int progressTime=0, totalTime; 		
	int OK = 123456;
	
	boolean hasSound=true;
	boolean gameover=false;
	boolean setMusicaFondo=false;
	boolean gamePaused=false;
	boolean progressBParado =false; 
	boolean marcadoresMostrados = false;
	int keybackTocado = 0;
	
	SonidosSimon soundSecuencias;
	int  sonidoPlay, sonidoTecla, sonidoOkStub, sonidoboton25, aplausosSound1, OhhSounds1, crowdSound;	
	MediaPlayer sonidoSalir_Nivel;
	int MAX_VOLUME = 100; //volumen m�ximo de referencia	
	
	Button botonOKinformativo;
	
	int refTipoSecuencia;
	
	int corte; // numero inicial en donde producimos el primer proceso de hacerOperaciones();
	int cadencia; // cada cuantos movientos se produce el proceso de hacerOperaciones();
	int initCorte;
	int b, a;
	int[] initNumbers, finalNumbers, a_b_Numbers;
	
	HiloComienzoJuego hilo;
	//AdView adView;
	SharedPreferences soundPrefs, conexionScores;
	SharedPreferences.Editor editor, editorScores;
	public static final String SONIDO = "sonido";	
	public static final String CONEXION = "conexion";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_timeattack);
		contexto=this;
		
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
		gamePrefs = getSharedPreferences(GAME_PREFS, 0);
		
		matrizTimeThread=new int[2];
		initNumbers = new int[6];
		finalNumbers = new int[6];
		a_b_Numbers = new int[2];
		setUpSounds(); //Configuracion de todos los sonidos
		
		//inflamos la vista de informacion del juego
		vistainformacion = (ViewStub)findViewById(R.id.viewstubinforsecuencias);	
		
		inflarSpinner();	
		
		getExtras();
		
		base=numFilas*numCol;		
		
		configPantalla();
		
		initViews();	//Inicializacion de todas las views				 
		a=0;			//Inicializamos valores a y b
		b=0;
		//cadencia=0;
		//corte=0;
		
		//Preferencias conexion 
		conexionScores = this.getSharedPreferences(CONEXION, 0);		
		//Conectamos en la activity con google play services
		/*
		if(conexionScores.getBoolean(CONEXION, true))
			beginUserInitiatedSignIn();
		*/
	}
	//METODOS GOOGLE PLAY GAMES 
	/*
	@Override
	public void onSignInFailed() {			
	}

	@Override
	public void onSignInSucceeded() {
		//Mejor no poner nada porque si no sale en cada nivel que pasemos
		//Toast.makeText(this, getResources().getString(R.string.you_are_connected), Toast.LENGTH_LONG).show();		
	}
	
	public void subirPuntuacion(){			
		//solo subimos puntuacion si estamos conectados:		
		String[] savedScore = gamePrefs.getString("highScoresSecuencias", "").split("\\|");
		String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));		
		Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_sequence_numbers), Integer.parseInt(punt));		
		
	}

	//Para ver marcadores , ver boton botonScores	
	///FIN GOOGLE PLAY GAMES
	*/
	public void setUpSounds(){
		
		soundSecuencias = new SonidosSimon(this);
		soundSecuencias.setVolume(0.4f);
		//sonidoSalir_Nivel= soundSecuencias.load(R.raw.button54);	
		sonidoSalir_Nivel = MediaPlayer.create(this, R.raw.button54);
		sonidoSalir_Nivel.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		sonidoPlay = soundSecuencias.load(R.raw.keyboardtypesngl);
		sonidoTecla = soundSecuencias.load(R.raw.keyboardtypesngl);
		sonidoOkStub = soundSecuencias.load(R.raw.button54);
		sonidoboton25 = soundSecuencias.load(R.raw.sonidobutton25);
		aplausosSound1 = soundSecuencias.load(R.raw.estrellitas);
		OhhSounds1 = soundSecuencias.load(R.raw.ohh1);
		crowdSound = soundSecuencias.load(R.raw.crwdbugl);
	}
	
	//funcion para establecer volumen customizado		
	public float setCustomVolumen(int soundVolume){  //volumen que queremos poner (la mitad del total de volumen) o  lo que queramos!!!
		return (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME))); // ver:http://stackoverflow.com/questions/5215459/android-mediaplayer-setvolume-function
	}
	
	public void comienzoJuego(){
		//sonido pitido comienzo juego un seg.
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {				
				//mp1.start();
			}			
		}, 500 );
		
		hilo = new HiloComienzoJuego();
		hilo.start();		
	}
	public void setStopProgressBar(boolean stop){
		this.progressBParado = stop;	
	}
	
	public boolean getStopProgressBar(){
		return progressBParado;
	}	
	
	class HiloComienzoJuego extends Thread {
		@Override
		public void run() {						
			while(progressTime < totalTime & !getGameOver()){
			 if(!getGamePaused()){				 
				try {						
					Thread.sleep(1000);
					if(getGamePaused()) continue; 
				} catch (InterruptedException e) {								
					e.printStackTrace();					
				}		
				if(!getStopProgressBar())
					progressbar.incrementProgressBy(1);		
				progressTime++;					
				if((totalTime-progressTime)<10 && (totalTime-progressTime)>0){
					handler.post(new Runnable(){
						@Override
						public void run() {
							if(!getGameOver()){										
								new ComboPantalla1(contexto, String.valueOf(totalTime-progressTime), 
												R.id.timeattack_layout,
												contexto.getResources().getColor(R.color.red), false,
												1,3,1,3,1000,  R.drawable.fondo_combotransparente).run();
								}
							}
						});								
					}				
			 }	//fin if
			}//fin while	
			
			if(!getGameOver())
				if(soundPrefs.getBoolean("sonido", true)){ 
					soundSecuencias.play(OhhSounds1);
					//musicafondo.stop();
					}	
			handler.post(new Runnable(){
				@Override
				public void run() {					
					botonInformacion.setVisibility(View.INVISIBLE);
					botonVolverajugar.setVisibility(View.INVISIBLE);
					botonSonido.setVisibility(View.INVISIBLE);
					progressbar.setVisibility(View.INVISIBLE);
					textTiempo.setVisibility(View.INVISIBLE);					
					gridlay.setVisibility(View.INVISIBLE);					
					if(!getGameOver())						
						new Timer().schedule(new TimerTask(){
							@Override
							public void run() {
								handler.post(new Runnable(){
									@Override
									public void run() {			
										marcadorNivelnosuperado.setText(contexto.getResources().getString(R.string.nivel)
											+" "+String.valueOf(nivel)+" "+contexto.getResources().getString(R.string.nosuperado));   
										vistaFinalFracaso.setVisibility(View.VISIBLE);	
									}												
								});										
							}										
						}, time);					
				}							
			});			
		}
	}
	
	
	
	public void inflarSpinner(){ //Ponerlo en otra clase para que ocupe menos menoria??????
		
		levels = new ArrayList<String>();	
		//nivel50
		for (int i=0; i<49; ++i){
			levels.add(i, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(i+1));
		}
		//Ultimo nivel: Ponemos la palabra Final 
		levels.add(49,contexto.getResources().getString(R.string.nivel)+" Final"+" 50");
	
		
	}
	
	public void setInitNumbers(int x, int y, int z, int u, int v, int w){
		initNumbers[0]=x; initNumbers[1]=y; initNumbers[2]=z; initNumbers[3]=u; initNumbers[4]=v; initNumbers[5]=w; 
	}
	public int[] getInitNumbers(){
		return initNumbers;
	}
	
	public void setFinalNumbers(int x, int y, int z, int u, int v, int w){
		finalNumbers[0]=x; finalNumbers[1]=y; finalNumbers[2]=z; finalNumbers[3]=u; finalNumbers[4]=v; finalNumbers[5]=w;		
	}
	
	public int[]get_a_b_Numbers(){
		a_b_Numbers[0]=new Random().nextInt(finalNumbers[0]+finalNumbers[1])+finalNumbers[2]; //numero a
		a_b_Numbers[1]=new Random().nextInt(finalNumbers[3]+finalNumbers[4])+finalNumbers[5]; //numero b
		return a_b_Numbers;
	}
	public void setInicialCorte(int initCorte){
		//corte= new Random().nextInt(8-5)+5; 	  // En caso de poner corte variable
		this.initCorte=initCorte;
		this.corte= initCorte;
	}
	public int getInicialCorte(){
		return this.initCorte;
	}
	
	
	public void getExtras(){		
	
		Bundle extras = this.getIntent().getExtras();	
				
		nivel = extras.getInt("nivel");				
		indexTotalSecuencia=extras.getInt("indexTotalSecuencia");				
		indexTotalAux = extras.getInt("indexTotalAux");
		totalTime = extras.getInt("tiempoTotal");
		vInicio = extras.getInt("vistaInicio");		
		vspin =extras.getInt("vistaspinner");
				
		acumulado=extras.getLong("acumulado");						
		setMusicaFondo = extras.getBoolean("musicafondo");
		
		switch (nivel){
		case 1:
			
			numFilas=3; 
			numCol =3;		
			totalTime=10;
			//Numeros iniciales:
			setInitNumbers(1,0,2,1,0,2); // Con lo cual: b:(7-2)+2 (Aleatorio entre 2 y 6 inclusive) y a:(4-1)+1 --> suma a y suma b. 
			//Numeros siguientes
			setFinalNumbers(1,0,2,1,0,2); // a: ultimos tres numeros, b: tres primeros numeros -- suma a y suma b				
			//Cadencia y corte:
			setInicialCorte(10);
			cadencia=7;			 //cadencia = new Random().nextInt(6-3)+3; //En caso de poner Cadencia variable	
			break;		
		case 2:
			numFilas=3;		numCol=3;	totalTime=14;
			setInitNumbers(1,0,3,1,0,1);	setFinalNumbers(1,0,3,1,0,1);
			setInicialCorte(10);	cadencia=6;
			break;			
		case 3:
			numFilas=3; 	numCol=3;	totalTime=14;	
			setInitNumbers(1,0,0,7,-3,3);	setFinalNumbers(1,0,0,7,-3,3);
			setInicialCorte(10);	cadencia=6;
			break;							
		case 4:
			numFilas=3;		numCol=3;	totalTime=17;
			setInitNumbers(1,0, 2,5,-3,3);	setFinalNumbers(1,0,-2,7,-3,3);
			setInicialCorte(10);	cadencia=6;
			break;				
		case 5:			
			numFilas=3;		numCol=3;	totalTime=17;
			setInitNumbers(6,-4,4, 6,-4,4); //(Aleatorio entre 4 y 5 ambos inclusive). 
			setFinalNumbers(1,0,-2,7,-3,3);
			setInicialCorte(10);	cadencia=6;
			break;			
		case 6:
			numFilas=3; 	numCol =3;	totalTime=18;
			setInitNumbers(1,0,-2,1,0,3);	setFinalNumbers(1,0,-2,7,-3,3);
			setInicialCorte(10);	cadencia=6;
			break;		
		case 7:
			numFilas=3;		numCol=3;	totalTime=17;
			setInitNumbers(1,0,-3,6,-4,4);	setFinalNumbers(3,-1,1,3,-1,1);
			setInicialCorte(10);	cadencia=7;			
			break;
		case 8:
			numFilas=3;		numCol=3;	totalTime=17;
			setInitNumbers(7,-5,-5,8,-6,6); /*restamos entre 4 o 5*/ setFinalNumbers(3,-1,1,3,0,-4);
			setInicialCorte(10);	cadencia=7;			
			break;		
			
		case 9:
			numFilas=3;		numCol=3;	totalTime=17;
			setInitNumbers(11,-7,-7,10,-8,8 ); setFinalNumbers(2,0,-35, 44,-42,42);
			setInicialCorte(10);
			cadencia=7;			
			break;			
												
		case 10:	
			numFilas=3;		numCol=3;	totalTime=17;
			setInitNumbers(15,-9,-9,12,-10,10);		setFinalNumbers(3,0,-4,4,-3,3);
			setInicialCorte(10);	cadencia=4;			
			break;		
			
		case 11:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(6,-4,4,4,-2,2);			setFinalNumbers(8,-6,6,6,-4,4);
			setInicialCorte(6);		cadencia=10;			
			break;	
			
		case 12:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(8,-6,6,4,-2,2);			setFinalNumbers(10,-8,8,6,-4,4);
			setInicialCorte(6);		cadencia=10;			
			break;				
			
		case 13:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(10,-8,8,4,-2,2);			setFinalNumbers(12,-10,10,6,-4,4);
			setInicialCorte(6);		cadencia=10;			
			break;		
			
		case 14:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(6,-4,4,4,-2,2);		setFinalNumbers(5,-3,-3,8,-6,6);
			setInicialCorte(6);		cadencia=10;			
			break;		
			
		case 15:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(8,-6,6,4,-2,2);		setFinalNumbers(5,-3,-3,10,-8,8);
			setInicialCorte(6);		cadencia=10;			
			break;		
			
		case 16:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(10,-8,8,4,-2,2);		setFinalNumbers(5,-3,-3,12,-10,10);
			setInicialCorte(6);		cadencia=10;				
			break;			
			
		case 17:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(5,-3,-3, 6,-4,4);	setFinalNumbers(5,-3,-3, 8,-6,6);
			setInicialCorte(6);		cadencia=10;						
			break;	
			
		case 18:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(5,-3,-3, 8,-6,6);		setFinalNumbers(5,-3,-3, 10,-8,8);
			setInicialCorte(6);		cadencia=10;					
			break;	
			
		case 19:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(5,-3,-3, 10,-8,8);		setFinalNumbers(5,-3,-3, 12,-10,10);
			setInicialCorte(6);		cadencia=10;				
			break;	
			
		case 20:	
			numFilas=4;		numCol=3;	totalTime=23;
			setInitNumbers(5,-3,-3, 12,-10,10);		setFinalNumbers(5,-3,-3, 14,-12,12);
			setInicialCorte(6);		cadencia=10;				
			break;		
			
		case 21:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(9,-7,7,7,-5,5);			setFinalNumbers(6,-4,-4, 7,-5,5);
			setInicialCorte(8);		cadencia=10;				
			break;		
						
		case 22:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(11,-9,9, 7,-5,5);			setFinalNumbers(6,-4,-4, 9,-7,7);
			setInicialCorte(8);		cadencia=10;				
			break;		
			
		case 23:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(13,-11,11,7,-5,5);		setFinalNumbers(6,-4,-4,11,-9,9);
			setInicialCorte(8);		cadencia=10;				
			break;		
			
		case 24:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(7,-5,5, 5,-3,3);			setFinalNumbers(6,-4,-4, 9,-7,7);
			setInicialCorte(6);		cadencia=5;				
			break;		
						
		case 25:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(9,-7,7, 5,-3,3);		setFinalNumbers(6,-4,-4, 11,-9,9);
			setInicialCorte(6);		cadencia=5;				
			break;		
			
		case 26:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(11,-9,9, 5,-3,3);	setFinalNumbers(6,-4,-4, 13,-11,11);
			setInicialCorte(6);		cadencia=5;				
			break;	
						
		case 27:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(6,-4,-4, 7,-5,5);	setFinalNumbers(6,-4,-4,9,-7,7);
			setInicialCorte(8);		cadencia=10;				
			break;	
			
		case 28:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(6,-4,-4, 9,-7,7); 	setFinalNumbers(6,-4,-4, 11,-9,9);
			setInicialCorte(8);		cadencia=10;				
			break;	
			
		case 29:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(6,-4,-4,11,-9,9);	setFinalNumbers(6,-4,-4, 13,-11,11);
			setInicialCorte(8);		cadencia=10;				
			break;		
			
		case 30:	
			numFilas=4;		numCol=4;	totalTime=27;
			setInitNumbers(6,-4,-4, 13,-11,11);	setFinalNumbers(6,-4,-4, 15,-13,13);
			setInicialCorte(8);		cadencia=10;				
			break;	
			
		case 31:	
			numFilas=5;		numCol=4;	totalTime=34;
			setInitNumbers(8,-6,6,6,-4,4);			setFinalNumbers(7,-5,-5,10,-5,5);
			setInicialCorte(6);		cadencia=6;			
			break;	
			
		case 32:	
			numFilas=5;		numCol=4;	totalTime=34;
			setInitNumbers(10,-8,8,6,-4,4);			setFinalNumbers(7,-5,-5,12,-8,8);
			setInicialCorte(6);		cadencia=6;			
			break;				
			
		case 33:	
			numFilas=5;		numCol=4;	totalTime=34;
			setInitNumbers(12,-10,10,6,-4,4);		setFinalNumbers(7,-5,-5,14,-10,10);
			setInicialCorte(6);		cadencia=6;			
			break;		
			
		case 34:	
			numFilas=5;		numCol=4;	totalTime=34;
			setInitNumbers(14,-12,12,6,-4,4);		setFinalNumbers(7,-5,-5,16,-10,10);
			setInicialCorte(6);		cadencia=6;			
			break;		
			
		case 35:	
			numFilas=5;		numCol=4;	totalTime=34;
			setInitNumbers(16,-14,14,6,-4,4);		setFinalNumbers(7,-5,-5,18,-10,10);
			setInicialCorte(6);		cadencia=6;			
			break;		
			
		case 36:	
			numFilas=5;		numCol=4;	totalTime=36;
			setInitNumbers(7,-5,-5,8,-6,6);		setFinalNumbers(7,-5,-5,10,-5,5);
			setInicialCorte(6);		cadencia=6;				
			break;			
			
		case 37:	
			numFilas=5;		numCol=4;	totalTime=36;
			setInitNumbers(7,-5,-5, 10,-8,8);	setFinalNumbers(7,-5,-5, 12,-8,8);
			setInicialCorte(6);		cadencia=6;						
			break;	
			
		case 38:	
			numFilas=5;		numCol=4;	totalTime=36;
			setInitNumbers(7,-5,-5, 12,-10,10);		setFinalNumbers(7,-5,-5, 14,-9,9);
			setInicialCorte(6);		cadencia=6;					
			break;	
			
		case 39:	
			numFilas=5;		numCol=4;	totalTime=36;
			setInitNumbers(7,-5,-5, 14,-12,12);		setFinalNumbers(7,-5,-5, 16,-10,10);
			setInicialCorte(6);		cadencia=6;				
			break;	
			
		case 40:	
			numFilas=5;		numCol=4;	totalTime=36;
			setInitNumbers(7,-5,-5, 16,-14,14);		setFinalNumbers(7,-5,-5, 18,-10,10);
			setInicialCorte(6);		cadencia=6;				
			break;			
			
		case 41:	
			numFilas=6;		numCol=4;	totalTime=42;
			setInitNumbers(9,-7,-7,18,-16,16);		setFinalNumbers(9,-7,-7,20,-10,10);
			setInicialCorte(6);		cadencia=6;			
			break;	
			
		case 42:	
			numFilas=6;		numCol=4;	totalTime=42;
			setInitNumbers(13,-9,-9,20,-18,18);		setFinalNumbers(13,-9,-9,22,-12,12);
			setInicialCorte(6);		cadencia=6;			
			break;				
			
		case 43:	
			numFilas=6;		numCol=4;	totalTime=42;
			setInitNumbers(17,-11,-11,22,-20,20);			setFinalNumbers(17,-11,-11,24,-14,14);
			setInicialCorte(6);		cadencia=6;			
			break;		
			
		case 44:	
			numFilas=6;		numCol=4;	totalTime=42;
			setInitNumbers(21,-13,-13,24,-22,22);		setFinalNumbers(21,-13,-13,26,-16,16);
			setInicialCorte(6);		cadencia=6;			
			break;		
			
		case 45:	
			numFilas=6;		numCol=4;	totalTime=42;
			setInitNumbers(24,-14,-14,26,-24,24);		setFinalNumbers(24,-14,-14,28,-18,18);
			setInicialCorte(6);		cadencia=6;			
			break;		
			
		case 46:	
			numFilas=8;		numCol=4;	totalTime=52;
			setInitNumbers(35,-20,-20,27,-25,25);		setFinalNumbers(36,-25,-25,29,-19,19);
			setInicialCorte(8);		cadencia=8;				
			break;			
			
		case 47:	
			numFilas=8;		numCol=4;	totalTime=52;
			setInitNumbers(37,-22,-22, 29,-27,27);	setFinalNumbers(38,-27,-27, 31,-21,21);
			setInicialCorte(8);		cadencia=7;						
			break;	
			
		case 48:	
			numFilas=8;		numCol=4;	totalTime=52;
			setInitNumbers(39,-24,-24, 31,-29,29);		setFinalNumbers(40,-29,-29, 33,-23,23);
			setInicialCorte(8);		cadencia=7;					
			break;	
			
		case 49:	
			numFilas=8;		numCol=4;	totalTime=52;
			setInitNumbers(41,-26,-26, 33,-31,31);		setFinalNumbers(42,-31,-31, 35,-25,25);
			setInicialCorte(8);		cadencia=7;				
			break;	
			
		case 50:	
			numFilas=8;		numCol=4;	totalTime=50;
			setInitNumbers(50,-35,-35, 42,-40,40);		setFinalNumbers(5,-3,-3, 44,-34,34);
			setInicialCorte(8);		cadencia=6;				
			break;			
			
		}
	}

	public void setVistaInformation(){
	  if(vInicio!=0){	 //siempre que no sea la pantalla de inicio		
		vistainformacion.inflate();
		vistainformacion.setVisibility(View.VISIBLE);
		botonOKinformativo = (Button)findViewById(R.id.ok_text_inforsecuencias);
		botonOKinformativo.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {	
				if (soundPrefs.getBoolean("sonido", true))
					soundSecuencias.play(sonidoOkStub);
				vistainformacion.setVisibility(View.INVISIBLE);
			}
		});
	  }
	}
	
	public void configPantalla(){
		//Pillamos dimensiones pantalla:
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//Ponemos la pantalla full screen:
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		
		//Ojo por defecto se reservo espacio para publi en relacion: publi = dm.heightPixels/6;
		publi = dm.heightPixels/6;	//Publicidad
				
		setGaps(numCol,numFilas); // Establecemos los margenes contando publicidad, anchoy y alto de cada celda				
	}
	
	public void setGaps (int numC, int numF){
		final int gapRefer =  dm.widthPixels/62; // = 6dp = 11.61 pixels
		final int sumHorizGapRef =  (3+1)*gapRefer; // Para 3 columnas calculamos 4 huecos y sumamos el total de los 4.
		final int sumVertGapRef = (4+1)*gapRefer;
		
		final int sumAnchoImRef = dm.widthPixels-sumHorizGapRef ; //Espacio total de las fichas juntas restando la suma de los huecos horizontales
		final int sumAltoImRef = (dm.heightPixels-publi) - sumVertGapRef; //Idem para filas pero restando la publicidad
			
		gap = sumHorizGapRef/(numC+1); // hueco dependiendo del numero de col
		gapLeft = gap;  //hueco izquierdo
		gapTop = gap;
		anchoIm = (sumAnchoImRef/numC) ; //ancho de cada ficha dependiendo del num col	
		altoIm = (sumAltoImRef/numF);	//idem para filas	
		
		float relac = (float)altoIm/(float)anchoIm;
		
		//Igualamos anchura de la imagen con altura en caso de que la relacion entre ambas sea menor que un tanto por ciento
		//y si no ... las dejamos como estan
		if(anchoIm >altoIm){ 
			if( relac <0.9 ){	//Ponemos un 10% de deformacion (o lo que sea). Si queremos menos subimos el indice
				// O igualamos anchuras , o bien: subimos un poquito la anchura para que no quede todo tan cuadradito:
				//anchoIm=altoIm;
				anchoIm = (int)(altoIm/0.9);
				gapLeft =  (dm.widthPixels - (anchoIm*numC))/2;			
			}
		}else if(altoIm>anchoIm){
			if((1/relac)<0.9){
				// O igualamos alturas , o bien: subimos un poquito la altura para que no quede todo tan cuadradito:
				//altoIm =anchoIm;  
				altoIm = (int)(anchoIm/0.9);
				gapTop = ((dm.heightPixels-publi) - (altoIm*numF))/2;
			}
		}
	}
	
	
	public void initViews(){
		
		final AlphaAnimation alfa = new AlphaAnimation(0, 1.0f);
		alfa.setDuration(1000);
		
		//Mostramos la vista: mode_timeattack_inicio.xml que viene implicita en el ViewStub	
		
		vistaInicio = (ViewStub)findViewById(R.id.viewstubSecuenciasInicio);
		vistaInicio.inflate();
		vistaInicio.setVisibility(vInicio); //Visibilidad proveniento del intent
		
		logoSecuenciaInicio = (ImageView)findViewById(R.id.logosecuenciasinicio);
		logoSecuenciaInicio.setImageResource(R.drawable.logonumeros);
		
		textoTituloSeries = (TextView)findViewById(R.id.textosecuenciasinicio);
		textoTituloSeries.setText(getResources().getString(R.string.titulonumeros));
		
		vistaFinalFracaso = (ViewStub)findViewById(R.id.viewstubTimeattackFinalfracaso);
		vistaFinalFracaso.inflate();
		vistaFinalFracaso.setVisibility(View.INVISIBLE);
		
		//tiempo no superado e infotiempo
		marcadorNivelnosuperado = (TextView)this.findViewById(R.id.text_levelnosuperado);
		
		//BotonPausar. No lo usaremos para que sea mas dificil
									
		//Boton Sonido
		botonSonido = (ImageView)this.findViewById(R.id.timeattack_botonsonido);		
		
		//El boton de volver a jugar lo usaremos como boton de informacion de lo que hay que hacer
		botonInformacion = (ImageView)this.findViewById(R.id.timeattack_botonbolverajugar);	
		botonInformacion.setImageResource(R.drawable.botoninfo_72);
		
		//El boton de pause lo usaremos como Boton de volver a jugar, en el lado de la derecha
		botonVolverajugar = (ImageView)this.findViewById(R.id.timeattack_botonpause);
		botonVolverajugar.setImageResource(R.drawable.boton_volverajugar3);
		botonVolverajugar.setBackgroundResource(R.drawable.fondo_blanco);		
		
		// gridview:
		gridlay = (GridLayout)findViewById(R.id.grid_timeattack);	
		
		//Texto para informar del tiempo que llevamos		
		textTiempo = (TextView)this.findViewById(R.id.timeattack_infotiempo);
		textTiempo.setTextSize(17);
		
		//Barra de progreso
		progressbar = (ProgressBar)this.findViewById(R.id.timeattack_progressbar);		
		progressbar.setMax(totalTime);			
		
	//INICIACION DEL SPINNER PARA SELECCION DE NIVEL:
		final SharedPreferences.Editor editor = gamePrefs.edit(); //editor para poner en memoria el nivel que seleccionamos en el spinner
		
		spinnerNivel = (Spinner)this.findViewById(R.id.secuencias_spiner);		
		spinnerNivel.setPrompt("Niveles");
		spinnerNivel.setVisibility(vspin);
		
		//Meteremos los elementos de forma dinamica:	
		niveles = new ArrayList<String>(); 
		
		for (int i=0; i<indexTotalSecuencia; ++i)
			niveles.add(i,levels.get(i));			
		
				
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, niveles); //spinner_item.xml creado manualmente, se pueden usar los que vienen por defecto
		//adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
		adapter2.setDropDownViewResource(R.layout.my_spinner_textview); //spinner para centrar el texto en el centro y no a la izquierda ver http://stackoverflow.com/questions/5755506/text-is-pushed-to-the-left-in-a-spinner-android
		spinnerNivel.setAdapter(adapter2);	
		
		elemSpinner = spinnerNivel.getCount();		
		
		spinnerNivel.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
					//Con este truco solo cambiamos la apariencia del primer elemento del spinner!!!
				 	((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
				  //  ((TextView) parentView.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
			        ((TextView) parentView.getChildAt(0)).setTextSize(22);		
			        ((TextView) parentView.getChildAt(0)).setGravity(17);	
			        
			        //A continunacion metemos la informacion del indice pulsado en un sharedpreferneces:
			       // si queremos que salga la informacion del stubview cada vez que pulsemos sobre un nivel del spiner , ponemos ese if.
			        //y si solo queremos que salga la informacion cuando pulsamos el nivel 1 del spinner, quitamos el if(vInicio==0)
			     //   if (vInicio==0){ 			        
			        	index = parentView.getSelectedItemPosition();	
						editor.putInt(NIVELTOCADO, index);
						editor.commit();
						nivelTocado=1+gamePrefs.getInt(NIVELTOCADO, 0);
			     //   }				
					if (index!=0){
						if (soundPrefs.getBoolean("sonido", true))
							soundSecuencias.play(sonidoOkStub);
					}					
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		spinnerNivel.setOnTouchListener(new View.OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(soundPrefs.getBoolean("sonido", true))
					//soundSecuencias.play(sonidoSalir_Nivel);
					sonidoSalir_Nivel.start();
				return false;
			}
		});
		
		
		
		//INICIACION DE VISTAS, PONERLAS VISIBLES O INVISIBLES:
		if(vInicio==View.VISIBLE){
			botonVolverajugar.setVisibility(View.INVISIBLE);
			botonSonido.setVisibility(View.INVISIBLE);
			botonInformacion.setVisibility(View.INVISIBLE);			
			textTiempo.setVisibility(View.INVISIBLE);
			gridlay.setVisibility(View.INVISIBLE);
			progressbar.setVisibility(View.INVISIBLE);		
			//adView.setVisibility(View.INVISIBLE);
			
		//Si no es la pantalla de inicio, ponemos texto informativo que solo saldra una vez	a traves de un stubview		
		}else{				
			new Timer().schedule(new TimerTask(){
				@Override
				public void run() {
					handler.post(new Runnable(){
						@Override
						public void run() {							
						 if(1+gamePrefs.getInt(NIVELTOCADO, 0)==nivel){
							 	vistainformacion.inflate();
								vistainformacion.setVisibility(View.VISIBLE);	
								botonOKinformativo = (Button)findViewById(R.id.ok_text_inforsecuencias);
								botonOKinformativo.setOnClickListener(new View.OnClickListener() {			
									@Override
									public void onClick(View v) {
										if (soundPrefs.getBoolean("sonido", true))
											soundSecuencias.play(sonidoOkStub);
										configGrid();
										botonVolverajugar.setVisibility(View.VISIBLE);
										botonSonido.setVisibility(View.VISIBLE);
										botonSonido.setImageResource(soundPrefs.getBoolean("sonido", true)==true?R.drawable.boton_sonidoonfondo:R.drawable.boton_sonidoofffondo);			
										botonInformacion.setVisibility(View.VISIBLE);			
										textTiempo.setVisibility(View.VISIBLE);
										textTiempo.startAnimation(alfa);
										gridlay.setVisibility(View.VISIBLE);										
										gridlay.startAnimation(alfa);
										progressbar.setVisibility(View.VISIBLE);		
										//adView.setVisibility(View.VISIBLE);
										comienzoJuego();
										vistainformacion.setVisibility(View.INVISIBLE);		
										
									}
								});							 
						 }else{
							 	configGrid();
							 	botonVolverajugar.setVisibility(View.VISIBLE);
							 	botonSonido.setVisibility(View.VISIBLE);
							 	botonSonido.setImageResource(soundPrefs.getBoolean("sonido", true)==true?R.drawable.boton_sonidoonfondo:R.drawable.boton_sonidoofffondo);			
								botonInformacion.setVisibility(View.VISIBLE);			
								textTiempo.setVisibility(View.VISIBLE);
								textTiempo.startAnimation(alfa);	
								gridlay.setVisibility(View.VISIBLE);
								gridlay.startAnimation(alfa);
								progressbar.setVisibility(View.VISIBLE);	
								//adView.setVisibility(View.VISIBLE);
								comienzoJuego();
						 }							
						}
					});					
				}				
			}, 500);	
		}					
	}
		
	//---casos de sumo x y resto y
	
	public void sumaryrestar (int aSumar, int aRestar, int firstElement, int size){
		ArrayList<Integer> auxShuffled = new ArrayList<Integer>();			
		pos = new ArrayList<Integer>();		
		numbers = new ArrayList<Integer>();		
		posMixed = new ArrayList<Integer>();
		numbersMixed = new ArrayList<Integer>();		
		int[] numbersMixedAux = new int[size];
		int[] posMixedAux =new int[size];		
		int elemento;			
			
		elemento= firstElement;		
		numbers.add(0, firstElement);				
		
				
		//Creamos el array de numeros
		for (int i=1; i<size;++i){			
			if(i%2==0){				
				numbers.add(i, elemento+aRestar) ;
				//System.out.println("elemento par :"+ "i:"+ i+" "+numbers.get(i));	
			}		
			else{				
				numbers.add(i, elemento + aSumar) ;
				//System.out.println("elemento impar :"+ "i:"+ i+" "+ numbers.get(i));	
			}							
			elemento = numbers.get(i);		
		}	
		//Creamos el array de sus posiciones y el auxiliar
		for(int i=0; i<size; ++i){			
			pos.add(i);
			auxShuffled.add(i);
		}
		Collections.shuffle(auxShuffled);
	
		//Una vez tenemos numbers y sus respectivas posiciones que vienen dada por el array pos, las 
		//desordenamos a traves de auxShuffed colocandolas en nuevas posiciones al azar. Con los subindices posMixed igual
		for (int i=0; i<size; ++i){
			numbersMixedAux[auxShuffled.get(i)] =  numbers.get(i);				
			posMixedAux[auxShuffled.get(i)] = pos.get(i);			
		}
		
		for (int i=0; i<size; ++i){
			numbersMixed.add(numbersMixedAux[i]);	
			posMixed.add(posMixedAux[i]);
		}

		//Una vez desordenados y relacionados los numeros con sus posiciones a traves de posMixed, sacamos
		//a traves del metodo orden posteriormente el orden en que se encuentran las posiciones posMixed
		//int indice = orden(posMixed).get(elemento);
		
	}
	
	
	//devuelve las posiciones en las que se encuentran los elementos de la lista de menor a mayor. 
	
	public ArrayList<Integer> orden (ArrayList<Integer> listado){
		ArrayList<Integer> lista= new ArrayList<Integer>();				
		for (int i=0; i<listado.size(); ++i){			
			lista.add(i, listado.indexOf(i)) ;			
		}		
		return lista;
	}
	
	public void setSumaRestaInicial(int x, int y, int z, int u, int v, int w){
		
		a=new Random().nextInt(x+y)+z;
		b=new Random().nextInt(u+v)+w;
		
		mostrarOperacion(a,b);
		//new ComboPantalla1(ActivitySecuencias.this, "A�ade "+a+ "y luego "+ b ,R.id.timeattack_layout, R.color.blue, 
			//	false, 1,1.05f,1,1.05f, 3000).run();
		hacerCombo(a,b);
		setGamePaused(true);
		new Timer().schedule(new TimerTask(){ //Mientras lo que dure el combo no se permite tocar ningun elemento. 
			@Override
			public void run() {
				setGamePaused(false);
			}			
		}, 2000);
		
		sumaryrestar(b,a ,0, base);
	}
		
	
	public void configGrid(){				
		parlinear= new ArrayList<LinearLayout>();
		//creamos valores  para asignarlos a las ids de los LinearLayouts que crearemos en Modo Timeattack normal:
		lista = new ArrayList<Integer>();		
		for (int i=0; i<base; ++i){
			lista.add(i);				
		}		
		Collections.shuffle(lista);
		
		
		/*************************************
		 SUMAMOS Y RESTAMOS O LO QUE SEA:
		 **********************/
		
		 setSumaRestaInicial(getInitNumbers()[0],getInitNumbers()[1],getInitNumbers()[2],getInitNumbers()[3]
						,getInitNumbers()[4],getInitNumbers()[5]);
		
		/*******************/		 
		 
		establecerGrid();		
	
		primerElemento= orden(posMixed).get(0); //ponemos el primer elemento		
		establecerPrimerElemento(primerElemento);
	}
	
	public void establecerGrid(){
		
		for (int i =0; i<numFilas;++i){
			for (int j=0; j<numCol; ++j){				
				LinearLayout linear = new LinearLayout(this);			
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();
				
				ImageView ima1 = new ImageView(this);
				ImageView ima2 = new ImageView(this);								
				
				linear.setId(numbersMixed.get(ids));
				linear.setTag(String.valueOf(ids));
				
				//System.out.println("linearids :"+ linear.getId());				
				params.height = altoIm;
				params.width =anchoIm;
				params.columnSpec = GridLayout.spec(j);
				params.rowSpec =GridLayout.spec(i);	
			    //Establecemos los margenes para que quede bien centrado				
				if(j==0){//primera fila
					if(i==0){
						params.setMargins(gapLeft, gapTop, 0, 0);
					}else if(i>0){
						params.setMargins(gapLeft, gap, 0, 0);	
					}					
				}		
				if(j==numCol-1){//ultima fila
					if(i==0){
						params.setMargins(gap, gapTop, gap, 0);
					}else if(i>0){
						params.setMargins(gap, gap, gap, 0);	
					}							
				}
				if(j>0 && j!=(numCol-1)){//filas intermedias
					if(i==0){
						params.setMargins(gap, gapTop, 0, 0);
					}else if(i>0){
						params.setMargins(gap, gap, 0, 0);	
					}										
				}
					
				linear.setLayoutParams(params);									
				
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(anchoIm,altoIm);	
			//	LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
							
				//ponemos un paddig solo a ima1:
				//System.out.println("alto :"+altoIm+" anchoIm :"+anchoIm);
				ima1.setPadding(altoIm/15, anchoIm/15, altoIm/15, anchoIm/15); 
				ima1.setLayoutParams(params2);						
				//ima1.setImageResource(R.drawable.iinterr);	
				ima1.setImageResource(Imagenes.numeros[linear.getId()]);				
				ima1.setVisibility(View.VISIBLE);					
				linear.addView(ima1,0);	
				
					
				ima2.setLayoutParams(params2);
				ima2.setImageResource(Imagenes.numeros[linear.getId()]);				
				ima2.setVisibility(View.GONE);	
				linear.addView(ima2, 1);						
				
				linear.setOnClickListener(new linearListener(linear )); 
				gridlay.addView(linear);				
				++ids;	
				
			}				
		}	
	}
	
	public void establecerPrimerElemento(int position){
		
		LinearLayout lin = (LinearLayout) gridlay.getChildAt(position); //indice correspondiente al primer numero de la serie		
		//numberInicial = lin.getId(); //seleccionamos el primer numero de la serie		
		final ImageView im1= (ImageView) lin.getChildAt(0);
		final ImageView im2 = (ImageView) lin.getChildAt(1);			
		im1.setImageResource(Imagenes.numeros[numbersMixed.get(position)]);
		im2.setImageResource(Imagenes.numeros[numbersMixed.get(position)]);
		lin.setId(OK);
		if(im1.getVisibility()==View.GONE){
			girarImagen(im2, im1, 400);				
			new Timer().schedule(new TimerTask(){
				@Override
				public void run() {			
					handler.post(new Runnable(){
						@Override
						public void run() {							
							girarImagen(im1, im2, 150);
						}						
					});					
				}			
			}, 300);			
		}			
		else{
			girarImagen(im1, im2, 500);	
		}				
				
		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.5f);
		alpha.setRepeatCount(4);
		alpha.setDuration(500);
		lin.setAnimation(alpha);		
	}
	
	class linearListener implements View.OnClickListener{
		LinearLayout linear, lin1;
		ImageView ima1, ima2;		
		ObjectAnimator animarPieza;
		int indice2, indice3,indice4,  tagTocado;
				
		
		public linearListener(LinearLayout linear){
			this.linear=linear;
			animarPieza = ObjectAnimator.ofFloat(linear, "translationX", 10,-10,10,-10,10,-10,10,-10,10,-10);
			animarPieza.setDuration(600);
		}

		@Override
		public void onClick(View v) {
			
			if (!getGamePaused()){
				
				if (parlinear.size()<1){				
					
					if (linear.getId()!=OK ){		
						if (soundPrefs.getBoolean("sonido", true))
							soundSecuencias.play(sonidoTecla);						
						parlinear.add(linear);						
						numClicks++;	
						ima1= (ImageView)parlinear.get(0).getChildAt(0);
						ima2 = (ImageView)parlinear.get(0).getChildAt(1);	
						girarImagen(ima1, ima2, 100);
						
						lin1 = (LinearLayout)parlinear.get(0);								
						
						indice3 = lin1.getId(); //el que tocamos						
						indice2 = numbers.get(indexImagen);	//el que deberia ser
									
						tagTocado = Integer.valueOf((String)lin1.getTag());
						// si son consecutivas, las dejamos las dos arriba
						if(indice2==indice3){	
							if (soundPrefs.getBoolean("sonido", true))
								soundSecuencias.play(sonidoboton25);	
							ViewHelper.setAlpha(lin1, 0.3f);
							animarPieza.start();
							parlinear.get(0).setId(OK);	
							parlinear.remove(0);					
							indexImagen++;
							aciertos++;										
							if(aciertos==corte){			
								hacerOperaciones(get_a_b_Numbers()[0], get_a_b_Numbers()[1],corte);									
								corte=corte+cadencia;								
							}							
							
							if(aciertos==base && (totalTime-progressTime)!=0){ //Terminamos la partida
								setGameOver(true);	
								if (soundPrefs.getBoolean("sonido", true))
									soundSecuencias.play(aplausosSound1);				
								botonInformacion.setVisibility(View.INVISIBLE);								
								textTiempo.setVisibility(View.INVISIBLE);
								progressbar.setVisibility(View.INVISIBLE);
								botonVolverajugar.setVisibility(View.INVISIBLE);
								botonSonido.setVisibility(View.INVISIBLE);
								AlphaAnimation alfa2 = new AlphaAnimation(1.0f, 0.5f);
								alfa2.setDuration(1000);
								alfa2.setFillAfter(true);
								gridlay.startAnimation(alfa2);						
								//timeCrono = SystemClock.elapsedRealtime() - crono.getBase(); //pillamos el tiempo que ha pasado cuando paramos el crono
								//crono.stop();
																
								new Timer().schedule(new TimerTask(){
									@Override
									public void run() {													
										handler.post(new Runnable(){
											@Override
											public void run() {
													new MarcadoresTimeAttack().run();
											}														
										});													
									}											 
								},time);								
							}
						}
						else{ //si no, le damos la vuelta a todos las que se dieron la primera vuelta y a empezar denuevo								
							darVueltaAtodas();
						}				
					}					
				}
			}
		}		
		
		
		public void hacerOperaciones(int a, int b, int corte){
			int j=0;		
			indexImagen=0;
			mostrarOperacion(a,b);
			hacerCombo(a, b);
			setGamePaused(true);
			new Timer().schedule(new TimerTask(){
				@Override
				public void run() {
					setGamePaused(false);
				}			
			}, 600);			
			//pasamos indice3+3 ya que seria el ultimo numero que tocamos + el numero que queremos sumarle, en este caso 3			
			sumaryrestar(a,b,indice3+b, base-corte);
			
			AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.5f);
			alpha.setRepeatCount(4);
			alpha.setDuration(500);
			lin1.setAnimation(alpha);
						
			for (int i=0; i<base; ++i){									
				//volvemos a poner las ids nuevas y cambiamos a los nuevos numeros
				LinearLayout linn = (LinearLayout)gridlay.getChildAt(i);									
				ImageView imagen1 = (ImageView)linn.getChildAt(0);
				ImageView imagen2 = (ImageView)linn.getChildAt(1);						
				if(linn.getId()!=OK){							
					linn.setId(numbersMixed.get(j));	
					imagen2.setImageResource(Imagenes.numeros[linn.getId()]);
					imagen1.setImageResource(Imagenes.numeros[linn.getId()]);				
					j++;					
				}else if(linn.getId()==OK){
					linn.setId(OK);				
				}									
			}
		}
		
		public void darVueltaAtodas(){		
			new Timer().schedule(new TimerTask(){
				@Override
				public void run() {
					handler.post(new Runnable(){
						@Override
						public void run() {
							setSumaRestaInicial(getInitNumbers()[0],getInitNumbers()[1],getInitNumbers()[2],getInitNumbers()[3]
									,getInitNumbers()[4],getInitNumbers()[5]);
							setInicialCorte(getInicialCorte());
							girarImagen(ima2, ima1, 100);			
							for (int i=0; i<base; ++i){
								LinearLayout child = (LinearLayout) gridlay.getChildAt(orden(posMixed).get(i));
								ImageView subchild1 = (ImageView)child.getChildAt(0);
								ImageView subchild2 = (ImageView)child.getChildAt(1);
								if(child.getId()==OK){
									girarImagen(subchild2, subchild1, 100);
									ViewHelper.setAlpha(child, 1f);	
								}								
							}												
							aciertos=1;
							indexImagen=1;											
						}										
					});																	
				}								
			}, 800);
			
			//quitamos el parlinear pasado 800+110 para que no haya problemas si se clica muchas veces un mismo tecla. 
			new Timer().schedule(new TimerTask(){
				@Override
				public void run() {		
					handler.post(new Runnable(){
						@Override
						public void run() {							 
							for (int i=0; i<base; ++i){
								LinearLayout linnn = (LinearLayout)gridlay.getChildAt(i);
								ImageView vista1 = (ImageView)linnn.getChildAt(0);
								ImageView vista2 = (ImageView)linnn.getChildAt(1);
								linnn.setId(numbersMixed.get(i));
								vista1.setImageResource(Imagenes.numeros[linnn.getId()]);
								vista2.setImageResource(Imagenes.numeros[linnn.getId()]);
							}
							primerElemento= orden(posMixed).get(0); //ponemos el primer elemento		
							establecerPrimerElemento(primerElemento);							
							parlinear.remove(0);								
						}						
					});					
				}								
			}, 800+110);			
		}
	}
	
	public void mostrarOperacion(int a, int b){
		if(b>=0 && a>=0){	
					textTiempo.setTextColor(getResources().getColor(R.color.white));
					textTiempo.setText(getResources().getString(R.string.suma)+" "+b+" "+
					getResources().getString(R.string.y)+" "+
					getResources().getString(R.string.suma)+" "+a);
					
		}else if(b>=0 && a<0){			
					textTiempo.setTextColor(getResources().getColor(R.color.white));
					textTiempo.setText(getResources().getString(R.string.suma)+" "+b+" "+
					getResources().getString(R.string.y)+" "+
					getResources().getString(R.string.resta)+" "+a);			
			
		}else if(b<0 && a>=0){		
					textTiempo.setTextColor(getResources().getColor(R.color.white));
					textTiempo.setText(getResources().getString(R.string.resta)+" "+b+" "+
					getResources().getString(R.string.y)+" "+
					getResources().getString(R.string.suma)+" "+a);					
			
		}else if(b<0 && a<0){	
					textTiempo.setTextColor(getResources().getColor(R.color.white));
					textTiempo.setText(getResources().getString(R.string.resta)+" "+b+" "+
					getResources().getString(R.string.y)+" "+
					getResources().getString(R.string.resta)+" "+a);					
		}
	}
	
	
	public void hacerCombo(int a, int b){
		if(b>=0 && a>=0){
			new ComboPantalla1(ActivitySecuencias.this,
					getResources().getString(R.string.suma)+" "+b+" "+"\n"+
					getResources().getString(R.string.y)+" "+
					getResources().getString(R.string.suma)+" "+a
					,R.id.timeattack_layout, getResources().getColor(R.color.white), 
				false, 1,1.05f,1,1.05f, 2000, R.drawable.fondo_combosecuencias).run();
		}else if(b>=0 && a<0){
			new ComboPantalla1(ActivitySecuencias.this,
					getResources().getString(R.string.suma)+" "+b+" "+"\n"+
					getResources().getString(R.string.y)+" "+
					getResources().getString(R.string.resta)+" "+a
					,R.id.timeattack_layout,getResources().getColor(R.color.white), 
				false, 1,1.05f,1,1.05f, 2000, R.drawable.fondo_combosecuencias).run();
			
		}else if(b<0 && a>=0){
			new ComboPantalla1(ActivitySecuencias.this,
					getResources().getString(R.string.resta)+" "+b+" "+"\n"+
					getResources().getString(R.string.y)+" "+
					getResources().getString(R.string.suma)+" "+a
					,R.id.timeattack_layout,getResources().getColor(R.color.white), 
				false, 1,1.05f,1,1.05f, 2000, R.drawable.fondo_combosecuencias).run();
			
		}else if(b<0 && a<0){
			new ComboPantalla1(ActivitySecuencias.this,
					getResources().getString(R.string.resta)+" "+b+" "+"\n"+
					getResources().getString(R.string.y)+" "+
					getResources().getString(R.string.resta)+" "+a
					,R.id.timeattack_layout, getResources().getColor(R.color.white), 
				false, 1,1.05f,1,1.05f, 2000, R.drawable.fondo_combosecuencias).run();
		}
	}
	
	public void girarImagen( ImageView imagen1, ImageView imagen2, int duracion){	
		
		final ImageView visibleImage;
		final ImageView invisibleImage;		
		
		if (imagen1.getVisibility() == View.GONE) {
			visibleImage = imagen2;
			invisibleImage = imagen1;
		} else {
			invisibleImage = imagen2;
			visibleImage = imagen1;
		}		
		final ObjectAnimator goneToVisible = ObjectAnimator.ofFloat(invisibleImage, "rotationY", -90f, 0f);
		ObjectAnimator visibleToGone = ObjectAnimator.ofFloat(visibleImage,"rotationY", 0f, 90f);	
		goneToVisible.setDuration(duracion);		
		visibleToGone.setDuration(duracion);			
		visibleToGone.start();
			
		visibleToGone.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {											
				visibleImage.setVisibility(View.GONE);
				invisibleImage.setVisibility(View.VISIBLE);
				goneToVisible.start();	
			}
		});			
	}
	
	
	
	//Sistema de puntuaciones
		public long scoreSystem(){							
			//Base de cada nivel. Eje para un panel 4x3 la base son 6. 
			long base = numFilas*numCol;							
			long punto = base*10;							
			//El restante de tiempo lo sumamos a la base 
			sumaTotal = punto + (totalTime-progressTime);		
			return sumaTotal;				
		}
		
		public void setTimeThread (){		
			
			final int Puntos = (int)scoreSystem();		
			final int timeToReach= DURACION_THREAD;							
			
			int  timeThreadPuntos = (int)(timeToReach/Puntos);
			int contaPuntos=1;
			
			if (timeThreadPuntos <5){
				while (timeThreadPuntos<5){
					timeThreadPuntos=(int)(timeToReach/(Puntos/contaPuntos));
					contaPuntos++;					
				}				
			}							
			matrizTimeThread[0]=contaPuntos;
			matrizTimeThread[1]=timeThreadPuntos;
						
		}
		
		
		class MarcadoresTimeAttack implements Runnable{
			
			TextView textNivel, textTiempo, textTiemporestante,textNivelLogrado,
			textPuntosTotales, textPuntosAcumulados,	
			textFelicidades, textCompletado;	
			Button backTomenu, nextLevel, botonCompartir, botonScores;
			
			ImageView imagenSuperado;
			LinearLayout  linearnivel,  lineartiempo, lineartiemporestante,
			linearpuntostotales, linearpuntosacumulados;
		
			
			int j=0,k=0,l=0,m=0;
			
			@Override
			public void run() {
				mostrarMarc();	
			}	
			
			public void mostrarMarc(){
				marcadoresMostrados = true;
				//Creamos una view tipo LinearLayout que sera agregada al RelativeLayout para que quede por encima
				LinearLayout panta = new LinearLayout(contexto){				
					LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view =li.inflate(R.layout.timeattack_final_exito, this, true);				
				};
						
				RelativeLayout llay = (RelativeLayout)((Activity) contexto).findViewById(R.id.timeattack_layout);	
				RelativeLayout.LayoutParams relatparams = new RelativeLayout.LayoutParams
					(LayoutParams.MATCH_PARENT, android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
			
				relatparams.setMargins(dm.widthPixels/15,dm.heightPixels/7,
					dm.widthPixels/15, dm.heightPixels/15);
				panta.setLayoutParams(relatparams);
				llay.addView(panta);				
				 
				
				 textNivel= (TextView)panta.findViewById(R.id.text_levelcompleted);
				 textFelicidades =(TextView)panta.findViewById(R.id.text_felicidades);
				 textCompletado=(TextView)panta.findViewById(R.id.text_completado);
				 imagenSuperado = (ImageView)panta.findViewById(R.id.imagen_logosuperado);
			
				 linearnivel = (LinearLayout)panta.findViewById(R.id.linearNivel);
				 linearnivel.setVisibility(LinearLayout.VISIBLE);
				 textNivelLogrado = (TextView)panta.findViewById(R.id.marcador_nivelsecuencias);
				
				 /*
				 linearpuntos = (LinearLayout)panta.findViewById(R.id.linearpuntos);
				 linearpuntos.setVisibility(LinearLayout.VISIBLE);
				 textPuntos =(TextView)panta.findViewById(R.id.marcador_puntostimeattack);
				 */
				 lineartiempo = (LinearLayout)panta.findViewById(R.id.lineartiempo);
				 lineartiempo.setVisibility(LinearLayout.VISIBLE);
				 textTiempo =(TextView)panta.findViewById(R.id.marcador_tiempototaltimeattack);	
				 
				 lineartiemporestante = (LinearLayout)panta.findViewById(R.id.lineartiemporestante);
				 lineartiemporestante.setVisibility(LinearLayout.VISIBLE);
				 textTiemporestante = (TextView)panta.findViewById(R.id.marcador_tiemporestantetimeattack);
				 
				 linearpuntostotales = 	(LinearLayout)panta.findViewById(R.id.linearpuntostotales);	
				 linearpuntostotales.setVisibility(View.VISIBLE);
				 textPuntosTotales =(TextView)panta.findViewById(R.id.marcador_puntostotalestimeattack);
				 
				 linearpuntosacumulados = 	(LinearLayout)panta.findViewById(R.id.linearpuntosacumulados);	
				 linearpuntosacumulados.setVisibility(View.VISIBLE);				 
				 textPuntosAcumulados =(TextView)panta.findViewById(R.id.marcador_puntosacumuladostimeattack);
				
				
				 backTomenu = (Button)panta.findViewById(R.id.marcador_menu_timeattack);
				 backTomenu.setVisibility(View.INVISIBLE);			
				 nextLevel =(Button)panta.findViewById(R.id.marcador_timeattack_botonplay);
				 nextLevel.setVisibility(View.INVISIBLE);
				 botonCompartir = (Button)panta.findViewById(R.id.compartir_nivel);
				 botonCompartir.setVisibility(View.INVISIBLE);
				 botonScores = (Button)panta.findViewById(R.id.global_scores);
							
				/*nivel50*/
				 if(nivel==50){	
					 	if (soundPrefs.getBoolean("sonido", true))	
							soundSecuencias.play(crowdSound);
						textFelicidades.setVisibility(View.INVISIBLE);
						textCompletado.setVisibility(View.INVISIBLE);
						ScaleAnimation anim = new ScaleAnimation(1,3,1,3);
						anim.setDuration(3000);
						anim.setRepeatCount(1);
						anim.setRepeatMode(Animation.REVERSE);				
						imagenSuperado.setImageResource(R.drawable.botoncopa_128);
						imagenSuperado.startAnimation(anim);
					}else{	
						textNivel.setVisibility(View.INVISIBLE);
						RotateAnimation rotarAnimation = new RotateAnimation(360, 0, // de 0 a 360 grados
			                Animation.RELATIVE_TO_SELF, 0.5f,  //pivote valor del filo izquierdo (valores entre 0 y 1 floats)
			                Animation.RELATIVE_TO_SELF, 0.5f); //pivote valor del filo superior
						rotarAnimation.setDuration(200);
						imagenSuperado.startAnimation(rotarAnimation);				
						
					}
				
				//Ponemos un alphaanimation para que aparezcabn los marcadores.
				
				AlphaAnimation alphaanim = new AlphaAnimation(0.5f,1);
				alphaanim.setDuration(400);
				panta.startAnimation(alphaanim);	
				
				
				nextLevel.setOnClickListener(new View.OnClickListener() {				
					@Override
					public void onClick(View arg0) {
						//Mandamos los datos iniciales:		
						Intent i = new Intent (contexto, ActivitySecuencias.class);	
						if (soundPrefs.getBoolean("sonido", true))
							//soundSecuencias.play(sonidoSalir_Nivel);
							sonidoSalir_Nivel.start();
						nivel++; 
						
						if((indexTotalSecuencia-indexTotalAux)>1){
							indexTotalAux++;
						}else{
							indexTotalSecuencia++;
							indexTotalAux++;
						}				
						i.putExtra("nivel", nivel);						
						i.putExtra("indexTotalSecuencia", indexTotalSecuencia);									
						i.putExtra("indexTotalAux", indexTotalAux);						
						i.putExtra("vistaInicio", View.INVISIBLE);			
						
						i.putExtra("acumulado", acumulado);						
						i.putExtra("musicafondo", true);
						startActivity(i);	
						finish();					
					}
				});
				
				backTomenu.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						if (soundPrefs.getBoolean("sonido", true))
							//soundSecuencias.play(sonidoSalir_Nivel);
							sonidoSalir_Nivel.start();
						//nivel50
						if(nivel!=50){					  	
						   if(keybackTocado==0){ //Ponemos una variable keybackTocado para que si pulsamos mucho el boton back no se acumulen elementos al spiner			    			 
			    			  if((indexTotalSecuencia-indexTotalAux)>1){
			    				  indexTotalAux++;
							 }else{
								indexTotalSecuencia++;
								indexTotalAux++;
							}
						   }
						}
						volverMenuInicial();						
					}				
				});
				
				
				botonCompartir.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						if (soundPrefs.getBoolean("sonido", true))
							//soundSecuencias.play(sonidoSalir_Nivel);
							sonidoSalir_Nivel.start();
						Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
						sharingIntent.setType("text/plain"); //Pasaremos solo 
						
						String shareBody = getResources().getString(R.string.sharecontent_puntos1)+" "+
								getResources().getString(R.string.sharecontent_puntos2)+" "+getResources().getString(R.string.titulonumeros)+								
								getResources().getString(R.string.sharecontent_puntos3)+" "+String.valueOf(nivel)+
								getResources().getString(R.string.sharecontent_puntos4)+" "+String.valueOf(scoreSystem())+
								getResources().getString(R.string.sharecontent_puntos5);
						
						sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.sharesubject_puntos)); 
						sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
						startActivity(Intent.createChooser(sharingIntent,getResources().getString(R.string.compartirvia)));	
						
					}
				});
				
				/*
				botonScores.setOnClickListener(new View.OnClickListener() {				
					@Override
					public void onClick(View v) {
						startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
								,getResources().getString(R.string.leaderboard_sequence_numbers))
								,500);						
					}
				});
				*/
						
				//Puntuaciones:
				//Pasado el tiempo de la animacion de rotar, ponemos las puntuaciones
				setTimeThread();
				new Timer().schedule(new TimerTask(){
					@Override
					public void run() {
						handler.post(new Runnable(){
							@Override
							public void run() {
								/*nivel50*/
								if(nivel==50){								
									textFelicidades.setVisibility(View.VISIBLE);
									textCompletado.setVisibility(View.VISIBLE);
									textFelicidades.setText(contexto.getResources().getString(R.string.felicidades));
									textCompletado.setText(contexto.getResources().getString(R.string.modocompletado));
									RotateAnimation rotateAnimation = new RotateAnimation(0, 360, // de 0 a 360 grados
							                Animation.RELATIVE_TO_SELF, 0.5f,  //pivote valor del filo izquierdo (valores entre 0 y 1 floats)
							                Animation.RELATIVE_TO_SELF, 0.5f); //pivote valor del filo superior
							        rotateAnimation.setDuration(500);
									textCompletado.startAnimation(rotateAnimation);				
									
								}else{								
									textNivel.setVisibility(View.VISIBLE);
									textNivel.setText(contexto.getResources().getString(R.string.nivel)+" "+String.valueOf(nivel)
											+" "+contexto.getResources().getString(R.string.completado));
								}								
							}
						});					
						
						//Nivel alcanzado:
						handler.post(new Runnable(){
							@Override
							public void run() {								
								textNivelLogrado.setText(String.valueOf(nivel));
																	
							}							
						});						
										
						//Tiempo:			
						handler.post(new Runnable(){
							@Override
							public void run() {
								textTiempo.setText(String.valueOf(progressTime));		
							}							
						});
												
						//Tiempo restante:								
						handler.post(new Runnable(){
							@Override
							public void run() {
								textTiemporestante.setText(String.valueOf(totalTime-progressTime));		
							}							
						});			
											
						//Puntos totales y acumulado total:
						handler.post(new Runnable(){
							@Override
							public void run() {
								
								textPuntosTotales.setText(String.valueOf(scoreSystem()));
								//Ponemos los puntos acumulados una vez terminado el bucle de los puntos totales 
								acumulado=acumulado+scoreSystem();
								new ScoresHigh(contexto, GAME_PREFS ,acumulado , "highScoresSecuencias").setHighScores() ;
								//subimos el acumulado total a Google play games en caso de estar conectado
								/*
								if(isSignedIn())										
									subirPuntuacion();
								*/
								textPuntosAcumulados.setText(String.valueOf(acumulado));	
								//Hacemos visible los botones una vez se haya completado el bucle del contador para que la variable acumulado se actualice.
								//menos en el ultimo nivel:
								/*nivel50*/
								if(nivel==50)
									nextLevel.setVisibility(View.GONE);
								else{
									nextLevel.setVisibility(View.VISIBLE);
									//Ponemos animacion alfa para que resalte
									AlphaAnimation alfa = new AlphaAnimation(0.7f,1f);
									alfa.setDuration(500);
									alfa.setRepeatMode(Animation.REVERSE);
									alfa.setRepeatCount(Animation.INFINITE);
									nextLevel.startAnimation(alfa);
									}										
								backTomenu.setVisibility(View.VISIBLE);	
								botonCompartir.setVisibility(View.VISIBLE);
								/*
								if(isSignedIn()) //solo se ve si estamos conectados				
									botonScores.setVisibility(View.VISIBLE);
								*/
							}						
						});					
					}				
				}, 200);			
			}			
		}
		
		
		//liberamos la memoria de todos los sonidos en el onDestroy. 
		//Por ejemplo cuando pulsamos al siguiente nivel, antes del finish(), llamamos a onDestroy		
		@Override 
		protected void onDestroy() {			
			super.onDestroy();
			//adView.destroy();
			soundSecuencias.unloadAll();
			sonidoSalir_Nivel.release();
		}
	
		
		@Override //Ponemos juego en foreground por pulsar boton home o que entre una llamada
		protected void onPause() {		
			super.onPause();		
			//adView.pause();
			setGamePaused(true);				
			//musicafondo.pause();
		}

		@Override //vuelta de foreground
		protected void onResume() {		
			super.onResume();
			//adView.resume();
			setGamePaused(false);					
		}
	
		public void botonSonido(View v){
			if (soundPrefs.getBoolean("sonido", true)){
				botonSonido.setImageResource(R.drawable.boton_sonidoofffondo);
				editor.putBoolean("sonido", false);
		    	editor.commit();
				//musicafondo.pause();
			}else{
				botonSonido.setImageResource(R.drawable.boton_sonidoonfondo);
				editor.putBoolean("sonido", true);
		    	editor.commit();
				//musicafondo.start();
			}
		}

		public void setGameOver(boolean terminado){
			this.gameover = terminado;
		}

		public synchronized boolean getGameOver(){
			return gameover;
		}	
		
		public void  setGamePaused(boolean pausado){
			this.gamePaused= pausado;		
		}
		
		public  boolean getGamePaused(){
			return gamePaused;
		}
		
		public void botonPlaySecuencias (View v){	
			Intent i = new Intent(this, ActivitySecuencias.class);		
			if (soundPrefs.getBoolean("sonido", true))
				soundSecuencias.play(sonidoPlay);
			indexTotalAux=index;	
			
			//Usamos for con if en vez de un switch case
			//nivel50//
			for ( int j=0; j<50; ++j){
				if (index+1==j+1)
					i.putExtra("nivel", j+1);				
			}			
		
			i.putExtra("indexTotalSecuencia", indexTotalSecuencia);			
			i.putExtra("indexTotalAux", indexTotalAux);	
			i.putExtra("vistaInicio", View.INVISIBLE);				
			i.putExtra("musicafondo", true);			
			startActivity(i);		
			finish();			
		}
		
		public void volverMenuInicial(){
			
			
			Intent i = new Intent(contexto,ActivitySecuencias.class);			
			i.putExtra("nivel", nivel);			
			i.putExtra("indexTotalSecuencia", indexTotalSecuencia);			
			i.putExtra("indexTotalAux", indexTotalAux);			
			i.putExtra("vistaInicio", View.VISIBLE);	
			i.putExtra("vistaspinner", View.VISIBLE);
			startActivity(i);
			finish();	
		}

		/*
		public void botonVolveraMenuFracaso (View v){
			//mp3.start();
			volverMenuInicial();
		}
		*/
		//BOTON SALIR:
		public void botonmenusalir(){
			Intent i = new Intent(this, ActivityModos.class);	
			//i.putExtra("indexTotalSecuencia", nivel);	
			i.putExtra("indexTotalSecuencia", indexTotalSecuencia);	
			i.putExtra("modoJuego", "secuencias");
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Ver http://www.htcmania.com/showthread.php?t=735248
			startActivity(i);
			finish();	
		}
		
		
		public void botonMenuSalirSecuencias (View v){
			Intent i = new Intent(this, ActivityModos.class);			
			if (soundPrefs.getBoolean("sonido", true))
				sonidoSalir_Nivel.start();
			//i.putExtra("indexTotalSecuencia", nivel);	
			i.putExtra("indexTotalSecuencia", indexTotalSecuencia);	
			i.putExtra("modoJuego", "secuencias");
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Ver http://www.htcmania.com/showthread.php?t=735248
			startActivity(i);
			finish();	
			
		}
		
		
		public void volveraMenu(){
			 AlertDialog.Builder alertbox = new AlertDialog.Builder(contexto);
		        alertbox.setIcon(R.drawable.boton_tick);
		        alertbox.setTitle(R.string.deseasalir);
		        alertbox.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface arg0, int arg1) {
		            	if(!marcadoresMostrados)
		        			setGameOver(true);		        			
			        	volverMenuInicial();	      		
		        		//musicafondo.stop();
		            }
		        });
		        alertbox.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface arg0, int arg1) {
		            	if(!marcadoresMostrados)
		            	  setGamePaused(false);		              	            	
		          }
		        });
		        AlertDialog alerta = alertbox.create();
		        alerta.show();	
		}
		
		//si pulsamos back en cualquier parte del juego en donde no sea visible vistaInicio, volvemos al menu inicial.
		//Si estamos en el menu donde aparece la seleccion de nivel y pulsamos atras, salimos del modo 
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(vInicio==View.INVISIBLE){
		    if ((keyCode == KeyEvent.KEYCODE_BACK)) {	
		    	if(marcadoresMostrados){ //si se ven los marcadores
		    		//ponemos un cuadro de dialogo en todos los niveles menos en el ultimo y tambien subimos un elemento en el spiner
		    		//nivel50
		    		 if(nivel!=50){	//Si no estamos En el ultimo nivel  sumamos un elemento al spinner		    			 
		    			 if(keybackTocado==0){ //Ponemos una variable keybackTocado para que si pulsamos mucho el boton back no se acumulen elementos al spiner
		    				 keybackTocado++;
		    				 if((indexTotalSecuencia-indexTotalAux)>1){
		    						indexTotalAux++;
		    				}else{
		    					indexTotalSecuencia++;
		    					indexTotalAux++;
		    				}		    				
		    			 }
		    			 volveraMenu();	
		    				
		    		}else{//si es el ultimo nivel entonces no mostramos el cuadro dialogo ni sumamos elementos al spinner	  
		    			volverMenuInicial(); //volvemos al menu inicial sin mostrar cuadro de dialogo
		    		}
		    	}else{ //si no se ven los marcadores , es decir durante el juego, mostramos el cuadro de dialogo
		    		setGamePaused(true);
		    		volveraMenu();
		    	}	 
		    }
		    
			}else if(vInicio==View.VISIBLE){
				botonmenusalir();
			}
		        return super.onKeyDown(keyCode, event);
		}
		
		//En realidad es el boton Informacion que mostrara las instrucciones de como jugar. 
		public void botonvolveraJugar (View v){			
			// hacerCombo(a,b);	
			vistainformacion.setVisibility(View.VISIBLE);
			botonOKinformativo = (Button)findViewById(R.id.ok_text_inforsecuencias);
			botonOKinformativo.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {					
					vistainformacion.setVisibility(View.INVISIBLE);
				}
			});
			
		}
		
		//Este es el boton Volver a jugar que lo colocamos a la derecha
		public void botonPause(View v){
			setGameOver(true);
			//mantenemos la variable nivel		
			Intent i = new Intent (contexto, ActivitySecuencias.class);		
			//mp3.start();						
			i.putExtra("nivel", nivel);			
			i.putExtra("indexTotalSecuencia", indexTotalSecuencia);			
			i.putExtra("indexTotalAux", indexTotalAux);			
			i.putExtra("vistaInicio", View.INVISIBLE);						
			
			i.putExtra("acumulado", acumulado );			
			i.putExtra("musicafondo", true);
			startActivity(i);	
			finish();		
			
		}
		
		public void botonVolveraMenuFracaso (View v){			
			volverMenuInicial();
		}
		
		
		public void botonVolveraJugarFracaso (View v){
			//mantenemos la variable nivel		
			Intent i = new Intent (contexto, ActivitySecuencias.class);		
			//mp3.start();						
			i.putExtra("nivel", nivel);			
			i.putExtra("indexTotalSecuencia", indexTotalSecuencia);			
			i.putExtra("indexTotalAux", indexTotalAux);			
			i.putExtra("vistaInicio", View.INVISIBLE);						
			
			i.putExtra("acumulado", acumulado );			
			i.putExtra("musicafondo", true);
			startActivity(i);	
			finish();		
		}		
		
}
