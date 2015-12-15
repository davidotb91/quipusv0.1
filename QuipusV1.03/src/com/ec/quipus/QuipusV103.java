/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.quipus;

import com.ec.entidades.DetalleRubro;
import com.ec.entidades.Usuario;
import com.ec.servicios.DetalleRubroJpaController;
import com.ec.servicios.HelperPersistencia;
import com.ec.servicios.UsuarioJpaController;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author david
 */
public class QuipusV103 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       HelperPersistencia help = new HelperPersistencia();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoQuipusPU");
        UsuarioJpaController controller = new UsuarioJpaController();

        for (Usuario item : controller.findUsuarioEntities()) {
            System.out.println("valores " + item.getNombreUsuario());
            item.setCedulaUsuario("9999999999");
            //controller.edit(item);
    }
    
}
}
