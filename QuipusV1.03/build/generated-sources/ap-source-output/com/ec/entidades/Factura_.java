package com.ec.entidades;

import com.ec.entidades.Detalle;
import com.ec.entidades.Proveedor;
import com.ec.entidades.Rubro;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-14T23:53:18")
@StaticMetamodel(Factura.class)
public class Factura_ { 

    public static volatile SingularAttribute<Factura, Date> fecha;
    public static volatile CollectionAttribute<Factura, Detalle> detalleCollection;
    public static volatile SingularAttribute<Factura, Integer> idFactu;
    public static volatile SingularAttribute<Factura, Proveedor> idProveedor;
    public static volatile CollectionAttribute<Factura, Rubro> rubroCollection;

}