package com.ec.entidades;

import com.ec.entidades.Rubro;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-14T23:53:18")
@StaticMetamodel(DetalleRubro.class)
public class DetalleRubro_ { 

    public static volatile SingularAttribute<DetalleRubro, BigDecimal> valorMaximo;
    public static volatile SingularAttribute<DetalleRubro, String> tipo;
    public static volatile SingularAttribute<DetalleRubro, Integer> idDetalleRubro;
    public static volatile SingularAttribute<DetalleRubro, Rubro> idRubro;
    public static volatile CollectionAttribute<DetalleRubro, Rubro> rubroCollection;
    public static volatile SingularAttribute<DetalleRubro, String> nombreRubro;

}