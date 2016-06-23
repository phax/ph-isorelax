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
package org.iso_relax.dispatcher.impl;

import java.util.Iterator;
import java.util.Map;

import org.iso_relax.dispatcher.IslandSchema;
import org.iso_relax.dispatcher.SchemaProvider;

/**
 * default implementation of SchemaProvider. Applications can use this class as
 * the base class of their own SchemaProvider.
 *
 * @author <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 */
public abstract class AbstractSchemaProviderImpl implements SchemaProvider
{

  /** a map from primary namespace to IslandSchema. */
  protected final Map <String, IslandSchema> schemata = new java.util.HashMap <> ();

  /**
   * adds a new IslandSchema. the caller should make sure that the given uri is
   * not defined already.
   */
  public void addSchema (final String uri, final IslandSchema s)
  {
    if (schemata.containsKey (uri))
      throw new IllegalArgumentException ();
    schemata.put (uri, s);
  }

  public IslandSchema getSchemaByNamespace (final String uri)
  {
    return schemata.get (uri);
  }

  public Iterator <String> iterateNamespace ()
  {
    return schemata.keySet ().iterator ();
  }

  public IslandSchema [] getSchemata ()
  {
    final IslandSchema [] r = new IslandSchema [schemata.size ()];
    schemata.values ().toArray (r);
    return r;
  }
}
