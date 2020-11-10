package model.dao;

import java.util.List;

import model.entities.Machine;

public interface MachineDao {

	void insert(Machine obj);
	void update(Machine obj);
	void deleteById(Integer id);
	Machine findById(Integer id);
	List<Machine> findAll();
}
