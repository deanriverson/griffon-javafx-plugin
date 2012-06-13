/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.griffon.runtime.javafx;

import groovy.util.Factory;
import groovy.util.FactoryBuilderSupport;
import groovyx.javafx.SceneGraphBuilder;
import org.codehaus.griffon.runtime.builder.DefaultCompositeBuilderCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavaFX based implementation of the <code>CompositeBuilderCustomizer</code> interface.
 *
 * @author Andres Almiray
 */
public class JavaFXCompositeBuilderCustomizer extends DefaultCompositeBuilderCustomizer {
    private static final Logger LOG = LoggerFactory.getLogger(JavaFXCompositeBuilderCustomizer.class);
    private static final SceneGraphBuilder sgb = new SceneGraphBuilder();

    @Override
    public void registerBeanFactory(FactoryBuilderSupport builder, String name, String groupName, Class<?> beanClass) {
        sgb.registerBeanFactory(name, groupName, beanClass);
        final Factory factory = sgb.getFactories().get(groupName + name);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Registering " + groupName + ":" + name + " with " + beanClass + " using " + factory);
        }
        builder.registerFactory(name, groupName, factory);
    }
}
