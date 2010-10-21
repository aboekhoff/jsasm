(ns jsasm.trial
  (:use (jsasm core ast)))

(def array-1 (ARRAY [(IDENT "foo") (IDENT "bar")]))
(def entries-1 [(ENTRY (STRING "foo") (NUMBER 42))
                (ENTRY (IDENT "bar") (NUMBER 99))])
(def object-1 (OBJECT entries-1))
(def projection-1 (PROJECT (IDENT "foo") (IDENT "bar")))
(def projection-2 (PROJECT projection-1 (IDENT "baz")))
(def call-1 (CALL (IDENT "zam")
                  [(IDENT "wham")
                   (NUMBER 42)
                   (IDENT "bam")
                   (STRING "blam!")]))
(def if-1 (IF (IDENT "hmm")
              [(CALL (IDENT "alert") [(NUMBER 42)])]
              [(CALL (IDENT "alert") [(NUMBER 43)])]))

(def if-2 (IF (IDENT "huh")
              [(CALL (IDENT "alert") [(NUMBER 99)])]
              if-1))

(def fn-1 (FUNCTION
           [(IDENT "wham") (IDENT "bam")]
           [(CALL (IDENT "alert")
                  [(OPERATOR '+ [(IDENT "wham") (IDENT "bam")])])]))

(def while-1 (WHILE
              (IDENT "true")
              [(CALL (IDENT "alert") [(STRING "not finished!")])]))

(def fn-2 (FUNCTION [] [(CALL (IDENT "alert") [(IDENT "wrapped")]) while-1]))
