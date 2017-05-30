package ch.bfh.project1.kanu.controller;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

/**
 * Die Klasse ValidierungsController beinhaltet die Validierung aller
 * GUI-Komponenten.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 11.04.2017
 * @version 1.0
 *
 */
public class ValidierungsController {

	/**
	 * Funktion überprüft ob der Wert des Textfeldes leer ist oder nicht
	 *
	 * @param textfield
	 *            - Zu überprüfendes Textfeld
	 */
	public static void setTextFeldRequired(TextField textfield) {
		textfield.setRequired(true);
		textfield.setRequiredError("Das Feld '" + textfield.getCaption() + "' darf nicht leer sein!");
	}

	/**
	 * Funktion überprüft ob der Wert des Passwortfeldes leer ist oder nicht
	 *
	 * @param passwordField
	 *            - Zu überprüfendes Passwortfeld
	 */
	public static void setPasswordFielRequired(PasswordField passwordField) {
		passwordField.setRequired(true);
		passwordField.setRequiredError("Das Feld '" + passwordField.getCaption() + "' darf nicht leer sein!");
	}

	/**
	 * Funktion überprüft ob der eingegebene Text eine gültige Email-Adresse ist
	 *
	 * @param textfield
	 *            - Zu überprüfendes Textfeld
	 */
	public static void checkIfEmail(TextField textfield) {
		textfield.addValidator(new EmailValidator("Ungültige E-Mail Adresse"));
	}

	/**
	 * Funktion überprüft ob das Passwort gleich ist wie das wiederholte
	 * Passwort.
	 *
	 * @param passwordField
	 * @param repliedPasswordField
	 */
	public static void checkIfPasswordIsEqualWithRepliedPassword(PasswordField passwordField,
			PasswordField repliedPasswordField) {

		repliedPasswordField.addValidator(new Validator() {

			@Override
			public void validate(Object value) throws InvalidValueException {
				if (!value.equals(passwordField.getValue())) {
					throw new InvalidValueException("Passwort stimmt nicht überein!");
				}
			}
		});
	}

	/**
	 * Funktion überprüft ob das Textfeld ein gültiger Integer ist.
	 * 
	 * @param textfield
	 */
	public static void checkIfInteger(TextField textfield) {
		textfield.addValidator(new Validator() {

			@Override
			public void validate(Object value) throws InvalidValueException {
				try {
					Integer.parseInt(textfield.getValue());
				} catch (NumberFormatException | NullPointerException e) {
					throw new InvalidValueException("Jahrgang muss eine Zahl sein");

				}
			}
		});

	}
}
