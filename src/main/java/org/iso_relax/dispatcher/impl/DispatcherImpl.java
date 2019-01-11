/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2019 Philip Helger (www.helger.com)
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
package org.iso_relax.dispatcher.impl;

import java.util.Enumeration;
import java.util.Vector;

import org.iso_relax.dispatcher.Dispatcher;
import org.iso_relax.dispatcher.ElementDecl;
import org.iso_relax.dispatcher.IslandVerifier;
import org.iso_relax.dispatcher.SchemaProvider;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * reference implementation of Dispatcher interface.
 *
 * @author <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 */
public class DispatcherImpl implements Dispatcher
{
  protected static final class Context
  {
    protected final IslandVerifier m_aHandler;
    protected final int m_nDepth;
    protected final Context m_aPrevious;

    public Context (final IslandVerifier phandler, final int pdepth, final Context pprevious)
    {
      m_aHandler = phandler;
      m_nDepth = pdepth;
      m_aPrevious = pprevious;
    }
  }

  /**
   * relays SAX events to IslandVerifiers. This class is kept separate to make
   * document of Dispatcher cleaner (by removing SAX events from Dispatcher).
   */
  private class Transponder implements ContentHandler, DTDHandler
  {
    public void unparsedEntityDecl (final String name,
                                    final String systemId,
                                    final String publicId,
                                    final String notation)
    {
      // memorize unparsedEntityDecl
      m_aUnparsedEntityDecls.add (new UnparsedEntityDecl (name, systemId, publicId, notation));
    }

    public void notationDecl (final String name, final String systemId, final String publicId)
    {
      // memorize notationDecl
      m_aNotationDecls.add (new NotationDecl (name, systemId, publicId));
    }

    public void setDocumentLocator (final Locator locator)
    {
      m_aDocumentLocator = locator;
      m_aCurrentHandler.setDocumentLocator (locator);
    }

    public void startElement (final String uri,
                              final String localName,
                              final String qName,
                              final Attributes attributes) throws SAXException
    {
      m_aCurrentHandler.startElement (uri, localName, qName, attributes);
      m_nDepth++;
      m_aNSMap.pushContext ();
    }

    public void endElement (final String uri, final String localName, final String qName) throws SAXException
    {
      m_aNSMap.popContext ();
      m_aCurrentHandler.endElement (uri, localName, qName);

      if (--m_nDepth == 0)
      {// cut in and restore the previos IslandVerifier.

        // call endPrefixMapping for all pre-declared prefixes.
        final Enumeration <?> e = m_aNSMap.getDeclaredPrefixes ();
        while (e.hasMoreElements ())
          m_aCurrentHandler.endPrefixMapping ((String) e.nextElement ());

        // gets labels which are actually verified.
        final ElementDecl [] results = m_aCurrentHandler.endIsland ();

        // pop context
        m_nDepth = m_aContextStack.m_nDepth;
        m_aCurrentHandler = m_aContextStack.m_aHandler;
        m_aContextStack = m_aContextStack.m_aPrevious;

        // report assigned label to the parent
        m_aCurrentHandler.endChildIsland (uri, results);
      }
    }

    public void characters (final char ch[], final int start, final int length) throws SAXException
    {
      m_aCurrentHandler.characters (ch, start, length);
    }

    public void ignorableWhitespace (final char ch[], final int start, final int length) throws SAXException
    {
      m_aCurrentHandler.ignorableWhitespace (ch, start, length);
    }

    public void processingInstruction (final String target, final String data) throws SAXException
    {
      m_aCurrentHandler.processingInstruction (target, data);
    }

    public void skippedEntity (final String name) throws SAXException
    {
      m_aCurrentHandler.skippedEntity (name);
    }

    // those events should not be reported to island verifier.
    public void startDocument ()
    {}

    public void endDocument ()
    {}

    public void startPrefixMapping (final String prefix, final String uri) throws SAXException
    {
      m_aNSMap.declarePrefix (prefix, uri);
      m_aCurrentHandler.startPrefixMapping (prefix, uri);
    }

    public void endPrefixMapping (final String prefix) throws SAXException
    {
      m_aCurrentHandler.endPrefixMapping (prefix);
    }
  }

  /**
   * depth of the nesting of elements from the start of the current
   * IslandVerifier. this value has to start with 1 to prevent initial
   * IslandVerifier from being cut in.
   */
  private int m_nDepth = 1;
  protected Locator m_aDocumentLocator;

  protected final NamespaceSupport m_aNSMap = new NamespaceSupport ();
  protected ErrorHandler m_aErrorHandler;

  /** current validating processor which processes this island. */
  private IslandVerifier m_aCurrentHandler;

  /** Dispatcher will consult this object about schema information */
  protected final SchemaProvider m_aSchema;

  /** this object passes SAX events to IslandVerifier. */
  protected Transponder m_aTransponder;

  protected Context m_aContextStack;
  protected final Vector <UnparsedEntityDecl> m_aUnparsedEntityDecls = new Vector <> ();
  protected final Vector <NotationDecl> m_aNotationDecls = new Vector <> ();

  public DispatcherImpl (final SchemaProvider pschema)
  {
    m_aSchema = pschema;
    m_aTransponder = new Transponder ();
    m_aCurrentHandler = pschema.createTopLevelVerifier ();
    m_aCurrentHandler.setDispatcher (this);
  }

  public SchemaProvider getSchemaProvider ()
  {
    return m_aSchema;
  }

  public void attachXMLReader (final XMLReader reader)
  {
    reader.setContentHandler (m_aTransponder);
  }

  public void switchVerifier (final IslandVerifier newVerifier) throws SAXException
  {
    // push context
    m_aContextStack = new Context (m_aCurrentHandler, m_nDepth, m_aContextStack);

    m_aCurrentHandler = newVerifier;
    m_aCurrentHandler.setDispatcher (this);
    m_aCurrentHandler.setDocumentLocator (m_aDocumentLocator);
    m_nDepth = 0;

    // inform new IslandHandler about all prefix mappings
    final Enumeration <?> e = m_aNSMap.getDeclaredPrefixes ();
    while (e.hasMoreElements ())
    {
      final String prefix = (String) e.nextElement ();
      m_aCurrentHandler.startPrefixMapping (prefix, m_aNSMap.getURI (prefix));
    }
  }

  public void setErrorHandler (final ErrorHandler handler)
  {
    m_aErrorHandler = handler;
  }

  public ErrorHandler getErrorHandler ()
  {
    return m_aErrorHandler;
  }

  public int countUnparsedEntityDecls ()
  {
    return m_aUnparsedEntityDecls.size ();
  }

  public UnparsedEntityDecl getUnparsedEntityDecl (final int index)
  {
    return m_aUnparsedEntityDecls.get (index);
  }

  public int countNotationDecls ()
  {
    return m_aNotationDecls.size ();
  }

  public NotationDecl getNotationDecl (final int index)
  {
    return m_aNotationDecls.get (index);
  }
}
