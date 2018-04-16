package my.vaadin.app;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

@Theme("mytheme")
public class MyUI extends UI {
    
    private HotelService service = HotelService.getHotelService();
    private Grid<Hotel> grid = new Grid<>(Hotel.class);
    private TextField filterByName = new TextField();
    private TextField filterByAddress = new TextField();
    private HotelForm form = new HotelForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        CssLayout filteringByName = initFilterByName();
        CssLayout filteringByAddress = initFilterByAddress();
        

        Button newHotelButton = new Button("Add new hotel");
        newHotelButton.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setHotel(new Hotel());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filteringByName, filteringByAddress, newHotelButton);

        grid.setColumns("name", "address", "starRating", "category", "phone", "fax", "description");
        
        grid.addColumn(hotel ->
        "<a href='https://" + hotel.getSite() + "' target='_blank'>"+ hotel.getSite() +"</a>",
        new HtmlRenderer());
        
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);

        layout.addComponents(toolbar, main);

        updateList();

        setContent(layout);

        form.setVisible(false);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                form.setVisible(false);
            } else {
                form.setHotel(event.getValue());
            }
        });
    }

	private CssLayout initFilterByName() {
		filterByName.setPlaceholder("filter by name...");
        filterByName.addValueChangeListener(e -> updateList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);

        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> filterByName.clear());

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterByName, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		return filtering;
	}
	
	private CssLayout initFilterByAddress() {
		filterByAddress.setPlaceholder("filter by address...");
		filterByAddress.addValueChangeListener(e -> updateByAddress());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);

        Button clearFilterTextBtn = new Button(FontAwesome.TIMES);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> filterByAddress.clear());

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterByAddress, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		return filtering;
	}

    public void updateList() {
        List<Hotel> hotels = service.findAll(filterByName.getValue());
        grid.setItems(hotels);
    }
    
    public void updateByAddress() {
        List<Hotel> hotels = service.findAll(filterByAddress.getValue());
        grid.setItems(hotels);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
