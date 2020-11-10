package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.MachineDao;
import model.entities.Components;
import model.entities.Machine;

public class MachineDaoJDBC implements MachineDao{
	
private Connection conn;
	
	public MachineDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Machine obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO machine "
					+ "(Name, Type) "
					+ "VALUES "
					+ "(?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getType());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setMachineId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Machine obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE machine "
					+ "SET Name = ?, Type = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getType());
			st.setInt(3, obj.getMachineId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM machine WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Machine findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT components.*,machine.Name, machine.Type " 
					+ "FROM components INNER JOIN machine "
					+ "ON components.MachineId = machine.Id " 
					+ "WHERE components.Id = ? ");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Machine mac = instantiateMachine(rs);
				return mac;
			}

			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Machine> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM machine ORDER BY Name");
			
			rs = st.executeQuery();

			List<Machine> list = new ArrayList<>();		//Cria variável do tipo List para armazenar lista de components
			Map<Integer, Machine> map = new HashMap<>();	//Cria variável tipo Map "Não aceita repetições" com busca HashMap "Rápido e não ordenado"

			while (rs.next()) {			//Percorre enquanto houver um próximo resultset					

				Machine mac = map.get(rs.getInt("Id"));
				//Consulta se existe Machine com machineId selecionado de mesmo valor, se não houver retorna Null e Instancia Machine (Para não criar vários objetos do tipo Machine)

				if (mac == null) {		//Se for null instancia Machine e Salva Machine no Map
					mac = instantiateMachine(rs);
					map.put(rs.getInt("Id"), mac);
				}

				//Components obj = instantiateComponents(rs, mac);	//Instancia obj e adiciona a list
				list.add(mac);
			}
			return list;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

	private Machine instantiateMachine(ResultSet rs) throws SQLException {
		Machine mac = new Machine();
		mac.setMachineId(rs.getInt("Id"));
		mac.setName(rs.getString("Name"));  //MacName é apelido para nome da coluna
		mac.setType(rs.getString("Type"));  //MacType é apelido para nome da coluna

		return mac;
	}

}
