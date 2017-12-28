package io.sunshower.kernel.configuration;

import io.sunshower.common.rs.MapAdapter;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@XmlRootElement(name = "configuration")
public class PluginConfiguration {

    static final Logger logger = Logger.getLogger(PluginConfiguration.class.getName());

    @XmlElement(name = "properties")
    @XmlJavaTypeAdapter(MapAdapter.class)
    private Map<String, String> pluginArguments;


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
            logger.log(Level.SEVERE, "Failed to " +
                    "unmarshall configuration for {0}.  " +
                    "Reason: {1}", new Object[]{wrapper, e});
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to " +
                    "unmarshall configuration for {0}.  " +
                    "Reason: File not found", wrapper);
        }
        return null;
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
            final File file       = Paths.get(location).toFile();
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(configuration, new File(file, "plugin.xml"));
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
            final File   file         = Paths.get(location).toFile();
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(
                    new StreamSource(
                            new BufferedInputStream(
                                    new FileInputStream(new File(file, "plugin.xml")))
                    ), PluginConfiguration.class
            ).getValue();
        }
    }

    static final class SerializationContextHolder {
        static final SerializationContext instance = new SerializationContext();
    }


}
