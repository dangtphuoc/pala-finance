package pala.bean;

import java.util.Date;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

import pala.gui.IncomeType;

@NodeEntity
public class IncomeItem {
	@GraphId private Long id;
	private IncomeType type;
	private double cost;
	private Date date;
	public IncomeItem() {
	}
	
	public IncomeItem(IncomeType type, double cost, Date date) {
		this.type = type;
		this.cost = cost;
		this.date = date;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public IncomeType getType() {
		return type;
	}
	public void setType(IncomeType type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
