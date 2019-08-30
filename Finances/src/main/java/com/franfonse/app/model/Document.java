package com.franfonse.app.model;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "document")
public class Document {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idDocument;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	private String title;
	private Date date;
	private Date month;
	private double balance;
	@OneToMany(mappedBy = "document", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Income> incomes;
	@OneToMany(mappedBy = "document", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Expense> expenses;
	
	
	public Document() {
	}
	
	public Document(User user, String title, Date date, Date month) {
		this.user = user;
		this.title = title;
		this.date = date;
		this.month = month;
		this.balance = 0;
	}
	

	public long getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(long idDocument) {
		this.idDocument = idDocument;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public List<Income> getIncomes() {
		return incomes;
	}

	public void setIncomes(List<Income> incomes) {
		this.incomes = incomes;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}
}