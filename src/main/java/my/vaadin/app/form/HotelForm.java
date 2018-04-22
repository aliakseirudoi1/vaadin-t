package my.vaadin.app.form;

import java.text.Normalizer.Form;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import my.vaadin.app.model.Category;
import my.vaadin.app.model.Hotel;
import my.vaadin.app.model.HotelCategory;
import my.vaadin.app.service.CategoryService;
import my.vaadin.app.service.HotelService;
import my.vaadin.app.views.HotelView;

public class HotelForm extends FormLayout {

	private TextField name = new TextField("Hotel name");
	private TextField address = new TextField("Address");
	private TextField starRating = new TextField("StarRating");
	private NativeSelect<Category> category = new NativeSelect<>("Category");
	private TextField phone = new TextField("Phone");

	private TextField fax = new TextField("Fax");
	private TextField site = new TextField("Site");
	private TextArea description = new TextArea("Description");

	private Button save = new Button("Save");
	private Button close = new Button("Close");

	private HotelService service = HotelService.getHotelService();
	private Hotel hotel;
	private HotelView hotelView;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);

	public HotelForm(HotelView hotelView) {
		this.hotelView = hotelView;

		name.setDescription("enter name");
		address.setDescription("enter address");
		starRating.setDescription("enter rating");
		category.setDescription("enter category");
		phone.setDescription("enter phone");
		fax.setDescription("enter fax");
		site.setDescription("enter url");
		description.setDescription("enter description");

		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, close);
		addComponents(name, address, starRating, category, phone, fax, site, description, buttons);

		System.out.println(111111);
		category.setItems(CategoryService.getCategoryService().findAll());
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);

		save.addClickListener(e -> this.save());
		close.addClickListener(e -> this.exit());
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.setBean(hotel);
		close.setVisible(hotel.isPersisted());
		setVisible(true);
		name.selectAll();

	}

	private void exit() {
		hotelView.updateList();
		setVisible(false);
	}

	public void deeteHotel(Hotel hotel) {
		this.hotel = hotel;
		service.delete(hotel);
		hotelView.updateList();
		setVisible(false);
	}

	private void save() {
		if (binder.isValid()) {
			try {
				binder.writeBean(hotel);
			} catch (ValidationException e) {
				Notification.show("unable to save: " + e.getMessage(), Type.HUMANIZED_MESSAGE);
			}

			service.save(hotel);
			hotelView.updateList();
			setVisible(false);
		}
	}

	public void prepareFields() {
		binder.forField(name).asRequired("enter name").bind(Hotel::getName, Hotel::setName);
		Validator<String> addressValidator = new Validator<String>() {

			@Override
			public ValidationResult apply(String value, ValueContext context) {
				if (value != null || value.isEmpty()) {
					return ValidationResult.error("The address is empty");
				}
				if (value.length() < 5) {
					return ValidationResult.error("The address is too short");
				}
				return ValidationResult.ok();
			}
		};

		binder.forField(address).withValidator(addressValidator).bind(Hotel::getAddress, Hotel::setAddress);
		binder.forField(starRating).withConverter(new StringToIntegerConverter("Please enter a number"))
				.bind(Hotel::getStarRating, Hotel::setStarRating);
		// cbinder.forField(starRating).bind(Hotel::getStarRating,
		// Hotel::setStarRating);
		binder.forField(category).bind(Hotel::getCategory, Hotel::setCategory);
		binder.forField(phone).bind(Hotel::getPhone, Hotel::setPhone);
		binder.forField(fax).bind(Hotel::getFax, Hotel::setFax);
		binder.forField(site).bind(Hotel::getSite, Hotel::setSite);
		binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
	}
}
