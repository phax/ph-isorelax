/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2021 Philip Helger (www.helger.com)
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

import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierFilter;
import org.iso_relax.verifier.VerifierHandler;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * plain vanilla {@link VerifierFilterImpl} implementation.
 * <p>
 * A verifier implementation can use this class to support VerifierFilter
 * functionality.
 * <p>
 * To use this class, implement the {@link Verifier#getVerifierFilter()} method
 * as follows:
 *
 * <pre>
 * public VerifierFilter getVerifierFilter () throws SAXException
 * {
 *   return new VerifierFilterImpl (getVerifierHandler ());
 * }
 * </pre>
 * <p>
 * Also, usually you may want to override <code>setErrorHandler</code> method so
 * that your <code>VerifierHandler</code> will send errors to that handler.
 *
 * @version $Id: VerifierFilterImpl.java,v 1.5 2003/05/30 23:46:33 kkawa Exp $
 * @author <a href="mailto:kohsuke.kawaguchi@sun.com">Kohsuke KAWAGUCHI</a>
 */
public class VerifierFilterImpl extends XMLFilterImpl implements VerifierFilter
{
  private final Verifier m_aVerifier;
  private final VerifierHandler m_aCore;

  public VerifierFilterImpl (final Verifier aVerifier) throws SAXException
  {
    m_aVerifier = aVerifier;
    m_aCore = m_aVerifier.getVerifierHandler ();
  }

  public boolean isValid ()
  {
    return m_aCore.isValid ();
  }

  @Override
  public void setErrorHandler (final ErrorHandler handler)
  {
    super.setErrorHandler (handler);
    // we need to call the setErrorHandler method of the verifier,
    // so that the verifier handler will use this error handler from now on.
    m_aVerifier.setErrorHandler (handler);
  }

  @Override
  public void setEntityResolver (final EntityResolver resolver)
  {
    super.setEntityResolver (resolver);
    m_aVerifier.setEntityResolver (resolver);
  }

  //
  //
  // ContentHandler events
  //
  //

  @Override
  public void setDocumentLocator (final Locator locator)
  {
    m_aCore.setDocumentLocator (locator);
    super.setDocumentLocator (locator);
  }

  @Override
  public void startDocument () throws SAXException
  {
    m_aCore.startDocument ();
    super.startDocument ();
  }

  @Override
  public void endDocument () throws SAXException
  {
    m_aCore.endDocument ();
    super.endDocument ();
  }

  @Override
  public void startPrefixMapping (final String prefix, final String uri) throws SAXException
  {
    m_aCore.startPrefixMapping (prefix, uri);
    super.startPrefixMapping (prefix, uri);
  }

  @Override
  public void endPrefixMapping (final String prefix) throws SAXException
  {
    m_aCore.endPrefixMapping (prefix);
    super.endPrefixMapping (prefix);
  }

  @Override
  public void startElement (final String uri,
                            final String localName,
                            final String qName,
                            final Attributes attributes) throws SAXException
  {
    m_aCore.startElement (uri, localName, qName, attributes);
    super.startElement (uri, localName, qName, attributes);
  }

  @Override
  public void endElement (final String uri, final String localName, final String qName) throws SAXException
  {
    m_aCore.endElement (uri, localName, qName);
    super.endElement (uri, localName, qName);
  }

  @Override
  public void characters (final char ch[], final int start, final int length) throws SAXException
  {
    m_aCore.characters (ch, start, length);
    super.characters (ch, start, length);
  }

  @Override
  public void ignorableWhitespace (final char ch[], final int start, final int length) throws SAXException
  {
    m_aCore.ignorableWhitespace (ch, start, length);
    super.ignorableWhitespace (ch, start, length);
  }

  @Override
  public void processingInstruction (final String target, final String data) throws SAXException
  {
    m_aCore.processingInstruction (target, data);
    super.processingInstruction (target, data);
  }

  @Override
  public void skippedEntity (final String name) throws SAXException
  {
    m_aCore.skippedEntity (name);
    super.skippedEntity (name);
  }
}
