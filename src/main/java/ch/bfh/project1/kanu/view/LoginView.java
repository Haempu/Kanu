package ch.bfh.project1.kanu.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import ch.bfh.project1.kanu.controller.LoginController;

/**
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 * 
 */

public class LoginView implements ViewTemplate {

	// UI Komponenten
	private FormLayout loginLayout = new FormLayout();
	private Label titel = new Label("Login");
	private TextField email = new TextField("E-Mail Adresse");
	private PasswordField password = new PasswordField("Passwort");
	private Button loginButton = new Button("Anmelden");

	// Controller
	private LoginController loginController;

	/**
	 * Konstruktor: LoginView
	 * 
	 * @param strukturView
	 * @param loginController
	 */
	public LoginView(LoginController loginController) {
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
		this.password.setInputPrompt("Passwort");

		this.loginLayout.addComponent(this.titel);
		this.loginLayout.addComponent(this.email);
		this.loginLayout.addComponent(this.password);
		this.loginLayout.addComponent(this.loginButton);
		this.loginLayout.setSpacing(true);

		setEventOnLogin();

		this.email.setImmediate(true);
		this.password.setImmediate(true);
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

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO: loginButton logik
			}
		});
	}

}
