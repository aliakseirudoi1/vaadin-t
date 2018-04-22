package my.vaadin.app.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Hotel implements Serializable, Cloneable {

	private Long id;

	private String name = "";

	private String address = "";

	private String site = "";

	private Category category;

	private Integer starRating;

	private String phone = "";

	private String fax = "";

	private String description = "";

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getStarRating() {
		return starRating;
	}

	public void setStarRating(Integer starRating) {
		this.starRating = starRating;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public Hotel clone() throws CloneNotSupportedException {
		return (Hotel) super.clone();
	}

	@Override
	public String toString() {
		return "Hotel [name=" + name + ", address=" + address + "]";
	}

}