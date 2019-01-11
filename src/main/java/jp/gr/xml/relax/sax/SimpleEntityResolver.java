/**
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2019 Philip Helger (www.helger.com)
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * SimpleEntityResolver
 *
 * @since Aug. 12, 2000
 * @version May. 28, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */

public class SimpleEntityResolver implements EntityResolver
{
  private final Map <String, String> publicIds_ = new HashMap<> ();
  private final Map <String, String> systemIds_ = new HashMap<> ();
  private final List <String> relativeSystemIds_ = new ArrayList<> ();

  public SimpleEntityResolver ()
  {}

  public SimpleEntityResolver (final String name, final String uri)
  {
    _init (new String [] [] { { name, uri } }, null);
  }

  public SimpleEntityResolver (final String [] [] systemIds)
  {
    _init (systemIds, null);
  }

  public SimpleEntityResolver (final String [] [] systemIds, final String [] [] publicIds)
  {
    _init (systemIds, publicIds);
  }

  private void _init (final String [] [] systemIds, final String [] [] publicIds)
  {
    if (systemIds != null)
    {
      for (final String [] systemId2 : systemIds)
      {
        final String systemId = systemId2[0];
        addSystemId (systemId, systemId2[1]);
      }
    }

    if (publicIds != null)
      for (final String [] publicId : publicIds)
        addPublicId (publicId[0], publicId[1]);
  }

  public void addSystemId (final String systemId, final String uri)
  {
    systemIds_.put (systemId, uri);
    relativeSystemIds_.add (systemId);
  }

  public void addPublicId (final String publicId, final String uri)
  {
    publicIds_.put (publicId, uri);
  }

  public InputSource resolveEntity (final String publicId, final String systemId)
  {
    if (systemId != null)
      if (_isExist (systemId))
        return new InputSource (systemId);

    if (publicId != null)
    {
      final String uri = publicIds_.get (publicId);
      if (uri != null)
        return new InputSource (uri);
      return null;
    }
    if (systemId != null)
    {
      final String uri = _getURIBySystemId (systemId);
      if (uri != null)
        return new InputSource (uri);
      return new InputSource (systemId);
    }
    return null;
  }

  private boolean _isExist (final String uri)
  {
    try
    {
      final URL url = new URL (uri);
      if ("file".equals (url.getProtocol ()))
      {
        try (final InputStream in = url.openStream ())
        {
          // empty
        }
        return true;
      }
      return false; // XXX : http
    }
    catch (final IOException e)
    {
      return false;
    }
  }

  private String _getURIBySystemId (final String systemId)
  {
    final String uri = systemIds_.get (systemId);
    if (uri != null)
      return uri;

    final int size = relativeSystemIds_.size ();
    for (int i = 0; i < size; i++)
    {
      final String relativeId = relativeSystemIds_.get (i);
      if (systemId.endsWith (relativeId))
        return systemIds_.get (relativeId);
    }
    return null;
  }
}
