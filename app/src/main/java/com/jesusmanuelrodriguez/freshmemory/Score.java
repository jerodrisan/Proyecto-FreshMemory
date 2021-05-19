package com.jesusmanuelrodriguez.freshmemory;



public class Score implements Comparable<Score> {

	//score date and number
	private String scoreDate;
	public int scoreNum;

	public Score( int num, String date){
		scoreDate=date;
		scoreNum=num;
	}

	//check this score against another
	public int compareTo(Score sc){
		//return 0 if equal
		//1 if passed greater than this
		//-1 if this greater than passed
		return sc.scoreNum>scoreNum? 1 : sc.scoreNum<scoreNum? -1 : 0;
	}

	//return score display text
	public String getScoreText(){
		return scoreNum+" pts"+"      -      "+scoreDate;
	}

}
