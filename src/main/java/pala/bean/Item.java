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
public class Item 
{   
    @GraphId Long id;
    
    @Indexed
    private String name;

    private String description;

    public Item( String name, String description )
    {
        this.name = name;
        this.description = description;
    }

    public Item()
    {
    }

    public String getName()
    {
        return name;
    }
    
    public String getDescription() {
		return description;
	}

    @Override
    public String toString()
    {
        return String.format("World{name='%s, moons=%s}", name, description);
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
