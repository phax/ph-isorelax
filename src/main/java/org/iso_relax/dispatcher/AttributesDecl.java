/*
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
package org.iso_relax.dispatcher;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * represents a constraint for XML attributes. This interface also provides
 * feature/property mechanism to encourage communications between two different
 * implementations.
 *
 * @author <a href="mailto:k-kawa@bigfoot.com">Kohsuke KAWAGUCHI</a>
 */
public interface AttributesDecl
{
  /**
   * gets name of this rule. every AttributesDecl has a unique name within the
   * schema.
   */
  String getName ();

  /**
   * looks up the value of a feature this method works like getFeature method of
   * SAX. featureName is a fully-qualified URI. Implementators are encouraged to
   * invent their own features, by using their own URIs.
   */
  boolean getFeature (String featureName) throws SAXNotRecognizedException, SAXNotSupportedException;

  /**
   * looks up the value of a property this method works like getProperty method
   * of SAX. propertyName is a fully-qualified URI. Implementators are
   * encouraged to invent their own properties, by using their own URIs.
   */
  Object getProperty (String propertyName) throws SAXNotRecognizedException, SAXNotSupportedException;
}
