package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Machine;

public class MachineServices {
	
	public List<Machine> findAll(){
		List<Machine> list = new ArrayList<Machine>();
		list.add(new Machine(1, "GS300", "Empacotamento"));
		list.add(new Machine(2, "GM14", "Balança"));
		list.add(new Machine(3, "CW1", "Checkweigher"));
		return list;
	}

}
