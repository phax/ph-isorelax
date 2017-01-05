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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * VerifierConfigurationException
 *
 * @since Feb. 23, 2001
 * @version Apr. 17, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class VerifierConfigurationException extends Exception
{
  private Exception cause_ = null;

  public VerifierConfigurationException (final String message)
  {
    super (message);
  }

  public VerifierConfigurationException (final Exception e)
  {
    super (e.getMessage ());
    cause_ = e;
  }

  public VerifierConfigurationException (final String message, final Exception e)
  {
    super (message);
    cause_ = e;
  }

  public Exception getException ()
  {
    if (cause_ != null)
    {
      return (cause_);
    }
    return (this);
  }

  public Exception getCauseException ()
  {
    return (cause_);
  }

  @Override
  public void printStackTrace ()
  {
    printStackTrace (new PrintWriter (System.err, true));
  }

  @Override
  public void printStackTrace (final PrintStream out)
  {
    printStackTrace (new PrintWriter (out));
  }

  @Override
  public void printStackTrace (final PrintWriter pwriter)
  {
    final PrintWriter writer = pwriter != null ? pwriter : new PrintWriter (System.err, true);
    super.printStackTrace (writer);
    if (cause_ != null)
    {
      writer.println ();
      writer.println ("StackTrace of Original Exception:");
      cause_.printStackTrace (writer);
    }
  }
}
