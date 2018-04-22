package ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import my.vaadin.app.views.CategoryView;
import my.vaadin.app.views.HotelView;

public class NavigatorUI extends UI{

	public Navigator navigator;

	public static final String HOTELVIEW = "hotel";
	public static final String CATEGORYVIEW = "category";
	
	@Override
	protected void init(VaadinRequest request) {

		
		// Create a navigator to control the views
        navigator = new Navigator(this, this);

        // Create and register the views
        navigator.addView("", new HotelView("You clicked item: Hotel view"));
        navigator.addView(HOTELVIEW, new HotelView("You clicked item: Hotel view"));
        navigator.addView(CATEGORYVIEW, new CategoryView("You clicked item: Category view"));
		
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NavigatorUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
	
}
