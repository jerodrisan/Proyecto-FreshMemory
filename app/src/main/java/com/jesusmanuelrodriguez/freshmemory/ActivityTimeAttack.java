package com.jesusmanuelrodriguez.freshmemory;
//new activitytimeattack

/*Nota A.

 Ese progressBar tambien lo podiamos haber hecho de usando la propiedad unicamente progressbar.setprogress
 en vez de incrementProgressBy. En ese caso seria algo mas complejo:   
 new Thread(){
 @Override
 public void run() {						
 while(progressTime<totalTime){	
 progressTime = getTime();						
 handler.post(new Runnable(){
 @Override
 public void run() {
 //para poner el progreso en la view no es necesario usar el handler en el caso del progressbar
 //pero si quisiesemos poner la propiedad visible o invisible si seria necesario el handler
 progressbar.setProgress(progressTime);									
 }								
 });	
 }					
 }					
 private int getTime(){						
 try {
 Thread.sleep(1000);							
 } catch (InterruptedException e) {							
 e.printStackTrace();
 }
 return i++;
 }

 }.start();
 */

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
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageButton;
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
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.ObjectAnimator;
//import android.widget.GridLayout;

public class ActivityTimeAttack extends Activity {
	
	Timer timerPB = null;

	long inictime=0, diftime=0, acumtime = 0, sumaacumtime = 0;
	/*
	 * MediaPlayer click1Sound,click2Sound, estrellitasSound, aplausosSound1,
	 * OhhSounds1, mp2, mp3, crowdSound;
	 */

	static final int time = 1000; // tiempo que dura la animacion
	int progressTime = 0, totalTime=0;

	String modo;

	int numFilas, numCol;

	int aciertos = 0;
	int numClicks = 0;
	int pb;

	int combo = 1; // Contador de combos, cada vez que se encuentran dos o mas parejas consecutivas
	int comboAux = 0;
	int totalCombos=0; // total de combos
	
	
	float acumulado = 0; // Acumulado de puntuacion segun se avanza de nivel.
	int nivel; // Contador de niveles
	int retardo = (int) time / 1000; // Retardo para poner marcadores
	int aditionalTime = 0;

	int pulsar = 0;

	Context contexto;
	DisplayMetrics dm;

	ArrayList<LinearLayout> parlinear;

	Handler handler = new Handler();

	ViewStub vistaInicio, vistaFinalFracaso, vistainformacion;
	ImageView botonPausar, botonvolverAjugar, botonSonido;
	ProgressBar progressbar;
	TextView textTiempo, marcadorPuntos, marcadorTiempoRestante, marcadorPuntosTotales, marcadorTiempoEmpleado, marcadorNivel,
			marcadorNivelnosuperado, marcadorAcumulados;
	GridLayout gridlay;	
	ImageButton botonPlayInicial, botonPlayFinal;
	Spinner spinnerNivel, spinnerTematica;
	LinearLayout panta;
	
	boolean vistaFracaso = false;
	boolean vistadeInicio= false;
	boolean vistadeInformacion=false;
	boolean gameover = false;
	boolean comenzarJuego = true;
	boolean gamePaused = false;
	boolean progressBParado = false;
	// boolean setMusicaFondo=false;
	boolean isRelaciones = false;
	boolean marcadoresMostrados = false;
	int keybackTocado = 0;

	ArrayList<String> levels;

	int index; // indice del elemento seleccionado del Spinner
	int index2; // indice del tema seleccionado
	int indexColor1, indexColor2; // indices usados en modo retoColores para
									// saber si el nivel tiene relacion con
									// palaras o no.
	int indexTotal; // total de elementos que se llenaran en el Spinner en modo normal
	int indexTotalRelaciones; // lo mismo pero en modo relaciones
	int indexTotalRetoColores;
	int indexTotalAux;

		
	SharedPreferences gamePrefs;
	public static final String GAME_PREFS = "Pruebar";
	private static final int DURACION_THREAD = 2000;
	private static final int NIVEL_MAXI_LOGRO_MATES=19;
	private static final int NIVEL_MAXI_LOGRO_ROMANOS=10;
	private static final int NIVEL_MAXI_LOGRO_COLORES=26;

	int MAX_VOLUME = 100; // volumen m�ximo de referencia

	int[] matrizTimeThread;

	Button botonOKinformativo;
	TextView textoinfoColor;
	ImageView imagen1infoColor, imagen2infoColor;
	//AdView adView;

	SharedPreferences soundPrefs, conexionScores, matesPreferencias, romanosPreferencias, coloresPreferencias;
	SharedPreferences.Editor editor, editorScores, editorMates, editorRomanos, editorColores;
	
	public static final String SONIDO = "sonido";
	public static final String CONEXION = "conexion";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_timeattack);
		
		setPreferencesAdsYSounds();		
		// Para controlar volumen con los botones.
		// Ver:http://stackoverflow.com/questions/628659/how-can-i-manage-audio-volumes-sanely-in-my-android-app/674207#674207
		// No es necesario implementarlo en ActivityBoard:
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		//setUpSounds(); //Configuracion de todos los sonidos
		
		configPantalla();				

		gamePrefs = getSharedPreferences(GAME_PREFS, 0);
		matrizTimeThread = new int[4];

		contexto = this;	

		// pillamos extras
		getExtras();
		// establecemos numero de filas, columnas, totalTime y en caso de retocolores otras variables DEPENDIENDO DEL NIVEL

		inflarSpinner();

		initViews(); // Inicializacion de todas las views		

		// Preferencias conexion
		conexionScores = this.getSharedPreferences(CONEXION, 0);

		// Conectamos en la activity con google play services
		/*
		if (conexionScores.getBoolean(CONEXION, true))
			beginUserInitiatedSignIn();
		*/
	} // FIN ON CREATE

	// METODOS GOOGLE PLAY GAMES
	/*
	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
		// Melor no poner nada porque si no sale en cada nivel que pasemos
		// Toast.makeText(this,
		// getResources().getString(R.string.you_are_connected),
		// Toast.LENGTH_LONG).show();
	}
	
	public void subirPuntuacion() {
		// solo subimos puntuacion si estamos conectados:
		
			if (modo.equals("normal")) {
				String[] savedScore = gamePrefs.getString("highScores", "").split("\\|");
				String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));	
				Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_time_challenge),Integer.parseInt(punt));
				if(nivel==5 && acumulado>=600){
					Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_cronotimer));	
				}
			} else if (modo.equals("relaciones")) {
				String[] savedScore = gamePrefs.getString("highScores2", "").split("\\|");
				String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));	
				Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_time_challenge_plus),Integer.parseInt(punt));
				if(nivel==10 && acumulado>=5500){
					Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_cronotimer_plus));	
				}
				
			} else if (modo.equals("retocolores")) {
				String[] savedScore = gamePrefs.getString("highScores3", "").split("\\|");
				String punt= savedScore[0].substring(0, savedScore[0].indexOf(" "));	
				Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_color_challenge),Integer.parseInt(punt));
				if(nivel==7 && progressTime + (aditionalTime * aciertos) - retardo <=50){
					Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_coloring));	
				}
				if(nivel==10 && acumulado>=10000){
					Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_coloring_10));	
				}
			}
		
	}

	public void checkLogroIncremental(){
		int restoMates = matesPreferencias.getInt("restoMates", 19);
		int restoRomanos = romanosPreferencias.getInt("restoRomanos", 10);
		int restoColores= coloresPreferencias.getInt("restoColores", 26);
		int j=0, k=0, m=0;
		//boolean[] array=new boolean[19];
		ArrayList<Boolean> array = new ArrayList<Boolean>();
		boolean check= false;		
		if(modo.equals("relaciones")){
			if(index2==2){ //Tema de Sumas
				check= matesPreferencias.getBoolean(String.valueOf(nivel), false);				
				if(!check){ //si no existe la preferencia la creamos y ademas ponemos el nivel como true , es decir, ya esta jugado
					editorMates.putBoolean(String.valueOf(nivel), true);	
					editorMates.commit();
					//Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_king_of_sums),1);	
				
					//si queremos que se incremente tambien estando offline y cuando se haga online aparezca
					for(int i=0; i<NIVEL_MAXI_LOGRO_MATES; ++i){						
						array.add(i,matesPreferencias.getBoolean(String.valueOf(i+1), false));						
						if(array.get(i))
							++j;					
					}

					if(isSignedIn()){
						restoMates= restoMates-j;
						editorMates.putInt("restoMates", restoMates);
						editorMates.commit();
						if(restoMates>=0)
							Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_king_of_sums),j);					
					}

				}
			}
			if(index2==4){ //Tema de Numeros romanos
				if(nivel<=10){
					check= romanosPreferencias.getBoolean(String.valueOf(nivel), false);
					if(!check){
						editorRomanos.putBoolean(String.valueOf(nivel), true);
						editorRomanos.commit();
						//Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_romans),1);
						//si queremos que se incremente tambien estando offline y cuando se haga online aparezca
						for(int i=0; i<NIVEL_MAXI_LOGRO_ROMANOS; ++i){
							array.add(i,romanosPreferencias.getBoolean(String.valueOf(i+1), false));
							if(array.get(i))
								++k;					
						}

						if(isSignedIn()){
							restoRomanos= restoRomanos-k;
							editorRomanos.putInt("restoRomanos", restoRomanos);
							editorRomanos.commit();
							if(restoRomanos>=0)
								Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_romans),k);					
						}

					}
				}
			}			
		}else if(modo.equals("retocolores")){
			check = coloresPreferencias.getBoolean(String.valueOf(nivel), false);
			if(!check){
				editorColores.putBoolean(String.valueOf(nivel), true);
				editorColores.commit();
				//Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_king_of_colors),1);	
				for(int i=0; i<NIVEL_MAXI_LOGRO_COLORES; ++i){
					array.add(i,coloresPreferencias.getBoolean(String.valueOf(i+1), false));
					if(array.get(i))
						++m;					
				}

				if(isSignedIn()){
					restoColores= restoColores-m;
					editorColores.putInt("restoColores", restoColores);
					editorColores.commit();
					if(restoColores>=0)
						Games.Achievements.increment(getApiClient(), getResources().getString(R.string.achievement_king_of_colors),m);	
				}

			}
		}					
	}		
	*/
	
	// --------------------------FIN GOOGLE PLAY GAMES----------------------------
	

	// Preferencias para sonido y para anuncio admob
	public void setPreferencesAdsYSounds() {

		// Preferencias para el sonido. Cuando creamos la actividad , por defecto el sonido estar� activado y asi lo guardaremos
		soundPrefs = this.getSharedPreferences(SONIDO, 0);
		editor = soundPrefs.edit();
		editor.putBoolean("sonido", soundPrefs.getBoolean("sonido", true));
		editor.commit();

		// publicidad
		/*
		adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		*/
		//Preferencias para modo Mates , Numeros romanos y reto colores
		matesPreferencias = this.getSharedPreferences("mates", 0);
		editorMates= matesPreferencias.edit();		
		
		romanosPreferencias = this.getSharedPreferences("romanos", 0);
		editorRomanos= romanosPreferencias.edit();
		
		coloresPreferencias = this.getSharedPreferences("colores", 0);
		editorColores = coloresPreferencias.edit();
		
	}

	public void setUpSounds() {
		// Sonidos:
		/*
		 * click1Sound = MediaPlayer.create(this, R.raw.keyboardtypesngl);
		 * click1Sound.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		 * 
		 * click2Sound = MediaPlayer.create(this, R.raw.keyboardtypesngl);
		 * click2Sound.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		 * 
		 * estrellitasSound = MediaPlayer.create(this, R.raw.estrellitas);
		 * estrellitasSound.setVolume(setCustomVolumen(70),
		 * setCustomVolumen(70));
		 * 
		 * aplausosSound1 = MediaPlayer.create(this, R.raw.applau22);
		 * aplausosSound1.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		 * 
		 * OhhSounds1 = MediaPlayer.create(this, R.raw.ohh1);
		 * OhhSounds1.setVolume(setCustomVolumen(80), setCustomVolumen(80));
		 * 
		 * mp2=MediaPlayer.create(this, R.raw.keyboardtypesngl);
		 * mp2.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		 * 
		 * mp3 = MediaPlayer.create(this, R.raw.button54);
		 * mp3.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		 * 
		 * 
		 * crowdSound = MediaPlayer.create(this, R.raw.crwdbugl);
		 * crowdSound.setVolume(setCustomVolumen(80), setCustomVolumen(80));
		 */
	}
	
	// funcion para establecer volumen customizado
	public float setCustomVolumen(int soundVolume) { // volumen que queremos poner (la mitad del total de volumen) o lo que queramos!!!
		return (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME))); // ver:http://stackoverflow.com/questions/5215459/android-mediaplayer-setvolume-function
	}
	
	
	public void configPantalla() {
		// Pillamos dimensiones pantalla:
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// Ponemos la pantalla full screen:
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}

	public void getExtras() {

		Bundle extras = this.getIntent().getExtras();

		modo = extras.getString("tipoModo");

		if (modo.equals("normal")) {
			indexTotal = extras.getInt("indexTotal");
		} else if (modo.equals("relaciones")) {
			indexTotalRelaciones = extras.getInt("indexTotalRelaciones");
		} else if (modo.equals("retocolores")) {
			indexTotalRetoColores = extras.getInt("indexTotalRetoColores");
		}
	}

	public void setupNiveles(int nivel) {

		switch (nivel) {
		case 1:

			if (modo.equals("retocolores")) { // Hacer parejas de colores iguales.
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor1),	R.drawable.color4, R.drawable.color4);
				isRelaciones = false;
				indexColor1 = 0; // usamos este indice solo cuando isRelaciones==false y lo aumentamos en una unidad
				numFilas = 3;
				numCol = 2;
				totalTime = 10;

			} else {
				numFilas = 2;	numCol = 2;		totalTime = 8;
			}			
			// * setupNiveles(false, true, getResources().getString(R.string.textoinfocolor1),R.drawable.color4, R.drawable.color4, false, 0, 3,2,11, 2,2,6);			
			break;
		case 2:
			if (modo.equals("retocolores")) { // Hacer parejas de colores iguales.
				isRelaciones = false;
				indexColor1 = 0;
				numFilas = 4;	numCol = 3; 	totalTime = 29; //20;
			} else {
				numFilas = 3;	numCol = 2;		totalTime = 16; //10;
			}
			break;

		case 3:
			if (modo.equals("retocolores")) { // relacionar colores con su nombre (Ej el color rosa con el nombre rosa)
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor3),	R.drawable.color9, R.drawable.color9verde);
				isRelaciones = true;
				indexColor2 = 0;
				numFilas = 4;	numCol = 3;		totalTime = 29; //20;
			} else {
				numFilas = 4;	numCol = 2;		totalTime = 16; //13;
			}
			break;
		case 4:
			if (modo.equals("retocolores")) { // relacionar colores con objetos (Ej color azul con cielo)
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor2),	R.drawable.color9, R.drawable.color9_rana);
				isRelaciones = true;
				indexColor2 = 1;
				numFilas = 4;	numCol = 3;		totalTime = 35; //24;
			} else {
				numFilas = 5;	numCol = 2;		totalTime = 27; //19;
			}
			break;
		case 5:
			if (modo.equals("retocolores")) { // Relacionar nombres pintados en colores con su opuesto (Ej nombre rosa pintado de blanco con nombre blanco pintado de rosa)
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor4),	R.drawable.color7_rojo_verde,R.drawable.color7_verde_rojo);
				isRelaciones = true;
				indexColor2 = 2;
				numFilas = 3;	numCol = 2;		totalTime = 16; //10;
			} else {
				numFilas = 4;	numCol = 3;		totalTime = 29;  //20;
			}
			break;
		case 6:
			if (modo.equals("retocolores")) { // Relacionar nombres pintados en/ colores con su opuesto (Ej nombre rosa pintado de blanco con nombre blanco pintado de rosa)
				isRelaciones = true;
				indexColor2 = 3;
				numFilas = 4;	numCol = 3;		totalTime = 31; //22;
			} else {
				numFilas = 4;	numCol = 4;		totalTime = 42; //32;
			}
			break;

		case 7:
			if (modo.equals("retocolores")) { // relacionar colores con su nombre (Ej el color rosa con el nombre rosa)
				isRelaciones = true;
				indexColor2 = 8;
				numFilas = 7;	numCol = 4;		totalTime = 54; //40;
			} else {
				numFilas = 5;	numCol = 4;		totalTime = 44; //30;
			}
			break;

		case 8:
			if (modo.equals("retocolores")) { // relacionar colores con objetos (Ej color azul con cielo)
				isRelaciones = true;
				indexColor2 = 9;
				numFilas = 7;	numCol = 4;		totalTime = 54; //40;
			} else {
				numFilas = 6;	numCol = 4;		totalTime = 46; //32;
			}
			break;

		case 9:
			if (modo.equals("retocolores")) { // Relacionar nombres pintados en colores con su opuesto (Ej nombre rosa pintado de blanco con nombre blanco pintado de rosa)
				isRelaciones = true;
				indexColor2 = 4;
				numFilas = 7;	numCol = 4;		totalTime = 58; //42;
			} else {
				numFilas = 7;	numCol = 4;		totalTime = 58; //42;
			}
			break;
		case 10:
			if (modo.equals("retocolores")) { // Relacionar diferentes iconos  COLOR NARANJA
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor5),	R.drawable.colornaranja13, R.drawable.colornaranja13);
				isRelaciones = false;
				indexColor1 = 1;
				numFilas = 8;	numCol = 4;		totalTime = 66;//47;
			} else {
				numFilas = 6;	numCol = 5;		totalTime = 64;//42;
			}
			break;
		case 11:
			if (modo.equals("retocolores")) { // Relacionar diferentes iconos . COLOR ROSA
				isRelaciones = false;
				indexColor1 = 2;
				numFilas = 8;	numCol = 4;		totalTime = 47;
			} else {
				numFilas = 8;	numCol = 4;		totalTime = 48;
			}
			break;
		case 12:
			if (modo.equals("retocolores")) { // Relacionar diferentes iconos . COLOR ROJO
				isRelaciones = false;
				indexColor1 = 3;
				numFilas = 8;	numCol = 5;		totalTime = 60;
			} else {
				numFilas = 9;	numCol = 4;		totalTime = 54;
			}
			break;
		case 13:
			if (modo.equals("retocolores")) { // Relacionar diferentes iconos . COLOR VERDE
				isRelaciones = false;
				indexColor1 = 4;
				numFilas = 8;	numCol = 6;		totalTime = 63;
			} else {
				numFilas = 8;	numCol = 5;		totalTime = 60;
			}
			break;
		case 14:
			if (modo.equals("retocolores")) { // Relacionar diferentes iconos . COLOR AZUL
				isRelaciones = false;
				indexColor1 = 5;
				numFilas = 8;	numCol = 6;		totalTime = 66;
			} else {
				numFilas = 7;	numCol = 6;		totalTime = 63;
			}
			break;
		case 15:
			if (modo.equals("retocolores")) { // Relacionar diferentes iconos . COLOR AMARILLO
				isRelaciones = false;
				indexColor1 = 6;
				numFilas = 8;	numCol = 6;		totalTime = 66;
			} else {
				numFilas = 8;	numCol = 6;		totalTime = 68;
			}
			break;
		case 16:
			if (modo.equals("retocolores")) { // Relacionar diferentes iconos . COLOR NEGRO
				isRelaciones = false;
				indexColor1 = 7;
				numFilas = 8;	numCol = 6;		totalTime = 66;
			} else {
				numFilas = 9;	numCol = 6;		totalTime = 75;
			}
			break;
		case 17:
			if (modo.equals("retocolores")) { // Relaciones franjas horizontales colores verde azul rojo
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor6),	R.drawable.colorfranja1, R.drawable.colorfranja1);
				isRelaciones = false;
				indexColor1 = 8;
				numFilas = 4;	numCol = 3;		totalTime = 22;
			} else {
				numFilas = 10;	numCol = 6;		totalTime = 80;
			}
			break;
		case 18:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales y verticales colores verde azul y rojo
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor7),	R.drawable.colorfranja1, R.drawable.colorfranjav1);
				isRelaciones = true;
				indexColor2 = 5;
				numFilas = 4;	numCol = 3;		totalTime = 20;
			} else {
				numFilas = 8;	numCol = 8;		totalTime = 90;
			}
			break;
		case 19:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales y verticales colores verde azul y rojo
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor6),	R.drawable.colorfranjav1, R.drawable.colorfranjav1);
				isRelaciones = false;
				indexColor1 = 9;
				numFilas = 6;	numCol = 4;		totalTime = 32;
			} else {
				numFilas = 10;	numCol = 7;		totalTime = 90;
			}
			break;
		case 20:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales colores morado
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor6),	R.drawable.colorfranja2_1, R.drawable.colorfranja2_1);
				isRelaciones = false;
				indexColor1 = 10;
				numFilas = 4;	numCol = 3;		totalTime = 17;
			}
			break;
		case 21:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales y verticales colores morado
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor7),	R.drawable.colorfranja2_1, R.drawable.colorfranjav2_1);
				isRelaciones = true;
				indexColor2 = 6;
				numFilas = 4;	numCol = 3;		totalTime = 17;
			}
			break;
		case 22:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales y verticales colores morado
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor6),	R.drawable.colorfranjav2_1, R.drawable.colorfranjav2_1);
				isRelaciones = false;
				indexColor1 = 11;
				numFilas = 6;	numCol = 4;		totalTime = 35;
			}
			break;
		case 23:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales colores azul
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor6),	R.drawable.colorfranja3_1, R.drawable.colorfranja3_1);
				isRelaciones = false;
				indexColor1 = 12;
				numFilas = 8;	numCol = 6;		totalTime = 80;
			}
			break;
		case 24:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales con su homonimo vertical colores azul
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor7),	R.drawable.colorfranja3_1, R.drawable.colorfranjav3_1);
				isRelaciones = true;
				indexColor2 = 7;
				numFilas = 8;	numCol = 6;		totalTime = 80;
			}
			break;
		case 25:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales colores azul
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor6),	R.drawable.colorfranja3_1, R.drawable.colorfranja3_1);
				isRelaciones = false;
				indexColor1 = 13;
				numFilas = 10;	numCol = 7;		totalTime = 90;
			}
			break;
		case 26:
			if (modo.equals("retocolores")) { // // Relaciones franjas horizontales colores gris
				comenzarJuego = false;
				setVistaInformation(getResources().getString(R.string.textoinfocolor6),	R.drawable.colorfranja4_2, R.drawable.colorfranja4_2);
				isRelaciones = false;
				indexColor1 = 14;
				numFilas = 8;	numCol = 6;		totalTime = 150;
			}
			break;
		}

	}

	/*
	 * Si queremos hacerlo mas simplificado ponemos esta funcion: public void
	 * setupNiveles (boolean comenzarJuegor, boolean setvistainfo, String texto,
	 * int im1, int im2, boolean isRelacioness, int indexColor, int numFilasA,
	 * int numColA, int totalTimeA, int numFilasB, int numColB, int totalTimeB
	 * ){
	 * 
	 * if (modo.equals("retocolores")){ // Hacer parejas de colores iguales.
	 * comenzarJuego= comenzarJuegor; if(setvistainfo)
	 * setVistaInformation(texto, im1, im2 ); isRelaciones= isRelacioness;
	 * if(isRelaciones) indexColor1=indexColor; //usamos este indice solo cuando
	 * isRelaciones==false y lo aumentamos en una unidad else if(!isRelaciones)
	 * indexColor2=indexColor; numFilas=numFilasA; numCol=numColA;
	 * totalTime=totalTimeA;
	 * 
	 * }else{ numFilas=numFilasB ; numCol = numColB; totalTime=totalTimeB; } }
	 */

	

	/*
	 * public void startMusicaFondo(){ new Timer().schedule(new TimerTask(){
	 * 
	 * @Override public void run() { if(soundPrefs.getBoolean("sonido", true))
	 * musicafondo.start(); } }, 200); }
	 */

	

	public void inflarSpinner() { // Ponerlo en otra clase para que ocupe menos menoria??????

		levels = new ArrayList<String>();

		if (modo.equals("retocolores")) {
			levels.add(0, contexto.getResources().getString(R.string.nivel)	+ " 1		(3x2)");
			levels.add(1, contexto.getResources().getString(R.string.nivel)	+ " 2		(4x3)");
			levels.add(2, contexto.getResources().getString(R.string.nivel)	+ " 3		(4x3)");
			levels.add(3, contexto.getResources().getString(R.string.nivel)	+ " 4		(4x3)");
			levels.add(4, contexto.getResources().getString(R.string.nivel)	+ " 5		(3x2)");
			levels.add(5, contexto.getResources().getString(R.string.nivel)	+ " 6		(4x3)");
			levels.add(6, contexto.getResources().getString(R.string.nivel)	+ " 7		(7x4)");
			levels.add(7, contexto.getResources().getString(R.string.nivel)	+ " 8		(7x4)");
			levels.add(8, contexto.getResources().getString(R.string.nivel)	+ " 9		(7x4)");
			levels.add(9, contexto.getResources().getString(R.string.nivel)	+ " 10		(8x4)");
			levels.add(10, contexto.getResources().getString(R.string.nivel)+ " 11		(8x4)");
			levels.add(11, contexto.getResources().getString(R.string.nivel)+ " 12		(8x5)");
			levels.add(12, contexto.getResources().getString(R.string.nivel)+ " 13		(8x6)");
			levels.add(13, contexto.getResources().getString(R.string.nivel)+ " 14		(8x6)");
			levels.add(14, contexto.getResources().getString(R.string.nivel)+ " 15		(8x6)");
			levels.add(15, contexto.getResources().getString(R.string.nivel)+ " 16		(8x6)");
			levels.add(16, contexto.getResources().getString(R.string.nivel)+ " 17		(4x3)");
			levels.add(17, contexto.getResources().getString(R.string.nivel)+ " 18		(4x3)");
			levels.add(18, contexto.getResources().getString(R.string.nivel)+ " 19		(6x4)");
			levels.add(19, contexto.getResources().getString(R.string.nivel)+ " 20		(4x3)");
			levels.add(20, contexto.getResources().getString(R.string.nivel)+ " 21		(8x6)");
			levels.add(21, contexto.getResources().getString(R.string.nivel)+ " 22		(4x3)");
			levels.add(22, contexto.getResources().getString(R.string.nivel)+ " 23		(6x4)");
			levels.add(23, contexto.getResources().getString(R.string.nivel)+ " 24		(8x6)");
			levels.add(24, contexto.getResources().getString(R.string.nivel)+ " 25		(10x7)");
			levels.add(25, contexto.getResources().getString(R.string.nivel)+ "  26  	FINAL");

		} else {
			levels.add(0, contexto.getResources().getString(R.string.nivel)+ " 1		(2x2)");
			levels.add(1, contexto.getResources().getString(R.string.nivel)+ " 2		(3x2)");
			levels.add(2, contexto.getResources().getString(R.string.nivel)+ " 3		(4x2)");
			levels.add(3, contexto.getResources().getString(R.string.nivel)+ " 4		(5x2)");
			levels.add(4, contexto.getResources().getString(R.string.nivel)+ " 5		(4x3)");
			levels.add(5, contexto.getResources().getString(R.string.nivel)+ " 6		(4x4)");
			levels.add(6, contexto.getResources().getString(R.string.nivel)+ " 7		(5x4)");
			levels.add(7, contexto.getResources().getString(R.string.nivel)+ " 8		(6x4)");
			levels.add(8, contexto.getResources().getString(R.string.nivel)+ " 9		(7x4)");
			levels.add(9, contexto.getResources().getString(R.string.nivel)+ " 10		(6x5)");
			levels.add(10, contexto.getResources().getString(R.string.nivel)+ " 11		(8x4)");
			levels.add(11, contexto.getResources().getString(R.string.nivel)+ " 12		(9x4)");
			levels.add(12, contexto.getResources().getString(R.string.nivel)+ " 13		(8x5)");
			levels.add(13, contexto.getResources().getString(R.string.nivel)+ " 14		(7x6)");
			levels.add(14, contexto.getResources().getString(R.string.nivel)+ " 15		(8x6)");
			levels.add(15, contexto.getResources().getString(R.string.nivel)+ " 16		(9x6)");
			if (modo.equals("normal")) {
				levels.add(16, contexto.getResources().getString(R.string.nivel) + " 17		(10x6)");
			} else if (modo.equals("relaciones")) {
				levels.add(16, contexto.getResources().getString(R.string.nivel) + "	 17	  FINAL");
			}
			levels.add(17, contexto.getResources().getString(R.string.nivel)+ " 18		(8x8)");
			levels.add(18, contexto.getResources().getString(R.string.nivel)+ "  19	  FINAL");
		}
	}
	
	// Inicializacion de vistas en el Oncreate, inicializamos y ponemos visible el stubview del principio para selecionar nivel, tema en los spinner
	public void initViews() {

		// Mostramos la vista: mode_timeattack_inicio.xml que viene implicita en el ViewStub

		if (modo.equals("normal")) {
			vistaInicio = (ViewStub) findViewById(R.id.viewstubTimeattackInicio);
		} else if (modo.equals("relaciones")) {
			vistaInicio = (ViewStub) findViewById(R.id.viewstubTimeattackInicioRelaciones);
		} else if (modo.equals("retocolores")) {
			vistaInicio = (ViewStub) findViewById(R.id.viewstubInicioRetoColores);
		}
		vistaInicio.inflate();
		//vistaInicio.setVisibility(View.VISIBLE);
		setVistaDeInicio(true);
		
		// idem con timeattack_final_fracaso.xml:
		vistaFinalFracaso = (ViewStub) findViewById(R.id.viewstubTimeattackFinalfracaso);
		vistaFinalFracaso.inflate();
		//vistaFinalFracaso.setVisibility(View.INVISIBLE);
		setVistaFracaso(false);
		
		//Vista informaciion para reto colores
		vistainformacion = (ViewStub) findViewById(R.id.viewstubinforsecuencias3);
		vistainformacion.inflate();
		setVistaInformacion(false);
		

		// BotonPausar
		botonPausar = (ImageView) this.findViewById(R.id.timeattack_botonpause);

		// Boton Sonido
		botonSonido = (ImageView) this.findViewById(R.id.timeattack_botonsonido);

		// Boton volveraJugar
		botonvolverAjugar = (ImageView) this.findViewById(R.id.timeattack_botonbolverajugar);

		// Barra de progreso
		progressbar = (ProgressBar) this.findViewById(R.id.timeattack_progressbar);		

		// gridview:
		gridlay = (GridLayout) findViewById(R.id.grid_timeattack);
		gridlay.setVisibility(View.INVISIBLE);
		
		// tiempo no superado e infotiempo
		marcadorNivelnosuperado = (TextView) this.findViewById(R.id.text_levelnosuperado);
		textTiempo = (TextView) this.findViewById(R.id.timeattack_infotiempo);
		textTiempo.setText(String.valueOf(totalTime)+" sec");
		
		// INICIACION DEL SPINNER PARA SELECCION DE NIVEL una vez inflado el stubview
		if (modo.equals("normal")) {
			spinnerNivel = (Spinner) this.findViewById(R.id.timeattack_spiner);
		} else if (modo.equals("relaciones")) {
			spinnerNivel = (Spinner) this.findViewById(R.id.timeattack_spiner_relaciones);
		} else if (modo.equals("retocolores")) {
			spinnerNivel = (Spinner) this.findViewById(R.id.retocolores_spiner);
		}
		spinnerNivel.setPrompt("Niveles");
		
		setInvisibleViews();
		
		setSpinners();
		
		

	}
	
	public void setSpinners(){				
				
				/*
				 Si sacamos la lista de un stringarray hecho en un array en Resources:
				 
				  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.niveles_timeattack,
				  android.R.layout.simple_spinner_item);
				  adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
				  spinnerNivel.setAdapter(adapter);
				  
				 */

				// Meteremos los elementos de forma dinamica:

				ArrayList<String> niveles = new ArrayList<String>();
				if (modo.equals("normal")) {
					for (int i = 0; i < indexTotal; ++i)
						niveles.add(i, levels.get(i));
				} else if (modo.equals("relaciones")) {
					for (int i = 0; i < indexTotalRelaciones; ++i)
						niveles.add(i, levels.get(i));
				} else if (modo.equals("retocolores")) {
					for (int i = 0; i < indexTotalRetoColores; ++i)
						niveles.add(i, levels.get(i));
				}
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, niveles); 
				// spinner_item.xml creado manualmente, se pueden usar los que vienen por defecto
				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerNivel.setAdapter(adapter2);
				
				spinnerNivel.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView, View view,
							int position, long id) {
						// Con este truco solo cambiamos la apariencia del primer elemento del spinner!!!
						((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
						// ((TextView)parentView.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
						((TextView) parentView.getChildAt(0)).setTextSize(22);
						index = parentView.getSelectedItemPosition();
											
						
						if (index!=0) if(soundPrefs.getBoolean("sonido", true))
							ActivityModosSeleccion.soundClick1.start();
						
						// Dependiendo del nivel tocado ponemos el nivel que sea
						indexTotalAux = index;
						// Asignamos el nivel dependiendo del nivel del spinner tocado nivel26
						for (int i = 0; i < 26; ++i) {
							if ((index + 1) == i + 1) {
								//nivel = i + 1;
								setLevel(i+1);
								break;
							}
						}
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
				spinnerNivel.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						if(soundPrefs.getBoolean("sonido", true))
							ActivityModosSeleccion.soundClick1.start();
						
						return false;

					}
				});

				// Pasamos los strings que estan en la clase Imagenes de int a string para ser pasados por el adaptador.
				String[] valoresClasico = new String[Imagenes.spinnerValores.length];
				String[] valoresRelaciones = new String[Imagenes.spinnerValores2.length];
				for (int i = 0; i < Imagenes.spinnerValores.length; ++i) {
					valoresClasico[i] = getResources().getString(Imagenes.spinnerValores[i]);
					valoresRelaciones[i] = getResources().getString(Imagenes.spinnerValores2[i]);
				}

				// SELECCION DE CATEGORIA EN EL SEGUNDO SPINNER
				if (modo.equals("normal")) {
					spinnerTematica = (Spinner) this.findViewById(R.id.timeattack_spiner_temas);
					spinnerTematica.setAdapter(new MyAdapter(this,R.layout.spinner_customtimeattack, valoresClasico));
					setSpinnerItemSelected_And_onTouchListener();

				} else if (modo.equals("relaciones")) {
					spinnerTematica = (Spinner) this.findViewById(R.id.timeattack_spiner_relacionesTemas);
					spinnerTematica.setAdapter(new MyAdapter(this,R.layout.spinner_customtimeattack, valoresRelaciones));
					setSpinnerItemSelected_And_onTouchListener();
				}
				/*
				 * if(modo.equals("normal")) spinnerTematica.setAdapter(new
				 * MyAdapter(this, R.layout.spinner_customtimeattack, spinerVal)); else
				 * if(modo.equals("relaciones")) spinnerTematica.setAdapter(new
				 * MyAdapter(this, R.layout.spinner_customtimeattack, spinerVal2));
				 */
		
		
	}

	public void setSpinnerItemSelected_And_onTouchListener() {
		spinnerTematica.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View view,
					int position, long id) {
				// if(vInicio==0){
				// Solo si la vista inicio es visible seleccionamos el item y si no lo vamos mandando por intent para que se pueda mantener el tema seleccionado:
				index2 = parentView.getSelectedItemPosition();
				
				if (index2!=0) if(soundPrefs.getBoolean("sonido", true))
					ActivityModosSeleccion.soundClick1.start();
				
				// }
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		spinnerTematica.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(soundPrefs.getBoolean("sonido", true))
					ActivityModosSeleccion.soundClick1.start();
				
				return false;
			}
		});

	}
		

	// CLASE ADAPTADOR PARA SPINNER DE IMAGENES en timeattack
	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context ctx, int txtViewResourceId,String[] spinnerValores) {
			super(ctx, txtViewResourceId, spinnerValores);
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(R.layout.spinner_customtimeattack, parent, false);

			TextView subSpinner = (TextView) mySpinner.findViewById(R.id.text_main_seen);
			ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.left_pic);
			if (modo.equals("normal")) {
				subSpinner.setText(Imagenes.spinnerValores[position]);
				left_icon.setImageResource(Imagenes.spinnerImages[position]);

			} else if (modo.equals("relaciones")) {
				subSpinner.setText(Imagenes.spinnerValores2[position]);
				left_icon.setImageResource(Imagenes.spinnerImages2[position]);
			}
			return mySpinner;
		}
	}
	
	public void configGrid(int numC, int numF) {

		// CONFIGURAMOS GAPS

		// Ojo por defecto se reservo espacio para publi en relacion: publi = dm.heightPixels/6;
		int publi = dm.heightPixels / 6; // Publicidad

		int gapRefer = dm.widthPixels / 62; // = 6dp = 11.61 pixels
		int sumHorizGapRef = (3 + 1) * gapRefer; // Para 3 columnas calculamos 4 huecos y sumamos el total de los 4.
		int sumVertGapRef = (4 + 1) * gapRefer;

		int sumAnchoImRef = dm.widthPixels - sumHorizGapRef; // Espacio total de las fichas juntas restando la suma de los huecos horizontales
		int sumAltoImRef = (dm.heightPixels - publi) - sumVertGapRef; // Idem para filas pero restando la publicidad

		int gap = sumHorizGapRef / (numC + 1); // hueco dependiendo del numero
												// de col
		int gapLeft = gap; // hueco izquierdo
		int gapTop = gap;
		int anchoIm = (sumAnchoImRef / numC); // ancho de cada ficha dependiendo
												// del num col
		int altoIm = (sumAltoImRef / numF); // idem para filas

		float relac = (float) altoIm / (float) anchoIm;

		// Igualamos anchura de la imagen con altura en caso de que la relacion entre ambas sea menor que un tanto por ciento y si no ... las dejamos como estan
		if (anchoIm > altoIm) {
			if (relac < 0.9) { // Ponemos un 10% de deformacion (o lo que sea). Si queremos menos subimos el indice
				// O igualamos anchuras , o bien: subimos un poquito la anchura para que no quede todo tan cuadradito: anchoIm=altoIm;
				anchoIm = (int) (altoIm / 0.9);
				gapLeft = (dm.widthPixels - (anchoIm * numC)) / 2;
			}
		} else if (altoIm > anchoIm) {
			if ((1 / relac) < 0.9) {
				// O igualamos alturas , o bien: subimos un poquito la altura para que no quede todo tan cuadradito: altoIm =anchoIm;
				altoIm = (int) (anchoIm / 0.9);
				gapTop = ((dm.heightPixels - publi) - (altoIm * numF)) / 2;
			}
		}
		
		// CONFIGURAMOS EL RESTO

		int base = (numFilas * numCol) / 2;

		parlinear = new ArrayList<LinearLayout>();

		// creamos pares de valores iguales para asignarlos a las ids de los LinearLayouts que crearemos en Modo Timeattack normal:
		ArrayList<String> lista = new ArrayList<String>();
		for (int i = 0; i < base; ++i) {
			lista.add(String.valueOf(i));
			lista.add(String.valueOf(i));
		}
		Collections.shuffle(lista);
		// Valores para caso de relaciones
		ArrayList<String> listaRelac = new ArrayList<String>();
		ArrayList<String> listaPalab = new ArrayList<String>();
		ArrayList<Integer> indice = new ArrayList<Integer>();

		for (int i = 0; i < (numFilas * numCol) / 2; ++i) {
			listaRelac.add(String.valueOf(i));
			listaPalab.add(String.valueOf(i));
			indice.add(0);
			indice.add(1);
		}
		Collections.shuffle(listaRelac);
		Collections.shuffle(listaPalab);
		Collections.shuffle(indice);

		// Si queremos que cambienn distintas imagenes en cada pantalla y no sean siempre las mismas hay que "shuflear" los arrays
		// (si queremos que siempre sea lo mismo entonces no usamos el metodo shuffleArray y ponemos directamente Imagenes.colores[indexColor1]

		// Para relaciones:
		int[][] arraysuffleRelac = Imagenes.shuffleArrayDoble(Imagenes.relac[index2][0], Imagenes.relac[index2][1]);

		// Para colores:
		int[] arraysuffleColores = Imagenes.shuffleArray(Imagenes.colores[indexColor1]);

		// Para relaciones modo Colores:
		int[][] arraysuffleRelColores = Imagenes.shuffleArrayDoble(Imagenes.relac2[indexColor2][0],	Imagenes.relac2[indexColor2][1]);
		int ids = 0, ids1 = 0, ids2 = 0;

		for (int i = 0; i < numFilas; ++i) {
			for (int j = 0; j < numCol; ++j) {
				LinearLayout linear = new LinearLayout(this);
				AutoResizeTextView ima1 = new AutoResizeTextView(this);
				AutoResizeTextView ima2 = new AutoResizeTextView(this);
				// AutoResizeTextView ima3 = new AutoResizeTextView(this);
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();

				if (modo.equals("relaciones")) {
					if (indice.get(ids) == 0) { // palabras
						linear.setId(Integer.parseInt(listaPalab.get(ids1)));
						ima2.setBackgroundResource(R.drawable.agris);
						if (index2 == 2) { // Ponemos el generador de sumas aleatorias en modo suma
							ima2.setText(Imagenes.genNumAleatorios()[linear.getId()]);
						} else if (index2 == 4) {
							ima2.setText(Imagenes.numerosRomanosPalabras2[linear.getId()]);
						} else {
							// ima2.setText(Imagenes.relac[index2][0][linear.getId()]);
							ima2.setText(arraysuffleRelac[0][linear.getId()]);
						}
						ima2.setTextColor(contexto.getResources().getColor(	R.color.black));
						ima2.resizeText();
						ima2.setTypeface(Typeface.DEFAULT_BOLD);
						ima2.setGravity(Gravity.CENTER);
						++ids1;

					}
					if (indice.get(ids) == 1) { // imagenes
						linear.setId(Integer.parseInt(listaRelac.get(ids2)));
						if (index2 == 2) { // En caso de ser el modo suma lo hacemos mas senciilo empezando por los numeros 1, 2, 3, 4 .. para que no sea tan complicado
							ima2.setBackgroundResource(Imagenes.numeros2[linear.getId()]);
						} else if (index2 == 4) { // idem en caso de numeros romanos
							ima2.setBackgroundResource(Imagenes.numerosRomanos2[linear.getId()]);
						} else { // En caso contrario :
							ima2.setBackgroundResource(arraysuffleRelac[1][linear.getId()]);
						}
						++ids2;
					}
				}
				if (modo.equals("normal")) {
					linear.setId(Integer.parseInt(lista.get(ids)));
					ima2.setBackgroundResource(Imagenes.clasic[index2][linear.getId()]);
				}
				if (modo.equals("retocolores")) {
					if (!isRelaciones) {
						linear.setId(Integer.parseInt(lista.get(ids)));
						ima2.setBackgroundResource(arraysuffleColores[linear.getId()]);
						// ima2.setBackgroundResource(Imagenes.colores[indexColor1][linear.getId()]);
						// ima1.setBackgroundResource(Imagenes.colores[indexColor1][linear.getId()]);
					} else if (isRelaciones) {
						if (indice.get(ids) == 0) { // palabras
							linear.setId(Integer.parseInt(listaPalab.get(ids1)));
							ima2.setBackgroundResource(arraysuffleRelColores[1][linear.getId()]);
							// ima2.setBackgroundResource(Imagenes.relac2[indexColor2][1][linear.getId()]);
							// ima1.setBackgroundResource(Imagenes.relac2[indexColor2][1][linear.getId()]);
							++ids1;
						}
						if (indice.get(ids) == 1) { // imagenes
							linear.setId(Integer.parseInt(listaRelac.get(ids2)));
							ima2.setBackgroundResource(arraysuffleRelColores[0][linear.getId()]);
							// ima2.setBackgroundResource(Imagenes.relac2[indexColor2][0][linear.getId()]);
							// ima1.setBackgroundResource(Imagenes.relac2[indexColor2][0][linear.getId()]);
							++ids2;
						}
					}
				}

				params.height = altoIm;
				params.width = anchoIm;
				params.columnSpec = GridLayout.spec(j);
				params.rowSpec = GridLayout.spec(i);
				// Establecemos los margenes para que quede bien centrado
				if (j == 0) {// primera fila
					if (i == 0) {
						params.setMargins(gapLeft, gapTop, 0, 0);
					} else if (i > 0) {
						params.setMargins(gapLeft, gap, 0, 0);
					}
				}
				if (j == numCol - 1) {// ultima fila
					if (i == 0) {
						params.setMargins(gap, gapTop, gap, 0);
					} else if (i > 0) {
						params.setMargins(gap, gap, gap, 0);
					}
				}
				if (j > 0 && j != (numCol - 1)) {// filas intermedias
					if (i == 0) {
						params.setMargins(gap, gapTop, 0, 0);
					} else if (i > 0) {
						params.setMargins(gap, gap, 0, 0);
					}
				}

				linear.setLayoutParams(params);

				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(anchoIm, altoIm);
				// LinearLayout.LayoutParams params2 = new
				// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				// android.widget.LinearLayout.LayoutParams.MATCH_PARENT);

				// ponemos un paddig para el icono de informacion:
				ima1.setPadding(20, 20, 20, 20);
				ima1.setLayoutParams(params2);
				 ima1.setBackgroundResource(Imagenes.relac[index2][1][linear.getId()]);
				 ima1.setBackgroundResource(Imagenes.clasic[index2][linear.getId()]);
				ima1.setBackgroundResource(R.drawable.iinterr);

				ima1.setVisibility(View.VISIBLE);
				linear.addView(ima1, 0);

				ima2.setLayoutParams(params2);
				// No pondremos el padding para imagenes peque�as queda mucho
				// mejor
				// ima2.setPadding(10, 10, 10, 10);
				ima2.setVisibility(View.GONE);
				// Ponemos un tag a ima2 para que en caso de pulsar dos veces
				// sobre la misma imagen no contabilice como acertado
				ima2.setTag(String.valueOf(ids));
				linear.addView(ima2, 1);

				linear.setOnClickListener(new linearListener(linear));
				gridlay.addView(linear);
				++ids;

			}
		}
	}
	
	public void comienzoJuego() {
		comenzarJuego=true; //en caso de Reto Colores si estaba puesto como false , lo cambiamos siempre a true
		// sonido pitido comienzo juego un seg.
				
		
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				activateGridlay(true);
				new HiloComienzoJuego().start();				
			}
			
		}, 500);
		
	}
	
	class HiloComienzoJuego extends Thread {
		
		@Override
		public void run() {
			
			progressbar.setMax(totalTime);
			while (progressTime < totalTime & !getGameOver()) {
				if (!getGamePaused()) {
					try {
						Thread.sleep(1000);
						if (getGamePaused())
							continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!getStopProgressBar())
						progressbar.incrementProgressBy(1);						
					progressTime++;
					handler.post(new Runnable() {
						@Override
						public void run() {
							textTiempo.setText(String.valueOf((totalTime - progressTime) < 60 ? totalTime - progressTime
											: ((totalTime - progressTime) / 60)
													+ ":"
													+ String.valueOf((totalTime - progressTime) % 60 > 9 ? (totalTime - progressTime) % 60
															: "0"+ (totalTime - progressTime) % 60)) + " sec");
						}
					});
					if ((totalTime - progressTime) < 5	&& (totalTime - progressTime) > 0) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								if (!getGameOver()) {
									textTiempo.setTextColor(Color.RED);
									new ComboPantalla1(contexto, String.valueOf(totalTime - progressTime),R.id.timeattack_layout, contexto
													.getResources().getColor(R.color.red),false, 1, 3, 1, 3, 1000,	R.drawable.fondo_combotransparente)
											.run();
								}
							}
						});
					}

				} // fin if
			}// fin while
			
			if(!getGameOver()) if(soundPrefs.getBoolean("sonido", true)){
				ActivityModosSeleccion.OhhSounds1.start(); }
			
			handler.post(new Runnable() {
				@Override
				public void run() {
					botonvolverAjugar.setVisibility(View.INVISIBLE);
					botonPausar.setVisibility(View.INVISIBLE);
					botonSonido.setVisibility(View.INVISIBLE);
					progressbar.setVisibility(View.INVISIBLE);
					textTiempo.setVisibility(View.INVISIBLE);
					gridlay.setVisibility(View.INVISIBLE);
					if (!getGameOver())						
						handler.postDelayed(new Runnable() {
							@Override
								public void run() {
									marcadorNivelnosuperado.setText(contexto.getResources().getString(R.string.nivel)
									+ " "+ String.valueOf(nivel)+ " "+ contexto.getResources().getString(R.string.nosuperado));
									//vistaFinalFracaso.setVisibility(View.VISIBLE);
									setVistaFracaso(true);										
									}
						}, time*2);
							
				}
			});
		}
	}

	

	// Mostramos 10 mejores puntuaciones del modo
	// Si queremos poner un boton mostrando los marcadores en esta ventana:
	/*
	 * public void botonHighscores(View v){
	 * vistaHighScores.setVisibility(View.VISIBLE);
	 * vistaInicio.setVisibility(View.INVISIBLE); //get text view TextView
	 * scoreView = (TextView)findViewById(R.id.timeattack_high_scores_list);
	 * //get shared prefs SharedPreferences scorePrefs =
	 * getSharedPreferences(GAME_PREFS, 0); //get scores String[] savedScores =
	 * scorePrefs.getString("highScores", "").split("\\|"); //build string
	 * StringBuilder scoreBuild = new StringBuilder(""); //Si queremos poner las
	 * posiciones usamos el indice i for(int i=0; i<savedScores.length; ++i){
	 * //En caso de poner subindices a�adimos esto
	 * :scoreBuild.append(String.valueOf(i+1)+"     -     ");
	 * scoreBuild.append(savedScores[i]+"\n"); }
	 * 
	 * //display scores scoreView.setMovementMethod(new
	 * ScrollingMovementMethod()); scoreView.setText(scoreBuild.toString()); }
	 */
	
	public void activateGridlay(boolean setHabilitar) {
		for (int i = 0; i < gridlay.getChildCount(); ++i) {
			// si las vistas hijas son LinearLayout
			if (gridlay.getChildAt(i).getClass() == LinearLayout.class) {
				// recorremos las vistas hijas Linearlayout para diferenciar entre botones e ImageVies
				LinearLayout ll = (LinearLayout) gridlay.getChildAt(i);
				ll.setEnabled(setHabilitar);
			}
		}
	}
	
	public void setVistaInformation(String texto, int imagen1, int imagen2) {	
		
		//vistainformacion.setVisibility(View.VISIBLE);
		setVistaInformacion(true);
		botonOKinformativo = (Button) findViewById(R.id.ok_text_inforcolores);
		textoinfoColor = (TextView) findViewById(R.id.text_infor_colores);
		textoinfoColor.setText(texto);
		imagen1infoColor = (ImageView) findViewById(R.id.imagen_infocolor1);
		imagen1infoColor.setImageResource(imagen1);
		imagen2infoColor = (ImageView) findViewById(R.id.imagen_infocolor2);
		imagen2infoColor.setImageResource(imagen2);
		botonOKinformativo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(soundPrefs.getBoolean("sonido", true))
					ActivityModosSeleccion.soundClick1.start();
				
				comienzoJuego();
				setVistaInformacion(false);
			}
		});
		// }
	}
	
	//--------------INICIO SETTERS Y GETTERS
	
	public void setVistaInformacion(boolean vistainfo){
		if(vistainfo)
			vistainformacion.setVisibility(View.VISIBLE);
		else
			vistainformacion.setVisibility(View.INVISIBLE);
		this.vistadeInformacion= vistainfo;		
	}
			
	public boolean getVistaInformacion(){
		return this.vistadeInformacion;
	}
	
	
	public void setVistaDeInicio(boolean vistainicio){
		if(vistainicio)
			vistaInicio.setVisibility(View.VISIBLE);			
		else
			vistaInicio.setVisibility(View.INVISIBLE);
		this.vistadeInicio=vistainicio;		
	}
	
	public boolean getVistaDeInicio(){
		return this.vistadeInicio;
	}
	
	public void setVistaFracaso (boolean vistafracaso){
		if(vistafracaso){
			vistaFinalFracaso.setVisibility(View.VISIBLE);
		}else
			vistaFinalFracaso.setVisibility(View.INVISIBLE);				
		this.vistaFracaso=vistafracaso;
	}
	
	public boolean getVistaFracaso(){
		return this.vistaFracaso;
	}
	
	
	public void setVisibleViews(){
		
		//vistaInicio.setVisibility(View.INVISIBLE);
		setVistaDeInicio(false);
		
		gridlay.setVisibility(View.VISIBLE);
		
		TranslateAnimation translateGridlay = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1,
				Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		
		translateGridlay.setDuration(500);
		gridlay.setAnimation(translateGridlay);
		
		progressbar.setVisibility(View.VISIBLE);
		botonPausar.setVisibility(View.VISIBLE);
		botonvolverAjugar.setVisibility(View.VISIBLE);
		botonSonido.setVisibility(View.VISIBLE);
		botonSonido.setImageResource(soundPrefs.getBoolean("sonido", true) == true ? R.drawable.boton_sonidoonfondo : R.drawable.boton_sonidoofffondo);		
		//adView.setVisibility(View.VISIBLE);
		textTiempo.setVisibility(View.VISIBLE);
						
		textTiempo.setText(String.valueOf(totalTime)+" sec");
		textTiempo.setTextColor(contexto.getResources().getColor(R.color.white));
		
	}
	
	
	public void setInvisibleViews(){
				
		textTiempo.setVisibility(View.INVISIBLE);		
		progressbar.setVisibility(View.INVISIBLE);
		botonPausar.setVisibility(View.INVISIBLE);
		botonvolverAjugar.setVisibility(View.INVISIBLE);
		botonSonido.setVisibility(View.INVISIBLE);		
		//adView.setVisibility(View.INVISIBLE);
	}
	
	
	public void setInitVariables(){
		progressbar.setProgress(0);
		progressTime=0;
		aciertos = 0;
		numClicks=0;
		
		//Iniciamos valores para tiempos adicionales:
		timerPB=null;
		inictime=0; diftime=0; acumtime = 0; sumaacumtime = 0;
		
		combo = 1; // Contador de combos, cada vez que se encuentran dos o mas parejas consecutivas
		comboAux = 0;
		totalCombos=0; 
		
		keybackTocado=0;		
		aditionalTime=0;
			
	}

	public void setLevel(int level){
		this.nivel=level;		
	}
	
	public int getLevel(){
		return this.nivel;
	}
	
	
	public void setGameOver(boolean terminado) {
		this.gameover = terminado;
	}

	public synchronized boolean getGameOver() {
		return gameover;
	}
	
	public void setStopProgressBar(boolean stop) {
		this.progressBParado = stop;
	}

	public boolean getStopProgressBar() {
		return progressBParado;
	}

	public void setGamePaused(boolean pausado) {
		this.gamePaused = pausado;
	}

	// es recomendable que el acceso a gamePaused sea "synchronized", ya que podr�a coincidir una lectura y escritura a la vez al estar en hilos
	// distintos
	public synchronized boolean getGamePaused() {
		return gamePaused;
	}
	
	//----------FIN GETTERS Y SETTERS
	
	//--------- INICIO BOTONES --------------
	
	public void botonPlayInicio(View v) {
		
		if(soundPrefs.getBoolean("sonido", true))
			ActivityModosSeleccion.soundPlay.start();
		
		setGameOver(false);
		int level= getLevel();
		setupNiveles(level);				

		configGrid(numCol, numFilas); // Configuracion del grid y disposicion de celdas
		
		activateGridlay(false); // desactivamos de primeras el grid para luego volverlo a activar una vez que comience el hilo

		
		setVisibleViews(); // Hacemos visibles las views del juego
		setInitVariables();  //Inicializamos variables
		
		acumulado=0;
		
		// Comenzamos el juego
		if (comenzarJuego)
			comienzoJuego();

	}

	public void botonSonido(View v) {
		if (soundPrefs.getBoolean("sonido", true)) {
			botonSonido.setImageResource(R.drawable.boton_sonidoofffondo);
			editor.putBoolean("sonido", false);
			editor.commit();
			// musicafondo.pause();
		} else {
			botonSonido.setImageResource(R.drawable.boton_sonidoonfondo);
			editor.putBoolean("sonido", true);
			editor.commit();
			// musicafondo.start();
		}
	}



	public void botonPause(View v) {
		if (pulsar == 0) {
			botonPausar.setImageResource(R.drawable.boton_reanudar);
			pulsar = 1;
			setGamePaused(true);
			// gridlay.setAlpha(0.5f);
			ViewHelper.setAlpha(gridlay, 0.5f);
			/*
			 * if(soundPrefs.getBoolean("sonido", true)) musicafondo.pause();
			 */
		} else {
			botonPausar.setImageResource(R.drawable.boton_pausa);
			pulsar = 0;
			setGamePaused(false);
			// gridlay.setAlpha(1.0f);
			ViewHelper.setAlpha(gridlay, 1f);
			/*
			 * if(soundPrefs.getBoolean("sonido", true)) musicafondo.start();
			 */
		}
	}

	public void botonvolveraJugar(View v) {
		// mantenemos la variable nivel
		setGameOver(true);
			
		gridlay.removeAllViews(); //quitamos todas las views del gridlay
		setInvisibleViews();			
		
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				setGameOver(false);				
				marcadoresMostrados=false;		
				if(!parlinear.isEmpty())
					parlinear.clear(); // reiniciamos el parlinear	
						
				setupNiveles(nivel);
				configGrid(numCol, numFilas); // Configuracion del grid y disposicion de celdas				

				activateGridlay(false); // desactivamos de primeras el grid para luego volverlo a activar una vez que comience el hilo
																									
				setVisibleViews();					
				setInitVariables();
						
				// Comenzamos el juego
				if (comenzarJuego)
					comienzoJuego();						
			}					
		}, 1000);				
				
		
	}

	public void botonVolveraJugarFracaso(View v) {
		// mantenemos la variable nivel		
		
		if(soundPrefs.getBoolean("sonido", true))
			ActivityModosSeleccion.soundClick1.start();
		
		setGameOver(true);
		setVistaFracaso(false);
		if(!parlinear.isEmpty())
			parlinear.clear(); // reiniciamos el parlinear		
		gridlay.removeAllViews(); //quitamos todas las views del gridlay
		setInvisibleViews();		
		
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				setGameOver(false);				
				marcadoresMostrados=false;						
				setupNiveles(nivel);
				configGrid(numCol, numFilas); // Configuracion del grid y disposicion de celdas				

				activateGridlay(false); // desactivamos de primeras el grid para luego volverlo a activar una vez que comience el hilo
																									
				setVisibleViews();					
				setInitVariables();
						
				// Comenzamos el juego
				if (comenzarJuego)
					comienzoJuego();						
			}					
		}, 1000);				
			
	
	}

	public void volveraMenu() {
		// Ponemos un alertdialog para preguntar si realmente queremos salir.
		// En caso afirmativo perdemos los progresos de la puntuacion con lo
		// cual ponemos la puntuacion obtenida
		// y no se acumulan mas puntos
		AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
		alert.setTitle(R.string.deseasalir);
		// alert.setMessage(R.string.perderapuntuaciones);
		alert.setIcon(R.drawable.boton_tick);
		alert.setPositiveButton(R.string.si,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						marcadoresMostrados=false;
						volverMenuInicial();
					}
				});
		alert.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		AlertDialog alerta = alert.create();
		alerta.show();

	}

	public void botonVolveraMenuFracaso(View v) {
		
		if(soundPrefs.getBoolean("sonido", true))
			ActivityModosSeleccion.soundClick1.start();
		volverMenuInicial();
		 
		
		setVistaFracaso(false);
		//vistaInicio.setVisibility(View.VISIBLE);		
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {						
				setVistaDeInicio(true);		
			}					
		}, 300);						
				
		setSpinners();
		
		if(panta!=null)
			panta.setVisibility(View.INVISIBLE);
				
		if(!getGameOver()){ //si estamos en el transcurso del juego, tendriamos que terminar juego y limpiar 
			setGameOver(true);
			if(!parlinear.isEmpty())
				parlinear.clear(); // reiniciamos el parlinear		
			gridlay.removeAllViews(); //quitamos todas las views del gridlay
			setInvisibleViews();
		}
	}

	// BOTON SALIR:
	public void botonmenusalir() {	
		System.out.println("indexTotal :"+indexTotal);
		
		Intent intent = new Intent();
		if(modo.equals("normal")){
			intent.putExtra("indexTotal", indexTotal);
			intent.putExtra("tipoModo", "normal");
		}else if(modo.equals("relaciones")){
			intent.putExtra("indexTotalRelaciones", indexTotalRelaciones);
			intent.putExtra("tipoModo", "relaciones");
		}else if(modo.equals("retocolores")){
			intent.putExtra("indexTotalRetoColores", indexTotalRetoColores);
			intent.putExtra("tipoModo", "retocolores");
		}	
		setResult(RESULT_OK, intent);
		finish();
	}

	public void botonMenuSalir(View v) {
		System.out.println("indexTotal :"+indexTotal);
		
		if(soundPrefs.getBoolean("sonido", true))
			ActivityModosSeleccion.soundClick1.start();
		
		Intent intent = new Intent();
		if(modo.equals("normal")){
			intent.putExtra("indexTotal", indexTotal);
			intent.putExtra("tipoModo", "normal");
		}else if(modo.equals("relaciones")){
			intent.putExtra("indexTotalRelaciones", indexTotalRelaciones);
			intent.putExtra("tipoModo", "relaciones");
		}else if(modo.equals("retocolores")){
			intent.putExtra("indexTotalRetoColores", indexTotalRetoColores);
			intent.putExtra("tipoModo", "retocolores");
		}	
		setResult(RESULT_OK, intent);		
		finish();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {				

		if (!getVistaDeInicio()) { 			//Si no esta activada la vista de inicio
			
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				if (marcadoresMostrados ) { // si se ven los marcadores (es que hemos pasado de nivel y podemos subir un nivel)
					System.out.println("SI SE VEN LOS MARCADORES ");
					if (modo.equals("normal")) { // modo normal
						// nivel19
						if (nivel != 19) {
							if (keybackTocado == 0) { // Ponemos una variable keybackTocado para que si pulsamos mucho el boton back no se acumulen elementos al spiner
								keybackTocado++;
								if ((indexTotal - indexTotalAux) > 1) {
									indexTotalAux++;
								} else {
									indexTotal++;
									indexTotalAux++;
								}
							}
							volveraMenu(); //abrimos cuadro de dialogo 
						} else {// si es el ultimo nivel entonces no mostramos el cuadro dialogo ni sumamos elementos al spinner
							volverMenuInicial(); // volvemos al menu inicial sin mostrar cuadro de dialogo
						}
					} else if (modo.equals("relaciones")) {
						// nivel17
						if (nivel != 17) {
							if (keybackTocado == 0) { // Ponemos una variable keybackTocado para que si pulsamos mucho el boton back no se acumulen elementos al spiner
								keybackTocado++;
								if ((indexTotalRelaciones - indexTotalAux) > 1) {
									indexTotalAux++;
								} else {
									indexTotalRelaciones++;
									indexTotalAux++;
								}
							}
							volveraMenu();
						} else {// si es el ultimo nivel entonces no mostramos el cuadro dialogo ni sumamos elementos al spinner
							volverMenuInicial(); // volvemos al menu inicial sin mostrar cuadro de dialogo
						}
					} else if (modo.equals("retocolores")) {
						// nivel26
						if (nivel != 26) {
							if (keybackTocado == 0) { // Ponemos una variable keybackTocado para que si pulsamos mucho el boton back no se acumulen elementos al spiner
								keybackTocado++;
								if ((indexTotalRetoColores - indexTotalAux) > 1) {
									indexTotalAux++;
								} else {
									indexTotalRetoColores++;
									indexTotalAux++;
								}
							}
							volveraMenu();
						} else {// si es el ultimo nivel entonces no mostramos el cuadro dialogo ni sumamos elementos al spinner
							volverMenuInicial(); // volvemos al menu inicial sin mostrar cuadro de dialogo
						}
					}

				} else { // si no se ven los marcadores , es decir durante el juego, mostramos el cuadro de dialogo
					if (!getGamePaused()) {
						setGamePaused(false);
					}					
					volveraMenu();
				}

			}
		} else if (getVistaDeInicio()) { //si esta activada la vista de inicio no mostramos el cuadro de dialogo			
			botonmenusalir();			
		}
		return super.onKeyDown(keyCode, event);
	}

	
	//MENU DE SELECCION DE NIVEL , TEMAS y subimos tambien el marcador obtenido
	public void volverMenuInicial() {
		/*
		if (modo.equals("normal")) {
			new ScoresHigh(contexto, GAME_PREFS, acumulado, "highScores").setHighScores();
		} else if (modo.equals("relaciones")) {
			new ScoresHigh(contexto, GAME_PREFS, acumulado, "highScores2").setHighScores();
		} else if (modo.equals("retocolores")) {
			new ScoresHigh(contexto, GAME_PREFS, acumulado, "highScores3").setHighScores();
		}		
		*/
		//vistaInicio.setVisibility(View.VISIBLE);
		if(!getVistaFracaso()){						
			handler.postDelayed(new Runnable(){
				@Override
				public void run() {							
					setVistaDeInicio(true);	
				}						
			}, 300);					
									
			setSpinners();
		}else if(getVistaFracaso()){
			setVistaFracaso(false);			
			handler.postDelayed(new Runnable(){
				@Override
				public void run() {						
					setVistaDeInicio(true);		
				}					
			}, 300);							
				
		}
		if(panta!=null)
			panta.setVisibility(View.INVISIBLE);
		
		if(!getGameOver()){ //si estamos en el transcurso del juego, tendriamos que terminar juego y limpiar 
			setGameOver(true);
			if(!parlinear.isEmpty())
				parlinear.clear(); // reiniciamos el parlinear		
			gridlay.removeAllViews(); //quitamos todas las views del gridlay
			setInvisibleViews();
			if(getVistaInformacion())
				setVistaInformacion(false);
		}	
		
	}
	
	//------------------- FINAL DE BOTONES ----------------

	// Si lo hacemos con onBackPressed seria lo mismo:
	// Mandamos datos a ActivityModos para que se guarde el ultimo nivel jugado.
	// De esta forma al entrar de nuevo al modo TimeAttack
	// se cargue el spinner con los niveles jugados
	/*
	 * @Override public void onBackPressed() { //super.onBackPressed(); Intent i
	 * = new Intent(); i.putExtra("nivel", indexTotal); setResult(RESULT_OK, i);
	 * finish(); }
	 */

		

	class linearListener implements View.OnClickListener {

		int OK = 123456; // id que asignaremos cuando al pulsar dos linarlayouts
							// su id sean iguales
		Timer timer;
		LinearLayout linear;
		AutoResizeTextView im1, im2, im3, im4, imdetras2, imdetras4;

		public linearListener(LinearLayout layout) {
			this.linear = layout;
		}

		@Override
		public void onClick(View v) {

			if (!getGamePaused()) {
				
				if (parlinear.size() < 2) {
					
					if (linear.getId() != OK) {
						
						parlinear.add(linear);
						if (parlinear.size() == 1) {							
							 if(soundPrefs.getBoolean("sonido", true)) ActivityModosSeleccion.soundPlay.start();

							im1 = (AutoResizeTextView) parlinear.get(0).getChildAt(0);
							im2 = (AutoResizeTextView) parlinear.get(0).getChildAt(1);
							// Hacemos desaparecer im1 parea que salga imdetras2 que estaria visible
							girarImagen(im1, im2, 100);
							// im1.setVisibility(View.GONE);
							// im2.setVisibility(View.VISIBLE);

						}
						if (parlinear.size() == 2) {

							// Descartamos que no pulsemos la misma ficha dos veces:
							if (parlinear.get(0).getChildAt(1).getTag() != parlinear.get(1).getChildAt(1).getTag()) {

								im1 = (AutoResizeTextView) parlinear.get(0).getChildAt(0);
								im2 = (AutoResizeTextView) parlinear.get(0).getChildAt(1);
								im3 = (AutoResizeTextView) parlinear.get(1).getChildAt(0);
								im4 = (AutoResizeTextView) parlinear.get(1).getChildAt(1);

								// hacemos desaparecer im3 y que salga imdetras4 que estaria visible
								girarImagen(im3, im4, 100);
								// im3.setVisibility(View.GONE);
								// im4.setVisibility(View.VISIBLE);

								// Si las ids son iguales se han emparejado los elementos:
								if (parlinear.get(0).getId() == parlinear.get(1).getId()) {		
									
									if (soundPrefs.getBoolean("sonido", true)) 
										  ActivityModosSeleccion.estrellitasSound.start();									 
									// y le ponemos id = OK a los dos
									parlinear.get(0).setId(OK);
									parlinear.get(1).setId(OK);
									// Sumamos segundos extra adicional dependiendo del nivel
									if (nivel > 6 && nivel <= 10) {
										setupDelayProgressBar(2, 2000);
									} else if (nivel > 10 && nivel <= 14) {
										setupDelayProgressBar(2, 2000);
									} else if (nivel > 14 && nivel <= 19) {
										setupDelayProgressBar(3, 3000);
									} else if (nivel > 19) {
										setupDelayProgressBar(3, 3000);
									}
									// Combos
									comboAux++;
									if (comboAux > 1) {
										combo++;
										totalCombos++;
										/*
										 * Si queremos pasamos la info del combo para que aparezca en pantalla: new ComboPantalla1(contexto,
										 * "Combo"+"\n    X" +String.valueOf(combo-1), R.id.timeattack_layout, contexto.getResources
										 * ().getColor(android .R.color.holo_blue_light), true).run();
										 */
									} else
										combo = 1;
									aciertos++;// Contador de aciertos

									// TERMINANOS LA PARTIDA
									if (aciertos == (numFilas * numCol) / 2	&& (totalTime - progressTime) != 0) {
										setGameOver(true);			
										if (soundPrefs.getBoolean("sonido", true)) 
											  ActivityModosSeleccion.aplausosSound1.start();	
										setInvisibleViews();										

									}									
									handler.postDelayed(new Runnable() {// Pasado el tiempo time se ejecuta lo que hay dentro del run  y Hacemos desaparecer la pareja encontrada
										@Override
										public void run() {
											AlphaAnimation alphaAnimation = new AlphaAnimation(	1, 0); // 1 total opaco. 0 es total transparente
											alphaAnimation.setDuration(time * 2);
											alphaAnimation.setFillAfter(true);
											// im2.setVisibility(View.GONE);quitamos im2 e im4 al instante y hacemos la animacion con imdetras2 y 4 que estan debajo.
											// im4.setVisibility(View.GONE);
											// imdetras2.startAnimation(alphaAnimation);
											// imdetras4.startAnimation(alphaAnimation);
											im2.startAnimation(alphaAnimation);
											im4.startAnimation(alphaAnimation);
											// debido a que si pulsamos una d las dos imagenes de manera accidental esta aparece cuando no debiera, se implementa un setalpha a 0
											alphaAnimation.setAnimationListener(new AnimationListener() {
												@Override
												public void onAnimationEnd(	Animation animation) {
													ViewHelper.setAlpha(im2,0);
													ViewHelper.setAlpha(im4,0);
												}
												@Override
												public void onAnimationRepeat(Animation animation) {}

												@Override
												public void onAnimationStart(Animation animation) {	}
											});
										}
									}, time);
									parlinear.remove(1);
									parlinear.remove(0);

												
									// MUESTRA DE RESULTADOS al haber concluido la partida
									if (getGameOver()) {
										marcadoresMostrados = true;
										// Hacemos que salte la pantalla de fin de nivel con el tiempo empleado , 1 segundo despues
										
										handler.postDelayed(new Runnable() {
											@Override
											public void run() {														
												new MarcadoresTimeAttack().run();
												parlinear.clear(); // reiniciamos el parlinear
												gridlay.removeAllViews(); //quitamos todas las views del gridlay														
											}
										}, time*1);											
									}
								} else { // En caso de que no sean iguales:
									
									  if (soundPrefs.getBoolean("sonido", true)) ActivityModosSeleccion.soundPlay.start();									 
									
									handler.postDelayed(new Runnable() {
										@Override
										public void run() {
										// quitamos el interrogante
											im1.setBackgroundResource(R.drawable.agris);
											im3.setBackgroundResource(R.drawable.agris);
											girarImagen(im1, im2, 100);
											girarImagen(im3, im4, 100);
										}
									}, 800);
										
									new Timer().schedule(new TimerTask() {
										@Override
										public void run() {
											parlinear.remove(1);
											parlinear.remove(0);
										}
									}, time); // time = 800 (tiempo de espera a que se den la vuelta) +/ tiempo que tarda en darse el giro

									combo = 1; // reseteamos el combo a 1 cuando no hay mas de dos parejas encontradas consecutivas
									comboAux = 0;
								}
								numClicks++;

							} else {
								parlinear.remove(1);
							}
						}// fin if
					}
				}

			}
		}// fin onClickview
		public void setupDelayProgressBar(int addTime, int addTimeMili) {

			setAditionalTime(addTime);
			if (!getStopProgressBar()) {
				timerPB = new Timer();
				stopProgressBar(addTimeMili);
				setStopProgressBar(true);
			} else if (getStopProgressBar()) {
				timerPB.cancel();
				setStopProgressBar(false);
				diftime = System.currentTimeMillis() - inictime;
				acumtime = addTimeMili - diftime;
				sumaacumtime = sumaacumtime + acumtime;
				timerPB = new Timer();
				stopProgressBar(addTimeMili + sumaacumtime);
				setStopProgressBar(true);
			}
		}

		public void setAditionalTime(int adTime) {
			aditionalTime = adTime;
			progressTime = progressTime - adTime;
			new ComboPantalla1(contexto, "+" + String.valueOf(adTime) + " "	+ contexto.getResources().getString(R.string.seg),
					R.id.timeattack_layout, contexto.getResources().getColor(R.color.green), true, 1, 3, 1, 3, 1000,
					R.drawable.fondo_combotransparente).run();
		}

		public void stopProgressBar(long t) {
			inictime = System.currentTimeMillis();
			timerPB.schedule(new TimerTask() {
				@Override
				public void run() {
					setStopProgressBar(false);
				}
			}, t);
		}

			
		
	}// fin clase listener

	
	public void girarImagen(TextView imagen1, TextView imagen2, int duracion) {

		final TextView visibleImage;
		final TextView invisibleImage;

		if (imagen1.getVisibility() == View.GONE) {
			visibleImage = imagen2;
			invisibleImage = imagen1;
		} else {
			invisibleImage = imagen2;
			visibleImage = imagen1;
		}
		final ObjectAnimator goneToVisible = ObjectAnimator.ofFloat(
				invisibleImage, "rotationY", -90f, 0f);
		ObjectAnimator visibleToGone = ObjectAnimator.ofFloat(visibleImage,
				"rotationY", 0f, 90f);
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

	public void girarImagen2(TextView imagen1, TextView imagen2, int duracion) {

		final TextView visibleImage;
		final TextView invisibleImage;

		if (imagen1.getVisibility() == View.GONE) {
			visibleImage = imagen2;
			invisibleImage = imagen1;
		} else {
			invisibleImage = imagen2;
			visibleImage = imagen1;
		}
		final ObjectAnimator goneToVisible = ObjectAnimator.ofFloat(
				invisibleImage, "alpha", 0, 1);
		ObjectAnimator visibleToGone = ObjectAnimator.ofFloat(visibleImage,
				"alpha", 1, 0);
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

	// Sistema de puntuaciones
	public float scoreSystem() {
		
		int cont = 0; 
		float total = 0, sumaTotal = 0;		
		
		float punto = nivel;
		// Aplicamosm un porcentaje a cada punto.
		float porcent = (float) aciertos / (float) numClicks;
		// Aunemtamos el total cada vez que acertemos
		while (cont < aciertos) {
			total = total + punto;
			cont++;
		}
		// En caso de acertar o no acertar mutiplicamos por el total por el
		// porcentaje.
		sumaTotal = (total + combo) * porcent;
		if (sumaTotal < 1)
			sumaTotal = 1;

		return sumaTotal;
	}

	public void setTimeThread() {
		final int PuntosTotales = (int) scoreSystem()* (totalTime - progressTime - aditionalTime + retardo);
		final int Puntos = (int) scoreSystem();
		final int timeToReach = DURACION_THREAD;

		int timeThreadPuntosTotal = (int) (timeToReach / PuntosTotales);
		int contaPuntosTot = 1;

		int timeThreadPuntos = (int) (timeToReach / Puntos);
		int contaPuntos = 1;

		if (timeThreadPuntos < 5) {
			while (timeThreadPuntos < 5) {
				timeThreadPuntos = (int) (timeToReach / (Puntos / contaPuntos));
				contaPuntos++;
			}
		}
		if (timeThreadPuntosTotal < 5) {
			while (timeThreadPuntosTotal < 5) {
				timeThreadPuntosTotal = (int) (timeToReach / (PuntosTotales / contaPuntosTot));
				contaPuntosTot++;
			}
		}
		matrizTimeThread[0] = contaPuntosTot;
		matrizTimeThread[1] = timeThreadPuntosTotal;
		matrizTimeThread[2] = contaPuntos;
		matrizTimeThread[3] = timeThreadPuntos;
	}

	class MarcadoresTimeAttack implements Runnable {

		TextView textNivel, textPuntos, textTiempor, textTiempoRestante,	textPuntosTotales,
		textPuntosAcumulados, textFelicidades,textCompletado;
		Button backTomenu, nextLevel, botonCompartir, botonScores;

		ImageView imagenSuperado;
		LinearLayout linearpuntos, lineartiempo, lineartiemporestante, linearpuntostotales, linearpuntosacumulados;

		int j = 0, k = 0, l = 0, m = 0;

		@Override
		public void run() {
			mostrarMarc();
		}

		public void mostrarMarc() {			
			// OJO NO USAREMOS EL VIEWSTUB
			// Creamos una view tipo LinearLayout que sera agregada al
			// RelativeLayout para que quede por encima
			panta = new LinearLayout(contexto) {
				LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = li.inflate(R.layout.timeattack_final_exito, this, true);

			};

			RelativeLayout llay = (RelativeLayout) ((Activity) contexto).findViewById(R.id.timeattack_layout);
			RelativeLayout.LayoutParams relatparams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,	android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);

			relatparams.setMargins(dm.widthPixels / 15, dm.heightPixels / 7,
					dm.widthPixels / 15, dm.heightPixels / 15);
			panta.setLayoutParams(relatparams);
			llay.addView(panta);

			textNivel = (TextView) panta.findViewById(R.id.text_levelcompleted);
			textFelicidades = (TextView) panta.findViewById(R.id.text_felicidades);
			textCompletado = (TextView) panta.findViewById(R.id.text_completado);
			imagenSuperado = (ImageView) panta.findViewById(R.id.imagen_logosuperado);

			linearpuntos = (LinearLayout) panta.findViewById(R.id.linearpuntos);
			linearpuntos.setVisibility(LinearLayout.VISIBLE);
			textPuntos = (TextView) panta.findViewById(R.id.marcador_puntostimeattack);

			lineartiempo = (LinearLayout) panta.findViewById(R.id.lineartiempo);
			lineartiempo.setVisibility(LinearLayout.VISIBLE);
			textTiempor = (TextView) panta.findViewById(R.id.marcador_tiempototaltimeattack);

			lineartiemporestante = (LinearLayout) panta.findViewById(R.id.lineartiemporestante);
			lineartiemporestante.setVisibility(LinearLayout.VISIBLE);
			textTiempoRestante = (TextView) panta.findViewById(R.id.marcador_tiemporestantetimeattack);

			linearpuntostotales = (LinearLayout) panta.findViewById(R.id.linearpuntostotales);
			linearpuntostotales.setVisibility(View.VISIBLE);
			textPuntosTotales = (TextView) panta.findViewById(R.id.marcador_puntostotalestimeattack);

			linearpuntosacumulados = (LinearLayout) panta.findViewById(R.id.linearpuntosacumulados);
			linearpuntosacumulados.setVisibility(View.VISIBLE);
			textPuntosAcumulados = (TextView) panta.findViewById(R.id.marcador_puntosacumuladostimeattack);

			backTomenu = (Button) panta.findViewById(R.id.marcador_menu_timeattack);
			backTomenu.setVisibility(View.INVISIBLE);
			nextLevel = (Button) panta.findViewById(R.id.marcador_timeattack_botonplay);
			nextLevel.setVisibility(View.INVISIBLE);
			botonCompartir = (Button) panta.findViewById(R.id.compartir_nivel);
			botonCompartir.setVisibility(View.INVISIBLE);
			botonScores = (Button) panta.findViewById(R.id.global_scores);

			/* nivel19 */
			if (modo.equals("normal"))
				setUpLastLevel1(19);
			else if (modo.equals("relaciones"))
				setUpLastLevel1(17);
			else if (modo.equals("retocolores"))
				setUpLastLevel1(26);
			// Ponemos un alphaanimation para que aparezcabn los marcadores.

			AlphaAnimation alphaanim = new AlphaAnimation(0.5f, 1);
			alphaanim.setDuration(2000);
			panta.startAnimation(alphaanim);
			
			// Puntuaciones:
			// Pasado el tiempo de la animacion , ponemos las puntuaciones . EL metodo setTimeThread() establece el tiempo que dura cada hilo
			// aunque solo lo usaremos en los Puntos Totales y los Puntos parciales.
			setTimeThread();
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					handler.post(new Runnable() {
						@Override
						public void run() {
							/* nivel19 */
							if (modo.equals("normal"))
								setUpLastLevel2(19);
							else if (modo.equals("relaciones"))
								setUpLastLevel2(17);
							else if (modo.equals("retocolores"))
								setUpLastLevel2(26);
						}
					});
					// Puntos:
					new Thread() {
						@Override
						public void run() {
							while (j < (int) scoreSystem()) {
								try {
									Thread.sleep(matrizTimeThread[3]);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								handler.post(new Runnable() {
									@Override
									public void run() {
										textPuntos.setText(String.valueOf(j));
									}
								});
								j += matrizTimeThread[2];
							}
							// finalizado el bucle ponemos los puntos en float
							handler.post(new Runnable() {
								@Override
								public void run() {
									textPuntos.setText(String.format("%.2f",scoreSystem()));
								}
							});
						}

					}.start();

					// Tiempo:
					new Thread() {
						@Override
						public void run() {
							
							while (k < (progressTime + (aditionalTime * aciertos) - retardo)) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								handler.post(new Runnable() {
									@Override
									public void run() {
										textTiempor.setText(String.valueOf(k));
									}
								});
								++k;
							}
						}
					}.start();

					// Tiempo restante:
					new Thread() {
						@Override
						public void run() {
							while (l < (totalTime - progressTime- aditionalTime + retardo)) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								handler.post(new Runnable() {
									@Override
									public void run() {
										textTiempoRestante.setText(String.valueOf(l));
									}
								});
								++l;
							}
						}
					}.start();

					// Puntos totales y acumulado total:
					new Thread() {
						@Override
						public void run() {
							while (m < (int) scoreSystem()* (totalTime - progressTime - aditionalTime + retardo)) {
								try {
									Thread.sleep(matrizTimeThread[1]);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								handler.post(new Runnable() {
									@Override
									public void run() {
										textPuntosTotales.setText(String.valueOf(m));
									}
								});
								m += matrizTimeThread[0]; // subimos en vez de uno en uno, de 5 en 5 para acortar un poco.
							}
							handler.post(new Runnable() {
								@Override
								public void run() {
									// una vez finalizado el bucle, ponemos los decimales float:
									textPuntosTotales.setText(String.format("%.2f",	scoreSystem()* (totalTime- progressTime	- aditionalTime + retardo)));
									// Ponemos los puntos acumulados una vez terminado el bucle de los puntos totales
									acumulado = acumulado+ scoreSystem()* (totalTime - progressTime	- aditionalTime + retardo);
									if (modo.equals("normal")) {
										new ScoresHigh(contexto, GAME_PREFS, acumulado, "highScores").setHighScores();
									} else if (modo.equals("relaciones")) {
										new ScoresHigh(contexto, GAME_PREFS, acumulado, "highScores2").setHighScores();
									} else if (modo.equals("retocolores")) {
										new ScoresHigh(contexto, GAME_PREFS, acumulado, "highScores3").setHighScores();
									}		
									// subimos el acumulado total a Google play games en caso de estar conectado
									/*
									if (isSignedIn())
										subirPuntuacion();
									checkLogroIncremental(); //aplicamos logros incrementales
									*/
									textPuntosAcumulados.setText(String.format("%.2f", acumulado));
									// Hacemos visible los botones una vez se haya completado el bucle del contador para que la variable acumulado se actualice.
									// menos en el ultimo nivel:
									/* nivel19 */
									if (modo.equals("normal"))
										setUpLastLevel3(19);
									else if (modo.equals("relaciones"))
										setUpLastLevel3(17);
									else if (modo.equals("retocolores"))
										setUpLastLevel3(26);
									backTomenu.setVisibility(View.VISIBLE);
									botonCompartir.setVisibility(View.VISIBLE);
									/*
									if (isSignedIn()) // solo se ve si estamos conectados
										botonScores.setVisibility(View.VISIBLE);
									*/
								}
							});
						}
					}.start();
				}
			}, 1000);
			
			nextLevel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
					if(soundPrefs.getBoolean("sonido", true))
						ActivityModosSeleccion.soundClick1.start();
					
					
				 // Subimos nivel, indexTotal y indexTotalAux
					nivel++;
					if (modo.equals("normal")) {
						if ((indexTotal - indexTotalAux) > 1) {
							indexTotalAux++;
						} else {
							indexTotal++;
							indexTotalAux++;
						}
					} else if (modo.equals("relaciones")) {
						if ((indexTotalRelaciones - indexTotalAux) > 1) {
							indexTotalAux++;
						} else {
							indexTotalRelaciones++;
							indexTotalAux++;
						}
					} else if (modo.equals("retocolores")) {
						if ((indexTotalRetoColores - indexTotalAux) > 1) {
							indexTotalAux++;
						} else {
							indexTotalRetoColores++;
							indexTotalAux++;
						}
					}		
					setGameOver(false);					
					
					marcadoresMostrados=false;
					
					setupNiveles(nivel);

					configGrid(numCol, numFilas); // Configuracion del grid y disposicion de celdas				

					activateGridlay(false); // desactivamos de primeras el grid para luego volverlo a activar una vez que comience el hilo
										
					panta.setVisibility(View.INVISIBLE);										
					setVisibleViews();					
					setInitVariables();
					
					// Comenzamos el juego
					if (comenzarJuego)
						comienzoJuego();

				}
			});

			backTomenu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(soundPrefs.getBoolean("sonido", true))
						ActivityModosSeleccion.soundClick1.start();
					
					if (modo.equals("normal")) {
						// nivel19
						if (nivel != 19) {
							if (keybackTocado == 0) {
								if ((indexTotal - indexTotalAux) > 1) {
									indexTotalAux++;
								} else {
									indexTotal++;
									indexTotalAux++;
								}
							}
						}
					} else if (modo.equals("relaciones")) {
						// nivel17
						if (nivel != 17) {
							if (keybackTocado == 0) {
								if ((indexTotalRelaciones - indexTotalAux) > 1) {
									indexTotalAux++;
								} else {
									indexTotalRelaciones++;
									indexTotalAux++;
								}
							}
						}
					} else if (modo.equals("retocolores")) {
						// nivel26
						if (nivel != 26) {
							if (keybackTocado == 0) {
								if ((indexTotalRetoColores - indexTotalAux) > 1) {
									indexTotalAux++;
								} else {
									indexTotalRetoColores++;
									indexTotalAux++;
								}
							}
						}
					}
					marcadoresMostrados=false;					
					volverMenuInicial();
				}
			});

			botonCompartir.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(soundPrefs.getBoolean("sonido", true))
						ActivityModosSeleccion.soundClick1.start();
					
					Intent sharingIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					sharingIntent.setType("text/plain"); // Pasaremos solo
					String titulomodo = null;
					if (modo.equals("normal")) {
						titulomodo = getResources().getString(R.string.botonmodotimeatack);

					} else if (modo.equals("relaciones")) {
						titulomodo = getResources().getString(R.string.botonmodotimeatack2);
					} else if (modo.equals("retocolores")) {
						titulomodo = getResources().getString(R.string.botonretocolores);
					}
					String shareBody = getResources().getString(R.string.sharecontent_puntos1)
							+ " "+ getResources().getString(R.string.sharecontent_puntos2)
							+ " "+ titulomodo + getResources().getString(R.string.sharecontent_puntos3)
							+ " "+ String.valueOf(nivel)+ getResources().getString(R.string.sharecontent_puntos4)
							+ " "+ String.format("%.2f",scoreSystem()* (totalTime - progressTime- aditionalTime + retardo))
							+ getResources().getString(	R.string.sharecontent_puntos5);

					sharingIntent.putExtra(	android.content.Intent.EXTRA_SUBJECT,getResources().getString(R.string.sharesubject_puntos));
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
					startActivity(Intent.createChooser(sharingIntent,getResources().getString(R.string.compartirvia)));

				}
			});
			/*
			botonScores.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(soundPrefs.getBoolean("sonido", true))
						ActivityModosSeleccion.soundClick1.start();
					
					if (modo.equals("normal")) {
						startActivityForResult(
								Games.Leaderboards.getLeaderboardIntent(getApiClient(),	getResources().getString(R.string.leaderboard_time_challenge)),	500);
					} else if (modo.equals("relaciones")) {
						startActivityForResult(
								Games.Leaderboards.getLeaderboardIntent(getApiClient(),	getResources().getString(R.string.leaderboard_time_challenge_plus)),500);
					} else if (modo.equals("retocolores")) {
						startActivityForResult(
								Games.Leaderboards.getLeaderboardIntent(getApiClient(),	getResources().getString(R.string.leaderboard_color_challenge)),500);
					}

				}
			});
			*/
			
			
		}

		public void setUpLastLevel1(int level) {
			if (nivel == level) {
				
				if(soundPrefs.getBoolean("sonido", true))
					ActivityModosSeleccion.SoundAplausos.start();
				
				textFelicidades.setVisibility(View.INVISIBLE);
				textCompletado.setVisibility(View.INVISIBLE);
				ScaleAnimation anim = new ScaleAnimation(1, 3, 1, 3);
				anim.setDuration(3000);
				anim.setRepeatCount(1);
				anim.setRepeatMode(Animation.REVERSE);
				imagenSuperado.setImageResource(R.drawable.botoncopa_128);
				imagenSuperado.startAnimation(anim);
			} else {
				textNivel.setVisibility(View.INVISIBLE);
				RotateAnimation rotarAnimation = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f,
				// pivote valor del filo izquierdo (valores entre 0 y 1 floats)
						Animation.RELATIVE_TO_SELF, 0.5f); // pivote valor del
															// filo superior
				rotarAnimation.setDuration(1000);
				imagenSuperado.startAnimation(rotarAnimation);

			}
		}

		public void setUpLastLevel2(int level) {
			if (nivel == level) {
				textFelicidades.setVisibility(View.VISIBLE);
				textCompletado.setVisibility(View.VISIBLE);
				textFelicidades.setText(contexto.getResources().getString(R.string.felicidades));
				textCompletado.setText(contexto.getResources().getString(R.string.modocompletado));
				RotateAnimation rotateAnimation = new RotateAnimation(0, 360, 	Animation.RELATIVE_TO_SELF, 0.5f,
				// pivote valor del filo izquierdo (valores entre 0 y 1 floats)
						Animation.RELATIVE_TO_SELF, 0.5f); // pivote valor del filo superior
				rotateAnimation.setDuration(500);
				textCompletado.startAnimation(rotateAnimation);

			} else {
				textNivel.setVisibility(View.VISIBLE);
				textNivel.setText(contexto.getResources().getString(R.string.nivel)	+ " "+ String.valueOf(nivel)
						+ " "+ contexto.getResources().getString(R.string.completado));
			}
		}

		public void setUpLastLevel3(int level) {
			if (nivel == level)
				nextLevel.setVisibility(View.GONE);
			else {
				nextLevel.setVisibility(View.VISIBLE);
				// Ponemos animacion alfa para que resalte
				AlphaAnimation alfa = new AlphaAnimation(0.7f, 1f);
				alfa.setDuration(500);
				alfa.setRepeatMode(Animation.REVERSE);
				alfa.setRepeatCount(Animation.INFINITE);
				nextLevel.startAnimation(alfa);
			}
		}

	}
	// Liberamos todos los mediaplayer en el ondestroy para que no ocupen memoria http://www.htcmania.com/showthread.php?t=778281
		@Override
		protected void onDestroy() {
			super.onDestroy();
			//adView.destroy();
			/*
			 * click1Sound.release(); click2Sound.release();
			 * estrellitasSound.release(); aplausosSound1.release();
			 * OhhSounds1.release(); mp2.release(); mp3.release();
			 * crowdSound.release();
			 */
		}
		
		@Override
		// Ponemos juego en foreground por pulsar boton home o que entre una llamada
		protected void onPause() {
			super.onPause();
			//adView.pause();
			setGamePaused(true);
			/*
			 * if(soundPrefs.getBoolean("sonido", true)) musicafondo.pause();
			 */
		}

		@Override
		// vuelta de foreground
		protected void onResume() {
			super.onResume();
			//adView.resume();
			if (pulsar == 1) {
				setGamePaused(true);
			} else {
				setGamePaused(false);
			}
		}

}
