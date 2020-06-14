package org.NauhWuun.jio.kernel;

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
    public static <T> boolean IsBoolean(T expression, String message) {
        if (! (Boolean) expression) {
            throw new IllegalArgumentException(message);
        }

        return  (Boolean) expression;
    }

    public static <T> boolean IsNull(T expression, String message) {
        if (null == expression) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static boolean IsEmpty(String expression, String message) {
        if (expression.equals("")) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static <T> boolean IsNotEqual(T expression_1, T expression_2) {
        return (expression_1 != expression_2);
    }

    public static <T extends Comparable<? super T> > boolean IsLess(T expression, String message) {
        if ((Integer) expression < 0) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static <T> boolean IsLessEqual(T expression, String message) {
        if ((Integer)expression <= 0) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static boolean IsLess(int expression, String message) {
        if (expression < 0) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static boolean IsLessEqual(int expression, String message) {
        if (expression <= 0) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static boolean IsLess(long expression, String message) {
        if (expression < 0) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static boolean IsLessEqual(long expression, String message) {
        if (expression <= 0) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static <T> boolean IsGreater(T expression, T greater, String message) {
        if ((Integer) expression > (Integer) greater) {
            throw new IllegalArgumentException(message);
        }

        return true;
    }

    public static boolean isBytesEqual(byte[] _0, byte[] _1) {
        return Arrays.equals(_0, _1);
    }

    public static void Print(String... message) {
        System.out.println(message);
    }
}