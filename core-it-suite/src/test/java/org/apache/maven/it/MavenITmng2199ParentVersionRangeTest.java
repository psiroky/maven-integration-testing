/*
 * Copyright 2014 The Apache Software Foundation.
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
package org.apache.maven.it;

import org.apache.maven.shared.verifier.util.ResourceExtractor;
import org.apache.maven.shared.verifier.Verifier;
import org.apache.maven.shared.verifier.VerificationException;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class MavenITmng2199ParentVersionRangeTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng2199ParentVersionRangeTest()
    {
        super( "[3.2.2,)" );
    }

    @Test
    public void testValidParentVersionRangeWithInclusiveUpperBound()
        throws Exception
    {
        failingMavenVersions( "(3.2.2,3.5.0-alpha-0)" );
        Verifier verifier = null;
        File testDir =
            ResourceExtractor.simpleExtractResources( getClass(),
                                                      "/mng-2199-parent-version-range/valid-inclusive-upper-bound" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
            verifier.addCliOption( "-U" );
            verifier.setAutoclean( false );

            verifier.executeGoal( "verify" );
            verifier.verifyErrorFreeLog();

            // All Maven versions not supporting remote parent version ranges will log a warning message whenever
            // building a parent fails. The build succeeds without any parent. If that warning message appears in the
            // log, parent resolution failed.
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            assertFalse( "Unexpected error message found.",
                         indexOf( lines, ".*Failed to build parent project.*" ) >= 0 );

        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testValidParentVersionRangeWithExclusiveUpperBound()
        throws Exception
    {
        failingMavenVersions( "(3.2.2,3.5.0-alpha-0)" );
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources(
            getClass(), "/mng-2199-parent-version-range/valid-exclusive-upper-bound" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
            verifier.addCliOption( "-U" );
            verifier.setAutoclean( false );

            verifier.executeGoal( "verify" );
            verifier.verifyErrorFreeLog();

            // All Maven versions not supporting remote parent version ranges will log a warning message whenever
            // building a parent fails. The build succeeds without any parent. If that warning message appears in the
            // log, parent resolution failed.
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            assertFalse( "Unexpected error message found.",
                         indexOf( lines, ".*Failed to build parent project.*" ) >= 0 );

        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testInvalidParentVersionRangeWithoutUpperBound()
        throws Exception
    {
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2199-parent-version-range/invalid" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
            verifier.setAutoclean( false );
            verifier.addCliOption( "-U" );
            verifier.executeGoal( "verify" );
            fail( "Expected 'VerificationException' not thrown." );
        }
        catch ( final VerificationException e )
        {
            assertNotNull( verifier );
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            assertTrue( "Expected error message not found.",
                        indexOf( lines, ".*(parent)? version range.*does not specify an upper bound.*" ) >= 0 );

        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testValidParentVersionRangeInvalidVersionExpression()
        throws Exception
    {
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources(
            getClass(), "/mng-2199-parent-version-range/expression" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
            verifier.setAutoclean( false );
            verifier.addCliOption( "-U" );
            verifier.executeGoal( "verify" );
            fail( "Expected 'VerificationException' not thrown." );
        }
        catch ( final VerificationException e )
        {
            assertNotNull( verifier );
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            int msg = indexOf( lines, ".*Version must be a constant.*org.apache.maven.its.mng2199:expression.*" );
            assertTrue( "Expected error message not found.", msg >= 0 );
        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testValidParentVersionRangeInvalidVersionInheritance()
        throws Exception
    {
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources(
            getClass(), "/mng-2199-parent-version-range/inherited" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
            verifier.setAutoclean( false );
            verifier.addCliOption( "-U" );
            verifier.executeGoal( "verify" );
            fail( "Expected 'VerificationException' not thrown." );
        }
        catch ( final VerificationException e )
        {
            assertNotNull( verifier );
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            int msg = indexOf( lines, ".*Version must be a constant.*org.apache.maven.its.mng2199:inherited.*" );
            assertTrue( "Expected error message not found.", msg >= 0 );
        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testValidLocalParentVersionRange()
        throws Exception
    {
        failingMavenVersions( "(,3.3.0),(3.3.9,3.5.0-alpha-0)" );
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources(
            getClass(), "/mng-2199-parent-version-range/valid-local/child" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath() );
            verifier.executeGoal( "verify" );
            verifier.verifyErrorFreeLog();

            // All Maven versions not supporting remote parent version ranges will log a warning message whenever
            // building a parent fails. The build succeeds without any parent. If that warning message appears in the
            // log, parent resolution failed. For this test, this really just tests the project on disk getting tested
            // is not corrupt. It's expected to find the local parent and not fall back to remote resolution. If it
            // falls back to remote resolution, this just catches the test project to be broken.
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            assertFalse( "Unexpected error message found.",
                         indexOf( lines, ".*Failed to build parent project.*" ) >= 0 );

        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testInvalidLocalParentVersionRange()
        throws Exception
    {
        failingMavenVersions( "[3.3.0,3.3.9)" );
        // Fallback to remote resolution not tested here. Remote parent expected to not be available anywhere.
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources(
            getClass(), "/mng-2199-parent-version-range/invalid-local/child" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath() );
            verifier.executeGoal( "verify" );
            fail( "Expected 'VerificationException' not thrown." );
        }
        catch ( final VerificationException e )
        {
            assertNotNull( verifier );
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            int msg = indexOf( lines,
                               ".*Non-resolvable parent POM org.apache.maven.its.mng2199:local-parent:\\[2,3\\].*" );
            assertTrue( "Expected error message not found.", msg >= 0 );
        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testInvalidLocalParentVersionRangeFallingBackToRemote()
        throws Exception
    {
        failingMavenVersions( "[3.3.9]" );
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources(
            getClass(), "/mng-2199-parent-version-range/local-fallback-to-remote/child" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath(), "remote" );
            verifier.executeGoal( "verify" );
            verifier.verifyErrorFreeLog();

            // All Maven versions not supporting remote parent version ranges will log a warning message whenever
            // building a parent fails. The build succeeds without any parent. If that warning message appears in the
            // log, parent resolution failed. For this test, local parent resolution falls back to remote parent
            // resolution with a version range in use. If the warning message is in the logs, that remote parent
            // resolution failed unexpectedly.
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            assertFalse( "Unexpected error message found.",
                         indexOf( lines, ".*Failed to build parent project.*" ) >= 0 );

        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testValidLocalParentVersionRangeInvalidVersionExpression()
        throws Exception
    {
        failingMavenVersions( "(,3.5.0-alpha-0)" );
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources(
            getClass(), "/mng-2199-parent-version-range/expression-local/child" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath() );
            verifier.executeGoal( "verify" );
            fail( "Expected 'VerificationException' not thrown." );
        }
        catch ( final VerificationException e )
        {
            assertNotNull( verifier );
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            int msg = indexOf( lines, ".*Version must be a constant.*org.apache.maven.its.mng2199:expression.*" );
            assertTrue( "Expected error message not found.", msg >= 0 );
        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    @Test
    public void testValidLocalParentVersionRangeInvalidVersionInheritance()
        throws Exception
    {
        failingMavenVersions( "(,3.5.0-alpha-0)" );
        Verifier verifier = null;
        File testDir = ResourceExtractor.simpleExtractResources(
            getClass(), "/mng-2199-parent-version-range/inherited-local/child" );

        try
        {
            verifier = newVerifier( testDir.getAbsolutePath() );
            verifier.executeGoal( "verify" );
            fail( "Expected 'VerificationException' not thrown." );
        }
        catch ( final VerificationException e )
        {
            assertNotNull( verifier );
            final List<String> lines = verifier.loadFile( new File( testDir, "log.txt" ), false );
            int msg = indexOf( lines, ".*Version must be a constant.*org.apache.maven.its.mng2199:inherited.*" );
            assertTrue( "Expected error message not found.", msg >= 0 );
        }
        finally
        {
            if ( verifier != null )
            {
                verifier.resetStreams();
            }
        }
    }

    private static int indexOf( final List<String> logLines, final String regex )
    {
        final Pattern pattern = Pattern.compile( regex );

        for ( int i = 0, l0 = logLines.size(); i < l0; i++ )
        {
            if ( pattern.matcher( logLines.get( i ) ).matches() )
            {
                return i;
            }
        }

        return -1;
    }

}
