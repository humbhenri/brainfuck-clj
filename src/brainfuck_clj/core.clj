(ns brainfuck-clj.core)

(def cells-size 30000)

(def cells (atom (byte-array cells-size (byte 0))))

(def pointer (atom 0))

(def read-cell []
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

(defn start-loop
  "Jump past the matching ] if the cell under pointer is 0"
  []
  )

(defn end-loop
  "Jump back to the matching [ if the cell under the pointer is nonzero "
  [])

(defn write-ascii [])

(defn read-ascii [])

(defn bf
  "Run brainfuck program text passed as argument"
  [program]
  (let [statements (.replaceAll program "[^+-></[/].,]" "")]
    (doseq [c statements]
      (cond
       (= c \+) (inc-cell-value)
       (= c \-) (dec-cell-value)
       (= c \>) (move-pointer-right)
       (= c \<) (move-pointer-left)
       (= c \[) (start-loop)
       (= c \]) (end-loop)
       (= c \.) (write-ascii)
       (= c \,) (read-ascii)))))