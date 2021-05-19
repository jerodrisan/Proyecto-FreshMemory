package com.jesusmanuelrodriguez.freshmemory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

public class ScoresHigh {
		
	SharedPreferences gamePrefs; 	
	String bloquePrefs;
	String nivel;
	Context contexto;
	float acumulado;

	ScoresHigh(Context contexto, String bloquePrefs, float acumulado, String nivel){		
		this.contexto = contexto;
		this.acumulado = acumulado;
		this.nivel= nivel;
		gamePrefs = contexto.getSharedPreferences(bloquePrefs, 0);
		
	}
	
	 public void setHighScores(){
		 int exScore = (int)acumulado;
			if(exScore>0){
				//we have a valid score	
				SharedPreferences.Editor scoreEdit = gamePrefs.edit();
				DateFormat dateForm = new SimpleDateFormat("dd/MM/yy");
				String dateOutput = dateForm.format(new Date());
				//get existing scores
				String scores = gamePrefs.getString(nivel, "");
				//check for scores
				if(scores.length()>0){
					//we have existing scores
					List<Score> scoreStrings = new ArrayList<Score>();
					//split scores
					String[] exScores = scores.split("\\|");
					//add score object for each
					for(String eSc : exScores){
						String[] parts = eSc.split(" pts      -      ");
						scoreStrings.add(new Score(Integer.parseInt(parts[0]),parts[1]));
					}
					//new score
					Score newScore = new Score(exScore,dateOutput);
					scoreStrings.add(newScore);
					//sort
					Collections.sort(scoreStrings);
					//get top ten
					StringBuilder scoreBuild = new StringBuilder("");
					for(int s=0; s<scoreStrings.size(); s++){
						if(s>=10) break;
						if(s>0) scoreBuild.append("|");					
						scoreBuild.append(scoreStrings.get(s).getScoreText());
					}
					//write to prefs
					scoreEdit.putString(nivel, scoreBuild.toString());
					scoreEdit.commit();
				}
				else{
					//no existing scores
					scoreEdit.putString(nivel, ""+exScore+" pts"+"      -      "+dateOutput);
					scoreEdit.commit();
				}
			}
	 }	
}
