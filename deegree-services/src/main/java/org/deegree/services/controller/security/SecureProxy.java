//$HeadURL$
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
package org.deegree.services.controller.security;

import static org.deegree.services.controller.OGCFrontController.getNormalizedKVPMap;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.deegree.services.authentication.SecurityException;
import org.deegree.services.controller.Credentials;
import org.deegree.services.controller.CredentialsProvider;
import org.slf4j.Logger;

/**
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class SecureProxy extends HttpServlet {

    private static final Logger LOG = getLogger( SecureProxy.class );

    private static final long serialVersionUID = 6154340524804958669L;

    private String proxiedUrl;

    private CredentialsProvider credentialsProvider;

    private XMLInputFactory fac = XMLInputFactory.newInstance();

    @Override
    public void init( ServletConfig config )
                            throws ServletException {
        super.init( config );
        @SuppressWarnings("unchecked")
        Enumeration<String> e = config.getInitParameterNames();
        while ( e.hasMoreElements() ) {
            String param = e.nextElement();
            if ( param.equalsIgnoreCase( "proxied_url" ) ) {
                proxiedUrl = config.getInitParameter( param );
            }
        }
        if ( proxiedUrl == null ) {
            String msg = "You need to define the 'proxied_url' init parameter in the web.xml.";
            LOG.info( "Secure Proxy was NOT started:" );
            LOG.info( msg );
            throw new ServletException( msg );
        }
        credentialsProvider = new SecurityConfiguration( getServletContext() ).getCredentialsProvider();
        if ( credentialsProvider == null ) {
            String msg = "You need to provide an WEB-INF/conf/services/security/security.xml which defines at least one credentials provider.";
            LOG.info( "Secure Proxy was NOT started:" );
            LOG.info( msg );
            throw new ServletException( msg );
        }
        LOG.info( "deegree 3 secure proxy initialized." );
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) {
        try {
            Credentials creds = credentialsProvider.doXML( fac.createXMLStreamReader( request.getInputStream(),
                                                                                      request.getCharacterEncoding() ),
                                                           request, response );
            System.out.println( "POST: " + creds );
        } catch ( SecurityException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( XMLStreamException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) {
        try {
            Map<String, String> normalizedKVPParams = getNormalizedKVPMap( request.getQueryString(), null );
            Credentials creds = credentialsProvider.doKVP( normalizedKVPParams, request, response );
            System.out.println( "KVP: " + creds );
        } catch ( UnsupportedEncodingException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
