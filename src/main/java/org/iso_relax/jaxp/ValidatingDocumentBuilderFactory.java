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
package org.iso_relax.jaxp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.VerifierConfigurationException;

/**
 * Wraps another {@link DocumentBuilderFactory} and adds validation capability.
 *
 * @author Daisuke OKAJIMA
 */
public class ValidatingDocumentBuilderFactory extends DocumentBuilderFactory
{
  protected Schema m_aSchema;
  protected DocumentBuilderFactory m_aWrappedFactory;

  private boolean m_bValidation = true;

  /**
   * creates a new instance that wraps the default DocumentBuilderFactory
   *
   * @param schema
   *        the compiled Schema object. It can not be null.
   */
  public ValidatingDocumentBuilderFactory (final Schema schema)
  {
    this (DocumentBuilderFactory.newInstance (), schema);
  }

  /**
   * creates a new instance with an internal DocumentBuilderFactory and Schema.
   *
   * @param wrapped
   *        internal DocumentBuilderFactory
   * @param schema
   *        compiled schema.
   */
  public ValidatingDocumentBuilderFactory (final DocumentBuilderFactory wrapped, final Schema schema)
  {
    m_aWrappedFactory = wrapped;
    m_aSchema = schema;
  }

  /**
   * returns a new DOM parser. If setValidating(false) is called previously,
   * this method simply returns the implementation of wrapped DocumentBuilder.
   */
  @Override
  public DocumentBuilder newDocumentBuilder () throws ParserConfigurationException
  {
    if (isValidating ())
    {
      try
      {
        return new ValidatingDocumentBuilder (m_aWrappedFactory.newDocumentBuilder (), m_aSchema.newVerifier ());
      }
      catch (final VerifierConfigurationException ex)
      {
        throw new ParserConfigurationException (ex.getMessage ());
      }
    }
    // if validation is disabled, we simply return the implementation of
    // wrapped DocumentBuilder
    return m_aWrappedFactory.newDocumentBuilder ();
  }

  @Override
  public void setFeature (final String name, final boolean value) throws ParserConfigurationException
  {
    m_aWrappedFactory.setFeature (name, value);
  }

  @Override
  public boolean getFeature (final String name) throws ParserConfigurationException
  {
    return m_aWrappedFactory.getFeature (name);
  }

  /**
   * @see DocumentBuilderFactory#setAttribute(String, Object)
   */
  @Override
  public void setAttribute (final String name, final Object value)
  {
    m_aWrappedFactory.setAttribute (name, value);
  }

  /**
   * @see DocumentBuilderFactory#getAttribute(String)
   */
  @Override
  public Object getAttribute (final String name)
  {
    return m_aWrappedFactory.getAttribute (name);
  }

  @Override
  public boolean isValidating ()
  {
    return m_bValidation;
  }

  @Override
  public void setValidating (final boolean _validating)
  {
    this.m_bValidation = _validating;
  }

  @Override
  public boolean isCoalescing ()
  {
    return m_aWrappedFactory.isCoalescing ();
  }

  public boolean isExpandEntityReference ()
  {
    return m_aWrappedFactory.isExpandEntityReferences ();
  }

  @Override
  public boolean isIgnoringComments ()
  {
    return m_aWrappedFactory.isIgnoringComments ();
  }

  @Override
  public boolean isIgnoringElementContentWhitespace ()
  {
    return m_aWrappedFactory.isIgnoringElementContentWhitespace ();
  }

  @Override
  public boolean isNamespaceAware ()
  {
    return m_aWrappedFactory.isNamespaceAware ();
  }

  @Override
  public void setCoalescing (final boolean coalescing)
  {
    m_aWrappedFactory.setCoalescing (coalescing);
  }

  public void setExpandEntityReference (final boolean expandEntityRef)
  {
    m_aWrappedFactory.setExpandEntityReferences (expandEntityRef);
  }

  @Override
  public void setIgnoringComments (final boolean ignoreComments)
  {
    m_aWrappedFactory.setIgnoringComments (ignoreComments);
  }

  @Override
  public void setIgnoringElementContentWhitespace (final boolean whitespace)
  {
    m_aWrappedFactory.setIgnoringElementContentWhitespace (whitespace);
  }

  @Override
  public void setNamespaceAware (final boolean awareness)
  {
    m_aWrappedFactory.setNamespaceAware (awareness);
  }
}
