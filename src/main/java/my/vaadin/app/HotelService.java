package my.vaadin.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;

public class HotelService {

	private static HotelService service;
	private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());

	private final HashMap<Long, Hotel> hotelsMap = new HashMap<>();
	private long nextId = 0;

	private HotelService() {
	}

	public static HotelService getHotelService() {
		if (service == null) {
			service = new HotelService();
			service.ensureTestData();
		}
		return service;
	}

	public synchronized List<Hotel> findAll() {
		return findAll(null);
	}

	public synchronized List<Hotel> findAll(String stringFilter) {
		ArrayList<Hotel> arrayList = new ArrayList<>();
		for (Hotel currentHotel : hotelsMap.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| currentHotel.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(currentHotel.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Hotel>() {

			@Override
			public int compare(Hotel o1, Hotel o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized long count() {
		return hotelsMap.size();
	}

	public synchronized void delete(Hotel value) {
		hotelsMap.remove(value.getId());
	}

	public synchronized void save(Hotel hotel) {
		if (hotel == null) {
			LOGGER.log(Level.SEVERE,
					"Hotel is null.");
			return;
		}
		if (hotel.getId() == null) {
			hotel.setId(nextId++);
		}
		try {
			hotel = (Hotel) hotel.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		hotelsMap.put(hotel.getId(), hotel);
	}

	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] names = new String[] { 
					"Orbita Mira_str267-47 5.5 +375298886655 +375298886655 www.booking.com someDescription",
					"Gorizont Nemiga_str277-57 5.0 +375298789966 +375298789966 www.booking.com someDescription",
					"Planeta Orlovskaya_str327-37 2.0 +375297485533 +375298789966 www.booking.com someDescription",
					"Europa Nezavisemosty_str927-13 5.0 +735293325665 +735293325665 www.booking.com someDescription",
					"Mars Kulman_str827-45 4.5 +375294562255 +375294562255 www.booking.com someDescription",
					"Snikers Latera_str327-88 4.5 +375298843232 +375298843232 www.booking.com someDescription",
					"Apple Pushkina_str257-113 3.0 +375294456678 +375294456678 www.booking.com someDescription",
					"Zamok Mira_str327-127 3.5 +375294433225 +375294433225 www.booking.com someDescription",
					};
			Random random = new Random(0);
			for (String name : names) {
				String[] split = name.split(" ");
				Hotel currentHotel = new Hotel();
				currentHotel.setName(split[0]);
				currentHotel.setAddress(split[1]);
				currentHotel.setStarRating(split[2]);
				currentHotel.setPhone(split[3]);
				currentHotel.setFax(split[4]);
				currentHotel.setCategory(HotelCategory.values()[random.nextInt(HotelCategory.values().length)]);
				currentHotel.setSite(split[5]);
			    currentHotel.setDescription(split[6]);
				save(currentHotel);
			}
		}
	}
}