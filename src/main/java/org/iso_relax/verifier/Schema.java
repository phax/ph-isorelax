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

/**
 * The compiled representation of schemas.
 * <p>
 * <code>Schema</code> object must be thread-safe; multiple-threads can access
 * one <code>Schema</code> obejct at the same time.
 * <p>
 * The schema object allows an application to "cache" a schema by compiling it
 * once and using it many times, possibly by different threads.
 */
public interface Schema
{
  /**
   * creates a new Verifier object that validates documents with this schema.
   *
   * @return a valid non-null instance of a Verifier.
   */
  Verifier newVerifier () throws VerifierConfigurationException;
}
