/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hcadavid
 */
public class JDBCDaoPaciente implements DaoPaciente {

    Connection con;

    public JDBCDaoPaciente(Connection con) {
        this.con = con;
    }
        

    @Override
    public Paciente load(int idpaciente, String tipoid) throws PersistenceException {
        PreparedStatement ps;
        String select = "select id,tipo_id,nombre,fecha_nacimiento,fecha_y_hora,resumen from "
                + "PACIENTES as p, CONSULTAS as c, where p.id = c.PACIENTES_id and p.tipo_id = c.PACIENTES_tipo_id and p.id = ?"
                + " and p.tipo_id = ?";
        Paciente p = null;
        try {
            ps = con.prepareStatement(select);
            ps.setInt(1, idpaciente);
            ps.setString(2, tipoid);
            ResultSet rs = ps.executeQuery();
            Set<Consulta> cons = new HashSet<Consulta>();
            String nombre = null;
            Date fechanam = null;
            while(rs.next()){
                nombre = rs.getString("nombre");
                fechanam = rs.getDate("fecha_nacimiento");
                cons.add(new Consulta(rs.getDate("fecha_y_hora"), rs.getString("resumen")));
                System.out.println("");
            }
            p = new Paciente(idpaciente, tipoid,nombre , fechanam);
            p.setConsultas(cons);
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while loading "+idpaciente,ex);
        }
        return p;
    }

    @Override
    public void save(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        String insert = "insert into PACIENTES(id,tipo_id,nombre,fecha_nacimiento) values(?,?,?,?)";
        try {
            Date fecha = p.getFechaNacimiento();
            String nombre = p.getNombre();
            String tipo = p.getTipo_id();
            int id = p.getId();
            Set<Consulta> cons = p.getConsultas();
            ps = con.prepareStatement(insert);
            ps.setInt(1, id);
            ps.setString(2, tipo);
            ps.setString(3, nombre);
            ps.setDate(4, fecha);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new PersistenceException("No inserto los datos, revisar la base de datos",ex);
        }
        

    }

    @Override
    public void update(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        /*try {
            
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while loading a product.",ex);
        } */
        throw new RuntimeException("No se ha implementado el metodo 'load' del DAOPAcienteJDBC");
    }
    
}
