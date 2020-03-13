package java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Valid-Params<p>
 *
 * (def valid-params?
 *   (x y)
 *     (and (type x) (type y)))
 *
 * (de function
 *   (x y)
 *     (cond ((not (valid-params? x y)) "Comments!!!")
 *     ((or (< x 0) (< y 0)) (function (x) (y)))
 *     ((< x y) (function y x))
 *     ((= y 0) x)
 *     ((! y))
 *     (t (function y (x y)))))
 *  (throws)
 */

public class ValidParams 
{
    private static Logger ValidLog = LoggerFactory.getLogger(ValidParams.class);

    public static void IsTrue(boolean expression, String message, boolean _throws) {
        if (! expression) {
            if (_throws) {
                ValidLog.error("expression isTure => False");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static <T> void IsNull(T expression, String message, boolean _throws) {
        if (expression == null) {
            if (_throws) {
                ValidLog.error("expression isNull => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static <T> boolean IsNull(T expression) {
        ValidLog.info("expression IsNull => ", expression);
        return (boolean)expression;
    }

    public static void IsEmpty(boolean expression, String message) {
        if (! expression) {
            ValidLog.info("expression IsEmpty => False");
        } else
            ValidLog.info(message);
    }

    public static void IsEmpty(String expression, String message, boolean _throws) {
        if (expression.equals("")) {
            if (_throws) {
                ValidLog.error("expression IsEmpty => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static boolean IsEmpty(String expression) {
        ValidLog.info("expression IsEmpty => ", expression.equals(""));
        return expression.equals("");
    }

    public static <T> void IsEqual(T expression_1, T expression_2, String message, boolean _throws) {
        if (expression_1 != expression_2) {
            if (_throws) {
                ValidLog.error("expression_1 IsEqual expression_2 => False");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static <T extends Comparable<? super T> > void IsLess(T expression, String message, boolean _throws) {
        if ((Integer)expression < 0) {
            if (_throws) {
                ValidLog.error("expression IsLess => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static <T> void IsLessEqual(T expression, String message, boolean _throws) {
        if ((Integer)expression <= 0) {
            if (_throws) {
                ValidLog.error("expression IsLessEqual => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static void IsLess(int expression, String message, boolean _throws) {
        if (expression < 0) {
            if (_throws) {
                ValidLog.error("expression IsLess => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static void IsLessEqual(int expression, String message, boolean _throws) {
        if (expression <= 0) {
            if (_throws) {
                ValidLog.error("expression IsLessEqual => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static void IsLess(long expression, String message, boolean _throws) {
        if (expression < 0) {
            if (_throws) {
                ValidLog.error("expression IsLess => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static void IsLessEqual(long expression, String message, boolean _throws) {
        if (expression <= 0) {
            if (_throws) {
                ValidLog.error("expression IsLessEqual => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static <T> void IsGreater(T expression, T greater, String message, boolean _throws) {
        if ((Integer)expression > (Integer)greater) {
            if (_throws) {
                ValidLog.error("expression IsGreater greater => Ture");
                throw new IllegalArgumentException(message);
            } else
                ValidLog.info(message);
        }
    }

    public static boolean isBytesEqual(byte[] _0, byte[] _1, String message) {
        boolean request = Arrays.equals(_0, _1);

        if (! request) {
            ValidLog.error("_0 isBytesEqual _1 => False");
        } else
            ValidLog.info(message);

        return request;
    }

    public static void Printof(String message) {
        ValidLog.info(message);
    }
}