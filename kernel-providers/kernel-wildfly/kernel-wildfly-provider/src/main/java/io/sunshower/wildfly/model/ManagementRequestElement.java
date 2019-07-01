package io.sunshower.wildfly.model;

import io.sunshower.common.rs.MapAdapter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Getter;

@Getter
@XmlRootElement(name = "management-request")
public class ManagementRequestElement {

  private String name;

  private String organization;

  @XmlAttribute(name = "release-codename")
  private String version;

  @XmlAttribute(name = "release-version")
  private String releaseVersion;

  @XmlAttribute(name = "profile-name")
  private String profileName;

  @XmlAttribute(name = "product-version")
  private String productVersion;

  @XmlAttribute(name = "product-name")
  private String productName;

  @XmlAttribute(name = "management-major-version")
  private String majorVersion;

  @XmlAttribute(name = "management-minor-version")
  private String minorVersion;

  @XmlAttribute(name = "management-micro-version")
  private String microVersion;

  @XmlElement(name = "deployment")
  @XmlJavaTypeAdapter(MapAdapter.class)
  private Map<String, String> deployments;

  public List<DeploymentDescriptorElement> getDeployments() {
    return deployments
        .entrySet()
        .stream()
        .map(t -> Models.parse(t.getKey(), t.getValue()))
        .collect(Collectors.toList());
  }
}
