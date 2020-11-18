package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.MachineDao;
import model.entities.Machine;

public class MachineService {
	
	private MachineDao dao = DaoFactory.createMachineDao();
	public List<Machine> findAll(){
	
		return dao.findAll();
	}

	
	public void saveOrUpdate(Machine obj) {

		if (obj.getMachineId() == null) {
			dao.insert(obj);
		}
		
		else {
			dao.update(obj);
		}
	}
	
	public void remove (Machine obj) {
		dao.deleteById(obj.getMachineId());
	}
}
