/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2025 Philip Helger (www.helger.com)
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

import java.util.Enumeration;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;
import org.xml.sax.helpers.NamespaceSupport;

import jp.gr.xml.relax.dom.DOMVisitorException;
import jp.gr.xml.relax.dom.IDOMVisitor;
import jp.gr.xml.relax.dom.UDOM;
import jp.gr.xml.relax.dom.UDOMVisitor;
import jp.gr.xml.relax.sax.DeclHandlerBase;
import jp.gr.xml.relax.sax.LexicalHandlerBase;

/**
 * Generates SAX events from a DOM tree.
 * <p>
 * call the <code>makeEvent</code> method to use it.
 *
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 *         <a href="kohsuke.kawaguchi@sun.com">Kohsuke KAWAGUCHI</a>
 */
public class SAXEventGenerator implements IDOMVisitor
{
  private final Node m_aRoot;
  private boolean m_bNeedDocumentEmulation = true;

  private String m_sSystemID;
  private String m_sPublicID;
  private DTDHandler m_aDTD;
  private ContentHandler m_aContent;
  private DeclHandler m_aDecl;
  private LexicalHandler m_aLexical;
  private ErrorHandler m_aError;
  private final NamespaceSupport m_aNamespace = new NamespaceSupport ();

  public SAXEventGenerator (final Node node)
  {
    m_aRoot = node;

    final DefaultHandler handler = new DefaultHandler ();
    m_aDTD = handler;
    m_aContent = handler;
    m_aError = handler;
    m_aLexical = new LexicalHandlerBase ();
    m_aDecl = new DeclHandlerBase ();
  }

  public void setSystemID (final String id)
  {
    m_sSystemID = id;
  }

  public void setPublicID (final String id)
  {
    m_sPublicID = id;
  }

  public void setDocumentEmulation (final boolean emulate)
  {
    m_bNeedDocumentEmulation = emulate;
  }

  public void setDTDHandler (final DTDHandler dtd)
  {
    m_aDTD = dtd;
  }

  public void setContentHandler (final ContentHandler content)
  {
    m_aContent = content;
  }

  public void setLexicalHandler (final LexicalHandler lexical)
  {
    m_aLexical = lexical;
  }

  public void setDeclHandler (final DeclHandler decl)
  {
    m_aDecl = decl;
  }

  public void setErrorHandler (final ErrorHandler error)
  {
    m_aError = error;
  }

  public void makeEvent () throws SAXException
  {
    try
    {
      if (!(m_aRoot instanceof Document) && m_bNeedDocumentEmulation)
      {
        emulateStartDocument ();
        UDOMVisitor.traverse (m_aRoot, this);
        emulateEndDocument ();
      }
      else
      {
        UDOMVisitor.traverse (m_aRoot, this);
      }
    }
    catch (final DOMVisitorException e)
    {
      final Throwable cause = e.getCause ();
      if (cause == null)
        throw new SAXException (e.getMessage ());
      if (cause instanceof SAXException)
        throw (SAXException) cause;
      throw new SAXException (e);
    }
  }

  public void makeEvent (final ContentHandler handler) throws SAXException
  {
    setContentHandler (handler);
    makeEvent ();
  }

  public void emulateStartDocument ()
  {
    try
    {
      _handleLocator ();
      m_aContent.startDocument ();
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  public void emulateEndDocument ()
  {
    try
    {
      m_aContent.endDocument ();
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  public boolean enter (final Element element)
  {
    try
    {
      m_aNamespace.pushContext ();
      String namespaceURI = element.getNamespaceURI ();
      if (namespaceURI == null)
      {
        namespaceURI = "";
      }
      String localName = element.getLocalName ();
      final String qName = element.getTagName ();
      if (localName == null)
        localName = qName;

      final NamedNodeMap attrMap = element.getAttributes ();
      final AttributesImpl attrs = new AttributesImpl ();
      final int size = attrMap.getLength ();
      for (int i = 0; i < size; i++)
      {
        final Attr attr = (Attr) attrMap.item (i);
        String attrNamespaceURI = attr.getNamespaceURI ();
        if (attrNamespaceURI == null)
        {
          attrNamespaceURI = "";
        }
        String attrLocalName = attr.getLocalName ();
        final String attrQName = attr.getName ();
        if (attrLocalName == null)
          attrLocalName = attrQName;
        final String attrValue = attr.getValue ();
        if (attrQName.startsWith ("xmlns"))
        {
          String prefix;
          final int index = attrQName.indexOf (':');
          if (index == -1)
          {
            prefix = "";
          }
          else
          {
            prefix = attrQName.substring (index + 1);
          }
          if (!m_aNamespace.declarePrefix (prefix, attrValue))
          {
            _errorReport ("bad prefix = " + prefix);
          }
          else
          {
            m_aContent.startPrefixMapping (prefix, attrValue);
          }
        }
        else
        {
          attrs.addAttribute (attrNamespaceURI, attrLocalName, attrQName, "CDATA", attrValue);
        }
      }
      m_aContent.startElement (namespaceURI, localName, qName, attrs);
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
    return (true);
  }

  public boolean enter (final Text text)
  {
    try
    {
      final String data = text.getData ();
      m_aContent.characters (data.toCharArray (), 0, data.length ());
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
    return (false);
  }

  public boolean enter (final CDATASection cdata)
  {
    try
    {
      m_aLexical.startCDATA ();
      final String data = cdata.getData ();
      m_aContent.characters (data.toCharArray (), 0, data.length ());
      m_aLexical.endCDATA ();
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
    return (false);
  }

  public boolean enter (final EntityReference entityRef)
  {
    try
    {
      m_aLexical.startEntity (entityRef.getNodeName ());
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
    return (true);
  }

  public boolean enter (final ProcessingInstruction pi)
  {
    try
    {
      m_aContent.processingInstruction (pi.getTarget (), pi.getData ());
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
    return (false);
  }

  public boolean enter (final Comment comment)
  {
    try
    {
      final String data = comment.getData ();
      m_aLexical.comment (data.toCharArray (), 0, data.length ());
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
    return (false);
  }

  public boolean enter (final Document doc)
  {
    try
    {
      _handleLocator ();
      m_aContent.startDocument ();
      _handleDoctype (doc.getDoctype ());
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
    return (true);
  }

  private void _handleLocator ()
  {
    if (m_sSystemID == null && m_sPublicID == null)
    {
      return;
    }
    _locatorEvent ();
  }

  private void _locatorEvent ()
  {
    final LocatorImpl locator = new LocatorImpl ();
    locator.setSystemId (m_sSystemID);
    locator.setPublicId (m_sPublicID);
    locator.setLineNumber (-1);
    locator.setColumnNumber (-1);
    m_aContent.setDocumentLocator (locator);
  }

  private void _handleDoctype (final DocumentType doctype)
  {
    try
    {
      if (doctype == null)
      {
        return;
      }
      final String systemID = doctype.getSystemId ();
      final String publicID = doctype.getPublicId ();
      final String internalSubset = doctype.getInternalSubset ();
      if (systemID != null)
      {
        m_aLexical.startDTD (doctype.getName (), publicID, systemID);
        if (internalSubset == null)
        {
          m_aLexical.endDTD ();
          _handleEntities (doctype);
        }
        else
        {
          _handleEntities (doctype);
          m_aLexical.endDTD ();
        }
      }
      else
      {
        _handleEntities (doctype);
      }
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  private void _handleEntities (final DocumentType doctype)
  {
    try
    {
      final NamedNodeMap entities = doctype.getEntities ();
      final int nEntities = entities.getLength ();
      for (int i = 0; i < nEntities; i++)
      {
        final Entity entity = (Entity) entities.item (i);
        final String publicID = entity.getPublicId ();
        final String systemID = entity.getSystemId ();
        final String notationName = entity.getNotationName ();
        if (publicID != null || systemID != null)
        {
          _handleExternalEntity (entity.getNodeName (), publicID, systemID, notationName);
        }
        else
        {
          _handleInternalEntity (entity);
        }
      }
      final NamedNodeMap notations = doctype.getNotations ();
      final int nNotations = notations.getLength ();
      for (int i = 0; i < nNotations; i++)
      {
        final Notation notation = (Notation) notations.item (i);
        final String publicID = notation.getPublicId ();
        final String systemID = notation.getSystemId ();
        m_aDTD.notationDecl (notation.getNodeName (), publicID, systemID);
      }
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  private void _handleExternalEntity (final String name,
                                      final String publicID,
                                      final String systemID,
                                      final String notationName)
  {
    try
    {
      if (notationName == null)
      {
        m_aDecl.externalEntityDecl (name, publicID, systemID);
      }
      else
      {
        m_aDTD.unparsedEntityDecl (name, publicID, systemID, notationName);
      }
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  private void _handleInternalEntity (final Entity entity)
  {
    try
    {
      m_aDecl.internalEntityDecl (entity.getNodeName (), UDOM.getXMLText (entity));
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  public boolean enter (final DocumentFragment docfrag)
  {
    return (true);
  }

  public void leave (final Element element)
  {
    try
    {
      String namespaceURI = element.getNamespaceURI ();
      if (namespaceURI == null)
      {
        namespaceURI = "";
      }
      String localName = element.getLocalName ();
      final String qName = element.getTagName ();
      if (localName == null)
        localName = qName;
      m_aContent.endElement (namespaceURI, localName, qName);

      for (final Enumeration <?> e = m_aNamespace.getDeclaredPrefixes (); e.hasMoreElements ();)
        m_aContent.endPrefixMapping ((String) e.nextElement ());

      m_aNamespace.popContext ();
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  public void leave (final EntityReference entityRef)
  {
    try
    {
      m_aLexical.endEntity (entityRef.getNodeName ());
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  public void leave (final Document doc)
  {
    try
    {
      m_aContent.endDocument ();
    }
    catch (final SAXException e)
    {
      _errorReport (e);
    }
  }

  private void _errorReport (final String message) throws DOMVisitorException
  {
    _errorReport (new SAXParseException (message, m_sPublicID, m_sSystemID, -1, -1));
  }

  private void _errorReport (final SAXException e) throws DOMVisitorException
  {
    try
    {
      SAXParseException parseException;
      if (e instanceof SAXParseException)
      {
        parseException = (SAXParseException) e;
      }
      else
      {
        parseException = new SAXParseException (e.getMessage (), m_sPublicID, m_sSystemID, -1, -1, e);
      }
      m_aError.fatalError (parseException);
      throw new DOMVisitorException (e);
    }
    catch (final SAXException ee)
    {
      throw new DOMVisitorException (ee);
    }
  }
}
