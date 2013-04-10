/*
 * Copyright 2009-2013 the original author or authors.
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

package griffon.javafx;

import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Default implementation of {@code WindowDisplayHandler} that simply makes the window
 * visible on show() and disposes it on hide().
 *
 * @author Andres Almiray
 */
public class DefaultWindowDisplayHandler implements WindowDisplayHandler {
    public void show(Window window, JavaFXApplication application) {
        if (window != null && window instanceof Stage) {
            ((Stage) window).show();
        }
    }

    public void hide(Window window, JavaFXApplication application) {
        if (window != null) {
            window.hide();
        }
    }
}