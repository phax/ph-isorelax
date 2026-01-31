/*
 * Copyright (c) 2001-2002, SourceForge ISO-RELAX Project
 * (ASAMI Tomoharu, Daisuke Okajima, Kohsuke Kawaguchi, and MURATA Makoto)
 *
 * Copyright (C) 2016-2026 Philip Helger (www.helger.com)
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
package jp.gr.xml.relax.xml;

/**
 * UXML
 *
 * @since Jan. 29, 2000
 * @version Feb. 24, 2001
 * @author ASAMI, Tomoharu (asami@zeomtech.com)
 */
public final class UXML
{
  private UXML ()
  {}

  public static String escape (final String string)
  {
    if (string.indexOf ('<') == -1 &&
        string.indexOf ('>') == -1 &&
        string.indexOf ('&') == -1 &&
        string.indexOf ('"') == -1 &&
        string.indexOf ('\'') == -1)
    {
      return string;
    }

    final int size = string.length ();
    final StringBuilder buffer = new StringBuilder (size * 2);
    for (int i = 0; i < size; i++)
    {
      final char c = string.charAt (i);
      if (c == '<')
        buffer.append ("&lt;");
      else
        if (c == '>')
          buffer.append ("&gt;");
        else
          if (c == '&')
            buffer.append ("&amp;");
          else
            if (c == '"')
              buffer.append ("&quot;");
            else
              if (c == '\'')
                buffer.append ("&apos;");
              else
                buffer.append (c);
    }
    return buffer.toString ();
  }

  public static String escapeEntityQuot (final String string)
  {
    if (string.indexOf ('%') == -1 && string.indexOf ('&') == -1 && string.indexOf ('"') == -1)
      return string;

    final int size = string.length ();
    final StringBuilder buffer = new StringBuilder (size * 2);
    for (int i = 0; i < size; i++)
    {
      final char c = string.charAt (i);
      if (c == '%')
        buffer.append ("&---;");
      else
        if (c == '&')
          buffer.append ("&amp;");
        else
          if (c == '"')
            buffer.append ("&quot;");
          else
            buffer.append (c);
    }
    return buffer.toString ();
  }

  public static String escapeEntityApos (final String string)
  {
    if (string.indexOf ('%') == -1 && string.indexOf ('&') == -1 && string.indexOf ('\'') == -1)
      return string;

    final int size = string.length ();
    final StringBuilder buffer = new StringBuilder (size * 2);
    for (int i = 0; i < size; i++)
    {
      final char c = string.charAt (i);
      if (c == '%')
        buffer.append ("&#x25;");
      else
        if (c == '&')
          buffer.append ("&amp;");
        else
          if (c == '\'')
            buffer.append ("&apos;");
          else
            buffer.append (c);
    }
    return buffer.toString ();
  }

  public static String escapeAttrQuot (final String string)
  {
    if (string.indexOf ('<') == -1 && string.indexOf ('&') == -1 && string.indexOf ('"') == -1)
      return string;

    final int size = string.length ();
    final StringBuilder buffer = new StringBuilder (size * 2);
    for (int i = 0; i < size; i++)
    {
      final char c = string.charAt (i);
      if (c == '<')
        buffer.append ("&lt;");
      else
        if (c == '&')
          buffer.append ("&amp;");
        else
          if (c == '"')
            buffer.append ("&quot;");
          else
            buffer.append (c);
    }
    return buffer.toString ();
  }

  public static String escapeAttrApos (final String string)
  {
    if (string.indexOf ('<') == -1 && string.indexOf ('&') == -1 && string.indexOf ('\'') == -1)
      return string;

    final int size = string.length ();
    final StringBuilder buffer = new StringBuilder (size * 2);
    for (int i = 0; i < size; i++)
    {
      final char c = string.charAt (i);
      if (c == '<')
        buffer.append ("&lt;");
      else
        if (c == '&')
          buffer.append ("&amp;");
        else
          if (c == '\'')
            buffer.append ("&apos;");
          else
            buffer.append (c);
    }
    return buffer.toString ();
  }

  public static String escapeSystemQuot (final String string)
  {
    if (string.indexOf ('"') == -1)
      return string;

    final int size = string.length ();
    final StringBuilder buffer = new StringBuilder (size * 2);
    for (int i = 0; i < size; i++)
    {
      final char c = string.charAt (i);
      if (c == '"')
      {
        buffer.append ("&quot;");
      }
      else
      {
        buffer.append (c);
      }
    }
    return buffer.toString ();
  }

  public static String escapeSystemApos (final String string)
  {
    if (string.indexOf ('\'') == -1)
      return string;

    final int size = string.length ();
    final StringBuilder buffer = new StringBuilder (size * 2);
    for (int i = 0; i < size; i++)
    {
      final char c = string.charAt (i);
      if (c == '\'')
        buffer.append ("&apos;");
      else
        buffer.append (c);
    }
    return buffer.toString ();
  }

  public static String escapeCharData (final String string)
  {
    if (string.indexOf ('<') == -1 && string.indexOf ('&') == -1 && string.indexOf ("]]>") == -1)
      return string;

    final int size = string.length ();
    final StringBuilder buffer = new StringBuilder (size * 2);
    int nBrackets = 0;
    for (int i = 0; i < size; i++)
    {
      final char c = string.charAt (i);
      if (c == '<')
        buffer.append ("&lt;");
      else
        if (c == '&')
          buffer.append ("&amp;");
        else
          if (c == '>' && nBrackets >= 2)
            buffer.append ("&gt;");
          else
            buffer.append (c);
      if (c == ']')
        nBrackets++;
      else
        nBrackets = 0;
    }
    return buffer.toString ();
  }
}
