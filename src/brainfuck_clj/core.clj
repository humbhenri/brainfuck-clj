(ns brainfuck-clj.core)

(def cells-size 30000)

(def cells (atom (byte-array cells-size (byte 0))))

;;; data pointer
(def pointer (atom 0))

;;; program counter
(def pc (atom 0))

(defn forward []
  (swap! pc inc))

(defn read-cell []
  (aget @cells @pointer))

(defn inc-cell-value []
  (aset-byte @cells @pointer (inc (read-cell))))

(defn dec-cell-value []
  (aset-byte @cells @pointer (dec (read-cell))))

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

(defn start-loop
  "Jump past the matching ] if the cell under pointer is 0"
  [program]
  (let [matching (find-end-loop @pc program)]
    (when (cell-is-zero?)
      (reset! pc (inc matching)))))

(defn end-loop
  "Jump back to the matching [ if the cell under the pointer is nonzero "
  [program]
  (let [matching (find-start-loop @pc program)]
    (when-not (cell-is-zero?)
      (reset! pc matching))))

(defn write-ascii []
  (print (char (read-cell)))
  (flush))

(defn read-ascii [] ())

(defn bf
  "Run brainfuck program text passed as argument"
  [program]
  (reset! pc 0)
  (reset! pointer 0)
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