/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2025 Philip Helger (www.helger.com)
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

import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * DTDSkipper
 *
 * @since May. 28, 2001
 * @version May. 28, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class DTDSkipper implements EntityResolver
{
  public InputSource resolveEntity (final String publicId, final String systemId)
  {
    if (!systemId.endsWith (".dtd"))
      return null;

    final StringReader reader = new StringReader ("");
    final InputSource is = new InputSource (reader);
    return is;
  }
}
