(ns brainfuck-clj.core-test
  (:use clojure.test
        brainfuck-clj.core
        midje.sweet))

(fact "should print brainfuck"
  (.trim (with-out-str (bf ">++++[>++++++<-]>-[[<+++++>>+<-]>-]<<[<]>>>>-
-.<<<-.>>>-.<.<.>---.<<+++.>>>++.<<---.[>]<<."))) => "brainfuck")

(fact "Print Hello World!"
  (.trim (with-out-str (bf "++++++++[>++++[>++>+++>+++>+<<<<-]>+>->+>>+[<]<-]>>.>
>---.+++++++..+++.>.<<-.>.+++.------.--------.>+.>++."))) => "Hello World!")