package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.apache.maven.shared.verifier.Verifier;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-4677">MNG-4677</a>.
 *
 * @author Benjamin Bentmann
 */
public class MavenITmng4677DisabledPluginConfigInheritanceTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4677DisabledPluginConfigInheritanceTest()
    {
        super( "[2.0.3,3.0-alpha-1),[3.0-beta-2,)" );
    }

    /**
     * Verify that the plugin-level configuration is not inherited if inherited=false is set.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testit()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4677" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.deleteDirectory( "child-1/target" );
        verifier.deleteDirectory( "child-2/target" );
        verifier.executeGoal( "org.apache.maven.its.plugins:maven-it-plugin-log-file:2.1-SNAPSHOT:log-string" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        List<String> log = verifier.loadLines( "target/log.txt", "UTF-8" );
        assertEquals( Arrays.asList( new String[] { "parent-only" } ), log );

        log = verifier.loadLines( "child-1/target/log.txt", "UTF-8" );
        assertEquals( Arrays.asList( new String[] { "managed" } ), log );

        log = verifier.loadLines( "child-2/target/log.txt", "UTF-8" );
        assertEquals( Arrays.asList( new String[] { "managed" } ), log );
    }

}
