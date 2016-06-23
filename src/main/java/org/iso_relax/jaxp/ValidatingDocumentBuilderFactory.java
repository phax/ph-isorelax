/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016 Philip Helger (www.helger.com)
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
  protected Schema _Schema;
  protected DocumentBuilderFactory _WrappedFactory;

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
    _WrappedFactory = wrapped;
    _Schema = schema;
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
        return new ValidatingDocumentBuilder (_WrappedFactory.newDocumentBuilder (), _Schema.newVerifier ());
      }
      catch (final VerifierConfigurationException ex)
      {
        throw new ParserConfigurationException (ex.getMessage ());
      }
    }
    // if validation is disabled, we simply return the implementation of
    // wrapped DocumentBuilder
    return _WrappedFactory.newDocumentBuilder ();
  }

  @Override
  public void setFeature (final String name, final boolean value) throws ParserConfigurationException
  {
    _WrappedFactory.setFeature (name, value);
  }

  @Override
  public boolean getFeature (final String name) throws ParserConfigurationException
  {
    return _WrappedFactory.getFeature (name);
  }

  /**
   * @see DocumentBuilderFactory#setAttribute(String, Object)
   */
  @Override
  public void setAttribute (final String name, final Object value)
  {
    _WrappedFactory.setAttribute (name, value);
  }

  /**
   * @see DocumentBuilderFactory#getAttribute(String)
   */
  @Override
  public Object getAttribute (final String name)
  {
    return _WrappedFactory.getAttribute (name);
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
    return _WrappedFactory.isCoalescing ();
  }

  public boolean isExpandEntityReference ()
  {
    return _WrappedFactory.isExpandEntityReferences ();
  }

  @Override
  public boolean isIgnoringComments ()
  {
    return _WrappedFactory.isIgnoringComments ();
  }

  @Override
  public boolean isIgnoringElementContentWhitespace ()
  {
    return _WrappedFactory.isIgnoringElementContentWhitespace ();
  }

  @Override
  public boolean isNamespaceAware ()
  {
    return _WrappedFactory.isNamespaceAware ();
  }

  @Override
  public void setCoalescing (final boolean coalescing)
  {
    _WrappedFactory.setCoalescing (coalescing);
  }

  public void setExpandEntityReference (final boolean expandEntityRef)
  {
    _WrappedFactory.setExpandEntityReferences (expandEntityRef);
  }

  @Override
  public void setIgnoringComments (final boolean ignoreComments)
  {
    _WrappedFactory.setIgnoringComments (ignoreComments);
  }

  @Override
  public void setIgnoringElementContentWhitespace (final boolean whitespace)
  {
    _WrappedFactory.setIgnoringElementContentWhitespace (whitespace);
  }

  @Override
  public void setNamespaceAware (final boolean awareness)
  {
    _WrappedFactory.setNamespaceAware (awareness);
  }
}
