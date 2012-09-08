/*
 * Copyright 2010-2012 the original author or authors.
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

package griffon.javafx.editors;

import griffon.core.resources.editors.AbstractPropertyEditor;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Andres Almiray
 */
public class ColorPropertyEditor extends AbstractPropertyEditor {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }

    public void setValue(Object value) {
        if (null == value) return;
        if (value instanceof CharSequence) {
            handleAsString(String.valueOf(value));
        } else if (value instanceof List) {
            handleAsList((List) value);
        } else if (value instanceof Map) {
            handleAsMap((Map) value);
        } else if (value instanceof Number) {
            handleAsNumber((Number) value);
        } else if (value instanceof Color) {
            super.setValue(value);
        } else {
            throw illegalValue(value, Color.class);
        }
    }

    private void handleAsString(String str) {
        try {
            super.setValue(Color.valueOf(str));
        } catch(IllegalArgumentException e) {
            throw illegalValue(str, Color.class, e);
        }
    }

    private void handleAsList(List list) {
        List values = new ArrayList();
        values.addAll(list);
        switch (list.size()) {
            case 3:
                values.add(1d);
                break;
            case 4:
                // ok
                break;
            default:
                throw illegalValue(list, Color.class);
        }
        for (int i = 0, valuesSize = values.size(); i < valuesSize; i++) {
            Object val = values.get(i);
            if (val instanceof Number) {
                values.set(i, parse((Number) val));
            } else if (val instanceof CharSequence) {
                values.set(i, parse(String.valueOf(val)));
            }
        }
        super.setValue(
                new Color(
                        (Double) values.get(0),
                        (Double) values.get(1),
                        (Double) values.get(2),
                        (Double) values.get(3)
                )
        );
    }

    private void handleAsMap(Map map) {
        double r = getMapValue(map, "red", 0d);
        double g = getMapValue(map, "green", 0d);
        double b = getMapValue(map, "blue", 0d);
        double a = getMapValue(map, "alpha", 1d);
        super.setValue(new Color(r, g, b, a));
    }

    private double parse(String val) {
        try {
            return (Integer.parseInt(String.valueOf(val).trim(), 16) & 0xFF)/255d;
        } catch (NumberFormatException e) {
            throw illegalValue(val, Color.class, e);
        }
    }

    private double parse(Number val) {
        return val.doubleValue();
    }

    private double getMapValue(Map map, String key, double defaultValue) {
        Object val = map.get(key);
        if (null == val) val = map.get(String.valueOf(key.charAt(0)));
        if (null == val) {
            return defaultValue;
        } else if (val instanceof CharSequence) {
            return parse(String.valueOf(val));
        } else if (val instanceof Number) {
            return parse((Number) val);
        }
        throw illegalValue(map, Color.class);
    }

    private void handleAsNumber(Number value) {
        double c = parse(value);
        super.setValue(new Color(c, c, c, 1d));
    }
}
