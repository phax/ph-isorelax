/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2024 Philip Helger (www.helger.com)
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

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * ContentHandler that "forks" the incoming SAX2 events to two ContentHandlers.
 *
 * @version $Id: ForkContentHandler.java,v 1.3 2003/05/30 23:46:33 kkawa Exp $
 * @author <a href="mailto:kohsuke.kawaguchi@sun.com">Kohsuke KAWAGUCHI</a>
 */
public class ForkContentHandler implements ContentHandler
{
  private final ContentHandler m_aLhs;
  private final ContentHandler m_aRhs;

  /**
   * Creates a ForkContentHandler.
   *
   * @param first
   *        This handler will receive a SAX event first.
   * @param second
   *        This handler will receive a SAX event after the first handler
   *        receives it.
   */
  public ForkContentHandler (final ContentHandler first, final ContentHandler second)
  {
    m_aLhs = first;
    m_aRhs = second;
  }

  /**
   * Creates ForkContentHandlers so that the specified handlers will receive SAX
   * events in the order of the array.
   */
  public static ContentHandler create (final ContentHandler [] handlers)
  {
    if (handlers.length == 0)
      throw new IllegalArgumentException ();

    ContentHandler result = handlers[0];
    for (int i = 1; i < handlers.length; i++)
      result = new ForkContentHandler (result, handlers[i]);
    return result;
  }

  public void setDocumentLocator (final Locator locator)
  {
    m_aLhs.setDocumentLocator (locator);
    m_aRhs.setDocumentLocator (locator);
  }

  public void startDocument () throws SAXException
  {
    m_aLhs.startDocument ();
    m_aRhs.startDocument ();
  }

  public void endDocument () throws SAXException
  {
    m_aLhs.endDocument ();
    m_aRhs.endDocument ();
  }

  public void startPrefixMapping (final String prefix, final String uri) throws SAXException
  {
    m_aLhs.startPrefixMapping (prefix, uri);
    m_aRhs.startPrefixMapping (prefix, uri);
  }

  public void endPrefixMapping (final String prefix) throws SAXException
  {
    m_aLhs.endPrefixMapping (prefix);
    m_aRhs.endPrefixMapping (prefix);
  }

  public void startElement (final String uri,
                            final String localName,
                            final String qName,
                            final Attributes attributes) throws SAXException
  {
    m_aLhs.startElement (uri, localName, qName, attributes);
    m_aRhs.startElement (uri, localName, qName, attributes);
  }

  public void endElement (final String uri, final String localName, final String qName) throws SAXException
  {
    m_aLhs.endElement (uri, localName, qName);
    m_aRhs.endElement (uri, localName, qName);
  }

  public void characters (final char ch[], final int start, final int length) throws SAXException
  {
    m_aLhs.characters (ch, start, length);
    m_aRhs.characters (ch, start, length);
  }

  public void ignorableWhitespace (final char ch[], final int start, final int length) throws SAXException
  {
    m_aLhs.ignorableWhitespace (ch, start, length);
    m_aRhs.ignorableWhitespace (ch, start, length);
  }

  public void processingInstruction (final String target, final String data) throws SAXException
  {
    m_aLhs.processingInstruction (target, data);
    m_aRhs.processingInstruction (target, data);
  }

  public void skippedEntity (final String name) throws SAXException
  {
    m_aLhs.skippedEntity (name);
    m_aRhs.skippedEntity (name);
  }

}
