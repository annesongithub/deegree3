//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
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
package org.deegree.commons.tom.datetime;

import static javax.xml.bind.DatatypeConverter.parseTime;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

/**
 * Represents an <code>xs:time</code> instance.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class Time extends TimeInstant {

    /**
     * Creates a new {@link Time} instance from the given <code>xs:time</code> encoded value.
     * 
     * @param time
     *            encoded time, must not be <code>null</code>
     * @throws IllegalArgumentException
     *             if parameter does not conform to lexical value space defined in XML Schema Part 2: Datatypes for
     *             <code>xs:time</code>
     */
    public Time( String xsDate ) throws IllegalArgumentException {
        super( parseTime( xsDate ), isLocal( xsDate ) );
    }

    public Time( Calendar cal, boolean isLocal ) {
        super( cal, isLocal );
    }

    /**
     * Returns this time instant as a {@link java.sql.Time}.
     * 
     * @return SQL date, never <code>null</code>
     */
    @Override
    public java.sql.Time getSQLDate() {
        return new java.sql.Time( getTimeInMilliseconds() );
    }

    @Override
    public String toString() {
        return DatatypeConverter.printTime( getCalendar() );
    }
}
