package io.sunshower.kernel.wildfly.model;

import lombok.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "deployment-descriptor")
public class DeploymentDescriptorElement {

    @XmlAttribute
    private String name;
    @XmlAttribute
    private String version;


}
