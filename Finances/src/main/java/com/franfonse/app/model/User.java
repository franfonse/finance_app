package com.franfonse.app.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
@Entity(name = "user")
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idUser;
	private String name;
	private String surname;
	private String email;
	private String username;
	private String password;
	private Date date;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Document> listDocuments;
	@ElementCollection
	@CollectionTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "roles")
	List<GrantedAuthority> roles;
	
	
	public User() {
	}

	public User(String name, String surname, String email, String username, String password, Date date) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.username = username;
		this.password = password;
		this.date = date;
	}
	

	public long getIdUser() {
		return idUser;
	}

	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public List<Document> getListDocuments() {
		return listDocuments;
	}

	public void setListDocuments(List<Document> listDocuments) {
		this.listDocuments = listDocuments;
	}

	public List<GrantedAuthority> getRoles() {
		return roles;
	}

	public void setRoles(List<GrantedAuthority> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}