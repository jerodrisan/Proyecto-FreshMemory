package com.jesusmanuelrodriguez.freshmemory;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* si usamos un ViewStub: (Aunque no me sale)
 	class ComboViewStub implements Runnable{
			@Override
			public void run() {
				ViewStub vista = (ViewStub)findViewById(R.id.viewstubCombo);
				vista.inflate();
				vista.setVisibility(View.VISIBLE);
				TextView texto = (TextView)findViewById(R.id.combo_textView1);
				LinearLayout linear = (LinearLayout)findViewById(R.id.combolinearlayout);
				texto.setText(String.valueOf(numClicks));	
				
				ScaleAnimation escalar = new ScaleAnimation(1,3,1,3);
				escalar.setDuration(2000);
				linear.startAnimation(escalar);				
			}		
		}
 
 */

public class ComboPantalla1  implements Runnable {
	
	Context context;	
	String texto;
	int idLayoutPadre;
	int color;
	int duracion;
	boolean reverse=false;
	float fromX, toX, fromY, toY;
	int fondo;
	
	public ComboPantalla1(Context context, String texto, int idLayoutPadre, int color, boolean reverse,
			float fromX, float toX, float fromY, float toY, int duracion,int fondo){
		this.texto=texto;	
		this.context= context;
		this.idLayoutPadre=idLayoutPadre;
		this.color=color;
		this.reverse=reverse;
		this.fromX= fromX;
		this.toX = toX;
		this.fromY = fromY;
		this.toY = toY;
		this.duracion=duracion;
		this.fondo = fondo;
	}
	
	@Override
	public void run() {
		mostrarCombo();		
	}
	
	public void mostrarCombo(){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		final LinearLayout combolayout = new LinearLayout(context){				
			LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view =li.inflate(R.layout.combo_pantalla1, this, true);				
		};
		RelativeLayout llay = (RelativeLayout)((Activity) context).findViewById(idLayoutPadre);	
		RelativeLayout.LayoutParams relatparams = new RelativeLayout.LayoutParams
			(LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);				
		
		relatparams.addRule(RelativeLayout.CENTER_IN_PARENT); //En caso de que queramos punerlo en el centro del RelativeLayout
		relatparams.setMargins(dm.widthPixels/5, dm.heightPixels/3, 0, 0);
		
		combolayout.setLayoutParams(relatparams);
		combolayout.setBackgroundResource(fondo);
		llay.addView(combolayout);				
		
		ScaleAnimation scale = new ScaleAnimation(fromX,toX,fromY,toY);
		scale.setDuration(duracion);	
		if(reverse){ //si indicamos que queremos ida y vuelta en la animacion
			scale.setRepeatMode(Animation.REVERSE);  
			scale.setRepeatCount(1);
		}
		AlphaAnimation alphaanim = new AlphaAnimation(0,1);
		alphaanim.setDuration(1000);
		//alphaanim.setRepeatCount(1);
		//alphaanim.setRepeatMode(Animation.REVERSE);	
		
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(scale);
		set.addAnimation(alphaanim);		
		combolayout.setAnimation(set);
		
		//Cuando termina la animacion ocultamos la view		
		scale.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation arg0) {
				combolayout.setVisibility(View.GONE);
				}
				@Override
				public void onAnimationRepeat(Animation arg0) {}		
				@Override
				public void onAnimationStart(Animation arg0) {}
		});
		
		TextView texto1 =(TextView)combolayout.findViewById(R.id.combo_textView1);
		texto1.setText(texto);
		texto1.setTextColor(color);		
		
		
		
	}
}
