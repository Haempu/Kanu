package ch.bfh.project1.kanu.util;

import java.util.Comparator;

import ch.bfh.project1.kanu.model.FahrerResultat;

public class ResultatComparator implements Comparator<FahrerResultat> {

	public int compare(FahrerResultat o, FahrerResultat q) {
		Integer b = Math.min(o.getZeitErsterLauf(), o.getZeitZweiterLauf()); //TODO 0 ist als unendlich zu gewichten!
		Integer a = Math.min(q.getZeitErsterLauf(), q.getZeitZweiterLauf());
		if(a == 0)
			return -1;
		if(b == 0)
			return 1;
		if(a == b)
			return 0;
		if(a < b)
			return 1;
		return -1;
	}

}
