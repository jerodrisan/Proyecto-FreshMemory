package com.jesusmanuelrodriguez.freshmemory;

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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Chronometer;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


//CLASE PARA EL MODO DE JUEGO SERIES DEL TIPO SECUENCIA.
public class ActivitySecuencias2 extends Activity{
	
	SharedPreferences gamePrefs;
	public static final String GAME_PREFS = "Secuencias";	
	// Lo usaremos para que el vistainformacion solo salga solo una vez ( correspondiente al nivel que tocamos en el spinner)
	//Luego recuperamos el valor en sucesivos niveles y si el el nivel no es el nivel tocado en un principio entonces no saldra el stubview
	public static final String NIVELTOCADO = "niveltocado"; 
	private static final int DURACION_THREAD = 2000;
	
	int[] matrizTimeThread;
	
	DisplayMetrics dm;
	Context contexto;
	
	Handler handler = new Handler();

	ViewStub  vistaInicio,  vistainformacion;
	ImageView   botonvolverAjugar, botonSonido, botonPausar, logoSecuenciaInicio;
	
	GridLayout gridlay;
	Spinner spinnerNivel;
	TextView  textTiempo, textPorcentajes, textoTituloNumeros;	
	Chronometer crono;
	
	String modo;
	int gap, gapLeft, gapTop, anchoIm, altoIm, publi;
	int base, numFilas, numCol;
	
	
	
	ArrayList<Integer> lista, numbersMixed, posMixed, numbers, pos, levelsImagenes;
	ArrayList<String> levels, spinnerValores ; 	
	//String [] spinnerValores;
	int[] spinnerImagenes1;
	ArrayList<LinearLayout> parlinear;
	
	//Variables recibidas por Intenet:
	int vInicio, vBar,  vspin;
	
	static final int time = 1000; //tiempo que dura la animacion 
	
	int numberInicial;
	int index; //indice del elemento seleccionado del Spinner
	int nivel=1; //contador de niveles
	int indexTotalSecuencia2; // total de elementos que se llenaran en el Spinner en modo normal
	int indexTotalAux;
	int ids=0;
	int elemSpinner;	
	
	
	
	int nivelTocado;
	
	int numClicks=1, aciertos=1;
	int numTandas=0; //Numero da tandas que se da la vuelta
	float total=0, sumaTotal=0;
	float acumulado = 0; //Acumulado de puntuacion segun se avanza de nivel.
	int i=0;
	
	int pulsar=0;
	
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
	boolean marcadoresMostrados= false;
	int keybackTocado =0;
	
	SonidosSimon soundSecuencias;
	int  sonidoPlay, sonidoTecla, sonidoOkStub, sonidoboton25, aplausosSound1, crowdSound;	
	MediaPlayer sonidoSalir_Nivel;
	int MAX_VOLUME = 100; //volumen m�ximo de referencia	
	Button botonOKinformativo;
	
	int refTipoSecuencia;
	
	int corte; // El panel se dara la vuelta cada x veces el numero de corte reorganizandose de nuevo en caso de no acertar
	int z=1; //variable que multiplica corte
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
		
		setUpSounds(); //Configuracion de todos los sonidos
		
		//inflamos la vista de informacion del juego
		vistainformacion = (ViewStub)findViewById(R.id.viewstubinforsecuencias2);	
		
		//spinnerValores = new String[22];
		spinnerImagenes1 = new int[22];		
	
		inflarSpinner();	
		
		getExtras();
		
		base=numFilas*numCol;			
		
		configPantalla();
		
		initViews();	//Inicializacion de todas las views	
		
		//Preferencias conexion 
		conexionScores = this.getSharedPreferences(CONEXION, 0);		
		//Conectamos en la activity con google play services
		/*
		if(conexionScores.getBoolean(CONEXION, true))
			beginUserInitiatedSignIn();
		*/
	} // FIN ON CREATE
	
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
			Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_sequence_series), Integer.parseInt(punt));
			if(nivel==5 && numTandas<=6){
				Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_craps));	
			}
			if(nivel==9 && numTandas<=16){
				Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_needles));	
			}
			if(nivel==15 && numTandas<=25){
				Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_math));	
			}
			if(nivel ==22 && numTandas <=25){
				Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_worms));	
			}
		
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
		aplausosSound1 = soundSecuencias.load(R.raw.applau22);
		crowdSound = soundSecuencias.load(R.raw.crwdbugl);
		
	}
	
	//funcion para establecer volumen customizado		
	public float setCustomVolumen(int soundVolume){  //volumen que queremos poner (la mitad del total de volumen) o  lo que queramos!!!
		return (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME))); // ver:http://stackoverflow.com/questions/5215459/android-mediaplayer-setvolume-function
	}
	
	public void inflarSpinner(){ //Ponerlo en otra clase para que ocupe menos menoria??????
		//Nivel22
		levels = new ArrayList<String>();			
		
		levels.add(0, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(1)+ " "+contexto.getResources().getString(R.string.facil)+"\n"+ contexto.getResources().getString(R.string.nums));
		levels.add(1, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(2)+ " "+contexto.getResources().getString(R.string.facil)+"\n"+ contexto.getResources().getString(R.string.lets));
		levels.add(2, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(3)+ " "+contexto.getResources().getString(R.string.facil)+"\n"+ contexto.getResources().getString(R.string.numsYlets));
		levels.add(3, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(4)+ " "+contexto.getResources().getString(R.string.facil)+"\n"+ contexto.getResources().getString(R.string.poligonos));
		levels.add(4, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(5)+ " "+contexto.getResources().getString(R.string.normal)+"\n"+ contexto.getResources().getString(R.string.dados));
		levels.add(5, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(6)+ " "+contexto.getResources().getString(R.string.normal)+"\n"+ contexto.getResources().getString(R.string.operaciones));
		levels.add(6, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(7)+ " "+contexto.getResources().getString(R.string.normal)+"\n"+ contexto.getResources().getString(R.string.formaspuntos));
		levels.add(7, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(8)+ " "+contexto.getResources().getString(R.string.normal)+"\n"+ contexto.getResources().getString(R.string.relojes));
		levels.add(8, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(9)+ " "+contexto.getResources().getString(R.string.normal)+"\n"+ contexto.getResources().getString(R.string.agujas));
		levels.add(9, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(10)+ " "+contexto.getResources().getString(R.string.normal)+"\n"+ contexto.getResources().getString(R.string.dados));
		levels.add(10, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(11)+ " "+contexto.getResources().getString(R.string.normal)+"\n"+ contexto.getResources().getString(R.string.operaciones));
		levels.add(11, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(12)+ " "+contexto.getResources().getString(R.string.normal)+"\n"+ contexto.getResources().getString(R.string.gusano1));
		levels.add(12, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(13)+ " "+contexto.getResources().getString(R.string.dificil)+"\n"+ contexto.getResources().getString(R.string.gusano2));
		levels.add(13, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(14)+ " "+contexto.getResources().getString(R.string.dificil)+"\n"+ contexto.getResources().getString(R.string.relojes));
		levels.add(14, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(15)+ " "+contexto.getResources().getString(R.string.dificil)+"\n"+ contexto.getResources().getString(R.string.operaciones));
		levels.add(15, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(16)+ " "+contexto.getResources().getString(R.string.dificil)+"\n"+ contexto.getResources().getString(R.string.formascolores));
		levels.add(16, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(17)+ " "+contexto.getResources().getString(R.string.dificil)+"\n"+ contexto.getResources().getString(R.string.formascubos));
		levels.add(17, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(18)+ " "+contexto.getResources().getString(R.string.dificil)+"\n"+ contexto.getResources().getString(R.string.numsYlets));
		levels.add(18, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(19)+ " "+contexto.getResources().getString(R.string.muydificil)+"\n"+ contexto.getResources().getString(R.string.dados));
		levels.add(19, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(20)+ " "+contexto.getResources().getString(R.string.muydificil)+"\n"+ contexto.getResources().getString(R.string.formasrayas));
		levels.add(20, contexto.getResources().getString(R.string.nivel)+" "+ String.valueOf(21)+ " "+contexto.getResources().getString(R.string.muydificil)+"\n"+ contexto.getResources().getString(R.string.gusano3));		
		//Ultimo nivel: Ponemos la palabra Final 
		levels.add(21, contexto.getResources().getString(R.string.nivel)+" "+ "FINAL"+ " "+contexto.getResources().getString(R.string.muydificil)+"\n"+ contexto.getResources().getString(R.string.gusano4));
		
		//Imagenes
		levelsImagenes = new ArrayList<Integer>();
		levelsImagenes.add(0, R.drawable.mates1);
		levelsImagenes.add(1, R.drawable.letra2);
		levelsImagenes.add(2, R.drawable.letra3);
		levelsImagenes.add(3, R.drawable.geome4);
		levelsImagenes.add(4, R.drawable.dado5);
		levelsImagenes.add(5, R.drawable.num6);
		levelsImagenes.add(6, R.drawable.puntos7);
		levelsImagenes.add(7, R.drawable.reloj8);
		levelsImagenes.add(8, R.drawable.agujas9);
		levelsImagenes.add(9, R.drawable.dado10);
		levelsImagenes.add(10, R.drawable.num11);
		levelsImagenes.add(11, R.drawable.gusano1);
		levelsImagenes.add(12, R.drawable.gusano2);
		levelsImagenes.add(13, R.drawable.reloj8_8);
		levelsImagenes.add(14, R.drawable.num15);
		levelsImagenes.add(15, R.drawable.formascolor1);
		levelsImagenes.add(16, R.drawable.formascubos1);
		levelsImagenes.add(17, R.drawable.mates18);
		levelsImagenes.add(18, R.drawable.dado18);
		levelsImagenes.add(19, R.drawable.formasyrayas1);
		levelsImagenes.add(20, R.drawable.gusanorr10);
		levelsImagenes.add(21, R.drawable.gusanorr11);
		
	}
	
	
public void getExtras(){		
	
		Bundle extras = this.getIntent().getExtras();	
				
		nivel = extras.getInt("nivel");				
		indexTotalSecuencia2=extras.getInt("indexTotalSecuencia2");				
		indexTotalAux = extras.getInt("indexTotalAux");		
		vInicio = extras.getInt("vistaInicio");		
		vspin =extras.getInt("vistaspinner");
				
		acumulado=extras.getFloat("acumulado");						
		setMusicaFondo = extras.getBoolean("musicafondo");
		
		switch (nivel){
		case 1:
			numFilas=2; 
			numCol =2;		
			setTipoSecuencia(0); //numeros 
			corte=2;
			break;		
		case 2:
			numFilas=3;
			numCol=2;		
			setTipoSecuencia(1); //letras (25)
			corte=5;
			break;			
		case 3:
			numFilas=3;
			numCol=2;	
			//setVistaInformation();
			setTipoSecuencia(3); //num y letras (36)
			corte=5;
			break;							
		case 4:
			numFilas=4;
			numCol=2;			
			setTipoSecuencia(2); //geometria (18)
			corte=8;
			break;				
		case 5:			
			numFilas=4;
			numCol=2;	
			setTipoSecuencia(5);  //dados (18)
			corte=8;
			break;			
		case 6:
			numFilas=5; 
			numCol =2;	
			setTipoSecuencia(11); //num operaciones (16)
			corte=10;
			break;		
		case 7:
			numFilas=4;
			numCol=2;
			setTipoSecuencia(10); //puntos (8)
			corte=8;
			break;
		case 8:
			numFilas=5;
			numCol=2;		
			setTipoSecuencia(9); //relojes (17)
			corte=11;
			break;		
		case 9:
			numFilas=5;
			numCol=2;			
			setTipoSecuencia(8); //agujas (13)
			corte=11;
			break;		
		case 10:	
			numFilas=4;
			numCol=3;	
			setTipoSecuencia(5); //dados (18)
			corte=16;
			break;		
		case 11:	
			numFilas=4;
			numCol=3;	
			setTipoSecuencia(11); //num operaciones (16)
			corte=16;
			break;			
		case 12:	
			numFilas=5;
			numCol=2;		
			setTipoSecuencia(6); //gusano1 (10)
			corte=13;
			break;			
		case 13:	
			numFilas=5;
			numCol=2;
			setTipoSecuencia(7); //gusano2 (11)
			corte=14;
			break;		
		case 14:	
			numFilas=4;
			numCol=3;
			setTipoSecuencia(9); // relojes (17)
			corte=18;
			break;		
		case 15:	
			numFilas=4;
			numCol=4;			
			setTipoSecuencia(11); //num operaciones (16)
			corte=22;
			break;		
		case 16:
			numFilas=3;
			numCol=3;			
			setTipoSecuencia(12); // formas colores (9)
			corte=12;
			break;	
		case 17:
			numFilas=3;
			numCol=3;			
			setTipoSecuencia(13); // formascubos (9)
			corte=12;
			break;	
			
		case 18:	
			numFilas=6;
			numCol=3;		
			setTipoSecuencia(3); //num y letras (36)
			corte= 25;
			break;				
		case 19:				
			numFilas=6;
			numCol=3;	
			setTipoSecuencia(5); // dados (18) 
			corte=26;
			break;						
		case 20:			
			numFilas=4;
			numCol=3;	
			setTipoSecuencia(14); // formas y rayas (12
			corte=15;
			break;						
		case 21:				
			numFilas=4;
			numCol=3;	
			setTipoSecuencia(15); // gusano3
			corte = 15;
			break;						
		case 22:				
			numFilas=4;
			numCol=3;		
			setTipoSecuencia(16); // gusano3
			corte = 15;
			break;			
		}
	}

	public void setVistaInformation(){
	  if(vInicio!=0){	 //siempre que no sea la pantalla de inicio		
		vistainformacion.inflate();
		vistainformacion.setVisibility(View.VISIBLE);
		botonOKinformativo = (Button)findViewById(R.id.ok_text_inforsecuencias2);
		botonOKinformativo.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(soundPrefs.getBoolean("sonido", true))
					soundSecuencias.play(sonidoOkStub);
				vistainformacion.setVisibility(View.INVISIBLE);
			}
		});
	  }
	}

	public void setTipoSecuencia (int refTipoSecuencia){
		this.refTipoSecuencia=  refTipoSecuencia;
	}
	
	public int[] getTipoSecuencia(){		
		return Imagenes.tipoSecuencia[refTipoSecuencia];
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
		logoSecuenciaInicio.setImageResource(R.drawable.logosecuencias);
		
		textoTituloNumeros = (TextView)findViewById(R.id.textosecuenciasinicio);
		textoTituloNumeros.setText(getResources().getString(R.string.tituloseries));
		
		//BotonPausar
		botonPausar = (ImageView)this.findViewById(R.id.timeattack_botonpause);
									
		//Boton Sonido
		botonSonido = (ImageView)this.findViewById(R.id.timeattack_botonsonido);		
		
		//Boton volveraJugar
		botonvolverAjugar = (ImageView)this.findViewById(R.id.timeattack_botonbolverajugar);	
		
		//Texto para informar de los porcentajes que llevamos		
		textPorcentajes = (TextView)this.findViewById(R.id.secuencias_porcentajestextview);
		
		// gridview:
		gridlay = (GridLayout)findViewById(R.id.grid_timeattack);	
		
							
		//Cronometro para el tiempo transcurrido		
		crono = (Chronometer)this.findViewById(R.id.secuencias_crono);
		
	//INICIACION DEL SPINNER PARA SELECCION DE NIVEL:
		final SharedPreferences.Editor editor = gamePrefs.edit(); //editor para poner en memoria el nivel que seleccionamos en el spinner
		
		spinnerNivel = (Spinner)this.findViewById(R.id.secuencias_spiner);		
		spinnerNivel.setPrompt(contexto.getResources().getString(R.string.Niveles));
		spinnerNivel.setVisibility(vspin);
		
		//Meteremos los elementos de forma dinamica:
		spinnerValores = new ArrayList<String>(); 
		for (int i=0; i<indexTotalSecuencia2; ++i){			
			spinnerValores.add(i, levels.get(i));
			spinnerImagenes1[i] =levelsImagenes.get(i);
		}
		
		spinnerNivel.setAdapter(new MyAdapter(this, R.layout.spinner_customseries, spinnerValores ));			
		elemSpinner = spinnerNivel.getCount();		
		
		spinnerNivel.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
				
					//Con este truco solo cambiamos la apariencia del primer elemento del spinner!!!
				 //	((TextView) parentView.getChildAt(3)).setTextColor(Color.BLACK);
				  //  ((TextView) parentView.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
			       // ((TextView) parentView.getChildAt(3)).setTextSize(22);		
			      //  ((TextView) parentView.getChildAt(3)).setGravity(17);	//con el 17 centramos
			        
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
				if (soundPrefs.getBoolean("sonido", true))		
					//soundSecuencias.play(sonidoSalir_Nivel)
					sonidoSalir_Nivel.start();
				return false;
			}
		});
		
		
		
		//INICIACION DE VISTAS, PONERLAS VISIBLES O INVISIBLES:
		if(vInicio==View.VISIBLE){
			botonPausar.setVisibility(View.INVISIBLE);
			botonSonido.setVisibility(View.INVISIBLE);
			botonvolverAjugar.setVisibility(View.INVISIBLE);			
			textPorcentajes.setVisibility(View.INVISIBLE);
			gridlay.setVisibility(View.INVISIBLE);
			//adView.setVisibility(View.INVISIBLE);
			crono.setVisibility(View.INVISIBLE);					
			
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
								botonOKinformativo = (Button)findViewById(R.id.ok_text_inforsecuencias2);
								botonOKinformativo.setOnClickListener(new View.OnClickListener() {			
									@Override
									public void onClick(View v) {
										if (soundPrefs.getBoolean("sonido", true))
											soundSecuencias.play(sonidoOkStub);
										configGrid();
										botonPausar.setVisibility(View.VISIBLE);
										botonSonido.setVisibility(View.VISIBLE);
										botonSonido.setImageResource(soundPrefs.getBoolean("sonido", true)==true?R.drawable.boton_sonidoonfondo:R.drawable.boton_sonidoofffondo);			
										botonvolverAjugar.setVisibility(View.VISIBLE);			
										textPorcentajes.setVisibility(View.VISIBLE);
										textPorcentajes.startAnimation(alfa);
										gridlay.setVisibility(View.VISIBLE);										
										gridlay.startAnimation(alfa);
										crono.setVisibility(View.VISIBLE);
										crono.setBase(SystemClock.elapsedRealtime());
										//adView.setVisibility(View.VISIBLE);
										crono.start();
										vistainformacion.setVisibility(View.INVISIBLE);		
										
									}
								});							 
						 }else{
							 	configGrid();
							 	botonPausar.setVisibility(View.VISIBLE);
							 	botonSonido.setVisibility(View.VISIBLE);
							 	botonSonido.setImageResource(soundPrefs.getBoolean("sonido", true)==true?R.drawable.boton_sonidoonfondo:R.drawable.boton_sonidoofffondo);			
								botonvolverAjugar.setVisibility(View.VISIBLE);			
								textPorcentajes.setVisibility(View.VISIBLE);
								textPorcentajes.startAnimation(alfa);	
								gridlay.setVisibility(View.VISIBLE);
								gridlay.startAnimation(alfa);
								//adView.setVisibility(View.VISIBLE);
								crono.setVisibility(View.VISIBLE);
								crono.setBase(SystemClock.elapsedRealtime());
								crono.start();
						 }							
						}
					});					
				}				
			}, 500);	
		}					
	}
	
	//CLASE ADAPTADOR PARA SPINNER DE IMAGENES en series
	public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context ctx, int txtViewResourceId, ArrayList<String> objects) {
            super(ctx, txtViewResourceId, objects);
        }
 
        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }
        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }
        
        public View getCustomView(int position, View convertView,  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.spinner_customseries, parent, false);
           
            TextView subSpinner = (TextView) mySpinner.findViewById(R.id.text_main_seen_series);
            ImageView left_icon1 = (ImageView) mySpinner.findViewById(R.id.left_pic1_series);          
           
            subSpinner.setText(spinnerValores.get(position));
            left_icon1.setImageResource(spinnerImagenes1[position]);
          
            
            return mySpinner;
        }
    }
	
	
	//devuelve las posiciones en las que se encuentran los elementos de la lista de menor a mayor. 
	
	public ArrayList<Integer> orden (ArrayList<Integer> listado){
		ArrayList<Integer> lista= new ArrayList<Integer>();				
		for (int i=0; i<listado.size(); ++i){			
			lista.add(i, listado.indexOf(i)) ;			
		}		
		return lista;
	}
	
	public void setupLista(){
		//creamos valores  para asignarlos a las ids de los LinearLayouts que crearemos en Modo Timeattack normal:
				lista = new ArrayList<Integer>();		
				for (int i=0; i<base; ++i){
					lista.add(i);				
				}		
				Collections.shuffle(lista);
	}
	
	
	public void configGrid(){				
		parlinear= new ArrayList<LinearLayout>();
		
		setupLista();
		
		establecerGrid();		
	
		int primerElemento= orden(lista).get(0); //ponemos el primer elemento			
		establecerPrimerElemento(primerElemento);
	}
	
	public void establecerGrid(){
		
		for (int i =0; i<numFilas;++i){
			for (int j=0; j<numCol; ++j){				
				LinearLayout linear = new LinearLayout(this);			
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();
				
				ImageView ima1 = new ImageView(this);
				ImageView ima2 = new ImageView(this);								
				
				linear.setId(lista.get(ids));
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
				ima1.setImageResource(R.drawable.iinterr);	
				//ima1.setImageResource(getTipoSecuencia()[linear.getId()]);				
				ima1.setVisibility(View.VISIBLE);					
				linear.addView(ima1,0);	
				
					
				ima2.setLayoutParams(params2);
				ima2.setImageResource(getTipoSecuencia()[linear.getId()]);				
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
		
		im1.setImageResource(getTipoSecuencia()[lista.get(position)]);
		im2.setImageResource(getTipoSecuencia()[lista.get(position)]);
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
	}
	
	class linearListener implements View.OnClickListener{
		LinearLayout linear, lin1;
		ImageView ima1, ima2;		
		ObjectAnimator animarPieza;
		int indice2, indice3,indice4,  tagTocado;
		int j=0;
		
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
						
						indice3 = Integer.valueOf((String)lin1.getTag()) ; //el que tocamos						
						indice2 = orden(lista).get(indexImagen);	//el que deberia ser	
						
						// si son consecutivas, las dejamos las dos arriba
						if(indice2==indice3){	
							if (soundPrefs.getBoolean("sonido", true))
								soundSecuencias.play(sonidoboton25);	
							
							animarPieza.start();
							parlinear.get(0).setId(OK);	
							parlinear.remove(0);					
							indexImagen++;
							aciertos++;							
						
							if(aciertos==base){ //Terminamos la partida
								setGameOver(true);	
								if (soundPrefs.getBoolean("sonido", true))
									soundSecuencias.play(aplausosSound1);									
								
								ViewHelper.setAlpha(gridlay, 0.4f);
								botonvolverAjugar.setVisibility(View.INVISIBLE);								
								textPorcentajes.setVisibility(View.INVISIBLE);
								crono.setVisibility(View.INVISIBLE);
								botonPausar.setVisibility(View.INVISIBLE);
								botonSonido.setVisibility(View.INVISIBLE);													
								timeCrono = SystemClock.elapsedRealtime() - crono.getBase(); //pillamos el tiempo que ha pasado cuando paramos el crono
								crono.stop();
																
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
								},time*2);								
							}
						}
						else{ //si no, le damos la vuelta a todos las que se dieron la primera vuelta y a empezar denuevo								
							darVueltaAtodas();
						}	
						final ScaleAnimation anim = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f);						
						anim.setDuration(500);
						anim.setRepeatCount(1);
						anim.setRepeatMode(Animation.REVERSE);	
						textPorcentajes.setText(String.valueOf(aciertos)+"/"+String.valueOf(numClicks)+
								" ("+String.valueOf((100*aciertos/numClicks)+"%)") 
								+" / "
								+contexto.getResources().getString(R.string.vueltas) +String.valueOf(numTandas));
						handler.post(new Runnable(){
							@Override
							public void run() {		
								if(indice2==indice3){
									textPorcentajes.startAnimation(anim);
								}									
							}							
						});
					}					
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
							numTandas++;							
							if(numTandas==(corte*z)){//damos la vuelta a todas y reorganizamos cada tandas
								z++;
								girarImagen(ima2, ima1, 100);								
								for(int i=0; i<base;++i){
									LinearLayout lin = (LinearLayout)gridlay.getChildAt(orden(lista).get(i));
									ImageView im1 = (ImageView)lin.getChildAt(0);
									ImageView im2 =(ImageView)lin.getChildAt(1);
									if(lin.getId()==OK){
										girarImagen(im2, im1, 100);										
										ViewHelper.setAlpha(lin, 1f);										
									}	
								}
								setupLista(); //creamos una nueva lista
								for(int i=0;i<base;++i){
									LinearLayout lin = (LinearLayout)gridlay.getChildAt(i);
									ImageView im1 = (ImageView)lin.getChildAt(0);
									ImageView im2 =(ImageView)lin.getChildAt(1);
									lin.setId(lista.get(i));
									im1.setImageResource(R.drawable.iinterr);	
									im2.setImageResource(getTipoSecuencia()[lin.getId()]);		
								}								
								int primerElemento= orden(lista).get(0); //ponemos el primer elemento			
								establecerPrimerElemento(primerElemento);								
							}else if(numTandas!=corte){
								girarImagen(ima2, ima1, 100);			
								for (int i=1; i<base; ++i){
									LinearLayout child = (LinearLayout) gridlay.getChildAt(orden(lista).get(i));
									ImageView subchild1 = (ImageView)child.getChildAt(0);
									ImageView subchild2 = (ImageView)child.getChildAt(1);
									if(child.getId()==OK){
										girarImagen(subchild2, subchild1, 100);
										child.setId(lista.get(i));
										ViewHelper.setAlpha(child, 1f);										
									}								
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
					parlinear.remove(0);	
				}								
			}, 800+110);			
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
		public float scoreSystem(){							
			//Base de cada nivel. Eje para un panel 4x3 la base son 6. 
			float base = numFilas*numCol;		
					
			float punto = base*10;
			//Aplicamosm un porcentaje a cada punto
			float porcent = (float)aciertos/(float)numClicks;			
			//Aunemtamos el total cada vez que acertemos				
			while (i<aciertos){				
				total = total +(punto);
				i++;		
				//System.out.println("i :"+i+" aciertos :"+aciertos+" total "+total);
			}							
			//En caso de acertar o no acertar mutiplicamos por el total por el porcentaje.						
			sumaTotal = (total*porcent)-numTandas;			
			if(sumaTotal<1)
				sumaTotal=1;
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
						
			TextView textNivel,textPuntos,textTiempo, textAciertos, textNivelLogrado,
			textPuntosTotales, textPuntosAcumulados,	
			textFelicidades, textCompletado;	
			Button backTomenu, nextLevel, botonCompartir, botonScores;
			
			ImageView imagenSuperado;
			LinearLayout linearaciertos, linearnivel, linearpuntos, lineartiempo, lineartiemporestante,
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
					dm.widthPixels/15, dm.heightPixels/25);
				panta.setLayoutParams(relatparams);
				llay.addView(panta);				
				 
				
				 textNivel= (TextView)panta.findViewById(R.id.text_levelcompleted);
				 textFelicidades =(TextView)panta.findViewById(R.id.text_felicidades);
				 textCompletado=(TextView)panta.findViewById(R.id.text_completado);
				 imagenSuperado = (ImageView)panta.findViewById(R.id.imagen_logosuperado);
				 
				 linearaciertos = (LinearLayout)panta.findViewById(R.id.linearaciertossecuencias);
				 linearaciertos.setVisibility(LinearLayout.VISIBLE);				
				 textAciertos=(TextView)panta.findViewById(R.id.marcador_aciertossecuencias);
				 
				 linearnivel = (LinearLayout)panta.findViewById(R.id.linearNivel);
				 linearnivel.setVisibility(LinearLayout.VISIBLE);
				 textNivelLogrado = (TextView)panta.findViewById(R.id.marcador_nivelsecuencias);
				
				 linearpuntos = (LinearLayout)panta.findViewById(R.id.linearpuntos);
				 linearpuntos.setVisibility(LinearLayout.VISIBLE);
				 textPuntos =(TextView)panta.findViewById(R.id.marcador_puntostimeattack);
				 
				 lineartiempo = (LinearLayout)panta.findViewById(R.id.lineartiempo);
				 lineartiempo.setVisibility(LinearLayout.VISIBLE);
				 textTiempo =(TextView)panta.findViewById(R.id.marcador_tiempototaltimeattack);	
				 
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
							
				/*nivel22*/
				 if(nivel==22){	
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
						rotarAnimation.setDuration(1000);
						imagenSuperado.startAnimation(rotarAnimation);				
						
					}
				
				//Ponemos un alphaanimation para que aparezcabn los marcadores.
				
				AlphaAnimation alphaanim = new AlphaAnimation(0.5f,1);
				alphaanim.setDuration(2000);
				panta.startAnimation(alphaanim);	
				
				
				nextLevel.setOnClickListener(new View.OnClickListener() {				
					@Override
					public void onClick(View arg0) {
						//Mandamos los datos iniciales:		
						Intent i = new Intent (contexto, ActivitySecuencias2.class);
						if (soundPrefs.getBoolean("sonido", true))
							//soundSecuencias.play(sonidoSalir_Nivel);
							sonidoSalir_Nivel.start();
						nivel++; 
						
						if((indexTotalSecuencia2-indexTotalAux)>1){
							indexTotalAux++;
						}else{
							indexTotalSecuencia2++;
							indexTotalAux++;
						}				
						i.putExtra("nivel", nivel);						
						i.putExtra("indexTotalSecuencia2", indexTotalSecuencia2);									
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
						//nivel22
						if(nivel!=22){
						  if(keybackTocado==0){ //Ponemos una variable keybackTocado para que si pulsamos mucho el boton back no se acumulen elementos al spiner		
							if((indexTotalSecuencia2-indexTotalAux)>1){
								indexTotalAux++;
							}else{
								indexTotalSecuencia2++;
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
								getResources().getString(R.string.sharecontent_puntos2)+" "+getResources().getString(R.string.tituloseries)+								
								getResources().getString(R.string.sharecontent_puntos3)+" "+String.valueOf(nivel)+
								getResources().getString(R.string.sharecontent_puntos4)+" "+String.format("%.2f", scoreSystem())+
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
								,getResources().getString(R.string.leaderboard_sequence_series))
								,500);						
					}
				});
				*/
						
				//Puntuaciones:
				//Pasado el tiempo de la animacion , ponemos las puntuaciones
				setTimeThread();
				new Timer().schedule(new TimerTask(){
					@Override
					public void run() {
						handler.post(new Runnable(){
							@Override
							public void run() {
								/*nivel22*/
								if(nivel==22){								
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
						//Porcentaje de aciertos:			
						handler.post(new Runnable(){
							@Override
							public void run() {								
								textAciertos.setText(String.valueOf(aciertos)+"/"+String.valueOf(numClicks)+
										" ("
										+String.valueOf((100*base/numClicks)+"%)")
										+" / "+ String.valueOf(numTandas));								
							}							
						});
						
						//Nivel alcanzado:
						handler.post(new Runnable(){
							@Override
							public void run() {								
								textNivelLogrado.setText(String.valueOf(nivel));
																	
							}							
						});
						
						//Puntos:
						new Thread(){
							@Override
							public void run() {
								while(j<(int)scoreSystem()){
									try {
										Thread.sleep(matrizTimeThread[1]);
									} catch (InterruptedException e) {							
										e.printStackTrace();
									}
									handler.post(new Runnable(){
										@Override
										public void run() {
											textPuntos.setText(String.valueOf(j));			
										}							
									});
									j+=matrizTimeThread[0];
								}
								//finalizado el bucle ponemos los puntos en float
								handler.post(new Runnable(){
									@Override
									public void run() {
										textPuntos.setText(String.format("%.2f", scoreSystem()));
									}
								});
							}							
						}.start();
													
						//Tiempo:					
						new Thread(){							 
							@Override
							public void run() {							
								//System.out.println(timeCrono);
								while(k<((int)timeCrono/1000)){
									try {
										Thread.sleep(5);
									} catch (InterruptedException e) {							
										e.printStackTrace();
									}
									handler.post(new Runnable(){
										@Override
										public void run() {
											textTiempo.setText(String.valueOf(k));		
										}							
									});
									++k;
								}
							}							
						}.start();
						
											
						//Puntos totales y acumulado total:
						new Thread(){
							@Override
							public void run() {
								while(m<(int) scoreSystem()){
									try {										
										Thread.sleep(matrizTimeThread[1]);										
									} catch (InterruptedException e) {							
										e.printStackTrace();
									}
									handler.post(new Runnable(){
										@Override
										public void run() {
											textPuntosTotales.setText(String.valueOf(m));		
										}					 		
									});
									m+=matrizTimeThread[0]; //subimos en vez de uno en uno, de x en x para acortar un poco. 
									
								}							
								handler.post(new Runnable(){
									@Override
									public void run() {
										//una vez finalizado el bucle, ponemos los decimales float:
										textPuntosTotales.setText(String.format("%.2f", scoreSystem()));
										//Ponemos los puntos acumulados una vez terminado el bucle de los puntos totales 										
										acumulado=acumulado+scoreSystem();
										new ScoresHigh(contexto, GAME_PREFS ,acumulado , "highScoresSecuencias").setHighScores() ;
										//subimos el acumulado total a Google play games en caso de estar conectado
										/*
										if(isSignedIn())										
											subirPuntuacion();
										*/
										textPuntosAcumulados.setText(String.format("%.2f", acumulado));	
										//Hacemos visible los botones una vez se haya completado el bucle del contador para que la variable acumulado se actualice.
										//menos en el ultimo nivel:
										/*nivel22*/
										if(nivel==22)
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
						}.start();	
					}				
				}, 1000);			
			}			
		}
		
		public void pausarCrono(){
			setGamePaused(true);	
			timeCrono =  crono.getBase() -SystemClock.elapsedRealtime(); //pillamos el tiempo que ha pasado cuando paramos el crono
			crono.stop();
			ViewHelper.setAlpha(gridlay, 0.5f);
		}
		
		public void reanudarCrono(){		
			setGamePaused(false);	
			crono.setBase(SystemClock.elapsedRealtime()+timeCrono);
			crono.start();		
			ViewHelper.setAlpha(gridlay, 1f);	
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
			if(!getGamePaused()){
				pausarCrono();				
	    		setGamePaused(false);
			}				
			//musicafondo.pause();
				
		}

		@Override //vuelta de foreground
		protected void onResume() {		
			//adView.resume();
			super.onResume();		
			if(!getGamePaused()){           	
           	  reanudarCrono();
             }				
		}				
		
		
		public void botonPause(View v){
			if(pulsar==0){
				botonPausar.setImageResource(R.drawable.boton_reanudar);
				pulsar=1;
				pausarCrono();
				//musicafondo.pause();
			}else{
				botonPausar.setImageResource(R.drawable.boton_pausa);
				pulsar=0;
				reanudarCrono();
				//musicafondo.start();
			}
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
			Intent i = new Intent(this, ActivitySecuencias2.class);	
			if (soundPrefs.getBoolean("sonido", true))
				soundSecuencias.play(sonidoPlay);
			indexTotalAux=index;	
			
			//Usamos for con if en vez de un switch case
			//NIVEL22//
			for ( int j=0; j<22; ++j){
				if (index+1==j+1)
					i.putExtra("nivel", j+1);				
			}			
		
			i.putExtra("indexTotalSecuencia2", indexTotalSecuencia2);			
			i.putExtra("indexTotalAux", indexTotalAux);	
			i.putExtra("vistaInicio", View.INVISIBLE);				
			i.putExtra("musicafondo", true);			
			startActivity(i);		
			finish();			
		}
		
		public void volverMenuInicial(){			
			
			Intent i = new Intent(contexto,ActivitySecuencias2.class);			
			i.putExtra("nivel", nivel);			
			i.putExtra("indexTotalSecuencia2", indexTotalSecuencia2);			
			i.putExtra("indexTotalAux", indexTotalAux);			
			i.putExtra("vistaInicio", View.VISIBLE);	
			i.putExtra("vistaspinner", View.VISIBLE);
			startActivity(i);
			finish();	
		}
		
		public void volveraMenu(){		
			//Ponemos un alertdialog para preguntar si realmente queremos salir. 
			//En caso afirmativo perdemos los progresos de la puntuacion con lo cual ponemos la puntuacion obtenida
			// y no se acumulan mas puntos
			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
			alert.setTitle(R.string.deseasalir);			
			alert.setIcon(R.drawable.boton_tick);
			alert.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					volverMenuInicial();
					if(!marcadoresMostrados){
						setGameOver(true);
						crono.stop();
					}					
				}
			});
			alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(!marcadoresMostrados)
						reanudarCrono();
				}
			});		
			AlertDialog alerta = alert.create();
			alerta.show();		
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
			//i.putExtra("indexTotalSecuencia2", nivel);
			i.putExtra("indexTotalSecuencia2", indexTotalSecuencia2);
			i.putExtra("modoJuego", "secuencias");
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Ver http://www.htcmania.com/showthread.php?t=735248
			startActivity(i);
			finish();	
		}
		
		
		public void botonMenuSalirSecuencias (View v){
			Intent i = new Intent(this, ActivityModos.class);
			if (soundPrefs.getBoolean("sonido", true))
				//soundSecuencias.play(sonidoSalir_Nivel);	
				sonidoSalir_Nivel.start();
			//i.putExtra("indexTotalSecuencia2", nivel);
			i.putExtra("indexTotalSecuencia2", indexTotalSecuencia2);
			i.putExtra("modoJuego", "secuencias");
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Ver http://www.htcmania.com/showthread.php?t=735248
			startActivity(i);
			finish();	
		}
		
		//si pulsamos back en cualquier parte del juego en donde no sea visible vistaInicio, volvemos al menu inicial.
		//Si estamos en el menu donde aparece la seleccion de nivel y pulsamos atras, salimos del modo TimeAttack
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(vInicio==View.INVISIBLE){
			    if ((keyCode == KeyEvent.KEYCODE_BACK)) {	
			    	if(marcadoresMostrados){ //si se ven los marcadores
			    		//ponemos un cuadro de dialogo en todos los niveles menos en el ultimo y tambien subimos un elemento en el spiner
			    		//nivel22
			    		 if(nivel!=22){	//Si no estamos En el ultimo nivel  sumamos un elemento al spinner		    			 
			    			 if(keybackTocado==0){ //Ponemos una variable keybackTocado para que si pulsamos mucho el boton back no se acumulen elementos al spiner			    				 
			    				 keybackTocado++;
			    				 if((indexTotalSecuencia2-indexTotalAux)>1){
			    						indexTotalAux++;
			    				}else{
			    					indexTotalSecuencia2++;
			    					indexTotalAux++;
			    				}		    				
			    			 }
			    			 volveraMenu();	
			    				
			    		}else{//si es el ultimo nivel entonces no mostramos el cuadro dialogo ni sumamos elementos al spinner	  
			    			volverMenuInicial(); //volvemos al menu inicial sin mostrar cuadro de dialogo
			    		}
			    	}else{ //si no se ven los marcadores , es decir durante el juego, mostramos el cuadro de dialogo			    	
			    		if(!getGamePaused()){
				    		pausarCrono();			    		
				    		setGamePaused(false);
				    	}	
			    		volveraMenu();
			    	}	 
			    }
		    
			}else if(vInicio==View.VISIBLE){
				botonmenusalir();
			}
		    return super.onKeyDown(keyCode, event);
		}
		
		
		public void botonvolveraJugar (View v){
			//mantenemos la variable nivel		
				setGameOver(true);
				Intent i = new Intent (contexto, ActivitySecuencias2.class);					
				
				i.putExtra("nivel", nivel);				
				i.putExtra("indexTotalSecuencia2", indexTotalSecuencia2);				
				i.putExtra("indexTotalAux", indexTotalAux);					
				i.putExtra("vistaInicio", View.INVISIBLE);
					
				i.putExtra("acumulado", acumulado);						
				//musicafondo.stop();
				i.putExtra("musicafondo", true);
				startActivity(i);	
				finish();		
		}

		
		/*
		public void botonVolveraJugarFracaso (View v){
			//mantenemos la variable nivel		
			Intent i = new Intent (contexto, ActivitySecuencias2.class);		
			//mp3.start();						
			i.putExtra("nivel", nivel);			
			i.putExtra("indexTotalSecuencia2", indexTotalSecuencia2);			
			i.putExtra("indexTotalAux", indexTotalAux);			
			i.putExtra("vistaInicio", View.INVISIBLE);						
			
			i.putExtra("acumulado", acumulado - scoreSystem());			
			i.putExtra("musicafondo", true);
			startActivity(i);	
			finish();		
		}
		*/
}
