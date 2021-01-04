/**
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
package jp.gr.xml.relax.sax;

/**
 * RELAXEntityResolver
 *
 * @since Nov. 23, 2000
 * @version May. 28, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class RELAXEntityResolver extends SimpleEntityResolver
{
  private static final String BASE_PATH = "/jp/gr/xml/relax/lib/";

  public RELAXEntityResolver ()
  {
    final String sCoreUri = getClass ().getResource (BASE_PATH + "relaxCore.dtd").toExternalForm ();
    final String sNSUri = getClass ().getResource (BASE_PATH + "relaxNamespace.dtd").toExternalForm ();
    final String sGrammarUri = getClass ().getResource (BASE_PATH + "relax.dtd").toExternalForm ();

    addSystemId ("http://www.xml.gr.jp/relax/core1/relaxCore.dtd", sCoreUri);
    addSystemId ("relaxCore.dtd", sCoreUri);
    addSystemId ("relaxNamespace.dtd", sNSUri);
    addSystemId ("relax.dtd", sGrammarUri);
    addPublicId ("-//RELAX//DTD RELAX Core 1.0//JA", sCoreUri);
    addPublicId ("-//RELAX//DTD RELAX Namespace 1.0//JA", sNSUri); // XXX
    addPublicId ("-//RELAX//DTD RELAX Grammar 1.0//JA", sGrammarUri); // XXX
  }
}
