package io.sunshower.api;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

public interface Resource {
  long getContentLength();

  Resource createRelative(String path);

  boolean exists();

  String getDescription();

  File getFile();

  String getFilename();

  URI getURI();

  URL getURL();

  boolean isFile();

  boolean isOpen();

  InputStream getInputStream();

  boolean isReadable();

  long lastModified();

  ReadableByteChannel getReadableChannel();
}
