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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;

import org.iso_relax.verifier.Verifier;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Wrapper DocumentBuilder with validation through JARV API.
 *
 * @author Daisuke OKAJIMA
 */
class ValidatingDocumentBuilder extends DocumentBuilder
{
  protected DocumentBuilder _WrappedBuilder;
  protected Verifier _Verifier;

  /**
   * creates a new instance with an internal DocumentBuilder and Schema.
   *
   * @param wrapped
   *        internal DOM parser
   * @param verifier
   *        verifier.
   */
  public ValidatingDocumentBuilder (final DocumentBuilder wrapped, final Verifier verifier)
  {
    _WrappedBuilder = wrapped;
    _Verifier = verifier;
  }

  /**
   * Parses the given InputSource and validates the resulting DOM.
   */
  @Override
  public Document parse (final InputSource inputsource) throws SAXException, IOException
  {
    return verify (_WrappedBuilder.parse (inputsource));
  }

  /**
   * Parses the given File and validates the resulting DOM.
   */
  @Override
  public Document parse (final File file) throws SAXException, IOException
  {
    return verify (_WrappedBuilder.parse (file));
  }

  /**
   * Parses the given InputStream and validates the resulting DOM.
   */
  @Override
  public Document parse (final InputStream strm) throws SAXException, IOException
  {
    return verify (_WrappedBuilder.parse (strm));
  }

  /**
   * Parses the given InputStream using the specified systemId and validates the
   * resulting DOM.
   */
  @Override
  public Document parse (final InputStream strm, final String systemId) throws SAXException, IOException
  {
    return verify (_WrappedBuilder.parse (strm, systemId));
  }

  /**
   * Parses the given url and validates the resulting DOM.
   */
  @Override
  public Document parse (final String url) throws SAXException, IOException
  {
    return verify (_WrappedBuilder.parse (url));
  }

  /**
   * @see DocumentBuilder#isNamespaceAware()
   */
  @Override
  public boolean isNamespaceAware ()
  {
    return _WrappedBuilder.isNamespaceAware ();
  }

  /**
   * returns true always.
   */
  @Override
  public boolean isValidating ()
  {
    return true;
  }

  /**
   * @see DocumentBuilder#setEntityResolver(EntityResolver)
   */
  @Override
  public void setEntityResolver (final EntityResolver entityresolver)
  {
    _WrappedBuilder.setEntityResolver (entityresolver);
    _Verifier.setEntityResolver (entityresolver);
  }

  /**
   * @see DocumentBuilder#setErrorHandler(ErrorHandler)
   */
  @Override
  public void setErrorHandler (final ErrorHandler errorhandler)
  {
    _WrappedBuilder.setErrorHandler (errorhandler);
    _Verifier.setErrorHandler (errorhandler);
  }

  /**
   * @see DocumentBuilder#newDocument()
   */
  @Override
  public Document newDocument ()
  {
    return _WrappedBuilder.newDocument ();
  }

  /**
   * @see DocumentBuilder#getDOMImplementation()
   */
  @Override
  public DOMImplementation getDOMImplementation ()
  {
    return _WrappedBuilder.getDOMImplementation ();
  }

  private Document verify (final Document dom) throws SAXException
  {
    if (_Verifier.verify (dom))
      return dom;
    throw new SAXException ("the document is invalid, but the ErrorHandler does not throw any Exception.");
  }
}
