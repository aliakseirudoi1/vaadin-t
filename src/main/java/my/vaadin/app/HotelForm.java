package my.vaadin.app;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class HotelForm extends FormLayout {

    private TextField name = new TextField("Hotel name");
    private TextField address = new TextField("Address");
    private TextField starRating = new TextField("StarRating");
    private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
    private TextField phone = new TextField("Phone");
    private TextField fax = new TextField("Fax");
    private TextField site = new TextField("Site");
    private TextArea description = new TextArea("Description");
    
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private HotelService service = HotelService.getHotelService();
    private Hotel hotel;
    private MyUI myUI;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);

    public HotelForm(MyUI myUI) {
        this.myUI = myUI;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        addComponents(name, address, starRating, category, phone, fax, site, description, buttons);

        category.setItems(HotelCategory.values());
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> this.save());
        delete.addClickListener(e -> this.delete());
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        binder.setBean(hotel);
        delete.setVisible(hotel.isPersisted());
        setVisible(true);
        name.selectAll();
    }

    private void delete() {
        service.delete(hotel);
        myUI.updateList();
        setVisible(false);
    }

    private void save() {
        service.save(hotel);
        myUI.updateList();
        setVisible(false);
    }
}
