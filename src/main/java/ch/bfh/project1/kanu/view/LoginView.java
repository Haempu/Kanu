package ch.bfh.project1.kanu.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
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

	// Membervariables
	private HorizontalLayout loginLayout = new HorizontalLayout();
	private HorizontalLayout logoutLayout = new HorizontalLayout();
	private TextField email = new TextField();
	private PasswordField password = new PasswordField();
	private Button loginButton = new Button("Anmelden");
	private Button logoutButton = new Button("Abmelden");
	private LoginController loginController;
	private StrukturView strukturView;

	/**
	 * Konstruktor: LoginView
	 * 
	 * @param strukturView
	 * @param loginController
	 */
	public LoginView(StrukturView strukturView, LoginController loginController) {
		this.strukturView = strukturView;
		this.loginController = loginController;
	}

	@Override
	public void viewInitialisieren() {
		this.email.setInputPrompt("E-Mail");
		this.password.setInputPrompt("Passwort");

		this.loginLayout.addComponent(this.email);
		this.loginLayout.addComponent(this.password);
		this.loginLayout.addComponent(this.loginButton);
		this.loginLayout.setSpacing(true);
		setEventOnLogin();
		this.loginLayout.setStyleName("login");

		this.logoutLayout.addComponent(this.logoutButton);
		setEventOnLogout();
		this.logoutLayout.setStyleName("login");

		this.email.setImmediate(true);
		this.password.setImmediate(true);

	}

	/**
	 * Funktion setzt was passieren soll wenn man auf den login Button klickt
	 */
	private void setEventOnLogin() {
		this.loginButton.setClickShortcut(KeyCode.ENTER);
		this.loginButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
	}

	/**
	 * Funktion setzt was passieren soll wenn man auf den Logout Button klickt
	 */
	private void setEventOnLogout() {
		this.logoutButton.setClickShortcut(KeyCode.ENTER);
		this.logoutButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
	}

	@Override
	public void viewAnzeigen(Component inhalt) {
		HorizontalLayout neuerInhalt = (HorizontalLayout) inhalt;
		// TODO: ist der benutzer eingeloggt?
		// if (!this.loginController.loginAufSession()) {
		neuerInhalt.addComponent(this.loginLayout);
		// } else {
		// inhalt.addComponent(this.logoutLayout);
		// }
	}

}
