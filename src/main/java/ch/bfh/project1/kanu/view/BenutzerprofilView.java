package ch.bfh.project1.kanu.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import ch.bfh.project1.kanu.controller.BenutzerprofilController;
import ch.bfh.project1.kanu.controller.SessionController;
import ch.bfh.project1.kanu.controller.ValidierungsController;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */

public class BenutzerprofilView implements ViewTemplate {

	// UI Komponenten
	private Label titel = new Label("Benutzerprofil");
	private TextField email = new TextField("Email-Adresse");
	private PasswordField altesPasswort = new PasswordField("Altes Passwort");
	private PasswordField neuesPasswort = new PasswordField("Neues Passwort");
	private PasswordField passwortBestaetigen = new PasswordField("Passwort bestätigen");
	private Button speichern = new Button("Speichern");
	private FormLayout benutzerProfilLayout = new FormLayout();
	private boolean init = false;

	// Kontroller
	private BenutzerprofilController bController = new BenutzerprofilController();

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.benutzerProfilLayout.setSpacing(true);
		this.titel.setStyleName("h2");

		this.email.setInputPrompt("Email-Adresse");
		this.altesPasswort.setInputPrompt("Passwort");
		this.neuesPasswort.setInputPrompt("Passwort");
		this.passwortBestaetigen.setInputPrompt("Passwort");

		ValidierungsController.setTextFeldRequired(this.email);
		ValidierungsController.checkIfEmail(this.email);
		ValidierungsController.checkIfPasswordIsEqualWithRepliedPassword(this.neuesPasswort, this.passwortBestaetigen);

		this.email.setValue(SessionController.getBenutzerEmail());

		this.speichern.addClickListener(event -> {
			if ((this.neuesPasswort.getValue() == null || this.neuesPasswort.getValue().equals(""))
					&& (this.passwortBestaetigen.getValue() == null
							|| this.passwortBestaetigen.getValue().equals(""))) {
				if (this.email.isValid()) {
					this.bController.emailAendern(SessionController.getBenutzerID(), this.email.getValue());
				}
			} else if (this.neuesPasswort.isValid() && this.altesPasswort.isValid()
					&& this.passwortBestaetigen.isValid()) {
				if (!this.bController.benutzerprofilAendern(SessionController.getBenutzerID(), this.email.getValue(),
						this.neuesPasswort.getValue(), this.neuesPasswort.getValue(),
						this.passwortBestaetigen.getValue())) {

				}
			}
		});

		this.benutzerProfilLayout.addComponent(this.titel);
		this.benutzerProfilLayout.addComponent(this.email);
		this.benutzerProfilLayout.addComponent(this.altesPasswort);
		this.benutzerProfilLayout.addComponent(this.neuesPasswort);
		this.benutzerProfilLayout.addComponent(this.passwortBestaetigen);
		this.benutzerProfilLayout.addComponent(this.speichern);

		this.init = true;
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(this.benutzerProfilLayout);
	}

	@Override
	public boolean istInitialisiert() {
		return this.init;
	}
}
