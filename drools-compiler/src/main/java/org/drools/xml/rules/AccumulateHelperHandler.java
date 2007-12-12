package org.drools.xml.rules;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

import org.drools.lang.descr.AccumulateDescr;
import org.drools.lang.descr.BaseDescr;
import org.drools.lang.descr.PatternDescr;
import org.drools.xml.BaseAbstractHandler;
import org.drools.xml.Configuration;
import org.drools.xml.ExtensibleXmlParser;
import org.drools.xml.Handler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author fernandomeyer
 */

public class AccumulateHelperHandler extends BaseAbstractHandler
    implements
    Handler {

    public AccumulateHelperHandler() {
        if ( (this.validParents == null) && (this.validPeers == null) ) {
            this.validParents = new HashSet();
            this.validParents.add( AccumulateDescr.class );

            this.validPeers = new HashSet();
            this.validPeers.add( null );

            this.validPeers.add( PatternDescr.class );
            this.validPeers.add( BaseDescr.class );

            this.allowNesting = true;
        }
    }

    public Object start(final String uri,
                        final String localName,
                        final Attributes attrs,
                        final ExtensibleXmlParser xmlPackageReader) throws SAXException {

        xmlPackageReader.startConfiguration( localName,
                                                  attrs );

        return new BaseDescr();
    }    
    
    public Object end(final String uri,
                      final String localName,
                      final ExtensibleXmlParser xmlPackageReader) throws SAXException {

        final Configuration config = xmlPackageReader.endConfiguration();

        final String expression = config.getText();

        final Object parent = xmlPackageReader.getParent();

        final AccumulateDescr accumulate = (AccumulateDescr) parent;

        if ( localName.equals( "init" ) ) {
            emptyContentCheck( localName, expression, xmlPackageReader );
            accumulate.setInitCode( expression.trim() );
        } else if ( localName.equals( "action" ) ) {  
            emptyContentCheck( localName, expression, xmlPackageReader );
            accumulate.setActionCode( expression.trim() );
        } else if ( localName.equals( "result" ) ) { 
            emptyContentCheck( localName, expression, xmlPackageReader );
            accumulate.setResultCode( expression.trim() );
        } else if ( localName.equals( "reverse" ) ) {
            emptyContentCheck( localName, expression, xmlPackageReader );
            accumulate.setReverseCode( expression.trim() );
        } else if ( localName.equals( "external-function" ) ) {
            accumulate.setExternalFunction( true );
            accumulate.setFunctionIdentifier( config.getAttribute( "evaluator" ) );
            accumulate.setExpression( config.getAttribute( "expression" ) );
        }

        return null;
    }

    public Class generateNodeFor() {
        return BaseDescr.class;
    }    

}