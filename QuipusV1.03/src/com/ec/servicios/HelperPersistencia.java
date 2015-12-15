/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicios;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author PC
 */
public class HelperPersistencia {

    public static EntityManagerFactory getEMF() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("QuipusV1.03PU");
        return emf;
    }
}
