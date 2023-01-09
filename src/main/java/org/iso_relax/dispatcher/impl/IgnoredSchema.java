/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2023 Philip Helger (www.helger.com)
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.iso_relax.dispatcher.AttributesDecl;
import org.iso_relax.dispatcher.AttributesVerifier;
import org.iso_relax.dispatcher.ElementDecl;
import org.iso_relax.dispatcher.IslandSchema;
import org.iso_relax.dispatcher.IslandVerifier;
import org.iso_relax.dispatcher.SchemaProvider;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXNotRecognizedException;

/**
 * IslandSchema implementation for "ignored" island. This schema exports
 * whatever importer wants, and anything is valid in this schema.
 *
 * @author <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 */
public class IgnoredSchema implements IslandSchema
{
  private static final ElementDecl [] s_aElemDecl = new ElementDecl [] { new ElementDecl ()
  {
    public String getName ()
    {
      return "$$any$$";
    }

    public Object getProperty (final String propertyName) throws SAXNotRecognizedException
    {
      throw new SAXNotRecognizedException (propertyName);
    }

    public boolean getFeature (final String featureName) throws SAXNotRecognizedException
    {
      throw new SAXNotRecognizedException (featureName);
    }
  } };

  private static final AttributesDecl [] s_aAttrDecl = new AttributesDecl [] { new AttributesDecl ()
  {
    public String getName ()
    {
      return "$$any$$";
    }

    public Object getProperty (final String propertyName) throws SAXNotRecognizedException
    {
      throw new SAXNotRecognizedException (propertyName);
    }

    public boolean getFeature (final String featureName) throws SAXNotRecognizedException
    {
      throw new SAXNotRecognizedException (featureName);
    }
  } };

  public ElementDecl getElementDeclByName (final String name)
  {
    return s_aElemDecl[0];
  }

  public ElementDecl [] getElementDecls ()
  {
    return s_aElemDecl;
  }

  public Iterator <ElementDecl> iterateElementDecls ()
  {
    final List <ElementDecl> vec = new ArrayList <> ();
    vec.add (s_aElemDecl[0]);
    return vec.iterator ();
  }

  public IslandVerifier createNewVerifier (final String namespaceURI, final ElementDecl [] rules)
  {
    return new IgnoreVerifier (namespaceURI, rules);
  }

  public AttributesDecl getAttributesDeclByName (final String name)
  {
    return s_aAttrDecl[0];
  }

  public AttributesDecl [] getAttributesDecls ()
  {
    return s_aAttrDecl;
  }

  public Iterator <AttributesDecl> iterateAttributesDecls ()
  {
    final List <AttributesDecl> vec = new ArrayList <> ();
    vec.add (s_aAttrDecl[0]);
    return vec.iterator ();
  }

  public AttributesVerifier createNewAttributesVerifier (final String namespaceURI, final AttributesDecl [] decls)
  {
    throw new UnsupportedOperationException ("not implemented yet");
  }

  public void bind (final SchemaProvider provider, final ErrorHandler handler)
  {}
}
