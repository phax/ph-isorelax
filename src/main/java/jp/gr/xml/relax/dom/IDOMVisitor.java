/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2022 Philip Helger (www.helger.com)
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

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * IDOMVisitor
 *
 * @since Oct. 7, 2000
 * @version Feb. 24, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
@SuppressWarnings ("unused")
public interface IDOMVisitor
{
  default boolean enter (final Element element) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final Attr attr) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final Text text) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final CDATASection cdata) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final EntityReference entityRef) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final Entity entity) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final ProcessingInstruction pi) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final Comment comment) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final Document doc) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final DocumentType doctype) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final DocumentFragment docfrag) throws DOMVisitorException
  {
    return false;
  }

  default boolean enter (final Notation notation) throws DOMVisitorException
  {
    return false;
  }

  /**
   * Fallback for all "other" node types not explicitly handled.
   * 
   * @param node
   *        Current node.
   * @return <code>true</code> to enter, <code>false</code> otherwise
   * @throws DOMVisitorException
   *         Just in case
   */
  default boolean enter (final Node node) throws DOMVisitorException
  {
    return false;
  }

  default void leave (final Element element) throws DOMVisitorException
  {}

  default void leave (final Attr attr) throws DOMVisitorException
  {}

  default void leave (final Text text) throws DOMVisitorException
  {}

  default void leave (final CDATASection cdata) throws DOMVisitorException
  {}

  default void leave (final EntityReference entityRef) throws DOMVisitorException
  {}

  default void leave (final Entity entity) throws DOMVisitorException
  {}

  default void leave (final ProcessingInstruction pi) throws DOMVisitorException
  {}

  default void leave (final Comment comment) throws DOMVisitorException
  {}

  default void leave (final Document doc) throws DOMVisitorException
  {}

  default void leave (final DocumentType doctype) throws DOMVisitorException
  {}

  default void leave (final DocumentFragment docfrag) throws DOMVisitorException
  {}

  default void leave (final Notation notation) throws DOMVisitorException
  {}

  default void leave (final Node node) throws DOMVisitorException
  {}
}
