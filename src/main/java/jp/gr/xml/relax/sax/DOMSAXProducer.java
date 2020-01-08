/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2020 Philip Helger (www.helger.com)
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
package jp.gr.xml.relax.sax;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import jp.gr.xml.relax.dom.DOMVisitorException;
import jp.gr.xml.relax.dom.UDOMVisitor;

/**
 * SAX event producer from DOM tree
 *
 * @since Feb. 18, 2001
 * @version Feb. 24, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class DOMSAXProducer
{
  private boolean m_bNeedDocumentEmulation = true;
  private final Node m_aRoot;
  private String m_sSystemID;
  private String m_sPublicID;
  private DTDHandler m_aDtd;
  private ContentHandler m_aContent;
  private DeclHandler m_aDecl;
  private LexicalHandler m_aLexical;
  private ErrorHandler m_aError;

  public DOMSAXProducer (final Node node)
  {
    m_aRoot = node;
  }

  public void setDocumentEmulation (final boolean emulate)
  {
    m_bNeedDocumentEmulation = emulate;
  }

  public void setSystemID (final String id)
  {
    m_sSystemID = id;
  }

  public void setPublicID (final String id)
  {
    m_sPublicID = id;
  }

  public void setDTDHandler (final DTDHandler dtd)
  {
    m_aDtd = dtd;
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
      final DOMSAXProducerVisitor visitor = new DOMSAXProducerVisitor ();
      visitor.setSystemID (m_sSystemID);
      visitor.setPublicID (m_sPublicID);
      visitor.setDTDHandler (m_aDtd);
      visitor.setContentHandler (m_aContent);
      visitor.setLexicalHandler (m_aLexical);
      visitor.setDeclHandler (m_aDecl);
      visitor.setErrorHandler (m_aError);
      if (!(m_aRoot instanceof Document) && m_bNeedDocumentEmulation)
      {
        visitor.emulateStartDocument ();
        UDOMVisitor.traverse (m_aRoot, visitor);
        visitor.emulateEndDocument ();
      }
      else
      {
        UDOMVisitor.traverse (m_aRoot, visitor);
      }
    }
    catch (final DOMVisitorException e)
    {
      final Throwable cause = e.getCause ();
      if (cause == null)
        throw new SAXException (e.getMessage ());
      if (cause instanceof SAXException)
        throw (SAXException) cause;
      throw new SAXException (e.getMessage ());
    }
  }

  public void makeEvent (final ContentHandler handler) throws SAXException
  {
    setContentHandler (handler);
    makeEvent ();
  }
}
