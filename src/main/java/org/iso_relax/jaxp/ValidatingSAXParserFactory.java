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

import org.iso_relax.verifier.Schema;
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
  protected SAXParserFactory _WrappedFactory;
  protected Schema _Schema;

  private boolean validation = true;

  /**
   * creates a new instance that wraps the default DocumentBuilderFactory
   *
   * @param schema
   *        the compiled Schema object. It can not be null.
   */
  public ValidatingSAXParserFactory (final Schema schema)
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
  public ValidatingSAXParserFactory (final SAXParserFactory wrapped, final Schema schema)
  {
    _WrappedFactory = wrapped;
    _Schema = schema;
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
        return new ValidatingSAXParser (_WrappedFactory.newSAXParser (), _Schema.newVerifier ());
      }
      catch (final VerifierConfigurationException ex)
      {
        throw new ParserConfigurationException (ex.getMessage ());
      }
    }
    return _WrappedFactory.newSAXParser ();
  }

  /**
   * @see SAXParserFactory#setFeature(String, boolean)
   */
  @Override
  public void setFeature (final String name, final boolean value) throws ParserConfigurationException,
                                                                  SAXNotRecognizedException,
                                                                  SAXNotSupportedException
  {
    _WrappedFactory.setFeature (name, value);
  }

  /**
   * @see SAXParserFactory#getFeature(String)
   */
  @Override
  public boolean getFeature (final String name) throws ParserConfigurationException,
                                                SAXNotRecognizedException,
                                                SAXNotSupportedException
  {
    return _WrappedFactory.getFeature (name);
  }

  @Override
  public boolean isNamespaceAware ()
  {
    return _WrappedFactory.isNamespaceAware ();
  }

  @Override
  public void setNamespaceAware (final boolean awareness)
  {
    _WrappedFactory.setNamespaceAware (awareness);
  }

  @Override
  public boolean isValidating ()
  {
    return validation;
  }

  @Override
  public void setValidating (final boolean validating)
  {
    validation = validating;
  }
}
