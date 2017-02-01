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
package org.iso_relax.verifier;

import org.xml.sax.SAXException;

/**
 * VerifierException
 *
 * @since Feb. 23, 2001
 * @version Mar. 4, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class VerifierException extends SAXException
{
  public VerifierException (final String message)
  {
    super (message);
  }

  public VerifierException (final Exception e)
  {
    super (e);
  }

  public VerifierException (final String message, final Exception e)
  {
    super (message, e);
  }
}
