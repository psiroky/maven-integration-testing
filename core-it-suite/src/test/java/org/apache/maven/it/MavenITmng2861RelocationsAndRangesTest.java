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
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-2861">MNG-2861</a>.
 *
 *
 */
public class MavenITmng2861RelocationsAndRangesTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng2861RelocationsAndRangesTest()
    {
        super( "(2.0.8,)" );
    }

    @Test
    public void testitMNG2861()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2861" );

        Verifier verifier = newVerifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "A/target" );
        verifier.deleteArtifacts( "org.apache.maven.its.mng2861" );
        verifier.filterFile( "settings-template.xml", "settings.xml", "UTF-8", verifier.newDefaultFilterProperties() );
        verifier.addCliOption( "--settings" );
        verifier.addCliOption( "settings.xml" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();

        List<String> artifacts = verifier.loadLines( "A/target/artifacts.txt", "UTF-8" );
        assertTrue( artifacts.toString(), artifacts.contains( "org.apache.maven.its.mng2861:B:jar:1.0-SNAPSHOT" ) );
        assertTrue( artifacts.toString(), artifacts.contains( "org.apache.maven.its.mng2861.new:project:jar:2.0" ) );
        assertTrue( artifacts.toString(), artifacts.contains( "org.apache.maven.its.mng2861:C:jar:1.0-SNAPSHOT" ) );
    }

}
