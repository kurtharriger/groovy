/*
 *
 * Copyright 2007 Jeremy Rayner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codehaus.groovy.tools.groovydoc;

import groovy.util.GroovyTestCase;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyMethodDoc;
import org.codehaus.groovy.groovydoc.GroovyRootDoc;

import java.util.ArrayList;

public class GroovyDocToolTest extends GroovyTestCase {
    GroovyDocTool xmlTool;
    GroovyDocTool xmlToolForTests;
    GroovyDocTool plainTool;
    private static final String FS = "/";
    private static final String MOCK_DIR = "mock" + FS + "doc";
    private static final String TEMPLATES_DIR = "main" + FS + "org" + FS + "codehaus" + FS + "groovy" + FS + "tools" + FS + "groovydoc" + FS + "gstring-templates";

    public void setUp() {
        plainTool = new GroovyDocTool("src" + FS + "test");

        xmlTool = new GroovyDocTool(
                new FileSystemResourceManager("src"), // template storage
                "src" + FS + "main", // source file dirs
                new String[]{TEMPLATES_DIR + FS + "top-level" + FS + "rootDocStructuredData.xml"},
                new String[]{TEMPLATES_DIR + FS + "package-level" + FS + "packageDocStructuredData.xml"},
                new String[]{TEMPLATES_DIR + FS + "class-level" + FS + "classDocStructuredData.xml"},
                new ArrayList()
        );

        xmlToolForTests = new GroovyDocTool(
                new FileSystemResourceManager("src"), // template storage
                "src" + FS + "test", // source file dirs
                new String[]{TEMPLATES_DIR + FS + "top-level" + FS + "rootDocStructuredData.xml"},
                new String[]{TEMPLATES_DIR + FS + "package-level" + FS + "packageDocStructuredData.xml"},
                new String[]{TEMPLATES_DIR + FS + "class-level" + FS + "classDocStructuredData.xml"},
                new ArrayList()
        );
    }

    public void testPlainGroovyDocTool() throws Exception {
        plainTool.add("org" + FS + "codehaus" + FS + "groovy" + FS + "tools" + FS + "groovydoc" + FS + "GroovyDocToolTest.java");
        GroovyRootDoc root = plainTool.getRootDoc();

        // loop through classes in tree
        GroovyClassDoc[] classDocs = root.classes();
        for (int i=0; i< classDocs.length; i++) {
            GroovyClassDoc clazz = root.classes()[i];

            assertEquals("GroovyDocToolTest", clazz.name());

            // loop through methods in class
            boolean seenThisMethod = false;
            GroovyMethodDoc[] methodDocs = clazz.methods();
            for (int j=0; j< methodDocs.length; j++) {
                GroovyMethodDoc method = clazz.methods()[j];

                if ("testPlainGroovyDocTool".equals(method.name())) {
                    seenThisMethod = true;
                }

            }
            assertTrue(seenThisMethod);
        }
    }

    public void testGroovyDocTheCategoryMethodClass() throws Exception {
        xmlTool.add("groovy" + FS + "util" + FS + "CliBuilder.groovy");
        xmlTool.add("groovy" + FS + "lang" + FS + "GroovyLogTestCase.groovy");
        xmlTool.add("groovy" + FS + "mock" + FS + "interceptor" + FS + "StrictExpectation.groovy");
        xmlTool.add("groovy" + FS + "ui" + FS + "Console.groovy");
        xmlTool.add("org" + FS + "codehaus" + FS + "groovy" + FS + "runtime" + FS + "GroovyCategorySupport.java");
        xmlTool.add("org" + FS + "codehaus" + FS + "groovy" + FS + "runtime" + FS + "ConvertedMap.java");
        MockOutputTool output = new MockOutputTool();
        xmlTool.renderToOutput(output, MOCK_DIR);

        String categoryMethodDocument = output.getText(MOCK_DIR + FS + "org" + FS + "codehaus" + FS + "groovy" + FS + "runtime" + FS + "CategoryMethod.html"); // todo - figure out how to get xml extension for templates

        assertTrue(categoryMethodDocument.indexOf("<method returns=\"boolean\" name=\"hasCategoryInAnyThread\">") > 0);

        String packageDocument = output.getText(MOCK_DIR + FS + "org" + FS + "codehaus" + FS + "groovy" + FS + "runtime" + FS + "packageDocStructuredData.xml");
        assertTrue(packageDocument.indexOf("<class name=\"CategoryMethod\" />") > 0);

        String rootDocument = output.getText(MOCK_DIR + FS + "rootDocStructuredData.xml");
        assertTrue(rootDocument.indexOf("<package name=\"org" + FS + "codehaus" + FS + "groovy" + FS + "runtime\" />") > 0);
        assertTrue(rootDocument.indexOf("<class path=\"org" + FS + "codehaus" + FS + "groovy" + FS + "runtime" + FS + "CategoryMethod\" name=\"CategoryMethod\" />") > 0);
    }

    public void testConstructors() throws Exception {
        xmlTool.add("groovy" + FS + "ui" + FS + "Console.groovy");
        MockOutputTool output = new MockOutputTool();
        xmlTool.renderToOutput(output, MOCK_DIR);

        String consoleDoc = output.getText(MOCK_DIR + FS + "groovy" + FS + "ui" + FS + "Console.html");
        assertTrue(consoleDoc.indexOf("<constructor name=\"Console\">") > 0);
        assertTrue(consoleDoc.indexOf("<parameter type=\"ClassLoader\" name=\"parent\" />") > 0);
    }

    public void testClassComment() throws Exception {
        xmlTool.add("groovy" + FS + "xml" + FS + "DOMBuilder.java");
        MockOutputTool output = new MockOutputTool();
        xmlTool.renderToOutput(output, MOCK_DIR);

        String domBuilderDoc = output.getText(MOCK_DIR + FS + "groovy" + FS + "xml" + FS + "DOMBuilder.html");
        assertTrue(domBuilderDoc.indexOf("A helper class for creating a W3C DOM tree") > 0);
    }

    public void testMethodComment() throws Exception {
        xmlTool.add("groovy" + FS + "model" + FS + "DefaultTableColumn.java");
        MockOutputTool output = new MockOutputTool();
        xmlTool.renderToOutput(output, MOCK_DIR);

        String defTabColDoc = output.getText(MOCK_DIR + FS + "groovy" + FS + "model" + FS + "DefaultTableColumn.html");
        System.out.println(defTabColDoc);

        assertTrue(defTabColDoc.indexOf("Evaluates the value of a cell") > 0);
    }
    public void testPackageName() throws Exception {
        xmlTool.add("groovy" + FS + "xml" + FS + "DOMBuilder.java");
        MockOutputTool output = new MockOutputTool();
        xmlTool.renderToOutput(output, MOCK_DIR);

        String domBuilderDoc = output.getText(MOCK_DIR + FS + "groovy" + FS + "xml" + FS + "DOMBuilder.html");
        assertTrue(domBuilderDoc.indexOf("<containingPackage name=\"groovy" + FS + "xml\">groovy.xml</containingPackage>") > 0);
    }

    public void testExtendsClauseWithoutSuperClassInTree() throws Exception {
        xmlTool.add("groovy" + FS + "xml" + FS + "DOMBuilder.java");
        MockOutputTool output = new MockOutputTool();
        xmlTool.renderToOutput(output, MOCK_DIR);

        String domBuilderDoc = output.getText(MOCK_DIR + FS + "groovy" + FS + "xml" + FS + "DOMBuilder.html");
        assertTrue(domBuilderDoc.indexOf("<extends>BuilderSupport</extends>") > 0);
    }

    public void testExtendsClauseWithSuperClassInTree() throws Exception {
        xmlTool.add("groovy" + FS + "xml" + FS + "DOMBuilder.java");
        xmlTool.add("groovy" + FS + "util" + FS + "BuilderSupport.java");
        MockOutputTool output = new MockOutputTool();
        xmlTool.renderToOutput(output, MOCK_DIR);

        String domBuilderDoc = output.getText(MOCK_DIR + FS + "groovy" + FS + "xml" + FS + "DOMBuilder.html");
        assertTrue(domBuilderDoc.indexOf("<extends>BuilderSupport</extends>") > 0);
    }
    
    public void testDefaultPackage() throws Exception {
    	xmlToolForTests.add("UberTestCaseBugs.java");
        MockOutputTool output = new MockOutputTool();
        xmlToolForTests.renderToOutput(output, MOCK_DIR);
        String domBuilderDoc = output.getText(MOCK_DIR + FS + "DefaultPackage" + FS + "UberTestCaseBugs.html");
        assertTrue(domBuilderDoc.indexOf("<extends>TestCase</extends>") > 0);    	
    }
}