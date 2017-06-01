package ch.bfh.project1.kanu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse Rangliste beinhaltet alle Instanzen der Klasse FahrerResultat
 * eines Rennens.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 28.03.2017
 * @version 1.0
 *
 */

public class Rangliste {
	private Rennen rennen;
	private List<FahrerResultat> resultate = new ArrayList<FahrerResultat>();

	public Rennen getRennen() {
		return this.rennen;
	}

	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}

	public List<FahrerResultat> getResultate() {
		return this.resultate;
	}

	public void setResultate(List<FahrerResultat> resultate) {
		this.resultate = resultate;
	}

}