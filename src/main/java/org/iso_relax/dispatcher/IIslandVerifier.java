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
package org.iso_relax.dispatcher;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Interface for verifier that validates one island.
 *
 * @author <a href="mailto:mura034@attglobal.net">MURATA Makoto (FAMILY
 *         Given)</a>, <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 * @version 1.1
 */
public interface IIslandVerifier extends ContentHandler
{
  /**
   * Dispatcher passes itself to IslandVerifier by calling this method from
   * Dispatcher.switchVerifier method.
   */
  void setDispatcher (IDispatcher disp);

  /**
   * substitute for endDocument event. This method is called after endElement
   * method is called for the top element in the island. endDocument method is
   * never called for IslandVerifier.
   *
   * @return the callee must return all validated ElementDecls. If every
   *         candidate fails, return an empty array. It is the callee's
   *         responsibility to report an error. The callee may also recover from
   *         error. Never return null.
   */
  public IElementDecl [] endIsland () throws SAXException;

  /**
   * this method is called after verification of the child island is completed,
   * instead of endElement method.
   *
   * @param uri
   *        namespace URI of the child island.
   * @param assignedDecls
   *        set of elementDecls that were successfully assigned to this child
   *        island. when every elementDecl was failed, then an empty array is
   *        passed.
   */
  public void endChildIsland (String uri, IElementDecl assignedDecls[]) throws SAXException;
}
