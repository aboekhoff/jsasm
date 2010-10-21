(ns jsasm.core
  (:require [clojure.string :as str]))

(def accumulator (StringBuilder.))
(defn write! [x] (.append accumulator x))
(defn clear! []  (.setLength accumulator 0))

(def *tab-width* 4)
(def depth (atom 0))
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

(defn in-brackets* [action] (OB) (action) (CB))
(defn in-parens* [action] (OP) (action) (CP))

(defmacro in-brackets [& body]
  `(do (OC) (indent!) (do ~@body) (unindent!) (TAB) (CC)))
(defmacro in-brackets [& body]
  `(do (OB) (do ~@body) (CB)))
(defmacro in-parens [& body]
  `(do (OP) (do ~@body) (CP)))

(defn separated-by [sep tokens]
  (when (seq tokens)
    (emit! (first tokens))
    (doseq [token (rest tokens)] (write! sep) (emit! token))))

(defn commas [tokens] (separated-by ", " tokens))
(defn emit-regex [s] (write! "/") (write! s) (write! "/"))
(defn emit-entry [k v] (NL) (TAB) (emit! k) (CL) (SP) (emit! v))
(defn comma-list [xs] (in-parens (commas xs)))

(defn emit-body [tokens]
  (OC) (indent!)
  (doseq [t tokens]
    (NL) (TAB) (emit! t) (SC))
  (unindent!) (NL) (TAB) (CC))

(defn if-token? [t] (= :IF (first t)))

(defn emit-if [test then else]
  (write! "if ")
  (in-parens (emit! test))
  (SP)
  (emit-body then)
  (when else
    (write! " else ")
    (if (if-token? else)
      (emit! else)
      (emit-body else))))

(defn emit-operator [opsym tokens]
  (case (count tokens)
    0 (throw (Exception. (str "empty arglist for operator " opsym)))
    1 (in-parens (write! opsym) (SP) (emit! (first tokens)))
    (in-parens (separated-by opsym tokens))))

(defn emit! [[tag a b c :as token]]
  (case tag
    :IDENT    (write! a)
    :STRING   (write! (pr-str a))
    :NUMBER   (write! a)
    :REGEX    (regex-string a)
    :ARRAY    (in-brackets (separated-by "," a))
    :ENTRY    (emit-entry a b)
    :OBJECT   (in-braces (separated-by "," a))
    :PROJECT  (do (emit! a) (in-brackets (emit! b)))
    :CALL     (do (emit! a) (comma-list b))
    :FUNCTION (in-parens (write! "function ")
                         (comma-list a)
                         (SP)
                         (emit-body b))
    :WHILE    (do (write! "while ")
                  (in-parens (emit! a))
                  (SP)
                  (emit-body b))
    :ENUM     (do (write! "for ")
                  (in-parens (emit! a) (write! " in ") (emit! b))
                  (SP)
                  (emit-body c))
    :IF       (emit-if a b c)
    :OPERATOR (in-parens (separated-by a b))))

(defn emit-tokens [tokens]
  (clear!)
  (doseq [t tokens] (emit! t))
  (str accumulator))

(defn emit-tokens* [& tokens] (emit-tokens tokens))
