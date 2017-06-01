package ch.bfh.project1.kanu.view;

import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import org.vaadin.teemusa.sidemenu.SideMenu;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

import ch.bfh.project1.kanu.controller.DBController;
import ch.bfh.project1.kanu.controller.LoginController;
import ch.bfh.project1.kanu.controller.SessionController;
import ch.bfh.project1.kanu.model.Benutzer.BenutzerRolle;
import ch.bfh.project1.kanu.model.Rennen;

/**
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 19.04.2017
 * @version 1.0
 *
 */

@Theme("mytheme")
public class StrukturView extends UI {

	// UI Komponenten
	private GridLayout seite = new GridLayout(1, 1);
	private Label logo = new Label("Kanu");
	private Panel inhaltPanel = new Panel();
	private SideMenu menu = new SideMenu();

	// Controller
	private LoginController loginController = new LoginController();

	// Alle Views
	private LoginView loginView = new LoginView(this.loginController, this);
	private BenutzerprofilView benutzerprofilView = new BenutzerprofilView();
	private RechnungsView rechnungsView = new RechnungsView();
	private FahreranmeldungsView fahreranmeldungsView = new FahreranmeldungsView(this);
	private FehlererfassungsView fehlererfassungsView = new FehlererfassungsView(this);
	private ZeiterfassungsView zeiterfassungsView = new ZeiterfassungsView(this);
	private MutationsView mutationsView = new MutationsView(this);
	private RennVerwaltungsView rennverwaltungsView = new RennVerwaltungsView(this);
	private StartlistenView slView = new StartlistenView(this);
	private RanglistenView rlView = new RanglistenView();

	@Override
	protected void init(VaadinRequest request) {

		this.getPage().setTitle("Kanu Club Grenchen");

		DBController db = DBController.getInstance();
		try {
			db.connect();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		this.inhaltPanel.setStyleName(Reindeer.PANEL_LIGHT);
		this.inhaltPanel.setSizeFull();
		this.inhaltPanel.addStyleName("inhaltPanel");
		this.inhaltPanel.setImmediate(true);
		this.logo.addStyleName("logo");

		if (this.loginController.loginAufSession()) {
			zeigeEingeloggtesMenu(null); // TODO: add benutzerrolle
		} else {
			zeigeAusgeloggtesMenu();
		}

		this.seite.setImmediate(true);
		this.seite.addComponent(this.inhaltPanel, 0, 0);
		this.seite.setComponentAlignment(this.inhaltPanel, Alignment.TOP_LEFT);
		this.seite.setRowExpandRatio(0, 0);

		this.seite.setSizeFull();
		this.menu.setImmediate(true);
		this.setContent(this.menu);
	}

	/**
	 * Funktion setzt das Menu für den eingeloggten Benutzer
	 * 
	 * @param benutzername
	 * @param bild
	 */
	private void setEingeloggterBenutzer(String benutzername) {
		this.menu.setUserName(benutzername);
		this.menu.setUserIcon(FontAwesome.MALE);

		this.benutzerprofilView.viewInitialisieren();

		this.menu.clearUserMenu();
		this.menu.addUserMenuItem("Benutzerprofil", FontAwesome.WRENCH, () -> {
			this.benutzerprofilView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addUserMenuItem("Abmelden", () -> {
			this.loginController.abmelden();
			// zeigeAusgeloggtesMenu();
			Page.getCurrent().reload();
		});
	}

	/**
	 * Funktion zeigt das ausgeloggte Menu
	 */
	public void zeigeAusgeloggtesMenu() {
		this.menu.removeAllComponents();
		this.menu.setMenuCaption("Kanu Club Grenchen");

		this.loginView.viewInitialisieren();
		this.loginView.viewInitialisieren();

		this.menu.addMenuItem("Login", () -> {
			this.loginView.viewAnzeigen(this.inhaltPanel);
		});

		// default view
		this.loginView.viewAnzeigen(this.inhaltPanel);
		this.menu.setContent(this.seite);
	}

	/**
	 * Funktion zeigt das für die Benutzerrolle entsprechende Menu.
	 * 
	 * @param benutzerRolle
	 */
	public void zeigeEingeloggtesMenu(BenutzerRolle benutzerRolle) {
		this.menu.removeAllComponents();
		this.menu.setMenuCaption("Kanu Club Grenchen");

		setEingeloggterBenutzer(SessionController.getBenutzerEmail());

		// TODO: menu nach benutzerrolle anzeigen
		this.menu.addMenuItem("Fahrer verwalten ", () -> {
			if (!this.mutationsView.istInitialisiert()) {
				this.mutationsView.viewInitialisieren();
			}
			this.mutationsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Fehler erfassen", () -> {
			if (!this.fehlererfassungsView.istInitialisiert()) {
				this.fehlererfassungsView.viewInitialisieren();
			}
			this.fehlererfassungsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Fahrer anmelden ", () -> {
			if (!this.fahreranmeldungsView.istInitialisiert()) {
				this.fahreranmeldungsView.viewInitialisieren();
			}
			this.fahreranmeldungsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Rechnungen verwalten ", () -> {
			if (!this.rechnungsView.istInitialisiert()) {
				this.rechnungsView.viewInitialisieren();
			}
			this.rechnungsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Rennen verwalten ", () -> {
			if (!this.rennverwaltungsView.istInitialisiert()) {
				this.rennverwaltungsView.viewInitialisieren();
			}
			this.rennverwaltungsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Startlisten verwalten ", () -> {
			this.slView.setRennen(null);
			if (!this.slView.istInitialisiert()) {
				this.slView.viewInitialisieren();
			}
			this.slView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Zeiterfassung ", () -> {
			if (!this.zeiterfassungsView.istInitialisiert()) {
				this.zeiterfassungsView.viewInitialisieren();
			}
			this.zeiterfassungsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Rangliste ", () -> {
			if (!this.rlView.istInitialisiert()) {
				rlView.setRennen(new Rennen(2)); // TODO weg
				this.rlView.viewInitialisieren();
			}
			this.rlView.viewAnzeigen(this.inhaltPanel);
		});

		// default view
		if (!this.mutationsView.istInitialisiert()) {
			this.mutationsView.viewInitialisieren();
		}
		this.mutationsView.viewAnzeigen(this.inhaltPanel);
		this.menu.setContent(this.seite);
	}

	/**
	 * Servlet: Is used to set to the start view.
	 */
	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = StrukturView.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
