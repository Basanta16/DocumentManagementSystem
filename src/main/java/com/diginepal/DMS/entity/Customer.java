package com.diginepal.DMS.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name="CUSTOMER")
public class Customer {
	
	
	@Id
	private int cid;
	private String firstName;
	private String secondName;
	@Column(unique = true)
	private String email;
	private String phone;
	private String image;
	private String FcitizenshipImg;
	private String BcitizenshipImg;
	private String licenseImg;
	@Column(length = 500)
	private String description;
	
	
	@ManyToOne
	private User user;
	
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getFcitizenshipImg() {
		return FcitizenshipImg;
	}
	public void setFcitizenshipImg(String fcitizenshipImg) {
		FcitizenshipImg = fcitizenshipImg;
	}
	public String getBcitizenshipImg() {
		return BcitizenshipImg;
	}
	public void setBcitizenshipImg(String bcitizenshipImg) {
		BcitizenshipImg = bcitizenshipImg;
	}
	public String getLicenseImg() {
		return licenseImg;
	}
	public void setLicenseImg(String licenseImg) {
		this.licenseImg = licenseImg;
	}
	
	/*
	 * @Override public String toString() { return "Customer [cid=" + cid +
	 * ", firstName=" + firstName + ", secondName=" + secondName + ", email=" +
	 * email + ", phone=" + phone + ", image=" + image + ", FcitizenshipImg=" +
	 * FcitizenshipImg + ", BcitizenshipImg=" + BcitizenshipImg + ", licenseImg=" +
	 * licenseImg + ", description=" + description + ", user=" + user + "]"; }
	 */
	@Override
	public boolean equals(Object obj) {
		return this.cid==((Customer)obj).getCid();
	}
	 
	
	
	
	
	

}
