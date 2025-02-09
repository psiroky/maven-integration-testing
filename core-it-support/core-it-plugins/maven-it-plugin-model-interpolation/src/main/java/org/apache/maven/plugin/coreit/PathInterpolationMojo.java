package org.apache.maven.plugin.coreit;

/*
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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.DefaultProjectBuilderConfiguration;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.interpolation.ModelInterpolationException;
import org.apache.maven.project.interpolation.ModelInterpolator;

import java.util.Properties;

/**
 * @goal path-interpolation
 * @phase validate
 */
public class PathInterpolationMojo
    extends AbstractMojo
{
    /**
     * The model interpolator
     * @component
     */
    private ModelInterpolator modelInterpolator;

    /**
     * The current Maven project.
     *
     * @parameter default-value="${project}"
     */
    private MavenProject project;

    public void execute()
        throws MojoExecutionException
    {
        try
        {
            Properties props = project.getProperties();

            modelInterpolator.interpolate( project.getOriginalModel(),
                                           project.getBasedir(),
                                           new DefaultProjectBuilderConfiguration().setExecutionProperties( props ),
                                           true );
        }
        catch ( ModelInterpolationException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }
}
