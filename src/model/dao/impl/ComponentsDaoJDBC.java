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
import model.dao.ComponentsDao;
import model.entities.Components;
import model.entities.Machine;

public class ComponentsDaoJDBC implements ComponentsDao {

	private Connection conn;

	public ComponentsDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Components obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO components "
					+ "(Quantity, Description, Code, Provider1, Provider2, MachineId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getQuantity());
			st.setString(2, obj.getDescription());
			st.setString(3, obj.getCode());
			st.setString(4, obj.getProvider1());
			st.setString(5, obj.getProvider2());
			st.setInt(6, obj.getMachine().getMachineId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
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
	public void update(Components obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE components "
					+ "SET Quantity = ?, Description = ?, Code = ?, Provider1 = ?, Provider2 = ?, MachineId = ? "
					+ "WHERE Id = ?");
			
			st.setInt(1, obj.getQuantity());
			st.setString(2, obj.getDescription());
			st.setString(3, obj.getCode());			
			st.setString(4, obj.getProvider1());
			st.setString(5, obj.getProvider2());
			st.setInt(6, obj.getMachine().getMachineId());
			st.setInt(7, obj.getId());
			
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
			st = conn.prepareStatement("DELETE FROM components WHERE Id = ?");
			
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
	public Components findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT components.*,machine.Name as MacName,machine.Type as MacType " 
					+ "FROM components INNER JOIN machine "
					+ "ON components.MachineId = machine.Id " 
					+ "WHERE components.Id = ? ");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Machine mac = instantiateMachine(rs);
				Components obj = instantiateComponents(rs, mac);
				return obj;
			}

			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Components instantiateComponents(ResultSet rs, Machine mac) throws SQLException {
		Components obj = new Components();
		obj.setId(rs.getInt("Id"));
		obj.setQuantity(rs.getInt("Quantity"));
		obj.setDescription(rs.getString("Description"));
		obj.setCode(rs.getString("Code"));
		obj.setProvider1(rs.getString("Provider1"));
		obj.setProvider2(rs.getString("Provider2"));
		obj.setMachine(mac);
		return obj;
	}

	private Machine instantiateMachine(ResultSet rs) throws SQLException {
		Machine mac = new Machine();
		mac.setMachineId(rs.getInt("MachineId"));//Tem que ser o MachineId, é o name column dentro do objeto Components
		mac.setName(rs.getString("MacName"));  //MacName é apelido para nome da coluna
		mac.setType(rs.getString("MacType"));  //MacType é apelido para nome da coluna

		return mac;
	}

	@Override
	public List<Components> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT components.*,machine.Name as MacName, machine.Type as MacType "
					+"FROM components INNER JOIN machine "
					+"ON components.MachineId = machine.Id "
					+"ORDER BY Name");
			
			rs = st.executeQuery();

			List<Components> list = new ArrayList<>();		//Cria variável do tipo List para armazenar lista de components
			Map<Integer, Machine> map = new HashMap<>();	//Cria variável tipo Map "Não aceita repetições" com busca HashMap "Rápido e não ordenado"

			while (rs.next()) {			//Percorre enquanto houver um próximo resultset					

				Machine mac = map.get(rs.getInt("MachineId"));
				//Consulta se existe Machine com machineId selecionado de mesmo valor, se não houver retorna Null e Instancia Machine (Para não criar vários objetos do tipo Machine)

				if (mac == null) {		//Se for null instancia Machine e Salva Machine no Map
					mac = instantiateMachine(rs);
					map.put(rs.getInt("MachineId"), mac);
				}

				Components obj = instantiateComponents(rs, mac);	//Instancia obj e adiciona a list
				list.add(obj);
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

	@Override
	public List<Components> findByMachineId(Machine machine) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT components.*,machine.Name as MacName, machine.Type as MacType "
					+"FROM components INNER JOIN machine "
					+"ON components.MachineId = machine.Id "
					+"WHERE MachineId = ? "
					+"ORDER BY Name");
			
			st.setInt(1, machine.getMachineId());

			rs = st.executeQuery();

			List<Components> list = new ArrayList<>();		//Cria variável do tipo List para armazenar lista de components
			Map<Integer, Machine> map = new HashMap<>();	//Cria variável tipo Map "Não aceita repetições" com busca HashMap "Rápido e não ordenado"

			while (rs.next()) {			//Percorre enquanto houver um próximo resultset					

				Machine mac = map.get(rs.getInt("MachineId"));
				//Consulta se existe Machine com machineId selecionado de mesmo valor, se não houver retorna Null e Instancia Machine (Para não criar vários objetos do tipo Machine)

				if (mac == null) {		//Se for null instancia Machine e Salva Machine no Map
					mac = instantiateMachine(rs);
					map.put(rs.getInt("MachineId"), mac);
				}

				Components obj = instantiateComponents(rs, mac);	//Instancia obj e adiciona a list
				list.add(obj);
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
	
}




