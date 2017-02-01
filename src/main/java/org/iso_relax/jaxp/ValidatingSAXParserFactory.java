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
package org.iso_relax.jaxp;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.iso_relax.verifier.ISchema;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Wraps another {@link SAXParserFactory} and adds validation capability.
 *
 * @author Daisuke OKAJIMA
 */
public class ValidatingSAXParserFactory extends SAXParserFactory
{
  protected SAXParserFactory m_aWrappedFactory;
  protected ISchema m_aSchema;

  private boolean m_bValidation = true;

  /**
   * creates a new instance that wraps the default DocumentBuilderFactory
   *
   * @param schema
   *        the compiled Schema object. It can not be null.
   */
  public ValidatingSAXParserFactory (final ISchema schema)
  {
    this (SAXParserFactory.newInstance (), schema);
  }

  /**
   * creates a new instance with an internal SAXParserFactory and Schema.
   *
   * @param wrapped
   *        internal SAXParser
   * @param schema
   *        compiled schema.
   */
  public ValidatingSAXParserFactory (final SAXParserFactory wrapped, final ISchema schema)
  {
    m_aWrappedFactory = wrapped;
    m_aSchema = schema;
  }

  /**
   * returns a new SAX parser. If setValidating(false) is called previously,
   * this method simply returns the implementation of wrapped SAXParser.
   */
  @Override
  public SAXParser newSAXParser () throws ParserConfigurationException, SAXException
  {
    if (isValidating ())
    {
      try
      {
        return new ValidatingSAXParser (m_aWrappedFactory.newSAXParser (), m_aSchema.newVerifier ());
      }
      catch (final VerifierConfigurationException ex)
      {
        throw new ParserConfigurationException (ex.getMessage ());
      }
    }
    return m_aWrappedFactory.newSAXParser ();
  }

  /**
   * @see SAXParserFactory#setFeature(String, boolean)
   */
  @Override
  public void setFeature (final String name, final boolean value) throws ParserConfigurationException,
                                                                  SAXNotRecognizedException,
                                                                  SAXNotSupportedException
  {
    m_aWrappedFactory.setFeature (name, value);
  }

  /**
   * @see SAXParserFactory#getFeature(String)
   */
  @Override
  public boolean getFeature (final String name) throws ParserConfigurationException,
                                                SAXNotRecognizedException,
                                                SAXNotSupportedException
  {
    return m_aWrappedFactory.getFeature (name);
  }

  @Override
  public boolean isNamespaceAware ()
  {
    return m_aWrappedFactory.isNamespaceAware ();
  }

  @Override
  public void setNamespaceAware (final boolean awareness)
  {
    m_aWrappedFactory.setNamespaceAware (awareness);
  }

  @Override
  public boolean isValidating ()
  {
    return m_bValidation;
  }

  @Override
  public void setValidating (final boolean validating)
  {
    m_bValidation = validating;
  }
}
