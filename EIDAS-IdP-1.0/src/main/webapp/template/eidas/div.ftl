<#--
/*
 * $Id: div.ftl,v 1.1 2010/05/13 01:11:02 rferreira Exp $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
-->
<div
    <#if parameters.id??>               id="${parameters.id?html}"         </#if>
    <#if parameters.name??>             name="${parameters.name?html}"         </#if>
    <#if parameters.cssClass??>         class="${parameters.cssClass?html}"    </#if>
    <#if parameters.cssStyle??>         style="${parameters.cssStyle?html}"    </#if>
    <#if parameters.title??>            title="${parameters.title?html}"<#rt/>
        </#if>
<#include "/${parameters.templateDir}/simple/scripting-events.ftl" />
<#include "/${parameters.templateDir}/simple/common-attributes.ftl" />
<#include "/${parameters.templateDir}/simple/dynamic-attributes.ftl" />
>