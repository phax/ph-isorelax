/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2021 Philip Helger (www.helger.com)
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
import org.xml.sax.ext.LexicalHandler;

/**
 * Base class of LexicalHandler
 *
 * @since Feb. 18, 2001
 * @version Feb. 24, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class LexicalHandlerBase implements LexicalHandler
{
  public void startDTD (final String name, final String publidId, final String systemID) throws SAXException
  {}

  public void endDTD () throws SAXException
  {}

  public void startEntity (final String name) throws SAXException
  {}

  public void endEntity (final String name) throws SAXException
  {}

  public void startCDATA () throws SAXException
  {}

  public void endCDATA () throws SAXException
  {}

  public void comment (final char ch[], final int start, final int length)
  {}
}
