package my.vaadin.app.views;

import java.util.List;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

import my.vaadin.app.form.CategoryForm;
import my.vaadin.app.form.HotelForm;
import my.vaadin.app.model.Category;
import my.vaadin.app.model.Hotel;
import my.vaadin.app.service.CategoryService;
import my.vaadin.app.service.HotelService;
import ui.NavigatorUI;

public class CategoryView extends VerticalLayout implements View {

	public static final String HOTELVIEW = "hotel";
	public static final String CATEGORYVIEW = "category";

	private CategoryService service = CategoryService.getCategoryService();
	private Grid<Category> categoryGrid = new Grid<>();
	private MenuBar menu = new MenuBar();
	private CategoryForm form = new CategoryForm(this);

	Button deleteCategoryButton = new Button("Delete category");
	Button newCategoryButton = new Button("Add new category");

	public CategoryView(String statusView) {

		HorizontalLayout menuLayout = new HorizontalLayout();
		HorizontalLayout statusLayoult = new HorizontalLayout();
		HorizontalLayout mainLayoult = new HorizontalLayout();
		HorizontalLayout toolbarLayoult = new HorizontalLayout();
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

		menuLayout.addComponents(menu);
		statusLayoult.addComponent(status);

		newCategoryButton.addClickListener(e -> {
			categoryGrid.asSingleSelect().clear();
			form.setCategory(new Category());
		});

		deleteCategoryButton.addClickListener(e -> {
			Category delCandidate = categoryGrid.getSelectedItems().iterator().next();
			service.delete(delCandidate);
			// deleteHotelButton.setEnabled(false);
			updateList();
		});

		categoryGrid.addColumn(Category::getName).setCaption("Name");
		mainLayoult.addComponents(categoryGrid, form);
		categoryGrid.setSizeFull();
		mainLayoult.setExpandRatio(categoryGrid, 1);

		toolbarLayoult.addComponents(newCategoryButton, deleteCategoryButton);
		addComponents(menuLayout, statusLayoult, toolbarLayoult, mainLayoult);
		updateList();
		form.setVisible(false);

		categoryGrid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				form.setVisible(false);
			} else {
				form.setCategory(event.getValue());
			}
		});

	}

	public void updateList() {
		List<Category> categories = service.findAll();
		categoryGrid.setItems(categories);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("Showing view: Category");
	}

}
