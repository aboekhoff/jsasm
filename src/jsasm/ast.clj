(ns jsasm.ast)

(defmacro defnode [tag]
  `(defn ~tag [& vals#]
     (->> vals#
          (cons ~(keyword tag))
          vec)))

(defmacro defnodes [& tags]
  `(do ~@(for [t tags]
           `(defnode ~t))))

(defnodes
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
  FINALLY
  DELETE)



