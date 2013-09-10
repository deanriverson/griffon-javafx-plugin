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

import javafx.stage.Window;

/**
 * Controls how windows are shown and hidden at runtime.
 *
 * @author Andres Almiray
 */
public interface WindowDisplayHandler {
    /**
     * Callback for displaying a window.
     *
     * @param window      the window to be displayed
     * @param application the current application
     */
    void show(Window window, JavaFXGriffonApplication application);

    /**
     * Callback for hiding a window.
     *
     * @param window      the window to hide
     * @param application the current application
     */
    void hide(Window window, JavaFXGriffonApplication application);
}