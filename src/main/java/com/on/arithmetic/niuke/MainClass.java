package com.on.arithmetic.niuke;


import java.util.Arrays;

/**
 * @author Nordlicht
 * 牛客往练习主函数
 */
public class MainClass {

    public static void main(String[] args) {
        MainClass mainClass = new MainClass();
        System.out.println(mainClass.Fibonacci(5));
        System.out.println(mainClass.JumpFloor(4));
        System.out.println(mainClass.Power(2, -3));
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        mainClass.reOrderArray(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 斐波那契数列
     *
     * @param n
     * @return
     */
    public int Fibonacci(int n) {
        if (n < 0) {
            return 0;
        } else if (n == 1 || n == 2) {
            return 1;
        } else {
            return Fibonacci(n - 2) + Fibonacci(n - 1);
        }
    }

    /**
     * 跳台阶问题和盒子覆盖问题
     *
     * @param target
     * @return
     */
    public int JumpFloor(int target) {
        if (target < 0) {
            return 0;
        } else if (target == 1) {
            return 1;
        } else if (target == 2) {
            return 2;
        } else {
            return JumpFloor(target - 2) + JumpFloor(target - 1);
        }
    }

    /**
     * 题目描述
     * 给定一个double类型的浮点数base和int类型的整数exponent。求base的exponent次方。
     * <p>
     * 保证base和exponent不同时为0
     * <p>
     * exponent可能存在负数，如果为负数，则需要用除法
     * 并且还需要注意边界问题
     *
     * @param base
     * @param exponent
     * @return
     */
    public double Power(double base, int exponent) {
        if (base == 0) {
            return 0.0;
        } else if (exponent == 0) {
            return 1;
        } else {
            double temp = base;
            if (exponent > 0) {
                for (int i = 0; i < exponent - 1; i++) {
                    base *= temp;
                }
            } else {
                for (int i = 0; i >= exponent; i--) {
                    base /= temp;
                }
            }
            return base;
        }
    }

    /**
     * 题目描述
     * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有的奇数位于数组的前半部分，
     * 所有的偶数位于数组的后半部分，并保证奇数和奇数，偶数和偶数之间的相对位置不变。
     *
     * @param array
     */
    public void reOrderArray(int[] array) {
        if (array.length == 0) {
            return;
        }
        int head = 0;
        int tail = array.length - 1;
        int tempHead = head;
        int tempTail = tail;
        int[] tempArr = new int[array.length];
        while (tempHead < tempTail) {
            if (array[head] % 2 != 0) {
                tempArr[tempHead] = array[head];
                tempHead++;
                head++;
            } else {
                head++;
            }
            if (array[tail] % 2 == 0) {
                tempArr[tempTail] = array[tail];
                tail--;
                tempTail--;
            } else {
                tail--;
            }
        }
        array = tempArr;
    }

}
