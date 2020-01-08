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
package org.iso_relax.dispatcher.impl;

import org.iso_relax.dispatcher.Dispatcher;
import org.iso_relax.dispatcher.ElementDecl;
import org.iso_relax.dispatcher.IslandSchema;
import org.iso_relax.dispatcher.IslandVerifier;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * ignores namespaces which have no associated grammar.
 *
 * @author <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 */
public final class IgnoreVerifier extends DefaultHandler implements IslandVerifier
{
  private final ElementDecl [] m_aRules;
  /**
   * elements in this namespace is validated by this IgnoreVerifier.
   */
  private final String m_sNamespaceToIgnore;
  private Dispatcher m_aDispatcher;

  /**
   * @param assignedRules
   *        this Verifier is supposed to validate these rules. since this
   *        IslandVerifier actually does nothing, all these rules will be
   *        reported as satisfied upon completion.
   */
  public IgnoreVerifier (final String pnamespaceToIgnore, final ElementDecl [] assignedRules)
  {
    m_sNamespaceToIgnore = pnamespaceToIgnore;
    m_aRules = assignedRules;
  }

  public ElementDecl [] endIsland ()
  {
    return m_aRules;
  }

  public void endChildIsland (final String uri, final ElementDecl [] assignedLabels)
  {}

  public void setDispatcher (final Dispatcher disp)
  {
    m_aDispatcher = disp;
  }

  @Override
  public void startElement (final String namespaceURI,
                            final String localName,
                            final String qName,
                            final Attributes attributes) throws SAXException
  {
    if (m_sNamespaceToIgnore.equals (namespaceURI))
    {
      // this element is "validated".
      return;
    }

    // try to locate a grammar of this namespace
    final IslandSchema is = m_aDispatcher.getSchemaProvider ().getSchemaByNamespace (namespaceURI);
    if (is == null)
    {
      // no grammar is declared with this namespace URI.
      return; // continue ignoring.
    }

    // a schema is found: revert to normal mode and validate them.
    final IslandVerifier iv = is.createNewVerifier (namespaceURI, is.getElementDecls ());
    m_aDispatcher.switchVerifier (iv);

    // simulate this startElement method.
    iv.startElement (namespaceURI, localName, qName, attributes);
  }
}
