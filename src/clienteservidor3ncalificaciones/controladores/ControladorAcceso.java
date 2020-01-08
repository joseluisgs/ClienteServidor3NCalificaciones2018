/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienteservidor3ncalificaciones.controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import utilidades.Utilidad;

/**
 *
 * @author link
 */
public class ControladorAcceso {
    // Patron Singleton -> Unsa sola instancia
    private static ControladorAcceso controlador;

    private ControladorAcceso() {
        //this.comp = new Compartido();
    }

    public static ControladorAcceso getInstancia() {
        if (controlador == null) {
            controlador = new ControladorAcceso();
        }
        return controlador;
    }
    
    public boolean indetificarUsuario(String email, String password){
        Utilidad u = Utilidad.nuevaInstancia();
        String pass = null;
        if(u.emailValido(email)){
            ControladorBD bd = ControladorBD.nuevaConexionBD();
            bd.abrirBD();
            String consulta="Select password from usuarios where tipo ='admin' and email = '"+email+"'";
            ResultSet rs = bd.consultarBD(consulta);
            try {
                while(rs.next()){
                    pass = rs.getString("password");
                }
            } catch (SQLException ex) {
                System.err.println("ControladorBD->"+ex.getMessage());
                return false;
            }
            bd.cerrarBD();
        }
        
        if(pass!=null && pass.equals(password)){
           return true;
        }else{
            return false;
        }
    }
    
}
