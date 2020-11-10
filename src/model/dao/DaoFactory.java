package model.dao;

import db.DB;
import model.dao.impl.ComponentsDaoJDBC;
import model.dao.impl.MachineDaoJDBC;

public class DaoFactory {

	public static MachineDao createMachineDao() {
		return new MachineDaoJDBC(DB.getConnection());
	}
	
	public static ComponentsDao createComponentsDao() {
		return new ComponentsDaoJDBC(DB.getConnection());
	}
}
