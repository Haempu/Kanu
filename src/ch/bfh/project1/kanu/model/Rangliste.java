package ch.bfh.project1.kanu.model;

import java.util.ArrayList;
import java.util.List;

public class Rangliste {
	/**
	 * Enth�lt die verschiedenen Ranglisten.
	 *
	 * @author Aebischer Patrik, B�siger Elia, Gestach Lukas
	 * @date 28.03.2017
	 * @version 1.0
	 *
	 */
	
	private Rennen rennen;
	private List<FahrerResultat> resultate = new ArrayList();
	
	public Rennen getRennen() {
		return rennen;
	}
	public void setRennen(Rennen rennen) {
		this.rennen = rennen;
	}
	public List<FahrerResultat> getResultate() {
		return resultate;
	}
	public void setResultate(List<FahrerResultat> resultate) {
		this.resultate = resultate;
	}
	
}
