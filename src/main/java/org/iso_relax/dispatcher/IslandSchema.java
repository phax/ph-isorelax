/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2026 Philip Helger (www.helger.com)
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
package org.iso_relax.dispatcher;

import java.util.Iterator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * represents a schema that validates one island.
 *
 * @author <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 */
public interface IslandSchema
{
  /**
   * creates a new IslandVerifier instance that is going to validate one island.
   *
   * @param namespaceURI
   *        namespace URI of the newly found element, which is going to be
   *        validated by the newly created IslandVerifier.
   * @param elementDecls
   *        set of ElementDecl objects that newly created verifier shall
   *        validate.
   */
  IslandVerifier createNewVerifier (String namespaceURI, ElementDecl [] elementDecls);

  /**
   * gets exported elementDecl object that has specified name.
   *
   * @return null if no elementDecl is exported under the given name.
   */
  ElementDecl getElementDeclByName (String name);

  /** iterates all exported elementDecl objects. */
  Iterator <ElementDecl> iterateElementDecls ();

  /** returns all exported elementDecl objects at once. */
  ElementDecl [] getElementDecls ();

  /**
   * gets exported AttributesDecl object that has specified name.
   *
   * @return null if no AttributesDecl is exported under the given name.
   */
  AttributesDecl getAttributesDeclByName (String name);

  /** iterates all exported attributesDecl objects. */
  Iterator <AttributesDecl> iterateAttributesDecls ();

  /** returns all exported attributesDecl objects at once. */
  AttributesDecl [] getAttributesDecls ();

  /**
   * creates a new AttributesVerifier instance that is going to validate
   * attribute declarations.
   *
   * @param namespaceURI
   *        namespace URI of the attributes, which is going to be validated by
   *        the newly created verifier.
   * @param decls
   *        set of AttributesDecl objects that newly created verifier shall
   *        validate.
   */
  AttributesVerifier createNewAttributesVerifier (String namespaceURI, AttributesDecl [] decls);

  /**
   * binds references to imported elementDecls by using given provider. this
   * method is only called once before the first validation starts.
   *
   * @exception SAXException
   *            any error has to be reported to ErrorHandler first.
   */
  void bind (SchemaProvider provider, ErrorHandler errorHandler) throws SAXException;
}
