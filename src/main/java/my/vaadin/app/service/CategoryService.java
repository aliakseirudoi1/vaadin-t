package my.vaadin.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import my.vaadin.app.model.Category;
import my.vaadin.app.model.Hotel;

public class CategoryService {

	private static CategoryService service;
	private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());

	private final HashMap<Long, Category> categoryiesMap = new HashMap<>();

	private long nextId = 0;

	private CategoryService() {
	}

	public static CategoryService getCategoryService() {
		if (service == null) {
			service = new CategoryService();
			service.ensureTestData();
		}
		return service;
	}

	public synchronized List<Category> findAll() {

		return new ArrayList<>(categoryiesMap.values());
	}

	public synchronized long count() {
		return categoryiesMap.size();
	}

	public synchronized void delete(Category value) {
		categoryiesMap.remove(value.getId());
	}

	public synchronized void save(Category category) {
		if (category == null) {
			LOGGER.log(Level.SEVERE, "Category is null.");
			return;
		}

		try {
			category = (Category) category.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		if (category.getId() == null) {
			category.setId(nextId++);
		}

		categoryiesMap.put(category.getId(), category);
	}

	public void ensureTestData() {
		final String[] cateroyies = new String[] { "Hotel", "BusinessHotel", "Apartments", "Restaurant", "GOOO" };

		for (String name : cateroyies) {
			Category currentcategory = new Category();
			currentcategory.setName(name);
			save(currentcategory);
		}
	}

}
