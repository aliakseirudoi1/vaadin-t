package my.vaadin.app.views;

import java.util.List;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

import my.vaadin.app.form.HotelForm;
import my.vaadin.app.model.Hotel;
import my.vaadin.app.service.HotelService;
import ui.NavigatorUI;

public class HotelView extends VerticalLayout implements View {

	public static final String HOTELVIEW = "hotel";
	public static final String CATEGORYVIEW = "category";

	private HotelService service = HotelService.getHotelService();
	private Grid<Hotel> hotelGrid = new Grid<>();
	private TextField filterByName = new TextField();
	private TextField filterByAddress = new TextField();
	private HotelForm form = new HotelForm(this);
	private MenuBar menu = new MenuBar();

	Button deleteHotelButton = new Button("Delete hotel");
	Button newHotelButton = new Button("Add new hotel");

	public HotelView(String statusView) {

		CssLayout filteringByName = initFilterByName();
		CssLayout filteringByAddress = initFilterByAddress();

		HorizontalLayout statusLayoult = new HorizontalLayout();
		HorizontalLayout toolbarLayoult = new HorizontalLayout();
		HorizontalLayout menuLayout = new HorizontalLayout();
		HorizontalLayout mainLayoult = new HorizontalLayout();
		mainLayoult.setSizeFull();

		final Label status = new Label(statusView);

		Command command = new Command() {
			MenuItem previous = null;

			@Override
			public void menuSelected(MenuItem selectedItem) {

				if (selectedItem.getText().equalsIgnoreCase("Hotel")) {
					getUI().getNavigator().navigateTo(NavigatorUI.HOTELVIEW);
				}

				if (selectedItem.getText().equalsIgnoreCase("Category")) {
					getUI().getNavigator().navigateTo(NavigatorUI.CATEGORYVIEW);
				}

				if (previous != null) {
					previous.setStyleName(null);
				}
				selectedItem.setStyleName("highlight");
				previous = selectedItem;
			}
		};
		MenuItem hotelItem = menu.addItem("Hotel", VaadinIcons.BUILDING, command);
		MenuItem categoryItem = menu.addItem("Category", VaadinIcons.ACADEMY_CAP, command);
		menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);

		statusLayoult.addComponent(status);
		toolbarLayoult.addComponents(filteringByName, filteringByAddress, newHotelButton, deleteHotelButton);
		menuLayout.addComponent(menu);
		mainLayoult.addComponents(hotelGrid, form);

		hotelGrid.addColumn(Hotel::getName).setCaption("Name");
		hotelGrid.addColumn(Hotel::getAddress).setCaption("Address");
		hotelGrid.addColumn(Hotel::getStarRating).setCaption("Rating");
		hotelGrid.addColumn(Hotel::getCategory).setCaption("Category");
		hotelGrid.addColumn(
				hotel -> "<a href='https://" + hotel.getSite() + "' target='_blank'>" + hotel.getSite() + "</a>",
				new HtmlRenderer()).setCaption("Url");
		hotelGrid.addColumn(Hotel::getDescription).setCaption("Description");

		hotelGrid.setSizeFull();
		mainLayoult.setExpandRatio(hotelGrid, 1);

		// buttons
		newHotelButton.addClickListener(e -> {
			hotelGrid.asSingleSelect().clear();

			form.setHotel(new Hotel());
		});

		deleteHotelButton.addClickListener(e -> {
			Hotel delCandidate = hotelGrid.getSelectedItems().iterator().next();
			service.delete(delCandidate);
			// deleteHotelButton.setEnabled(false);
			updateList();
		});

		addComponents(menuLayout, statusLayoult, toolbarLayoult, mainLayoult);
		updateList();
		form.setVisible(false);

		hotelGrid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				form.setVisible(false);
			} else {
				form.setHotel(event.getValue());
			}
		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("Showing view: Hotel");

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
		hotelGrid.setItems(hotels);

	}

	public void updateByAddress() {
		List<Hotel> hotels = service.findAll(filterByAddress.getValue());
		hotelGrid.setItems(hotels);
	}

}
