//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/deegree3/trunk/deegree-core/src/main/java/org/deegree/filter/function/other/IDiv.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.filter.function.other;

import java.util.List;

import javax.xml.namespace.QName;

import org.deegree.commons.tom.TypedObjectNode;
import org.deegree.commons.tom.primitive.PrimitiveValue;
import org.deegree.feature.Feature;
import org.deegree.feature.property.Property;
import org.deegree.filter.Expression;
import org.deegree.filter.FilterEvaluationException;
import org.deegree.filter.XPathEvaluator;
import org.deegree.filter.expression.Function;
import org.deegree.filter.function.FunctionProvider;

/**
 * Expects one argument that refers to a {@link Feature} and returns the (local) feature type name.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 27253 $, $Date: 2010-10-18 11:49:13 +0200 (Mo, 18. Okt 2010) $
 */
public class FtName implements FunctionProvider {

    private static final String NAME = "FtName";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getArgCount() {
        return 1;
    }

    @Override
    public Function create( List<Expression> params ) {
        return new Function( NAME, params ) {
            @Override
            public <T> TypedObjectNode[] evaluate( T obj, XPathEvaluator<T> xpathEvaluator )
                                    throws FilterEvaluationException {
                TypedObjectNode[] vals1 = getParams()[0].evaluate( obj, xpathEvaluator );
                if ( vals1.length == 1 ) {
                    TypedObjectNode node = vals1[0];
                    Feature feature = null;
                    if ( node instanceof Feature ) {
                        feature = (Feature) node;
                    } else if ( node instanceof Property && ( (Property) node ).getValue() instanceof Feature ) {
                        feature = (Feature) ( (Property) node ).getValue();
                    } else {
                        return new TypedObjectNode[0];
                    }
                    QName ftName = feature.getName();
                    return new TypedObjectNode[] { new PrimitiveValue( ftName.getLocalPart() ) };
                }
                return new TypedObjectNode[0];
            }
        };
    }
}