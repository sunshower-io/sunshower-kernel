package io.sunshower.kernel.configuration;

import io.sunshower.kernel.api.KernelPluginException;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@XmlRootElement(name = "configuration")
public class PluginConfiguration implements Iterable<Map.Entry<String, String>> {

    static final Logger logger = Logger.getLogger(PluginConfiguration.class.getName());

    @XmlElement(name = "properties")
    private Map<String, String> pluginArguments;

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        if(pluginArguments != null) {
            return pluginArguments.entrySet().iterator();
        }
        return Collections.<String, String>emptyMap().entrySet().iterator();
    }

    public void addProperty(String key, String value) {
        if (pluginArguments == null) {
            pluginArguments = new HashMap<>();
        }

        pluginArguments.put(key, value);
    }
    
    public String getProperty(String key) {
        if (pluginArguments == null) {
            pluginArguments = new HashMap<>();
        }
        return pluginArguments.get(key);
    }


    public static PluginConfiguration read(PluginWrapper wrapper) {
        final PluginConfiguration cfg = new PluginConfiguration();
        try {
            return SerializationContextHolder.instance.read(cfg, wrapper);
        } catch (JAXBException e) {
            logger.log(Level.INFO, "Failed to " +
                    "unmarshall configuration for {0}.  " +
                    "Reason: {1}", new Object[]{wrapper, e});
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO, "Failed to " +
                    "unmarshall configuration for {0}.  " +
                    "Reason: File not found.  Attempting to create...", wrapper);

            try {
                SerializationContextHolder.instance.create(wrapper);
                logger.log(Level.INFO, 
                        "Successfully created and persisted plugin configuration"
                );
            } catch (Exception ex ) {
                logger.warning("Failed to create wrapper on last attempt.  " +
                        "You have a detached plugin configuration " +
                        "(your configuration changes will not survive a reboot)");
            }
        }
        return new PluginConfiguration();
    }


    public void save(PluginWrapper wrapper) {
        try {
            SerializationContextHolder.instance.write(this, wrapper);
        } catch (JAXBException e) {
            throw new KernelPluginException(e);
        }
    }



    static final class SerializationContext {

        final JAXBContext context;
        SerializationContext() {
            try {
                context = JAXBContextFactory.createContext(
                        new Class[]{PluginConfiguration.class
                        }, new HashMap());
            } catch (JAXBException ex) {
                throw new IllegalStateException("Failed to instantiate plugin serialization context");
            }
        }

        synchronized void write(
                PluginConfiguration configuration,
                PluginWrapper wrapper
        ) throws JAXBException {
            PluginDescriptor descriptor = wrapper.getDescriptor();
            if (descriptor == null) {
                logger.log(Level.WARNING, "Failed to save plugin context " +
                                "for plugin: {0}.  No plugin descriptor was found",
                        wrapper.getPluginId());
                return;
            }
            URI location = descriptor.getLocation();
            if (location == null) {
                logger.log(Level.WARNING, "Failed to save plugin context " +
                                "for plugin: {0}.  No plugin descriptor was found",
                        wrapper.getPluginId());
                return;
            }
            final File file       = Paths.get(location).toFile().getParentFile();
            if(!file.exists()) {
                file.mkdirs();
            }
            final File pluginFile = new File(file, "plugin.xml");
            if(!pluginFile.exists()) {
                try {
                    pluginFile.createNewFile();
                } catch (IOException e) {
                    throw new KernelPluginException(e);
                }
            }
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(configuration, pluginFile);
        }

        synchronized PluginConfiguration read(PluginConfiguration cfg, PluginWrapper wrapper) throws JAXBException, FileNotFoundException {
            PluginDescriptor descriptor = wrapper.getDescriptor();
            if (descriptor == null) {
                logger.log(Level.WARNING, "Failed to save plugin context " +
                                "for plugin: {0}.  No plugin descriptor was found",
                        wrapper.getPluginId());
                return null;
            }
            URI location = descriptor.getLocation();
            if (location == null) {
                logger.log(Level.WARNING, "Failed to save plugin context " +
                                "for plugin: {0}.  No plugin descriptor was found",
                        wrapper.getPluginId());
                return null;
            }
            final File   file         = Paths.get(location).toFile().getParentFile();
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(
                    new StreamSource(
                            new BufferedInputStream(
                                    new FileInputStream(new File(file, "plugin.xml")))
                    ), PluginConfiguration.class
            ).getValue();
        }

        public PluginConfiguration create(PluginWrapper wrapper) throws IOException, JAXBException {

            PluginDescriptor descriptor = wrapper.getDescriptor();
            if (descriptor == null) {
                logger.log(Level.WARNING, "Failed to save plugin context " +
                                "for plugin: {0}.  No plugin descriptor was found",
                        wrapper.getPluginId());
                return new PluginConfiguration();
            }
            URI location = descriptor.getLocation();
            if (location == null) {
                logger.log(Level.WARNING, "Failed to save plugin context " +
                                "for plugin: {0}.  No plugin descriptor was found",
                        wrapper.getPluginId());
                return new PluginConfiguration();
            }
            
            final File file       = Paths.get(location).toFile().getParentFile();
            if(!file.exists()) {
                file.mkdirs();
            }
            final File pluginFile = new File(file, "plugin.xml");
            if(!pluginFile.createNewFile()) {
                logger.warning("Failed to create plugin file.  " +
                        "You will have a detatched plugin configuration");
                
            }
            PluginConfiguration cfg = new PluginConfiguration();
            write(cfg, wrapper);
            return cfg;
        }
    }

    static final class SerializationContextHolder {
        static final SerializationContext instance = new SerializationContext();
    }


}
