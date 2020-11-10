package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.MachineDao;
import model.entities.Machine;

public class MachineServices {
	
	private MachineDao dao = DaoFactory.createMachineDao();
	
	public List<Machine> findAll(){
	
		return dao.findAll();
	}

}
