package com.jesusmanuelrodriguez.freshmemory;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.nineoldandroids.animation.ObjectAnimator;
//import android.animation.ObjectAnimator;
//import android.animation.PropertyValuesHolder;

/*EN caso de hacerlo con un Handler:
new Handler().postDelayed(new Runnable() {				 
       @Override
       public void run() {
           // This method will be executed once the timer is over
           // Start your app main activity
           Intent i = new Intent(SplashScrenn.this, ActivityConfig.class);
           startActivity(i);	               
           finish();
       }
   }, 3000);
}

Tambien se puede hacer con un Timer.
*/		

public class SplashScrenn extends Activity implements AnimationListener{
	
	View view;
	Context contexto;
	private int state_machine = 0;
	Hilo hilo;
	MediaPlayer mp;
	ObjectAnimator anim1, anim2;
	TranslateAnimation animTrans1, animTrans2, animTr1, animTr2, animTr3;
	RotateAnimation animRotate1, animRotate2,  animRot1, animRot2;;
	AnimationDrawable frame, frame2, frame3; // 
	@Override
	protected void onCreate(Bundle savedInstanceState) { 	
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.splashscreen);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		contexto = this;
		hilo = new Hilo();
		hilo.start();
		mp = MediaPlayer.create(this, R.raw.effect28);
		//Esperamos 150 miliseg a que comience el sonido de animacion
		new Thread(new Runnable(){			
			@Override
			public void run() {
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {				
					e.printStackTrace();
				}
				mp.start();							
			}			
		}).start();
		
		
		//final View viewanim = (View)this.findViewById(R.id.anim_splash);
	    //final View viewanim2 = (View)this.findViewById(R.id.anim_splash2);
		
		//final ImageView imagenlogocompany = (ImageView)findViewById(R.id.imageview_logo);
		//final ImageView imagenonmbrecompany = (ImageView)findViewById(R.id.imageview_nombrecompany); 	
		//final TextView imagenonmbrecompany = (TextView)findViewById(R.id.textview_splash); 
		final ImageView imagentitulojuego = (ImageView)findViewById(R.id.imageview_logofresh);
	
		
		//ANIMACION  TITULO JUEGO MOVIMIENTO TRANSLACIONAL
		anim1 = ObjectAnimator.ofFloat(imagentitulojuego, "translationX", 100, -100, 90,-90, 80, -80, 60,-60, 40,-40, 20,-20, 15, -15, 10,-10, 5,-5);
		anim1.setDuration(3000);
		anim1.start();
	
		
		
		
	//ANIMACION DEL NOMBRE Y LOGO EMPRESA
		/*
		 final ScaleAnimation scale = new ScaleAnimation(
				 0, 1,
				 0, 1,
				 Animation.ABSOLUTE, 400,
				 Animation.ABSOLUTE, 400);
		 
		 scale.setDuration(4000);		
		// textoTitulo.setAnimation(scale);
		 //textoTitulo.startAnimation(scale);
		 
		 //USANDO OBJECTANIMATOR Usaremos libreria nineoldandroids para API=<10(Para API=>11 no usamos nineoldandroids y el primer parametro es: View.Scale_X )
		  PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1.0f ,4.0f ,1.0f ,4.0f,1.0f,3.0f,1.0f, 2.0f); //escalamos al 200% y bajamos al tamaño original
		  PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1.0f,4.0f,1.0f,4.0f,1.0f,3.0f,1.0f, 2.0f ); 
		  
		  ObjectAnimator scaleAnimation = ObjectAnimator.ofPropertyValuesHolder(imagenlogocompany, pvhX, pvhY);
		  scaleAnimation.setDuration(3000);
		  //scaleAnimation.setRepeatMode(Animation.REVERSE);		  
		  //scaleAnimation.setRepeatCount(2);
		  scaleAnimation.start();
		  
		  ObjectAnimator scaleAnimation2 = ObjectAnimator.ofPropertyValuesHolder(imagenonmbrecompany, pvhX, pvhY);
		  scaleAnimation2.setDuration(3000);
		 // scaleAnimation2.setRepeatMode(Animation.REVERSE);		  
		  //scaleAnimation2.setRepeatCount(2);
		  scaleAnimation2.start();
		*/
		  
		/*
		//ANIMACION DE LOS DOS BICHOS DE LAS VIEWS viewanim1 Y viewanim2:
		
		animTrans1 = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 1, 
				Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0);
		
		animTrans1.setInterpolator(new LinearInterpolator());
		animTrans1.setDuration(5000);
		
		//1º Ponemos un background a la animacion usando el archivo de la animacion de frames:
		viewanim.setBackgroundResource(R.anim.animation_bichito);
		viewanim2.setBackgroundResource(R.anim.animation_bichito);
		//2ª Hacemos un cast a AnimationDrawable del background de la view para trabajar con el:
		frame = (AnimationDrawable)viewanim.getBackground();
		frame2 = (AnimationDrawable)viewanim2.getBackground();
		//Comenzamos la animacion propia de los dos bichos
		frame.start();
		frame2.start();
		
		//animamos el bicho de la view 1 de forma traslacional:		
		//La view1 esta en el lado izquierdo del panel y desde ahi empieza a moverse
		viewanim.startAnimation(animTrans1);		
		
		//Animacion bicho de la view 2 rotacion: 
		//La viewanim2 esta puesta en el lado izquierdo del Layout con lo cual hace una semicircunferencia de radio en el centro
		animRotate1 = new RotateAnimation(0, 180, 
				Animation.RELATIVE_TO_PARENT, 0.5f,
				Animation.RELATIVE_TO_PARENT, 0);		
		animRotate1.setDuration(2000);
		animRotate1.setFillAfter(true);
		viewanim2.startAnimation(animRotate1);
		
		//SI QUEREMOS HACER QUE LA ANIMACION REALICE COMBINACION DE MOVIMIENTOS continuos, implementamos
		//la clae AnimationListener con los siguientes pasos. Lo haremos de forma dinamica sin usar xml
		//Lo  mejor es usar meter las animaciones en un XML para que no haya tanto codigo
		
		//Los pasos son:		
		
		// A: Crearemos la view de forma dinamica en vez de sacar la id del xml: 
		
			
			RelativeLayout parent = (RelativeLayout)this.findViewById(R.id.relative_parent);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(110,110);
			//POnemos la view en el centro del RelativeLayout
			params.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.imageview_splash);			
			view = new View(this);
			view.setId(11);
			view.setLayoutParams(params);
			view.setBackgroundResource(R.anim.animation_mariposa1);
			view.setVisibility(View.VISIBLE);
			parent.addView(view);
			
		// B: Crearemos los objetos traslacion que queramos: (Dinamicamente sin usar xml)	
		//Nota, Para dar sensacion de movimiento continuo creo que no se puede usar con Rotacion.
		
		 animTr1 = new TranslateAnimation(
				 Animation.ABSOLUTE, 0,
				 Animation.ABSOLUTE,100,
				 Animation.ABSOLUTE, 0, 
				 Animation.ABSOLUTE, -400	 
				 );
		 animTr1.setDuration(1000);
		 animTr1.setFillAfter(true);
		 
		 animTr2 = new TranslateAnimation( 
				 Animation.ABSOLUTE, 100,
				 Animation.ABSOLUTE, 180,
				 Animation.ABSOLUTE, -400,
				 Animation.ABSOLUTE, -400);
		 animTr2.setDuration(500);
		 animTr2.setFillAfter(true);
		 
		 animTr3 = new TranslateAnimation(
				 Animation.ABSOLUTE, 180,
				 Animation.ABSOLUTE,230,
				 Animation.ABSOLUTE, -400, 
				 Animation.ABSOLUTE, -0	 
				 );
		 animTr3.setDuration(1000);
		 animTr3.setFillAfter(true);
		 
		 //C:  Asignamos las animaciones a la view mediante el adaptador 
		 frame3 = (AnimationDrawable)view.getBackground();
		 frame3.start();
		 view.clearAnimation();
		 view.setAnimation(animTr1);
		 view.startAnimation(animTr1);
		 //asignamos un listener para que cuando termine la animacion de traslacion se haga la siguiente
		 animTr1.setAnimationListener(this);
		 */
		 		 
		
		 
	}
	
	@Override
	public void onAnimationEnd(Animation a) {
		a.setAnimationListener(null);
		switch (state_machine) {
		case 0:
			a = animTr2;
			state_machine=1;
			break;
		case 1:
			a = animTr3;
			state_machine=2;
			break;
		
		}
		a.setAnimationListener(this);		
			
		view.clearAnimation();
		view.setAnimation(a);
		view.startAnimation(a);
	}
	@Override
	public void onAnimationRepeat(Animation animation) {}		
	@Override
	public void onAnimationStart(Animation animation) {}
	
		
	@Override
	protected void onPause() {	
		super.onPause();
		//finalizamos la musica y finalizamos la actividad:
		this.finish();
	}
	
	

	class Hilo extends Thread{
		@Override
		public void run() {			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}finally{
				Intent i = new Intent("com.example.a3_memoriongame.ACTIVITYMODOSSELECCION"); 
				startActivity(i);				
			}			
		}
	}	
	

}
