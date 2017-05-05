package ch.bfh.project1.kanu.view;

import javax.servlet.annotation.WebServlet;

import org.vaadin.teemusa.sidemenu.SideMenu;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

import ch.bfh.project1.kanu.controller.LoginController;

/**
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas
 * @date 19.04.2017
 * @version 1.0
 *
 */

@Theme("mytheme")
public class StrukturView extends UI {

	// member variables
	private GridLayout seite = new GridLayout(1, 1);
	private Label logo = new Label("Kanu");
	private Panel inhaltPanel = new Panel();
	private SideMenu menu = new SideMenu();

	private LoginController loginController = new LoginController();

	private LoginView loginView = new LoginView(this.loginController);
	private BenutzerprofilView benutzerprofilView = new BenutzerprofilView();
	private RechnungsView rechnungsView = new RechnungsView();
	private FahreranmeldungsView fahreranmeldungsView = new FahreranmeldungsView();
	private FehlererfassungsView fehlererfassungsView = new FehlererfassungsView(this);
	private ZeiterfassungsView zeiterfassungsView = new ZeiterfassungsView(this);
	private MutationsView mutationsView = new MutationsView(this);

	@Override
	protected void init(VaadinRequest request) {

		this.getPage().setTitle("Kanu Club Grenchen");

		this.inhaltPanel.setStyleName(Reindeer.PANEL_LIGHT);
		this.inhaltPanel.setSizeFull();
		this.inhaltPanel.addStyleName("inhaltPanel");
		this.inhaltPanel.setImmediate(true);
		this.logo.addStyleName("logo");

		// TODO: init views & menu
		// TODO: inhalt setzen login/logout

		this.seite.setImmediate(true);
		this.seite.addComponent(this.inhaltPanel, 0, 0);
		this.seite.setComponentAlignment(this.inhaltPanel, Alignment.TOP_LEFT);
		this.seite.setRowExpandRatio(0, 0);

		this.seite.setSizeFull();

		// default view
		this.loginView.viewInitialisieren();
		this.loginView.viewAnzeigen(this.inhaltPanel);

		this.menu.setMenuCaption("Kanu Club Grenchen");

		// TODO: change elia.b@gawnet.ch to SessionController.getBenutzername();
		setEingeloggterBenutzer("elia.b@gawnet.ch");

		// TODO: menu nach benutzerrolle anzeigen und login/logout
		this.menu.addMenuItem("Fahrer verwalten ", () -> {
			this.mutationsView.viewInitialisieren();
			this.mutationsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Fehler erfassen", () -> {
			this.fehlererfassungsView.viewInitialisieren();
			this.fehlererfassungsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Fahrer anmelden ", () -> {
			this.fahreranmeldungsView.viewInitialisieren();
			this.fahreranmeldungsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Rechnungen verwalten ", () -> {
			this.rechnungsView.viewInitialisieren();
			this.rechnungsView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addMenuItem("Login", () -> {
			this.loginView.viewInitialisieren();
			this.loginView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.setContent(this.seite);
		this.setContent(this.menu);
	}

	/**
	 * Funktion setzt das Menu für den eingeloggten Benutzer
	 * 
	 * @param benutzername
	 * @param bild
	 */
	public void setEingeloggterBenutzer(String benutzername) {
		this.menu.setUserName(benutzername);
		this.menu.setUserIcon(FontAwesome.MALE);

		this.menu.clearUserMenu();
		this.menu.addUserMenuItem("Benutzerprofil", FontAwesome.WRENCH, () -> {
			this.benutzerprofilView.viewInitialisieren();
			this.benutzerprofilView.viewAnzeigen(this.inhaltPanel);
		});

		this.menu.addUserMenuItem("Abmelden", () -> {
			// TODO: abmelden
			Notification.show("Abmelden..", Type.TRAY_NOTIFICATION);
		});
	}

	/**
	 * Servlet: Is used to set to the start view.
	 */
	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = StrukturView.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
