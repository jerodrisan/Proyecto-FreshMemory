package com.jesusmanuelrodriguez.freshmemory;

import java.util.Random;

import com.jesusmanuelrodriguez.freshmemory.R;

public class Imagenes {
	
//----- http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
//----- Para hacer un shuffle de un array: 
	
public static int[] shuffleArray(int[] ar){	
    Random rnd = new Random();
    for (int i = ar.length - 1; i > 0; i--){
      int index = rnd.nextInt(i + 1);
      // Simple swap
      int a = ar[index];
      ar[index] = ar[i];
      ar[i] = a;
    }
    return ar;
  }

//En caso de hacer un shuffle de dos arrays por ejemplo en el metodo de relaciones:

public static int[][] shuffleArrayDoble(int[] ar, int[] ar2){	
	int[][]aux = new int[2][];	
    Random rnd = new Random();
    for (int i = ar.length - 1; i > 0; i--){
      int index = rnd.nextInt(i + 1);
      // Simple swap
      int a = ar[index];
      int b = ar2[index];
      
      ar[index] = ar[i];
      ar2[index] =ar2[i];
      
      ar[i] = a;
      ar2[i] = b;
    }
    aux[0]=ar;
    aux[1]=ar2;      
    return aux;
  }

	
//prueba

public static final int[] prueba1 = {1, 2, 3, 4, 5, 6, 7, 8 ,9, 10};
public static final int[] prueba2 ={10,20,30,40,50,60,70,80,90,100};
public static final int[][] pruebar ={prueba1, prueba2};
public static final int[] prueba11 = {11, 12, 13, 14, 15, 16, 17, 18 ,19, 20};
public static final int[] prueba22 ={110,120,130,140,150,160,170,180,190,1100};
public static final int[][] pruebarr ={prueba11, prueba22};
public static final int[][][] pruebatotar ={pruebar, pruebarr};

//Imagenes y palabras SPINER MODO TIMEATTACK

//Para clasico:
public static final int []  spinnerValores = { R.string.tema4, R.string.tema2, 
	R.string.tema3,R.string.tema1, R.string.tema5, R.string.tema6 };

public static final int[] spinnerImages ={R.drawable.tema4, R.drawable.tema2, R.drawable.tema3,R.drawable.tema1 ,
	R.drawable.tema5, R.drawable.tema6};

//Para relaciones:
public static final int [] spinnerValores2 ={R.string.tema66, R.string.tema11,	R.string.tema55, R.string.tema22,
	R.string.tema77, R.string.tema33 };

public static final int []  spinnerImages2 = {R.drawable.tema_12, R.drawable.tema7,	 R.drawable.tema_11,R.drawable.tema8,	
	R.drawable.tema_13,	R.drawable.tema9 
	};

//----- BANDERAS:

public static final int[] banderas ={R.drawable.bbande1, R.drawable.bbande2, R.drawable.bbande3, R.drawable.bbande4,
	R.drawable.bbande5, R.drawable.bbande6, R.drawable.bbande7, R.drawable.bbande8, R.drawable.bbande9, R.drawable.bbande10,
	R.drawable.bbande11, R.drawable.bbande12, R.drawable.bbande13, R.drawable.bbande14,
	R.drawable.bbande15, R.drawable.bbande16, R.drawable.bbande17, R.drawable.bbande18, R.drawable.bbande19, R.drawable.bbande20,
	R.drawable.bbande21, R.drawable.bbande22, R.drawable.bbande23, R.drawable.bbande24,
	R.drawable.bbande25, R.drawable.bbande26, R.drawable.bbande27, R.drawable.bbande28, R.drawable.bbande29, R.drawable.bbande30,
	R.drawable.bbande31, R.drawable.bbande32, R.drawable.bbande33, R.drawable.bbande34, R.drawable.bbande35		
	};


public static final int[] banderasRelaciones ={R.drawable.bbanderela1, R.drawable.bbanderela2, R.drawable.bbanderela3, R.drawable.bbanderela4,
	R.drawable.bbanderela5, R.drawable.bbanderela6, R.drawable.bbanderela7, R.drawable.bbanderela8, R.drawable.bbanderela9, R.drawable.bbanderela10,
	R.drawable.bbanderela11, R.drawable.bbanderela12, R.drawable.bbanderela13, R.drawable.bbanderela14,
	R.drawable.bbanderela15, R.drawable.bbanderela16, R.drawable.bbanderela17, R.drawable.bbanderela18, R.drawable.bbanderela19, R.drawable.bbanderela20,
	R.drawable.bbanderela21, R.drawable.bbanderela22, R.drawable.bbanderela23, R.drawable.bbanderela24,
	R.drawable.bbanderela25, R.drawable.bbanderela26, R.drawable.bbanderela27, R.drawable.bbanderela28, R.drawable.bbanderela29, R.drawable.bbanderela30,
	R.drawable.bbanderela31, R.drawable.bbanderela32, R.drawable.bbanderela33, R.drawable.bbanderela34, R.drawable.bbanderela35
};


public static final int[] banderasPalabras ={ R.string.flag1, R.string.flag2, R.string.flag3, R.string.flag4, R.string.flag5,
	R.string.flag6, R.string.flag7, R.string.flag8, R.string.flag9, R.string.flag10,
	R.string.flag11, R.string.flag12, R.string.flag13, R.string.flag14, R.string.flag15,
	R.string.flag16, R.string.flag17, R.string.flag18, R.string.flag19, R.string.flag20,
	R.string.flag21, R.string.flag22, R.string.flag23, R.string.flag24, R.string.flag25,
	R.string.flag26, R.string.flag27, R.string.flag28, R.string.flag29, R.string.flag30,
	R.string.flag31, R.string.flag32, R.string.flag33, R.string.flag34, R.string.flag35
};


		//DEPORTES:

public static final int[] deportes={R.drawable.ddepor1, R.drawable.ddepor2, R.drawable.ddepor3, R.drawable.ddepor4,
	R.drawable.ddepor5,R.drawable.ddepor6, R.drawable.ddepor7, R.drawable.ddepor8, R.drawable.ddepor9, R.drawable.ddepor10,
	R.drawable.ddepor11, R.drawable.ddepor12, R.drawable.ddepor13, R.drawable.ddepor14,
	R.drawable.ddepor15,R.drawable.ddepor16, R.drawable.ddepor17, R.drawable.ddepor18, R.drawable.ddepor19, R.drawable.ddepor20,
	R.drawable.ddepor21, R.drawable.ddepor22, R.drawable.ddepor23, R.drawable.ddepor24,
	R.drawable.ddepor25,R.drawable.ddepor26, R.drawable.ddepor27, R.drawable.ddepor28, R.drawable.ddepor29, R.drawable.ddepor30,
	R.drawable.ddepor31, R.drawable.ddepor32, R.drawable.ddepor33, R.drawable.ddepor34,	R.drawable.ddepor35
};

public static final int [] deportespalabra ={R.string.depor1, R.string.depor2, R.string.depor3, R.string.depor4,
	R.string.depor5, R.string.depor6, R.string.depor7, R.string.depor8, R.string.depor9, R.string.depor10,
	R.string.depor11, R.string.depor12, R.string.depor13, R.string.depor14,
	R.string.depor15, R.string.depor16, R.string.depor17, R.string.depor18, R.string.depor19, R.string.depor20,
	R.string.depor21, R.string.depor22, R.string.depor23, R.string.depor24,
	R.string.depor25, R.string.depor26, R.string.depor27, R.string.depor28, R.string.depor29, R.string.depor30,
	R.string.depor31, R.string.depor32, R.string.depor33, R.string.depor34,	R.string.depor35
};
		//EMOTICONOS

public static final int[] emoticonos={R.drawable.eemoti1, R.drawable.eemoti2, R.drawable.eemoti3, R.drawable.eemoti4,
	R.drawable.eemoti5, R.drawable.eemoti6, R.drawable.eemoti7, R.drawable.eemoti8, R.drawable.eemoti9, R.drawable.eemoti10,
	R.drawable.eemoti11, R.drawable.eemoti12, R.drawable.eemoti13, R.drawable.eemoti14,	R.drawable.eemoti15, 
	R.drawable.eemoti16, R.drawable.eemoti17, R.drawable.eemoti18, R.drawable.eemoti19, R.drawable.eemoti20,
	R.drawable.eemoti21, R.drawable.eemoti22, R.drawable.eemoti23, R.drawable.eemoti24,	R.drawable.eemoti25,
	R.drawable.eemoti26, R.drawable.eemoti27, R.drawable.eemoti28, R.drawable.eemoti29, R.drawable.eemoti30, R.drawable.eemoti31,
	 R.drawable.eemoti32, R.drawable.eemoti33, R.drawable.eemoti34, R.drawable.eemoti35
};


	// -------- MARCAS

public static final int [] marcas ={R.drawable.mmarca1, R.drawable.mmarca2, R.drawable.mmarca3, R.drawable.mmarca4,
	R.drawable.mmarca5, R.drawable.mmarca6, R.drawable.mmarca7, R.drawable.mmarca8, R.drawable.mmarca9, R.drawable.mmarca10,
	R.drawable.mmarca11, R.drawable.mmarca12, R.drawable.mmarca13, R.drawable.mmarca14,
	R.drawable.mmarca15, R.drawable.mmarca16, R.drawable.mmarca17, R.drawable.mmarca18, R.drawable.mmarca19, R.drawable.mmarca20,
	R.drawable.mmarca21, R.drawable.mmarca22, R.drawable.mmarca23, R.drawable.mmarca24,
	R.drawable.mmarca25, R.drawable.mmarca26, R.drawable.mmarca27, R.drawable.mmarca28, R.drawable.mmarca29, R.drawable.mmarca30,
	R.drawable.mmarca31, R.drawable.mmarca32, R.drawable.mmarca33, R.drawable.mmarca34,R.drawable.mmarca35	
};

 // --------- OBJETOS

public static final int [] objetos ={R.drawable.oobjeto1, R.drawable.oobjeto2, R.drawable.oobjeto3, R.drawable.oobjeto4,
	R.drawable.oobjeto5, R.drawable.oobjeto6, R.drawable.oobjeto7, R.drawable.oobjeto8, R.drawable.oobjeto9, R.drawable.oobjeto10,
	R.drawable.oobjeto11, R.drawable.oobjeto12, R.drawable.oobjeto13, R.drawable.oobjeto14,
	R.drawable.oobjeto15, R.drawable.oobjeto16, R.drawable.oobjeto17, R.drawable.oobjeto18, R.drawable.oobjeto19, R.drawable.oobjeto20,
	R.drawable.oobjeto21, R.drawable.oobjeto22, R.drawable.oobjeto23, R.drawable.oobjeto24,
	R.drawable.oobjeto25, R.drawable.oobjeto26, R.drawable.oobjeto27, R.drawable.oobjeto28, R.drawable.oobjeto29, R.drawable.oobjeto30,
	R.drawable.oobjeto31, R.drawable.oobjeto32, R.drawable.oobjeto33, R.drawable.oobjeto34,R.drawable.oobjeto35	
};

 // --------- COSAS

public static final int [] cosas ={R.drawable.ccosas1, R.drawable.ccosas2, R.drawable.ccosas3, R.drawable.ccosas4,
	R.drawable.ccosas5, R.drawable.ccosas6, R.drawable.ccosas7, R.drawable.ccosas8, R.drawable.ccosas9, R.drawable.ccosas10,
	R.drawable.ccosas11, R.drawable.ccosas12, R.drawable.ccosas13, R.drawable.ccosas14,
	R.drawable.ccosas15, R.drawable.ccosas16, R.drawable.ccosas17, R.drawable.ccosas18, R.drawable.ccosas19, R.drawable.ccosas20,
	R.drawable.ccosas21, R.drawable.ccosas22, R.drawable.ccosas23, R.drawable.ccosas24,
	R.drawable.ccosas25, R.drawable.ccosas26, R.drawable.ccosas27, R.drawable.ccosas28, R.drawable.ccosas29, R.drawable.ccosas30,
	R.drawable.ccosas31, R.drawable.ccosas32, R.drawable.ccosas33, R.drawable.ccosas34,	R.drawable.ccosas35
};

 //----------- PROFESIONES

public static final int [] profesiones ={R.drawable.pprofe1, R.drawable.pprofe2, R.drawable.pprofe3, R.drawable.pprofe4,
	R.drawable.pprofe5, R.drawable.pprofe6, R.drawable.pprofe7, R.drawable.pprofe8, R.drawable.pprofe9, R.drawable.pprofe10,
	R.drawable.pprofe11, R.drawable.pprofe12, R.drawable.pprofe13, R.drawable.pprofe14,
	R.drawable.pprofe15, R.drawable.pprofe16, R.drawable.pprofe17, R.drawable.pprofe18, R.drawable.pprofe19, R.drawable.pprofe20,
	R.drawable.pprofe21, R.drawable.pprofe22, R.drawable.pprofe23, R.drawable.pprofe24,
	R.drawable.pprofe25, R.drawable.pprofe26, R.drawable.pprofe27, R.drawable.pprofe28, R.drawable.pprofe29, R.drawable.pprofe30,
	R.drawable.pprofe31, R.drawable.pprofe32, R.drawable.pprofe33, R.drawable.pprofe34,R.drawable.pprofe35
};


public static final int [] profesionesPalabras ={R.string.profe1, R.string.profe2, R.string.profe3, R.string.profe4, R.string.profe5,
	R.string.profe6, R.string.profe7, R.string.profe8, R.string.profe9, R.string.profe10,
	R.string.profe11, R.string.profe12, R.string.profe13, R.string.profe14, R.string.profe15,
	R.string.profe16, R.string.profe17, R.string.profe18, R.string.profe19, R.string.profe20,
	R.string.profe21, R.string.profe22, R.string.profe23, R.string.profe24, R.string.profe25,
	R.string.profe26, R.string.profe27, R.string.profe28, R.string.profe29, R.string.profe30,
	R.string.profe31, R.string.profe32, R.string.profe33, R.string.profe34, R.string.profe35
};



// -----------SEÑALES DE TRAFICO
/*
public static final int[] senales ={R.drawable.senal1, R.drawable.senal2, R.drawable.senal3, R.drawable.senal4,
	R.drawable.senal5, R.drawable.senal6, R.drawable.senal7, R.drawable.senal8, R.drawable.senal9, R.drawable.senal10,
	R.drawable.senal11, R.drawable.senal12, R.drawable.senal13, R.drawable.senal14,
	R.drawable.senal15, R.drawable.senal16, R.drawable.senal17, R.drawable.senal18, R.drawable.senal19, R.drawable.senal20,
	R.drawable.senal21, R.drawable.senal22, R.drawable.senal23, R.drawable.senal24,
	R.drawable.senal25, R.drawable.senal26, R.drawable.senal27, R.drawable.senal28, R.drawable.senal29, R.drawable.senal30,
	R.drawable.senal31, R.drawable.senal32, R.drawable.senal33, R.drawable.senal34, R.drawable.senal35
};
*/
/*
public static final int[] senalespalabras ={R.drawable.senalpala1, R.drawable.senalpala2, R.drawable.senalpala3, R.drawable.senalpala4,
	R.drawable.senalpala5, R.drawable.senalpala6, R.drawable.senalpala7, R.drawable.senalpala8, R.drawable.senalpala9, R.drawable.senalpala10,
	R.drawable.senalpala11, R.drawable.senalpala12, R.drawable.senalpala13, R.drawable.senalpala14,
	R.drawable.senalpala15, R.drawable.senalpala16, R.drawable.senalpala17, R.drawable.senalpala18, R.drawable.senalpala19, R.drawable.senalpala20,
	R.drawable.senalpala21, R.drawable.senalpala22, R.drawable.senalpala23, R.drawable.senalpala24,
	R.drawable.senalpala25, R.drawable.senalpala26, R.drawable.senalpala27, R.drawable.senalpala28, R.drawable.senalpala29, R.drawable.senalpala30,
	R.drawable.senalpala31, R.drawable.senalpala32, R.drawable.senalpala33, R.drawable.senalpala34, R.drawable.senalpala35
};
*/

// ----------- NUMEROS
public static final int[] numeros ={ R.drawable.mates1, R.drawable.mates2, R.drawable.mates3, R.drawable.mates4, R.drawable.mates5,
	R.drawable.mates6, R.drawable.mates7, R.drawable.mates8, R.drawable.mates9, R.drawable.mates10,
	R.drawable.mates11, R.drawable.mates12, R.drawable.mates13, R.drawable.mates14, R.drawable.mates15,R.drawable.mates16, R.drawable.mates17, R.drawable.mates18, R.drawable.mates19, R.drawable.mates20,
	R.drawable.mates21, R.drawable.mates22, R.drawable.mates23, R.drawable.mates24, R.drawable.mates25,R.drawable.mates26, R.drawable.mates27, R.drawable.mates28, R.drawable.mates29, R.drawable.mates30,
	R.drawable.mates31, R.drawable.mates32, R.drawable.mates33, R.drawable.mates34, R.drawable.mates35,R.drawable.mates36, R.drawable.mates37, R.drawable.mates38, R.drawable.mates39, R.drawable.mates40,	
	R.drawable.mates41, R.drawable.mates42, R.drawable.mates43, R.drawable.mates44, R.drawable.mates45,R.drawable.mates46, R.drawable.mates47, R.drawable.mates48, R.drawable.mates49, R.drawable.mates50,
	R.drawable.mates51, R.drawable.mates52, R.drawable.mates53, R.drawable.mates54, R.drawable.mates55,R.drawable.mates56, R.drawable.mates57, R.drawable.mates58, R.drawable.mates59, R.drawable.mates60,
	R.drawable.mates61, R.drawable.mates62, R.drawable.mates63, R.drawable.mates64, R.drawable.mates65,R.drawable.mates66, R.drawable.mates67, R.drawable.mates68, R.drawable.mates69, R.drawable.mates70,
	R.drawable.mates71, R.drawable.mates72, R.drawable.mates73, R.drawable.mates74, R.drawable.mates75,R.drawable.mates76, R.drawable.mates77, R.drawable.mates78, R.drawable.mates79, R.drawable.mates80,
	R.drawable.mates81, R.drawable.mates82, R.drawable.mates83, R.drawable.mates84, R.drawable.mates85,R.drawable.mates86, R.drawable.mates87, R.drawable.mates88, R.drawable.mates89, R.drawable.mates90,
	R.drawable.mates91, R.drawable.mates92, R.drawable.mates93, R.drawable.mates94, R.drawable.mates95,R.drawable.mates96, R.drawable.mates97, R.drawable.mates98, R.drawable.mates99, R.drawable.mates100,
	R.drawable.mates101, R.drawable.mates102, R.drawable.mates103, R.drawable.mates104, R.drawable.mates105,R.drawable.mates106, R.drawable.mates107, R.drawable.mates108, R.drawable.mates109, R.drawable.mates110,
	R.drawable.mates111, R.drawable.mates112, R.drawable.mates113, R.drawable.mates114, R.drawable.mates115,R.drawable.mates116, R.drawable.mates117, R.drawable.mates118, R.drawable.mates119, R.drawable.mates120,
	R.drawable.mates121, R.drawable.mates122, R.drawable.mates123, R.drawable.mates124, R.drawable.mates125,R.drawable.mates126, R.drawable.mates127, R.drawable.mates128, R.drawable.mates129, R.drawable.mates130,
	R.drawable.mates131, R.drawable.mates132, R.drawable.mates133, R.drawable.mates134, R.drawable.mates135,R.drawable.mates136, R.drawable.mates137, R.drawable.mates138, R.drawable.mates139, R.drawable.mates140,
	R.drawable.mates141, R.drawable.mates142, R.drawable.mates143, R.drawable.mates144, R.drawable.mates145,R.drawable.mates146, R.drawable.mates147, R.drawable.mates148, R.drawable.mates149, R.drawable.mates150,
	R.drawable.mates151, R.drawable.mates152, R.drawable.mates153, R.drawable.mates154, R.drawable.mates155,R.drawable.mates156, R.drawable.mates157, R.drawable.mates158, R.drawable.mates159, R.drawable.mates160,
	R.drawable.mates161, R.drawable.mates162, R.drawable.mates163, R.drawable.mates164, R.drawable.mates165,R.drawable.mates166, R.drawable.mates167, R.drawable.mates168, R.drawable.mates169, R.drawable.mates170,
	R.drawable.mates171, R.drawable.mates172, R.drawable.mates173, R.drawable.mates174, R.drawable.mates175,R.drawable.mates176, R.drawable.mates177, R.drawable.mates178, R.drawable.mates179, R.drawable.mates180,
	R.drawable.mates181, R.drawable.mates182, R.drawable.mates183, R.drawable.mates184, R.drawable.mates185,R.drawable.mates186, R.drawable.mates187, R.drawable.mates188, R.drawable.mates189, R.drawable.mates190,
	R.drawable.mates191, R.drawable.mates192, R.drawable.mates193, R.drawable.mates194, R.drawable.mates195,R.drawable.mates196, R.drawable.mates197, R.drawable.mates198, R.drawable.mates199, R.drawable.mates200
	
};

//Creamos un clon de numeros que nos vale para el modo timeattack hacer una pequeña chapuza
public static final int[] numeros2 ={R.drawable.mates1, R.drawable.mates2, R.drawable.mates3, R.drawable.mates4, R.drawable.mates5,
	R.drawable.mates6, R.drawable.mates7, R.drawable.mates8, R.drawable.mates9, R.drawable.mates10,
	R.drawable.mates11, R.drawable.mates12, R.drawable.mates13, R.drawable.mates14, R.drawable.mates15,R.drawable.mates16, R.drawable.mates17, R.drawable.mates18, R.drawable.mates19, R.drawable.mates20,
	R.drawable.mates21, R.drawable.mates22, R.drawable.mates23, R.drawable.mates24, R.drawable.mates25,R.drawable.mates26, R.drawable.mates27, R.drawable.mates28, R.drawable.mates29, R.drawable.mates30,
	R.drawable.mates31, R.drawable.mates32, R.drawable.mates33, R.drawable.mates34, R.drawable.mates35,R.drawable.mates36, R.drawable.mates37, R.drawable.mates38, R.drawable.mates39, R.drawable.mates40,	
	R.drawable.mates41, R.drawable.mates42, R.drawable.mates43, R.drawable.mates44, R.drawable.mates45,R.drawable.mates46, R.drawable.mates47, R.drawable.mates48, R.drawable.mates49, R.drawable.mates50,
	R.drawable.mates51, R.drawable.mates52, R.drawable.mates53, R.drawable.mates54, R.drawable.mates55,R.drawable.mates56, R.drawable.mates57, R.drawable.mates58, R.drawable.mates59, R.drawable.mates60,
	R.drawable.mates61, R.drawable.mates62, R.drawable.mates63, R.drawable.mates64, R.drawable.mates65,R.drawable.mates66, R.drawable.mates67, R.drawable.mates68, R.drawable.mates69, R.drawable.mates70,
	R.drawable.mates71, R.drawable.mates72, R.drawable.mates73, R.drawable.mates74, R.drawable.mates75,R.drawable.mates76, R.drawable.mates77, R.drawable.mates78, R.drawable.mates79, R.drawable.mates80,
	R.drawable.mates81, R.drawable.mates82, R.drawable.mates83, R.drawable.mates84, R.drawable.mates85,R.drawable.mates86, R.drawable.mates87, R.drawable.mates88, R.drawable.mates89, R.drawable.mates90,
	R.drawable.mates91, R.drawable.mates92, R.drawable.mates93, R.drawable.mates94, R.drawable.mates95,R.drawable.mates96, R.drawable.mates97, R.drawable.mates98, R.drawable.mates99, R.drawable.mates100,
	R.drawable.mates101, R.drawable.mates102, R.drawable.mates103, R.drawable.mates104, R.drawable.mates105,R.drawable.mates106, R.drawable.mates107, R.drawable.mates108, R.drawable.mates109, R.drawable.mates110,
	R.drawable.mates111, R.drawable.mates112, R.drawable.mates113, R.drawable.mates114, R.drawable.mates115,R.drawable.mates116, R.drawable.mates117, R.drawable.mates118, R.drawable.mates119, R.drawable.mates120,
	R.drawable.mates121, R.drawable.mates122, R.drawable.mates123, R.drawable.mates124, R.drawable.mates125,R.drawable.mates126, R.drawable.mates127, R.drawable.mates128, R.drawable.mates129, R.drawable.mates130,
	R.drawable.mates131, R.drawable.mates132, R.drawable.mates133, R.drawable.mates134, R.drawable.mates135,R.drawable.mates136, R.drawable.mates137, R.drawable.mates138, R.drawable.mates139, R.drawable.mates140,
	R.drawable.mates141, R.drawable.mates142, R.drawable.mates143, R.drawable.mates144, R.drawable.mates145,R.drawable.mates146, R.drawable.mates147, R.drawable.mates148, R.drawable.mates149, R.drawable.mates150,
	R.drawable.mates151, R.drawable.mates152, R.drawable.mates153, R.drawable.mates154, R.drawable.mates155,R.drawable.mates156, R.drawable.mates157, R.drawable.mates158, R.drawable.mates159, R.drawable.mates160,
	R.drawable.mates161, R.drawable.mates162, R.drawable.mates163, R.drawable.mates164, R.drawable.mates165,R.drawable.mates166, R.drawable.mates167, R.drawable.mates168, R.drawable.mates169, R.drawable.mates170,
	R.drawable.mates171, R.drawable.mates172, R.drawable.mates173, R.drawable.mates174, R.drawable.mates175,R.drawable.mates176, R.drawable.mates177, R.drawable.mates178, R.drawable.mates179, R.drawable.mates180,
	R.drawable.mates181, R.drawable.mates182, R.drawable.mates183, R.drawable.mates184, R.drawable.mates185,R.drawable.mates186, R.drawable.mates187, R.drawable.mates188, R.drawable.mates189, R.drawable.mates190,
	R.drawable.mates191, R.drawable.mates192, R.drawable.mates193, R.drawable.mates194, R.drawable.mates195,R.drawable.mates196, R.drawable.mates197, R.drawable.mates198, R.drawable.mates199, R.drawable.mates200,	
	R.drawable.mates201, R.drawable.mates202, R.drawable.mates203, R.drawable.mates204, R.drawable.mates205,R.drawable.mates206, R.drawable.mates207, R.drawable.mates208, R.drawable.mates209, R.drawable.mates210,
	R.drawable.mates211, R.drawable.mates212, R.drawable.mates213, R.drawable.mates214, R.drawable.mates215,R.drawable.mates216, R.drawable.mates217, R.drawable.mates218, R.drawable.mates219, R.drawable.mates220,
	R.drawable.mates221, R.drawable.mates222, R.drawable.mates223, R.drawable.mates224, R.drawable.mates225,R.drawable.mates226, R.drawable.mates227, R.drawable.mates228, R.drawable.mates229, R.drawable.mates230,
	R.drawable.mates231, R.drawable.mates232, R.drawable.mates233, R.drawable.mates234, R.drawable.mates235,R.drawable.mates236, R.drawable.mates237, R.drawable.mates238, R.drawable.mates239, R.drawable.mates240,
	R.drawable.mates241, R.drawable.mates242, R.drawable.mates243, R.drawable.mates244, R.drawable.mates245,R.drawable.mates246, R.drawable.mates247, R.drawable.mates248, R.drawable.mates249, R.drawable.mates250,
	R.drawable.mates251, R.drawable.mates252, R.drawable.mates253, R.drawable.mates254, R.drawable.mates255,R.drawable.mates256, R.drawable.mates257, R.drawable.mates258, R.drawable.mates259, R.drawable.mates260,
	R.drawable.mates261, R.drawable.mates262, R.drawable.mates263, R.drawable.mates264, R.drawable.mates265,R.drawable.mates266, R.drawable.mates267, R.drawable.mates268, R.drawable.mates269, R.drawable.mates270,
	R.drawable.mates271, R.drawable.mates272, R.drawable.mates273, R.drawable.mates274, R.drawable.mates275,R.drawable.mates276, R.drawable.mates277, R.drawable.mates278, R.drawable.mates279, R.drawable.mates280,
	R.drawable.mates281, R.drawable.mates282, R.drawable.mates283, R.drawable.mates284, R.drawable.mates285,R.drawable.mates286, R.drawable.mates287, R.drawable.mates288, R.drawable.mates289, R.drawable.mates290,
	R.drawable.mates291, R.drawable.mates292, R.drawable.mates293, R.drawable.mates294, R.drawable.mates295,R.drawable.mates296, R.drawable.mates297, R.drawable.mates298, R.drawable.mates299, R.drawable.mates300
	
};


public static final int[] numerospalabras = {R.string.suma1, R.string.suma2, R.string.suma3, R.string.suma4, R.string.suma5,
	R.string.suma6, R.string.suma7, R.string.suma8, R.string.suma9, R.string.suma10, R.string.suma11, R.string.suma12, 
	R.string.suma13, R.string.suma14, R.string.suma15, R.string.suma16, R.string.suma17,
	R.string.suma18, R.string.suma19, R.string.suma20, R.string.suma21, R.string.suma22, R.string.suma23, R.string.suma24, 
	R.string.suma25, R.string.suma26, R.string.suma27, R.string.suma28, R.string.suma29,
	R.string.suma30, R.string.suma31, R.string.suma32, R.string.suma33, R.string.suma34, R.string.suma35
};




//----------------------NUMEROS ROMANOS:

public static final int[] numerosRomanos ={R.drawable.mates1, R.drawable.mates2, R.drawable.mates3, R.drawable.mates4, R.drawable.mates5,
	R.drawable.mates6, R.drawable.mates7, R.drawable.mates8, R.drawable.mates9, R.drawable.mates10,
	R.drawable.mates20, R.drawable.mates30, R.drawable.mates40,  R.drawable.mates50,  R.drawable.mates60, 
	R.drawable.mates70,	R.drawable.mates80,  R.drawable.mates90,  R.drawable.mates100, R.drawable.mates200,
	R.drawable.mates300, R.drawable.mates400,	R.drawable.mates500, R.drawable.mates600, R.drawable.mates700,
	R.drawable.mates800, R.drawable.mates900, R.drawable.mates1000,	R.drawable.mates2000, R.drawable.mates3000,
	R.drawable.mates4000, R.drawable.mates5000,	R.drawable.mates6000, R.drawable.mates7000,	R.drawable.mates8000
	
};

//Usamos este clon para hacer una pqueña chapuza en el modo timeattack para numeros romamos
public static final int[]  numerosRomanos2 = {R.drawable.mates1, R.drawable.mates2, R.drawable.mates3, R.drawable.mates4, R.drawable.mates5,
	R.drawable.mates6, R.drawable.mates7, R.drawable.mates8, R.drawable.mates9, R.drawable.mates10,
	R.drawable.mates20, R.drawable.mates30, R.drawable.mates40,  R.drawable.mates50,  R.drawable.mates60, 
	R.drawable.mates70,	R.drawable.mates80,  R.drawable.mates90,  R.drawable.mates100, R.drawable.mates200,
	R.drawable.mates300, R.drawable.mates400,	R.drawable.mates500, R.drawable.mates600, R.drawable.mates700,
	R.drawable.mates800, R.drawable.mates900, R.drawable.mates1000,	R.drawable.mates2000, R.drawable.mates3000,
	R.drawable.mates4000, R.drawable.mates5000,	R.drawable.mates6000, R.drawable.mates7000,	R.drawable.mates8000	
};

public static final int[] numerosRomanosPalabras = {R.string.numero1, R.string.numero2, R.string.numero3, R.string.numero4, R.string.numero5, 
		R.string.numero6, R.string.numero7, R.string.numero8, R.string.numero9, R.string.numero10, 	
		R.string.numero11, R.string.numero12, R.string.numero13, R.string.numero14, R.string.numero15, 
		R.string.numero16, R.string.numero17, R.string.numero18, R.string.numero19, R.string.numero20, 	
		R.string.numero21, R.string.numero22, R.string.numero23, R.string.numero24, R.string.numero25, 
		R.string.numero26, R.string.numero27, R.string.numero28, R.string.numero29, R.string.numero30, 	
		R.string.numero31, R.string.numero32, R.string.numero33, R.string.numero34, R.string.numero35 
};

//Usamos este clon
public static final int[] numerosRomanosPalabras2 = {R.string.numero1, R.string.numero2, R.string.numero3, R.string.numero4, R.string.numero5, 
	R.string.numero6, R.string.numero7, R.string.numero8, R.string.numero9, R.string.numero10, 	
	R.string.numero11, R.string.numero12, R.string.numero13, R.string.numero14, R.string.numero15, 
	R.string.numero16, R.string.numero17, R.string.numero18, R.string.numero19, R.string.numero20, 	
	R.string.numero21, R.string.numero22, R.string.numero23, R.string.numero24, R.string.numero25, 
	R.string.numero26, R.string.numero27, R.string.numero28, R.string.numero29, R.string.numero30, 	
	R.string.numero31, R.string.numero32, R.string.numero33, R.string.numero34, R.string.numero35 	
};

// ------------ VARIOS

public static final int[] varios ={R.drawable.vvarios1, R.drawable.vvarios2, R.drawable.vvarios3, R.drawable.vvarios4,
	R.drawable.vvarios5, R.drawable.vvarios6, R.drawable.vvarios7, R.drawable.vvarios8, R.drawable.vvarios9, R.drawable.vvarios10,
	R.drawable.vvarios11, R.drawable.vvarios12, R.drawable.vvarios13, R.drawable.vvarios14,
	R.drawable.vvarios15, R.drawable.vvarios16, R.drawable.vvarios17, R.drawable.vvarios18, R.drawable.vvarios19, R.drawable.vvarios20,
	R.drawable.vvarios21, R.drawable.vvarios22, R.drawable.vvarios23, R.drawable.vvarios24,
	R.drawable.vvarios25, R.drawable.vvarios26, R.drawable.vvarios27, R.drawable.vvarios28, R.drawable.vvarios29, R.drawable.vvarios30,
	R.drawable.vvarios31, R.drawable.vvarios32, R.drawable.vvarios33, R.drawable.vvarios34,	R.drawable.vvarios35
};


public static final int [] variosPalabras ={R.string.varios1, R.string.varios2, R.string.varios3, R.string.varios4, R.string.varios5,
	R.string.varios6, R.string.varios7, R.string.varios8, R.string.varios9, R.string.varios10,
	R.string.varios11, R.string.varios12, R.string.varios13, R.string.varios14, R.string.varios15,
	R.string.varios16, R.string.varios17, R.string.varios18, R.string.varios19, R.string.varios20,
	R.string.varios21, R.string.varios22, R.string.varios23, R.string.varios24, R.string.varios25,
	R.string.varios26, R.string.varios27, R.string.varios28, R.string.varios29, R.string.varios30,
	R.string.varios31, R.string.varios32, R.string.varios33, R.string.varios34, R.string.varios35	
};

//--MODO CLASIC PARA ActivityTimeAttack
public static final int[][] clasic = {cosas, deportes, emoticonos,banderas, marcas, objetos};

//-- MODO RELACIONES para ActivityTimeAttack
public static final int [][] img1 = { Imagenes.deportespalabra, Imagenes.deportes};	
public static final int [][] img2 = { Imagenes.profesionesPalabras, Imagenes.profesiones};	
public static final int [][] img3 = { Imagenes.variosPalabras, Imagenes.varios};	
public static final int [][] img5 = { Imagenes.numerospalabras, Imagenes.numeros};	
public static final int [][] img6 = { Imagenes.banderasPalabras, Imagenes.banderasRelaciones};	
public static final int [][] img7 = { Imagenes.numerosRomanosPalabras, Imagenes.numerosRomanos};
public static final int[][][] relac ={img6, img1, img5, img2, img7, img3};
	 
		
public static final int[] previews ={R.drawable.preview_timeattack, R.drawable.preview_timeattack2,
	R.drawable.preview_timeattack3, R.drawable.preview_timeattack4, R.drawable.preview_numeros,
	R.drawable.preview_series,	R.drawable.preview_simon,  R.drawable.preview_colores};


//Genrador de sumas aleatorias. Usaremos este en vez de las fijas que estan en strings.xml (suma1, suma2... )
//es decir , no usaremos: public static final int[] numeros
public static String[] genNumAleatorios(){
	int [][] matrizNum = new int[2][30];
	String [] Num_Signed = new String[30];
	String[] stringNumeros = new String[30];
	
	int randomm, valor;
	
	for (int i=0; i<30;++i){
		if(i<10){
			randomm = new Random().nextInt(10);
			valor = i+1 -  randomm;		
			matrizNum[0][i]=randomm;
			matrizNum[1][i]=valor;
		}else if (i>=10 && i<20){
			randomm = new Random().nextInt(20-10)+10;
			valor = i+1 -  randomm;	
			matrizNum[0][i]=randomm;
			matrizNum[1][i]=valor;
		}else if (i>=20){
			randomm = new Random().nextInt(30);
			valor = i+1 -  randomm;		
			matrizNum[0][i]=randomm;
			matrizNum[1][i]=valor;				
		}								
	}
	for(int i=0; i<30;++i){
		//System.out.println(matrizNum[0][i] + " "+ matrizNum[1][i] + " = ");
		if(matrizNum[1][i]>=0){
			Num_Signed[i]= " + "+matrizNum[1][i];
		}else if(matrizNum[1][i]<0){
			Num_Signed[i]= " - "+Math.abs(matrizNum[1][i]);
		}				
		stringNumeros[i] = String.valueOf(matrizNum[0][i]) + Num_Signed[i];			
	}
	return stringNumeros;
}
// ------------ MODO SECUENCIAS. TIPO DE SENCUENCIAS.

public static final int[] letras ={ R.drawable.letra1, R.drawable.letra2, R.drawable.letra3, R.drawable.letra4, R.drawable.letra5,
	R.drawable.letra6, R.drawable.letra7, R.drawable.letra8, R.drawable.letra9, R.drawable.letra10,
	R.drawable.letra11, R.drawable.letra12, R.drawable.letra13, R.drawable.letra14, R.drawable.letra15,
	R.drawable.letra16, R.drawable.letra17, R.drawable.letra18, R.drawable.letra19, R.drawable.letra20,
	R.drawable.letra21, R.drawable.letra22, R.drawable.letra23, R.drawable.letra24, R.drawable.letra25
};
public static final int [] geomet = {R.drawable.geome1, R.drawable.geome2, R.drawable.geome3, R.drawable.geome4, R.drawable.geome5,
	R.drawable.geome6, R.drawable.geome7, R.drawable.geome8, R.drawable.geome9,R.drawable.geome10, R.drawable.geome11, R.drawable.geome12,
	R.drawable.geome13, R.drawable.geome14,R.drawable.geome15, R.drawable.geome16, R.drawable.geome17, R.drawable.geome18 };


public static final int[] numylet ={R.drawable.letra1, R.drawable.mates1, R.drawable.letra2, R.drawable.mates2, R.drawable.letra3, R.drawable.mates3,
	R.drawable.letra4, R.drawable.mates4, R.drawable.letra5, R.drawable.mates5, R.drawable.letra6, R.drawable.mates6,
	R.drawable.letra7, R.drawable.mates7, R.drawable.letra8, R.drawable.mates8, R.drawable.letra9, R.drawable.mates9,
	R.drawable.letra10, R.drawable.mates10, R.drawable.letra11, R.drawable.mates11, R.drawable.letra12, R.drawable.mates12,
	R.drawable.letra13, R.drawable.mates13, R.drawable.letra14, R.drawable.mates14, R.drawable.letra15, R.drawable.mates15,
	R.drawable.letra16, R.drawable.mates16, R.drawable.letra17, R.drawable.mates17, R.drawable.letra18, R.drawable.mates18,
}; 

public static final int[] sumo3_resto1 = {
	R.drawable.mates1, R.drawable.mates4, R.drawable.mates3, R.drawable.mates6, R.drawable.mates5, R.drawable.mates8, R.drawable.mates7,
	R.drawable.mates10, R.drawable.mates9, R.drawable.mates12, R.drawable.mates11, R.drawable.mates14, R.drawable.mates13, R.drawable.mates16,
	R.drawable.mates15, R.drawable.mates18, R.drawable.mates17, R.drawable.mates20, R.drawable.mates19, R.drawable.mates22, R.drawable.mates21,
	R.drawable.mates24, R.drawable.mates23, R.drawable.mates26, R.drawable.mates25, R.drawable.mates28, R.drawable.mates27, R.drawable.mates30,
	R.drawable.mates29, R.drawable.mates32, R.drawable.mates31, R.drawable.mates34, R.drawable.mates33, R.drawable.mates36, R.drawable.mates35	
};

public static final int[] dados = {R.drawable.dado1, R.drawable.dado2, R.drawable.dado3, R.drawable.dado4, R.drawable.dado5,
	R.drawable.dado6, R.drawable.dado7, R.drawable.dado8, R.drawable.dado9, R.drawable.dado10, R.drawable.dado11,
	R.drawable.dado12, R.drawable.dado13, R.drawable.dado14, R.drawable.dado15, R.drawable.dado16, R.drawable.dado17, R.drawable.dado18
	
};

public static final int[] gusano1 = {R.drawable.gusano1, R.drawable.gusano2, R.drawable.gusano3, R.drawable.gusano4,
	R.drawable.gusano5, R.drawable.gusano6, R.drawable.gusano7, R.drawable.gusano8, R.drawable.gusano9, R.drawable.gusano10
};

public static final int[] gusano2 = {R.drawable.gusanor1, R.drawable.gusanor2, R.drawable.gusanor3, R.drawable.gusanor4,
	R.drawable.gusanor5, R.drawable.gusanor6, R.drawable.gusanor7, R.drawable.gusanor8, R.drawable.gusanor9, R.drawable.gusanor10
	, R.drawable.gusanor11
};

public static final int [] agujas ={ R.drawable.agujas1, R.drawable.agujas2, R.drawable.agujas3, R.drawable.agujas4, 
	R.drawable.agujas5, R.drawable.agujas6, R.drawable.agujas7, R.drawable.agujas8, R.drawable.agujas9,
	R.drawable.agujas10, R.drawable.agujas11, R.drawable.agujas12, R.drawable.agujas13
};

public static final int [] relojes ={ R.drawable.reloj1, R.drawable.reloj1_1, R.drawable.reloj2, R.drawable.reloj2_2,
	R.drawable.reloj3, R.drawable.reloj3_3, R.drawable.reloj4, R.drawable.reloj5, R.drawable.reloj6, R.drawable.reloj6_6,
	R.drawable.reloj7, R.drawable.reloj8, R.drawable.reloj8_8, R.drawable.reloj9, R.drawable.reloj10, R.drawable.reloj10_10,
	R.drawable.reloj11	
};

public static final int[] puntos = {R.drawable.puntos1, R.drawable.puntos2, R.drawable.puntos3, R.drawable.puntos4,
	R.drawable.puntos5, R.drawable.puntos6, R.drawable.puntos7, R.drawable.puntos8
};

public static final int [] num_operaciones ={R.drawable.num1, R.drawable.num2, R.drawable.num3, R.drawable.num4, 
	R.drawable.num5, R.drawable.num6, R.drawable.num7, R.drawable.num8, R.drawable.num9, R.drawable.num10,
	R.drawable.num11, R.drawable.num12, R.drawable.num13, R.drawable.num14, R.drawable.num15, R.drawable.num16	
};

public static final int[] formascolores = {R.drawable.formascolor1, R.drawable.formascolor2, R.drawable.formascolor3,
	R.drawable.formascolor4, R.drawable.formascolor5, R.drawable.formascolor6, R.drawable.formascolor7,
	R.drawable.formascolor8, R.drawable.formascolor9	
};

public static final int[] formascubos ={ R.drawable.formascubos1, R.drawable.formascubos2, R.drawable.formascubos3, 
	R.drawable.formascubos4, R.drawable.formascubos5, R.drawable.formascubos6, R.drawable.formascubos7, 
	R.drawable.formascubos8, R.drawable.formascubos9 	
};

public static final int[] formasyrayas ={ R.drawable.formasyrayas1,  R.drawable.formasyrayas2, R.drawable.formasyrayas3,
	 R.drawable.formasyrayas4, R.drawable.formasyrayas5, R.drawable.formasyrayas6, R.drawable.formasyrayas7,
	 R.drawable.formasyrayas8, R.drawable.formasyrayas9, R.drawable.formasyrayas10, R.drawable.formasyrayas11,
	 R.drawable.formasyrayas12	
};

public static final int[] gusano3 ={ R.drawable.gusanorr1,  R.drawable.gusanorr2,  R.drawable.gusanorr3,  R.drawable.gusanorr4, 
	 R.drawable.gusanorr5,  R.drawable.gusanorr6,  R.drawable.gusanorr7,  R.drawable.gusanorr8,  R.drawable.gusanorr9, 
	 R.drawable.gusanorr10,  R.drawable.gusanorr11,  R.drawable.gusanorr12	
};

public static final int[] gusano4 ={R.drawable.gusanorr1,  R.drawable.gusanorr2, R.drawable.gusanorrr3, R.drawable.gusanorrr4,
	 R.drawable.gusanorrr5, R.drawable.gusanorrr6, R.drawable.gusanorrr7, R.drawable.gusanorrr8, R.drawable.gusanorrr9,
	 R.drawable.gusanorrr10, R.drawable.gusanorrr11, R.drawable.gusanorrr12	
};

public static final int [][] tipoSecuencia = {Imagenes.numeros, Imagenes.letras, Imagenes.geomet, Imagenes.numylet,
	Imagenes.sumo3_resto1,	Imagenes.dados, Imagenes.gusano1, Imagenes.gusano2, Imagenes.agujas, Imagenes.relojes,
	Imagenes.puntos, Imagenes.num_operaciones, formascolores, formascubos, formasyrayas, gusano3, gusano4  };

//------------ IMAGENES MODO COLORES 

public static final int[] coloresnaranja = {R.drawable.ccosas1, R.drawable.ccosas11, R.drawable.ccosas12,
	R.drawable.ccosas20, R.drawable.colornaranja9, R.drawable.colornaranja10, R.drawable.colornaranja12, 
	R.drawable.colornaranja13, R.drawable.ddepor17, R.drawable.eemoti5, R.drawable.eemoti7,
	R.drawable.mmarca7, R.drawable.mmarca23, R.drawable.oobjeto1, R.drawable.vvarios2, R.drawable.vvarios8
};

public static final int[] coloresamarillos = {R.drawable.ccosas27, R.drawable.ccosas29, R.drawable.ccosas30, 
	R.drawable.coloramarillo1, R.drawable.coloramarillo2, R.drawable.coloramarillo5, R.drawable.coloramarillo6,
	R.drawable.coloramarillo7, R.drawable.coloramarillo11,  R.drawable.coloramarillo12, R.drawable.coloramarillo13, 
	R.drawable.coloramarillo14, R.drawable.coloramarillo15, R.drawable.coloramarillo17, R.drawable.coloramarillo18,
	R.drawable.eemoti13, R.drawable.eemoti15, R.drawable.eemoti18, R.drawable.eemoti31, R.drawable.eemoti34, 
	R.drawable.mmarca1, R.drawable.vvarios2, R.drawable.vvarios22, R.drawable.vvarios25
};

public static final int[] coloresazules = {R.drawable.bbande7, R.drawable.bbanderela18, R.drawable.ccosas22, 
	R.drawable.ccosas32, R.drawable.ddepor7, R.drawable.ddepor13, R.drawable.ddepor22, R.drawable.ddepor26, 
	R.drawable.ddepor29, R.drawable.eemoti29, R.drawable.eemoti33, R.drawable.interro, R.drawable.mmarca9, 
	R.drawable.mmarca10, R.drawable.mmarca17, R.drawable.mmarca18, R.drawable.mmarca26, R.drawable.mmarca29,
	R.drawable.mmarca32, R.drawable.oobjeto5,  R.drawable.oobjeto14,  R.drawable.oobjeto31, R.drawable.pprofe6,
	R.drawable.vvarios1
};

public static final int[] coloresnegros ={R.drawable.ccosas4, R.drawable.color1, R.drawable.colornegro1, 
	R.drawable.colornegro4, R.drawable.colornegro5, R.drawable.colornegro7, R.drawable.colornegro9,
	R.drawable.colornegro14, R.drawable.colornegro16, R.drawable.colornegro17, R.drawable.colornegro18,
	R.drawable.colornegro23, R.drawable.ddepor4, R.drawable.ddepor12, R.drawable.ddepor19,  R.drawable.ddepor33, 
	R.drawable.eemoti4, R.drawable.mmarca2, R.drawable.oobjeto2,  R.drawable.oobjeto20,  R.drawable.oobjeto23, 
	 R.drawable.oobjeto24,  R.drawable.oobjeto25, R.drawable.pprofe3, R.drawable.pprofe26, R.drawable.pprofe27, 
	 R.drawable.pprofe33, R.drawable.vvarios32
};

public static final int[] coloresrojos ={ R.drawable.bbande2,  R.drawable.bbande10,  R.drawable.bbande13, 
	 R.drawable.bbande29,  R.drawable.bbande34, R.drawable.ccosas3, R.drawable.ccosas7, R.drawable.ccosas21, 
	 R.drawable.colorrojo16, R.drawable.colorrojo18, R.drawable.ddepor1, R.drawable.eemoti10, R.drawable.eemoti11, 
	 R.drawable.eemoti20, R.drawable.eemoti28, R.drawable.mmarca5, R.drawable.pprofe8, R.drawable.pprofe11, 
	 R.drawable.pprofe12, R.drawable.vvarios7
};

public static final int[] coloresrosas ={R.drawable.ccosas10, R.drawable.color12, R.drawable.colorrosa6, 
	R.drawable.colorrosa7, R.drawable.colorrosa8, R.drawable.colorrosa10, R.drawable.colorrosa11, R.drawable.colorrosa12, 
	R.drawable.colorrosa13, R.drawable.colorrosa14, R.drawable.colorrosa15, R.drawable.colorrosa16, 
	R.drawable.eemoti1, 	R.drawable.eemoti6, R.drawable.pprofe32, R.drawable.pprofe35 	
};

public static final int[] coloresverdes={ R.drawable.colorverde2, R.drawable.colorverde6, R.drawable.colorverde7, 
	R.drawable.colorverde8, R.drawable.colorverde9, R.drawable.colorverde10, R.drawable.colorverde12, 
	R.drawable.colorverde14, R.drawable.colorverde15, R.drawable.colorverde16, R.drawable.colorverde18, 
	R.drawable.colorverde19, R.drawable.ddepor2, R.drawable.ddepor34, R.drawable.eemoti16,  R.drawable.eemoti23, 
	R.drawable.eemoti24, R.drawable.mmarca3, R.drawable.mmarca20, R.drawable.mmarca25, R.drawable.mmarca30, 
	R.drawable.pprofe2,  R.drawable.pprofe18,  R.drawable.pprofe20
};

public static final int[] coloresfranja1 = {R.drawable.colorfranja1, R.drawable.colorfranja2, R.drawable.colorfranja3, 
	R.drawable.colorfranja4, R.drawable.colorfranja5, R.drawable.colorfranja6, 	
	R.drawable.colorfranjav1, R.drawable.colorfranjav2, R.drawable.colorfranjav3, R.drawable.colorfranjav4,
	R.drawable.colorfranjav5, R.drawable.colorfranjav6
};

public static final int[] coloresfranja1h ={R.drawable.colorfranja1, R.drawable.colorfranja2, R.drawable.colorfranja3, 
	R.drawable.colorfranja4, R.drawable.colorfranja5, R.drawable.colorfranja6	
};

public static final int[] coloresfranja1v = {R.drawable.colorfranjav1, R.drawable.colorfranjav2, R.drawable.colorfranjav3, R.drawable.colorfranjav4,
	R.drawable.colorfranjav5, R.drawable.colorfranjav6
};


public static final int[] coloresfranja2 = {R.drawable.colorfranja2_1, R.drawable.colorfranja2_2,R.drawable.colorfranja2_3,
	R.drawable.colorfranja2_4, R.drawable.colorfranja2_5, R.drawable.colorfranja2_6,
	R.drawable.colorfranjav2_1, R.drawable.colorfranjav2_2,R.drawable.colorfranjav2_3,
	R.drawable.colorfranjav2_4, R.drawable.colorfranjav2_5, R.drawable.colorfranjav2_6
};

public static final int[] coloresfranja2h ={R.drawable.colorfranja2_1, R.drawable.colorfranja2_2,R.drawable.colorfranja2_3,
	R.drawable.colorfranja2_4, R.drawable.colorfranja2_5, R.drawable.colorfranja2_6
};

public static final int[] coloresfranja2v ={R.drawable.colorfranjav2_1, R.drawable.colorfranjav2_2,R.drawable.colorfranjav2_3,
	R.drawable.colorfranjav2_4, R.drawable.colorfranjav2_5, R.drawable.colorfranjav2_6	
};



public static final int[] coloresfranja3 ={ R.drawable.colorfranja3_1,  R.drawable.colorfranja3_2,  R.drawable.colorfranja3_3,
	 R.drawable.colorfranja3_4,  R.drawable.colorfranja3_5,  R.drawable.colorfranja3_6,  R.drawable.colorfranja3_7,
	 R.drawable.colorfranja3_8,  R.drawable.colorfranja3_9,  R.drawable.colorfranja3_10,  R.drawable.colorfranja3_11,
	 R.drawable.colorfranja3_12,  R.drawable.colorfranja3_13,  R.drawable.colorfranja3_14,  R.drawable.colorfranja3_15,
	 R.drawable.colorfranja3_16,  R.drawable.colorfranja3_17,  R.drawable.colorfranja3_18,  R.drawable.colorfranja3_19,
	 R.drawable.colorfranja3_20,  R.drawable.colorfranja3_21,  R.drawable.colorfranja3_22,  R.drawable.colorfranja3_23,
	 R.drawable.colorfranja3_24	
};

public static final int[] coloresfranja3_2 ={R.drawable.colorfranjav3_1, R.drawable.colorfranjav3_2, R.drawable.colorfranjav3_3,
	R.drawable.colorfranjav3_4,R.drawable.colorfranjav3_5, R.drawable.colorfranjav3_6, R.drawable.colorfranjav3_7, R.drawable.colorfranjav3_8,
	R.drawable.colorfranjav3_9, R.drawable.colorfranjav3_10, R.drawable.colorfranjav3_11, R.drawable.colorfranjav3_12,
	R.drawable.colorfranjav3_13, R.drawable.colorfranjav3_14, R.drawable.colorfranjav3_15, R.drawable.colorfranjav3_16,
	R.drawable.colorfranjav3_17, R.drawable.colorfranjav3_18, R.drawable.colorfranjav3_19, R.drawable.colorfranjav3_20, R.drawable.colorfranjav3_21,
	R.drawable.colorfranjav3_22, R.drawable.colorfranjav3_23, R.drawable.colorfranjav3_24	
};

public static final int[] coloresfranja3y3_2 = { R.drawable.colorfranja3_1,  R.drawable.colorfranja3_2,  R.drawable.colorfranja3_3,
	 R.drawable.colorfranja3_4,  R.drawable.colorfranja3_5,  R.drawable.colorfranja3_6,  R.drawable.colorfranja3_7,
	 R.drawable.colorfranja3_8,  R.drawable.colorfranja3_9,  R.drawable.colorfranja3_10,  R.drawable.colorfranja3_11,
	 R.drawable.colorfranja3_12,  R.drawable.colorfranja3_13,  R.drawable.colorfranja3_14,  R.drawable.colorfranja3_15,
	 R.drawable.colorfranja3_16,  R.drawable.colorfranja3_17,  R.drawable.colorfranja3_18,
	 R.drawable.colorfranjav3_1, R.drawable.colorfranjav3_2, R.drawable.colorfranjav3_3,
	 R.drawable.colorfranjav3_4,R.drawable.colorfranjav3_5, R.drawable.colorfranjav3_6, R.drawable.colorfranjav3_7, R.drawable.colorfranjav3_8,
	 R.drawable.colorfranjav3_9, R.drawable.colorfranjav3_10, R.drawable.colorfranjav3_11, R.drawable.colorfranjav3_12,
	 R.drawable.colorfranjav3_13, R.drawable.colorfranjav3_14, R.drawable.colorfranjav3_15, R.drawable.colorfranjav3_16,
	 R.drawable.colorfranjav3_17 
};

public static final int coloresfranja4[] ={R.drawable.colorfranja4_1, R.drawable.colorfranja4_2, R.drawable.colorfranja4_3,
	R.drawable.colorfranja4_4, R.drawable.colorfranja4_5, R.drawable.colorfranja4_6, R.drawable.colorfranja4_7,
	R.drawable.colorfranja4_8, R.drawable.colorfranja4_9, R.drawable.colorfranja4_10, R.drawable.colorfranja4_11,
	R.drawable.colorfranja4_12,R.drawable.colorfranja4_13, R.drawable.colorfranja4_14, R.drawable.colorfranja4_15,
	R.drawable.colorfranja4_16, R.drawable.colorfranja4_17, R.drawable.colorfranja4_18, R.drawable.colorfranja4_19,
	R.drawable.colorfranja4_20, R.drawable.colorfranja4_21, R.drawable.colorfranja4_22, R.drawable.colorfranja4_23,
	R.drawable.colorfranja4_24	
};

public static final int[] colores1 ={R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4,
	R.drawable.color5, R.drawable.color6, R.drawable.color7, R.drawable.color8, R.drawable.color9, R.drawable.color10,
	R.drawable.color11, R.drawable.color12, R.drawable.color13, R.drawable.color14	};

//A: Por un lado usaremos un array para cuando haya que relacionar solo colores sin palabras:
public static final int[][] colores = {colores1, coloresnaranja, coloresrosas, coloresrojos, coloresverdes,
	coloresazules, coloresamarillos, coloresnegros, coloresfranja1h ,coloresfranja1, coloresfranja2h, coloresfranja2,
	coloresfranja3, coloresfranja3y3_2,	coloresfranja4};

//----------
public static final int[] colores_1 ={R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4,
	R.drawable.color5, R.drawable.color6, R.drawable.color7, R.drawable.color8, R.drawable.color9, R.drawable.color10,
	R.drawable.color11, R.drawable.color12, R.drawable.color13, R.drawable.color14	
};

public static final int[] colores_1_1 = {R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4,
	R.drawable.color5, R.drawable.color6, R.drawable.color7, R.drawable.color8, R.drawable.color9, R.drawable.color10,
	R.drawable.color11, R.drawable.color12, R.drawable.color13, R.drawable.color14		
};

public static final int[] colores_1_1_1 ={R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4,
	R.drawable.color5, R.drawable.color6, R.drawable.color7, R.drawable.color8, R.drawable.color9, R.drawable.color10,
	R.drawable.color11, R.drawable.color12, R.drawable.color13, R.drawable.color14			
};

public static final int[] colores_1_1_1_1 ={R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4,
	R.drawable.color5, R.drawable.color6, R.drawable.color7, R.drawable.color8, R.drawable.color9, R.drawable.color10,
	R.drawable.color11, R.drawable.color12, R.drawable.color13, R.drawable.color14		
};

public static final int[] coloresfranja1h_1 ={R.drawable.colorfranja1, R.drawable.colorfranja2, R.drawable.colorfranja3, 
	R.drawable.colorfranja4, R.drawable.colorfranja5, R.drawable.colorfranja6	
};

public static final int[] coloresfranja1v_1 = {R.drawable.colorfranjav1, R.drawable.colorfranjav2, R.drawable.colorfranjav3, R.drawable.colorfranjav4,
	R.drawable.colorfranjav5, R.drawable.colorfranjav6
};

public static final int[] coloresfranja2h_1={R.drawable.colorfranja2_1, R.drawable.colorfranja2_2,R.drawable.colorfranja2_3,
	R.drawable.colorfranja2_4, R.drawable.colorfranja2_5, R.drawable.colorfranja2_6	
};

public static final int[] coloresfranja2v_1 ={R.drawable.colorfranjav2_1, R.drawable.colorfranjav2_2,R.drawable.colorfranjav2_3,
	R.drawable.colorfranjav2_4, R.drawable.colorfranjav2_5, R.drawable.colorfranjav2_6	
};


public static final int[] coloresfranja_3 ={R.drawable.colorfranja3_1,  R.drawable.colorfranja3_2,  R.drawable.colorfranja3_3,
	 R.drawable.colorfranja3_4,  R.drawable.colorfranja3_5,  R.drawable.colorfranja3_6,  R.drawable.colorfranja3_7,
	 R.drawable.colorfranja3_8,  R.drawable.colorfranja3_9,  R.drawable.colorfranja3_10,  R.drawable.colorfranja3_11,
	 R.drawable.colorfranja3_12,  R.drawable.colorfranja3_13,  R.drawable.colorfranja3_14,  R.drawable.colorfranja3_15,
	 R.drawable.colorfranja3_16,  R.drawable.colorfranja3_17,  R.drawable.colorfranja3_18,  R.drawable.colorfranja3_19,
	 R.drawable.colorfranja3_20,  R.drawable.colorfranja3_21,  R.drawable.colorfranja3_22,  R.drawable.colorfranja3_23,
	 R.drawable.colorfranja3_24		
};


public static final int[] colores1palabras = {R.drawable.color1negro,R.drawable.color2blanco,
	R.drawable.color3fucsia, R.drawable.color4rojo, R.drawable.color5gris,
	R.drawable.color6naranja,R.drawable.color7amarillo,	R.drawable.color8marron,  R.drawable.color9verde, R.drawable.color10lila,
	R.drawable.color11azul,  R.drawable.color12rosa ,	R.drawable.color13salmon , R.drawable.color14morado			
};

public static final int[] colores1palabras_1 ={R.drawable.color1negro,R.drawable.color2blanco,
	R.drawable.color3fucsia, R.drawable.color4rojo, R.drawable.color5gris,
	R.drawable.color6naranja,R.drawable.color7amarillo,	R.drawable.color8marron,  R.drawable.color9verde, R.drawable.color10lila,
	R.drawable.color11azul,  R.drawable.color12rosa ,	R.drawable.color13salmon , R.drawable.color14morado		
};



public static final int[] colores2palabras={R.drawable.color1_pantera,R.drawable.color2_nieve,
	R.drawable.color3_pintalabios, R.drawable.color4_sangre, R.drawable.color5_ceniza,
	R.drawable.color6_calabaza, R.drawable.color7_maiz,	R.drawable.color8_cafe,  R.drawable.color9_rana, R.drawable.color14_flor,
	R.drawable.color11_cielo,  R.drawable.color12_lazo , R.drawable.color13_salmon , R.drawable.color10_berenjena		
};

public static final int[] colores2palabras_2 ={R.drawable.color1_pantera,R.drawable.color2_nieve,
	R.drawable.color3_pintalabios, R.drawable.color4_sangre, R.drawable.color5_ceniza,
	R.drawable.color6_calabaza, R.drawable.color7_maiz,	R.drawable.color8_cafe,  R.drawable.color9_rana, R.drawable.color14_flor,
	R.drawable.color11_cielo,  R.drawable.color12_lazo , R.drawable.color13_salmon , R.drawable.color10_berenjena	
};

public static final int[]colores1palagras1 ={R.drawable.color1_amar_naranj, R.drawable.color2_azul_blanco, 
	R.drawable.color3_fucs_marron, R.drawable.color4_gris_rosa, R.drawable.color5_morado_verde, R.drawable.color6_negro_rojo		
};

public static final int[] colores2palabras2 ={R.drawable.color1_naranj_amar, R.drawable.color2_blanco_azul,
	R.drawable.color3_marron_fucs, R.drawable.color4_rosa_gris, R.drawable.color5_verde_morado, R.drawable.color6_rojo_negro	
};

public static final int[] colores3palabras3 ={R.drawable.color1_amarill_marron, R.drawable.color2_azul_rosa, 
	R.drawable.color3_blanco_negro, R.drawable.color4_burdeos_salmon, R.drawable.color5_fucs_morado, 
	R.drawable.color6_gris_naranj, R.drawable.color7_rojo_verde, R.drawable.color8_lima_oliva	
};

public static final int[] colores4palabras4 ={R.drawable.color1_marron_amarill, R.drawable.color2_rosa_azul,
	R.drawable.color3_negro_blanco, R.drawable.color4_salm_burde, R.drawable.color5_morado_fucs,
	R.drawable.color6_naranj_gris, R.drawable.color7_verde_rojo, R.drawable.color8_oliva_lima	
};

public static final int[] colores5palabras5 ={R.drawable.color1_amar_naranj, R.drawable.color2_azul_blanco, 
	R.drawable.color3_fucs_marron, R.drawable.color4_gris_rosa, R.drawable.color5_morado_verde, R.drawable.color6_negro_rojo,
	R.drawable.color1_amarill_marron, R.drawable.color2_azul_rosa, 
	R.drawable.color3_blanco_negro, R.drawable.color4_burdeos_salmon, R.drawable.color5_fucs_morado, 
	R.drawable.color6_gris_naranj, R.drawable.color7_rojo_verde, R.drawable.color8_lima_oliva		
};

public static final int[] colores6palabras6 ={R.drawable.color1_naranj_amar, R.drawable.color2_blanco_azul,
	R.drawable.color3_marron_fucs, R.drawable.color4_rosa_gris, R.drawable.color5_verde_morado, R.drawable.color6_rojo_negro,	
	R.drawable.color1_marron_amarill, R.drawable.color2_rosa_azul,
	R.drawable.color3_negro_blanco, R.drawable.color4_salm_burde, R.drawable.color5_morado_fucs,
	R.drawable.color6_naranj_gris, R.drawable.color7_verde_rojo, R.drawable.color8_oliva_lima		
};



//B: Por otro lado gestionaremos array en caso de relacionar colores con palabras :

public static final int[][] coloresYpalabras1 = {colores_1, colores1palabras};
public static final int[][] coloresYpalabras2 = {colores_1_1, colores2palabras};
public static final int[][] coloresYpalabras3 =  {colores1palagras1, colores2palabras2};
public static final int[][] coloresYpalabras4 = {colores3palabras3, colores4palabras4};
public static final int[][] coloresypalabras5 = {colores5palabras5, colores6palabras6};
public static final int[][] coloresYfranjas1 ={coloresfranja1h_1, coloresfranja1v_1 };
public static final int[][] coloresYfranjas2 ={coloresfranja2h_1, coloresfranja2v_1};
public static final int[][] coloresYfranjas3 = {coloresfranja_3, coloresfranja3_2};
public static final int[][] coloresypalabras6 ={colores_1_1_1, colores1palabras_1};
public static final int[][] coloresypalabras7 ={colores_1_1_1_1, colores2palabras_2};

public static final int[][][] relac2 = {coloresYpalabras1, coloresYpalabras2, coloresYpalabras3,
	coloresYpalabras4, coloresypalabras5, coloresYfranjas1, coloresYfranjas2, coloresYfranjas3,coloresypalabras6 , coloresypalabras7};


// -----------IMAGENES MODO SIMON

public static final int[][] imagenesSimon = {
	{R.drawable.botonsimon1, R.drawable.botonsimon1_1}, {R.drawable.botonsimon2, R.drawable.botonsimon2_2},
	{R.drawable.botonsimon3, R.drawable.botonsimon3_3}, {R.drawable.botonsimon4, R.drawable.botonsimon4_4},
	{R.drawable.botonsimon5, R.drawable.botonsimon5_5}, {R.drawable.botonsimon6, R.drawable.botonsimon6_6},
	{R.drawable.botonsimon7, R.drawable.botonsimon7_7}, {R.drawable.botonsimon8, R.drawable.botonsimon8_8},
	{R.drawable.botonsimon9, R.drawable.botonsimon9_9}
};

}
