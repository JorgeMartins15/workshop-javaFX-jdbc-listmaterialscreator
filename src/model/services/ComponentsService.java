package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ComponentsDao;
import model.entities.Components;

public class ComponentsService {
	
	private ComponentsDao dao = DaoFactory.createComponentsDao();
	public List<Components> findAll(){
	
		return dao.findAll();
	}

	
	public void saveOrUpdate(Components obj) {

		if (obj.getId() == null) {
			dao.insert(obj);
		}
		
		else {
			dao.update(obj);
		}
	}
	
	public void remove (Components obj) {
		dao.deleteById(obj.getId());
	}
}
