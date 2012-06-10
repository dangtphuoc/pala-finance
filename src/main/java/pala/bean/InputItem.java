package pala.bean;


import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * A Spring Data Neo4j enhanced World entity.
 * <p/>
 * This is the initial POJO in the Universe.
 */
@NodeEntity
public class InputItem 
{   
    @GraphId Long id;
    
    private Item item;

    private double cost;

    public InputItem( Item item, double cost )
    {
        this.item = item;
        this.cost = cost;
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
