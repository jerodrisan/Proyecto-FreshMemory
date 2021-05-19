	package com.jesusmanuelrodriguez.freshmemory;


//new activitymodos
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
*/

public class ActivityModos extends Activity{ //Ver diferencia entre FragmentActivity y Activity: http://stackoverflow.com/questions/10609268/difference-between-fragment-and-fragmentactivity
	
	Context contexto;
		
	int indexTotal =1;	
	int indexTotalRelaciones=1 ;
	int indexTotalRetoColores=1;
	int indexTotalSecuencia=1;
	int indexTotalSecuencia2=1;
	int cambioScore=1;
	
	//MediaPlayer sonidoAnimator;
	
	TextView infoTextView, textModoScores,infoTextViewTitle;
	ImageView imagePreview, flechaderecha, flechaizquierda;
	RelativeLayout relatlayout;	
	LinearLayout contenedor;
	ViewStub marcadores;
	SharedPreferences timeattackScores, classicScores, relacionesScores, retocoloresScores,
	numerosScores, seriesScores, simonScores ;
	Button botonclasicorr;
	LinearLayout linear1, linear2, linear3, linear4, linear5, linear6;
	Handler handler;
	Timer timer;
	int anima=0;
	AlphaAnimation animInicial;	
	TranslateAnimation animBoton2;
	int MAX_VOLUME = 100; //volumen m�ximo de referencia
	Bundle extras;
	String modoJuego;
	//AdView adView;
	SharedPreferences soundPrefs;
	SharedPreferences.Editor editor;
	public static final String SONIDO = "sonido";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	
		setVolumeControlStream(AudioManager.STREAM_MUSIC);	
		contexto = this;		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );	
		
		 //Preferencias para el sonido. Cuando creamos la actividad , por defecto el sonido estar� activado y asi lo guardaremos 
		soundPrefs = this.getSharedPreferences(SONIDO, 0);
		editor = soundPrefs.edit();
		editor.putBoolean("sonido", soundPrefs.getBoolean("sonido", true)); 
		editor.commit();
				    
		handler = new Handler();	
		//En caso de pulsar en la activity anterior el boton de parejas o el boton de secuencias pondremos uno u otro vista para esta actividad
		extras = getIntent().getExtras();
		modoJuego = extras.getString("modoJuego");
		if(modoJuego.equals("parejas")){
			setContentView(R.layout.activity_modos);
			/*
			adView = (AdView)this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
		    adView.loadAd(adRequest);
		    */
			setupViewModoParejas();
			getPreferenciasParejas();
			animarBotonesParejas();
			animacionInformativa();
			
		}else if(modoJuego.equals("secuencias")){
			setContentView(R.layout.activity_modos2);
			/*
			adView = (AdView)this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
		    adView.loadAd(adRequest);
		    */
			setupViewModoSecuencias();
			getPreferenciasSecuencias();
			animarBotonesSecuencias();
			animacionInformativaSecuencias();
		}
		//Pillamos las preferencias tanto de modo parejas como de Secuencias:
		//getPreferencias();
			
		//Obtencion de views a traves del stubView			
		marcadores = (ViewStub)this.findViewById(R.id.viewstubHighScores);		
		marcadores.inflate();		
		marcadores.setVisibility(View.INVISIBLE);						
		textModoScores = (TextView)findViewById(R.id.timeattack_moderrr);				
		flechaderecha =(ImageView)this.findViewById(R.id.boton_derecha_scores);
		flechaizquierda = (ImageView)this.findViewById(R.id.boton_izquierda_scores);
	
	
		
		
	}// fin onCreate()
	
	public  void setupViewModoParejas(){
		
		relatlayout = (RelativeLayout)this.findViewById(R.id.relativelayout_modos);		
		contenedor = (LinearLayout)findViewById(R.id.linearmodosseleccion);
		
		infoTextView = (TextView)this.findViewById(R.id.textviewinformation);
		infoTextViewTitle= (TextView)this.findViewById(R.id.textviewinformationtitle);
		
		imagePreview = (ImageView)this.findViewById(R.id.imageninfor1);		
		
		linear1 = (LinearLayout)this.findViewById(R.id.linearmodos1);
		linear2 = (LinearLayout)this.findViewById(R.id.linearmodos2);
		linear3 = (LinearLayout)this.findViewById(R.id.linearmodos3);
		linear4 = (LinearLayout)this.findViewById(R.id.linearmodos4);	
		linear5 = (LinearLayout)this.findViewById(R.id.linearmodos5);	
	}
	
	public void setupViewModoSecuencias(){
		
		relatlayout = (RelativeLayout)this.findViewById(R.id.relativelayout_modos2);
		contenedor = (LinearLayout)findViewById(R.id.linearmodosseleccion_2);
		
		infoTextView = (TextView)this.findViewById(R.id.textviewinformationsecuencias);
		infoTextViewTitle= (TextView)this.findViewById(R.id.textviewinformationtitlesecuencias);
		
		imagePreview = (ImageView)this.findViewById(R.id.imageninfor1secuencias);
		
		linear1 = (LinearLayout)this.findViewById(R.id.linearsecuencia1);
		linear2 = (LinearLayout)this.findViewById(R.id.linearsecuencia2);
		linear3 = (LinearLayout)this.findViewById(R.id.linearsecuencia3);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {	
				
				String modo = data.getStringExtra("tipoModo");
				
				if(modo.equals("normal")){
					indexTotal =data.getIntExtra("indexTotal", 1);	
					setMaxValue(indexTotal, "indexTotal",timeattackScores);
				}								
				else if(modo.equals("relaciones")){
					indexTotalRelaciones =data.getIntExtra("indexTotalRelaciones", 1);	
					setMaxValue(indexTotalRelaciones,"indexTotalRelaciones",timeattackScores);
				}					
				else if(modo.equals("retocolores")){
					indexTotalRetoColores = data.getIntExtra("indexTotalRetoColores", 1);
					setMaxValue(indexTotalRetoColores,"indexTotalRetoColores",timeattackScores);
				}				
								
			}
		}
		
	}
		
	
	public void getPreferenciasParejas(){
		timeattackScores = getSharedPreferences(ActivityTimeAttack.GAME_PREFS, 0);	
		classicScores = getSharedPreferences(ActivityBoard.HighScoresLevelClassic, 0);	
		relacionesScores =	 getSharedPreferences(ActivityBoard.HighScoresLevelRelaciones, 0);		
		
	}
	
	

	public void getPreferenciasSecuencias(){
		
		numerosScores = getSharedPreferences(ActivitySecuencias.GAME_PREFS, 0);
		seriesScores = getSharedPreferences(ActivitySecuencias2.GAME_PREFS,0);
		simonScores = getSharedPreferences(ActivitySimon.GAME_PREFS, 0);	
		
		//Para modo ActivitySecuencias (modo numeros):
		indexTotalSecuencia= getIntent().getIntExtra("indexTotalSecuencia", indexTotalSecuencia);		
		setMaxValue(indexTotalSecuencia,"indexTotalSecuencia",numerosScores);	
		
		//Para modo ActivitySecuencias2 (modo series):
		indexTotalSecuencia2= getIntent().getIntExtra("indexTotalSecuencia2", indexTotalSecuencia2);		
		setMaxValue(indexTotalSecuencia2,"indexTotalSecuencia2",seriesScores);
		
	}
	
	public void animarBotonesParejas(){
		//Animacion de los linearlayout en el comienzo:
		animacionBotones(linear1, 1.0f, 0, 0,0);
		animacionBotones(linear2, -1.0f, 0, 0, 0);
		animacionBotones(linear3, 1.0f, 0, 0, 0);
		animacionBotones(linear4, -1.0f, 0, 0,0);
		animacionBotones(linear5, 1.0f, 0, 0,0);
	}
	
	public void animarBotonesSecuencias(){
		//Animacion de los linearlayout en el comienzo:
		animacionBotones(linear1, 1.0f, 0, 0,0);
		animacionBotones(linear2, -1.0f, 0, 0, 0);
		animacionBotones(linear3, 1.0f, 0, 0, 0);				
	}
	
	
	
	public float setCustomVolumen(int soundVolume){  //volumen que queremos poner (la mitad del total de volumen) o  lo que queramos!!!
		return (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME))); // ver:http://stackoverflow.com/questions/5215459/android-mediaplayer-setvolume-function
	}
		
	@Override //ver por que se ponen los mediaplayer en este caso en onResume(): http://www.htcmania.com/showthread.php?t=778281
	protected void onResume() { //Ver: http://stackoverflow.com/questions/10410096/android-how-to-use-oncreate-when-pressing-backbutton
		super.onResume();
		//adView.resume();
		/*	
		sonidoAnimator = MediaPlayer.create(this, R.raw.button54);			
		sonidoAnimator.setVolume(setCustomVolumen(70), setCustomVolumen(70));				
		*/		
		if(modoJuego.equals("parejas")){
			animacionBotones(linear1, 1.0f, 0, 0,0);
			animacionBotones(linear2, -1.0f, 0, 0, 0);
			animacionBotones(linear3, 1.0f, 0, 0, 0);
			animacionBotones(linear4, -1.0f, 0, 0,0);	
			animacionBotones(linear5, 1.0f, 0, 0,0);
		}else if(modoJuego.equals("secuencias")){
			animacionBotones(linear1, 1.0f, 0, 0,0);	
			animacionBotones(linear2, -1.0f, 0, 0,0);
			animacionBotones(linear3, 1.0f, 0, 0, 0);	
		}
	}

	@Override
	protected void onPause() {		
		super.onPause();		
		//adView.pause();
		/*
		new Timer().schedule(new TimerTask(){		
			@Override
			public void run() {					
				sonidoAnimator.release();
			}			
		}, sonidoAnimator.getDuration());
		*/
	} 
	
	@Override
	protected void onDestroy() {	
		super.onDestroy();
		//adView.destroy();
		//sonidoAnimator.release();
		
	}
	

	public void animacionBotones(View view, float fromX, float toX, float fromY, float toY){
		animBoton2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, fromX, Animation.RELATIVE_TO_PARENT, toX,
				 Animation.RELATIVE_TO_PARENT, fromY, Animation.RELATIVE_TO_PARENT, toY);
		animBoton2.setDuration(700);		
		view.startAnimation(animBoton2);	
		new Timer().schedule(new TimerTask(){ //O bien lo hacemos con un animBoton2.setAnimationListener(new Animation.AnimationListener()
			@Override
			public void run() {
				
				if(soundPrefs.getBoolean("sonido", true))
					//sonidoAnimator.start();
					ActivityModosSeleccion.soundClick1.start();
						
			}			
		}, 700); 
	}
	
	//Ponemos el ultimo nivel
	public void setMaxValue (int nivel, String KEYString, SharedPreferences sharedPref){
		
		SharedPreferences.Editor editor = sharedPref.edit();
		int levell = sharedPref.getInt(KEYString, 0); // donde  es el valor devuelto si no existe la preferencia
		
		if(nivel >levell){
			editor.putInt(KEYString, nivel);
			editor.commit();
		}			
	}
	
	//Lo recogemos 
	public int getValue(String KEYString, SharedPreferences sharedPref){	
		return sharedPref.getInt(KEYString, 1);		
	}
	
	public void amimarInfo(Animation anim, int titulo, int textoinfo, int img ){
		relatlayout.startAnimation(anim);		
		infoTextViewTitle.setText(titulo);
		infoTextView.setText(textoinfo);
		imagePreview.setImageResource(img);
	}
	
	public void animacionInformativa(){		
	
		timer = new Timer();
		animInicial= new AlphaAnimation(0.5f,1);
		animInicial.setDuration(3000);			
		animInicial.setRepeatCount(1);		
		animInicial.setRepeatMode(Animation.REVERSE);
		
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run() {
							
						if(anima==0){							
							amimarInfo(animInicial,R.string.botonmodoclasico,
									R.string.textviewinfor1,Imagenes.previews[0]);	
							anima=1;							
						}								
							
						else if(anima==1){
							amimarInfo(animInicial,R.string.botonmodorelaciones,
									R.string.textviewinfor3,Imagenes.previews[2]);					
							anima=2;							
							}
						
						else if (anima==2){
							amimarInfo(animInicial,R.string.botonmodotimeatack,
									R.string.textviewinfor2,Imagenes.previews[1]);	
								anima=3;
							}
						else if (anima==3){
							amimarInfo(animInicial,R.string.botonmodotimeatack2,
									R.string.textviewinfor4,Imagenes.previews[3]);	
							anima=4;
						}else if(anima==4){
							amimarInfo(animInicial,R.string.botonretocolores,
									R.string.textviewinfor5,Imagenes.previews[7]);	
							anima=0;
						}
						}						
					});
				}				
			}, 0, 6000);
	}
	
	public void animacionInformativaSecuencias(){
		timer = new Timer();
		animInicial= new AlphaAnimation(0.5f,1);
		animInicial.setDuration(3000);			
		animInicial.setRepeatCount(1);		
		animInicial.setRepeatMode(Animation.REVERSE);
		
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				handler.post(new Runnable(){
					@Override
					public void run() {
							
						if(anima==0){							
							amimarInfo(animInicial,R.string.titulonumeros,
									R.string.textviewinfornumeros,Imagenes.previews[4]);	
							anima=1;							
						}								
							
						else if(anima==1){
							amimarInfo(animInicial,R.string.tituloseries,
									R.string.textviewinforseries,Imagenes.previews[5]);					
							anima=2;							
							}
						
						else if (anima==2){
							amimarInfo(animInicial,R.string.modosimon,
									R.string.textviewinforsimon,Imagenes.previews[6]);	
								anima=0;
							}										
						}						
					});
				}				
			}, 0, 6000);
		
	}
	
	
	//Metodo general para mostrar marcadores de cualquier modo: 	
	
	public void mostrarMarcadores(String[] savedScores){
		
		marcadores.setVisibility(View.VISIBLE);		
		TextView scoreView = (TextView)findViewById(R.id.timeattack_high_scores_list);			
		StringBuilder scoreBuild = new StringBuilder("");
		//Si queremos poner las posiciones usamos el indice i
		for(int i=0; i<savedScores.length; ++i){
			//scoreBuild.append(String.valueOf(i+1)+"     -     ");
			scoreBuild.append(savedScores[i]+"\n");
		}			
		/* O bien en caso de no poner posiciones:
		for(String score : savedScores){
				scoreBuild.append(score+"\n");
		}
		*/	
		scoreView.setMovementMethod(new ScrollingMovementMethod());
		scoreView.setText(scoreBuild.toString());			
	}
	
	
	public void botonOKhighScores(View v){
		marcadores.setVisibility(View.INVISIBLE);
		//habilitamos de nuevo las views para que se puedan clickear
		activateAndDeactivateViews(true);	
	}
	
	
	//---------------------------------------------------------
	//BOTON TIMEATTACK 1, IR A MODO INFORMACION Y MOSTRAR MARCADORES
	public void botonTimeAttack (View v){
				
		Intent i = new Intent(contexto, ActivityTimeAttack.class);
		
		if(soundPrefs.getBoolean("sonido", true))
			//sonidoAnimator.start();
			ActivityModosSeleccion.soundClick1.start();
		
		i.putExtra("tipoModo", "normal");	
		i.putExtra("indexTotal", getValue("indexTotal",timeattackScores));		
		
		startActivityForResult(i, 1);	
	}	
	

	public void informationTimeAttack ( View v){	
		
		timer.cancel();				
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);				
		amimarInfo(anim,R.string.botonmodotimeatack,R.string.textviewinfor2,Imagenes.previews[1]);
		
	}		
	
	public void botonMarcadoresTimeattack (View v){		
		//Desabilitamos botones que hay debajo para que no se puedan clickear
		activateAndDeactivateViews(false);	
		flechaderecha.setVisibility(View.GONE);
		flechaizquierda.setVisibility(View.GONE);
		
		String[] savedScores = timeattackScores.getString("highScores", "").split("\\|");
		mostrarMarcadores(savedScores);
		textModoScores.setText(getResources().getString(R.string.botonmodotimeatack));
	}
		
	//----------------------------------
	//BOTON TIMEATTACK RELACIONES
	public void botonTimeAttackrelaciones (View v){
		Intent i = new Intent(contexto, ActivityTimeAttack.class);	
		
		if(soundPrefs.getBoolean("sonido", true))
			//sonidoAnimator.start();
			ActivityModosSeleccion.soundClick1.start();
		
		i.putExtra("tipoModo", "relaciones");		
		i.putExtra("indexTotalRelaciones", getValue("indexTotalRelaciones",timeattackScores));
		
		startActivityForResult(i, 1);	
	}	
	
	public void informationTimeAttack2 ( View v){	
		
		timer.cancel();	
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);				
		amimarInfo(anim,R.string.botonmodotimeatack2,R.string.textviewinfor4,Imagenes.previews[3]);
		
	}		
	
	public void botonMarcadoresTimeattack2 (View v){		
		//Desabilitamos botones que hay debajo para que no se puedan clickear
		activateAndDeactivateViews(false);	
		flechaderecha.setVisibility(View.GONE);
		flechaizquierda.setVisibility(View.GONE);
		
		String[] savedScores = timeattackScores.getString("highScores2", "").split("\\|");
		mostrarMarcadores(savedScores);
		textModoScores.setText(getResources().getString(R.string.botonmodotimeatack2));
	}
	//-------------------------------------
	//BOTO MODO RETO COLORES: IR A MODO, INFORMACION Y MOSTRAR MARCADORES
	
	public void botonRetoColores (View v){
		Intent i = new Intent(contexto, ActivityTimeAttack.class);
		
		if(soundPrefs.getBoolean("sonido", true))
			//sonidoAnimator.start();
			ActivityModosSeleccion.soundClick1.start();
		
		i.putExtra("tipoModo", "retocolores");		
		i.putExtra("indexTotalRetoColores", getValue("indexTotalRetoColores",timeattackScores));
	
		startActivityForResult(i, 1);	
	}
	
	public void informationRetocolores (View v){
		
		timer.cancel();	
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);				
		amimarInfo(anim,R.string.botonretocolores ,R.string.textviewinfor5 ,Imagenes.previews[7]);
		
	}
	
	public void botonMarcadoresRetocolores (View v){
		//Desabilitamos botones que hay debajo para que no se puedan clickear
		activateAndDeactivateViews(false);	
		flechaderecha.setVisibility(View.GONE);
		flechaizquierda.setVisibility(View.GONE);
				
		String[] savedScores = timeattackScores.getString("highScores3", "").split("\\|");
		mostrarMarcadores(savedScores);
		textModoScores.setText(getResources().getString(R.string.botonretocolores));
	}
	
	//-----------------------------------
	//BOTON MODO CLASICO: IR A MODO , INFORMACION Y MOSTRAR MARCADORS	
	public void botonModoClasico (View v){
		Intent i =  new Intent (contexto, ActivityClassic.class);	
		
		if(soundPrefs.getBoolean("sonido", true))
			//sonidoAnimator.start();
			ActivityModosSeleccion.soundClick1.start();
		
		startActivity(i);		
	}	
	public void informacionModoClasico (View v){	
		
		timer.cancel();	
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);			
		amimarInfo(anim,R.string.botonmodoclasico,R.string.textviewinfor1,Imagenes.previews[0]);
	
	}	
	public void botonMarcadoresClasico (View v){
		//Desabilitamos botones que hay debajo para que no se puedan clickear
		activateAndDeactivateViews(false);	
			
		flechaderecha.setVisibility(View.VISIBLE);
		flechaizquierda.setVisibility(View.VISIBLE);	
		
		//Exponemos por defecto el primer nivel. Luego cuando se pulsen las flechas, se irann mostrando los demas niveles. 
		String[] savedScores = classicScores.getString("nivel1", "").split("\\|"); 
		mostrarMarcadores(savedScores);		
		//En caso de pulsar teclas derecha o izquierda vamos mostrando los marcadores de los distintos niveles:
		textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+	getResources().getString(R.string.clasicofacil));
		
		//si pulsamos el boton de la flecha derecha: 
		flechaderecha.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String[] savedScores;
				if(cambioScore==1){
					textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+getResources().getString(R.string.clasicomedio));
					savedScores= classicScores.getString("nivel2", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=2;
				}else if(cambioScore ==2){
					textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+getResources().getString(R.string.clasicodificil));
					savedScores= classicScores.getString("nivel3", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=3;
				}else if(cambioScore==3){
					textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+getResources().getString(R.string.clasicomaximo));
					savedScores= classicScores.getString("nivel4", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=4;
				}else{
					textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+getResources().getString(R.string.clasicofacil));
					savedScores= classicScores.getString("nivel1", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=1;
				}
			}
		});
		
		//si pulsamos el boton de la flecha izquierda: 
		flechaizquierda.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String[] savedScores;
				if(cambioScore==1){
					textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+getResources().getString(R.string.clasicomaximo));
					savedScores= classicScores.getString("nivel4", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=4;
				}else if(cambioScore ==4){
					textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+getResources().getString(R.string.clasicodificil));
					savedScores= classicScores.getString("nivel3", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=3;
				}else if(cambioScore==3){
					textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+getResources().getString(R.string.clasicomedio));
					savedScores= classicScores.getString("nivel2", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=2;
				}else{
					textModoScores.setText(getResources().getString(R.string.botonmodoclasico)+" "+getResources().getString(R.string.clasicofacil));
					savedScores= classicScores.getString("nivel1", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=1;
				}
				
			}
		});
		
	}
	
	//-------------------------------------------------------------------
	//BOTON MODO RELACIONES: IR A MODO, INFORMACION Y MOSTRAR MARCADORES	
	public void botonModoRelaciones (View v){
		Intent i = new Intent(contexto, ActivityRelaciones.class);	
		
		if(soundPrefs.getBoolean("sonido", true))
			//sonidoAnimator.start();
			ActivityModosSeleccion.soundClick1.start();
		startActivity(i);
	}	
	public void informationRelaciones (View v){		
		
		timer.cancel();	
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);	
		amimarInfo(anim,R.string.botonmodorelaciones,R.string.textviewinfor3,Imagenes.previews[2]);	
	}	
	
	public void botonMarcadoresRelaciones(View v){
		//Desabilitamos botones que hay debajo para que no se puedan clickear
		activateAndDeactivateViews(false);	
					
		flechaderecha.setVisibility(View.VISIBLE);
		flechaizquierda.setVisibility(View.VISIBLE);	
				
		//Exponemos por defecto el primer nivel. Luego cuando se pulsen las flechas, se irann mostrando los demas niveles. 
		String[] savedScores = relacionesScores.getString("nivel1", "").split("\\|"); 
		mostrarMarcadores(savedScores);		
		//En caso de pulsar teclas derecha o izquierda vamos mostrando los marcadores de los distintos niveles:
		textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesfacil));
				
		//si pulsamos el boton de la flecha derecha: 
		flechaderecha.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String[] savedScores;
				if(cambioScore==1){
					textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesmedio));
					savedScores= relacionesScores.getString("nivel2", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=2;
				}else if(cambioScore ==2){
					textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesdificil));
					savedScores= relacionesScores.getString("nivel3", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=3;
				}else if(cambioScore==3){
					textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesmaximo));
					savedScores= relacionesScores.getString("nivel4", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=4;
				}else{
					textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesfacil));
					savedScores= relacionesScores.getString("nivel1", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=1;
				}
			}
		});
				
		//si pulsamos el boton de la flecha izquierda: 
		flechaizquierda.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String[] savedScores;
				if(cambioScore==1){
					textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesmaximo));
					savedScores= relacionesScores.getString("nivel4", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=4;
				}else if(cambioScore ==4){
					textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesdificil));
					savedScores= relacionesScores.getString("nivel3", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=3;
				}else if(cambioScore==3){
					textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesmedio));
					savedScores= relacionesScores.getString("nivel2", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=2;
				}else{
					textModoScores.setText(getResources().getString(R.string.botonmodorelaciones)+" "+getResources().getString(R.string.relacionesfacil));
					savedScores= relacionesScores.getString("nivel1", "").split("\\|"); 
					mostrarMarcadores(savedScores);	
					cambioScore=1;
				}
			}
		});
	}
	
	//------------------------------------------------
	////BOTON MODO NUMEROS: IR A MODO, INFORMACION Y MOSTRAR MARCADORES	
	
	public void botonNumeros (View v){
		Intent i = new Intent(contexto, ActivitySecuencias.class);	
		
		if(soundPrefs.getBoolean("sonido", true))
			//sonidoAnimator.start();
			ActivityModosSeleccion.soundClick1.start();
		
		i.putExtra("nivel", 1);
		i.putExtra("indexTotalSecuencia", getValue("indexTotalSecuencia",numerosScores));
		i.putExtra("indexTotalAux", 1);	
		i.putExtra("tiempoTotal", 20);
		i.putExtra("vistaInicio", View.VISIBLE);
		i.putExtra("vistaspinner", View.VISIBLE);
		i.putExtra("level", "NIVEL 1	(2x2)");	
		startActivity(i);	
		
	}
	
	public void informationNumeros (View v){
		timer.cancel();	
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);				
		amimarInfo(anim,R.string.titulonumeros,R.string.textviewinfornumeros,Imagenes.previews[4]);
		
	}
	
	public void botonMarcadoresNumeros (View v){
		
		//Desabilitamos botones que hay debajo para que no se puedan clickear
		activateAndDeactivateViews(false);	
		flechaderecha.setVisibility(View.GONE);
		flechaizquierda.setVisibility(View.GONE);
			
		String[] savedScores = numerosScores.getString("highScoresSecuencias", "").split("\\|");
		mostrarMarcadores(savedScores);
		textModoScores.setText(getResources().getString(R.string.titulonumeros));
	}
	
	//--------------------------
	//MODO SERIES IR A MODO, INFORMACION Y MOSTRAR MARCADORES
	
	public void botonModoSeries (View v){
		Intent i = new Intent(contexto, ActivitySecuencias2.class);	
		
		if(soundPrefs.getBoolean("sonido", true))
			//sonidoAnimator.start();
			ActivityModosSeleccion.soundClick1.start();
		i.putExtra("nivel", 1);
		i.putExtra("indexTotalSecuencia2", getValue("indexTotalSecuencia2",seriesScores));
		i.putExtra("indexTotalAux", 1);			
		i.putExtra("vistaInicio", View.VISIBLE);
		i.putExtra("vistaspinner", View.VISIBLE);
		i.putExtra("level", "NIVEL 1	(2x2)");	
		startActivity(i);	
		
	}
	
	public void informacionModoSeries (View v){
		timer.cancel();	
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);				
		amimarInfo(anim,R.string.tituloseries,R.string.textviewinforseries,Imagenes.previews[5]);
		
	}
	
	public void botonMarcadoresSeries (View v){
		
			//Desabilitamos botones que hay debajo para que no se puedan clickear
			activateAndDeactivateViews(false);	
			flechaderecha.setVisibility(View.GONE);
			flechaizquierda.setVisibility(View.GONE);
			
			String[] savedScores = seriesScores.getString("highScoresSecuencias", "").split("\\|");
			mostrarMarcadores(savedScores);
			textModoScores.setText(getResources().getString(R.string.botonmodoSecuencias));
	}
	
	
	//------------------------------------------------
	// BOTON MODO SIMON: IR A MODO, INFORMACION Y MOSTRAR MARCADORES	
	
	public void botonModoSimon (View v){
		
		Intent i = new Intent(contexto, ActivitySimon.class);	
		
		if(soundPrefs.getBoolean("sonido", true))
			//sonidoAnimator.start();
			ActivityModosSeleccion.soundClick1.start();
		startActivity(i);
	}
	
	
	
	public void informationSimon (View v){
		timer.cancel();	
		AlphaAnimation anim = new AlphaAnimation(0,1);
		anim.setDuration(1000);				
		amimarInfo(anim,R.string.modosimon,R.string.textviewinforsimon,Imagenes.previews[6]);
		
	}
	
	public void botonMarcadoresSimon (View v){
		//Desabilitamos botones que hay debajo para que no se puedan clickear
		activateAndDeactivateViews(false);	
		flechaderecha.setVisibility(View.GONE);
		flechaizquierda.setVisibility(View.GONE);
		
		String[] savedScores = simonScores.getString("maximapuntuacionmodos", "").split("\\|");
		mostrarMarcadores(savedScores);
		textModoScores.setText(getResources().getString(R.string.modosimon));
		
	}
	
	
	//---------------------
	//Desactivamos los botones que hay debajo 
	public void activateAndDeactivateViews(boolean setHabilitar){	
		
		for (int i=0; i<contenedor.getChildCount();++i){
			//si las vistas hijas son LinearLayout 
				if (contenedor.getChildAt(i).getClass() == LinearLayout.class){
					//recorremos las vistas hijas Linearlayout para diferenciar entre botones e ImageVies 
					LinearLayout ll = (LinearLayout) contenedor.getChildAt(i);
					for (int j=0; j<ll.getChildCount();++j){
						if(ll.getChildAt(j).getClass() == Button.class){
							Button boton = (Button) ll.getChildAt(j); //por ultimo los desabilitamos
							boton.setEnabled(setHabilitar);
						}else{
							ImageView im = (ImageView)ll.getChildAt(j);
							im.setEnabled(setHabilitar);
						}
					}
				}
		}
	}
	
		
	//--------MENU OPCIONES , RESETEAR MARCADORES Y ABOUT
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflar = getMenuInflater();		
		inflar.inflate(R.menu.memorion_menu, menu);  	
		MenuItem item1 =menu.findItem(R.id.resetmarcadoresparejas);
		MenuItem item2 =menu.findItem(R.id.resetmarcadoressecuencias);
		if(modoJuego.equals("parejas")){
			item1.setVisible(true);			
		}else if(modoJuego.equals("secuencias")){
			item2.setVisible(true);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		// Checkbox de item sonido:
		View checkBoxView = View.inflate(this, R.layout.checkbox_menu, null);
		final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkboxmenu);		
		
		int itemId = item.getItemId();
		int aux=0;
		if (modoJuego.equals("parejas")){
			aux= R.id.resetmarcadoresparejas;
		}else if (modoJuego.equals("secuencias")){
			aux = R.id.resetmarcadoressecuencias;
		}		
		
		if (itemId == R.id.aboutus) {
			//Codigo para aboutUs	
			Intent i = new Intent(contexto, AboutUs.class);
			contexto.startActivity(i);
			
		} else if (itemId ==aux) {
			AlertDialog.Builder alerta = new AlertDialog.Builder(this);
			alerta.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {		
					if(modoJuego.equals("parejas")){	
						deleteParejas();						
					}else if(modoJuego.equals("secuencias")){					
						deleteSecuencias();
					}
				}
			});
			
			alerta.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			if(modoJuego.equals("parejas")){
				alerta.setMessage(R.string.seborraranlosrecordsparejas);
			}else if(modoJuego.equals("secuencias")){
				alerta.setMessage(R.string.seborraranlosrecordssecuencias);
			}
			alerta.setIcon(R.drawable.boton_resetmarcadores);
			alerta.setTitle(R.string.borrarmarcadores);
			AlertDialog alert = alerta.create();
			alert.show();
		}else if(itemId==R.id.menu_sonido){
			
			checkBox.setText(R.string.activarsonido);							
			checkBox.setChecked(soundPrefs.getBoolean("sonido", true)); //pillamos el valor que hemos pulsado la ultima vez.
			checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(buttonView.isChecked()){
						editor.putBoolean("sonido", true);	
						
					}else {
						editor.putBoolean("sonido", false);		
						
					}						
				}
			});		
			AlertDialog.Builder alertasonido = new AlertDialog.Builder(this);
			alertasonido.setTitle(R.string.sonido);
			//alertasonido.setMessage("Sonido");
			alertasonido.setIcon(R.drawable.boton_sonido);
			alertasonido.setView(checkBoxView);
			alertasonido.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {
					editor.commit();
					System.out.println("pulsado ? "+ String.valueOf(soundPrefs.getBoolean("sonido",true)));
				}
			});
			alertasonido.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {					
				@Override
				public void onClick(DialogInterface dialog, int which) {						
					dialog.cancel();
					System.out.println("pulsado ? "+ String.valueOf(soundPrefs.getBoolean("sonido",true)));
				}
			});
			AlertDialog alert = alertasonido.create();
			alert.show();
		}
		return false;
	}
	
	public void deleteParejas(){
		SharedPreferences.Editor deletetimeattack, deleteClassic, deleteRelaciones;
					
		deletetimeattack = timeattackScores.edit();
		deleteClassic = classicScores.edit();					
		deleteRelaciones = relacionesScores.edit();
		
		// si queremos borrar solo una parte: deletetimeattack.remove("highScores");
		//para borrar todo:
		deletetimeattack.clear();
		deleteClassic.clear(); //en este caso eliminamostodos los valores
		deleteRelaciones.clear();		
		
		deletetimeattack.commit();	
		deleteClassic.commit();
		deleteRelaciones.commit();
		
		
		setMaxValue(1,"indexTotal",timeattackScores);
		setMaxValue(1,"indexTotalRelaciones",timeattackScores);
		setMaxValue(1,"indexTotalRetoColores",timeattackScores);
		
	}
	
	public void deleteSecuencias(){
		SharedPreferences.Editor deleteNumeros ,deleteSimon, deleteSeries;		
		
		deleteNumeros = numerosScores.edit();
		deleteSimon = simonScores.edit();
		deleteSeries = seriesScores.edit();
		
		deleteNumeros.clear();
		deleteSimon.clear();
		deleteSeries.clear();
		
		deleteNumeros.commit();
		deleteSimon.commit();
		deleteSeries.commit();
		setMaxValue(1, "indexTotalSecuencia",numerosScores );
		setMaxValue(1, "indexTotalSecuencia2",seriesScores );		
		
	}
	
}
