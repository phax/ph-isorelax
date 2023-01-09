/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2023 Philip Helger (www.helger.com)
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

/**
 * A class that provides information about the verifier implementation.
 * <p>
 * Implementations of this interface are discovered through
 * <code>META-INF/services</code>, just like JAXP. This object then provides
 * VerifierFactory implementation for the specified schema language.
 *
 * @author <a href="mailto:kohsukekawaguchi@yahoo.com">Kohsuke KAWAGUCHI</a>
 */
public interface VerifierFactoryLoader
{
  /**
   * returns a VerifierFactory that supports the specified schema language, or
   * returns null if it's not supported.
   */
  VerifierFactory createFactory (String schemaLanguage);
}
