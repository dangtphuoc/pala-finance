package pala.bean;


import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * A Spring Data Neo4j enhanced World entity.
 * <p/>
 * This is the initial POJO in the Universe.
 */
@NodeEntity
public class Item 
{   
    @GraphId Long id;
    
    @Indexed
    private String name;

    private String description;
    
    @RelatedTo(type="item", direction = Direction.INCOMING)
    @Fetch
    private Set<InputItem> inputItems;
    
    private boolean active;

    public Item( String name, String description )
    {
        this.name = name;
        this.description = description;
        this.active = true;
    }

    public Item()
    {
    }
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
    public String getName()
    {
        return name;
    }
    
    public String getDescription() {
		return description;
	}
	public Set<InputItem> getInputItems() {
		return inputItems;
	}
	
	public void setInputItems(Set<InputItem> inputItems) {
		this.inputItems = inputItems;
	}
	
	
    public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
    public String toString()
    {
        return name;
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
		Item other = (Item) obj;
		if (id == null) return other.id == null;
        return id.equals(other.id);
    }
}
