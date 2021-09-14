package com.example.myapplication;

public class Calculator {
    String expression;

    public Calculator(){}
    public Calculator(String expression){
        if (expression.charAt(0)=='-'){
            this.expression = "0-"+expression;
        }else {
            this.expression=expression;
        }

    }

    public String getResult() {
        //创建两个栈，数栈，一个符号栈
        ArrayStack numStack = new ArrayStack(20);
        ArrayStack operStack = new ArrayStack(20);
        //定义需要的相关变量
        int index = 0;
        double num1 = 0;
        double num2 = 0;
        double oper = 0;
        double res = 0;
        char ch = ' '; //将每次扫描得到char保存到ch
        String keepNum = ""; //用于拼接 多位数
        //开始while循环的扫描expression
        while(true) {
            //依次得到expression 的每一个字符
            ch = expression.substring(index, index+1).charAt(0);
            //判断ch是什么，然后做相应的处理
            if(operStack.isOper(ch)) {//如果是运算符
                //判断当前的符号栈是否为空
                if(!operStack.isEmpty()) {
                    //如果符号栈有操作符，就进行比较,如果当前的操作符的优先级小于或者等于栈中的操作符,就需要从数栈中pop出两个数,
                    //这里需要判断是否此时的栈顶是否为左括号，如果是左括号不进入此循环
                    //我们设定的左括号是优先级大于加减乘除，所以当发现下一个进栈的符号的优先级比此时的栈顶的左括号优先级小的时候，
                    //应该让符号直接进栈，不进行弹出左符号的运算（左括号弹出来运算是不行的）
                    if(operStack.priority(ch) <= operStack.priority(operStack.peek()) & operStack.peek()!=40) {
                        num1 = numStack.pop();
                        num2 = numStack.pop();
                        oper = operStack.pop();
                        res = numStack.cal(num1, num2, oper);
                        //把运算的结果如数栈
                        numStack.push(res);
                        //然后将当前的操作符入符号栈
                        operStack.push(ch);
                        /**
                         * 进行右括号的判断。匹配左括号
                         * 当发现进入的是右括号时就优先进行括号内的计算
                         */
                    } else if(ch==41){
                        //先让右括号进栈
                        operStack.push(ch);
                        if (ch==41) {
                            //再把右括号弹出
                            double oper1 = operStack.pop();
                            //弹出右括号后开始进行括号内运算
                            while(true) {
                                //右括号
                                num1 = numStack.pop();
                                num2 = numStack.pop();
                                oper = operStack.pop();
                                res = numStack.cal(num1, num2, oper);
                                //把运算的结果如数栈
                                numStack.push(res);
                                //当运算到栈顶符号为左括号时候，就弹出栈顶元素左括号，结束循环
                                if(operStack.peek()==40) {
                                    double oper2 = operStack.pop();
                                    break;
                                }
                            }

                        }

                        //如果当前的操作符的优先级大于栈中的操作符， 就直接入符号栈.
                    }
                    else {
                        operStack.push(ch);
                    }
                }else {
                    //如果为空直接入符号栈
                    operStack.push(ch);
                }
            } else { //如果是数，则直接入数栈

                //分析思路
                //1. 当处理多位数时，不能发现是一个数就立即入栈，因为他可能是多位数
                //2. 在处理数，需要向expression的表达式的index 后再看一位,如果是数就进行扫描，如果是符号才入栈
                //3. 因此我们需要定义一个变量 字符串，用于拼接

                //处理多位数
                keepNum += ch;

                //如果ch已经是expression的最后一位，就直接入栈
                if (index == expression.length() - 1) {
                    numStack.push(Double.parseDouble(keepNum));
                }else{

                    //判断下一个字符是不是数字，如果是数字，就继续扫描，如果是运算符，则入栈
                    //注意是看后一位，不是index++
                    if (operStack.isOper(expression.substring(index+1,index+2).charAt(0))) {
                        //如果后一位是运算符，则入栈 keepNum = "1" 或者 "123"
                        numStack.push(Double.parseDouble(keepNum));
                        //重要的!!!!!!, keepNum清空
                        keepNum = "";

                    }
                }
            }
            //让index + 1, 并判断是否扫描到expression最后.
            index++;
            if (index >= expression.length()) {
                break;
            }
        }

        //当表达式扫描完毕，就顺序的从 数栈和符号栈中pop出相应的数和符号，并运行.
        while(true) {
            if(operStack.isEmpty()) {
                break;
            }
            num1 = numStack.pop();
            num2 = numStack.pop();
            oper = operStack.pop();
            res = numStack.cal(num1, num2, oper);
            numStack.push(res);//入栈

        }
        //将数栈的最后数，pop出，就是结果
        double res2 = numStack.pop();
        return ""+res2;
    }


}