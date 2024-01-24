/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2024 Philip Helger (www.helger.com)
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

import java.util.Iterator;

/**
 * provides necessary schema information for Dispatcher. This interface can be
 * implemented by applications.
 *
 * @author <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 */
public interface SchemaProvider
{
  /**
   * creates IslandVerifier that validates document element.
   */
  IslandVerifier createTopLevelVerifier ();

  /**
   * gets IslandSchema whose primary namespace URI is the given value.
   *
   * @return null if no such IslandSchema exists.
   */
  IslandSchema getSchemaByNamespace (String uri);

  /**
   * iterates all namespaces that are registered in this object.
   */
  Iterator <String> iterateNamespace ();

  /**
   * returns all IslandSchemata at once.
   */
  IslandSchema [] getSchemata ();
}
