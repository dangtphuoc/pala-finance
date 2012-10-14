package pala.repository;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContent {
	public static ConfigurableApplicationContext applicationContext =  new ClassPathXmlApplicationContext( "/spring/financeContext.xml");
}
