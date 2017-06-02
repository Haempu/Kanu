package ch.bfh.project1.kanu.view;

import ch.bfh.project1.kanu.controller.LoginController;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 * 
 */

public class LoginView implements ViewTemplate {

	private boolean init = false; // Ist die View initialisiert

	// UI Komponenten
	private FormLayout loginLayout = new FormLayout();
	private Label titel = new Label("Login");
	private TextField email = new TextField("E-Mail Adresse");
	private PasswordField passwort = new PasswordField("Passwort");
	private Button loginButton = new Button("Anmelden");

	// Controller
	private LoginController loginController;

	/**
	 * Konstruktor: LoginView
	 * 
	 * @param strukturView
	 * @param loginController
	 */
	public LoginView(LoginController loginController, StrukturView strukturView) {
		this.loginController = loginController;
	}

	/**
	 * Die Funktion initialisiert die View
	 */
	@Override
	public void viewInitialisieren() {
		this.loginLayout.setSpacing(true);
		this.titel.setStyleName("h2");

		this.email.setInputPrompt("E-Mail");
		this.passwort.setInputPrompt("Passwort");

		// Komponenten zum Layout hinzufügen
		this.loginLayout.addComponent(this.titel);
		this.loginLayout.addComponent(this.email);
		this.loginLayout.addComponent(this.passwort);
		this.loginLayout.addComponent(this.loginButton);
		this.loginLayout.setSpacing(true);

		setEventOnLogin();

		this.email.setImmediate(true);
		this.passwort.setImmediate(true);

		this.init = true;
	}

	/**
	 * Die Funktion zeigt die View an.
	 */
	@Override
	public void viewAnzeigen(Component inhalt) {
		Panel inhaltsPanel = (Panel) inhalt;
		inhaltsPanel.setContent(this.loginLayout);
	}

	/**
	 * Funktion setzt was passieren soll wenn man auf den login Button klickt
	 */
	private void setEventOnLogin() {
		this.loginButton.setClickShortcut(KeyCode.ENTER);
		this.loginButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -3024525327492473410L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (LoginView.this.loginController.loginMitBenutzer(email.getValue(), passwort.getValue())) {
					Page.getCurrent().reload();
				} else {
					Notification.show("Login fehlgeschlagen. Bitte versuchen Sie es erneut.", Type.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Funktion gibt zurück ob die View bereits initialisiert wurde.
	 * 
	 * @return
	 */
	@Override
	public boolean istInitialisiert() {
		return this.init;
	}

}
