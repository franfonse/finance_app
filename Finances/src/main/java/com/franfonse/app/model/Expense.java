package com.franfonse.app.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "expense")
public class Expense {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idExpense;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "document_id")
	private Document document;
	private String description;
	private Date date;
	private double value;
	
	
	public Expense() {
	}

	public Expense(Document document, String description, Date date, double value) {
		this.document = document;
		this.description = description;
		this.date = date;
		this.value = value;
	}
	

	public long getIdExpense() {
		return idExpense;
	}

	public void setIdExpense(long idExpense) {
		this.idExpense = idExpense;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}