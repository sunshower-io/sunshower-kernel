package io.sunshower.kernel.wildfly.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "management-request")
public class ManagementRequest {

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
  private List<DeploymentDescriptor> deployments;
}
