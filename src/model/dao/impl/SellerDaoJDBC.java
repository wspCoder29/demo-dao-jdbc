package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	//Dependência que estabelece conexão com o BD
	private Connection conn;
	public SellerDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	
	

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByid(Integer Id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		
		//Recebe a query
		PreparedStatement st = null;
		
		//recebe o resultado da query
		ResultSet rs = null;
		
	try {
		st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName "
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "WHERE seller.Id = ?"
		);
		
		//VERIFICAR O QUE ESSA LINHA FAZ
		st.setInt(1, id);
		rs = st.executeQuery();
		if(rs.next()) {
			//Instanciando obj Department
			Department dep = InstantiateDepartment(rs);
			//Instanciando obj Seller
			Seller obj = InstantiateSeller(rs, dep);
			return obj;
		}
		return null;
		
	}catch(SQLException e) {
		throw new DbException(e.getMessage());
	}finally {
		DB.closeStatement(st);
		DB.closeResultSet(rs);
	}
		
	}
	
	
	private Seller InstantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDepartment(dep);
		return seller;
	}
	

	private Department InstantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
			dep.setId(rs.getInt("DepartmentId"));
			dep.setName(rs.getString("DepName"));
		return dep;
	}




	@Override
	public List<Seller> findAll() {
		//Recebe a query
		PreparedStatement st = null;
		
		//recebe o resultado da query
		ResultSet rs = null;
		
	try {
		st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName "+
				"FROM seller INNER JOIN department "+
				"ON seller.DepartmentId = department.Id "+
				"ORDER BY Name "
		);
		
		rs = st.executeQuery();
		
		List<Seller> list = new ArrayList<>();
		Map<Integer, Department> map = new HashMap<>();
		
		while(rs.next()) {
			
			Department dep = map.get(rs.getInt("DepartmentId"));
			
			if(dep == null) {
				dep = InstantiateDepartment(rs);
				map.put(rs.getInt("DepartmentId"), dep);
			}
			
			Seller obj = InstantiateSeller(rs, dep);
			list.add(obj);
		}
		return list;

	} catch (SQLException e) {
		throw new DbException(e.getMessage());
	} finally {
		DB.closeStatement(st);
		DB.closeResultSet(rs);
	}
}



	@Override
	public List<Seller> findByDepartment(Department department) {
		
		//Recebe a query
		PreparedStatement st = null;
		
		//recebe o resultado da query
		ResultSet rs = null;
		
	try {
		st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName "+
				"FROM seller INNER JOIN department "+
				"ON seller.DepartmentId = department.Id "+
				"WHERE DepartmentId = ? "+
				"ORDER BY Name "
		);
		
		st.setInt(1, department.getId());
		rs = st.executeQuery();
		
		List<Seller> list = new ArrayList<>();
		Map<Integer, Department> map = new HashMap<>();
		
		while(rs.next()) {
			
			Department dep = map.get(rs.getInt("DepartmentId"));
			
			if(dep == null) {
				dep = InstantiateDepartment(rs);
				map.put(rs.getInt("DepartmentId"), dep);
			}
			
			Seller obj = InstantiateSeller(rs, dep);
			list.add(obj);
		}
		return list;
		
	}catch(SQLException e) {
		throw new DbException(e.getMessage());
	}finally {
		DB.closeStatement(st);
		DB.closeResultSet(rs);
	}
	
		
	}

}
