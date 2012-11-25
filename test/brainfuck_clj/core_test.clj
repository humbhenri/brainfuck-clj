(ns brainfuck-clj.core-test
  (:use clojure.test
        brainfuck-clj.core
        midje.sweet))

(fact "should print brainfuck"
  (.trim (with-out-str (bf ">++++[>++++++<-]>-[[<+++++>>+<-]>-]<<[<]>>>>-
-.<<<-.>>>-.<.<.>---.<<+++.>>>++.<<---.[>]<<."))) => "brainfuck")

(fact "Hello world!"
  (.trim (with-out-str (bf ">+++++++++[<++++++++>-]<.>+++++++[<++++>-]<+.+++++++..+++.[-]>++++++++[<++++>-]
<.>+++++++++++[<++++++++>-]<-.--------.+++.------.--------.[-]>++++++++[<++++>-
]<+.[-]++++++++++."))) => "Hello world!")

(fact "Test [ and ]"
  (let [program ">++++[>++++++<-]>-[[<+++++>>+<-]>-]<<[<]>>>>-
-.<<<-.>>>-.<.<.>---.<<+++.>>>++.<<---.[>]<<."]
    (find-end-loop 5 program) => 15
    (find-end-loop 18 program) => 34
    (find-end-loop 19 program) => 31
    (find-end-loop 37 program) => 39
    (find-end-loop 85 program) => 87
    (find-start-loop 15 program) => 5
    (find-start-loop 34 program) => 18
    (find-start-loop 31 program) => 19
    (find-start-loop 39 program) => 37
    (find-start-loop 87 program) => 85))