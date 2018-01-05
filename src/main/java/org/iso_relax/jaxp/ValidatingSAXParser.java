/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2018 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iso_relax.jaxp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;

import org.iso_relax.verifier.Verifier;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Wrapper SAXParser with validation through JARV API. For the present, SAX1
 * features are not supported.
 *
 * @author Daisuke OKAJIMA
 */
@SuppressWarnings ("deprecation")
class ValidatingSAXParser extends SAXParser
{
  protected SAXParser m_aWrappedParser;
  protected Verifier m_aVerifier;

  /**
   * creates a new instance with an internal SAXParser and Schema.
   *
   * @param wrapped
   *        internal SAXParser
   * @param verifier
   *        verifier.
   */
  public ValidatingSAXParser (final SAXParser wrapped, final Verifier verifier)
  {
    m_aWrappedParser = wrapped;
    m_aVerifier = verifier;
  }

  /**
   * unsupported
   */
  @Override
  public Parser getParser ()
  {
    throw new UnsupportedOperationException ("getParser() method is not supported. Use getXMLReader().");
  }

  /**
   * returns a new XMLReader for parsing and validating the input
   */
  @Override
  public XMLReader getXMLReader () throws SAXException
  {
    final XMLFilter filter = m_aVerifier.getVerifierFilter ();
    filter.setParent (m_aWrappedParser.getXMLReader ());
    return filter;
  }

  /**
   * @see SAXParser#isNamespaceAware()
   */
  @Override
  public boolean isNamespaceAware ()
  {
    return m_aWrappedParser.isNamespaceAware ();
  }

  /**
   * returns true always
   */
  @Override
  public boolean isValidating ()
  {
    return true;
  }

  /**
   * @see SAXParser#setProperty(String, Object)
   */
  @Override
  public void setProperty (final String name, final Object value) throws SAXNotRecognizedException,
                                                                  SAXNotSupportedException
  {
    m_aWrappedParser.setProperty (name, value);
  }

  /**
   * @see SAXParser#getProperty(String)
   */
  @Override
  public Object getProperty (final String name) throws SAXNotRecognizedException, SAXNotSupportedException
  {
    return m_aWrappedParser.getProperty (name);
  }

  // SAX1 features are not supported
  @Override
  public void parse (final File f, final HandlerBase hb)
  {
    throw new UnsupportedOperationException ("SAX1 features are not supported");
  }

  @Override
  public void parse (final InputSource is, final HandlerBase hb)
  {
    throw new UnsupportedOperationException ("SAX1 features are not supported");
  }

  @Override
  public void parse (final InputStream is, final HandlerBase hb)
  {
    throw new UnsupportedOperationException ("SAX1 features are not supported");
  }

  @Override
  public void parse (final InputStream is, final HandlerBase hb, final String systemId)
  {
    throw new UnsupportedOperationException ("SAX1 features are not supported");
  }

  @Override
  public void parse (final String uri, final HandlerBase hb)
  {
    throw new UnsupportedOperationException ("SAX1 features are not supported");
  }

  /**
   * parses and validates the given File using the given DefaultHandler
   */
  @Override
  public void parse (final File f, final DefaultHandler dh) throws SAXException, IOException
  {
    final XMLReader reader = getXMLReader ();
    final InputSource source = new InputSource (new FileInputStream (f));
    reader.setContentHandler (dh);
    reader.parse (source);
  }

  /**
   * parses and validates the given InputSource using the given DefaultHandler
   */
  @Override
  public void parse (final InputSource source, final DefaultHandler dh) throws SAXException, IOException
  {
    final XMLReader reader = getXMLReader ();
    reader.setContentHandler (dh);
    reader.parse (source);
  }

  /**
   * parses and validates the given InputSource using the given DefaultHandler
   */
  @Override
  public void parse (final InputStream is, final DefaultHandler dh) throws SAXException, IOException
  {
    final XMLReader reader = getXMLReader ();
    final InputSource source = new InputSource (is);
    reader.setContentHandler (dh);
    reader.parse (source);
  }

  /**
   * parses and validates the given InputSream using the given DefaultHandler
   * and systemId
   */
  @Override
  public void parse (final InputStream is, final DefaultHandler dh, final String systemId) throws SAXException,
                                                                                           IOException
  {
    final XMLReader reader = getXMLReader ();
    final InputSource source = new InputSource (is);
    source.setSystemId (systemId);
    reader.setContentHandler (dh);
    reader.parse (source);
  }

  /**
   * parses and validates the given uri using the given DefaultHandler
   */
  @Override
  public void parse (final String uri, final DefaultHandler dh) throws SAXException, IOException
  {
    final XMLReader reader = getXMLReader ();
    final InputSource source = new InputSource (uri);
    reader.setContentHandler (dh);
    reader.parse (source);
  }

}
