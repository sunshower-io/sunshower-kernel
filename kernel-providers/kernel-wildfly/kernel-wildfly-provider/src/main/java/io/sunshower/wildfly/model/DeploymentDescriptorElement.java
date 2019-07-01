package io.sunshower.wildfly.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "deployment-descriptor")
public class DeploymentDescriptorElement {

  @XmlAttribute private String name;
  @XmlAttribute private String version;
}
