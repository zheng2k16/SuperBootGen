package com.ssx.entity;

import com.ssx.utils.PomLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author ssx
 * @Date 2021/7/22 10:56
 * @Version 1.0
 *
 * aim to sub Module
 */
@SuppressWarnings("all")
@Component
@Scope("prototype")
public class DefaultSubModuleEntity extends AbstractModuleEntity {
    private static final transient Logger log = LoggerFactory.getLogger(DefaultSubModuleEntity.class);
    private Boolean subModuleIgnoredSupport;

    public DefaultSubModuleEntity() {
        setPomLevel(PomLevel.CHILDREN);
    }

    public Boolean getSubModuleIgnoredSupport() {
        return subModuleIgnoredSupport;
    }
    public void setSubModuleIgnoredSupport(Boolean subModuleIgnoredSupport) {
        this.subModuleIgnoredSupport = subModuleIgnoredSupport;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getArtifactId());
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = Objects.equals(this.getArtifactId(), ((AbstractModuleEntity) o).getArtifactId());
        if (equals) throw new RuntimeException(this.getArtifactId() + " duplicated");
        return equals;
    }
}
