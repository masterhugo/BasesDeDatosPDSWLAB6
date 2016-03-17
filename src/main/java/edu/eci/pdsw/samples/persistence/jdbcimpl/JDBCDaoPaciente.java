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
        String select2 = "select p.nombre,p.fecha_nacimiento,c.fecha_y_hora,c.resumen from "
                + "PACIENTES as p, CONSULTAS as c where p.id = c.PACIENTES_id and p.tipo_id = c.PACIENTES_tipo_id and p.id = ?"
                + " and p.tipo_id = ? ";
        String select1 = "select count(*) from CONSULTAS as p where PACIENTES_id = ? and PACIENTES_tipo_id = ?";
        String select3 = "select nombre, fecha_nacimiento from PACIENTES where id=? and tipo_id=?";
        Paciente p = null;
        String nombre = null;
        Date fechanam = null;
        Set<Consulta> cons = new HashSet<Consulta>();
        try {
            //saber si tiene consultas
            ps = con.prepareStatement(select1,ResultSet.TYPE_SCROLL_INSENSITIVE, 
                            ResultSet.CONCUR_UPDATABLE);
            int cont = -1;
            ps.setInt(1, idpaciente);
            ps.setString(2, tipoid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cont = rs.getInt(1);
            }
            
            //buscar el usuario con sus consultas
            if(cont>0){
                ps = con.prepareStatement(select2,ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_UPDATABLE);
                ps.setInt(1, idpaciente);
                ps.setString(2, tipoid);
                rs = ps.executeQuery();
                if(!rs.first()) throw new PersistenceException("No encontro ningun resultado.");
                rs.beforeFirst();
                while(rs.next()){
                    nombre = rs.getString("nombre");
                    fechanam = rs.getDate("fecha_nacimiento");
                    cons.add(new Consulta(rs.getDate("fecha_y_hora"), rs.getString("resumen")));
                }
                //select = "select fecha_y_hora,fecha_nacimiento from PACIENTES as p where id = ? and tipo_id = ?";
                if(nombre != null){
                    p = new Paciente(idpaciente,tipoid,nombre,fechanam);
                    p.setConsultas(cons);
                }else throw new PersistenceException("No encontro ningun resultado.");
            }else{
                ps = con.prepareStatement(select3,ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                ResultSet.CONCUR_UPDATABLE);
                ps.setInt(1, idpaciente);
                ps.setString(2, tipoid);
                rs = ps.executeQuery();
                if(!rs.first()) throw new PersistenceException("No encontro ningun resultado.");
                rs.beforeFirst();
                while(rs.next()){
                    nombre = rs.getString("nombre");
                    fechanam = rs.getDate("fecha_nacimiento");
                }
                if(nombre != null){
                    p = new Paciente(idpaciente,tipoid,nombre,fechanam);
                    p.setConsultas(cons);
                }else throw new PersistenceException("No encontro ningun resultado.");
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Un error ocurrio cuando se intento cargar el paciente: "+idpaciente+" "+ex.getMessage(),ex);
            //throw new PersistenceException(ex.getMessage(),ex);
        }
        return p;
    }

    @Override
    public void save(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        String insert = "insert into PACIENTES(id,tipo_id,nombre,fecha_nacimiento) values(?,?,?,?)";
        try {
            //registro el paciente
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
            
            //registro las consultas del paciente
            insert = "insert into CONSULTAS(fecha_y_hora,resumen,PACIENTES_id,PACIENTES_tipo_id) values(?,?,?,?)";
            ps = con.prepareStatement(insert);
            for (Consulta con1 : cons) {
                ps.setDate(1, con1.getFechayHora());
                ps.setString(2, con1.getResumen());
                ps.setInt(3, id);
                ps.setString(4, tipo);
                ps.executeUpdate();
            }
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
