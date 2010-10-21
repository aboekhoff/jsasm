(ns jsasm.trial
  (:use (jsasm core ast)))

;;;; TODO
;;;; make these into some sort of test suite

(def array-1 (ARRAY [(LIT "foo") (LIT "bar")]))
(def entries-1 [(ENTRY (LIT "foo") (LIT 42))
                (ENTRY (LIT "bar") (LIT 99))])
(def object-1 (OBJECT entries-1))
(def projection-1 (PROJECT (LIT "foo") (LIT "bar")))
(def projection-2 (PROJECT projection-1 (LIT "baz")))
(def call-1 (CALL (LIT "zam")
                  [(LIT "wham")
                   (LIT 42)
                   (LIT "bam")
                   (LIT "blam!")]))
(def if-1 (IF (LIT "hmm")
              [(CALL (LIT 'alert) [(LIT 42)])]
              [(CALL (LIT 'alert) [(LIT 43)])]))

(def if-2 (IF (LIT "huh")
              [(CALL (LIT 'alert) [(LIT 99)])]
              if-1))

(def fn-1 (FUNCTION
           [(LIT 'wham) (LIT 'bam)]
           [(CALL (LIT 'alert)
                  [(OPERATOR '+ [(LIT 'wham) (LIT 'bam)])])]))

(def while-1 (WHILE
              (LIT "true")
              [(CALL (LIT 'alert) [(LIT "not finished!")])]))

(def fn-2 (FUNCTION [] [(CALL (LIT 'alert) [(LIT "wrapped")]) while-1]))
