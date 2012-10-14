package pala.bean;

import java.util.Date;

import org.springframework.data.neo4j.annotation.MapResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

@MapResult
public interface ReportByItemResult {
	@ResultColumn("item.name")
	String getName();
	
	@ResultColumn("sum(n.cost)")
	double getCost();
}
