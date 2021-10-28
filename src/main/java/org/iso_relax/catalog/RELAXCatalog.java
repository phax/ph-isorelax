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
package org.iso_relax.catalog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * RELAXCatalog
 *
 * @since Feb. 23, 2001
 * @version Feb. 23, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class RELAXCatalog
{
  private final class CatalogHandler extends DefaultHandler
  {
    @Override
    public void startElement (final String namespaceURI,
                              final String localName,
                              final String qName,
                              final Attributes atts)
    {
      final String uri = atts.getValue ("uri");
      final String grammar = atts.getValue ("grammar");
      m_aGrammars.put (uri, grammar);
    }
  }

  private final Map <String, String> m_aGrammars = new HashMap <> ();

  @Deprecated
  public RELAXCatalog () throws ParserConfigurationException, SAXException, IOException
  {
    this ("http://www.iso-relax.org/catalog");
  }

  public RELAXCatalog (final String rootURI) throws ParserConfigurationException, SAXException, IOException
  {
    final String catalogFile = rootURI + "/catalog.xml";
    final SAXParserFactory factory = SAXParserFactory.newInstance ();
    final SAXParser saxParser = factory.newSAXParser ();
    saxParser.parse (catalogFile, new CatalogHandler ());
  }

  public InputSource getGrammar (final String uri)
  {
    final String location = m_aGrammars.get (uri);
    if (location == null)
      return null;
    return new InputSource (location);
  }
}
