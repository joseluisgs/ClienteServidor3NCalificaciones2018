/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienteservidor3ncalificaciones.controladores;

import clienteservidor3ncalificaciones.modelos.Alumno;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utilidades.Utilidad;

/**
 *
 * @author link
 */
public class ControladorAlumnos {
    // Patron Singleton -> Unsa sola instancia
    private static ControladorAlumnos controlador;

    private ControladorAlumnos() {
        //this.comp = new Compartido();
    }

    public static ControladorAlumnos getInstancia() {
        if (controlador == null) {
            controlador = new ControladorAlumnos();
        }
        return controlador;
    }
    
    public ArrayList<Alumno> listarAlumnos(String filtro) {
        ArrayList<Alumno> lista = new ArrayList<Alumno>();
        ControladorBD c = ControladorBD.nuevaConexionBD();
        c.abrirBD();
        ResultSet rs = c.consultarBD("Select * from alumnos  where nombre like \"%"+filtro+"%\" order by id");
        c.cerrarBD();
        try {
            while(rs.next()){
                Alumno a = new Alumno(rs.getInt("id"), rs.getString("nombre"), rs.getFloat("calificacion"));
                lista.add(a);
            }
        } catch (SQLException ex) {
            System.err.println("ControladorAlumno->Error al listar " +ex.getMessage());
        }
        
        return lista;
        
    }
    
    public Alumno datosAlumno(String filtro) {
        ArrayList<Alumno> lista = this.listarAlumnos(filtro);
        if(lista.isEmpty())
            return null;
        else
            return this.listarAlumnos(filtro).get(0);
        
    }
    
    public int numAprobados(String filtro) {
        ControladorBD c = ControladorBD.nuevaConexionBD();
        c.abrirBD();
        ResultSet rs = c.consultarBD("Select count(id) as aprobados from alumnos  where nombre like \"%"+filtro+"%\" and calificacion>=5");
        c.cerrarBD(); 
        try {
            while(rs.next())
                return rs.getInt("aprobados");
               
        } catch (SQLException ex) {
            System.err.println("ControladorAlumno->Error al listar " +ex.getMessage());
            return 0;
        }
        return 0;
    }
    
    public int numSuspensos(String filtro) {
        ControladorBD c = ControladorBD.nuevaConexionBD();
        c.abrirBD();
        ResultSet rs = c.consultarBD("Select count(id) as suspensos from alumnos  where nombre like \"%"+filtro+"%\" and calificacion<5");
        c.cerrarBD(); 
        try {
            while(rs.next())
                return rs.getInt("suspensos");
               
        } catch (SQLException ex) {
            System.err.println("ControladorAlumno->Error al listar " +ex.getMessage());
            return 0;
        }
        return 0;
    }
    
    public float media(String filtro) {
        ControladorBD c = ControladorBD.nuevaConexionBD();
        c.abrirBD();
        ResultSet rs = c.consultarBD("Select avg(calificacion) as media from alumnos  where nombre like \"%"+filtro+"%\"");
        c.cerrarBD(); 
        try {
            while(rs.next())
                return rs.getFloat("media");
               
        } catch (SQLException ex) {
            System.err.println("ControladorAlumno->Error al listar " +ex.getMessage());
            return 0;
        }
        return 0;
    }
    
    public int insertarAlumno(String nombre, float calificacion){
        // Habria que comprbar que no existe antes y bla bla bla bla ba
        int res = 0;
       if(Utilidad.nuevaInstancia().notaValida(Float.toString(calificacion))){
           ControladorBD c = ControladorBD.nuevaConexionBD();
           c.abrirBD();
           String consulta = "insert into alumnos (nombre, calificacion) values ('"+nombre+"','"+calificacion+"')";
           //System.out.println(consulta);
           res = c.actualizarBD(consulta);
           c.cerrarBD(); 
       }else{
           // Al comprbar que no existe podemos devolver distintos códigos de errores
           res = -1; // Nota invalidad
       }
       return res;
    }
    
}
