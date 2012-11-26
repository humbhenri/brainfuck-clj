(ns brainfuck-clj.core
  (:gen-class))

(def cells-size 30000)

(def cells (atom (short-array cells-size (short 0))))

(defn reset-cells! [] (reset! cells (short-array cells-size (short 0))))

;;; data pointer
(def pointer (atom 0))

;;; program counter
(def pc (atom 0))

(defn forward []
  (swap! pc inc))

(defn read-cell []
  (aget @cells @pointer))

(defn write-cell [^Short value]
  (aset-short @cells @pointer value))

(defn write-ascii []
  (print (char (read-cell))))

(defn read-ascii []
  (write-cell (Short/valueOf (read-line))))

(defn inc-cell-value []
  (write-cell (inc (read-cell))))

(defn dec-cell-value []
  (write-cell (dec (read-cell))))

(defn move-pointer-right []
  (when (< @pointer cells-size)
    (swap! pointer inc)))

(defn move-pointer-left []
  (when (> @pointer 0)
    (swap! pointer dec)))

(defn cell-is-zero? []
  (= (read-cell) 0))

(defn find-end-loop
  "Find position of matching ] assuming the char under pos is ["
  [pos program]
  (loop [stack (list \[)
         counter (inc pos)]
    (if-not (empty? stack)
      (recur (case (.charAt program counter)
               \[ (conj stack \[)
               \] (pop stack)
               stack)
             (inc counter))
      (dec counter))))

(def find-end-loop-memoized (memoize find-end-loop))

(defn find-start-loop
  "Find position of matching [ assuming the char under pos is ]"
  [pos program]
  (loop [stack (list \])
         counter (dec pos)]
    (if-not (empty? stack)
      (recur (case (.charAt program counter)
               \] (conj stack \[)
               \[ (pop stack)
               stack)
             (dec counter))
      (inc counter))))

(def find-start-loop-memoized (memoize find-start-loop))

(defn start-loop
  "Jump past the matching ] if the cell under pointer is 0"
  [program]
  (let [matching (find-end-loop-memoized @pc program)]
    (when (cell-is-zero?)
      (reset! pc (inc matching)))))

(defn end-loop
  "Jump back to the matching [ if the cell under the pointer is nonzero "
  [program]
  (let [matching (find-start-loop-memoized @pc program)]
    (when-not (cell-is-zero?)
      (reset! pc matching))))

(defn bf
  "Run brainfuck program text passed as argument"
  [program]
  (reset! pc 0)
  (reset! pointer 0)
  (reset-cells!)
  (let [statements (.replaceAll program "[^+-><\\[\\].,]" "")
        end (dec (.length statements))]
    ;; (add-watch pc "pc" (fn [k r o n]
    ;;                      (println o "->" n end statements)))

    (loop [c (.charAt statements @pc)]
      (case c
        \+ (inc-cell-value)
        \- (dec-cell-value)
        \> (move-pointer-right)
        \< (move-pointer-left)
        \[ (start-loop statements)
        \] (end-loop statements)
        \. (write-ascii)
        \, (read-ascii))
      (when (< @pc end)
        (forward)
        (recur (.charAt statements @pc))))))

(def user-message "Welcome to Brainfuck Interpreter (R) 2012\n Enter a program and press enter, exit or quit
to leave.")

(defn -main [& args]
  (println user-message)
  (loop [input (.trim (read-line))]
    (when (or (not= input "exit") (not= input "quit"))
      (bf input)
      (println)
      (recur (.trim (read-line))))))
