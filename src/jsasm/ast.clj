(ns jsasm.ast)

(defmacro deftoken [tag]
  `(defn ~tag [& vals#]
     (->> vals#
          (cons ~(keyword tag))
          vec)))

(defmacro deftokens [& tags]
  `(do ~@(for [t tags]
           `(defnode ~t))))

(deftokens
  LIT
  REGEX
  ARRAY
  OBJECT
  ENTRY
  FUNCTION
  PROJECT
  CALL
  IF
  WHILE
  OPERATOR
  BREAK
  RETURN
  VAR
  NEW
  TRY
  THROW
  CATCH
  FINALLY)



