/*
 * Copyright 2003-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovy

/**
 * methods with specific parameters (e.g.&nbsp;primitives)
 * for use with groovy tests
 *
 * @author <a href="mailto:jeremy.rayner@bigfoot.com">Jeremy Rayner</a>
 * @version $Revision$
 */
class DummyMethodsGroovy {
    static void main(String[] args) {
        DummyMethodsGroovy tmp = new DummyMethodsGroovy()
        String answer = tmp.foo("Hey", 1, 2)
        System.out.println("Answer: " + answer)
    }

    String foo(String a, float b, float c) {
        return "float args"
    }

    String foo(String a, int b, int c) {
        return "int args"
    }
}