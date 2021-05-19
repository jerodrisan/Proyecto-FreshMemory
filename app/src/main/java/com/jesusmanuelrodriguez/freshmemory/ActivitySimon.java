package com.jesusmanuelrodriguez.freshmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import com.google.android.gms.games.Games;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitySimon extends Activity{
	
	SharedPreferences gamePrefs;
	SharedPreferences.Editor editor;
	Context contexto;
	
	public static final String GAME_PREFS = "Simon";
	public static final String MAX_PUNT = "maximapuntuacion";
	public static final String MAX_PUNT_MODOS = "maximapuntuacionmodos"; //mismo dato que maxima puntuacion pero para que sea visto en los marcadores de ActivityModos
	private static final int NUM_FILAS =3;
	private static final int NUM_COLUMNAS = 3;
	
	private static final int SPEED_SLOW = 1200;
	private static final int SPEED_SLOW_THREAD= 1300;
	private static final int SPEED_MEDIUM = 700;
	private static final int SPEED_MEDIUM_THREAD= 780;
	private static final int SPEED_FAST = 200;
	private static final int SPEED_FAST_THREAD= 250;
	
	DisplayMetrics dm;
	
	int gap, gapLeft, gapTop, anchoIm, altoIm;
	int zonaJuegoHight;
	int ids=0;
	int indice;
	int level=1;
	int contMaquina=0;
	int contJugador=0;
	int maximaPunt;
	int conta=0;	
	int asfa=0;

	
	String nivel;
	boolean turnoJugador= false;
	boolean rondaTerminada = false;
	boolean finPartida = false;
	boolean running=true; //variable para controlar el hilo
	
		
	ViewStub vistainformacion;
	Button botonStart, botonNivel, botonOkstubview;
	TextView textYourTurn, textAciertos;
	GridLayout gridlay;
	LinearLayout linear, linear1_simon;
	
	Hilo1 startRondaMaquina;
	Handler handler;
	
	ArrayList<LinearLayout> parlinear;
	ArrayList<Integer> lista, ordenes;	
	
	SonidosSimon soundSimon;	
	//1:  int sonido1 = soundSimon.load(R.raw.pruebasimon); (seria el sound_id)	
	int[] sonidoTeclas = new  int[10]; 
	int sonido1;
	int sonidoPlay, sonidoNivel,sonidoOkstub, sonidoRecord;
	
	SharedPreferences soundPrefs, conexionScores;
	SharedPreferences.Editor editor2, editorScores;
	public static final String SONIDO = "sonido";	
	public static final String CONEXION = "conexion";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simon);	
		
		contexto=this;
		
		//Preferencias para el sonido. Cuando creamos la actividad , por defecto el sonido estar� activado y asi lo guardaremos 
		soundPrefs = this.getSharedPreferences(SONIDO, 0);		
		editor2 = soundPrefs.edit();
		editor2.putBoolean("sonido", soundPrefs.getBoolean("sonido", true)); 
		editor2.commit();
				
		gamePrefs = getSharedPreferences(GAME_PREFS, 0);		
		maximaPunt = gamePrefs.getInt(MAX_PUNT, 0);				
		
		ordenes = new ArrayList<Integer>();	
		
		linear1_simon = (LinearLayout)this.findViewById(R.id.linear1_Simon);
		configPantalla();
		
		initViews();	//Inicializacion de todas las views	y sharedpreferences			
		
		configSounds();
	
		botonStart.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(soundPrefs.getBoolean("sonido", true))
					soundSimon.play(sonidoPlay);
				iniciarPartida();				
				if(ordenes.size()!=0){
					ordenes.clear();
				}
			}
		});				 
		configGrid(); 					
		handler = new Handler();		
	
		
		//Preferencias conexion 
		conexionScores = this.getSharedPreferences(CONEXION, 0);		
		//Conectamos en la activity con google play services
		/*
		if(conexionScores.getBoolean(CONEXION, true))
			beginUserInitiatedSignIn();
		*/
		
	}//FIN ON CREATE
	/*
	@Override
	public void onSignInFailed() {			
	}

	@Override
	public void onSignInSucceeded() {			
	}
	
	public void subirPuntuacion(){		
		//solo subimos puntuacion si estamos conectados:
		if(isSignedIn()){			
			Games.Leaderboards.submitScore(getApiClient(), getResources().getString(R.string.leaderboard_simon), gamePrefs.getInt(MAX_PUNT, 0));	
			if (contMaquina-1<=8){
				Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_fly_memory));
			}
			else if(contMaquina-1 >8 && contMaquina-1 <=14){
				Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_fish_memory));
			}
			else if(contMaquina-1 >14 && contMaquina<=25){
				Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_memorion));
			}
			else {
				Games.Achievements.unlock(getApiClient(), getResources().getString(R.string.achievement_elephant_memory));
			}
		}
	}
	//Para ver marcadores , ver boton botonScores	
	///FIN GOOGLE PLAY GAMES
	*/
	
	public void configSounds(){
		soundSimon = new SonidosSimon(this);
		soundSimon.setVolume(0.5f);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);		
		sonidoTeclas[0]=soundSimon.load(R.raw.loop1);
		sonidoTeclas[1]=soundSimon.load(R.raw.loop2);
		sonidoTeclas[2]=soundSimon.load(R.raw.loop3);
		sonidoTeclas[3]=soundSimon.load(R.raw.loop4);
		sonidoTeclas[4]=soundSimon.load(R.raw.loop5);
		sonidoTeclas[5]=soundSimon.load(R.raw.loop6);
		sonidoTeclas[6]=soundSimon.load(R.raw.loop7);
		sonidoTeclas[7]=soundSimon.load(R.raw.loop8);
		sonidoTeclas[8]=soundSimon.load(R.raw.loop9);
		sonidoTeclas[9]=soundSimon.load(R.raw.loopintermitente);
		sonidoPlay = soundSimon.load(R.raw.sonidoplay);
		sonidoNivel = soundSimon.load(R.raw.button54);
		sonidoOkstub = soundSimon.load(R.raw.latchmetalclick1);
		sonidoRecord = soundSimon.load(R.raw.applau22);
		
	}
	
	public void iniciarPartida(){
		if(!finPartida){			
			textAciertos.setText(String.valueOf(0));			
			turnoJugador=false;		
			quitarBotonNivel_Start();			
			setNivel(getNivel());
			iniciarMaquina();			
		}		
	}
	
	
	public void findepartida(){
		
		//Registramos el marcador mas alto
		editor = gamePrefs.edit();		
		if((contMaquina-1) >gamePrefs.getInt(MAX_PUNT, 0)){
			editor.putInt(MAX_PUNT, (contMaquina-1));
			editor.commit();
			//subimos el acumulado total a Google play games en caso de estar conectado
			/*
			if(isSignedIn())										
				subirPuntuacion();
			*/
			new Timer().schedule(new TimerTask(){ //Resaltamos el record obtenido
				@Override
				public void run() {		
					handler.post(new Runnable(){
						@Override
						public void run() {
							if(soundPrefs.getBoolean("sonido", true))
								soundSimon.play(sonidoRecord);
							new ComboPantalla1(ActivitySimon.this, ActivitySimon.this.getResources().getString(R.string.nuevorecord)+" "+
									gamePrefs.getInt(MAX_PUNT, 0) ,R.id.simon_layout,
									getResources().getColor(R.color.green), false, 1,1.5f,1,1.5f, 3000, 
									 R.drawable.fondo_combotransparente).run();
							
						}					
					});				
				}			
			}, 2000);			
			
		}		
		finPartida=false;
		ponerBotonNivel_Start();		
		textYourTurn.setText(getNivel());		
		textAciertos.setText(getResources().getString(R.string.maxPuntuacionSimon)+"  "+
				String.valueOf(gamePrefs.getInt(MAX_PUNT, 0)));
		//Marcador de los puntos Obtenidos
		new ComboPantalla1(this, getResources().getString(R.string.puntosSimon)+" "+
				(contMaquina-1) ,R.id.simon_layout,
				getResources().getColor(android.R.color.black), true, 1,1.5f,1,1.5f, 1000,
				 R.drawable.fondo_combotransparente).run();
		
		//Pasamos tambien LA PUNTUACION obtenida a la pantalla de modos para poder ver la fecha y el los mejores resultados 		
		new ScoresHigh(this, GAME_PREFS ,(float)(contMaquina-1) , MAX_PUNT_MODOS).setHighScores() ;
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run() {
						new MarcadoresSimon().run();						
					}					
				});								
			}			
		}, 1500);
		
		//Si queremos un Toast:
		//Toast.makeText(ActivitySimon.this, this.getResources().getString(R.string.partidaterminada)+"  "
		//		+String.valueOf(contMaquina-1) , Toast.LENGTH_LONG).show();
		
	}
		
	public void iniciarMaquina(){						
			if(turnoJugador ==false){		
				textYourTurn.setText(getResources().getString(R.string.turnoMaquina));	
				new Timer().schedule(new TimerTask(){
					@Override
					public void run() {
						new Hilo1().start();
					}					
				}, 500);				
				contMaquina=0;				
			}
	}
	
	public void quitarBotonNivel_Start(){
		botonNivel.setClickable(false);			
		botonNivel.startAnimation(desvanecerBoton(1.0f, 0.3f, 300));
		botonStart.setClickable(false);
		botonStart.startAnimation(desvanecerBoton(1.0f, 0.3f, 300));
		
	}
	public void ponerBotonNivel_Start(){
		botonNivel.startAnimation(desvanecerBoton(0.3f, 1.0f, 300));		
		botonNivel.setClickable(true);			
		botonStart.startAnimation(desvanecerBoton(0.3f, 1.0f, 300));
		botonStart.setClickable(true);
	}
	
	class Hilo1 extends Thread{
		
		Random r = new Random(System.currentTimeMillis());
		int num = r.nextInt(gridlay.getChildCount());		
		
		@Override
		public void run() {				
			super.run();			
			ordenes.add(num);
			while (contMaquina < ordenes.size()){		
			//  if(!running)return;
				LinearLayout linear1 = (LinearLayout) gridlay.getChildAt(ordenes.get(contMaquina));	
				
				ImageView imagen1 = (ImageView)linear1.getChildAt(0);
				ImageView imagen2 = (ImageView)linear1.getChildAt(1);
				
				simularToque(imagen1, imagen2, getTimeLevel()[0]);						
				try {
					Thread.sleep(getTimeLevel()[1]);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}										
				++contMaquina;			  
			}
			turnoJugador=true;	
			handler.post(new Runnable(){
				@Override
				public void run() {
					textYourTurn.setText(ActivitySimon.this.getResources().getString(R.string.tuTurno));	
					
				}				
			});			
		}		
	}	
	
	public void simularToque(final ImageView imagen1, final ImageView imagen2, int tiempo){
		handler.post(new Runnable(){ //ponemos handler ya que las views solo las pueden tocar los threads originales
			@Override
			public void run() {
				if(soundPrefs.getBoolean("sonido", true))
					sonido1 =soundSimon.play(sonidoTeclas[ordenes.get(contMaquina)]);				
				imagen1.setVisibility(View.GONE);
				imagen2.setVisibility(View.VISIBLE);
			}			
		});		
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				handler.post(new Runnable(){ //idem 
					@Override
					public void run() {
						soundSimon.stop(sonido1);
						imagen1.setVisibility(View.VISIBLE);
						imagen2.setVisibility(View.GONE);						
					}					
				});				
			}			
		},tiempo);
	}
	
		
	public void seleccionarNivel(View v){
		if(soundPrefs.getBoolean("sonido", true))
			soundSimon.play(sonidoNivel);
		if(textYourTurn.getText().equals(getResources().getString(R.string.nivelmedio))){
			textYourTurn.setText(getResources().getString(R.string.niveldificil));
			setNivel(getResources().getString(R.string.niveldificil));
		}else if(textYourTurn.getText().equals(getResources().getString(R.string.niveldificil))){
			textYourTurn.setText(getResources().getString(R.string.nivelfacil));
			setNivel(getResources().getString(R.string.nivelfacil));
		}else if(textYourTurn.getText().equals(getResources().getString(R.string.nivelfacil))){
			textYourTurn.setText(getResources().getString(R.string.nivelmedio));
			setNivel(getResources().getString(R.string.nivelmedio));
		}		
	}
	
	public void setNivel(String nivel){
		this.nivel= nivel;
	}
	
	public String getNivel(){
		return this.nivel;
	}
	
	public int[] getTimeLevel(){		
		int[] arrayTime = new int[2];		
		if(getNivel().equals(this.getResources().getString(R.string.nivelmedio))){
			arrayTime[0]=SPEED_MEDIUM;
			arrayTime[1]=SPEED_MEDIUM_THREAD;			
		}else if (getNivel().equals(this.getResources().getString(R.string.niveldificil))){
			arrayTime[0]= SPEED_FAST;
			arrayTime[1]= SPEED_FAST_THREAD;
		}else if(getNivel().equals(this.getResources().getString(R.string.nivelfacil))){
			arrayTime[0]= SPEED_SLOW;
			arrayTime[1]= SPEED_SLOW_THREAD;
		}
		return arrayTime;
	}
	
	
	public Animation desvanecerBoton(float desde, float hasta, int duracion){
		AlphaAnimation alfa = new AlphaAnimation(desde, hasta);
		alfa.setDuration(duracion);		
		alfa.setFillAfter(true);
		return alfa;
	}
	
	public Animation animarPieza(){
		AlphaAnimation alfa = new AlphaAnimation(1.0f,0.3f);
		alfa.setDuration(100);
		alfa.setRepeatCount(15);
		alfa.setRepeatMode(Animation.REVERSE);
		return alfa;
	}
	
	public void initViews(){
		gridlay = (GridLayout)this.findViewById(R.id.grid_simon);
		vistainformacion = (ViewStub)this.findViewById(R.id.viewstubinforsecuencias3);
		vistainformacion.inflate();		
				
		botonStart = (Button)this.findViewById(R.id.buttonStartSimon);
		botonStart.setEnabled(false);
		
		botonOkstubview = (Button)this.findViewById(R.id.ok_text_infosimon);
		botonOkstubview.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(soundPrefs.getBoolean("sonido", true))
					soundSimon.play(sonidoOkstub);
				botonStart.setEnabled(true);
				vistainformacion.setVisibility(View.INVISIBLE);
			}
		});
		
		botonNivel = (Button)this.findViewById(R.id.buttonNivelSimon);
		textYourTurn = (TextView)this.findViewById(R.id.textYourTurn);
		textAciertos = (TextView)this.findViewById(R.id.textHighScoreSimon);	
				
		nivel = getResources().getString(R.string.nivelmedio);	
		setNivel(nivel);
		
		textYourTurn.setText(nivel);
		textAciertos.setText(getResources().getString(R.string.maxPuntuacionSimon)+"  "+
				String.valueOf(maximaPunt));
		
	}
		

	class touchPiezas implements View.OnTouchListener{			
		
		ImageView im1,im2;				
						
		@Override
		public boolean onTouch(View view, MotionEvent event) {			
			
			LinearLayout linear= (LinearLayout)view;			
			im1= (ImageView) linear.getChildAt(0);
			im2 = (ImageView) linear.getChildAt(1);			
			
			int action = event.getAction();		
				
			switch(action){
			
			case MotionEvent.ACTION_DOWN :
			
			 conta++; //contador a usar dentro de la clase onTouchListener() para no pulsar mas de una tecla a la vez
			 if(conta==1){ 
			 if(!finPartida){	
			  if(turnoJugador ){									
				 if(linear.getId()==ordenes.get(contJugador)){
					 	if(soundPrefs.getBoolean("sonido", true))
					 		sonido1 = soundSimon.play(sonidoTeclas[ordenes.get(contJugador)]);								
						im1.setVisibility(View.GONE);
						im2.setVisibility(View.VISIBLE);							
					
				 }else{				
						//Terminamos partida			 					
						//simularToque(im1, im2, 500); //simulamos toque
					 	if(soundPrefs.getBoolean("sonido", true))
					 		sonido1=soundSimon.play(sonidoTeclas[9]);
						LinearLayout lin = (LinearLayout)gridlay.getChildAt(ordenes.get(contJugador));
						ImageView im3= (ImageView)lin.getChildAt(1); //mediante un alfa animation indicamos cual hubiese sido la pieza correcta a pulsar
						im3.startAnimation(animarPieza()); 
					 
						turnoJugador=false;
						finPartida=true;
						contJugador=0;		
						
						findepartida();								 
				}
			  }	
			}	
			}							 
			break;
						
			case MotionEvent.ACTION_UP:				
			
			conta--;
			if (conta==0){
			  if(!finPartida ){	
				if(turnoJugador ){					
					soundSimon.stop(sonido1);
					im1.setVisibility(View.VISIBLE);
					im2.setVisibility(View.GONE);										
					contJugador++;	
					if(contJugador==contMaquina){ //turno de la maquina cuando llegamos a las pulsaciones que haya hecho la maquina							
						turnoJugador=false;
						contJugador=0;
						textAciertos.setText(String.valueOf(contMaquina));								
						iniciarMaquina();	
						}	
				}				
			  }			
			}
			break;						
			}			
			return true;		
		}
	}

	

	public void configPantalla(){
		//Pillamos dimensiones pantalla:
		dm = new DisplayMetrics();				
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//Ponemos la pantalla full screen:
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);					
								
		setGaps(NUM_COLUMNAS, NUM_FILAS); // Establecemos los margenes contando publicidad, anchoy y alto de cada celda	
		
	}
	
	public void setGaps (int numC, int 	numF){
		/*Queremos restarle a la altura de la pantalla entera la altura de linear1, pero como linear1 todavia no se ha creado 
		 * completamente en onCreate, onStart y onPause ya que es una view de un layout programada en xml , hay que hacerlo por varios metodos
		 * uno de ellos es el que he puesto pero hay otros. Ver http://www.sherif.mobi/2013/01/how-to-get-widthheight-of-view.html
		 * Poner en google: getheight android returns 0 (ya que la vista si no se ha creado devuelve 0). 	
		 * 
		 * Otra forma: http://stackoverflow.com/questions/10411975/how-to-get-the-width-and-height-of-an-image-view-in-android	  
		 */
		linear1_simon.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);		
		int linear1_height = linear1_simon.getMeasuredHeight();				
		zonaJuegoHight = dm.heightPixels-linear1_height;
		
		//System.out.println("gridlayAltura :" + linear1_height + " dm.heightPixels : "+  dm.heightPixels + "zonaJuegoHight :" + zonaJuegoHight);
		
		final int gapRefer =  dm.widthPixels/62; // = 6dp = 11.61 pixels
		final int sumHorizGapRef =  (3+1)*gapRefer; // Para 3 columnas calculamos 4 huecos y sumamos el total de los 4.
		final int sumVertGapRef = (4+1)*gapRefer;
		
		final int sumAnchoImRef = dm.widthPixels-sumHorizGapRef ; //Espacio total de las fichas juntas restando la suma de los huecos horizontales
		final int sumAltoImRef = zonaJuegoHight - sumVertGapRef; //Idem para filas pero restando la publicidad
			
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
				gapTop = (zonaJuegoHight - (altoIm*numF))/2;
			}
		}
	}
	
		
	
	public void configGrid(){
		parlinear= new ArrayList<LinearLayout>();
		
		lista = new ArrayList<Integer>();		
		for (int i=0; i<NUM_FILAS*NUM_COLUMNAS; ++i){
			lista.add(i);				
		}		
		Collections.shuffle(lista);
				
		for (int i =0; i<NUM_FILAS;++i){
			for (int j=0; j<NUM_COLUMNAS; ++j){				
				linear = new LinearLayout(this);			
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();
				
				ImageView ima1 = new ImageView(this);
				ImageView ima2 = new ImageView(this);
			
				linear.setId(ids);
								
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
				if(j==NUM_COLUMNAS-1){//ultima fila
					if(i==0){
						params.setMargins(gap, gapTop, gap, 0);
					}else if(i>0){
						params.setMargins(gap, gap, gap, 0);	
					}							
				}
				if(j>0 && j!=(NUM_COLUMNAS-1)){//filas intermedias
					if(i==0){
						params.setMargins(gap, gapTop, 0, 0);
					}else if(i>0){
						params.setMargins(gap, gap, 0, 0);	
					}										
				}
					
				linear.setLayoutParams(params);									
				
				LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(anchoIm,altoIm);	
			//	LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
							
				
				ima1.setLayoutParams(params2);										
				ima1.setImageResource(Imagenes.imagenesSimon[linear.getId()][1]);							
				ima1.setVisibility(View.VISIBLE);					
				linear.addView(ima1,0);	
				
					
				ima2.setLayoutParams(params2);		
				ima2.setImageResource(Imagenes.imagenesSimon[linear.getId()][0]);					
				ima2.setVisibility(View.GONE);				
				linear.addView(ima2, 1);										
				
				linear.setOnTouchListener(new touchPiezas()); 					
				gridlay.addView(linear);				
				++ids;									
			}				
		}		
				
	}
	
	class MarcadoresSimon implements Runnable{
		
		TextView puntos;
		ImageView logosuperado;
		Button botonOk, botonCompartir,botonScores;
		LinearLayout linearsimon;

		@Override
		public void run() {		
			mostrarMarc();
		}
		
		
		public void mostrarMarc(){
			
			//Creamos una view tipo LinearLayout que sera agregada al RelativeLayout para que quede por encima
			LinearLayout panta = new LinearLayout(contexto){				
				LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view =li.inflate(R.layout.simon_final, this, true);				
			};
					
			RelativeLayout llay = (RelativeLayout)((Activity) contexto).findViewById(R.id.simon_layout);	
			RelativeLayout.LayoutParams relatparams = new RelativeLayout.LayoutParams
				(LayoutParams.MATCH_PARENT, android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
		
			relatparams.setMargins(dm.widthPixels/12,dm.heightPixels/7,
				dm.widthPixels/12, dm.heightPixels/8);
			panta.setLayoutParams(relatparams);
			llay.addView(panta);		
			
			linearsimon = (LinearLayout)panta.findViewById(R.id.linearmarcadorsimon);
			puntos = (TextView)panta.findViewById(R.id.simonpuntuacion);
			logosuperado = (ImageView)panta.findViewById(R.id.imagen_logosuperado);
			botonOk = (Button)panta.findViewById(R.id.simon_botonok);
			botonCompartir = (Button)panta.findViewById(R.id.simon_compartir);
			botonScores = (Button)panta.findViewById(R.id.global_scores);
			
			RotateAnimation rotarAnimation = new RotateAnimation(360, 0, // de 0 a 360 grados
	                Animation.RELATIVE_TO_SELF, 0.5f,  //pivote valor del filo izquierdo (valores entre 0 y 1 floats)
	                Animation.RELATIVE_TO_SELF, 0.5f); //pivote valor del filo superior
			rotarAnimation.setDuration(50);
			logosuperado.startAnimation(rotarAnimation);	
			
			//Ponemos un alphaanimation para que aparezcabn los marcadores.			
			AlphaAnimation alphaanim = new AlphaAnimation(0.5f,1);
			alphaanim.setDuration(1500);
			panta.startAnimation(alphaanim);	
			
			new Timer().schedule(new TimerTask(){
				@Override
				public void run() {
					handler.post(new Runnable(){
						@Override
						public void run() {
							puntos.setVisibility(View.VISIBLE);
							puntos.setText(String.valueOf(contMaquina-1));			
							botonOk.setVisibility(View.VISIBLE);
							botonCompartir.setVisibility(View.VISIBLE);
							/*
							if(isSignedIn()) //solo se ve si estamos conectados				
								botonScores.setVisibility(View.VISIBLE);
							*/
						}						
					});									
				}
				
			}, 100);
			
			botonOk.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(soundPrefs.getBoolean("sonido", true))
						soundSimon.play(sonidoNivel);
					linearsimon.setVisibility(View.INVISIBLE);			
				}
			});
			
			botonCompartir.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(soundPrefs.getBoolean("sonido", true))
						soundSimon.play(sonidoNivel);
					Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
					sharingIntent.setType("text/plain"); //Pasaremos solo 
					
					String shareBody =
							getResources().getString(R.string.sharecontent_puntos1)+" "+
							getResources().getString(R.string.sharecontent_puntos2)+" "+getResources().getString(R.string.modosimon)+	
							getResources().getString(R.string.sharecontent_puntos4)+" "+String.valueOf(contMaquina-1)+
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
					if(soundPrefs.getBoolean("sonido", true))
						soundSimon.play(sonidoNivel);
					startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient()
							,getResources().getString(R.string.leaderboard_simon))
							,500);						
				}
			});
			*/
		}
		
	}

	//Liberamos todos los sonidos de la memoria.
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		soundSimon.unloadAll();
	}


}
