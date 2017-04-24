package ch.bfh.project1.kanu.view;

import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.responsive.Responsive;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
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
	private GridLayout seite = new GridLayout(2, 3);
	private Label logo = new Label("Kanu");
	private HorizontalLayout header = new HorizontalLayout();
	private HorizontalLayout mitte = new HorizontalLayout();
	private MenuBar menu = new MenuBar();
	private Panel inhaltPanel = new Panel();
	private HorizontalLayout loginLogoutPart = new HorizontalLayout();

	private LoginController loginController = new LoginController();

	private LoginView loginView = new LoginView(this, loginController);

	@Override
	protected void init(VaadinRequest request) {

		this.inhaltPanel.setStyleName(Reindeer.PANEL_LIGHT);
		this.inhaltPanel.setSizeFull();
		this.inhaltPanel.addStyleName("inhaltPanel");
		this.inhaltPanel.setImmediate(true);
		this.logo.addStyleName("logo");

		// TODO: init views & menu
		// TODO: inhalt setzen login/logout

		this.seite.setImmediate(true);
		this.loginView.viewInitialisieren();
		this.loginView.viewAnzeigen(this.loginLogoutPart);

		this.seite.addComponent(this.logo, 0, 0);
		this.seite.setComponentAlignment(this.logo, Alignment.MIDDLE_LEFT);
		this.seite.addComponent(this.loginLogoutPart, 1, 0);
		this.seite.setComponentAlignment(this.loginLogoutPart, Alignment.MIDDLE_RIGHT);
		this.seite.setRowExpandRatio(0, 0);

		this.seite.addComponent(this.menu, 0, 1, 1, 1);
		this.seite.setComponentAlignment(this.menu, Alignment.TOP_LEFT);

		this.seite.addComponent(this.inhaltPanel, 0, 2, 1, 2);
		this.seite.setComponentAlignment(this.inhaltPanel, Alignment.TOP_LEFT);
		this.seite.setRowExpandRatio(2, 10);

		this.seite.setSizeFull();
		this.setContent(this.seite);

		new Responsive(this.logo);
	}

	/**
	 * Servlet: Is used to set to the start view.
	 */
	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = StrukturView.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
