/*
 * Copyright 2009-2011 the original author or authors.
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

class JavafxGriffonAddon {
    // lifecycle methods

    // called once, after the addon is created
    //def addonInit(app) {
    //}

    // called once, after all addons have been inited
    //def addonPostInit(app) {
    //}

    // called many times, after creating a builder
    //def addonBuilderInit(app) {
    //}

    // called many times, after creating a builder and after
    // all addons have been inited
    //def addonBuilderPostInit(app) {
    //}


    // to add MVC Groups use create-mvc


    // builder fields, these are added to all builders.
    // closures can either be literal { it -> println it}
    // or they can be method closures: this.&method

    // adds methods to all builders
    //def methods = [
    //    methodName: { /*Closure*/ }
    //]

    // adds properties to all builders
    //def props = [
    //    propertyName: [
    //        get: { /* optional getter closure */ },
    //        set: {val-> /* optional setter closure */ },
    //  ]
    //]

    // adds new factories to all builders
    //def factories = [
    //    factory : /*instance that extends Factory*/
    //]

    // adds application event handlers
    def events = [
        InitializeMVCGroup: { mvcType, mvcName, instanceMap ->
            // Give GroovyFX's SceneGraphBuilder a reference to the primary Stage
            instanceMap.builder.primaryStage = instanceMap.view.app.primaryStage
        }
    ]

    // handle synthetic node properties or
    // intercept existing ones
    //def attributeDelegates = [
    //    {builder, node, attributes -> /*handler code*/ }
    //]

    // called before a node is instantiated
    //def preInstantiateDelegates = [
    //    {builder, attributes, value -> /*handler code*/ }
    //]

    // called after the node was instantiated
    //def postInstantiateDelegates = [
    //    {builder, attributes, node -> /*handler code*/ }
    //]

    // called after the node has been fully
    // processed, including child content
    //def postNodeCompletionDelegates = [
    //    {builder, parent, node -> /*handler code*/ }
    //]
}
