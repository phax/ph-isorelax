/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2025 Philip Helger (www.helger.com)
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
package jp.gr.xml.relax.sax;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

/**
 * Base class of DeclHandler
 *
 * @since Feb. 18, 2001
 * @version Feb. 24, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class DeclHandlerBase implements DeclHandler
{
  public void elementDecl (final String name, final String model) throws SAXException
  {}

  public void attributeDecl (final String eName,
                             final String aName,
                             final String type,
                             final String valueDefault,
                             final String value) throws SAXException
  {}

  public void internalEntityDecl (final String name, final String value) throws SAXException
  {}

  public void externalEntityDecl (final String name, final String publicId, final String systemId) throws SAXException
  {}
}
