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
package org.iso_relax.verifier.impl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.iso_relax.verifier.VerifierFilter;
import org.iso_relax.verifier.VerifierHandler;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * Partial implementation of {@link VerifierImpl}.
 * <p>
 * This class is useful as the base class of the verifier implementation.
 * <p>
 * The only remaining method that has to be implemented by the derived class is
 * the <code>getVerifierHandler</code> method. Please be noted that applications
 * can call the <code>setErrorHandler</code> method after the
 * <code>getVerifierHandler</code> method and that change should take effect.
 *
 * @version $Id: VerifierImpl.java,v 1.4 2003/05/30 23:46:33 kkawa Exp $
 * @author <a href="mailto:kohsuke.kawaguchi@sun.com">Kohsuke KAWAGUCHI</a>
 */
public abstract class VerifierImpl implements Verifier
{
  protected XMLReader m_aReader;
  // default error handler must not report any error.
  protected ErrorHandler m_aErrorHandler = new ErrorHandler ()
  {
    public void warning (final SAXParseException e)
    {}

    public void error (final SAXParseException e)
    {}

    public void fatalError (final SAXParseException e)
    {}
  };
  protected EntityResolver m_aEntityResolver;
  private VerifierFilter m_aFilter;

  protected VerifierImpl () throws VerifierConfigurationException
  {
    prepareXMLReader ();
  }

  /**
   * Creates and sets a sole instance of XMLReader which will be used by this
   * verifier.
   */
  protected void prepareXMLReader () throws VerifierConfigurationException
  {
    try
    {
      final SAXParserFactory factory = SAXParserFactory.newInstance ();
      factory.setNamespaceAware (true);
      m_aReader = factory.newSAXParser ().getXMLReader ();
    }
    catch (final SAXException e)
    {
      throw new VerifierConfigurationException (e);
    }
    catch (final ParserConfigurationException pce)
    {
      throw new VerifierConfigurationException (pce);
    }
  }

  @SuppressWarnings ("deprecation")
  public boolean isFeature (final String feature) throws SAXNotRecognizedException, SAXNotSupportedException
  {

    if (FEATURE_HANDLER.equals (feature) || FEATURE_FILTER.equals (feature))
      return true;

    throw new SAXNotRecognizedException (feature);
  }

  public void setFeature (final String feature, final boolean value) throws SAXNotRecognizedException,
                                                                     SAXNotSupportedException
  {

    throw new SAXNotRecognizedException (feature);
  }

  public Object getProperty (final String property) throws SAXNotRecognizedException, SAXNotSupportedException
  {

    throw new SAXNotRecognizedException (property);
  }

  public void setProperty (final String property, final Object value) throws SAXNotRecognizedException,
                                                                      SAXNotSupportedException
  {

    throw new SAXNotRecognizedException (property);
  }

  public void setErrorHandler (final ErrorHandler handler)
  {
    this.m_aErrorHandler = handler;
  }

  public void setEntityResolver (final EntityResolver resolver)
  {
    this.m_aEntityResolver = resolver;
  }

  public boolean verify (final String uri) throws SAXException, IOException
  {
    return verify (new InputSource (uri));
  }

  public boolean verify (final InputSource source) throws SAXException, IOException
  {

    final VerifierHandler handler = getVerifierHandler ();

    m_aReader.setErrorHandler (m_aErrorHandler);
    if (m_aEntityResolver != null)
      m_aReader.setEntityResolver (m_aEntityResolver);
    m_aReader.setContentHandler (handler);
    m_aReader.parse (source);

    return handler.isValid ();
  }

  public boolean verify (final File f) throws SAXException, IOException
  {
    String uri = "file:" + f.getAbsolutePath ();
    if (File.separatorChar == '\\')
    {
      uri = uri.replace ('\\', '/');
    }
    return verify (new InputSource (uri));
  }

  public boolean verify (final Node node) throws SAXException
  {
    final SAXEventGenerator generator = new SAXEventGenerator (node);
    // generate startDocument/endDocument events
    generator.setDocumentEmulation (true);
    generator.setErrorHandler (m_aErrorHandler);
    final VerifierHandler handler = getVerifierHandler ();
    generator.makeEvent (handler);
    return handler.isValid ();
  }

  public abstract VerifierHandler getVerifierHandler () throws SAXException;

  public VerifierFilter getVerifierFilter () throws SAXException
  {
    if (m_aFilter == null)
      m_aFilter = new VerifierFilterImpl (this);
    return m_aFilter;
  }
}
