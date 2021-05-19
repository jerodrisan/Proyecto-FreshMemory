package com.jesusmanuelrodriguez.freshmemory;

/* Creacion de un radiogrupo de forma dinamica:

LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams
		(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		50 , LayoutParams.WRAP_CONTENT);

RadioGroup radiogrupo2 = new RadioGroup(this);
radiogrupo2.setLayoutParams(params2);
radiogrupo2.setOrientation(RadioGroup.HORIZONTAL);
RadioButton[] rb = new RadioButton[4];		

for (int i=0; i<4; ++i){
	
	rb[i] = new RadioButton(this);
	rb[i].setButtonDrawable(R.drawable.boton);
	rb[i].setLayoutParams(params);
	radiogrupo2.addView(rb[i]);
	
}
rb[0].setChecked(true);
linear.addView(radiogrupo2);
*/


/*
Si queremos poner un RadiGrupo en el xml sin hacer nada de forma dinamica, usariamos: 
	 En el atributo del radioboton: 
			android:button="@drawable/radio_style4" 
			
	donde radio_style4".xml
	
	<?xml version="1.0" encoding="utf-8"?>
	<selector xmlns:android="http://schemas.android.com/apk/res/android" >
	     <item  android:state_checked="true"   android:drawable="@drawable/panel1071"   ></item>
	    
	    <item	android:state_checked="false"	android:drawable="@drawable/panel1072"	 ></item>

	</selector>
	Y asi para cada uno de los radiobotones
	 */
/*
  	Punto B:
 En caso de que queramos poner un dos radiogrupos con 4 radiobotones cada uno en dos filas diferentes parece ser
 que no se puede. He estado intentando relacionar los dos radiogropos para que quede de tal forma que  al pulsar
 cualquier radioboton de la primera fila se desactiven los restantes incluidos los del radiogrupo de la segunda 
 fila . Tambien al reves (pulsar cualquier radioboton del radiogrupo de la segunda fila que se pongan checked false
 todos incluidos los de la primera pero no hay forma. 
 POr lo tanto se ha hecho uso de un gridLayout con Linearlayouts y dentro imagevies en vez de uso de radiogrupos . 
 
 Este es el codigo intentado de los radiogrupos: 
 
 En el onCreate: 
 
 	RadioGroup rg2 = (RadioGroup)this.findViewById(R.id.radiogrupo2);
		RadioGroup rg3 = (RadioGroup)this.findViewById(R.id.radiogrupo3);
		
			
		for(int i=0; i<rg2.getChildCount(); ++i){
			RadioButton view = (RadioButton )rg2.getChildAt(i);
			if(i==0){
				view.setButtonDrawable(R.drawable.panel431);
				view.setChecked(true);
			}
			else{
				view.setButtonDrawable(R.drawable.panel432);	
				view.setChecked(false);
			}
		}
		for(int i=0; i<rg3.getChildCount(); ++i){
			RadioButton view = (RadioButton )rg3.getChildAt(i);
			view.setButtonDrawable(R.drawable.panel432);	
			view.setChecked(false);			
		}
		rg2.setOnCheckedChangeListener(new radiolistener());
		rg3.setOnCheckedChangeListener(new radiolistener2());
		
	}
	
La clase seria: 
	
	
	class radiolistener implements RadioGroup.OnCheckedChangeListener{
		
		@Override
		public void onCheckedChanged(RadioGroup radiogroup, int i) {
		  	
			for (int j=0; j<radiogroup.getChildCount(); ++j){
				
				RadioButton view = (RadioButton) radiogroup.getChildAt(j);
				
				if (view.getId()==i){
					System.out.println("hola");
					view.setChecked(true);
					//view.setButtonDrawable(contexto.getResources().getDrawable(R.drawable.panel431));
					view.setButtonDrawable(R.drawable.panel431);					
										
				}else{ 
					System.out.println("1bucle bg2");
					view.setChecked(false);
				//view.setButtonDrawable(contexto.getResources().getDrawable(R.drawable.panel432));
				view.setButtonDrawable(R.drawable.panel432);				
				}
			}
			for(int k=0; k<rg3.getChildCount();++k){	
				System.out.println("2bucle bg2");
					RadioButton v = (RadioButton)rg3.getChildAt(k);
					v.setChecked(false);
					v.setButtonDrawable(R.drawable.panel432);
			}
			}			
		}
 
 El xml de los radiogrupos: 
 
 	     <RadioGroup 
         android:id="@+id/radiogrupo2"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:orientation="horizontal"
         >
        
             <RadioButton 
                android:id="@+id/radiotema1"
                android:layout_width="170px"
         		android:layout_height="wrap_content"   
           		android:layout_marginLeft="1dp"
                 
                 />
             <RadioButton 
                android:id="@+id/radiotema2"
                android:layout_width="170px"
         		android:layout_height="wrap_content"      
           		android:layout_marginLeft="1dp"
                 
                 />
              <RadioButton 
                android:id="@+id/radiotema3"
                android:layout_width="170px"
         		android:layout_height="wrap_content"      
           		android:layout_marginLeft="1dp"
                 
                 />
               <RadioButton 
                android:id="@+id/radiotema4"
                android:layout_width="170px"
         		android:layout_height="wrap_content"    
           		android:layout_marginLeft="1dp"                 
                 />
             
       </RadioGroup>
       
            <RadioGroup 
         android:id="@+id/radiogrupo3"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:orientation="horizontal"           
           >
         
             <RadioButton 
                android:id="@+id/radiotema5"
                android:layout_width="170px"
         		android:layout_height="wrap_content"                   
           		android:layout_marginLeft="1dp"
                 
                 />
             <RadioButton 
                android:id="@+id/radiotema6"
                android:layout_width="170px"
         		android:layout_height="wrap_content"   
           		android:layout_marginLeft="1dp"
                 
                 />
              <RadioButton 
                android:id="@+id/radiotema7"
                android:layout_width="170px"
         		android:layout_height="wrap_content"       
           		android:layout_marginLeft="1dp"
                 
                 />
               <RadioButton 
                android:id="@+id/radiotema8"
                android:layout_width="170px"
         		android:layout_height="wrap_content"          	
           		android:layout_marginLeft="1dp"
                 
                 />    
              
       </RadioGroup>    
       
 
 PUNTO B: 
 	PONER view.visible o no : 
 	
 		linear = (LinearLayout)this.findViewById(R.id.pruebaaaa);			
		linear.setOnClickListener(new clicklisten());
		
 		class clicklisten implements View.OnClickListener{
		
		@Override
		public void onClick(View view) {
			
			ImageView v = (ImageView)linear.getChildAt(0);
			ImageView v2 = (ImageView)linear.getChildAt(1);
			
			if(v.getVisibility()==View.VISIBLE){
				v.setVisibility(View.GONE);
				v2.setVisibility(View.VISIBLE);
			}else{
				v.setVisibility(View.VISIBLE);
				v2.setVisibility(View.GONE);
			}
			/* Forma mas elegante:
			 * int visibility1 = (v.getVisibility()==View.VISIBLE)?View.GONE:view.VISIBLE;
			int visibility2 = (v.getVisibility()==View.VISIBLE)?view.VISIBLE:view.GONE;
			v.setVisibility(visibility1);
			v2.setVisibility(visibility2);
			 */
		 
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v4.app.FragmentActivity;
//import android.widget.GridLayout;
import android.support.v7.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ActivityClassic extends FragmentActivity{//Ver diferencia entre FragmentActivity y Activity: http://stackoverflow.com/questions/10609268/difference-between-fragment-and-fragmentactivity
	
	Context contexto;	
	int viewactual; 	
	ImageView imagenactual, imagenactual2, imagenPreview;	
	RadioGroup rg;
	RadioButton rb43, rb64, rb85, rb107;
	LinearLayout linearclicado;
	String clicado;
	Button botonPlay;
	//MediaPlayer mp1, mp2;
	int MAX_VOLUME = 100; //volumen m�ximo de referencia
	int previews [] = {R.drawable.sample11, R.drawable.sample12, R.drawable.sample13, R.drawable.sample14 };
	//AdView adView;
	SharedPreferences soundPrefs;
	SharedPreferences.Editor editor;
	public static final String SONIDO = "sonido";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		//Las imagenes de los cuadros 128x128  son de 171x171 hay que tratar de adaptarlas 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_classic);
		
		 //Preferencias para el sonido. Cuando creamos la actividad , por defecto el sonido estar� activado y asi lo guardaremos 
		soundPrefs = this.getSharedPreferences(SONIDO, 0);
		editor = soundPrefs.edit();
		editor.putBoolean("sonido", soundPrefs.getBoolean("sonido", true)); 
		editor.commit();
		
		//Modo pantalla completa:
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//publicidad
		/*
		adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		*/
		//System.out.println("Mensaje desde activity Clasic");
		
		//Peque�a animacion boton play
		botonPlay = (Button)this.findViewById(R.id.boton_playclasico);
		AlphaAnimation anim = new AlphaAnimation(0.7f, 1);
		anim.setDuration(1000);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setRepeatMode(Animation.REVERSE);
		botonPlay.setAnimation(anim);
		
		//RelativeLayout relats = (RelativeLayout)this.findViewById(R.id.linearConfig2);
		AlphaAnimation anim2 = new AlphaAnimation(1,0.5f);
		anim2.setDuration(1000);
		anim2.setRepeatCount(Animation.INFINITE);
		anim2.setRepeatMode(Animation.REVERSE);
		//relats.setAnimation(anim2);
		
		contexto = this;
		
		imagenPreview = (ImageView)this.findViewById(R.id.imagenpreview);
		
		
		rg = (RadioGroup)this.findViewById(R.id.radiogrupo1);
		
		rb43 = (RadioButton)this.findViewById(R.id.rb43);
		rb64= (RadioButton)this.findViewById(R.id.rb64);
		rb85 = (RadioButton)this.findViewById(R.id.rb85);
		rb107 = (RadioButton)this.findViewById(R.id.rb107);
		
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
		
		final GridLayout grid = (GridLayout)this.findViewById(R.id.idGrid);
		
		//Sacamos la imagen primera del primer LinearLayout para asignarsela a imagen actual, 
		//ya que por defecto esa estar� como visible y el resto como gone.
		
		
		LinearLayout first = (LinearLayout)grid.getChildAt(0);
		ImageView im1 =(ImageView)first.getChildAt(0);	
		ImageView im2 =(ImageView)first.getChildAt(1);
		imagenactual=im1;
		imagenactual2=im2;
		
		clicado = grid.getChildAt(0).getTag().toString();
		setClicado(clicado);
		//Creamos un bucle para que cada vez que pulsemos un boton se cambie de estado. 
		
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
		//mp1.release();	
		/*
		new Timer().schedule(new TimerTask(){		
			@Override
			public void run() {					
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
	
	public void setImagePreview(){
		int rb = rg.getCheckedRadioButtonId();		
		if (rb == R.id.rb43) {
			setImageResource(0);
		} else if (rb == R.id.rb64) {
			setImageResource(1);
		} else if (rb == R.id.rb85) {
			setImageResource(2);
		} else if (rb == R.id.rb107) {
			setImageResource(3);
		}
	}
	public void setImageResource(int i){
		
		if(getClicado().equals("paises") || getClicado().equals("deportes")|| getClicado().equals("smiles")||
				getClicado().equals("cosas")|| getClicado().equals("marcas") || getClicado().equals("objetos")){
			 imagenPreview.setImageResource(previews[i]);
		}		
	}
	
	
	public void botonPlay(View v){	
		
		if(soundPrefs.getBoolean("sonido", true))
			//mp2.start();
			ActivityModosSeleccion.soundPlay.start();
		
		int rb = rg.getCheckedRadioButtonId();		
		Intent i = new Intent(contexto, ActivityBoard.class);
		i.putExtra("modo", "clasico");		
		if (rb == R.id.rb43) {
			String[] str43 = rb43.getTag().toString().split("x");
			i.putExtra("numFilas", Integer.parseInt(str43[0]));
			i.putExtra("numColum", Integer.parseInt(str43[1]));
			i.putExtra("nivel", "nivel1"); 	//variable usada para poner el nivel el bloque de prefs y luego sacarla 
			i.putExtra("tematica", clicado);
			if(clicado.equals("paises"))  startActivity(i);
			if(clicado.equals("deportes"))	startActivity(i);
			if(clicado.equals("smiles"))  startActivity(i);
			if(clicado.equals("cosas")) startActivity(i);
			if(clicado.equals("marcas"))  startActivity(i);
			if(clicado.equals("objetos"))  startActivity(i);
		} else if (rb == R.id.rb64) {
			String[] str64 = rb64.getTag().toString().split("x");
			i.putExtra("numFilas", Integer.parseInt(str64[0]));
			i.putExtra("numColum", Integer.parseInt(str64[1]));
			i.putExtra("tematica", clicado);
			i.putExtra("nivel", "nivel2");
			if(clicado.equals("paises"))  startActivity(i);
			if(clicado.equals("deportes"))	startActivity(i);
			if(clicado.equals("smiles"))  startActivity(i);
			if(clicado.equals("cosas")) startActivity(i);
			if(clicado.equals("marcas"))  startActivity(i);
			if(clicado.equals("objetos"))  startActivity(i);
		} else if (rb == R.id.rb85) {
			String[] str85 = rb85.getTag().toString().split("x");
			i.putExtra("numFilas", Integer.parseInt(str85[0]));
			i.putExtra("numColum", Integer.parseInt(str85[1]));
			i.putExtra("tematica", clicado);
			i.putExtra("nivel", "nivel3");
			if(clicado.equals("paises"))  startActivity(i);
			if(clicado.equals("deportes"))	startActivity(i);
			if(clicado.equals("smiles"))  startActivity(i);
			if(clicado.equals("cosas")) startActivity(i);
			if(clicado.equals("marcas"))  startActivity(i);
			if(clicado.equals("objetos"))  startActivity(i);
		} else if (rb == R.id.rb107) {
			String[] str107 = rb107.getTag().toString().split("x");
			i.putExtra("numFilas", Integer.parseInt(str107[0]));
			i.putExtra("numColum", Integer.parseInt(str107[1]));
			i.putExtra("tematica", clicado);
			i.putExtra("nivel", "nivel4");
			if(clicado.equals("paises"))  startActivity(i);
			if(clicado.equals("deportes"))	startActivity(i);
			if(clicado.equals("smiles"))  startActivity(i);
			if(clicado.equals("cosas")) startActivity(i);
			if(clicado.equals("marcas"))  startActivity(i);
			if(clicado.equals("objetos"))  startActivity(i);
		}
	}
	
	
		//La comprobacion de las alturas y anchuras del imageview no se hace en el onCreate sino en este metodo.
		@Override
		public void onWindowFocusChanged(boolean hasFocus){		
			ImageView im = (ImageView)findViewById(R.id.iv00);
			//RadioButton rd = (RadioButton)this.findViewById(R.id.rb43);
			int alt = im.getHeight();
			int anch = im.getWidth();
			//System.out.println("altura " + alt + "ancho " +anch);
		}
			
}
	



	

    
    

