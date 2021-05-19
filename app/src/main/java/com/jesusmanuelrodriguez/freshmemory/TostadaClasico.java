package com.jesusmanuelrodriguez.freshmemory;

import com.jesusmanuelrodriguez.freshmemory.R;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/* si usamos una tostada clasica: 
 //tostada clasica:
		class Tostada implements Runnable{
			String men;
			String tipo;
			Tostada(String mensaje, String tipo){
				this.men=mensaje;
				this.tipo=tipo;
			}
			@Override
			public void run() {
				if(tipo.equals("largo")){
					Toast.makeText(contexto, men, Toast.LENGTH_LONG).show();		
				}else 
					Toast.makeText(contexto, men, Toast.LENGTH_SHORT).show();				
			}			
		}
 */

public class TostadaClasico implements Runnable{
	
	Context contexto;	
	ImageView imgLogro ;
	TextView txtPuntos ;
	TextView txtAciertos;	
	int idImagen;
	float puntos;
	int aciertos, numParejas;	
	View layout;
	LayoutInflater inflater ;
	ViewGroup linear;
	DisplayMetrics dm;
	
	//Pillamos la imagen de la pareja encontrada, texto, número de aciertos y número de parejas que quedan. 
	TostadaClasico (Context contexto, int idImagen, float puntos, int aciertos, int numParejas){
		this.contexto = contexto;
		this.idImagen= idImagen;
		this.puntos=puntos;
		this.aciertos=aciertos;
		this.numParejas=numParejas;
	}
	
	@Override
	public void run() {				
		inflarViewyAsignar();
		mostrarTostada();
		
	}
	
	public void inflarViewyAsignar(){
		linear= (ViewGroup)((Activity) contexto).findViewById(R.id.toast_layout);
		//LinearLayout linear = (LinearLayout)findViewById(R.id.toast_layout);	
		inflater = ((Activity) contexto).getLayoutInflater(); 
		layout = inflater.inflate(R.layout.tostada_clasico, linear);
		/* O bien para ser mas precisos:
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater)contexto.getSystemService(infService);			 
		View layout = li.inflate(R.layout.tostada_layout, (ViewGroup) findViewById(R.id.toast_layout));
		*/
		//Pillamos las views del layout inflado del xml: 
		imgLogro = (ImageView)layout.findViewById(R.id.imgAcierto);
		txtPuntos = (TextView)layout.findViewById(R.id.txtTostadaPuntos);
		txtAciertos = (TextView)layout.findViewById(R.id.txtTostadaAciertos);	
		
	}
	
	public void mostrarTostada(){
		 dm = new DisplayMetrics();
		((Activity) contexto).getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		imgLogro.setImageResource(idImagen);				
		txtPuntos.setText(String.format("%.1f", puntos)+" pts");
		txtAciertos.setText(String.valueOf(aciertos)+"/"+String.valueOf(numParejas));
		Toast toast = new Toast(contexto);
		toast.setGravity(Gravity.CENTER, 0, 500);
		toast.setDuration(toast.LENGTH_SHORT);				
		toast.setView(layout);				
		toast.show();
		
		ScaleAnimation scalar = new ScaleAnimation(1,1.2f,1,1.2f);
		scalar.setDuration(300);
		scalar.setRepeatMode(Animation.REVERSE);
		scalar.setRepeatCount(1);		
		//txtPuntos.setAnimation(scalar); Se sale la animaciond el cuadro!!!
		
		
	}	

}
