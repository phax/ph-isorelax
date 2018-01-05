/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2018 Philip Helger (www.helger.com)
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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * splits incoming SAX events to "islands", and feed events to IslandVerifier.
 *
 * @author <a href="mailto:mura034@attglobal.net">MURATA Makoto (FAMILY
 *         Given)</a>, <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 * @version 1.1
 */
public interface Dispatcher
{
  /**
   * configure XMLReader to use this Dispatcher as a ContentHandler.
   */
  void attachXMLReader (XMLReader reader);

  /**
   * switches to the child IslandVerifier. this method can only be called during
   * startElement method.
   */
  void switchVerifier (IslandVerifier newVerifier) throws SAXException;

  /**
   * sets application-implemented ErrorHandler, which will receive all
   * validation errors.
   */
  void setErrorHandler (ErrorHandler handler);

  /**
   * gets ErrorHandler to which IslandVerifier reports validation errors. the
   * caller may not assume that this method returns the same object that was
   * passed to setErrorHandler method. this method cannot return null.
   */
  ErrorHandler getErrorHandler ();

  /** get ShcmeaProvider object which is attached to this Dispatcher. */
  SchemaProvider getSchemaProvider ();

  public static class NotationDecl
  {
    public final String m_sName;
    public final String m_sPublicId;
    public final String m_sSystemId;

    public NotationDecl (final String pname, final String ppublicId, final String psystemId)
    {
      m_sName = pname;
      m_sPublicId = ppublicId;
      m_sSystemId = psystemId;
    }
  }

  /** counts notation declarations found in this XML instance. */
  int countNotationDecls ();

  /**
   * gets <i>i</i>th notation declaration found in this XML instance.
   * IslandVerifiers can not receive DTDHandler events. Those who need DTD
   * information should call this method.
   */
  NotationDecl getNotationDecl (int index);

  public static class UnparsedEntityDecl
  {
    public final String m_sName;
    public final String m_sPublicId;
    public final String m_sSystemId;
    public final String m_sNotation;

    public UnparsedEntityDecl (final String pname,
                               final String ppublicId,
                               final String psystemId,
                               final String pnotation)
    {
      m_sName = pname;
      m_sPublicId = ppublicId;
      m_sSystemId = psystemId;
      m_sNotation = pnotation;
    }
  }

  /** counts unparsed entities found in this XML instance. */
  int countUnparsedEntityDecls ();

  /**
   * gets <i>i</i>th unparsed entity found in this XML instance. IslandVerifiers
   * can not receive DTDHandler events. Those who need DTD information should
   * call this method.
   */
  UnparsedEntityDecl getUnparsedEntityDecl (int index);

}
