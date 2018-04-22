package my.vaadin.app.model;

import java.io.Serializable;

public class Category implements Serializable, Cloneable {

	private Long id;

	private String name = "";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Category clone() throws CloneNotSupportedException {
		return (Category) super.clone();
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isPersisted() {
		return id != null;
	}

}
