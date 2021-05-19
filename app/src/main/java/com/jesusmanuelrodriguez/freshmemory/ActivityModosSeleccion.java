package com.jesusmanuelrodriguez.freshmemory;




import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
*/
public class ActivityModosSeleccion extends Activity {
	
	Button parejas, secuencias, botonSignin, botonWorldRecords, botonSignout, botonsubirRecord;
	LinearLayout linearConexiones;
	//MediaPlayer soundAnimator;
	public static MediaPlayer soundClick1, soundPlay,soundPlay2,soundRapeCreek,
	SoundAplausos, soundCombo, musicaFondo, estrellitasSound, OhhSounds1, aplausosSound1;
	/*
    AdView adView;
	public static AdRequest adRequest;
	*/
	int MAX_VOLUME = 100; //volumen m�ximo de referencia
	SharedPreferences soundPrefs, conexionScores;
	SharedPreferences.Editor editor, editorScores;
	public static final String SONIDO = "sonido";
	public static final String CONEXION = "conexion";
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modosseleccion);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );
		 //Preferencias para el sonido. Cuando creamos la actividad , por defecto el sonido estar� activado y asi lo guardaremos 
		
		
		soundClick1 =  MediaPlayer.create(this, R.raw.button54);				
		soundClick1.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		soundPlay =  MediaPlayer.create(this, R.raw.keyboardtypesngl);				
		soundPlay.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		soundPlay2 =  MediaPlayer.create(this, R.raw.keyboardtypesngl);				
		soundPlay2.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		soundRapeCreek = MediaPlayer.create(this, R.raw.ropecreck);		
		soundRapeCreek.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		SoundAplausos = MediaPlayer.create(this, R.raw.crwdbugl);
		SoundAplausos.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		soundCombo = MediaPlayer.create(this, R.raw.sonidobutton25); //sonido para combos
		soundCombo.setVolume(setCustomVolumen(80), setCustomVolumen(80));
		estrellitasSound = MediaPlayer.create(this, R.raw.estrellitas);
		estrellitasSound.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		OhhSounds1 = MediaPlayer.create(this, R.raw.ohh1);
		OhhSounds1.setVolume(setCustomVolumen(80), setCustomVolumen(80));
		aplausosSound1 = MediaPlayer.create(this, R.raw.applau22);
		aplausosSound1.setVolume(setCustomVolumen(70), setCustomVolumen(70));
		
		
		soundPrefs = this.getSharedPreferences(SONIDO, 0);
		editor = soundPrefs.edit();
		editor.putBoolean("sonido", true); 
		editor.commit();
		
		//Preferencias conexion a google juegos:
		conexionScores = this.getSharedPreferences(CONEXION, 0);
		editorScores = conexionScores.edit();
		editorScores.putBoolean("conexion", false);
		editorScores.commit();
		
		//publicidad
		/*
		adView = (AdView)this.findViewById(R.id.adView);
		adRequest = new AdRequest.Builder()		
		.build();
	    adView.loadAd(adRequest);
	    */
		parejas = (Button)findViewById(R.id.modoparejass);
		secuencias =(Button)findViewById(R.id.modosecuenciass);
		botonSignin = (Button)findViewById(R.id.sign_in_button);
		botonWorldRecords = (Button)findViewById(R.id.global_scores);
		botonSignout = (Button)findViewById(R.id.sign_out_button);
		linearConexiones = (LinearLayout)findViewById(R.id.linear_conexiones);
		
		//QUitamos el autologin a google + al iniciar la activity
		//getGameHelper().setMaxAutoSignInAttempts(0);
		//O bien cambiamos en la clase	GameHelper:	mConnectOnStart = false;
		
		botonWorldRecords.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				if(soundPrefs.getBoolean("sonido", true))					
					soundClick1.start();				
				
				//worldRecords();
			}
		});						
		
		botonSignin.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				if(soundPrefs.getBoolean("sonido", true))
					soundClick1.start();
				
				//conectar();
			}
		});
		//Este es el boton de los logros. No vamos a poner un boton de desconectar. 
		botonSignout.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				if(soundPrefs.getBoolean("sonido", true))
					soundClick1.start();				

				//mostrarLogros();
			}
		});
				
		
		animarBotones();
		
		//Modo parejas
		parejas.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
				if(soundPrefs.getBoolean("sonido", true))
					soundClick1.start();
				
				Intent i  = new Intent (ActivityModosSeleccion.this, ActivityModos.class);
				i.putExtra("modoJuego", "parejas");				
				ActivityModosSeleccion.this.startActivity(i);
				
			}
		});
		//Modos secuencias
		secuencias.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
				if(soundPrefs.getBoolean("sonido", true))
					soundClick1.start();
				
				Intent i  = new Intent (ActivityModosSeleccion.this, ActivityModos.class);
				i.putExtra("modoJuego", "secuencias");
				ActivityModosSeleccion.this.startActivity(i);
				
			}
		});		
		
					
	}//FIN ONCREATE
	
	/*
	//METODOS CONEXION A GOOGLE PLAY SERVICES SCORES
	@Override
	public void onSignInFailed() {
		//Mejor no ponemos nada
		//Toast.makeText(this, "Offline", Toast.LENGTH_LONG).show();
		
	}

	@Override
	public void onSignInSucceeded() {			
		Toast.makeText(this, getResources().getString(R.string.you_are_connected), Toast.LENGTH_LONG).show();
		botonSignin.setVisibility(View.GONE);
		linearConexiones.setVisibility(View.VISIBLE);			
	}

	public void conectar(){
		editorScores.putBoolean("conexion", true);
		editorScores.commit();
		beginUserInitiatedSignIn();
		
	}
	
	public void desconectar(){			
		if(isSignedIn()){		
			 signOut();		
			 editorScores.putBoolean("conexion", false);
			 editorScores.commit();
			 botonSignin.setVisibility(View.VISIBLE);
			 linearConexiones.setVisibility(View.GONE);	
			 Toast.makeText(this, getResources().getString(R.string.you_are_not_connected), Toast.LENGTH_LONG).show();
		}
	}
	
	public void mostrarLogros(){
		 if (isSignedIn()) {
			 startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 5000);
	        } else {
	        	Toast.makeText(this, getResources().getString(R.string.not_available), Toast.LENGTH_LONG).show();
	        }
		
	}
	
	public void worldRecords(){
		 if (isSignedIn()) {
	            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), 5000);
	        } else {
	        	Toast.makeText(this, getResources().getString(R.string.not_available), Toast.LENGTH_LONG).show();
	        }
	}
	*/
	
	///------fin google play games
	 
	
	public void animarBotones(){
		//Animacion de los linearlayout en el comienzo:
		animacionBotones(parejas, 1.0f, 0, 0,0);
		animacionBotones(secuencias, -1.0f, 0, 0, 0);
		
	}
	
	public void animacionBotones(View view, float fromX, float toX, float fromY, float toY){
		TranslateAnimation animBoton2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, fromX, Animation.RELATIVE_TO_PARENT, toX,
				 Animation.RELATIVE_TO_PARENT, fromY, Animation.RELATIVE_TO_PARENT, toY);
		animBoton2.setDuration(700);		
		view.startAnimation(animBoton2);	
		new Timer().schedule(new TimerTask(){ //O bien lo hacemos con un animBoton2.setAnimationListener(new Animation.AnimationListener()
			@Override
			public void run() {				
				if(soundPrefs.getBoolean("sonido", true))
					soundClick1.start();
								
			}			
		}, 700); 
	}
	
	//------- BOTONES SHAREME Y RATEME
	//(shareit)
	public void botonShareit (View v){
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		
		if(soundPrefs.getBoolean("sonido", true))
			soundClick1.start();
		
		sharingIntent.setType("text/plain"); //Pasaremos solo 
		String shareBody = getResources().getString(R.string.sharecontent);
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.sharesubject)); 
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent,getResources().getString(R.string.compartirvia)));	
	}
	
		
	//(rateme)
	/*
	private boolean MyStartActivity(Intent aIntent) {
	    try{
	        startActivity(aIntent);
	        return true;
	    }catch (ActivityNotFoundException e){
	        return false;
	    }
	}
	*/
	
	public void botonRateme (View v){
		//desconectar();
		/*
		Intent intent = new Intent(Intent.ACTION_VIEW);
		
		if(soundPrefs.getBoolean("sonido", true))
			soundClick1.start();
		
	    //Try Google play
	    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.jesusmanuelrodriguez.freshmemory"));
	    startActivity(intent);
	    */
	    /*
	    if (MyStartActivity(intent) == false) {
	        //Market (Google play) app seems not installed, let's try to open a webbrowser
	        intent.setData(Uri.parse("https://play.google.com/store/apps/details?[Id]"));
	        if (MyStartActivity(intent) == false) {
	            //Well if this also fails, we have run out of options, inform the user.
	            Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
	        }
	    }
	    */
	}
	
	
	
	//-------------------------
		//DESEA SALIR DEL JUEGO?
		@Override
		public void onBackPressed() {
			AlertDialog.Builder alertbox = new AlertDialog.Builder(ActivityModosSeleccion.this);
	        alertbox.setIcon(R.drawable.boton_tick);
	        alertbox.setTitle(R.string.deseasalir2);
	        alertbox.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
	        alertbox.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					
				}
			});		
	        alertbox.show();
	        //super.onBackPressed();
		}
		
		public float setCustomVolumen(int soundVolume){  //volumen que queremos poner (la mitad del total de volumen) o  lo que queramos!!!
			return (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME))); // ver:http://stackoverflow.com/questions/5215459/android-mediaplayer-setvolume-function
		}

		@Override
		protected void onPause() {			
			super.onPause();
			//adView.pause();
			/*
			new Timer().schedule(new TimerTask(){		
				@Override
				public void run() {						
					soundAnimator.release();
				}			
			}, soundAnimator.getDuration());					
			*/
		}

		@Override
		protected void onResume() {			
			super.onResume();		
			//adView.resume();
			/*
			soundAnimator =  MediaPlayer.create(this, R.raw.button54);				
			soundAnimator.setVolume(setCustomVolumen(70), setCustomVolumen(70));
			*/
			animarBotones();
			
		}
		
		@Override
		protected void onDestroy() {	
			super.onDestroy();
			//adView.destroy();
			//soundClick1.release();soundPlay.release();soundPlay2.release();
			//soundRapeCreek.release(); SoundAplausos.release(); soundCombo.release(); musicaFondo.release();
			
		}
		
		
		
		//--------MENU OPCIONES , RESETEAR MARCADORES Y ABOUT
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflar = getMenuInflater();
			inflar.inflate(R.menu.memorion_menu, menu);  		
			MenuItem item = menu.findItem(R.id.resetmarcadores);
			item.setVisible(true);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Checkbox de item sonido:
			View checkBoxView = View.inflate(this, R.layout.checkbox_menu, null);
			final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkboxmenu);		
		
			int itemId = item.getItemId();
			
			if (itemId == R.id.aboutus) {
				//Codigo para aboutUs	
				Intent i = new Intent(this, AboutUs.class);
				this.startActivity(i);
			} else if (itemId == R.id.resetmarcadores) {
				AlertDialog.Builder alerta = new AlertDialog.Builder(this);
				alerta.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {								
					@Override
					public void onClick(DialogInterface arg0, int arg1) {					
						deleteSecuencias();
						deleteParejas();
					}
				});
				alerta.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				alerta.setIcon(R.drawable.boton_resetmarcadores);
				alerta.setMessage(R.string.seborraranlosrecords);
				alerta.setTitle(R.string.borrarmarcadores);
				AlertDialog alert = alerta.create();
				alert.show();
			}else if (itemId ==R.id.menu_sonido){				
				
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
		
		public void deleteSecuencias(){
			SharedPreferences.Editor deleteNumeros ,deleteSimon, deleteSeries;	
			SharedPreferences numerosScores, simonScores, seriesScores;			
		
			numerosScores = getSharedPreferences(ActivitySecuencias.GAME_PREFS, 0);
			seriesScores = getSharedPreferences(ActivitySecuencias2.GAME_PREFS,0);
			simonScores = getSharedPreferences(ActivitySimon.GAME_PREFS, 0);	
			
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
		
		public  void deleteParejas(){
			SharedPreferences.Editor deletetimeattack, deleteClassic, deleteRelaciones;
			SharedPreferences timeattackScores, classicScores, relacionesScores;
			
			timeattackScores = getSharedPreferences(ActivityTimeAttack.GAME_PREFS, 0);	
			classicScores = getSharedPreferences(ActivityBoard.HighScoresLevelClassic, 0);	
			relacionesScores =	 getSharedPreferences(ActivityBoard.HighScoresLevelRelaciones, 0);
						
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
		
		public void setMaxValue (int nivel, String KEYString, SharedPreferences sharedPref){
			
			SharedPreferences.Editor editor = sharedPref.edit();
			int levell = sharedPref.getInt(KEYString, 0); // donde  es el valor devuelto si no existe la preferencia
			
			if(nivel >levell){
				editor.putInt(KEYString, nivel);
				editor.commit();
			}			
		}	

}
