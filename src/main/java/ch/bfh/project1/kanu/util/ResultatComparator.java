package ch.bfh.project1.kanu.util;

import java.util.Comparator;

import ch.bfh.project1.kanu.model.FahrerResultat;

public class ResultatComparator implements Comparator<FahrerResultat> {

	public int compare(FahrerResultat o, FahrerResultat q) {
		//-1 wenn o kleiner als q --> wenn o schneller war als q
		//+1 wenn o grösser als q --> wenn o langsamer war als q
		Integer b = o.getZeitTotal();
		Integer a = q.getZeitTotal();
		if(a == b)
			return 0;
		if(a < b)
			return 1;
		return -1;
	}

}
