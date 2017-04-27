package mx.com.gseguros.confpantallas.bd;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class ConnectDB {

	private DriverManagerDataSource dataSource;
	
	public ConnectDB() {
		//ApplicationContext context = new ClassPathXmlApplicationContext("appContext-dao.xml");

		dataSource = new DriverManagerDataSource();
		/*dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dataSource.setUrl("jdbc:sqlserver://127.0.0.1:1433;databaseName=FopDinamic");
		dataSource.setUsername("sa");
		dataSource.setPassword("SalmaCamila2017");*/
		/*
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
		dataSource.setUsername("SYSTEM");
		dataSource.setPassword("salmac");
		*/
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@10.1.1.133:6521:GSEGUROS");
		dataSource.setUsername("ice");
		dataSource.setPassword("ice");
	}

	public DriverManagerDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DriverManagerDataSource dataSource) {
		this.dataSource = dataSource;
	}
}
