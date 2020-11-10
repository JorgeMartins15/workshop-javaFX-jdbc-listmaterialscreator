package model.dao;

import java.util.List;

import model.entities.Components;
import model.entities.Machine;

public interface ComponentsDao {

	void insert(Components obj);
	void update(Components obj);
	void deleteById(Integer id);
	Components findById(Integer id);
	List<Components> findAll();
	List<Components> findByMachineId(Machine machine);
	
}
