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
            System.out.println("Hubo un error al iniciar o leer y lanzo: "+ex.getMessage());
        }
    }
    
    
}
