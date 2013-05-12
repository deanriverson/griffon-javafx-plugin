/*
 * Copyright 2010-2013 the original author or authors.
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

package griffon.javafx.formatters;

import griffon.core.resources.formatters.AbstractFormatter;
import griffon.core.resources.formatters.ParseException;
import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.util.Arrays;

import static griffon.util.GriffonNameUtils.isBlank;
import static java.lang.Integer.toHexString;
import static org.codehaus.groovy.runtime.StringGroovyMethods.padLeft;

/**
 * @author Andres Almiray
 * @since 1.3.0
 */
public class ColorFormatter extends AbstractFormatter<Color> {
    public static final String PATTERN_SHORT = "#RGB";
    public static final String PATTERN_SHORT_WITH_ALPHA = "#RGBA";
    public static final String PATTERN_LONG = "#RRGGBB";
    public static final String PATTERN_LONG_WITH_ALPHA = "#RRGGBBAA";
    public static final String DEFAULT_PATTERN = PATTERN_LONG;

    private static final String[] PATTERNS = new String[]{
        PATTERN_LONG,
        PATTERN_LONG_WITH_ALPHA,
        PATTERN_SHORT,
        PATTERN_SHORT_WITH_ALPHA
    };

    public static final ColorFormatter SHORT = new ColorFormatter(PATTERN_SHORT);
    public static final ColorFormatter SHORT_WITH_ALPHA = new ColorFormatter(PATTERN_SHORT_WITH_ALPHA);
    public static final ColorFormatter LONG = new ColorFormatter(PATTERN_LONG);
    public static final ColorFormatter LONG_WITH_ALPHA = new ColorFormatter(PATTERN_LONG_WITH_ALPHA);

    public static ColorFormatter getInstance(String pattern) {
        return new ColorFormatter(pattern);
    }

    private final ColorFormatterDelegate delegate;

    public ColorFormatter() {
        this(DEFAULT_PATTERN);
    }

    protected ColorFormatter(String pattern) {
        if (PATTERN_SHORT.equals(pattern)) {
            delegate = new ShortColorFormatterDelegate();
        } else if (PATTERN_SHORT_WITH_ALPHA.equals(pattern)) {
            delegate = new ShortWithAlphaColorFormatterDelegate();
        } else if (PATTERN_LONG.equals(pattern)) {
            delegate = new LongColorFormatterDelegate();
        } else if (PATTERN_LONG_WITH_ALPHA.equals(pattern)) {
            delegate = new LongWithAlphaColorFormatterDelegate();
        } else if (isBlank(pattern)) {
            delegate = new LongColorFormatterDelegate();
        } else {
            throw new IllegalArgumentException("Invalid pattern '" + pattern + "'. Valid patterns are " + Arrays.toString(PATTERNS));
        }
    }

    public String format(Color color) {
        return delegate.format(color);
    }

    public Color parse(String str) throws ParseException {
        return delegate.parse(str);
    }

    public static Color parseColor(String str) throws ParseException {
        if (str.startsWith("#")) {
            switch (str.length()) {
                case 4:
                    return (Color) SHORT.parse(str);
                case 5:
                    return (Color) SHORT_WITH_ALPHA.parse(str);
                case 7:
                    return (Color) LONG.parse(str);
                case 9:
                    return (Color) LONG_WITH_ALPHA.parse(str);
                default:
                    throw parseError(str, Color.class);
            }
        } else {
            // assume it's a Color constant
            try {
                String colorFieldName = str.toUpperCase();
                Field field = Color.class.getField(colorFieldName);
                return (Color) field.get(null);
            } catch (Exception e) {
                throw parseError(str, Color.class, e);
            }
        }
    }

    private static interface ColorFormatterDelegate {
        String getPattern();

        String format(Color color);

        Color parse(String str) throws ParseException;
    }

    private static abstract class AbstractColorFormatterDelegate implements ColorFormatterDelegate {
        private final String pattern;

        private AbstractColorFormatterDelegate(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }

    private static int red(Color color) {
        return toIntColor(color.getRed());
    }

    private static int green(Color color) {
        return toIntColor(color.getGreen());
    }

    private static int blue(Color color) {
        return toIntColor(color.getBlue());
    }

    private static int alpha(Color color) {
        return toIntColor(color.getOpacity());
    }

    private static int toIntColor(double c) {
        return new Double(c * 255).intValue();
    }

    private static class ShortColorFormatterDelegate extends AbstractColorFormatterDelegate {
        private ShortColorFormatterDelegate() {
            super(PATTERN_SHORT);
        }

        public String format(Color color) {
            if (color == null) {
                throw new IllegalArgumentException("Cannot format given Color because it's null");
            }

            return new StringBuilder("#")
                .append(toHexString(red(color)).charAt(0))
                .append(toHexString(green(color)).charAt(0))
                .append(toHexString(blue(color)).charAt(0))
                .toString();
        }

        public Color parse(String str) throws ParseException {
            if (isBlank(str) || !str.startsWith("#") || str.length() != 4) {
                throw parseError(str, Color.class);
            }

            int r = parseHexInt(new StringBuilder()
                .append(str.charAt(1))
                .append(str.charAt(1))
                .toString().toUpperCase(), Color.class);
            int g = parseHexInt(new StringBuilder()
                .append(str.charAt(2))
                .append(str.charAt(2))
                .toString().toUpperCase(), Color.class);
            int b = parseHexInt(new StringBuilder()
                .append(str.charAt(3))
                .append(str.charAt(3))
                .toString().toUpperCase(), Color.class);

            return Color.rgb(r, g, b);
        }
    }

    private static class ShortWithAlphaColorFormatterDelegate extends AbstractColorFormatterDelegate {
        private ShortWithAlphaColorFormatterDelegate() {
            super(PATTERN_SHORT_WITH_ALPHA);
        }

        public String format(Color color) {
            if (color == null) {
                throw new IllegalArgumentException("Cannot format given Color because it's null");
            }

            return new StringBuilder("#")
                .append(toHexString(red(color)).charAt(0))
                .append(toHexString(green(color)).charAt(0))
                .append(toHexString(blue(color)).charAt(0))
                .append(toHexString(alpha(color)).charAt(0))
                .toString();
        }

        public Color parse(String str) throws ParseException {
            if (isBlank(str)) return null;
            if (!str.startsWith("#") || str.length() != 5) {
                throw parseError(str, Color.class);
            }

            int r = parseHexInt(new StringBuilder()
                .append(str.charAt(1))
                .append(str.charAt(1))
                .toString().toUpperCase(), Color.class);
            int g = parseHexInt(new StringBuilder()
                .append(str.charAt(2))
                .append(str.charAt(2))
                .toString().toUpperCase(), Color.class);
            int b = parseHexInt(new StringBuilder()
                .append(str.charAt(3))
                .append(str.charAt(3))
                .toString().toUpperCase(), Color.class);
            int a = parseHexInt(new StringBuilder()
                .append(str.charAt(4))
                .append(str.charAt(4))
                .toString().toUpperCase(), Color.class);

            return Color.rgb(r, g, b, a / 255d);
        }
    }

    private static class LongColorFormatterDelegate extends AbstractColorFormatterDelegate {
        private LongColorFormatterDelegate() {
            super(PATTERN_LONG);
        }

        public String format(Color color) {
            if (color == null) {
                throw new IllegalArgumentException("Cannot format given Color because it's null");
            }

            return new StringBuilder("#")
                .append(padLeft(toHexString(red(color)), 2, "0"))
                .append(padLeft(toHexString(green(color)), 2, "0"))
                .append(padLeft(toHexString(blue(color)), 2, "0"))
                .toString();
        }

        public Color parse(String str) throws ParseException {
            if (isBlank(str)) return null;
            if (!str.startsWith("#") || str.length() != 7) {
                throw parseError(str, Color.class);
            }

            int r = parseHexInt(new StringBuilder()
                .append(str.charAt(1))
                .append(str.charAt(2))
                .toString().toUpperCase(), Color.class);
            int g = parseHexInt(new StringBuilder()
                .append(str.charAt(3))
                .append(str.charAt(4))
                .toString().toUpperCase(), Color.class);
            int b = parseHexInt(new StringBuilder()
                .append(str.charAt(5))
                .append(str.charAt(6))
                .toString().toUpperCase(), Color.class);

            return Color.rgb(r, g, b);
        }
    }

    private static class LongWithAlphaColorFormatterDelegate extends AbstractColorFormatterDelegate {
        private LongWithAlphaColorFormatterDelegate() {
            super(PATTERN_LONG_WITH_ALPHA);
        }

        public String format(Color color) {
            if (color == null) {
                throw new IllegalArgumentException("Cannot format given Color because it's null");
            }

            return new StringBuilder("#")
                .append(padLeft(toHexString(red(color)), 2, "0"))
                .append(padLeft(toHexString(green(color)), 2, "0"))
                .append(padLeft(toHexString(blue(color)), 2, "0"))
                .append(padLeft(toHexString(alpha(color)), 2, "0"))
                .toString();
        }

        public Color parse(String str) throws ParseException {
            if (isBlank(str)) return null;
            if (!str.startsWith("#") || str.length() != 9) {
                throw parseError(str, Color.class);
            }

            int r = parseHexInt(new StringBuilder()
                .append(str.charAt(1))
                .append(str.charAt(2))
                .toString().toUpperCase(), Color.class);
            int g = parseHexInt(new StringBuilder()
                .append(str.charAt(3))
                .append(str.charAt(4))
                .toString().toUpperCase(), Color.class);
            int b = parseHexInt(new StringBuilder()
                .append(str.charAt(5))
                .append(str.charAt(6))
                .toString().toUpperCase(), Color.class);
            int a = parseHexInt(new StringBuilder()
                .append(str.charAt(7))
                .append(str.charAt(8))
                .toString().toUpperCase(), Color.class);

            return Color.rgb(r, g, b, a / 255d);
        }
    }
}
