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
package jp.gr.xml.relax.dom;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

import jp.gr.xml.relax.xml.UXML;

/**
 * XMLMaker
 *
 * @since Oct. 27, 2000
 * @version Feb. 24, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class XMLMaker implements IDOMVisitor
{
  protected StringBuilder m_aSB = new StringBuilder ();
  protected Charset m_aEncoding = StandardCharsets.UTF_8;
  protected boolean m_bDOM2 = false;
  protected boolean m_bExpandEntityReference = false;
  protected boolean m_bEmptyElementTag = false;

  public XMLMaker ()
  {}

  public void setEncoding (final Charset encoding)
  {
    m_aEncoding = encoding;
  }

  public void setDOM2 (final boolean dom2)
  {
    m_bDOM2 = dom2;
  }

  public void setExpandEntityReference (final boolean expand)
  {
    m_bExpandEntityReference = expand;
  }

  public void setEmptyElementTag (final boolean empty)
  {
    m_bEmptyElementTag = empty;
  }

  public String getText ()
  {
    return m_aSB.toString ();
  }

  public boolean enter (final Element element)
  {
    final String tag = element.getTagName ();
    m_aSB.append ('<').append (tag);
    final NamedNodeMap attrs = element.getAttributes ();
    final int nAttrs = attrs.getLength ();
    for (int i = 0; i < nAttrs; i++)
    {
      final Attr attr = (Attr) attrs.item (i);
      if (attr.getSpecified ())
      {
        m_aSB.append (' ');
        enter (attr);
        leave (attr);
      }
    }
    m_aSB.append ('>');
    return true;
  }

  public void leave (final Element element)
  {
    final String tag = element.getTagName ();
    m_aSB.append ("</").append (tag).append ('>');
  }

  public boolean enter (final Attr attr)
  {
    m_aSB.append (attr.getName ()).append ("=\"").append (UXML.escapeAttrQuot (attr.getValue ())).append ('"');
    return true;
  }

  public boolean enter (final Text text)
  {
    m_aSB.append (UXML.escapeCharData (text.getData ()));
    return true;
  }

  public boolean enter (final CDATASection cdata)
  {
    m_aSB.append ("<![CDATA[").append (cdata.getData ()).append ("]]>");
    return true;
  }

  public boolean enter (final EntityReference entityRef)
  {
    m_aSB.append ("&").append (entityRef.getNodeName ()).append (";");
    return false;
  }

  public boolean enter (final Entity entity)
  {
    final String name = entity.getNodeName ();
    final String pid = entity.getPublicId ();
    final String sid = entity.getSystemId ();
    final String notation = entity.getNotationName ();
    m_aSB.append ("<!ENTITY ").append (name);
    if (sid != null)
    {
      if (pid != null)
        m_aSB.append (" PUBLIC \"").append (pid).append ("\" \"").append (UXML.escapeSystemQuot (sid)).append ("\">");
      else
        m_aSB.append (" SYSTEM \"").append (UXML.escapeSystemQuot (sid)).append ("\">");
      if (notation != null)
        m_aSB.append (" NDATA ").append (notation).append (">");
    }
    else
    {
      m_aSB.append (" \"");
      final XMLMaker entityMaker = new XMLMaker ();
      UDOMVisitor.traverseChildren (entity, entityMaker);
      m_aSB.append (UXML.escapeEntityQuot (entityMaker.getText ())).append ("\"").append (">");
    }
    return false;
  }

  public boolean enter (final ProcessingInstruction pi)
  {
    m_aSB.append ("<?").append (pi.getTarget ()).append (" ").append (pi.getData ()).append ("?>");
    return true;
  }

  public boolean enter (final Comment comment)
  {
    m_aSB.append ("<!--").append (comment.getData ()).append ("-->");
    return (true);
  }

  public boolean enter (final Document doc)
  {
    m_aSB.append ("<?xml version=\"1.0\" encoding=\"").append (m_aEncoding.name ()).append ("\" ?>\n");
    return (true);
  }

  public boolean enter (final DocumentType doctype)
  {
    if (m_bDOM2)
    {
      final String name = doctype.getName ();
      final String publicId = doctype.getPublicId ();
      final String systemId = doctype.getSystemId ();
      final String internalSubset = doctype.getInternalSubset ();
      m_aSB.append ("<!DOCTYPE ").append (name);
      if (publicId != null)
        m_aSB.append (" PUBLIC \"").append (publicId).append ("\"");
      if (systemId != null)
        m_aSB.append (" SYSTEM \"").append (systemId).append ("\"");
      if (internalSubset != null)
        m_aSB.append (" [").append (internalSubset).append ("]");
      m_aSB.append (">\n");
      return (true);
    }

    {
      final String name = doctype.getName ();
      final NamedNodeMap entities = doctype.getEntities ();
      final NamedNodeMap notations = doctype.getNotations ();
      m_aSB.append ("<!DOCTYPE ").append (name);
      if (entities != null && entities.getLength () > 0 || notations != null && notations.getLength () > 0)
      {

        m_aSB.append (" [");
        final int nEntities = entities.getLength ();
        for (int i = 0; i < nEntities; i++)
        {
          final XMLMaker entityMaker = new XMLMaker ();
          UDOMVisitor.traverse (entities.item (i), entityMaker);
          m_aSB.append (entityMaker.getText ());
        }
        final int nNotations = notations.getLength ();
        for (int i = 0; i < nNotations; i++)
        {
          enter ((Notation) notations.item (i));
          leave ((Notation) notations.item (i));
        }
        m_aSB.append ("]");
      }
      m_aSB.append (">\n");
      return (true);
    }
  }

  public boolean enter (final DocumentFragment docfrag)
  {
    // do nothing
    return true;
  }

  public boolean enter (final Notation notation)
  {
    final String name = notation.getNodeName ();
    final String pid = notation.getPublicId ();
    final String sid = notation.getSystemId ();
    m_aSB.append ("<!NOTATION ").append (name);
    if (pid != null)
    {
      m_aSB.append (" PUBLIC \"").append (pid).append ("\"");
      if (sid != null)
      {
        m_aSB.append (" \"").append (UXML.escapeSystemQuot (sid)).append ("\"");
      }
    }
    else
      if (sid != null)
      {
        m_aSB.append (" SYSTEM \"").append (UXML.escapeSystemQuot (sid)).append ("\"");
      }
    m_aSB.append (">");
    return (true);
  }

  public boolean enter (final Node node)
  {
    throw new InternalError (node.toString ());
  }

  public void leave (final Node node)
  {
    throw new InternalError (node.toString ());
  }

  public boolean isParsedEntity (final EntityReference entityRef)
  {
    final String name = entityRef.getNodeName ();
    final Document doc = entityRef.getOwnerDocument ();
    final DocumentType doctype = doc.getDoctype ();
    if (doctype == null)
      return false;

    final NamedNodeMap entities = doctype.getEntities ();
    final Entity entity = (Entity) entities.getNamedItem (name);
    if (entity == null)
      return false;

    return entity.getNotationName () == null;
  }
}
