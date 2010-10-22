(ns jsasm.core
  (:require [clojure.string :as str]))

(def accumulator (StringBuilder.))
(defn write! [x] (.append accumulator x))
(defn clear! []  (.setLength accumulator 0))

(def *tab-width* 2)
(def depth (atom 0))
(defn reset-depth! [] (reset! depth 0))
(defn indent! []
  (swap! depth + *tab-width*))
(defn unindent! []
  (swap! depth - *tab-width*))

(defn CL [] (write! ":"))
(defn SC [] (write! ";"))
(defn NL [] (write! "\n"))
(defn SP [] (write! " "))
(defn OP [] (write! "("))
(defn CP [] (write! ")"))
(defn OB [] (write! "["))
(defn CB [] (write! "]"))
(defn OC [] (write! "{"))
(defn CC [] (write! "}"))
(defn CM [] (write! ","))
(defn TAB [] (dotimes [_ @depth] (SP)))

(defmacro in-brackets [& body]
  `(do (OB) (do ~@body) (CB)))
(defmacro in-parens [& body]
  `(do (OP) (do ~@body) (CP)))

(declare emit)

(defn separated-by [sep tokens]
  (when (seq tokens)
    (emit (first tokens))
    (doseq [token (rest tokens)] (write! sep) (emit token))))

(defn commas [tokens] (separated-by ", " tokens))
(defn emit-regex [s] (write! "/") (write! s) (write! "/"))
(defn emit-entry [k v] (NL) (TAB) (emit k) (CL) (SP) (emit v))
(defn comma-list [xs] (in-parens (commas xs)))

(defn emit-body [tokens]
  (if (empty? tokens)
    (write! "{;}")
    (do (OC)
        (indent!)
        (doseq [t tokens] (NL) (TAB) (emit t) (SC))
        (unindent!) (NL) (TAB) (CC))))

(defn if-token? [t] (= :IF (first t)))

(defn emit-if [test then else]
  (write! "if ")
  (in-parens (emit test))
  (SP)
  (emit-body then)
  (when else
    (write! " else ")
    (if (and (if-token? (first else))
             (= 1 (count else)))
      (emit (first else))
      (emit-body else))))

(defn emit-operator [opsym tokens]
  (case (count tokens)
    0 (throw (Exception. (str "empty arglist for operator " opsym)))
    1 (in-parens (write! opsym) (SP) (emit (first tokens)))
    (in-parens (separated-by opsym tokens))))

(defn emit-literal [x]
  (cond
   (nil? x)    (write! "null")
   (symbol? x) (write! (name x))
   (string? x) (write! (pr-str x))
   :else       (write! x)))

(defn emit [[tag a b c :as token]]
  (case tag
    :LIT      (emit-literal a)
    :REGEX    (do (write! "/") (emit a) (write! "/"))
    :ARRAY    (in-brackets (commas a))
    :ENTRY    (emit-entry a b)
    :OBJECT   (in-brackets (commas a))
    :PROJECT  (do (emit a) (in-brackets (emit b)))
    :CALL     (do (emit a) (comma-list b))
    :FUNCTION (in-parens
               (write! "function ")
               (comma-list a)
               (SP)
               (emit-body b))
    :WHILE    (do (write! "while ")
                  (in-parens (emit a))
                  (SP)
                  (emit-body b))
    :ENUM     (do (write! "for ")
                  (in-parens (emit a) (write! " in ") (emit b))
                  (SP)
                  (emit-body c))
    :IF       (emit-if a b c)
    :SET!     (do (emit a) (write! " = ") (emit b))
    :OPERATOR (in-parens (separated-by a b))
    :BREAK    (write! "break")
    :RETURN   (do (write! "return ") (emit a))
    :VAR      (do (write! "var ") (commas a))
    :NEW      (do (write! "new ") (emit a))))

(defn emit-tokens [tokens]
  (clear!)
  (reset-depth!)
  (doseq [t tokens] (emit t))
  (str accumulator))

(defn emit-tokens* [& tokens] (emit-tokens tokens))
