/*
 * Copyright (C) 2016 hcadavid
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

import edu.eci.pdsw.samples.entities.*;
import edu.eci.pdsw.samples.persistence.*;
import java.io.*;
import java.sql.Date;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class PacientePersistenceTest {
    
    @Test
    public void AgregarPacienteNuevoSinConsultasALaBaseDeDatos(){
        DaoFactory daof = null;
        try {
            InputStream input = null;
            input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
            Properties properties=new Properties();
            properties.load(input);
            
            daof=DaoFactory.getInstance(properties);
            
            daof.beginSession();
            DaoPaciente paciente = daof.getDaoPaciente();
            paciente.save(new Paciente(1, "CC", "Hugo Alvarez", Date.valueOf("1995-05-15")));
            
            Paciente p3 = paciente.load(1, "CC");
            assertEquals(p3.getConsultas().size(), 0);
            
            daof.commitTransaction();        
            daof.endSession();
        } catch (IOException | PersistenceException ex) {
            if(daof!=null){
                try {
                    daof.endSession();
                } catch (PersistenceException ex1) {
                    System.out.println("Hubo un error al cerrar y lanzo: "+ex1.getMessage());
                }
            }
            fail("Hubo un error al iniciar o leer y lanzo prueba 1: "+ex.getMessage());
        }
    }
    @Test
    public void AgregarPacienteNuevoUnaConsultaALaBaseDeDatos(){
        DaoFactory daof = null;
        try {
            InputStream input;
            input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
            Properties properties=new Properties();
            properties.load(input);
            
            daof=DaoFactory.getInstance(properties);
            
            daof.beginSession();
            DaoPaciente paciente = daof.getDaoPaciente();
            Paciente p = new Paciente(2, "CC", "Hugo Alvarez", Date.valueOf("1995-05-15"));
            Set<Consulta> cons = new HashSet<>();
            cons.add(new Consulta(Date.valueOf("2001-01-01"), "Gracias"));
            p.setConsultas(cons);
            paciente.save(p);
            Paciente p3 = paciente.load(p.getId(), p.getTipo_id());
            assertEquals(p3.getConsultas().size(), 1);
            daof.commitTransaction();        
            daof.endSession();
        } catch (IOException | PersistenceException ex) {
            if(daof!=null){
                try {
                    daof.endSession();
                } catch (PersistenceException ex1) {
                    System.out.println("Hubo un error al cerrar y lanzo: "+ex1.getMessage());
                }
            }
            fail("Hubo un error al iniciar o leer y lanzo prueba 2: "+ex.getMessage());
        }
    }
    @Test
    public void AgregarPacienteNuevoMuchasConsultasALaBaseDeDatos(){
        DaoFactory daof = null;
        try {
            InputStream input = null;
            input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
            Properties properties=new Properties();
            properties.load(input);
            
            daof=DaoFactory.getInstance(properties);
            
            daof.beginSession();
            DaoPaciente paciente = daof.getDaoPaciente();
            Paciente p = new Paciente(3, "CC", "Hugo Alvarez", Date.valueOf("1995-05-15"));
            Set<Consulta> cons = new HashSet<Consulta>();
            cons.add(new Consulta(Date.valueOf("2001-01-01"), "Gracias"));
            cons.add(new Consulta(Date.valueOf("2001-05-05"), "Ya no"));
            p.setConsultas(cons);
            paciente.save(p);
            
            
            Paciente p3 = paciente.load(p.getId(), p.getTipo_id());
            assertEquals(p3.getConsultas().size(), 2);
            daof.commitTransaction();        
            daof.endSession();
        } catch (IOException | PersistenceException ex) {
            if(daof!=null){
                try {
                    daof.endSession();
                } catch (PersistenceException ex1) {
                    System.out.println("Hubo un error al cerrar y lanzo: "+ex1.getMessage());
                }
            }
            fail("Hubo un error al iniciar o leer y lanzo prueba 3: "+ex.getMessage());
        }
    }
    @Test
    public void AgregarPacienteNuevoDenuevoVariasConsultasALaBaseDeDatos(){
        DaoFactory daof = null;
        try {
            InputStream input = null;
            input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
            Properties properties=new Properties();
            properties.load(input);
            
            daof=DaoFactory.getInstance(properties);
            
            daof.beginSession();
            DaoPaciente paciente = daof.getDaoPaciente();
            int i = 0;
            Paciente p = null;
            paciente.save(new Paciente(4, "CC", "Lucena", Date.valueOf("2001-01-01")));
            p = new Paciente(4, "CC", "Lucena", Date.valueOf("2001-01-01"));
            Set<Consulta> cons = new HashSet<Consulta>();
            cons.add(new Consulta(Date.valueOf("2001-01-01"), "Gracias"));
            cons.add(new Consulta(Date.valueOf("2001-05-05"), "Ya no"));
            p.setConsultas(cons);
            paciente.save(p);
            
            fail("No debio continuar");
            daof.commitTransaction();        
            daof.endSession();
        } catch (IOException | PersistenceException ex) {
            if(daof!=null){
                try {
                    daof.endSession();
                } catch (PersistenceException ex1) {
                    System.out.println("Hubo un error al cerrar y lanzo: "+ex1.getMessage());
                }
            }
            assertEquals(ex.getMessage(), "No inserto los datos, revisar la base de datos");
        }
    }
    
}
