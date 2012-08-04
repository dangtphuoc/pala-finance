package pala.bean;


import java.io.File;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * A Spring Data Neo4j enhanced World entity.
 * <p/>
 * This is the initial POJO in the Universe.
 */
@NodeEntity
public class InputItem 
{   
    @GraphId Long id;
    
    @RelatedTo(type="item", direction=Direction.OUTGOING)
    @Fetch private Item item;

    private double cost;
    
    private Date date;
    private Date dateTime;
    private String detail;
    private String attachment;

    public InputItem( Item item, double cost, Date dateTime, Date date)
    {
        this.item = item;
        this.cost = cost;
        this.dateTime = dateTime;
        this.date = date;
    }

    public InputItem()
    {
    }

    public Item getItem() {
		return item;
	}
    
    public void setItem(Item item) {
		this.item = item;
	}
    
    public double getCost() {
		return cost;
	}
    
    public void setCost(double cost) {
		this.cost = cost;
	}

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date date) {
		this.dateTime = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Override
    public String toString()
    {
        return String.format("World{name='%s, moons=%n}", item.getName(), this.cost);
    }

	@Override
	public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		InputItem other = (InputItem) obj;
		if (id == null) return other.id == null;
        return id.equals(other.id);
    }
}
