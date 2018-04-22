package my.vaadin.app.form;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import my.vaadin.app.model.Category;
import my.vaadin.app.model.Hotel;
import my.vaadin.app.service.CategoryService;
import my.vaadin.app.service.HotelService;
import my.vaadin.app.views.CategoryView;
import my.vaadin.app.views.HotelView;

public class CategoryForm extends FormLayout {

	private TextField name = new TextField("Category name");

	private CategoryService service = CategoryService.getCategoryService();
	private Category category;
	private CategoryView categoryView;
	private Binder<Category> binder = new Binder<>(Category.class);

	private Button save = new Button("Save");
	private Button close = new Button("Close");

	public CategoryForm(CategoryView categoryView) {
		this.categoryView = categoryView;

		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, close);
		addComponents(name, buttons);

		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);

		binder.bindInstanceFields(this);

		save.addClickListener(e -> this.save());
		close.addClickListener(e -> this.exit());
	}

	public void setCategory(Category category) {
		this.category = category;
		binder.setBean(category);
		close.setVisible(category.isPersisted());
		setVisible(true);
		name.selectAll();
	}

	private void exit() {
		categoryView.updateList();
		setVisible(false);
	}

	public void deleteCategory(Category category) {
		this.category = category;
		service.delete(category);
		categoryView.updateList();
		setVisible(false);
	}

	private void save() {
		if (binder.isValid()) {
			try {
				binder.writeBean(category);
			} catch (ValidationException e) {
				Notification.show("unable to save: " + e.getMessage(), Type.HUMANIZED_MESSAGE);
			}

			service.save(category);
			categoryView.updateList();
			setVisible(false);
		}
	}

	public void prepareFields() {
		binder.forField(name).asRequired("enter name").bind(Category::getName, Category::setName);
	}

}
