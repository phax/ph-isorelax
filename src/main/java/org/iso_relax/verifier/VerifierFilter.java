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
package org.iso_relax.verifier;

import org.xml.sax.XMLFilter;

/**
 * XMLFilter implementation that validates a document.
 * <p>
 * An instance of this interface can be obtained through the
 * {@link Verifier#getVerifierFilter} method.
 * <p>
 * The implementation validates incoming SAX events and then pass it to the
 * successive SAX handlers.
 *
 * @since Feb. 23, 2001
 * @version Feb. 24, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public interface VerifierFilter extends XMLFilter
{
  /**
   * checks if the document was valid.
   * <p>
   * This method can be only called after this handler receives the endDocument
   * event.
   *
   * @return <b>true</b> if the document was valid, <b>false</b> if not.
   * @exception IllegalStateException
   *            If this method is called before the endDocument event is
   *            dispatched.
   */
  boolean isValid () throws IllegalStateException;
}
