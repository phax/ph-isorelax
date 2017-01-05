/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2017 Philip Helger (www.helger.com)
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
  private boolean needDocumentEmulation_ = true;
  private final Node root_;
  private String systemID_;
  private String publicID_;
  private DTDHandler dtd_;
  private ContentHandler content_;
  private DeclHandler decl_;
  private LexicalHandler lexical_;
  private ErrorHandler error_;

  public DOMSAXProducer (final Node node)
  {
    root_ = node;
  }

  public void setDocumentEmulation (final boolean emulate)
  {
    needDocumentEmulation_ = emulate;
  }

  public void setSystemID (final String id)
  {
    systemID_ = id;
  }

  public void setPublicID (final String id)
  {
    publicID_ = id;
  }

  public void setDTDHandler (final DTDHandler dtd)
  {
    dtd_ = dtd;
  }

  public void setContentHandler (final ContentHandler content)
  {
    content_ = content;
  }

  public void setLexicalHandler (final LexicalHandler lexical)
  {
    lexical_ = lexical;
  }

  public void setDeclHandler (final DeclHandler decl)
  {
    decl_ = decl;
  }

  public void setErrorHandler (final ErrorHandler error)
  {
    error_ = error;
  }

  public void makeEvent () throws SAXException
  {
    try
    {
      final DOMSAXProducerVisitor visitor = new DOMSAXProducerVisitor ();
      visitor.setSystemID (systemID_);
      visitor.setPublicID (publicID_);
      visitor.setDTDHandler (dtd_);
      visitor.setContentHandler (content_);
      visitor.setLexicalHandler (lexical_);
      visitor.setDeclHandler (decl_);
      visitor.setErrorHandler (error_);
      if (!(root_ instanceof Document) && needDocumentEmulation_)
      {
        visitor.emulateStartDocument ();
        UDOMVisitor.traverse (root_, visitor);
        visitor.emulateEndDocument ();
      }
      else
      {
        UDOMVisitor.traverse (root_, visitor);
      }
    }
    catch (final DOMVisitorException e)
    {
      final Exception cause = e.getCauseException ();
      if (cause == null)
        throw (new SAXException (e.getMessage ()));
      if (cause instanceof SAXException)
        throw ((SAXException) cause);
      throw (new SAXException (e.getMessage ()));
    }
  }

  public void makeEvent (final ContentHandler handler) throws SAXException
  {
    setContentHandler (handler);
    makeEvent ();
  }
}
